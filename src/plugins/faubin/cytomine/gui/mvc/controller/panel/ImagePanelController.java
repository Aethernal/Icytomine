package plugins.faubin.cytomine.gui.mvc.controller.panel;

import icy.system.thread.ThreadUtil;

import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;

import plugins.faubin.cytomine.gui.mvc.model.panel.ImagePanelModel;
import plugins.faubin.cytomine.gui.mvc.template.Controller;
import plugins.faubin.cytomine.gui.mvc.view.frame.TermFrame;
import plugins.faubin.cytomine.gui.mvc.view.panel.ImagePanelView;
import plugins.faubin.cytomine.gui.tileViewer.CytomineReader;
import be.cytomine.client.Cytomine;
import be.cytomine.client.models.ImageInstance;

public class ImagePanelController extends Controller {
	private ImagePanelModel model;
	private ImagePanelView view;
	private TermFrame terms;

	public ImagePanelController(Cytomine cytomine, long idImage,
			JTabbedPane tabbedPane) {
		super(tabbedPane);

		this.model = new ImagePanelModel(cytomine, this);

		ImageInstance image = model.getImage(idImage);
		this.view = new ImagePanelView(idImage, image, this);

		try {
			Long projectOntologyID = cytomine.getProject(
					image.getLong("project")).getLong("ontology");
			this.terms = new TermFrame(
					cytomine.getTermsByOntology(projectOntologyID));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public ImageIcon getImageIcon(ImageInstance imageInstance) {
		return model.getIcon(imageInstance);
	}

	public void deleteRoi(final ImageInstance instance) {
		ThreadUtil.bgRun(new Runnable() {

			@Override
			public void run() {
				model.deleteRoi(instance);
			}
		});
	}

	public void openInIcy(final ImageInstance instance) {
		ThreadUtil.bgRun(new Runnable() {

			@Override
			public void run() {
				model.openInIcy(instance);
			}
		});
	}

	public void openInIcyWithAnnotations(final ImageInstance instance) {
		ThreadUtil.bgRun(new Runnable() {

			@Override
			public void run() {
				model.openInIcyWithAnnotations(instance);
			}
		});
	}

	public void showTermsFrame() {
		terms.setVisible(true);
	}

	@Override
	public void close() {
		tabbedPane.remove(view);
	}

	public void generateSection(final ImageInstance instance) {
		ThreadUtil.bgRun(new Runnable() {
			@Override
			public void run() {
				model.generateSection(instance);
			}
		});

	}

	public void UpdateRoi(final ImageInstance instance) {
		ThreadUtil.bgRun(new Runnable() {

			@Override
			public void run() {
				model.updateRoi(instance, terms.getSelectedTerms());
			}

		});
	}

	public void uploadRoi(final ImageInstance instance) {

		ThreadUtil.bgRun(new Runnable() {

			@Override
			public void run() {
				model.uploadRoi(instance, terms.getSelectedTerms());
			}

		});
	}

	public int getMaxSize() {
		return view.getMaxSize();
	}

	public void generateGlomerule(final ImageInstance instance) {
		ThreadUtil.bgRun(new Runnable() {

			@Override
			public void run() {
				model.generateGlomerule(instance);
			}

		});

	}

	public Cytomine getCytomine() {
		return model.getCytomine();
	}
	
	public ImagePanelView getView() {
		return view;
	}
	
	public ImagePanelModel getModel() {
		return model;
	}

}
