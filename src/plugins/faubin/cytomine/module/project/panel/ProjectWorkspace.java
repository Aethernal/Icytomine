package plugins.faubin.cytomine.module.project.panel;

import icy.main.Icy;
import icy.system.thread.ThreadUtil;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowSorter;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import plugins.faubin.cytomine.module.main.mvc.View;
import plugins.faubin.cytomine.module.main.mvc.panel.Workspace;
import plugins.faubin.cytomine.module.project.ProjectView;
import plugins.faubin.cytomine.module.tileViewer.CytomineReader;
import plugins.faubin.cytomine.oldgui.mvc.model.utils.Configuration;
import be.cytomine.client.collections.ImageInstanceCollection;
import be.cytomine.client.models.ImageInstance;

import javax.swing.JSplitPane;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JLabel;
import javax.swing.JSeparator;

@SuppressWarnings("serial")
public class ProjectWorkspace extends Workspace {
	
	private ProjectView view;
	
	private JTable table;
	private NoEditProjectsModel tableModel;
	
	private JScrollPane thumbnail;
	
	//configuration
	Configuration configuration = Configuration.getConfiguration();
	
	//pagination
	int nbImages;
	int nbPage;
	int currentPage;
	JTextField page;
	
	public ProjectWorkspace(ProjectView view, long ID) {
		super();
		this.view = view;
		
		tableModel = new NoEditProjectsModel();
		RowSorter<NoEditProjectsModel> sorter = new TableRowSorter<NoEditProjectsModel>(tableModel);
		
		String[] columns = { "Preview", "ID", "Name", "User", "Annotations", "Width", "Height" };
		tableModel.setColumnIdentifiers(columns);
		
		//Pagination system
		nbImages = getNbImages(ID);
		nbPage = (int) Math.ceil(	(double) nbImages / (double) configuration.nbRowPerPage	);
		
		JPanel panel_1 = new JPanel();
		add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		final JSplitPane splitPane = new JSplitPane();
		splitPane.setBackground(Color.DARK_GRAY);
		panel_1.add(splitPane, BorderLayout.CENTER);
		
		JScrollPane scrollPane = new JScrollPane();
		splitPane.setLeftComponent(scrollPane);
		
		table = new JTable();
		scrollPane.setViewportView(table);
		table.setModel(tableModel);
		
		table.getTableHeader().setReorderingAllowed(false);
		table.setRowSorter(sorter);
		table.setGridColor(Color.GRAY);
		
		table.addMouseListener(mouseListener);
		table.addKeyListener(keyListener);
		table.getSelectionModel().addListSelectionListener(selectionListener);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		splitPane.setRightComponent(scrollPane_1);
		
		JPanel panel_2 = new JPanel();
		scrollPane_1.setViewportView(panel_2);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_3 = new JPanel();
		panel_3.setBackground(Color.DARK_GRAY);
		panel_2.add(panel_3, BorderLayout.SOUTH);
		panel_3.setLayout(new BorderLayout(0, 0));
		
		JLabel lblNewLabel = new JLabel("To open the dynamic viewer, double click on the image in the list");
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		panel_3.add(lblNewLabel, BorderLayout.CENTER);
		
		JSeparator separator = new JSeparator();
		panel_3.add(separator, BorderLayout.SOUTH);
		
		JSeparator separator_1 = new JSeparator();
		panel_3.add(separator_1, BorderLayout.NORTH);
		
		thumbnail = new JScrollPane();
		panel_2.add(thumbnail, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.DARK_GRAY);
		add(panel, BorderLayout.SOUTH);

		JButton btnFirst = new JButton("0");
		btnFirst.addActionListener(pageNumberFirst);
		panel.add(btnFirst);

		JButton button = new JButton("<");
		button.addActionListener(pageNumberDown);
		panel.add(button);

		page = new JTextField();
		page.setHorizontalAlignment(SwingConstants.CENTER);
		page.setText("0");
		page.addActionListener(pageNumberChange);
		panel.add(page);

		JButton button_1 = new JButton(">");
		button_1.addActionListener(pageNumberUp);
		panel.add(button_1);

		JButton btnLast = new JButton("" + (nbPage - 1));
		btnLast.addActionListener(pageNumberLast);
		panel.add(btnLast);
		
		pageUpdated();
		
	}
	
	private int getNbImages(long ID){
		return view.getNbImages(ID);
	}
	
	private ImageIcon getImageIcon(ImageInstance instance, int size){
		return view.getImageIcon(instance, size);
		
	}
	
	private void clearTable(){
		for (int i = tableModel.getRowCount() - 1; i >= 0; i--) {
			tableModel.removeRow(i);
		}
	}
	
	private void addToTable(final ImageInstance image){

		tableModel.addRow(
				new Object[] { 
					"LOADING",
					image.getLong("id"),
					image.getStr("originalFilename"),
					image.getLong("user"),
					image.getInt("numberOfAnnotations"),
					image.getInt("width"),
					image.getInt("height")
			}
		);
		
		tableModel.setValueAt(getImageIcon(image, configuration.iconPreviewMaxSize), tableModel.getRowCount() - 1, 0);
		
		table.setRowHeight(tableModel.getRowCount() - 1, 30);
			
	}
	
	private void setTable(final ImageInstanceCollection images){
		ThreadUtil.bgRun(new Runnable(){

			@Override
			public void run() {
				clearTable();
				for (int i = 0; i < images.size(); i++) {
					addToTable(images.get(i));
				}
			}
			
		});
	}
	
	public ImageInstanceCollection getImages(long ID){
		return view.getImages(ID);
	}
	
	/**
	 * @return project ID or -1 if no row was selected
	 */
	public long getSelected(){
		int row = table.getSelectedRow();
		if(row == -1){
			return -1;
		}
		return (Long) tableModel.getValueAt(row, 1);
	}

	class NoEditProjectsModel extends DefaultTableModel {

		public boolean isCellEditable(int row, int column) {
			return false;
		}

		public Class<?> getColumnClass(int column) {
	        Class<?> returnValue;
	        switch(column){
	        case 0:
	        	returnValue = ImageIcon.class;
	        	break;
	        case 1:
	        	returnValue = Long.class;
	        	break;
	        case 2:
	        	returnValue = String.class;
	        	break;
	        case 3:
	        	returnValue = Long.class;
	        	break;
	        case 4:
	        	returnValue = Integer.class;
	        	break;
	        case 5:
	        	returnValue = Integer.class;
	        	break;
	        case 6:
	        	returnValue = Integer.class;
	        	break;
	        default:
	        	returnValue = Object.class;
	        	break;
	        }
	        
			return returnValue;
		}

	}

	@Override
	public View getView() {
		return view;
	}
	
	private boolean isInt(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private void pageUpdated() {
		table.getSelectionModel().clearSelection();
		if (isInt(page.getText())) {
			int newPage = Integer.parseInt(page.getText());
			loadPage(newPage);
		} else {
			page.setText("" + currentPage);
		}
	}

	private void loadPage(int nb) {
		if (nb >= 0 && nb < nbPage) {
			currentPage = nb;
			setTable(loadImageFromOffset(nb * configuration.nbRowPerPage, configuration.nbRowPerPage));
		} else if (nb > nbPage) {
			page.setText("" + (nbPage - 1));
		} else if (nb < 0) {
			page.setText("0");
		} else {
			page.setText("" + currentPage);
		}
	}

	private ImageIcon getThumbnail(long ID, int size){
		ImageIcon instance = view.getThumbnail(ID, size);
		return instance;
	}
	
	public void eventSelectionChanged(Long ID){
		int maxSize = Math.min(thumbnail.getWidth(), thumbnail.getHeight());
		
		JLabel label = new JLabel(getThumbnail(ID, maxSize));
		thumbnail.setViewportView(label);
	}
	
	private void openDynamicView(CytomineReader reader){
		Icy.getMainInterface().addSequence(reader.getSequence());
	}
	
	private ImageInstanceCollection loadImageFromOffset(int i, int nbRowPerPage) {
		return view.loadImageFromOffset(i, nbRowPerPage);
		
	}

	public ActionListener pageNumberChange = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			pageUpdated();
		}

	};

	public ActionListener pageNumberUp = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (isInt(page.getText())) {
				page.setText("" + (Integer.parseInt(page.getText()) + 1));
				pageUpdated();
			}
		}

	};

	public ActionListener pageNumberDown = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (isInt(page.getText())) {
				page.setText("" + (Integer.parseInt(page.getText()) - 1));
				pageUpdated();
			}
		}

	};

	public ActionListener pageNumberFirst = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			page.setText("0");
			pageUpdated();
		}

	};

	public ActionListener pageNumberLast = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			page.setText("" + (nbPage - 1));
			pageUpdated();

		}

	};
	
	MouseAdapter mouseListener = new MouseAdapter() {
	    public void mousePressed(MouseEvent me) {
	        JTable table =(JTable) me.getSource();
	        Point p = me.getPoint();
	        int row = table.rowAtPoint(p);
	        if (me.getClickCount() == 2) {
	            long ID = (Long) tableModel.getValueAt(row, 1);
	            openDynamicView(view.openReader(ID));
	        }
	    }
	};
	
	KeyListener keyListener = new KeyListener(){

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == 10){
				int row = table.getSelectedRow();
				if(row != -1){
					long ID = (Long) tableModel.getValueAt(row, 1);
					openDynamicView(view.openReader(ID));
				}
			}
			
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	};
	
	private ListSelectionListener selectionListener = new ListSelectionListener() {
		@Override
		public void valueChanged(ListSelectionEvent e) {
			if (e.getValueIsAdjusting() == false) {
				try{
					Long id = Long.parseLong(table.getValueAt(	table.getSelectedRow(), 1).toString()	);
					eventSelectionChanged(id);
				}catch(IndexOutOfBoundsException e2){
				}
			}
		}
	};

}
