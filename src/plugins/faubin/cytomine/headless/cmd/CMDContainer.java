package plugins.faubin.cytomine.headless.cmd;

import java.util.TreeMap;

import plugins.faubin.cytomine.Config;
import plugins.faubin.cytomine.headless.Console;

public class CMDContainer extends CMD {
	private TreeMap<String, CMD> commands;
	
	public CMDContainer(Console console, String command) {
		super(console);
		commands = new TreeMap<String, CMD>();
		
		super.initialize(command, "Container", new String[]{});
	}
	
	@Override
	public Object execute(String[] args){

		//container should not be executed without other command
		if(args.length >= 1){
			//delete first argument that was this command
			String[] newArgs = new String[args.length-1];
			for (int i = 1; i < args.length; i++) {
				newArgs[i-1] = args[i];
			}
			
			//if argument match a command execute it
			if(commands.get(args[0]) != null){
				commands.get(args[0]).execute(newArgs);
			}else{
				System.out.println(Config.messages.get("unknown_command"));
				System.out.println(Config.messages.get("check_help"));
			}
			
		}else{
			System.out.println(Config.messages.get("args_invalid_container_length"));
			System.out.println(Config.messages.get("check_help"));
		}
		
		
		return null;
	}
	
	
	/**
	 * @param cmd
	 * add the CMD to the container commands list
	 */
	public void add(CMD cmd){
		commands.put(cmd.command, cmd);
	}
	
	@Override
	public String toString(){
		StringBuffer str = new StringBuffer();
		
		str.append(""+super.toString());
		
		if(commands.keySet().size()>0){
			str.append('\n');
		}
		
		for (String key : commands.keySet()) {
			CMD cmd = commands.get(key);
			cmd.nbTab = nbTab+1;
			
			str.append(cmd.toString()+'\n');
		}
		
		/*return
		*	command
		*		subcommand1 <arg1> <arg2> - description 
		*		subcommand2 <arg1> <arg2> - description 
		*/
		
		return str.toString();
	}
	
	public CMD getCMD(String command){
		CMD result = null;
		
		//if in the list return it
		if(commands.get(command)!=null){
			return commands.get(command);
		}
		
		//else check for instance of Container to search inside too
		for (String key : commands.keySet()) {
			CMD cmd = commands.get(key);
			
			if(cmd instanceof CMDContainer){
				return ((CMDContainer) cmd).getCMD(command);
			}
		}
		
		return result;
	}
	
}
