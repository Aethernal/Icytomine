package plugins.faubin.cytomine.utils.contours.methode1.swip_chainage;

import java.util.Stack;

import plugins.faubin.cytomine.utils.contours.methode1.maillons.Chaine;
import plugins.faubin.cytomine.utils.contours.methode1.maillons.Chainon;
import plugins.faubin.cytomine.utils.contours.methode1.maillons.LinkerPar;

public class Swip_ChainageMem {
	
	public static final int MOY_CH = 100 ;/* 50 */
	public static final int MAX_CH = 5000 ;/* 5000 */
	
	public static LinkerPar LeLinker;
	
	public static int LeLinker_memory_set(short[] p_image, int lmin, int nx, int ny){
 /***** memory allocation and variable setting ***********/
	
		LeLinker = new LinkerPar();
		LeLinker.chainons_pile = new Stack<Chainon>();
		LeLinker.chaines_pile = new Stack<Chaine>();
		LeLinker.chaines = new Chaine[MAX_CH];
		LeLinker.fusions = new Chaine[MAX_CH];
		LeLinker.bruits = new int[MAX_CH];
		
 
		/* conservation des adresses de debut de pile */
		LeLinker.chainons_pile_deb=LeLinker.chainons_pile.get(0);
		LeLinker.chaines_pile_deb=LeLinker.chaines_pile.get(0);
		
		LeLinker.nb_chainons_pile = (int)(MOY_CH*MAX_CH);
		LeLinker.nb_chaines_pile = (int)MAX_CH;
		LeLinker.nb_chaines_max = (int)MAX_CH;
		LeLinker.ibruit = lmin;
		LeLinker.dim_x = nx;
		LeLinker.dim_y = ny;
		LeLinker.edge = (short[]) p_image;

		 return(1);
	}
	
	public static void LeLinker_memory_free(){
	  LeLinker.chainons_pile_deb=null; LeLinker.chainons_pile=null;
	  LeLinker.chaines_pile_deb=null; LeLinker.chaines_pile=null;
	  LeLinker.chaines=null; LeLinker.chaines=null;
	  LeLinker.fusions=null; LeLinker.fusions=null;
	  LeLinker.bruits=null; LeLinker.bruits=null;
	  LeLinker=null;
	}
	
	
	
}
