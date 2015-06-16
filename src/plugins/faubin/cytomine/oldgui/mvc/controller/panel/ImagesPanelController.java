package plugins.faubin.cytomine.oldgui.mvc.controller.panel;

import icy.system.thread.ThreadUtil;

import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;

import plugins.faubin.cytomine.oldgui.mvc.model.panel.ImagesPanelModel;
import plugins.faubin.cytomine.oldgui.mvc.model.utils.Configuration;
import plugins.faubin.cytomine.oldgui.mvc.template.Controller;
import plugins.faubin.cytomine.oldgui.mvc.view.panel.ImagesPanelView;
import be.cytomine.client.Cytomine;
import be.cytomine.client.CytomineException;
import be.cytomine.client.collections.ImageInstanceCollection;
import be.cytomine.client.models.ImageInstance;

public class ImagesPanelController extends Controller {
	private ImagesPanelModel model;
	private ImagesPanelView view;

	private long ProjectID;
	static Configuration configuration = Configuration.getConfiguration();

	public ImagesPanelController(Cytomine cytomine, long idProjet,
			JTabbedPane tabbedPane) {
		super(tabbedPane);

		this.model = new ImagesPanelModel(cytomine, this);

		this.ProjectID = idProjet;

		ImageInstanceCollection images = model.getImages(idProjet);
		this.view = new ImagesPanelView(idProjet, images, this);

		ImageInstanceCollection collection = model.getImagesFromIndexByNb(
				idProjet, 0, configuration.nbRowPerPage);
		
		view.loadRows(collection);
	}

	public ImageIcon getImageIcon(ImageInstance imageInstance, int size) {
		return model.getIcon(imageInstance, size);
	}

	public void openImage(final long id) {
		ThreadUtil.invokeLater(new Runnable() {

			@Override
			public void run() {
				ImagePanelController imagePanel = new ImagePanelController(model.getCytomine(), ProjectID,id, tabbedPane);
				tabbedPane.add(imagePanel.getView().getName(), imagePanel.getView());
				tabbedPane.setSelectedComponent(imagePanel.getView());
				
			}
		});
	}

	@Override
	public void close() {
		tabbedPane.remove(view);
	}

	public void generateAndUploadSectionROI() {
		ThreadUtil.bgRun(new Runnable() {
			@Override
			public void run() {
				try {
					model.generateAndUploadSectionROI(model.getImages(ProjectID));
				} catch (CytomineException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	
	public void generateSectionAndGlomerules() {
		ThreadUtil.bgRun(new Runnable() {
			@Override
			public void run() {
				try {
					model.generateSectionAndGlomerules(model.getImages(ProjectID));
				} catch (CytomineException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	public void deleteAllAnnotations() {
		ThreadUtil.bgRun(new Runnable() {
			@Override
			public void run() {
				model.deleteAllAnnotations(model.getImages(ProjectID));
			}
		});

	}

	public void loadImageFromOffset(int offset, int length) {
		ImageInstanceCollection collection = model.getImagesFromIndexByNb(
				ProjectID, offset, length);
		view.loadRows(collection);
	}

	public ImagesPanelView getView() {
		return view;
	}
	
	public ImagesPanelModel getModel() {
		return model;
	}

	public void generateAndUploadGlomeruleROI() {
		ThreadUtil.bgRun(new Runnable() {
			@Override
			public void run() {
				try {
					model.generateGlomerule(model.getImages(ProjectID));
				} catch (CytomineException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	
	public long getProjectID() {
		return ProjectID;
	}
}
