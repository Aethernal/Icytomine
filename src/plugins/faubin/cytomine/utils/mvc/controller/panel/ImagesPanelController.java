package plugins.faubin.cytomine.utils.mvc.controller.panel;

import icy.system.thread.ThreadUtil;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;

import plugins.faubin.cytomine.Config;
import plugins.faubin.cytomine.utils.ConsoleUI;
import plugins.faubin.cytomine.utils.mvc.model.panel.ImagesPanelModel;
import plugins.faubin.cytomine.utils.mvc.template.Controller;
import plugins.faubin.cytomine.utils.mvc.view.panel.ImagesPanelView;
import be.cytomine.client.Cytomine;
import be.cytomine.client.collections.ImageInstanceCollection;
import be.cytomine.client.models.ImageInstance;

public class ImagesPanelController extends Controller {
	private ImagesPanelModel model;
	private ImagesPanelView view;

	private long idProjet;

	public ImagesPanelController(Cytomine cytomine, long idProjet,
			JTabbedPane tabbedPane) {
		super(tabbedPane);

		this.model = new ImagesPanelModel(cytomine, this);

		this.idProjet = idProjet;

		ImageInstanceCollection images = model.getImages(idProjet);
		this.view = new ImagesPanelView(idProjet, images, this);

		tabbedPane.add(view.getName(), view);
		tabbedPane.setSelectedComponent(view);

		ImageInstanceCollection collection = model.getImagesFromIndexByNb(
				idProjet, 0, Config.nbDisplayedImage);
		
		view.loadRows(collection);
	}

	public ImageIcon getImageIcon(ImageInstance imageInstance, int size) {
		return model.getIcon(imageInstance, size);
	}

	public void openImage(final long id) {
		ThreadUtil.invokeLater(new Runnable() {

			@Override
			public void run() {
				ImagePanelController imagePanel = new ImagePanelController(
						model.getCytomine(), id, tabbedPane);
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
				model.generateAndUploadSectionROI(model.getImages(idProjet));
			}
		});
	}

	public void deleteAllAnnotations() {
		ThreadUtil.bgRun(new Runnable() {
			@Override
			public void run() {
				model.deleteAllAnnotations(model.getImages(idProjet));
			}
		});

	}

	public void loadImageFromOffset(int offset, int length) {
		ImageInstanceCollection collection = model.getImagesFromIndexByNb(
				idProjet, offset, length);
		view.loadRows(collection);
	}

}
