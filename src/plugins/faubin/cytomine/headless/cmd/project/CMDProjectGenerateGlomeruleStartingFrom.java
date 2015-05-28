package plugins.faubin.cytomine.headless.cmd.project;

import plugins.faubin.cytomine.Config;
import plugins.faubin.cytomine.IcytomineUtil;
import plugins.faubin.cytomine.headless.Console;
import plugins.faubin.cytomine.headless.cmd.CMD;
import plugins.faubin.cytomine.headless.cmd.CMDAction;
import be.cytomine.client.collections.ImageInstanceCollection;
import be.cytomine.client.models.ImageInstance;

public class CMDProjectGenerateGlomeruleStartingFrom extends CMD {

	public CMDProjectGenerateGlomeruleStartingFrom(Console console) {
		super(console);

		String command = "generate_glomerules_offset";
		String description = "Generate ROI of the glomerules for all the images in the project using already created sections and upload them to cytomine";
		String[] arguments = new String[] { "(long) projectID", "(int) offset" };

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
					int offset = Integer.parseInt(args[1]);
					try {
						try {
							final ImageInstanceCollection collection = console.cytomine
									.getImageInstances(projectID);
							try{
								// variables
								int count = offset;
								System.out.println("generating ROIs ...");
								for (int i = offset; i < collection.size(); i++) {
	
									ImageInstance instance = collection.get(i);
								
									IcytomineUtil.generateGlomerule(console.cytomine, instance, 2, null);
									
									System.gc();
									
									count++;
									System.out.println("\n" + count + " of "
											+ collection.size() + "\n");
								}
							}catch(Exception e){
								e.printStackTrace();
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
