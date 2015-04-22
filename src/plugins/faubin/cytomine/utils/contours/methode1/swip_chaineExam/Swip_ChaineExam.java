package plugins.faubin.cytomine.utils.contours.methode1.swip_chaineExam;

import plugins.faubin.cytomine.utils.contours.methode1.maillons.Chaine;
import plugins.faubin.cytomine.utils.contours.methode1.maillons.LinkerPar;
import plugins.faubin.cytomine.utils.contours.methode1.swip_maillon.Swip_maillon;


public class Swip_ChaineExam {

	public static final int VAL_MAX = 150;
	private static LinkerPar LeLinker;
	
	public static void P_XXXX(int i,int j, Short edge, int[][][] itab, int l1,int l2,Chaine[] kbuf,int idebug)
	{
	    Chaine UneChaine;
	    int sipremier=0;

	    if (idebug!=0) System.out.println("Configuration P_XXXX Line "+i+" Col "+j+"");

	    LeLinker.nb_chaines += 1;
	    if (LeLinker.nb_chaines >= LeLinker.nb_chaines_max) {
	        System.out.println("Too many chains\n");
	        return;
	    }
	    UneChaine = (Chaine) Swip_maillon.opench_();
	    if (UneChaine == null) {
	        System.out.println("There is no more space for chains.\n");
	        return;
	    }
	    kbuf[ LeLinker.nb_chaines - 1] = UneChaine ;
	    itab[ j][l2 ][ 2 ] = LeLinker.nb_chaines;
	    itab[ j][l2 ][ 1 ] = 'P'   ;
	    Swip_maillon.nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
	}

	public static void P_XXXO(int i,int j,Short edge,int [][][] itab, int l1, int l2, Chaine[] kbuf,int idebug){
		    Chaine UneChaine;
		    int sipremier=0;
	
		    if (idebug!=0) System.out.println("Configuration P_XXXO Line "+i+" Col "+j+"");
	
		    itab[ j][l2 ][ 2 ] = itab[ j-1][l2 ][ 2 ];
		    itab[ j][l2 ][ 1 ] = 'O' ;
		    if (itab[ j-1][l2 ][ 1 ] == 'P') {
		        itab[ j-1][l2 ][ 1 ] = 'D';
		        sipremier = 1;
		    } else itab[ j-1][l2 ][ 1 ] = 'X';
		    Swip_maillon.nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
		    if (!(sipremier!=0))
		        if ( ptest5668(i,j,edge) ) {
		            itab[ j][l2 ][ 1 ] = 'F';
		        }
	}
	
	public static boolean ptest5668(int i,int j,Short edge){
	    int p5,p6,p8;
	    int jdimx,dimx;

	 
	    dimx = LeLinker.dim_x;
	    jdimx = j * LeLinker.dim_x;
	    edge = (short) (edge + i + jdimx);
	    p5 = edge + dimx;
	    p6 = edge + 1 - dimx;
	    p8 = edge + 1 + dimx;

	    return ( (p5 != 0 && p6 != 0) || (p6 != 0 && p8 != 0));
	}
	
	public static int ptest5(int i,int j,Short edge){
	    return edge + i + (j + 1) * LeLinker.dim_x;
	}
	
	public static void initab_(int l,int itab[][][]){
	    int k ;

	    for (k=0; k<=LeLinker.dim_y + 1; k++) itab[k][l][1] = 'X' ;	
	}
	
	public static void FSA(int i,int j,Short edge,int[][][] itab,int l1,int l2,Chaine[] kbuf,int idebug){
	    int ncov = 0 ; 
	    int ncfv = 0 ;
	    int poids[] = {1,8,4,2} ;
	    int k ,vitab;
	    int V;

	    /*--------- traitement de la ligne l1 -------------*/

	    for (k=(-1) ; k<=1; k++) {
	    	vitab=itab[j+k][l1][1];
	    	if (vitab <= 'F') ncfv += poids[k+1] ;
		else
	    	    if (vitab <= 'P') ncov += poids[k+1];
	    }

	    /*--------- Traitement de la ligne l2   ------------*/

	    vitab=itab[j-1][l2][1];
	    if (vitab <= 'F') ncfv += poids[3] ;
	    else
	        if (vitab <= 'P') ncov += poids[3] ;	

	    V=ncfv*10 + ncov;

	    (gtotab[V])(i,j,edge,itab,l1,l2,kbuf,idebug);
	}
	
}
