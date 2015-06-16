package plugins.faubin.cytomine.module.tileViewer.roiconfiguration;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import plugins.faubin.cytomine.utils.IcytomineUtil;
import be.cytomine.client.models.Term;

@SuppressWarnings("serial")
public class OntologySelectionOption extends JPanel {

	private long ID;
	private String nom;
	private JCheckBox checkBox;
	private JPanel color;
	
	private RoiConfigurationPanel menu;
	
	public OntologySelectionOption(Term term, RoiConfigurationPanel roiConfigurationPanel) {
		
		//panel initialisation
		setLayout(new GridLayout(0, 2, 0, 0));
		
		checkBox = new JCheckBox("term");
		checkBox.addActionListener(checkBoxListener);
		add(checkBox);
		
		color = new JPanel();
		color.setBackground(Color.LIGHT_GRAY);
		add(color);
		
		//variables initialisation
		this.nom = term.getStr("name");
		this.ID = term.getId();
		this.color.setBackground(IcytomineUtil.hexToColor(term.getStr("color")));
		
		checkBox.setText(nom);
		
		this.menu = roiConfigurationPanel;
	}

	public boolean isSelected(){
		return checkBox.isSelected();
	}
	
	public void setSelected(boolean value){
		checkBox.setSelected(value);
	}
	
	public String getNom() {
		return nom;
	}
	
	public long getID() {
		return ID;
	}
	
	public Color getColor(){
		return color.getBackground();
	}
	
	ActionListener checkBoxListener = new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent e) {
			menu.optionFired(OntologySelectionOption.this);
			
		}
		
	};
}
