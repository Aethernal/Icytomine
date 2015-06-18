package plugins.faubin.cytomine.module.tileViewer.toolbar;

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

import be.cytomine.client.CytomineException;
import plugins.faubin.cytomine.module.tileViewer.CytomineReader;

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
	private JButton btnCrop;
	
	CytomineReader reader;


	/**
	 * Create the frame.
	 */
	public Toolbar(CytomineReader reader) {
		super("Dynamic Viewer Toolbar", true, false);
		addToDesktopPane();
		
		this.reader = reader;
		reader.getSequence().addListener(sequenceListener);
		
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
		
		btnCrop = new JButton("Crop annotation");
		contentPane.add(btnCrop);
		btnCrop.addActionListener(actionCrop);
		
		btnLoad = new JButton("Load");
		contentPane.add(btnLoad);
		btnLoad.addActionListener(actionLoad);
		
		
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
	
	ActionListener actionReplace = new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent e) {
			reader.saveAnnotations(true);
		}
		
	};
	
	ActionListener actionUpload = new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent e) {
			reader.saveAnnotations(false);
		}
		
	};
	
	ActionListener actionLoad = new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				reader.loadAnnotations();
			} catch (CytomineException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	};
	
	ActionListener actionCrop = new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent e) {
			reader.cropAnnotation();
		}
		
	};
	
}
