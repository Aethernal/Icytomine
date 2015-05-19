package plugins.faubin.cytomine.headless.cmd;

import icy.main.Icy;
import plugins.faubin.cytomine.headless.Console;


public class CMDExit extends CMD{
	
	public CMDExit(Console console) {
		super(console);
		
		String command = "exit";
		String description = "Exit the program";
		String[] arguments = new String[]{};
		
		super.initialize(command, description, arguments);
		
	}
	
	@Override
	public Object execute(final String[] args){
		
		//generating action with arguments
		CMDAction action = new CMDAction(args) {
			
			@Override
			public Object call(){
				
				Icy.exit(false);
				
				return null;
			}
		};
		
		//update and execute command
		super.updateAction(action);
		super.execute(args);
		
		return null;
	}
	
}
