package plugins.faubin.cytomine.headless.cmd.project;

import icy.sequence.Sequence;
import plugins.faubin.cytomine.headless.Console;
import plugins.faubin.cytomine.headless.cmd.CMD;
import plugins.faubin.cytomine.headless.cmd.CMDAction;
import plugins.faubin.cytomine.oldgui.mvc.model.utils.Configuration;
import plugins.faubin.cytomine.utils.Config;
import plugins.faubin.cytomine.utils.IcytomineUtil;
import be.cytomine.client.Cytomine;
import be.cytomine.client.collections.ImageInstanceCollection;
import be.cytomine.client.models.ImageInstance;
import be.cytomine.client.models.User;

public class CMDProjectGenerateSectionAndGlomerule extends CMD {

	Configuration configuration = Configuration.getConfiguration();
	
	public CMDProjectGenerateSectionAndGlomerule(Console console) {
		super(console);

		String command = "generate_sections_glomerules";
		String description = "Generate ROI of the glomerules for all the images in the project by created section roi at first";
		String[] arguments = new String[] { "(long) projectID" };

		super.initialize(command, description, arguments);

	}

	@Override
	public Object execute(final String[] args) {

		// generating action with arguments
		CMDAction action = new CMDAction(args) {

			@Override
			public Object call() {
				int nbAnnotations = 0;
				int count = 0;
				try {
					long projectID = Long.parseLong(args[0]);
					try {
						try {
							final ImageInstanceCollection instances = console.cytomine
									.getImageInstances(projectID);
							try{
								// variables
								System.out.println("generating ROIs ...");
								for (int i = 0; i < instances.size(); i++) {
									count ++;
									
									System.out.println(count + " of " + instances.size());
									long idSection = Config.IDMap.get("SectionGenerationSoftware");
									User jobSection = IcytomineUtil.generateNewUserJob(console.cytomine, idSection, projectID);
									
									console.cytomine.changeStatus(jobSection.getLong("job"), Cytomine.JobStatus.RUNNING, 0);
									
									ImageInstance instance = instances.get(i);

									Sequence sequence = IcytomineUtil.loadImage(instance, console.cytomine, configuration.iconPreviewMaxSize, null);

									System.out.println("starting section detection");
									
									IcytomineUtil.generateSectionsROI(console.cytomine, jobSection, projectID,instance, 
											sequence, null);
									
									System.out.println("section detection done");
									
									console.cytomine.changeStatus(jobSection.getLong("job"), Cytomine.JobStatus.SUCCESS, 100);
									
									IcytomineUtil.sleep(1000);
									
									long idGlomerule = Config.IDMap.get("GlomeruleGenerationSoftware");
									User jobGlomerule = IcytomineUtil.generateNewUserJob(console.cytomine, idGlomerule, projectID);
									
									System.out.println("starting glomerule detection");
									
									IcytomineUtil.generateGlomerule(console.cytomine, jobSection, jobGlomerule, instance, 2, null);
									
									System.out.println("glomerule detection done");
									
									System.gc();
									
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
