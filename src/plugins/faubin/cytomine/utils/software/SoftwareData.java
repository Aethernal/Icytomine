package plugins.faubin.cytomine.utils.software;

import java.util.List;
import java.util.TreeMap;

import be.cytomine.client.models.SoftwareParameter;

/**
 * @author faubin
 * this class is used to create a new software or keep the id up to date for different cytomine servers.
 */
public class SoftwareData {
	public long ID;
	private String name;
	private TreeMap<String, Parameter> parameters;
	
	public SoftwareData(String name){
		this.name = name;
		ID = -1;
		parameters = new TreeMap<String, Parameter>();
	}
	
	public SoftwareData(long ID, String name){
		this.name = name;
		this.ID = ID;
		parameters = new TreeMap<String, Parameter>();
	}
	
	public String getName() {
		return name;
	}
	
	public Parameter getParameter(String parameter){
		return parameters.get(parameter);
	}
	
	
	public TreeMap<String, Parameter> getParameters() {
		return parameters;
	}
	
	public void addParameter(Parameter parameter){
		parameters.put(parameter.getName(), parameter);
	}
	
	public String toString(){
		String msg = "{ID:"+ID+", name:"+name+", parameters:[";
		for(Parameter p : parameters.values()){
			msg+=p.toString();
		}
		msg+="]}";
		
		return msg;
	}
	
}
