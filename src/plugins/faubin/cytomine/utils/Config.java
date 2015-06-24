package plugins.faubin.cytomine.utils;

import java.util.Map;
import java.util.TreeMap;

import javax.swing.UIManager;

/**
 * @author faubin
 * this class was used to store constant.
 * it was partially remplaced by the Configuration class that store and localy save the configurations of the plugin
 */
public class Config {

	// basic configuration
	public static boolean debug = true;
	public static final String version = "1.2.0";

	// image downloaded
	public static int glomerulDetectionDefaultZoom = 1; 

	//pre-defined messages
	public static Map<String, String> messages  = new TreeMap<String, String>();
	
	// ID MAP
//	public static Map<String, Long> IDMap = new TreeMap<String, Long>();

	
	public static void initialize() {
		
//		//ID ontology
//		IDMap.put("ontology_section", 621938l);
//		IDMap.put("ontology_glomerule", 621948l);
//		
//		//ID Software
//		IDMap.put("SectionGenerationSoftware", 632699l);
//			IDMap.put("SectionGenerationSoftware_param1", 632709l);
//			IDMap.put("SectionGenerationSoftware_param2", 632715l);
//			IDMap.put("SectionGenerationSoftware_param3", 632721l);
//		
//		IDMap.put("GlomeruleGenerationSoftware", 632827l);
//			IDMap.put("GlomeruleGenerationSoftware_param1", 632837l);
//			IDMap.put("GlomeruleGenerationSoftware_param2", 632843l);
//			IDMap.put("GlomeruleGenerationSoftware_param3", 632849l);
//			IDMap.put("GlomeruleGenerationSoftware_param4", 632855l);
//			IDMap.put("GlomeruleGenerationSoftware_param5", 632861l);
//			IDMap.put("GlomeruleGenerationSoftware_param6", 632867l);
//			IDMap.put("GlomeruleGenerationSoftware_param7", 632873l);
		
		//Errors messages
		
		messages.put("args_invalid_length", "The number of arguments is invalid !");
		messages.put("args_invalid_type", "One or more arguments have an invalid type !");
		messages.put("args_invalid_container_length", "This is a command container, you should provide another command right after it !");
		
		messages.put("console_welcome","Welcome to Icytomine, write 'help' for a list of commands");
		messages.put("connection_failed", "The connection to cytomine failed !");
		
		messages.put("project_get_failed", "The project was not found !");
		messages.put("image_get_failed", "The image was not found !");
		
		messages.put("suggest_connection", "You should try to connect with 'connect' command before doing anything else !");
		messages.put("connection_required","You should be connected to cytomine to do that, try 'help' for more informations");
		
		messages.put("unknown_command", "Unknown command, check the commands list with 'help' command !");
		
		messages.put("check_help", "Type 'help' for more informations !");
		
	}

}
