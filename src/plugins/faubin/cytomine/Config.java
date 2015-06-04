package plugins.faubin.cytomine;

import java.util.Map;
import java.util.TreeMap;

public class Config {

	// basic configuration
	public static boolean debug = true;
	public static final String version = "1.2.0";

	// image downloaded
	public static int glomerulDetectionDefaultZoom = 1; 

	//pre-defined messages
	public static Map<String, String> messages  = new TreeMap<String, String>();
	
	// ID MAP
	public static Map<String, Long> IDMap = new TreeMap<String, Long>();

	
	public static void initialize() {
		//ID ontology
		IDMap.put("ontology_section", 12237l);
		IDMap.put("ontology_glomerule", 12224l);
		
		//ID Software
		IDMap.put("software_SectionGeneration3", 6571943l);
		IDMap.put("SectionGenerationSoftware", 8334270l);
			IDMap.put("SectionGenerationSoftware_param1", 8334278l);
			IDMap.put("SectionGenerationSoftware_param2", 8334284l);
			IDMap.put("SectionGenerationSoftware_param3", 8334290l);
		
		IDMap.put("GlomeruleGenerationSoftware", 8424480l);
			IDMap.put("GlomeruleGenerationSoftware_param1", 8424490l);
			IDMap.put("GlomeruleGenerationSoftware_param2", 8424496l);
			IDMap.put("GlomeruleGenerationSoftware_param3", 8424502l);
			IDMap.put("GlomeruleGenerationSoftware_param4", 8424508l);
			IDMap.put("GlomeruleGenerationSoftware_param5", 8424514l);
			IDMap.put("GlomeruleGenerationSoftware_param6", 8424520l);
			IDMap.put("GlomeruleGenerationSoftware_param7", 8424526l);
		
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
