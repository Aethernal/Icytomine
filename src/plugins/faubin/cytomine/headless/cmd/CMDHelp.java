package plugins.faubin.cytomine.headless.cmd;

import java.util.Collection;
import java.util.Iterator;

import plugins.faubin.cytomine.headless.Console;


public class CMDHelp extends CMD{
	
	public CMDHelp(Console console) {
		super(console);
		
		String command = "help";
		String description = "Show a list of all commands or a specific command";
		String[] arguments = new String[]{};
		
		super.initialize(command, description, arguments);
		
	}
	
	@Override
	public Object execute(String[] args){
		
		//search all commands
		CMDAction action = new CMDAction(args) {
			
			@Override
			public Object call(){
				StringBuffer str = new StringBuffer();
				int count = 0;
				str.append("Listing all commands"+"\n\n");
				
				Collection<CMD> commands = console.commands.values();
				for (Iterator<CMD> iterator = commands.iterator(); iterator.hasNext();) {
					CMD cmd = iterator.next();
					cmd.nbTab = 1;
					str.append(cmd.toString()+"\n");
					
					count++;
				}
				str.append("\n"+"Found "+count+" results !");
				
				return str.toString();
			}
			
		};
		
		
		//update and execute command
		super.updateAction(action);
		Object returned = super.execute(args);
		
		//process returned value
		if(returned!=null){
			System.out.println( (String) returned );
		}
		
		return null;
	}
	
}
