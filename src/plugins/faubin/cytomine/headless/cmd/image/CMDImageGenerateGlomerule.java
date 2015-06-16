package plugins.faubin.cytomine.headless.cmd.image;

import plugins.faubin.cytomine.headless.Console;
import plugins.faubin.cytomine.headless.cmd.CMD;
import plugins.faubin.cytomine.headless.cmd.CMDAction;
import plugins.faubin.cytomine.utils.Config;
import plugins.faubin.cytomine.utils.IcytomineUtil;
import be.cytomine.client.collections.ImageInstanceCollection;
import be.cytomine.client.models.ImageInstance;
import be.cytomine.client.models.User;

public class CMDImageGenerateGlomerule extends CMD {

	public CMDImageGenerateGlomerule(Console console) {
		super(console);

		String command = "generate_glomerules";
		String description = "Generate ROI of the glomerules for one image using already created sections and upload them to cytomine";
		String[] arguments = new String[] { "(long) imageID" };

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
						try {
							final ImageInstance collection = console.cytomine
									.getImageInstance(imageID);
							long projectID = collection.getLong("project");
							try{
								// variables
								System.out.println("generating ROIs ...");
	
									long idSection = Config.IDMap.get("SectionGenerationSoftware");
									long idGlomerule = Config.IDMap.get("GlomeruleGenerationSoftware");
									User jobSection = IcytomineUtil.generateNewUserJob(console.cytomine, idSection, projectID);
									User jobGlomerule = IcytomineUtil.generateNewUserJob(console.cytomine, idGlomerule, projectID);
									
									ImageInstance instance = collection;
								
									IcytomineUtil.generateGlomerule(console.cytomine, jobSection, jobGlomerule, instance, 2, null);
									
									System.gc();
									
								
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
