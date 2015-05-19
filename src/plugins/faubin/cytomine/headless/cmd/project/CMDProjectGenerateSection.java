package plugins.faubin.cytomine.headless.cmd.project;

import icy.sequence.Sequence;

import java.util.List;

import plugins.faubin.cytomine.Config;
import plugins.faubin.cytomine.headless.Console;
import plugins.faubin.cytomine.headless.cmd.CMD;
import plugins.faubin.cytomine.headless.cmd.CMDAction;
import plugins.faubin.cytomine.utils.cytomine.CytomineUtil;
import plugins.faubin.cytomine.utils.roi.roi2dpolygon.CytomineImportedROI;
import be.cytomine.client.collections.ImageInstanceCollection;
import be.cytomine.client.models.ImageInstance;

public class CMDProjectGenerateSection extends CMD {

	public CMDProjectGenerateSection(Console console) {
		super(console);

		String command = "generate_sections";
		String description = "Generate ROI of the sections for all the images in the project and upload them to cytomine";
		String[] arguments = new String[] { "(long) projectID", "(int) image_max_size" };

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
					try {
						final int maxSize = Integer.parseInt(args[1]);
						try {
							final ImageInstanceCollection collection = console.cytomine
									.getImageInstances(projectID);

							// variables
							int count = 0;
							System.out.println("generating ROIs ...");
							for (int i = 0; i < collection.size(); i++) {

								ImageInstance instance = collection.get(i);
								try {
									
									
									// downloading image
									Sequence seq = CytomineUtil
											.loadImage(instance,
													console.cytomine, maxSize, null);
									// creating rois
									List<CytomineImportedROI> rois = CytomineUtil
											.generateSectionsROI(seq, null);
									// adding roi to sequence
									for (int j = 0; j < rois.size(); j++) {
										seq.addROI(rois.get(j));
									}
									nbAnnotations += CytomineUtil.uploadRoi(
											console.cytomine, instance, seq, null);

								} catch (Exception e) {
									e.printStackTrace();
								}
								count++;
								System.out.println("\n" + count + " of "
										+ collection.size() + "\n");
							}

						} catch (Exception e) {
							System.out.println(Config.messages
									.get("project_get_failed"));
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
