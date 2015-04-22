package plugins.faubin.cytomine.utils.contours.methode1.swip_maillon;

import plugins.faubin.cytomine.utils.contours.methode1.maillons.Chaine;
import plugins.faubin.cytomine.utils.contours.methode1.maillons.Chainon;
import plugins.faubin.cytomine.utils.contours.methode1.maillons.LinkerPar;

public class Swip_maillon {
	
	public static LinkerPar LeLinker;
	
	public static final int OPEN = 0;
	public static final int CLOSE = 1 ;
	public static final int CONTOUR = 2;
	
	public static Chaine opench_(){
	    Chaine UneChaine;
	    int i;
	
	    /* Allocate 3 items, returning NULL in case of failure. */
	
	    UneChaine = alloc_chaine();
	
	    if (UneChaine==null) return null;
	
	    /* Set up fields of head. */
	
	    UneChaine.mode = OPEN;
	    UneChaine.dernier = null;
	    UneChaine.numero = ++(LeLinker.numero);
	
	    /* Presently there are no relations. */
	
	    for ( i=0 ; i<4 ; ++i) {
	    	UneChaine.tete.famille[i] = 0;	/* pas d'aieul==AVI...(aulico, no?) */
	    	UneChaine.queue.famille[i] = 0;	/* pas de fils */
	    }
	
	    /* Return the pointer to the new list. */
	
	    return(UneChaine);
	}
	
	public static Chaine alloc_chaine(){
		Chaine UneChaine;
		if (!(LeLinker.nb_chaines_pile-- != 0)) return null;
		else
		{
			UneChaine = LeLinker.chaines_pile.pop();
			return UneChaine;
		}
	}
	
	public static Chainon nwmail_(Chaine UneChaine, int ix,int iy){
	    Chainon UnChainon;
	
	    UnChainon = alloc_chainon();
	    if (UnChainon!=null) {
	    	UnChainon.succ = null;
	    	UnChainon.x = (short) ix;
	    	UnChainon.y = (short) iy;
		if (UneChaine.dernier == null)
			UneChaine.premier = UneChaine.dernier = UnChainon;
		else
			UneChaine.dernier = UneChaine.dernier.succ = UnChainon;
	
	    }
	    return UnChainon;
	}
	
	public static Chainon alloc_chainon(){
		Chainon UnChainon;
		if (!(LeLinker.nb_chainons_pile-- != 0)) return null;
		else
		{
			UnChainon = LeLinker.chainons_pile.pop();
			return UnChainon;
		}
	}
	
	public static int nbmail_(Chaine UneChaine){
		Chainon UnChainon = UneChaine.premier;
		int i=0;
	
		while ( UnChainon != null ) 
		{
			i++;
			UnChainon = UnChainon.succ;
		}
		return i;
	}
	
	public static boolean un_seul_lien( int numero, Chaine UneChaine, Character typep, int idebug){
	    
	    if (numero > 0 ){
	    	typep = 'D';
		return( !(UneChaine.tete.famille[1]!=0 ) );
	    } else {
	    	typep = 'F';
		return( !(UneChaine.queue.famille[1]!=0 ) );
	    }
	}
	
	public static boolean TsupTest (Chaine UneChaine,Chaine[] kbuf, int nb, int[] famille, int idebug) {
	    Character type = null ;
	
	    if (nb >= 2){ return true; };
		
	        /*--------------   ici nb == 1  -----------------------------------*/
	
	    if (famille[0] > 0) {
	    	return ( ( !(un_seul_lien( UneChaine.numero,kbuf[famille[0] -1 ], type, idebug) ) ) );
	    }else{
	    	return ( ( !(un_seul_lien( -( UneChaine.numero),kbuf[Math.abs(famille[0]) -1 ], type, idebug) ) ) );
	    }
	}
	
	public static boolean aieul_b(Chaine UneChaine, int aieul){
	    int i;
	
	
	    for ( i=0 ; i<4 ; ++i) {
	    	if ( UneChaine.tete.famille[i] == 0 ) {
	    	    UneChaine.tete.famille[i] = aieul ;
		    return true;
		}
		if ( UneChaine.tete.famille[i] == aieul ) return false;
	    }
	
	    return false;
	}
	
	public static boolean fils_b(Chaine UneChaine, int fils){
	    int i;
	
	
	    for ( i=0 ; i<4 ; ++i) {
	    	if ( UneChaine.queue.famille[i] == 0 ) {
	    	    UneChaine.queue.famille[i] = fils ;
		    return true;
		}
		if ( UneChaine.queue.famille[i] == fils ) return false;
	    }
	
	    return false;
	}
	
	public static void refiliat (Chaine[] kbuf,int[] progen, int numprogen, int idebug){
	    Chaine UneChaine;
	    int j ;
	
	    UneChaine = kbuf[Math.abs(progen[0])-1] ;
	    if (progen[0] > 0) 
	    	for (j=1 ; j< numprogen ; j++) {
	    	    if (progen[j] >0) 
			if (aieul_b ( UneChaine,progen[j] ) ) 
				aieul_b ( kbuf[progen[j]-1],progen[0] ) ;
		    if (progen[j] < 0) 
			if (aieul_b  ( UneChaine,progen[j] ) ) 
				fils_b  ( kbuf[Math.abs(progen[j])-1],progen[0] ) ;
		}
	
	    if (progen[0] < 0){
	    	for (j=1 ; j< numprogen ; j++) {
	    	    if (progen[j] > 0){}
			if (aieul_b( kbuf[progen[j]-1],progen[0] ) ) {
				fils_b ( UneChaine,progen[j] );
	    	    if (progen[j] < 0)
	    		if (fils_b ( kbuf[Math.abs(progen[j])-1],progen[0] ) ) 
				fils_b ( UneChaine,progen[j] );
			}
	    	}
	    }
	}
	
	public static int[] laieul_(Chaine UneChaine, Integer nb){
	    int i; 
	    int[] aieul;
	
	    nb = 0;
	    aieul = UneChaine.tete.famille;
	    for ( i=0 ; i<4 ; ++i) if ( aieul[i]  == 0 ) break; else (nb)++;
	    return aieul;
	}
	
	public static int[] lfils_(Chaine UneChaine, Integer nb){
	    int i;
	    int[] fils;
		
	    nb = 0;
	    fils = UneChaine.queue.famille;
	    for ( i=0 ; i<4 ; ++i) if ( fils[i] == 0 ) break; else (nb)++;
	    return fils;
	}
	
	int ssaieul_ (Chaine UneChaine, int nume){
	    int i,found = 0 ;
	 
	    nume = Math.abs(nume);
	
	    /* Search 'nume' */
	
	    for ( i=0 ; i<4 ; ++i) {
	    	if (UneChaine.tete.famille[i] == 0) break ;	
		if (Math.abs(UneChaine.tete.famille[i]) == nume) found = i+1 ;
	    }
	
	    /* Recompact famille. */
	
	    if (found!=0) {
	    	UneChaine.tete.famille[found-1] = UneChaine.tete.famille[i-1] ;
		UneChaine.tete.famille[i-1] = 0 ;
	    }
	
	    return found;
	}
	
}
