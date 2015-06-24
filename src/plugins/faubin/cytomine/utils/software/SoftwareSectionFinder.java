package plugins.faubin.cytomine.utils.software;

public class SoftwareSectionFinder extends SoftwareData{

	public SoftwareSectionFinder() {
		super("Icy_Section_Finder");
		addParameter(new Parameter("imageID", "0"));
		addParameter(new Parameter("maxSize", "2048"));
		addParameter(new Parameter("threshold", "0"));
		
	}

	
	
}
