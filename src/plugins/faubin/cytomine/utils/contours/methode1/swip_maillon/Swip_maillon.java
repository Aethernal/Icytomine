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
	
	public static int aieul_(Chaine UneChaine, int aieul){
	    int i;
	
	    if (Math.abs(aieul) == UneChaine.numero) return 0;
	
	    for (i=0 ; i<4 ; i++) {
	    	if (UneChaine.tete.famille[i] == 0 ) {
	    	    UneChaine.tete.famille[i] = aieul;
		    return 1;
		}
		if ( Math.abs(UneChaine.tete.famille[i]) == Math.abs(aieul) ) return 0;
	    }
	
	    return 0;
	}
	
	public static int fils_(Chaine UneChaine, int fils){
	    int i;
	
	    if (Math.abs(fils) == UneChaine.numero) return 0;
	
	    for ( i=0 ; i<4 ; ++i) {
	    	if ( UneChaine.queue.famille[i] == 0 ) {
	    	    UneChaine.queue.famille[i] = fils ;
		    return 1;
		}
		if( Math.abs(UneChaine.queue.famille[i]) == Math.abs(fils) ) return 0;
	    }
	
	    return 0;
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
	
	public static boolean TsupTest (Chaine UneChaine,Chaine[] kbuf, int[] nbrea, int[] famille, int idebug) {
	    Character type = null ;
	
	    if (nbrea[0] >= 2){ return true; };
		
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
	
	public static void refiliat (Chaine[] kbuf,int[] progen, int[] nbrea, int idebug){
	    Chaine UneChaine;
	    int j ;
	
	    UneChaine = kbuf[Math.abs(progen[0])-1] ;
	    if (progen[0] > 0) 
	    	for (j=1 ; j< nbrea[0] ; j++) {
	    	    if (progen[j] >0) 
			if (aieul_b ( UneChaine,progen[j] ) ) 
				aieul_b ( kbuf[progen[j]-1],progen[0] ) ;
		    if (progen[j] < 0) 
			if (aieul_b  ( UneChaine,progen[j] ) ) 
				fils_b  ( kbuf[Math.abs(progen[j])-1],progen[0] ) ;
		}
	
	    if (progen[0] < 0){
	    	for (j=1 ; j< nbrea[0] ; j++) {
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
	
	public static void supp_ds_famille(Chaine[] kbuf,int[] parent, int number, int idebug){ 
	    Chaine UneChaine;
	    int j;
	
	    for ( j=0 ; j<4 ; j++) {
	    	if (parent[j] == 0) break;
		if (parent[j] > 0) {
	    	    UneChaine = kbuf[ parent[j] -1 ];
		    ssaieul_(UneChaine,number);
		} else {
		    UneChaine = kbuf[ -(parent[j]) -1 ];
		    ssfils_ (UneChaine,number);
		}
	    }
	}
	
	public static void remp_ds_famille(Chaine[] kbuf, int[] famille, int n_new, int n_ancien, int idebug){ 
	    Chaine UneChaine;
	    int j;
	
	    for ( j=0 ; j<4 ; j++) {
	    	if (famille[j] == 0) break;
		if (famille[j] > 0) {
	    	    UneChaine = kbuf[ famille[j] -1 ];
		    if ( !( (sraieul_(UneChaine,n_ancien,n_new) != 0 ) ) )
			ssaieul_(UneChaine,Math.abs(n_new));
	    	} else {
		    UneChaine = kbuf[ -(famille[j]) -1 ]; 
		    if (!( (srfils_ (UneChaine,n_ancien,n_new)) != 0 ))  
			ssfils_(UneChaine,Math.abs(n_new)) ; 
		}
	    }
	}
	
	public static int sraieul_(Chaine UneChaine, int n_ancien, int n_new){
	    int i;
	
	    for ( i=0 ; i<4 ; ++i) {
	    	if ( UneChaine.tete.famille[i] == 0 ) break;
			if ( Math.abs(UneChaine.tete.famille[i]) == n_ancien ) {
		    	    UneChaine.tete.famille[i] = n_new; 
			    return(1);
			}
	    }
	
	    return 0;
	}
	
	public static int srfils_(Chaine UneChaine, int n_ancien, int n_new){
	    int i;
	
	    for ( i=0 ; i<4 ; ++i) {
	    	if ( UneChaine.queue.famille[i] == 0 ) break;
			if ( Math.abs(UneChaine.queue.famille[i]) == n_ancien ) {
		    	    UneChaine.queue.famille[i] = n_new; 
			    return 1;
			}
	    }
	
	    return 0;
	}
	
	public static int[] laieul_(Chaine UneChaine, int[] nbrea){
	    int i;
	    int[] aieul;
	
	    nbrea[0] = 0;
	    aieul = UneChaine.tete.famille;
	    for ( i=0 ; i<4 ; ++i) if ( aieul[i]  == 0 ) break; else (nbrea[0])++;
	    return aieul;
	}
	
	public static int[] lfils_(Chaine UneChaine, int[] nbref){
	    int i;
	    int[] fils;
		
	    nbref[0] = 0;
	    fils = UneChaine.queue.famille;
	    for ( i=0 ; i<4 ; ++i) if ( fils[i] == 0 ) break; else (nbref[0])++;
	    return fils;
	}
	
	/*
	    ssaieul_,ssfils_
	    this function suppresses chain #nume parents/sons from table famille, recompacting it.
	    Returns 0 if no suppression, index of item+1 if ok.
	*/
	
	public static int ssaieul_ (Chaine UneChaine, int nume){
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
	
	public static int ssfils_ (Chaine UneChaine, int nume){
	    int i,found = 0 ;
	 
	    nume = Math.abs(nume);
	
	    /*  Search for 'nume'. */
	
	    for ( i=0 ; i<4 ; ++i)  {
	    	if (UneChaine.queue.famille[i] == 0) break ;	
		if (Math.abs(UneChaine.queue.famille[i]) == nume) found = i+1 ;
	    }
	
	    /* Compact 'famille'. */
	
	    if (found!=0) {
	    	UneChaine.queue.famille[found-1]=UneChaine.queue.famille[i-1] ;
	    	UneChaine.queue.famille[i-1] = 0 ;
	    }
	    
	    return found;
	}
	
	/*
	    nbfils,nbaieul
	    If the chain x has only a son/parent, return it, otherwise return NULL
	*/
	
	public static Chaine nbfils(Chaine UneChaine,Chaine[] kbuf, int idebug){ 
	    
	    int i,nb[] = new int[]{0}, fils[];
	
	    fils = lfils_(UneChaine,nb);
	    
	    for ( i=0 ; i < nb[0] ; i++ ) { 
	    	if (Math.abs( fils[i]) == UneChaine.numero) {
	    	    UneChaine.mode = CONTOUR;
		    return(null);
		}
	    }
	
	    if ( nb[0] == 1) return( kbuf[ Math.abs(fils[0]) -1] ); else return(null);
	}
	
	public static Chaine nbaieul(Chaine UneChaine, Chaine[] kbuf, int idebug) { 
	    int nb[] = new int[]{0},i,aieul[];
	    aieul =  laieul_(UneChaine,nb);
	
	    for ( i=0 ; i < nb[0] ; i++ ) { 
	    	if (Math.abs(aieul[i]) == UneChaine.numero) {
	    	    UneChaine.mode = CONTOUR;
	    	    return(null);
		}
	    }
	
	    if ( nb[0] == 1) return( kbuf[ Math.abs(aieul[0]) -1] ); else return(null);
	}
	
	/*
	    fuschn_: this function queues chain suite to the end of chain tete, giving to it its sons.
	*/
	
	public static void fuschn_(Chaine UneChaine,Chaine UneChaineSuite){	
	    int i;
	
	    UneChaine.dernier.succ =  UneChaineSuite.premier;
	    UneChaine.dernier = UneChaineSuite.dernier ;
	
	    for ( i=0 ; i<4 ; ++i)
	    	UneChaine.queue.famille[i] = UneChaineSuite.queue.famille[i] ;
	}
	
	/*
	    miroir_
	    Inverts the orientation of a chain.
	*/
	
	public static void miroir_(Chaine UneChaine){
	    Chainon UnChainon,UnAutreChainon,UnChainonTemp;
	    int numero,i;
	
	    /* Set up. */
	
	    UnChainon = UneChaine.premier;
	    UnAutreChainon = UnChainon.succ;
	
	    /* Si UnChainon == NULL ... On est mal !! */
	
	    UneChaine.premier = UneChaine.dernier;
	    UneChaine.dernier = UnChainon;
	
	    /* Invert list of pixels. */
	
	    while ( UnAutreChainon != null ) {
		UnChainonTemp = UnAutreChainon.succ;
		UnAutreChainon.succ = UnChainon;
		UnChainon = UnAutreChainon;
		UnAutreChainon = UnChainonTemp;
	    }
	
	
	    /* Swap parents with sons. */
	
	    for ( i=0 ; i<4 ; ++i) {
	    	numero = UneChaine.tete.famille[i] ;
		UneChaine.tete.famille[i] = UneChaine.queue.famille[i];
		UneChaine.queue.famille[i] = numero;
	    } 
	
	    /* Null terminate list of pixels. */
	
	    UneChaine.dernier.succ = null;
	
	}
	
	public static int invers_orient(Chaine[] kbuf,int[] famille, int numero, int idebug){
	    int i,j;
	    Chaine	UneChaine;
	
	    for (i=0 ; i<4 ; i++) { 
		if (famille[i] == 0) return(1) ; 
		if ( famille[i] > 0) {
		    UneChaine = kbuf[ famille[i] -1 ];
		    for (j=0 ; j<4 ; j++)
			if (UneChaine.tete.famille[j] == numero) {
	    		    UneChaine.tete.famille[j]= -numero;
			    return(1);
			}
			return(0);
	    	} else {
		    UneChaine = kbuf[ -(famille[i]) -1 ];
		    for (j=0 ; j<4 ; j++)
			if (UneChaine.queue.famille[j] == numero) {
	    		    UneChaine.queue.famille[j]= -numero;
			    return(1);
	    	    }
		    return(0);
	    	}
	    }
	    return(0);
	}
	
	public static Chaine inclure(Chaine[] kbuf,Chaine UneChaineP,char typep, Chaine UneChaineX, char typex, int idebug){ 
	    int numero_x;
	
	    if (typex == typep) {
	    	miroir_(UneChaineP);
		if ( !((invers_orient(kbuf,UneChaineP.tete.famille,
					-(UneChaineP.numero),idebug )) != 0 ) )
	    		System.out.println("INCLURE : erreur algo UneChaineP.tete\n"); 
		if ( !((invers_orient(kbuf,UneChaineP.queue.famille,
					UneChaineP.numero,idebug )) != 0 ) )
			System.out.println("INCLURE : erreur algo UneChaineP.queue\n"); 
	    }
	
	    numero_x = UneChaineX.numero;
	
	    if (typex == 'D' ) {
	    	fuschn_(UneChaineP,UneChaineX);
		UneChaineP.numero = numero_x;
		kbuf[ numero_x -1 ] =UneChaineP;
		return( UneChaineP );
	    } else {
	    	fuschn_(UneChaineX,UneChaineP);
		kbuf[ numero_x -1 ]=UneChaineX;
		return( UneChaineX );
	    }
	}
	
}
