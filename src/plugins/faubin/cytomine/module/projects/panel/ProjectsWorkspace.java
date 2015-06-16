package plugins.faubin.cytomine.module.projects.panel;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import net.sourceforge.jeval.operator.OpenParenthesesOperator;
import plugins.faubin.cytomine.module.main.mvc.View;
import plugins.faubin.cytomine.module.main.mvc.panel.Workspace;
import plugins.faubin.cytomine.module.projects.ProjectsView;
import be.cytomine.client.collections.ProjectCollection;
import be.cytomine.client.models.Project;

@SuppressWarnings("serial")
public class ProjectsWorkspace extends Workspace {
	
	private ProjectsView view;
	
	private JTable table;
	private NoEditProjectsModel tableModel;
	
	public ProjectsWorkspace(ProjectsView projectView) {
		super();
		this.view = projectView;
		
		table = new JTable();
		
		tableModel = new NoEditProjectsModel();
		RowSorter<NoEditProjectsModel> sorter = new TableRowSorter<NoEditProjectsModel>(tableModel);
		table.setModel(tableModel);
		
		String[] columns = { "ID", "Name", "Ontology ID", "Images", "User annotations" };
		tableModel.setColumnIdentifiers(columns);
		
		table.getTableHeader().setReorderingAllowed(false);
		table.setRowSorter(sorter);
		table.setGridColor(Color.GRAY);
		
		scroll.setViewportView(table);
		
		table.addMouseListener(mouseListener);
		table.addKeyListener(keyListener);
		setTable(getProjects());
	}
	
	private void addToTable(Project project){
		tableModel.addRow(
			new Object[] { 
				project.getLong("id"),
				project.getStr("name"),
				project.getLong("ontology"),
				project.getInt("numberOfImages"),
				project.getInt("numberOfAnnotations"), 
			}
		);
	}
	
	private void clearTable(){
		for (int i = tableModel.getRowCount() - 1; i >= 0; i--) {
			tableModel.removeRow(i);
		}
	}
	
	private void setTable(ProjectCollection projects){
		clearTable();
		for (int i = 0; i < projects.size(); i++) {
			addToTable(projects.get(i));
		}
	}
	
	public ProjectCollection getProjects(){
		return view.getProjects();
	}
	
	/**
	 * @return project ID or -1 if no row was selected
	 */
	public long getSelected(){
		int row = table.getSelectedRow();
		if(row == -1){
			return -1;
		}
		return (Long) tableModel.getValueAt(row, 0);
	}

	class NoEditProjectsModel extends DefaultTableModel {

		public boolean isCellEditable(int row, int column) {
			return false;
		}

		public Class<?> getColumnClass(int column) {
			Class<?> returnValue;
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

	@Override
	public View getView() {
		return view;
	}
	
	MouseAdapter mouseListener = new MouseAdapter() {
	    public void mousePressed(MouseEvent me) {
	        JTable table =(JTable) me.getSource();
	        Point p = me.getPoint();
	        int row = table.rowAtPoint(p);
	        if (me.getClickCount() == 2) {
	            long ID = (Long) tableModel.getValueAt(row, 0);
	            view.openProject(ID);
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
					long ID = (Long) tableModel.getValueAt(row, 0);
					view.openProject(ID);
				}
			}
			
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	};
	
}
