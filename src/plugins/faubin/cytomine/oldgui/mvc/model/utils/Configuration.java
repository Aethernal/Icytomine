package plugins.faubin.cytomine.oldgui.mvc.model.utils;

import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


@SuppressWarnings("serial")
public class Configuration implements Serializable{
	
	//connection values
	public boolean rememberMe;
	public String publicKey;
	public String privateKey;
	public String host;
	
	//default values
	private static final boolean defaultRememberMe = true;
	private static final String defaultPublicKey = "d42e7fe6-4e84-4c04-82f5-9863d10d590e";
	private static final String defaultPrivateKey = "d22a6cd4-04cb-4d06-8a46-082379219b4c";
	private static final String defaultHost = "http://cytomine.web.pasteur.fr";
	
	//images sizes
	public int glomerulDetectionZoom; 
	public int thumbnailMaxSize; 
	public int previewMaxSize; 
	public int iconPreviewMaxSize; 
	
	//default values
	private static final int defaultGlomerulDetectionZoom = 1; 
	private static final int defaultThumbnailMaxSize = 2048; 
	private static final int defaultPreviewMaxSize = 512; 
	private static final int defaultIconPreviewMaxSize = 128; 
	
	
	//images view
	public int nbRowPerPage;
	
	//default values
	public static final int defaultNbRowPerPage = 30;
	
	//glomerule generation
	public int cValue;
	public int minSize;
	public int maxSize;
	public int vMinLong;
	public int valRatioAxis;
	
	//default values
	private static final int defaultCValue = 1;
	private static final int defaultMinSize = 200;
	private static final int defaultMaxSize = 231000;
	private static final int defaultVMinLong = 10;
	private static final int defaultValRatioAxis = 2;
	
	//dynamic viewer
	public Dimension dynamicViewerDim;
	
	//default values
	private static final Dimension defaultDynamicViewerDim = new Dimension(500,500);
	
	//singleton
	private static Configuration configuration = createConfiguration();
	
	public static Configuration getConfiguration() {
		return configuration;
	}

	private static Configuration createConfiguration(){
		Configuration configuration;
		File file = new File("saved/config.data");
		
		if(file.exists()){
			configuration = loadConfiguration(file);
		}else{
			configuration = defaultConfiguration();
		}
		
		return configuration;
		
	}
	
	private Configuration(){
		
	}
	
	/**
	 * load a configuration if one exist with no error or generate a default configuration
	 * @param file
	 */
	@SuppressWarnings("resource")
	protected static Configuration loadConfiguration(File file){
		try {
			ObjectInputStream stream = new ObjectInputStream(new FileInputStream(file));
			
			try {
				Configuration conf = (Configuration) stream.readObject();
				return conf;
				
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				return defaultConfiguration();
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return defaultConfiguration();
		} catch (IOException e) {
			e.printStackTrace();
			return defaultConfiguration();
		}
	}
	
	/**
	 * generate new configuration with default values
	 */
	protected static Configuration defaultConfiguration(){
		Configuration config = new Configuration();
		//connection
		config.rememberMe = defaultRememberMe;
		config.publicKey = defaultPublicKey;
		config.privateKey = defaultPrivateKey;
		config.host = defaultHost;
		
		//images size
		config.glomerulDetectionZoom = defaultGlomerulDetectionZoom; 
		config.thumbnailMaxSize = defaultThumbnailMaxSize; 
		config.previewMaxSize = defaultPreviewMaxSize; 
		config.iconPreviewMaxSize = defaultIconPreviewMaxSize; 
		
		//images view
		config.nbRowPerPage = defaultNbRowPerPage;
		
		//glomerule generation
		config.cValue = defaultCValue;
		config.minSize = defaultMinSize;
		config.maxSize = defaultMaxSize;
		config.vMinLong = defaultVMinLong;
		config.valRatioAxis = defaultValRatioAxis;
		
		//Dyn viewer
		config.dynamicViewerDim = defaultDynamicViewerDim;
		
		return config;
	
	}
	
	@SuppressWarnings("resource")
	public void save() throws FileNotFoundException, IOException{
		File folder = new File("saved");
		
		if(folder.exists()){
			if(!folder.isDirectory()){
				folder.mkdir();
			}
		}else{
			folder.mkdir();
		}
		
		File file = new File("saved/config.data");
		
		ObjectOutputStream obj = new ObjectOutputStream(new FileOutputStream(file));
		
		obj.writeObject(this);
		
		System.out.println("configuration saved");
		
	}
	
}
