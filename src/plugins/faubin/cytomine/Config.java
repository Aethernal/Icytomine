package plugins.faubin.cytomine;

import java.util.Map;
import java.util.TreeMap;

public class Config {

	// basic configuration
	public static boolean debug = true;
	public static final String version = "1.1.3";

	// image downloaded
	public static int thumbnailDefaultMaxSize = 2048;
	public static int iconPreviewDefaultMaxSize = 100;
	public static int previewDefaultMaxSize = 512;

	// server connection
	public static final String defaultServer = "http://cytomine.web.pasteur.fr";
	public static final String defaultPublicKey = "";
	public static final String defaultPrivateKey = "";

	// imagesPanel
	public static final int nbDisplayedImage = 5;

	//pre-defined messages
	public static Map<String, String> messages  = new TreeMap<String, String>();
	
	// ID MAP
	public static Map<String, Long> globalID = new TreeMap<String, Long>();

	
	public static void initialize() {
		globalID.put("ontology_section", new Long(12237));
		
		//Errors messages
		
		messages.put("args_invalid_length", "The number of arguments is invalid !");
		messages.put("args_invalid_type", "One or more arguments have an invalid type !");
		messages.put("args_invalid_container_length", "This is a container command, you should provide another command right after it !");
		
		messages.put("console_welcome","Welcome to Icytomine, write 'help' for a list of commands");
		messages.put("connection_failed", "The connection to cytomine failed !");
		
		messages.put("project_get_failed", "The project was not found !");
		
		messages.put("suggest_connection", "You should try to connect with 'connect' command before doing anything else !");
		messages.put("connection_required","You should be connected to cytomine to do that, try 'help' for more informations");
		
		messages.put("unknown_command", "Unknown command, check the commands list with 'help' command !");
		
		messages.put("check_help", "Type 'help' for more informations !");
		
	}

}
