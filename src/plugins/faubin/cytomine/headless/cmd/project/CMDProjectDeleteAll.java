package plugins.faubin.cytomine.headless.cmd.project;

import plugins.faubin.cytomine.Config;
import plugins.faubin.cytomine.IcytomineUtil;
import plugins.faubin.cytomine.headless.Console;
import plugins.faubin.cytomine.headless.cmd.CMD;
import plugins.faubin.cytomine.headless.cmd.CMDAction;
import be.cytomine.client.collections.ImageInstanceCollection;
import be.cytomine.client.models.ImageInstance;


public class CMDProjectDeleteAll extends CMD{
	
	public CMDProjectDeleteAll(Console console) {
		super(console);
		
		String command = "delete_annotations_all";
		String description = "Delete all annotations in the project";
		String[] arguments = new String[]{"(long) projectID"};
		
		super.initialize(command, description, arguments);
		
	}
	
	@Override
	public Object execute(String[] args){
		
		//generating action with arguments
		CMDAction action = new CMDAction(args) {
			
			@Override
			public Object call(){
				int nbAnnotations = 0;
				try {
					long projectID = Long.parseLong(args[0]);
					try {
						ImageInstanceCollection collection = console.cytomine
								.getImageInstances(projectID);
						int count = 0;
						for (int i = 0; i < collection.size(); i++) {
							ImageInstance instance = collection.get(i);

							nbAnnotations += IcytomineUtil.deleteAllRoi(
									console.cytomine, instance, null);
							count++;
							System.out.println(count+ " of " + collection.size());
						}
					} catch (Exception e) {
						System.out.println(Config.messages.get("project_get_failed"));
					}
				} catch (NumberFormatException e) {
					System.out.println(Config.messages.get("args_invalid_type"));
				}
				return nbAnnotations;
			}
		};
		
		//update and execute command
		super.updateAction(action);
		Object returned = super.execute(args);
		
		if(returned != null ){
			System.out.println( (Integer)returned + " annotations were removed !");
		}
		
		return null;
	}
	
}
