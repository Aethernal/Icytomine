package plugins.faubin.cytomine.gui.mvc.model.utils;

import java.util.Stack;

public class ThreadForRunnablePool extends Thread{

	Stack<Runnable> stack;
	
	public ThreadForRunnablePool(Stack<Runnable> stack) {
		this.stack  = stack;
	}
	
	@Override
	public void run(){
		
		while(!stack.isEmpty()){
			Runnable run = stack.pop();
			run.run();
		}
		
	}
	
}
