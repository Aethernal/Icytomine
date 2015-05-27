package plugins.faubin.cytomine.gui.mvc.view.frame;

import icy.gui.frame.IcyFrame;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import plugins.faubin.cytomine.AnnotationTerm;
import be.cytomine.client.collections.TermCollection;

public class TermFrame extends IcyFrame {
	private DefaultListModel<Object> defaultTerms;
	private JList<Object> defaultList;
	private DefaultListModel<Object> selectedTerms;
	private JList<Object> selectedList;

	private JPanel contentPane;

	public void windowClosing(WindowEvent e) {
		this.setVisible(false);
	}

	/**
	 * Create the frame.
	 * 
	 * @param termCollection
	 */
	public TermFrame(TermCollection termCollection) {
		super("Icytomine : terms settings", true, true, false, false);

		setBounds(new Rectangle(100, 100, 450, 300));
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(new GridLayout(1, 0, 0, 0));

		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane);

		defaultTerms = new DefaultListModel<Object>();
		defaultList = new JList<Object>(defaultTerms);
		defaultList
				.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		defaultList.setLayoutOrientation(JList.VERTICAL);

		scrollPane.setViewportView(defaultList);

		JScrollPane scrollPane_1 = new JScrollPane();
		panel.add(scrollPane_1);

		selectedTerms = new DefaultListModel<Object>();
		selectedList = new JList<Object>(selectedTerms);
		scrollPane_1.setViewportView(selectedList);

		for (int i = 0; i < termCollection.size(); i++) {
			defaultTerms.addElement(new AnnotationTerm(termCollection.get(i)
					.getStr("name"), termCollection.get(i).getLong("id")));
		}

		JSplitPane splitPane_1 = new JSplitPane();
		contentPane.add(splitPane_1, BorderLayout.SOUTH);

		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(action_add);
		splitPane_1.setLeftComponent(btnAdd);

		JButton btnRemove = new JButton("Remove");
		btnRemove.addActionListener(action_remove);
		splitPane_1.setRightComponent(btnRemove);

		addToDesktopPane();
		
	}

	public DefaultListModel<Object> getSelectedTerms() {
		return selectedTerms;
	}

	private ActionListener action_add = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			int[] selected = defaultList.getSelectedIndices();

			for (int i = 0; i < selected.length; i++) {
				Object element = defaultTerms.get(selected[i]);
				if (!selectedTerms.contains(element)) {
					selectedTerms.addElement(element);
				}
			}
		}
	};

	private ActionListener action_remove = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			int[] selected = selectedList.getSelectedIndices();

			for (int i = 0; i < selected.length; i++) {
				Object element = selectedTerms.get(selected[i]);
				selectedTerms.removeElement(element);
			}
		}
	};

}
