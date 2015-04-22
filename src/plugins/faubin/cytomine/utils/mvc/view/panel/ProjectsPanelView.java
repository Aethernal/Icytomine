package plugins.faubin.cytomine.utils.mvc.view.panel;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import plugins.faubin.cytomine.utils.mvc.controller.panel.ProjectsPanelController;
import be.cytomine.client.collections.ProjectCollection;

public class ProjectsPanelView extends JPanel {

	private ProjectsPanelController controller;

	private JTable table;

	private DefaultTableModel model;

	/**
	 * Create the panel.
	 */
	public ProjectsPanelView(ProjectCollection projects,
			ProjectsPanelController controller) {
		this.controller = controller;

		setName("Projects   ");
		setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);

		model = new NotEditableTableModel();

		table = new JTable(model);
		table.getSelectionModel().addListSelectionListener(tableListener);
		scrollPane.setViewportView(table);

		// add columns to table

		String[] columns = { "ID", "Name", "Ontology ID", "Images",
				"User annotations" };

		model.setColumnIdentifiers(columns);

		table.getTableHeader().setReorderingAllowed(false);
		
		for (int i = 0; i < projects.size(); i++) {
			model.addRow(new String[] { projects.get(i).getStr("id"),
					projects.get(i).getStr("name"),
					projects.get(i).getStr("ontology"),
					projects.get(i).getStr("numberOfImages"),
					projects.get(i).getStr("numberOfAnnotations"), });
		}
	}

	private ListSelectionListener tableListener = new ListSelectionListener() {
		@Override
		public void valueChanged(ListSelectionEvent e) {
			if (e.getValueIsAdjusting() == true) {
				Long id = Long.parseLong(table.getValueAt(
						table.getSelectedRow(), 0).toString());
				controller.openProject(id);
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

	}
}
