package plugins.faubin.cytomine.utils.software;

public class Parameter {
	private String name;
	public long ID;
	private ParameterType type;
	private String DefaultValue;
	
	public Parameter(String name, String defaultValue){
		this.name = name;
		ID = -1;
		type = ParameterType.Number;
	}
	
	public Parameter(String name, String defaultValue, long iD) {
		super();
		this.name = name;
		ID = iD;
		type = ParameterType.Number;
	}
	
	public enum ParameterType{
		Number;
		
		public String toString(){
			return this.name();
		}
		
	}
	
	public String getName() {
		return name;
	}
	
	public ParameterType getType() {
		return type;
	}
	
	public String getDefaultValue() {
		return DefaultValue;
	}
	
	public String toString(){
		return "{ID:"+ID+", name:"+name+", type:"+type+"}";
	}
}
