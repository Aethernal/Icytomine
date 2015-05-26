package plugins.faubin.cytomine.gui.tileViewer;

import icy.main.Icy;
import icy.painter.Overlay;
import icy.roi.ROI2D;
import icy.sequence.Sequence;

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
import java.util.Stack;

import javax.swing.JPanel;
import javax.swing.Timer;

import plugins.kernel.roi.roi2d.ROI2DPolygon;
import be.cytomine.client.Cytomine;
import be.cytomine.client.CytomineException;
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
	Rectangle window_position;

	int overlap;

	// zoom
	int zoom;
	int oldZoom;

	// mouse mouvement
	protected boolean mouseGrabbed;
	protected double oldMouseX;
	protected double oldMouseY;
	Point view_position;

	// sequence
	Sequence sequence;
	Overlay overlay;
	boolean locked;

	@SuppressWarnings({ "unchecked" })
	public CytomineReader(Cytomine cytomine, ImageInstance instance,
			Dimension dim) throws CytomineException {
		this.overlap = 0;
		this.cytomine = cytomine;

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
		view_position = new Point(-getWidth() / 2, -getHeight() / 2);
		window_position = getBounds();

		// sequence initialisation
		sequence = new Sequence();
		sequence.setName("" + instance.getLong("id") + " - Dynamic view");

		overlay = new OverlayCytomine(this);
		locked = true;
		sequence.addOverlay(overlay);

		// repaint loop every 16 ms
		Timer repaint = new Timer(16, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

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

	public Point2D getCenter() {
		Point2D center = new Point2D.Double();

		center.setLocation(getWidth() / 2, getHeight() / 2);

		return center;
	}

	public void read_window() {
		window_position.width = (int) (window_position.width * Math
				.pow(2, zoom));
		window_position.height = (int) (window_position.height * Math.pow(2,
				zoom));
		window_position.x = (int) (window_position.x * Math.pow(2, zoom));
		window_position.y = (int) (window_position.y * Math.pow(2, zoom));
	}

	// print the whole picture
	public void readAll() throws CytomineException {
		window_position.x = 1;
		window_position.y = 1;
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
				Dimension viewerSize = Icy.getMainInterface()
						.getViewers(sequence).get(0).getBounds().getSize();
				setSize(viewerSize.width, viewerSize.height);
				window_position.setSize(getSize());

				int[] colsANDrows = getColsAndRows(
						window_position.getLocation(),
						window_position.getSize(), zoom);

				int cols = colsANDrows[0];
				int rows = colsANDrows[1];
				int col0 = colsANDrows[2];
				int row0 = colsANDrows[3];

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
					threads[i] = new ThreadUrl(queue, out_queue, zoom,
							cytomine);
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

		String baseUrl = cytomine.getImageServersFromAbstractImage(image.image
				.getLong("id"));

		String url = baseUrl + "&tileGroup=" + tile_group + "&z="
				+ (image.depth - zoom) + "&x=" + col + "&y=" + row
				+ "&mimeType=openslide/" + image.mime + "";

		int x_paste = (int) ((col * image.tile_size));
		int y_paste = (int) ((row * image.tile_size));

		Tile tile = new Tile(url, x_paste, y_paste);

		return tile;
	}

	/**
	 * @return an array if tiles representing the full image at the actual zoom,
	 *         these tiles don't have downloaded the buffuredImage corresponding
	 */
	public List<Tile> getAllTiles(int zoom) {
		List<Tile> tiles = new ArrayList<Tile>();

		window_position.x = 0;
		window_position.y = 0;

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

		g2.translate(getWidth() / 2 + view_position.x, getHeight() / 2
				+ view_position.y);

		for (int i = 0; i < out_queue[zoom].size(); i++) {
			Tile tile = out_queue[zoom].get(i);

			if (tile.image != null) {
				g2.drawImage(tile.image, tile.c, tile.r, image.tile_size,
						image.tile_size, null);
			}

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
			g3.drawString(msg,
					15+msgWidth, 20);
		}

		// crosshair
		if (locked) {
			g3.setColor(Color.red);
		} else {
			g3.setColor(Color.blue);
		}

		// dispose
		g2.dispose();
		g3.dispose();

	}

	public boolean left() {
		int previous_x = window_position.x;
		window_position.x = Math.max(0, window_position.x
				- (window_position.width - overlap));
		return previous_x != window_position.x;
	}

	public boolean right() {
		if (window_position.x >= (image.levels[zoom].get("level_width") - window_position.width)) {
			return false;
		} else {
			int new_x = window_position.x + (window_position.width - overlap);
			if (new_x > (image.levels[zoom].get("level_width") - window_position.width)) {
				new_x = image.levels[zoom].get("level_width")
						- window_position.width;
			}
			window_position.x = new_x;
			return true;
		}
	}

	public boolean up() {
		int previous_y = window_position.y;
		window_position.y = Math.max(0, window_position.y
				- (window_position.height - overlap));
		return previous_y != window_position.y;
	}

	public boolean down() {
		if (window_position.y >= (image.levels[zoom].get("level_height") - window_position.height)) {
			return false;
		} else {
			int new_y = window_position.y + (window_position.height - overlap);
			if (new_y > (image.levels[zoom].get("level_height") - window_position.height)) {
				new_y = image.levels[zoom].get("level_height")
						- window_position.height;
			}
			window_position.y = new_y;
			return true;
		}
	}

	public boolean next() {
		if (right()) {
			return true;
		} else {
			window_position.x = 0;
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

	boolean inc_zoom(Point point) {
		int previous_zoom = zoom;
		zoom = Math.max(0, zoom - 1);
		if (previous_zoom != zoom) {
			oldZoom = previous_zoom;
			double zoom_factor = Math.pow(2, Math.abs(previous_zoom - zoom));
			translate_to_zoom(zoom_factor, point);
			resetQueues();
		}

		return previous_zoom != zoom;
	}

	boolean dec_zoom(Point point) {
		int previous_zoom = zoom;
		zoom = Math.min(image.depth - 1, zoom + 1);
		if (previous_zoom != zoom) {
			oldZoom = previous_zoom;
			double zoom_factor = Math.pow(2, Math.abs(previous_zoom - zoom));
			translate_to_zoom(1 / zoom_factor, point);
			resetQueues();
		}
		return previous_zoom != zoom;
	}

	void translate_to_zoom(double zoom_factor, Point mousePos) {
		if (mousePos != null) {
			view_position.x = (int) ((view_position.x + (getCenter().getX() - mousePos
					.getX())) * zoom_factor);
			view_position.y = (int) ((view_position.y + (getCenter().getY() - mousePos
					.getY())) * zoom_factor);
		} else {
			view_position.x = (int) ((view_position.x) * zoom_factor);
			view_position.y = (int) ((view_position.y) * zoom_factor);
		}

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

					view_position.setLocation((int) (view_position.x + speedX),
							(int) (view_position.y + speedY));
					window_position.setLocation(-view_position.x - getWidth()
							/ 2, -view_position.y - getHeight() / 2);

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

	void moveScaleROI(double scale) {
		List<ROI2D> rois = sequence.getROI2Ds();

		for (int i = 0; i < rois.size(); i++) {
			ROI2D roi = rois.get(i);
			if (roi instanceof ROI2DPolygon) {

				double dx = roi.getPosition2D().getX() * scale;
				double dy = roi.getPosition2D().getY() * scale;

				roi.setPosition2D(new Point2D.Double(dx, dy));

			}
		}

	}

	void scaleROI(double zoom_factor) {
		List<ROI2D> rois = sequence.getROI2Ds();

		for (int i = 0; i < rois.size(); i++) {
			ROI2D roi = rois.get(i);
			if (roi instanceof ROI2DPolygon) {

				double oldWidth = roi.getBounds().width;
				double oldHeight = roi.getBounds().height;

				ViewerTool.scaleROIPolygon((ROI2DPolygon) (roi), zoom_factor);

				double newWidth = roi.getBounds().width;
				double newHeight = roi.getBounds().height;

				double dx = newWidth - oldWidth;
				double dy = newHeight - oldHeight;

				roi.translate(-dx / 2, -dy / 2);
				// roi.setPosition2D(new Point2D.Double( , ) );
			}

		}
	}

	public Sequence getSequence() {
		return sequence;
	}
	
}
