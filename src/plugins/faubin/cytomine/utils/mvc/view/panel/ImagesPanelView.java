package plugins.faubin.cytomine.utils.mvc.view.panel;

import icy.system.thread.ThreadUtil;

import java.awt.BorderLayout;
import java.awt.MouseInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import plugins.faubin.cytomine.Config;
import plugins.faubin.cytomine.utils.mvc.controller.panel.ImagesPanelController;
import plugins.faubin.cytomine.utils.mvc.view.frames.ProjectsFrame;
import be.cytomine.client.collections.ImageInstanceCollection;

public class ImagesPanelView extends JPanel {

	private ImagesPanelController controller;
	private JTable table;

	private NotEditableTableModel model;
	private JTextField page;

	private int totalPages = 0;
	private int currentPage = 0;
	
	private ImageInstanceCollection images;
	

	/**
	 * Create the panel.
	 */
	public ImagesPanelView(long idProj, ImageInstanceCollection instances,
			final ImagesPanelController controller) {
		this.controller = controller;

		if (instances.size() % 10 != 0) {
			totalPages = (int) Math.floor(instances.size()
					/ Config.nbDisplayedImage) + 1;
		} else {
			totalPages = (int) Math.floor(instances.size()
					/ Config.nbDisplayedImage);
		}

		setName("Projet: " + idProj + "   ");
		setLayout(new BorderLayout(0, 0));

		JMenuBar menuBar = new JMenuBar();
		add(menuBar, BorderLayout.NORTH);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);

		JMenuItem mntmGenerateSectionsFor = new JMenuItem(
				"Generate sections for all images");
		mntmGenerateSectionsFor.addActionListener(actionGenerateSection);

		mnEdit.add(mntmGenerateSectionsFor);

		JMenuItem mntmDeleteAllAnnotations = new JMenuItem(
				"Delete All Annotations");
		mntmDeleteAllAnnotations.addActionListener(actionDeleteAllAnnotations);
		mnEdit.add(mntmDeleteAllAnnotations);

//		JMenuItem mntmClose = new JMenuItem("Close");
//		mntmClose.addActionListener(actionClose);
//		menuBar.add(mntmClose);

		scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);

		model = new NotEditableTableModel();

		table = new JTable(model);
		table.getSelectionModel().addListSelectionListener(tableListener);
		scrollPane.setViewportView(table);

		JPanel panel = new JPanel();
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

		JButton btnLast = new JButton("" + (totalPages - 1));
		btnLast.addActionListener(pageNumberLast);
		panel.add(btnLast);

		// add columns to table

		String[] columns = { "Preview", "ID", "Name", "User", "Annotations",
				"Width", "Height" };

		model.setColumnIdentifiers(columns);
		
		table.getTableHeader().setReorderingAllowed(false);
		
		this.addComponentListener(new ComponentListener() {
			
			@Override
			public void componentShown(ComponentEvent e) {
			}
			
			@Override
			public void componentResized(ComponentEvent e) {
				loadRows(images);
			}
			
			@Override
			public void componentMoved(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void componentHidden(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}
		});

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
		if (isInt(page.getText())) {
			int newPage = Integer.parseInt(page.getText());
			loadPage(newPage);
		} else {
			page.setText("" + currentPage);
		}
	}

	private void loadPage(int nb) {
		if (nb >= 0 && nb < totalPages) {
			currentPage = nb;
			controller.loadImageFromOffset(nb * Config.nbDisplayedImage,
					Config.nbDisplayedImage);
		} else if (nb > totalPages) {
			page.setText("" + (totalPages - 1));
		} else if (nb < 0) {
			page.setText("0");
		} else {
			page.setText("" + currentPage);
		}
	}

	public void loadRows(final ImageInstanceCollection instances) {
		ThreadUtil.invokeLater(new Runnable() {
			@Override
			public void run() {
				images = instances;
				double total = images.size();
				double actual = 0;
				// reset table row to clean after a page change
				int rowCount = model.getRowCount();
				// Remove rows one by one from the end of the table
				for (int i = rowCount - 1; i >= 0; i--) {
					model.removeRow(i);
				}

				ProjectsFrame.progressBar.setValue(0);

				// generate row
				for (int i = 0; i < images.size(); i++) {
					model.addRow(new Object[] { "LOADING",
							images.get(i).getStr("id"),
							images.get(i).getStr("originalFilename"),
							images.get(i).getStr("user"),
							images.get(i).getStr("numberOfAnnotations"),
							images.get(i).getStr("width"),
							images.get(i).getStr("height") });

					int size = scrollPane.getSize().height/Config.nbDisplayedImage-4;
					if(size <= 10){ size = 10;}
					model.setValueAt(controller.getImageIcon(images.get(i), size), model.getRowCount() - 1, 0);
					
					table.setRowHeight(model.getRowCount() - 1, size);
					actual++;

					// update progressBar
					ProjectsFrame.progressBar
							.setValue((int) (actual / total * 100));
				}

			}
		});

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
			page.setText("" + (totalPages - 1));
			pageUpdated();

		}

	};

	private ListSelectionListener tableListener = new ListSelectionListener() {
		@Override
		public void valueChanged(ListSelectionEvent e) {
			if (e.getValueIsAdjusting() == true) {
				Long id = Long.parseLong(table.getValueAt(
						table.getSelectedRow(), 1).toString());
				controller.openImage(id);
			}
			if (e.getValueIsAdjusting() == false) {
				table.clearSelection();
			}

		}
	};

	class NotEditableTableModel extends DefaultTableModel {

		public boolean isCellEditable(int row, int column) {
			return false;
		}

		// Returning the Class of each column will allow different
		// renderers to be used based on Class
		public Class getColumnClass(int column) {
			return getValueAt(0, column).getClass();
		}
	}

	public ActionListener actionClose = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			controller.close();
		}

	};

	public ActionListener actionGenerateSection = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			controller.generateAndUploadSectionROI();
		}

	};

	public ActionListener actionDeleteAllAnnotations = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			controller.deleteAllAnnotations();
		}

	};
	private JScrollPane scrollPane;

}
