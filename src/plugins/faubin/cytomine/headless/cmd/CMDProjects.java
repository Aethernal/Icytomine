package plugins.faubin.cytomine.headless.cmd;

import icy.main.Icy;
import plugins.faubin.cytomine.Config;
import plugins.faubin.cytomine.headless.Console;
import be.cytomine.client.Cytomine;
import be.cytomine.client.collections.ProjectCollection;


public class CMDProjects extends CMD{
	
	public CMDProjects(Console console) {
		super(console);
		
		String command = "projects";
		String description = "Show all project the user has access; formated like: ID name ontologyID numberOfImage numberOfAnnotations";
		String[] arguments = new String[]{};
		
		super.initialize(command, description, arguments);
		
	}
	
	@Override
	public Object execute(final String[] args){
		
		//generating action with arguments
		CMDAction action = new CMDAction(args) {
			
			@Override
			public Object call(){
				StringBuffer str = new StringBuffer();
				try {
					ProjectCollection collection = console.cytomine.getProjects();
					str.append("Listing all projects"+"\n");

					// variables
					long ID;
					String name;
					long ontology;
					int nbImages;
					int nbAnnotations;

					// listing
					for (int i = 0; i < collection.size(); i++) {
						ID = collection.get(i).getLong("id");
						name = collection.get(i).getStr("name");
						ontology = collection.get(i).getLong("ontology");
						nbImages = collection.get(i).getInt("numberOfImages");
						nbAnnotations = collection.get(i).getInt(
								"numberOfAnnotations");

						str.append("\t" + ID + " " + name + " " + ontology
								+ " " + nbImages + " " + nbAnnotations + "\n");
						
					}
					str.append("Result found: " + collection.size());

				} catch (Exception e) {
					e.printStackTrace();
				}
				
				return str.toString();
			}
		};
		
		//update and execute command
		super.updateAction(action);
		Object returned = super.execute(args);
		
		if(returned != null ){
			System.out.println((String) returned);
		}
		
		return null;
	}
	
}
