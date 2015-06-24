package plugins.faubin.cytomine.headless.cmd.image;

import icy.sequence.Sequence;
import plugins.faubin.cytomine.headless.Console;
import plugins.faubin.cytomine.headless.cmd.CMD;
import plugins.faubin.cytomine.headless.cmd.CMDAction;
import plugins.faubin.cytomine.utils.Config;
import plugins.faubin.cytomine.utils.Configuration;
import plugins.faubin.cytomine.utils.IcytomineUtil;
import plugins.faubin.cytomine.utils.software.SoftwareGlomeruleFinder;
import plugins.faubin.cytomine.utils.software.SoftwareSectionFinder;
import be.cytomine.client.Cytomine;
import be.cytomine.client.collections.ImageInstanceCollection;
import be.cytomine.client.models.ImageInstance;
import be.cytomine.client.models.User;

public class CMDImageGenerateSectionAndGlomerule extends CMD {

	Configuration configuration = Configuration.getConfiguration();
	
	public CMDImageGenerateSectionAndGlomerule(Console console) {
		super(console);

		String command = "generate_sections_glomerules";
		String description = "Generate ROI of the glomerules for one image by creating section annotation at first";
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
				int count = 0;
				try {
					long imageID = Long.parseLong(args[0]);
					
					try {
						try {
							final ImageInstance instances = console.cytomine
									.getImageInstance(imageID);
							long projectID = instances.getLong("project");
							try{
								// variables
								System.out.println("generating ROIs ...");
									
								IcytomineUtil.createSectionSoftware(console.cytomine,projectID);
								IcytomineUtil.createGlomeruleSoftware(console.cytomine,projectID);
								
									long idSection = configuration.softwareID.get(console.cytomine.getHost()).get(new SoftwareSectionFinder().getName()).ID;
									long idGlomerule = configuration.softwareID.get(console.cytomine.getHost()).get(new SoftwareGlomeruleFinder().getName()).ID;
									User jobSection = IcytomineUtil.generateNewUserJob(console.cytomine, idSection, projectID);
									
									console.cytomine.changeStatus(jobSection.getLong("job"), Cytomine.JobStatus.RUNNING, 0);
									
									ImageInstance instance = instances;

									Sequence sequence = IcytomineUtil.loadImage(instance, console.cytomine, configuration.thumbnailMaxSize, null);

									System.out.println("starting section detection");
									
									IcytomineUtil.generateSectionsROI(console.cytomine, jobSection, projectID,instance, 
											sequence, null);
									
									System.out.println("section detection done");
									
									console.cytomine.changeStatus(jobSection.getLong("job"), Cytomine.JobStatus.SUCCESS, 100);
									
									IcytomineUtil.sleep(1000);
									
									User jobGlomerule = IcytomineUtil.generateNewUserJob(console.cytomine, idGlomerule, projectID);
									
									System.out.println("starting glomerule detection");
									
									IcytomineUtil.generateGlomerule(console.cytomine, jobSection, jobGlomerule, instance, 2, null);
									
									System.out.println("glomerule detection done");
									
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
