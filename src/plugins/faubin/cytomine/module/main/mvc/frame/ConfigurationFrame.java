package plugins.faubin.cytomine.module.main.mvc.frame;

import icy.gui.frame.IcyFrame;

import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.JSplitPane;

import plugins.faubin.cytomine.oldgui.mvc.view.panel.configuration.ConnectionConfigurationPanel;
import plugins.faubin.cytomine.oldgui.mvc.view.panel.configuration.GlomGenConfigurationPanel;
import plugins.faubin.cytomine.oldgui.mvc.view.panel.configuration.ImagesDownloadConfigurationPanel;
import plugins.faubin.cytomine.oldgui.mvc.view.panel.configuration.ImagesViewConfigurationPanel;

import java.awt.BorderLayout;
import java.awt.Color;

public class ConfigurationFrame extends IcyFrame {

	//singleton
	private static ConfigurationFrame frame = new ConfigurationFrame();
	
	public static ConfigurationFrame getConfigFrame() {
		return frame;
	}
	
	private JTree tree;

	/**
	 * Create the frame.
	 */
	@SuppressWarnings("serial")
	private ConfigurationFrame() {
		
		super("Configuration", true, true);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		setBounds(new Rectangle(100, 100, 450, 300));
		
		JPanel contentPane = new JPanel();
		contentPane.setBackground(Color.DARK_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setBackground(Color.DARK_GRAY);
		contentPane.add(splitPane);
		
		scrollPane = new JScrollPane();
		splitPane.setRightComponent(scrollPane);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		splitPane.setLeftComponent(scrollPane_1);
		
		// JTree will be used to navigate through the sections
		
		tree = new JTree();
		tree.setBackground(Color.WHITE);
		scrollPane_1.setViewportView(tree);
		
		tree.addTreeSelectionListener(treeListener);
		tree.setModel(new DefaultTreeModel(
			new DefaultMutableTreeNode("configuration") {
				{
					DefaultMutableTreeNode node_1;
					node_1 = new DefaultMutableTreeNode("main");
						node_1.add(new DefaultMutableTreeNode(Item.Login));
						node_1.add(new DefaultMutableTreeNode(Item.ImageDownload));
						node_1.add(new DefaultMutableTreeNode(Item.ImagesView));
					add(node_1);
					node_1 = new DefaultMutableTreeNode("function");
						node_1.add(new DefaultMutableTreeNode(Item.GlomGen));
					add(node_1);
					add(new DefaultMutableTreeNode(Item.DynView));
				}
			}
		));
		
		addToDesktopPane();
	}
	
	
	/**
	 * @author faubin
	 * items for the JTree
	 */
	public enum Item {
		Login, ImageDownload, ImagesView, GlomGen, DynView;
	}
	
	
	// event fired when selecting an item
	TreeSelectionListener treeListener = new TreeSelectionListener() {
		
		@Override
		public void valueChanged(TreeSelectionEvent e) {
			try{
				
				// try to get an Item, if it is not it will not 				
				Item item = (Item)((DefaultMutableTreeNode)tree.getLastSelectedPathComponent()).getUserObject();
				 
				switch ( item ) {
				case Login:
					scrollPane.setViewportView(new ConnectionConfigurationPanel());
					break;
				case ImageDownload:
					scrollPane.setViewportView(new ImagesDownloadConfigurationPanel());
					break;
				case ImagesView:
					scrollPane.setViewportView(new ImagesViewConfigurationPanel());
					break;
				case GlomGen:
					scrollPane.setViewportView(new GlomGenConfigurationPanel());
					break;
				default:
					System.out.println("this item is in creation");
					break;
				}
			
			}catch(Exception e2){
			}
			
			
		}
		
	};
	private JScrollPane scrollPane;
}

