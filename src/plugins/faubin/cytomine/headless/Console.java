package plugins.faubin.cytomine.headless;

import icy.main.Icy;

import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import plugins.faubin.cytomine.Config;
import plugins.faubin.cytomine.headless.cmd.CMD;
import plugins.faubin.cytomine.headless.cmd.CMDConnect;
import plugins.faubin.cytomine.headless.cmd.CMDContainer;
import plugins.faubin.cytomine.headless.cmd.CMDExit;
import plugins.faubin.cytomine.headless.cmd.CMDHelp;
import plugins.faubin.cytomine.headless.cmd.CMDProjects;
import plugins.faubin.cytomine.headless.cmd.image.CMDImageGenerateSection;
import plugins.faubin.cytomine.headless.cmd.project.CMDProjectDeleteAll;
import plugins.faubin.cytomine.headless.cmd.project.CMDProjectDeleteTerm;
import plugins.faubin.cytomine.headless.cmd.project.CMDProjectGenerateGlomerule;
import plugins.faubin.cytomine.headless.cmd.project.CMDProjectGenerateSection;
import plugins.faubin.cytomine.headless.cmd.project.CMDProjectList;
import plugins.faubin.cytomine.headless.cmd.project.CMDProjectListTerm;
import be.cytomine.client.Cytomine;


public class Console {

	public Cytomine cytomine = null;
	//command map for easy access
	public Map<String, CMD> commands;
	
    public Console() {
    	
	}
    
    public void initialize(){
		//initialising variables
		String query = "";
		Scanner sc = new Scanner(System.in);
		commands = new TreeMap<String ,CMD>();
		
		String[] launchArgs = Icy.getCommandLinePluginArgs();
		
    	//commands initialisation
    	iniCommands();
		
		//connecting to cytomine if args where given
		if(launchArgs.length>=3){
			System.out.println("Connecting to cytomine using launch args");
			cytomine = new Cytomine(launchArgs[0], launchArgs[1], launchArgs[2]);
			try{
				cytomine.getProjects();
				
				if(launchArgs.length>=4){
					String cmd = launchArgs[3];
					String[] cmdArgs = new String[launchArgs.length-4];
					for (int i = 4; i < launchArgs.length; i++) {
						cmdArgs[i-4] = launchArgs[i];
					}
					if(commands.get(cmd)!=null){
						commands.get(cmd).execute(cmdArgs);
						Icy.exit(false);
						System.exit(0);
					}else{
						System.out.println(Config.messages.get("unknown_command"));
						Icy.exit(false);
						System.exit(1);
					}
				}
				
			}catch(Exception e){
				System.out.println(Config.messages.get("connection_failed"));
				cytomine = null;
			}
		}
		
		System.out.println(Config.messages.get("console_welcome"));
		if(cytomine==null){
			System.out.println(Config.messages.get("suggest_connection"));
		}
		
		do{
			sc = new Scanner(System.in);
			query = sc.nextLine();
			
			//split query to separate the command and arguments
			String[] querySplit = query.toLowerCase().split(" ");
			String cmd = querySplit[0];
			String[] args = new String[querySplit.length-1];
			for (int i = 1; i < querySplit.length; i++) {
				args[i-1] = querySplit[i];
			}
			
			//action
			if(this.cytomine == null){
				if(cmd.equals("connect") || cmd.equals("exit") || cmd.equals("help") ){
					if(commands.get(cmd)!=null){
						commands.get(cmd).execute(args);
					}else{
						System.out.println(Config.messages.get("unknown_command"));
					}
				}else{
					System.out.println(Config.messages.get("connection_required"));
				}
			}else{
				if(commands.get(cmd)!=null){
					commands.get(cmd).execute(args);
				}else{
					System.out.println(Config.messages.get("unknown_command"));
				}
			}
			
		}while(!query.equals("exit"));
		sc.close();
    }
    
    private void iniCommands() {
    	//base commands
		CMDConnect cmdConnect = new CMDConnect(this);
		CMDExit cmdExit= new CMDExit(this); 
		CMDHelp cmdHelp = new CMDHelp(this); 
		
		addCMD(cmdConnect);
		addCMD(cmdExit);
		addCMD(cmdHelp);
		
		//plug-in
		CMDProjects cmdProjects = new CMDProjects(this);
		CMDContainer cmdProject = new CMDContainer(this, "project"); 
			CMDProjectDeleteAll cmdProjectDeleteAll = new CMDProjectDeleteAll(this);
			CMDProjectDeleteTerm cmdProjectDeleteTerm = new CMDProjectDeleteTerm(this);
			CMDProjectList cmdProjectList = new CMDProjectList(this);
			CMDProjectGenerateSection cmdProjectGenerateSections = new CMDProjectGenerateSection(this);
			CMDProjectListTerm cmdProjectListTerms = new CMDProjectListTerm(this);
			CMDProjectGenerateGlomerule cmdProjectGenerateGlomerule = new CMDProjectGenerateGlomerule(this);
			
			cmdProject.add(cmdProjectDeleteAll);
			cmdProject.add(cmdProjectDeleteTerm);
			cmdProject.add(cmdProjectList);
			cmdProject.add(cmdProjectGenerateSections);
			cmdProject.add(cmdProjectListTerms);
			cmdProject.add(cmdProjectGenerateGlomerule);
			
		CMDContainer cmdImage = new CMDContainer(this, "image"); 	
			CMDImageGenerateSection cmdImageGenerateSections = new CMDImageGenerateSection(this);
			
			cmdImage.add(cmdImageGenerateSections);
			
		addCMD(cmdProjects);
		addCMD(cmdProject);
		addCMD(cmdImage);
	}

	public void writeUnknownCommand(){
    	System.out.println("unknown command, check the commands list by using 'help' command");
    }
    
    public void addCMD(CMD cmd){
    	commands.put(cmd.getCommand(), cmd);
    }
	
}
