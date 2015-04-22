package plugins.faubin.cytomine.headless.cmd.project;

import plugins.faubin.cytomine.Config;
import plugins.faubin.cytomine.headless.Console;
import plugins.faubin.cytomine.headless.cmd.CMD;
import plugins.faubin.cytomine.headless.cmd.CMDAction;
import be.cytomine.client.collections.TermCollection;
import be.cytomine.client.models.Project;
import be.cytomine.client.models.Term;

public class CMDProjectListTerm extends CMD {

	public CMDProjectListTerm(Console console) {
		super(console);

		String command = "terms";
		String description = "List all terms from the project";
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
					Long projectID = Long.parseLong(args[0]);
					try {
						Project project = console.cytomine
								.getProject(projectID);
						long ontologyID = project.getLong("ontology");
						TermCollection terms = console.cytomine
								.getTermsByOntology(ontologyID);

						str.append("Terms"+"\n");

						// variables
						long termID;
						String termName;
						int count = 0;

						for (int i = 0; i < terms.size(); i++) {
							Term term = terms.get(i);
							termID = term.getLong("id");
							termName = term.getStr("name");

							str.append("\t" + termID + " " + termName
									+ "\n");
							count++;
						}

						str.append("Found " + count + " results");

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
