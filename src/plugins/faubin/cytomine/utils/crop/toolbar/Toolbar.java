package plugins.faubin.cytomine.utils.crop.toolbar;

import icy.gui.frame.IcyFrame;
import icy.sequence.Sequence;
import icy.sequence.SequenceEvent;
import icy.sequence.SequenceListener;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.GridLayout;

import javax.swing.JButton;

import be.cytomine.client.Cytomine;
import be.cytomine.client.CytomineException;
import plugins.faubin.cytomine.module.tileViewer.CytomineReader;
import plugins.faubin.cytomine.utils.crop.CytomineCrop;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;

public class Toolbar extends IcyFrame {

	private JPanel contentPane;
	
	private JButton btnUpload;
	private JButton btnReplace;
	private JButton btnLoad;
	private JButton btnLocal;
	
	CytomineCrop crop;
	Cytomine cytomine;

	/**
	 * Create the frame.
	 */
	public Toolbar(CytomineCrop crop, Cytomine cytomine) {
		super("Cytomine Crop Toolbar", true, false);
		
		this.crop = crop;
		crop.getSequence().addListener(sequenceListener);
		this.cytomine = cytomine;
		
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(new Rectangle(100, 100, 450, 100));
		contentPane = new JPanel();
		contentPane.setBackground(Color.DARK_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(1, 0, 0, 0));
		
		btnUpload = new JButton("Upload");
		contentPane.add(btnUpload);
		btnUpload.addActionListener(actionUpload);
		
		btnReplace = new JButton("Replace");
		contentPane.add(btnReplace);
		btnReplace.addActionListener(actionReplace);
		
		btnLocal = new JButton("Save to local");
		contentPane.add(btnLocal);
		btnLocal.addActionListener(actionSaveLocal);
		
		btnLoad = new JButton("Load");
		contentPane.add(btnLoad);
		
		addToDesktopPane();
		setVisible(true);
	}
	
	SequenceListener sequenceListener = new SequenceListener() {
		
		@Override
		public void sequenceClosed(Sequence sequence) {
			dispose();
		}
		
		@Override
		public void sequenceChanged(SequenceEvent sequenceEvent) {
		}
	};
	
	/* -- ActionListener -- */
	
	ActionListener actionUpload = new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent e) {
			crop.upload(cytomine);
		}
		
	};
	
	ActionListener actionReplace = new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent e){
			crop.replace(cytomine);
		}
	};
	
	ActionListener actionSaveLocal = new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent e){
			crop.localSave();
		}
	};
	
	ActionListener actionLoad = new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent e){
			crop.load(cytomine);
		}

	};
}
