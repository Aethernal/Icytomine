package plugins.faubin.cytomine.headless.cmd.project;

import plugins.faubin.cytomine.Config;
import plugins.faubin.cytomine.headless.Console;
import plugins.faubin.cytomine.headless.cmd.CMD;
import plugins.faubin.cytomine.headless.cmd.CMDAction;
import be.cytomine.client.collections.ImageInstanceCollection;
import be.cytomine.client.models.ImageInstance;

public class CMDProjectList extends CMD {

	public CMDProjectList(Console console) {
		super(console);

		String command = "list";
		String description = "List all images from the project";
		String[] arguments = new String[] { "(long) projectID" };

		super.initialize(command, description, arguments);

	}

	@Override
	public Object execute(final String[] args) {

		// generating action with arguments
		CMDAction action = new CMDAction(args) {

			@Override
			public Object call() {
				StringBuffer str = new StringBuffer();
				try {
					long idProject = Long.parseLong(args[0]);
					try {
						ImageInstanceCollection collection = console.cytomine
								.getImageInstances(idProject);

						str.append("Listing all image for project " + idProject);

						// variables
						long ID;
						String filename;
						long user;
						int nbAnnotations;
						int width;
						int height;

						for (int i = 0; i < collection.size(); i++) {
							ImageInstance image = collection.get(i);

							ID = image.getLong("id");
							filename = image.getStr("originalFilename");
							user = image.getLong("user");
							nbAnnotations = image.getInt("numberOfAnnotations");
							width = image.getInt("width");
							height = image.getInt("height");

							str.append("\t" + ID + " " + filename + " " + user
									+ " " + nbAnnotations + " " + width + " "
									+ height + "\n");
						}
						str.append("Result found: " + collection.size());

					} catch (Exception e) {
						System.out.println(Config.messages
								.get("project_get_failed"));
					}
				} catch (NumberFormatException e) {
					System.out
							.println(Config.messages.get("args_invalid_type"));
				}
				return str.toString();
			}
		};

		// update and execute command
		super.updateAction(action);
		Object returned = super.execute(args);

		if (returned != null) {
			System.out.println((String) returned);
		}

		return null;
	}

}
