package plugins.faubin.cytomine.module.projects.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import plugins.faubin.cytomine.module.main.mvc.panel.Menu;
import plugins.faubin.cytomine.module.projects.ProjectsView;

public class ProjectsMenu extends Menu {
	private ProjectsView view;
	
	private JButton sections;
	private JButton glomerules;
	
	public ProjectsMenu(ProjectsView view) {
		super();
		this.view = view;
		
		/*------------------------------------------*/
		/* definition
		/*------------------------------------------*/
		sections = new JButton("Sections detection");
		glomerules = new JButton("Glomerules detection");
		
		/* -----------------------------------------*/
		/* add to menu
		/*------------------------------------------*/
		add(sections);
		add(glomerules);
		
		/*------------------------------------------*/
		/* add listeners
		/*------------------------------------------*/
		sections.addActionListener(sectionDetection);
		glomerules.addActionListener(glomeruleDetection);
		
		
	}
	
	/*------------------------------------------*/
	/* Listeners
	/*------------------------------------------*/
	ActionListener sectionDetection = new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent e) {
			view.sectionDetection();
			
		}
		
	};
	/*------------------------------------------*/
	ActionListener glomeruleDetection = new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent e) {
			view.glomeruleDetection();
			
		}
		
	};
	
	
}
