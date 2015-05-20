package plugins.faubin.cytomine.utils.mvc.view.panel;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.DefaultCaret;

import plugins.faubin.cytomine.Config;
import plugins.faubin.cytomine.utils.mvc.controller.panel.ImagePanelController;
import utils.CytomineReader;
import be.cytomine.client.CytomineException;
import be.cytomine.client.models.ImageInstance;

@SuppressWarnings("serial")
public class ImagePanelView extends JPanel {

	private ImagePanelController controller;
	private ImageInstance instance;
	private JTextField maxSize;
	private CytomineReader preview;
	
	/**
	 * Create the panel.
	 */
	public ImagePanelView(long idProj, ImageInstance image,
			ImagePanelController controller) {
		this.controller = controller;
		this.instance = image;

		setName("Image: " + idProj + "   ");
		setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		add(panel, BorderLayout.CENTER);
		panel.setLayout(new GridLayout());

		JScrollPane info_scroll = new JScrollPane();
		info_scroll.setBounds(0, 0, 223, 274);
		panel.add(info_scroll);

		try {
			preview = new CytomineReader(controller.getCytomine(), getInstance(),
					getSize());
			preview.setBounds(0, 0, 400, 400);

		} catch (CytomineException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		DefaultCaret caret;

		JPanel informations = new JPanel();
		informations.setLayout(new GridLayout(0, 1, 0, 0));

		JLabel lblFilename = new JLabel("ID");
		informations.add(lblFilename);

		JTextPane lblValueid = new JTextPane();
		lblValueid.setEditable(false);
		informations.add(lblValueid);

		caret = (DefaultCaret) lblValueid.getCaret();
		caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);

		JLabel lblName = new JLabel("Name");
		informations.add(lblName);

		JTextPane lblValuename = new JTextPane();
		lblValuename.setEditable(false);
		informations.add(lblValuename);

		caret = (DefaultCaret) lblValuename.getCaret();
		caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);

		JLabel lblUser = new JLabel("User");
		informations.add(lblUser);

		JTextPane lblValueuser = new JTextPane();
		lblValueuser.setEditable(false);
		informations.add(lblValueuser);

		caret = (DefaultCaret) lblValueuser.getCaret();
		caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);

		JLabel lblAnnotations = new JLabel("Annotations");
		informations.add(lblAnnotations);

		JTextPane lblValueannotation = new JTextPane();
		lblValueannotation.setEditable(false);
		informations.add(lblValueannotation);

		caret = (DefaultCaret) lblValueannotation.getCaret();
		caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);

		JLabel lblWidth = new JLabel("Width");
		informations.add(lblWidth);

		JTextPane lblValuewidth = new JTextPane();
		lblValuewidth.setEditable(false);
		informations.add(lblValuewidth);

		caret = (DefaultCaret) lblValuewidth.getCaret();
		caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);

		JLabel lblHeight = new JLabel("Height");
		informations.add(lblHeight);

		JTextPane lblValueheight = new JTextPane();
		lblValueheight.setEditable(false);
		informations.add(lblValueheight);

		caret = (DefaultCaret) lblValueheight.getCaret();
		caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);

		// inserting data

		lblValueid.setText(image.getStr("id"));
		lblValuename.setText(image.getStr("originalFilename"));
		lblValueuser.setText(image.getStr("user"));
		lblValueannotation.setText(image.getStr("numberOfAnnotations"));
		lblValuewidth.setText(image.getStr("width"));
		lblValueheight.setText(image.getStr("height"));

		info_scroll.setViewportView(informations);
		JMenuBar menuBar = new JMenuBar();
		add(menuBar, BorderLayout.NORTH);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenu mnOpen = new JMenu("Import");
		mnFile.add(mnOpen);

		JMenuItem mntmOpenInIcy = new JMenuItem("Image");
		mntmOpenInIcy.setToolTipText("import the image from Cytomine to Icy");
		mnOpen.add(mntmOpenInIcy);

		JMenuItem mntmOpenInIcyWithAnnos = new JMenuItem(
				"Image with Annotations");
		mntmOpenInIcyWithAnnos
				.setToolTipText("import the image from Cytomine to Icy with user annotations");
		mnOpen.add(mntmOpenInIcyWithAnnos);

		JSeparator separator = new JSeparator();
		mnOpen.add(separator);

		JLabel lblImageMaxSize = new JLabel("Image Max Size");
		lblImageMaxSize
				.setToolTipText("define the maximum size of the imported images, other function might use this value");
		mnOpen.add(lblImageMaxSize);

		maxSize = new JTextField();
		maxSize.setText("" + Config.thumbnailDefaultMaxSize);
		mnOpen.add(maxSize);
		maxSize.setColumns(10);

		JMenu mnSave = new JMenu("Export");
		mnFile.add(mnSave);

		JMenuItem mntmUpdate = new JMenuItem("Update");
		mntmUpdate
				.setToolTipText("delete all the previouss annotations from the image and upload the one inside the curently active sequence with the selected terms");
		mntmUpdate.addActionListener(actionUpdate);
		mnSave.add(mntmUpdate);

		JMenuItem mntmUpload = new JMenuItem("Upload");
		mntmUpload
				.setToolTipText("upload all ROIs from the currently active sequence to cytomine with the selected terms");
		mntmUpload.addActionListener(actionUpload);

		mnSave.add(mntmUpload);
		mntmOpenInIcyWithAnnos
				.addActionListener(actionOpenInIcyWithAnnotations);
		mntmOpenInIcy.addActionListener(actionOpenInIcy);

		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);

		JMenu mnRoi = new JMenu("ROI");
		mnEdit.add(mnRoi);

		JMenu mnGenerate = new JMenu("Generate");

		mnRoi.add(mnGenerate);

		JMenuItem mntmSections = new JMenuItem("Sections");
		mntmSections
				.setToolTipText("generate sections ROIs from the current image and then output the result on a new Sequence");
		mntmSections.addActionListener(actionGenerateSection);
		mnGenerate.add(mntmSections);

		JMenuItem mntmGlomerules = new JMenuItem("Glomerules");
		mntmGlomerules.addActionListener(actionGenerateGlomerule);
		mnGenerate.add(mntmGlomerules);
		
		JMenu mnDelete = new JMenu("Delete");
		mnRoi.add(mnDelete);
		
		JMenuItem mntmSections_1 = new JMenuItem("Sections");
		mnDelete.add(mntmSections_1);
		
		JMenuItem mntmGlomerules_1 = new JMenuItem("Glomerules");
		mnDelete.add(mntmGlomerules_1);
		
				JMenuItem mntmDeleteRoi = new JMenuItem("All");
				mnDelete.add(mntmDeleteRoi);
				mntmDeleteRoi
						.setToolTipText("delete all roi of the current user from the image");
				mntmDeleteRoi.addActionListener(actionDeleteRoi);

		JMenuItem mntmTerm = new JMenuItem("Term");
		mntmTerm.setToolTipText("show a frame that allow to select which terms are going to be attribued to the ROIs");
		mntmTerm.addActionListener(actionShowTerms);
		mnEdit.add(mntmTerm);

	}

	public int getMaxSize() {
		int i = 2048;
		try {
			i = Integer.parseInt(maxSize.getText());
		} catch (Exception e) {

		}
		return i;
	}

	public ImageInstance getInstance() {
		return instance;
	}

	public ActionListener actionGenerateSection = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			controller.generateSection(getInstance());

		}

	};

	public ActionListener actionGenerateGlomerule = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			controller.generateGlomerule(getInstance(),preview);

		}

	};

	public ActionListener actionDeleteRoi = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			controller.deleteRoi(getInstance());
		}

	};

	public ActionListener actionUpdate = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			controller.UpdateRoi(getInstance());
		}

	};

	public ActionListener actionUpload = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			controller.uploadRoi(getInstance());
		}

	};

	public ActionListener actionShowTerms = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			controller.showTermsFrame();
		}

	};

	public ActionListener actionOpenInIcy = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			controller.openInIcy(getInstance());
		}

	};

	public ActionListener actionOpenInIcyWithAnnotations = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			controller.openInIcyWithAnnotations(getInstance());
		}

	};

	public ActionListener actionClose = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			controller.close();
		}

	};

}
