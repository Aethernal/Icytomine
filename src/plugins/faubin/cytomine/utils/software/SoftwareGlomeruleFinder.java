package plugins.faubin.cytomine.utils.software;


public class SoftwareGlomeruleFinder extends SoftwareData{

	public SoftwareGlomeruleFinder() {
		super("Icy_Glomeruli_Finder");
		addParameter(new Parameter("imageID", "0"));
		addParameter(new Parameter("zoom", "2Ø"));
		addParameter(new Parameter("minSize", "200"));
		addParameter(new Parameter("maxSize", "231000"));
		addParameter(new Parameter("cvalue", "1"));
		addParameter(new Parameter("valratioAxis", "2"));
		addParameter(new Parameter("vminlong", "10"));
		
	}

	
	
}
