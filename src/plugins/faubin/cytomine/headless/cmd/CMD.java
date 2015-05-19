package plugins.faubin.cytomine.headless.cmd;

import plugins.faubin.cytomine.Config;
import plugins.faubin.cytomine.headless.Console;

public abstract class CMD {
	protected Console console;
	protected String command = "";
	protected CMDAction action = null;
	protected String[] arguments = new String[]{};
	protected String description = "";
	
	//toString variable
	protected int nbTab = 0;
	
	public CMD(Console console) {
		this.console = console;
	}
	
	public void initialize(String command, String description, String[] arguments){
		this.command = command;
		this.description = description;
		this.arguments = arguments;
	}
	
	public Object execute(String[] args){
		if(arguments.length == args.length){
			try {
				return action.call();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			System.out.println(Config.messages.get("args_invalid_length"));
			System.out.println(Config.messages.get("check_help"));
		}
		return null;
	}

	public String getCommand() {
		return command;
	}
	
	/**
	 * @param action
	 * Should be done before each execution to update args
	 */
	public void updateAction(CMDAction action){
		this.action = action;
	}
	
	public String toString(){
		StringBuffer str = new StringBuffer();
		
		//tab space
		for (int i = 0; i < nbTab; i++) {
			str.append('\t');
		}
		
		//command name
		str.append(""+command);
		
		//write required arguments
		for (int i = 0; i < arguments.length; i++) {
			str.append(" <"+arguments[i]+">");
		}
		
		//write description
		str.append(" - "+description);
		
		//result should be
		//Command <arg1> <arg2> - this is an example of command
		
		return str.toString();
	}

}
