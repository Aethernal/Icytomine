package plugins.faubin.cytomine.headless.cmd.image;

import icy.sequence.Sequence;

import java.util.List;

import plugins.faubin.cytomine.Config;
import plugins.faubin.cytomine.headless.Console;
import plugins.faubin.cytomine.headless.cmd.CMD;
import plugins.faubin.cytomine.headless.cmd.CMDAction;
import plugins.faubin.cytomine.utils.cytomine.CytomineUtil;
import plugins.faubin.cytomine.utils.roi.roi2dpolygon.CytomineImportedROI;
import be.cytomine.client.models.ImageInstance;

public class CMDImageGenerateSection extends CMD {

	public CMDImageGenerateSection(Console console) {
		super(console);

		String command = "generate_section";
		String description = "Generate the sections ROIs of the image and upload them to cytomine";
		String[] arguments = new String[] { "(long) imageID",
				"(int) image_max_size" };

		super.initialize(command, description, arguments);

	}

	@Override
	public Object execute(final String[] args) {

		// generating action with arguments
		CMDAction action = new CMDAction(args) {

			@Override
			public Object call() {
				int nbAnnotations = 0;
				try {
					long imageID = Long.parseLong(args[0]);
					try {
						final int maxSize = Integer.parseInt(args[1]);
						try {

							ImageInstance instance = console.cytomine
									.getImageInstance(imageID);
							try {
								System.out.println("generating ROIs ...");
								
								// downloading image
								Sequence seq = CytomineUtil.loadImage(instance,
										console.cytomine, maxSize, null);
								// creating rois
								List<CytomineImportedROI> rois = CytomineUtil
										.generateSectionsROI(seq, null);
								// adding roi to sequence
								for (int j = 0; j < rois.size(); j++) {
									seq.addROI(rois.get(j));
								}
								nbAnnotations += CytomineUtil.uploadROI(
										console.cytomine, instance, seq, null);

							} catch (Exception e) {
								e.printStackTrace();
							}

						} catch (Exception e) {
							System.out.println(Config.messages
									.get("image_get_failed"));
						}
					} catch (NumberFormatException e) {
						System.out.println(Config.messages
								.get("args_invalid_type"));
					}
				} catch (NumberFormatException e) {
					System.out
							.println(Config.messages.get("args_invalid_type"));
				}
				return nbAnnotations;
			}
		};

		// update and execute command
		super.updateAction(action);
		Object returned = super.execute(args);

		if (returned != null) {
			System.out.println((Integer) returned
					+ " annotations were created !");
		}

		return null;
	}

}
