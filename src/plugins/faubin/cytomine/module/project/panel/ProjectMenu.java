package plugins.faubin.cytomine.module.project.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import plugins.faubin.cytomine.module.main.IcytomineFrame;
import plugins.faubin.cytomine.module.main.mvc.panel.Menu;
import plugins.faubin.cytomine.module.project.ProjectView;

public class ProjectMenu extends Menu {
	private ProjectView view;
	
	/*------------------------------------------*/
	/* Buttons
	/*------------------------------------------*/
	private JButton sections;
	private JButton glomerules;
	private JButton sectAndGlom;
	private JButton uploadThumbnail;
	private JButton cropSection;
	
	public ProjectMenu(ProjectView view) {
		super();
		this.view = view;
		

		/*------------------------------------------*/
		/* definition
		/*------------------------------------------*/
		sections = new JButton("Sections detection");
		glomerules = new JButton("Glomerules detection");
		sectAndGlom = new JButton("Section and Glomerules detection");
		uploadThumbnail = new JButton("Upload thumbnail");
		cropSection = new JButton("Crop sections");
		
		/* -----------------------------------------*/
		/* add to menu
		/*------------------------------------------*/
		add(sections);
		add(glomerules);
		add(sectAndGlom);
		add(uploadThumbnail);
		add(cropSection);
		
		/*------------------------------------------*/
		/* add listeners
		/*------------------------------------------*/
		sections.addActionListener(sectionDetection);
		glomerules.addActionListener(glomeruleDetection);
		sectAndGlom.addActionListener(sectglomDetection);
		uploadThumbnail.addActionListener(uploadThumbnailAction);
		cropSection.addActionListener(cropSectionAction);
		
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
	/*-------------------------------------------*/
	ActionListener sectglomDetection = new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent e) {
			view.sectglomDetection();
			
		}
		
	};
	/*-------------------------------------------*/
	ActionListener uploadThumbnailAction = new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent e) {
			view.uploadThumbnail();
			
		}
		
	};
	/*-------------------------------------------*/
	ActionListener cropSectionAction = new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent e) {
			view.cropSection();
			
		}
		
	};
	
}
