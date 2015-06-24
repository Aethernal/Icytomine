package plugins.faubin.cytomine.module.main;

import icy.file.Loader;
import icy.file.SequenceFileImporter;
import icy.gui.frame.IcyFrame;
import icy.main.Icy;
import icy.sequence.Sequence;
import icy.system.thread.ThreadUtil;
import icy.util.XMLUtil;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Rectangle;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.JSplitPane;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JScrollPane;

import java.awt.GridLayout;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;

import javax.swing.JTextField;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.ScrollPaneConstants;

import java.awt.SystemColor;
import java.util.List;
import java.util.concurrent.Callable;

import javax.swing.JLayeredPane;

import org.w3c.dom.Node;

import be.cytomine.client.Cytomine;
import be.cytomine.client.CytomineException;
import be.cytomine.client.collections.ImageInstanceCollection;
import be.cytomine.client.collections.ProjectCollection;
import be.cytomine.client.collections.SoftwareCollection;
import be.cytomine.client.models.ImageInstance;
import be.cytomine.client.models.Software;
import plugins.faubin.cytomine.module.main.mvc.Controller;
import plugins.faubin.cytomine.module.main.mvc.custom.CustomTabbedPaneUI;
import plugins.faubin.cytomine.module.main.mvc.frame.ConfigurationFrame;
import plugins.faubin.cytomine.module.main.mvc.frame.InputID;
import plugins.faubin.cytomine.module.main.mvc.panel.Workspace;
import plugins.faubin.cytomine.module.project.ProjectController;
import plugins.faubin.cytomine.module.projects.ProjectsController;
import plugins.faubin.cytomine.module.tileViewer.CytomineReader;
import plugins.faubin.cytomine.module.tileViewer.toolbar.Toolbar;
import plugins.faubin.cytomine.utils.Config;
import plugins.faubin.cytomine.utils.Configuration;
import plugins.faubin.cytomine.utils.IcytomineUtil;
import plugins.faubin.cytomine.utils.crop.CytomineCrop;
import plugins.faubin.cytomine.utils.software.SoftwareGlomeruleFinder;
import plugins.faubin.cytomine.utils.software.SoftwareSectionFinder;

/**
 * @author faubin
 *
 */
public class IcytomineFrame extends IcyFrame {
	
	public static IcytomineFrame frame = new IcytomineFrame();
	
	public static IcytomineFrame getIcytomineFrame() {
		return frame;
	}
	
	protected Configuration configuration = Configuration.getConfiguration();
	
	public static Cytomine cytomine;
	
	private JPanel contentPane;
	private JTabbedPane tabbedPane;
	private JScrollPane menu;
	

	/**
	 * this is the main frame of the plugin. It contain his own menu, and an area for the modules
	 */
	public IcytomineFrame() {
		super("Icytomine",true,true,false,false);
		addToDesktopPane();
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(new Rectangle(100, 100, 450, 300));
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setForeground(Color.BLACK);
		menuBar.setBackground(Color.BLACK);
		setJMenuBar(menuBar);
		
		JMenu mnProjects = new JMenu("Project");
		mnProjects.setForeground(Color.WHITE);
		mnProjects.setBackground(Color.WHITE);
		menuBar.add(mnProjects);
		
		JMenuItem mntmOpenList = new JMenuItem("open list");
		mntmOpenList.addActionListener(actionListProjects);
		mnProjects.add(mntmOpenList);
		
		JMenuItem mntmOpenById = new JMenuItem("open by ID");
		mntmOpenById.addActionListener(actionOpenProjectID);
		mnProjects.add(mntmOpenById);
		
		JMenu mnImage = new JMenu("Image");
		mnImage.setBackground(Color.WHITE);
		menuBar.add(mnImage);
		
		JMenuItem mntmOpenById_1 = new JMenuItem("open by ID");
		mnImage.add(mntmOpenById_1);
		mntmOpenById_1.addActionListener(actionOpenImageID);
		
		JMenuItem mntmLoadLocalCrop = new JMenuItem("load local cytomine crop");
		mntmLoadLocalCrop.addActionListener(actionOpenCrop);
		
		mnImage.add(mntmLoadLocalCrop);
		
		JMenuItem mntmConfiguration = new JMenuItem("Configuration");
		mntmConfiguration.addActionListener(actionConfiguration);
		mntmConfiguration.setForeground(Color.WHITE);
		mntmConfiguration.setBackground(Color.WHITE);
		menuBar.add(mntmConfiguration);
		contentPane = new JPanel();
		contentPane.setBackground(Color.DARK_GRAY);
		contentPane.setBorder(new LineBorder(new Color(0, 0, 0), 2, true));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel bottom_credit = new JPanel();
		bottom_credit.setBackground(Color.DARK_GRAY);
		contentPane.add(bottom_credit, BorderLayout.SOUTH);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.DARK_GRAY);
		contentPane.add(panel);
		
		JPanel workspace_area = new JPanel();
		workspace_area.setForeground(Color.DARK_GRAY);
		workspace_area.setBorder(null);
		workspace_area.setBackground(Color.WHITE);
		workspace_area.setLayout(new BorderLayout(0, 0));
		contentPane.add(workspace_area, BorderLayout.CENTER);
		
		final JSplitPane splitPane = new JSplitPane();
		splitPane.setBackground(Color.DARK_GRAY);
		workspace_area.add(splitPane, BorderLayout.CENTER);
		
		JPanel left_menu = new JPanel();
		splitPane.setLeftComponent(left_menu);
		left_menu.setBorder(null);
		left_menu.setBackground(new Color(0, 153, 204));
		left_menu.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_4 = new JPanel();
		panel_4.setForeground(SystemColor.menuText);
		panel_4.setBackground(SystemColor.menu);
		left_menu.add(panel_4, BorderLayout.CENTER);
		panel_4.setLayout(new BorderLayout(0, 0));
		
		menu = new JScrollPane();
		menu.setViewportBorder(null);
		panel_4.add(menu, BorderLayout.CENTER);
		
		JLabel lblActions = new JLabel("  Actions  ");
		lblActions.setForeground(SystemColor.menuText);
		lblActions.setBackground(SystemColor.menu);
		lblActions.setHorizontalAlignment(SwingConstants.CENTER);
		panel_4.add(lblActions, BorderLayout.NORTH);
		
		JPanel workspace = new JPanel();
		workspace.setBackground(Color.DARK_GRAY);
		splitPane.setRightComponent(workspace);
		workspace.setLayout(new BorderLayout(0, 0));
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setForeground(Color.WHITE);
		tabbedPane.setBorder(null);
		tabbedPane.setBackground(Color.DARK_GRAY);
		tabbedPane.setUI(new CustomTabbedPaneUI());
		workspace.add(tabbedPane);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.DARK_GRAY);
		contentPane.add(panel_1, BorderLayout.WEST);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(Color.DARK_GRAY);
		contentPane.add(panel_2, BorderLayout.EAST);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBackground(Color.DARK_GRAY);
		contentPane.add(panel_3, BorderLayout.NORTH);
		
		tabbedPane.addChangeListener(chList);
		
		
	}
	
	/**
	 * this function allow to define witch menu will be used
	 * @param panel
	 */
	private void setMenu(JPanel panel){
		menu.setViewportView(panel);
	}
	
	/**
	 * this function allow to select a tab between all the tabs available, tab correspond to a module
	 * @param c
	 */
	private void setWorkSpace(Container c){
		tabbedPane.setSelectedComponent(c);
	}
	
	
	/**
	 * this function allow to show a module by showing his workspace and menu
	 * @param controller
	 */
	private void showModule(Controller controller){
		setWorkSpace(controller.getView().getWorkSpace());
		setMenu(controller.getView().getMenu());
	}
	
	/**
	 * this function allow to add a new module to the interface, it add a new tab and then show the module
	 * @param controller
	 */
	public void addModule(Controller controller){
		tabbedPane.addTab(controller.getName(), controller.getView().getWorkSpace());
		showModule(controller);
	}
	
	/**
	 * this function is used to remove a module from the interface
	 * @param workspace
	 */
	public void removeModule(Workspace workspace){
		setMenu(null);
		tabbedPane.remove(workspace);
	}
	
	//actions listeners
	
	/**
	 * start the projects module
	 */
	ActionListener actionListProjects = new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent e) {
			ProjectsController projects = new ProjectsController(cytomine);
			projects.applyToFrame();
		}
		
	};
	
	/**
	 * start the images module showing  the content of a project.
	 */
	ActionListener actionOpenProjectID = new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent e) {


			final InputID input = new InputID();
			
			Runnable runnable = new Runnable(){

				@Override
				public void run() {
					long ID = input.getID();
					ProjectController project = new ProjectController(cytomine, ID);
					project.applyToFrame();
				}
				
			};
			
			input.setRunnable(runnable);
			
			input.setVisible(true);
			
		}
		
	};
	
	/**
	 * start the dynamic viewer module with the specified image
	 */
	ActionListener actionOpenImageID = new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent e) {


			final InputID input = new InputID();
			
			Runnable runnable = new Runnable(){

				@Override
				public void run() {
					long ID = input.getID();
					
					ImageInstance instance;
					try {
						instance = cytomine.getImageInstance(ID);
						CytomineReader reader = new CytomineReader(cytomine, instance, configuration.dynamicViewerDim, true);
						Toolbar toolbar = new Toolbar(reader);
						toolbar.setVisible(true);
						
						Icy.getMainInterface().addSequence(reader.getSequence());
						
					} catch (CytomineException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				
			};
			
			input.setRunnable(runnable);
			
			input.setVisible(true);
			
		}
		
	};
	
	/**
	 * allow to load a saved cytomine crop using his xml file that should contain the <cytomine></cytomine> node
	 */
	ActionListener actionOpenCrop = new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent e) {
				
			JFileChooser fch = new JFileChooser();
			int r = fch.showOpenDialog(null);
			
			if(r == JFileChooser.APPROVE_OPTION){
				
				Sequence seq = Loader.loadSequence(fch.getSelectedFile().getAbsolutePath(), 0, false);
				Icy.getMainInterface().addSequence(seq);
				seq.loadXMLData();
				System.out.println(seq.getFilename());
				
				Node cytomineNode = seq.getNode("cytomine");
				Node original = XMLUtil.getElement(cytomineNode, "originalBounding");
				Node scaled = XMLUtil.getElement(cytomineNode, "scaledBounding");
				
				System.out.println(cytomineNode);
				System.out.println(original);
				
				
				
				double ratio = XMLUtil.getElementDoubleValue(cytomineNode, "ratio", 1);
				long instanceID = XMLUtil.getElementLongValue(cytomineNode, "instance", 0);
				
				int x,y,width,height;
				
				x = XMLUtil.getElementIntValue(original, "x", 0);
				y = XMLUtil.getElementIntValue(original, "y", 0);
				width = XMLUtil.getElementIntValue(original, "width", 0);
				height = XMLUtil.getElementIntValue(original, "height", 0);
				
				Rectangle originalRect = new Rectangle(x,y,width,height);
				
				x = XMLUtil.getElementIntValue(scaled, "x", 0);
				y = XMLUtil.getElementIntValue(scaled, "y", 0);
				width = XMLUtil.getElementIntValue(scaled, "width", 0);
				height = XMLUtil.getElementIntValue(scaled, "height", 0);
				
				Rectangle scaledRect = new Rectangle(x,y,width,height);
				
				CytomineCrop crop = new CytomineCrop(seq, instanceID, originalRect, scaledRect, ratio);
				plugins.faubin.cytomine.utils.crop.toolbar.Toolbar toolbar = new plugins.faubin.cytomine.utils.crop.toolbar.Toolbar(crop, cytomine);
				
			}
			
				
			

			
		}
		
	};
	
	/**
	 * show the configuration frame
	 */
	ActionListener actionConfiguration = new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent e) {
			ConfigurationFrame.getConfigFrame().setVisible(true);
		}
	};

	/**
	 * tab listener to swap workspace and menu when a tab is selected or removed
	 */
	ChangeListener chList = new ChangeListener(){

		@Override
		public void stateChanged(ChangeEvent e) {
			
			Component c = tabbedPane.getSelectedComponent();
			if(c instanceof Workspace){
				Workspace w = (Workspace) c;
				setMenu(w.getView().getMenu());
			}else if(c == null){
				setMenu(null);
			}
			
		}

	};
}
