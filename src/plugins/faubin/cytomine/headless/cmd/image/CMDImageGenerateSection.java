package plugins.faubin.cytomine.headless.cmd.image;

import icy.sequence.Sequence;

import java.util.List;

import plugins.faubin.cytomine.Config;
import plugins.faubin.cytomine.IcytomineUtil;
import plugins.faubin.cytomine.gui.roi.roi2dpolygon.CytomineImportedROI;
import plugins.faubin.cytomine.headless.Console;
import plugins.faubin.cytomine.headless.cmd.CMD;
import plugins.faubin.cytomine.headless.cmd.CMDAction;
import be.cytomine.client.models.ImageInstance;
import be.cytomine.client.models.User;

public class CMDImageGenerateSection extends CMD {

	public CMDImageGenerateSection(Console console) {
		super(console);

		String command = "generate_section";
		String description = "Generate the sections ROIs of the image and upload them to cytomine";
		String[] arguments = new String[] { "(long) projectID", "(long) imageID",
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
					long projectID = Long.parseLong(args[0]);
					long imageID = Long.parseLong(args[1]);
					try {
						final int maxSize = Integer.parseInt(args[1]);
						try {

							ImageInstance instance = console.cytomine
									.getImageInstance(imageID);
							try {
								System.out.println("generating ROIs ...");
								
								// downloading image
								Sequence seq = IcytomineUtil.loadImage(instance,
										console.cytomine, maxSize, null);
								// creating rois
								long idSoftware = Config.IDMap.get("SectionGenerationSoftware");
								User job = IcytomineUtil.generateNewUserJob(console.cytomine, idSoftware,projectID);
								
								IcytomineUtil
										.generateSectionsROI(console.cytomine, job,projectID, instance, seq, null);

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
