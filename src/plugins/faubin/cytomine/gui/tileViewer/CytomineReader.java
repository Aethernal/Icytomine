package plugins.faubin.cytomine.gui.tileViewer;

import icy.main.Icy;
import icy.painter.Overlay;
import icy.roi.ROI;
import icy.roi.ROI2D;
import icy.roi.ROIEvent;
import icy.roi.ROIEvent.ROIEventType;
import icy.roi.ROIListener;
import icy.sequence.Sequence;
import icy.sequence.SequenceEvent;
import icy.sequence.SequenceEvent.SequenceEventSourceType;
import icy.sequence.SequenceEvent.SequenceEventType;
import icy.sequence.SequenceListener;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

import javax.swing.JPanel;
import javax.swing.Timer;

import plugins.faubin.cytomine.AnnotationTerm;
import plugins.faubin.cytomine.IcytomineUtil;
import plugins.faubin.cytomine.gui.mvc.view.frame.ProcessingFrame;
import plugins.faubin.cytomine.gui.roi.roi2dpolygon.CytomineImportedROI;
import plugins.faubin.cytomine.gui.tileViewer.roiconfiguration.RoiConfigurationPanel;
import plugins.kernel.roi.roi2d.ROI2DPolygon;
import be.cytomine.client.Cytomine;
import be.cytomine.client.CytomineException;
import be.cytomine.client.collections.AnnotationCollection;
import be.cytomine.client.models.Annotation;
import be.cytomine.client.models.ImageInstance;

/*
 * this panel is an interface to view high resolution images from Cytomine by loading them tiles by tiles and writing them to 
 * a BufferedImage.
 * Tiles are loaded depending on the virtual position of the viewer on the image and on the zoom level
 */
@SuppressWarnings("serial")
public class CytomineReader extends JPanel {

	Cytomine cytomine;
	public WholeSlide image;

	// tiles
	ThreadUrl threads[];
	Stack<Tile> queue;
	Stack<Tile> out_queue[];

	// crop
	Point2D.Double window_position;
	Dimension window_size;
	int overlap;

	// zoom
	int zoom;
	int oldZoom;
	boolean reload;
	
	// mouse mouvement
	protected boolean mouseGrabbed;
	protected double oldMouseX;
	protected double oldMouseY;
	Point2D.Double view_position;

	// sequence
	ImageInstance instance;
	Sequence sequence;
	Overlay overlay;
	boolean locked;
	boolean listenerActive;

	@SuppressWarnings({ "unchecked" })
	public CytomineReader(Cytomine cytomine, ImageInstance instance,
			Dimension dim, boolean listenerActive) throws CytomineException {
		this.overlap = 0;
		this.cytomine = cytomine;
		this.instance = instance;

		// creating wholeSlide object
		long abstractID = instance.getLong("baseImage");
		WholeSlide slide = new WholeSlide(cytomine, abstractID, 0);
		this.image = slide;

		this.zoom = image.depth - 4;
		oldZoom = zoom;

		// panel configuration
		setSize(dim);
		setBackground(Color.WHITE);
		setOpaque(true);

		// Listeners
		addMouseListener(mouseListener);
		addMouseWheelListener(wheelListener);

		// thread pool
		threads = new ThreadUrl[8];

		// queue to stock queries
		queue = new Stack<Tile>();
		out_queue = new Stack[image.depth];
		for (int i = 0; i < out_queue.length; i++) {
			out_queue[i] = new Stack<Tile>();
		}

		// view position
		view_position = new Point2D.Double(-getWidth() / 2, -getHeight() / 2);
		window_position = new Point2D.Double();
		window_size = getBounds().getSize();

		// sequence initialisation
		sequence = new Sequence();
		sequence.setName("" + instance.getLong("id") + " - Dynamic view");

		sequence.addListener(sequenceListener);
		
		overlay = new OverlayCytomine(this);
		locked = true;
		sequence.addOverlay(overlay);
		this.listenerActive = listenerActive;
		
		// repaint loop every 16 ms
		Timer repaint = new Timer(16, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				cleanOutsidTiles();
				
				// sequence load
				BufferedImage image = new BufferedImage(getWidth(),
						getHeight(), BufferedImage.TYPE_INT_RGB);
				image.createGraphics().setColor(Color.white);
				image.createGraphics().fillRect(0, 0, getWidth(), getHeight());
				paintComponent(image.createGraphics());
				sequence.setImage(0, 0, image);

			}

		});

		repaint.setRepeats(true);
		repaint.start();
		
	}
	
	public void cleanOutsidTiles(){
		int[] colsANDrows = getColsAndRows(window_position, window_size, zoom);

		int cols = colsANDrows[0];
		int rows = colsANDrows[1];
		int col0 = colsANDrows[2];
		int row0 = colsANDrows[3];
		
		//clean out_queue from outside tiles
		int count=0;
		for (int i = 0; i < out_queue[zoom].size(); i++) {
			Tile tile = out_queue[zoom].get(i);
			
			int col = tile.getC();
			int row = tile.getR();
			
			if(col < col0 || col > col0+cols || row < row0 || row > row0+rows){
				
				count++;
				out_queue[zoom].remove(i);
				i--;
					
			}
			
		}
		
	}

	public Point2D.Double getCenter() {
		Point2D.Double center = new Point2D.Double();

		center.setLocation(getWidth() / 2, getHeight() / 2);

		return center;
	}

	public void read_window() {
		window_size.width = (int) (window_size.width * Math
				.pow(2, zoom));
		window_size.height = (int) (window_size.height * Math.pow(2,
				zoom));
		window_position.setLocation( (window_position.getX() * Math.pow(2, zoom)), (window_position.getY() * Math.pow(2, zoom)) );
	}

	// print the whole picture
	public void readAll() throws CytomineException {
		window_position.setLocation(1, 1);
		resetQueues();
		read();
		do {
			read();
		} while (next());

	}

	// print picture from current position
	public void read() throws CytomineException {
		new Thread() {
			public void run() {

				// update size
				Dimension viewerSize = Icy.getMainInterface().getViewers(sequence).get(0).getBounds().getSize();
				if(!getSize().equals(viewerSize)){
					
					Point2D.Double dist = new Point2D.Double(viewerSize.getWidth()-getWidth(),viewerSize.getHeight()-getHeight());
					
					setSize(viewerSize);
					window_size.setSize(getSize());
					
					moveROI(dist.getX()/2, dist.getY()/2);
				}
				
				int[] colsANDrows = getColsAndRows(
						window_position,
						window_size, zoom);

				int cols = colsANDrows[0];
				int rows = colsANDrows[1];
				int col0 = colsANDrows[2];
				int row0 = colsANDrows[3];

				//put needed tiles to input queue
				
				for (int r = 0; r < rows; r++) {
					for (int c = 0; c < cols; c++) {
						int row = row0 + r;
						int col = col0 + c;

						Tile tile;
						try {
							tile = generateTile(row, col, zoom);
							queue.push(tile);
						} catch (CytomineException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}

				for (int i = 0; i < threads.length; i++) {
					threads[i] = new ThreadUrl(queue, out_queue, zoom, cytomine);
					threads[i].start();
				}
			}
		}.start();

	}

	public int[] getColsAndRows(Point2D position, Dimension size, int zoom) {
		// avoid asking out of range
		if ((position.getX() + size.width) > image.levels[zoom].get("level_width")) {
			position.setLocation(image.levels[zoom].get("level_width") - size.width , position.getY() );
		}
		if ((position.getY() + size.height) > image.levels[zoom].get("level_height")) {
			position.setLocation(position.getX(), image.levels[zoom].get("level_height") - size.height );
		}
		if (position.getX() < 0) {
			position.setLocation(0, position.getY());
		}
		if (position.getY() < 0) {
			position.setLocation(position.getX(), 0);
		}

		// how many tiles will be loaded
		int row0 = (int) (Math.floor(position.getY() / image.tile_size));
		int col0 = (int) (Math.floor(position.getX() / image.tile_size));
		int row1 = (int) (Math.floor(Math.min(
				image.levels[zoom].get("level_height"),
				(position.getY() + size.height) / image.tile_size)));
		int col1 = (int) (Math.floor(Math.min(
				image.levels[zoom].get("level_width"),
				(position.getX() + size.width) / image.tile_size)));
		int cols = col1 - col0 + 1;
		int rows = row1 - row0 + 1;

		return new int[] { cols, rows, col0, row0 };

	}

	public Tile generateTile(int row, int col, int zoom)
			throws CytomineException {
		int tile_group = ViewerTool.findTileGroup(image, zoom, col, row);

		String baseUrl = cytomine.getImageServersOfAbstractImage(image.image
				.getLong("id"));
		

		String url = baseUrl + "&tileGroup=" + tile_group + "&z="
				+ (image.depth - zoom) + "&x=" + col + "&y=" + row
				+ "&mimeType=openslide/" + image.mime + "";

		int x_paste = (int) ((col));
		int y_paste = (int) ((row));

		Tile tile = new Tile(url, x_paste, y_paste);

		return tile;
	}

	/**
	 * @return an array if tiles representing the full image at the actual zoom,
	 *         these tiles don't have downloaded the buffuredImage corresponding
	 */
	public List<Tile> getAllTiles(int zoom) {
		List<Tile> tiles = new ArrayList<Tile>();

		window_position.setLocation(0, 0);

		for (int i = 0; i < image.levels[zoom].get("x_tiles") + 1; i++) {
			for (int j = 0; j < image.levels[zoom].get("y_tiles") + 1; j++) {
				int col = i;
				int row = j;

				try {
					Tile tile = generateTile(row, col, zoom);
					tiles.add(tile);
				} catch (CytomineException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

		return tiles;

	}

	public List<Tile> getCrop(Point2D position, Dimension size, int zoom) {
		List<Tile> tiles = new ArrayList<Tile>();

		int[] data = getColsAndRows(position, size, zoom);

		int cols = data[0];
		int rows = data[1];
		int col0 = data[2];
		int row0 = data[3];

		for (int i = 0; i < cols; i++) {
			for (int j = 0; j < rows; j++) {
				int col = col0 + i;
				int row = row0 + j;

				try {
					Tile tile = generateTile(row, col, zoom);
					tiles.add(tile);
				} catch (CytomineException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

		return tiles;

	}

	@Override
	public void paintComponent(Graphics g) {

		Graphics2D g2 = (Graphics2D) g.create();
		Graphics2D g3 = (Graphics2D) g.create();
		// update panel size

		FontMetrics font = g3.getFontMetrics();

		g2.translate(getWidth() / 2 + view_position.getX(), getHeight() / 2 + view_position.getY());

		for (int i = 0; i < out_queue[zoom].size(); i++) {
			Tile tile = out_queue[zoom].get(i);

			if (tile.image != null) {
				g2.drawImage(tile.image, tile.c * image.tile_size, tile.r * image.tile_size, image.tile_size,
						image.tile_size, null);
			}
			//show tiles
//			g2.setColor(Color.RED);
//			g2.drawRect(tile.c * image.tile_size, tile.r * image.tile_size, image.tile_size,image.tile_size);

		}

		// zoom
		if (locked) {
			double percent = ((double)(image.depth - zoom)/image.depth*100);
			NumberFormat ft = new DecimalFormat("#0.00");
			
			String msg = ft.format(percent) + "%";
			int msgWidth = font.stringWidth(msg);
			int msgHeight = font.getHeight();
			g3.setColor(Color.black);
			g3.fillRect(5, 20 - msgHeight + 3, msgWidth, msgHeight);
			g3.setColor(Color.RED);

			g3.drawString(msg, 5, 20);

			msg = "press SpaceBar to toggle dynamic view";
			int msgWidth2 = font.stringWidth(msg);
			g3.setColor(Color.black);
			g3.fillRect(15+msgWidth, 20 - msgHeight + 3, msgWidth2, msgHeight);
			
			g3.setColor(Color.RED);
			g3.drawString(msg, 15+msgWidth, 20);
			
			//crosshair
			g3.drawOval((int)(getCenter().getX()-5), (int)(getCenter().getY()-5), 10, 10);
			
		}

		// dispose
		g2.dispose();
		g3.dispose();

	}

	public boolean left() {
		double previous_x = window_position.getX();
		window_position.setLocation(Math.max(0, window_position.getX() - (window_size.width - overlap)), window_position.getY());
		return previous_x != window_position.getX();
	}

	public boolean right() {
		if (window_position.getX() >= (image.levels[zoom].get("level_width") - window_size.width)) {
			return false;
		} else {
			double new_x = window_position.getX() + (window_size.width - overlap);
			if (new_x > (image.levels[zoom].get("level_width") - window_size.width)) {
				new_x = image.levels[zoom].get("level_width")
						- window_size.width;
			}
			window_position.setLocation(new_x, window_position.getY());	
			return true;
		}
	}

	public boolean up() {
		double previous_y = window_position.getY();
		window_position.setLocation(window_position.getX(), Math.max(0, window_position.getY() - (window_size.height - overlap)) );
		return previous_y != window_position.getY();
	}

	public boolean down() {
		if (window_position.getY() >= (image.levels[zoom].get("level_height") - window_size.height)) {
			return false;
		} else {
			double new_y = window_position.getY() + (window_size.height - overlap);
			if (new_y > (image.levels[zoom].get("level_height") - window_size.height)) {
				new_y = image.levels[zoom].get("level_height")
						- window_size.height;
			}
			window_position.setLocation(window_position.getX(), new_y);
			
			return true;
		}
	}

	public boolean next() {
		if (right()) {
			return true;
		} else {
			window_position.setLocation(0, window_position.getY());
			
			return down();
		}
	}

	public boolean previous() {
		if (left()) {
			return true;
		} else {
			while (right())
				;
			return up();
		}
	}

	public void resetQueues() {
		queue = new Stack<Tile>();
		
		try {
			read();
		} catch (CytomineException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	boolean inc_zoom() {
		int previous_zoom = zoom;
		zoom = Math.max(0, zoom - 1);
		if (previous_zoom != zoom) {
			oldZoom = previous_zoom;
			double zoom_factor = Math.pow(2, Math.abs(previous_zoom - zoom));
			translate_to_zoom(zoom_factor);
			resetQueues();
		}

		return previous_zoom != zoom;
	}

	boolean dec_zoom() {
		int previous_zoom = zoom;
		zoom = Math.min(image.depth - 1, zoom + 1);
		if (previous_zoom != zoom) {
			oldZoom = previous_zoom;
			double zoom_factor = Math.pow(2, Math.abs(previous_zoom - zoom));
			translate_to_zoom(1 / zoom_factor);
			resetQueues();
		}
		return previous_zoom != zoom;
	}

	void translate_to_zoom(double zoom_factor) {
		
		view_position.setLocation( view_position.getX() * zoom_factor, view_position.getY() * zoom_factor);
		scaleROI(zoom_factor);
		
	}

	MouseWheelListener wheelListener = new CytomineMouseWheelListener(this);

	MouseListener mouseListener = new CytomineMouseListener(this);

	void mouseMove() {
		Thread mouseThread = new Thread() {
			public void run() {
				while (mouseGrabbed) {

					double mouseX = MouseInfo.getPointerInfo().getLocation().x;
					double mouseY = MouseInfo.getPointerInfo().getLocation().y;

					double speedX = mouseX - oldMouseX;
					double speedY = mouseY - oldMouseY;

					view_position.setLocation( (view_position.getX() + speedX), (view_position.getY() + speedY));
					window_position.setLocation(-view_position.getX() - getWidth()/ 2, -view_position.getY() - getHeight() / 2);

					oldMouseX = mouseX;
					oldMouseY = mouseY;

					moveROI(speedX, speedY);

				}

			}
		};

		mouseThread.start();
	}

	void moveROI(double dx, double dy) {

		final List<ROI2D> rois = sequence.getROI2Ds();

		for (int i = 0; i < rois.size(); i++) {
			rois.get(i).translate(dx, dy);
		}

	}

	void scaleROI(double zoom_factor) {
		List<ROI2D> rois = sequence.getROI2Ds();

		for (int i = 0; i < rois.size(); i++) {
			ROI2D roi = rois.get(i);
			if (roi instanceof ROI2DPolygon) {
		
				Point2D.Double pos = new Point2D.Double(roi.getPosition2D().getX(),roi.getPosition2D().getY());
				
				if(zoom_factor<1){
					roi.translate(getWidth()*zoom_factor, getHeight()*zoom_factor);
				}else{
					roi.translate(-getWidth()/(2*zoom_factor), -getHeight()/(2*zoom_factor) );
				}
				
				ViewerTool.scaleROIPolygon((ROI2DPolygon) (roi), zoom_factor);
				
			}

		}
	}

	public Sequence getSequence() {
		return sequence;
	}
	
	public void loadAnnotations() throws CytomineException{
		sequence.removeAllROI();
		
		Map<String, String> filter = new TreeMap<String, String>();
		filter.put("user", "" + cytomine.getCurrentUser().getLong("id"));
		filter.put("image", "" + instance.getLong("id"));

		AnnotationCollection collection = cytomine.getAnnotations(filter);
		
		for (int i = 0; i < collection.size(); i++) {
			Long annonationID = collection.get(i).getLong("id");

			Annotation annotation = cytomine.getAnnotation(annonationID);

			String polygon = annotation.getStr("location");

			Dimension imageSize = new Dimension(instance.getInt("width"),
					instance.getInt("height"));
			int imageSizeY = instance.getInt("height");

			int sizeX = image.getLevels()[zoom].get("level_width");
			int sizeY = image.getLevels()[zoom].get("level_height");
			Dimension thumbnailSize = new Dimension(sizeX,sizeY);
			
			Point2D.Double ratio = IcytomineUtil.getScaleRatio(imageSize, thumbnailSize);

			CytomineImportedROI roi = CytomineImportedROI.build(IcytomineUtil.WKTtoPoint2D(polygon, ratio, imageSizeY), annotation, cytomine);

			roi.addListener(annotationListener);
			roi.initialise(cytomine, instance);
			
			roi.translate(getWidth()/2 + view_position.getX(), getHeight()/2 + view_position.getY());
			
			sequence.addROI(roi);
			
		}
		
	}
	
	public void saveAnnotations(){
		IcytomineUtil.deleteAllRoi(cytomine, instance, null);
		
		List<Long> terms = new ArrayList<Long>();
		
		for (int i = 0; i < sequence.getROI2Ds().size(); i++) {
			ROI2D roi = sequence.getROI2Ds().get(i);

			
			// convert to ROI for cytomine if possible
			CytomineImportedROI newROI;
			try {
				newROI = (CytomineImportedROI) roi;
				terms = newROI.terms;
			} catch (Exception e) {
				Annotation annotation = new Annotation();
				annotation.set("term", terms.toString());

				newROI = new CytomineImportedROI(((ROI2DPolygon) roi).getPoints(), annotation);
				newROI.setColor(Color.black);
				sequence.getROI2Ds().set(i, newROI);

				sequence.removeROI(roi);
				sequence.addROI(newROI);
			}
			
			Point2D.Double translation = new Point2D.Double(-(getWidth()/2 + view_position.getX()) , -(getHeight()/2 + view_position.getY()));
			
			int width = instance.getInt("width");
			int height = instance.getInt("height");
			int levelWidth = image.getLevels()[zoom].get("level_width");
			int levelHeight = image.getLevels()[zoom].get("level_height");
			
			Point2D.Double ratio = new Point2D.Double(width / levelWidth, height / levelHeight);
			
			IcytomineUtil.uploadROI(cytomine, instance, newROI, ratio, instance.getInt("height"), translation, terms, null);
			
		}

		
		try {
			loadAnnotations();
		} catch (CytomineException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	SequenceListener sequenceListener = new SequenceListener() {

		@Override
		public void sequenceClosed(Sequence sequence) {
			// TODO Auto-generated method stub

		}

		@Override
		public void sequenceChanged(SequenceEvent sequenceEvent) {
			if (listenerActive) {
				if (sequenceEvent.getType() == SequenceEventType.ADDED) {
					if (sequenceEvent.getSourceType() == SequenceEventSourceType.SEQUENCE_ROI) {
						Object source = sequenceEvent.getSource();
						try {
							final ROI2DPolygon roi = (ROI2DPolygon) source;

							roi.addListener(new ROIListener() {

								@Override
								public void roiChanged(ROIEvent event) {
									// TODO Auto-generated method stub
									if (event.getType() == ROIEventType.SELECTION_CHANGED) {

										if (!roi.isSelected()
												&& !(roi instanceof CytomineImportedROI)) {
											System.out.println(event.getType());
											Object source = event.getSource();
											ROI2DPolygon roi = (ROI2DPolygon) source;

											CytomineImportedROI annotation = new CytomineImportedROI(
													roi.getPoints(), instance,
													cytomine);

											sequence.removeROI(roi);
											sequence.addROI(annotation);
											annotation
													.addListener(annotationListener);
										}

									}
								}
							});

						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				}

			}
		}
	};
	
	ROIListener annotationListener = new ROIListener(){

		@Override
		public void roiChanged(ROIEvent event) {
			if(event.getType()==ROIEventType.SELECTION_CHANGED){
				Object source = event.getSource();
				CytomineImportedROI roi = (CytomineImportedROI) source;
				
				if(roi.isSelected()){
					roi.getConfig().setVisible(true);
				}else{
					roi.getConfig().setVisible(false);
				}
				
			}
			
		}
		
	};
	
}
