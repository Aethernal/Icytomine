package plugins.faubin.cytomine.module.tileViewer.roiconfiguration;

import icy.gui.frame.IcyFrame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import plugins.faubin.cytomine.utils.CytomineImportedROI;
import be.cytomine.client.collections.TermCollection;
import be.cytomine.client.models.Term;

public class RoiConfigurationPanel extends IcyFrame {

	
	private JPanel contentPane;
	private JTextField roiName;
	private CytomineImportedROI roi;
	
	/**
	 * Create the frame.
	 */
	public RoiConfigurationPanel(CytomineImportedROI roi, TermCollection terms) {
		super("Configuration", true, true, false, false);
		
		//frame initialisation
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(new Rectangle(100, 100, 200, 200));
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		roiName = new JTextField();
		roiName.setHorizontalAlignment(SwingConstants.CENTER);
		roiName.setText(roi.getName());
		contentPane.add(roiName, BorderLayout.NORTH);
		roiName.setColumns(10);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		scrollPane.setViewportView(panel);
		panel.setLayout(new GridLayout(terms.size(), 0, 0, 0));
		
		//variables initialisation
		this.roi = roi;
		
		//listeners
		roiName.addActionListener(nameChanged);
		
		for (int i = 0; i < terms.size(); i++) {
			Term term = terms.get(i);
			OntologySelectionOption option = new OntologySelectionOption(term, this);
			if(roi.terms.contains(term.getId())){
				option.setSelected(true);
			}
			panel.add(option);
		}
		
		addToDesktopPane();

	}
	
	public void optionFired(OntologySelectionOption option){
		if(option.isSelected()){
			roi.setColor(option.getColor());
			roi.terms.add(option.getID());
		}else{
			roi.setColor(Color.BLACK);
			roi.terms.remove(option.getID());
		}
	}
	
	ActionListener nameChanged = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			roi.setName(roiName.getText());
		}
	};

	
	
}
