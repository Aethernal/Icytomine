package plugins.faubin.cytomine.oldgui.mvc.view.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;

import plugins.faubin.cytomine.oldgui.mvc.controller.panel.ProjectsPanelController;
import be.cytomine.client.collections.ProjectCollection;

@SuppressWarnings("serial")
public class ProjectsPanelView extends JPanel {

	private ProjectsPanelController controller;

	private JTable table;

	private NotEditableTableModel model;

	Color cellColor;

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

		table = new JTable(model) {

			@Override
			public Component prepareRenderer(TableCellRenderer renderer,
					int index_row, int index_col) {
				Component comp = super.prepareRenderer(renderer, index_row,
						index_col);

				if (index_row >= 0) {
					// comp.setBackground(cellColor);
				} else {
					// comp.setBackground(Color.CYAN);
				}

				if (isCellSelected(index_row, index_col)) {
					// comp.setBackground(new Color(0, 0, 112));

				}

				return comp;
			}

		};

		table.getSelectionModel().addListSelectionListener(tableListener);

		RowSorter<NotEditableTableModel> sorter = new TableRowSorter<NotEditableTableModel>(
				model);

		scrollPane.setViewportView(table);

		// add columns to table

		String[] columns = { "ID", "Name", "Ontology ID", "Images",
				"User annotations" };

		model.setColumnIdentifiers(columns);

		table.getTableHeader().setReorderingAllowed(false);

		for (int i = 0; i < projects.size(); i++) {
			model.addRow(new Object[] { projects.get(i).getLong("id"),
					projects.get(i).getStr("name"),
					projects.get(i).getLong("ontology"),
					projects.get(i).getInt("numberOfImages"),
					projects.get(i).getInt("numberOfAnnotations"), });
		}

		table.setRowSorter(sorter);

		table.setSelectionBackground(Color.RED);

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

		@SuppressWarnings({ "unchecked", "rawtypes" })
		public Class getColumnClass(int column) {
			Class returnValue;
			switch (column) {

			case 0:
				returnValue = Long.class;
				break;
			case 1:
				returnValue = String.class;
				break;
			case 2:
				returnValue = Long.class;
				break;
			case 3:
				returnValue = Integer.class;
				break;
			case 4:
				returnValue = Integer.class;
				break;
			default:
				returnValue = Object.class;
				break;
			}
			return returnValue;
		}

	}
}
