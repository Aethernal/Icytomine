package plugins.faubin.cytomine.utils.contours.methode1.swip_maillon;
///*    swip_Maillon.c
// *      
// * DESCRIPTION
// *      
// *
// * AUTHOR
// *
// *      Giraudon, Copyright INRIA projet PASTIS
// *	Gerard.Giraudon@sophia.inria.fr
// *
// * UPDATES
// *
// *     03/05/1991 
// *
// * Version : 1.0 
// * Date of IP version : 06/04/2000
// ******************************/
//
//
//
//#ifndef DEPEND
//#include<stdio.h>
//#endif DEPEND
//
//#include <malloc.h>
//
//
//#include "Maillon.h"
//
//#define OPEN	0
//#define CLOSE	1
//#define CONTOUR 2
//
//extern LinkerPar LeLinker;
//
///******************************/
///* Memory Management Routines */
///******************************/
//
//
//
//Chainon 
//alloc_chainon()
//{
//	Chainon UnChainon;
//	if (!LeLinker->nb_chainons_pile--) return NULL;
//	else
//	{
//		UnChainon = LeLinker->chainons_pile++;
//		return UnChainon;
//	}
//}
//
//
//Chaine 
//alloc_chaine()
//{
//	Chaine UneChaine;
//	if (!LeLinker->nb_chaines_pile--) return NULL;
//	else
//	{
//		UneChaine = LeLinker->chaines_pile++;
//		return UneChaine;
//	}
//}
//
//
//Chaine
//chaine_suivante(linker,pi)
//LinkerPar linker;
//int *pi;
//{
//      int j;
//      Chaine	*kbuf;
//	
//      kbuf = linker->chaines;
//      for (j = *pi; j < linker->nb_chaines_max; j++)
//          if (kbuf[j] != NULL) 
//		{
//			*pi = j+1;
//			return kbuf[j];
//		}
//
//	*pi = linker->nb_chaines_max;
//	return NULL;
//}
//
//
///*
//    opench_: function which creates a new chain of maillons, that is the bloody INRIA's structure.
//*/
//
//Chaine opench_()
//{
//    Chaine UneChaine;
//    int i;
//
//    /* Allocate 3 items, returning NULL in case of failure. */
//
//    UneChaine = alloc_chaine();
//
//    if (UneChaine==NULL) return NULL;
//
//    /* Set up fields of head. */
//
//    UneChaine->mode = OPEN;
//    UneChaine->dernier = NULL;
//    UneChaine->numero = ++(LeLinker->numero);
//
//    /* Presently there are no relations. */
//
//    for ( i=0 ; i<4 ; ++i) {
//    	UneChaine->tete.famille[i] = 0;	/* pas d'aieul==AVI...(aulico, no?) */
//	UneChaine->queue.famille[i] = 0;	/* pas de fils */
//    }
//
//    /* Return the pointer to the new list. */
//
//    return(UneChaine);
//}
//
//
//
//
//
//
//
///*
//    nwmail_: function which inserts a new edge pixel in the chain.
//*/
//
//Chainon nwmail_(UneChaine,ix,iy)
//Chaine UneChaine;
//int ix,iy;
//{
//    Chainon UnChainon;
//
//    UnChainon = alloc_chainon();
//    if (UnChainon!=NULL) {
//    	UnChainon->succ = NULL;
//    	UnChainon->x = ix;
//    	UnChainon->y = iy;
//	if (UneChaine->dernier == NULL)
//		UneChaine->premier = UneChaine->dernier = UnChainon;
//	else
//		UneChaine->dernier = UneChaine->dernier->succ = UnChainon;
//
//    }
//    return UnChainon;
//}
//
///*
//    nbmail_: function which computes the number of pixels in a chain.
//*/
//
//int nbmail_(UneChaine)
//Chaine UneChaine;
//{
//	Chainon UnChainon = UneChaine->premier;
//	int i=0;
//
//	while ( UnChainon != NULL ) 
//	{
//		i++;
//		UnChainon = UnChainon->succ;
//	}
//	return i;
//}
//
//
///*
//    aieul_,fils_
//    Function which inserts a parent/son in the chain description.
//*/
//
//int aieul_(UneChaine,aieul)
//Chaine UneChaine;
//int aieul;
//{
//    int i;
//
//    if (abs(aieul) == UneChaine->numero) return 0;
//
//    for (i=0 ; i<4 ; i++) {
//    	if (UneChaine->tete.famille[i] == 0 ) {
//    	    UneChaine->tete.famille[i] = aieul;
//	    return 1;
//	}
//	if ( abs(UneChaine->tete.famille[i]) == abs(aieul) ) return 0;
//    }
//
//    return 0;
//}
//
//int fils_(UneChaine,fils)
//Chaine UneChaine;
//int    fils;	
//{
//    int i;
//
//    if (abs(fils) == UneChaine->numero) return 0;
//
//    for ( i=0 ; i<4 ; ++i) {
//    	if ( UneChaine->queue.famille[i] == 0 ) {
//    	    UneChaine->queue.famille[i] = fils ;
//	    return 1;
//	}
//	if( abs(UneChaine->queue.famille[i]) == abs(fils) ) return 0;
//    }
//
//    return 0;
//}
//
//
///*
//    aieul_b,fils_b
//    mise en place d'un aieul/fils dans refiliat
//*/
//
//int aieul_b(UneChaine,aieul)
//Chaine UneChaine;
//int aieul;
//{
//    int i;
//
//
//    for ( i=0 ; i<4 ; ++i) {
//    	if ( UneChaine->tete.famille[i] == 0 ) {
//    	    UneChaine->tete.famille[i] = aieul ;
//	    return 1;
//	}
//	if ( UneChaine->tete.famille[i] == aieul ) return 0;
//    }
//
//    return 0;
//}
//
//int fils_b(UneChaine,fils)
//Chaine UneChaine;
//int fils;	
//{
//    register int i;
//
//
//    for ( i=0 ; i<4 ; ++i) {
//    	if ( UneChaine->queue.famille[i] == 0 ) {
//    	    UneChaine->queue.famille[i] = fils ;
//	    return 1;
//	}
//	if ( UneChaine->queue.famille[i] == fils ) return 0;
//    }
//
//    return 0;
//}
//
//
///*
//    sraieul_,srfils_
//    function which searches chain #n_ancien in the parents/sons of tete, replacing it which #n_new.
//    Returns true or false according to result of action.
//*/
//
//int sraieul_(UneChaine,n_ancien,n_new)
//Chaine UneChaine;
//int n_ancien,n_new;
//{
//    int i;
//
//    for ( i=0 ; i<4 ; ++i) {
//    	if ( UneChaine->tete.famille[i] == 0 ) break;
//	if ( abs(UneChaine->tete.famille[i]) == n_ancien ) {
//    	    UneChaine->tete.famille[i] = n_new; 
//	    return(1);
//	}
//    }
//
//    return 0;
//}
//
//int srfils_(UneChaine,n_ancien,n_new)
//Chaine UneChaine;
//int n_ancien,n_new;
//{
//    int i;
//
//    for ( i=0 ; i<4 ; ++i) {
//    	if ( UneChaine->queue.famille[i] == 0 ) break;
//	if ( abs(UneChaine->queue.famille[i]) == n_ancien ) {
//    	    UneChaine->queue.famille[i] = n_new; 
//	    return 1;
//	}
//    }
//
//    return 0;
//}
//
///*
//    laieul_,lfils_
//    function which counts the number of parents/sons and returns the pointer to the array famille
//*/
//
//int *laieul_(UneChaine,nb)
//Chaine UneChaine;
//int *nb;
//{
//    int i,*aieul;
//
//    *nb = 0;
//    aieul = UneChaine->tete.famille;
//    for ( i=0 ; i<4 ; ++i) if ( aieul[i]  == 0 ) break; else (*nb)++;
//    return aieul;
//}
//
//int *lfils_(UneChaine,nb)
//Chaine UneChaine;
//int *nb;
//{
//    int i,*fils;
//	
//    *nb = 0;
//    fils = UneChaine->queue.famille;
//    for ( i=0 ; i<4 ; ++i) if ( fils[i] == 0 ) break; else (*nb)++;
//    return fils;
//}
//
///*
//    ssaieul_,ssfils_
//    this function suppresses chain #nume parents/sons from table famille, recompacting it.
//    Returns 0 if no suppression, index of item+1 if ok.
//*/
//
//int ssaieul_ (UneChaine,nume)
//Chaine UneChaine;
//int nume ;
//{
//    int i,found = 0 ;
// 
//    nume = abs(nume);
//
//    /* Search 'nume' */
//
//    for ( i=0 ; i<4 ; ++i) {
//    	if (UneChaine->tete.famille[i] == 0) break ;	
//	if (abs(UneChaine->tete.famille[i]) == nume) found = i+1 ;
//    }
//
//    /* Recompact famille. */
//
//    if (found) {
//    	UneChaine->tete.famille[found-1] = UneChaine->tete.famille[i-1] ;
//	UneChaine->tete.famille[i-1] = 0 ;
//    }
//
//    return found;
//}
//
//int ssfils_ (UneChaine,nume)
//Chaine UneChaine;
//int nume ;  
//{
//    int i,found = 0 ;
// 
//    nume = abs(nume);
//
//    /*  Search for 'nume'. */
//
//    for ( i=0 ; i<4 ; ++i)  {
//    	if (UneChaine->queue.famille[i] == 0) break ;	
//	if (abs(UneChaine->queue.famille[i]) == nume) found = i+1 ;
//    }
//
//    /* Compact 'famille'. */
//
//    if (found) {
//    	UneChaine->queue.famille[found-1]=UneChaine->queue.famille[i-1] ;
//    	UneChaine->queue.famille[i-1] = 0 ;
//    }
//    
//    return found;
//}
//
///*
//    nbfils,nbaieul
//    If the chain x has only a son/parent, return it, otherwise return NULL
//*/
//
//Chaine nbfils(UneChaine,kbuf,idebug) 
//Chaine UneChaine,kbuf[];
//int    idebug;
//{ 
//    int nb,i,*fils;
//
//    fils = (int *) lfils_(UneChaine,&nb);
//    
//    for ( i=0 ; i < nb ; i++ ) { 
//    	if (abs( fils[i]) == UneChaine->numero) {
//    	    UneChaine->mode = CONTOUR;
//	    return(NULL);
//	}
//    }
//
//    if ( nb == 1) return( kbuf[ abs(fils[0]) -1] ); else return(NULL);
//}
//
//Chaine nbaieul(UneChaine,kbuf,idebug) 
//Chaine UneChaine,kbuf[];
//int    idebug;
//{ 
//    int nb,i,*aieul;
//    aieul = (int *) laieul_(UneChaine,&nb);
//
//    for ( i=0 ; i < nb ; i++ ) { 
//    	if ( abs(aieul[i]) == UneChaine->numero) {
//    	    UneChaine->mode = CONTOUR;
//    	    return(NULL);
//	}
//    }
//
//    if ( nb == 1) return( kbuf[ abs(aieul[0]) -1] ); else return(NULL);
//}
//
///*
//    fuschn_: this function queues chain suite to the end of chain tete, giving to it its sons.
//*/
//
//fuschn_(UneChaine,UneChaineSuite)
//Chaine UneChaine,UneChaineSuite;
//{	
//    int i;
//
//    UneChaine->dernier->succ =  UneChaineSuite->premier;
//    UneChaine->dernier = UneChaineSuite->dernier ;
//
//    for ( i=0 ; i<4 ; ++i)
//    	UneChaine->queue.famille[i] = UneChaineSuite->queue.famille[i] ;
//}
//
///*
//    miroir_
//    Inverts the orientation of a chain.
//*/
//
//miroir_(UneChaine)
//Chaine UneChaine;
//{
//    Chainon UnChainon,UnAutreChainon,UnChainonTemp;
//    int numero,i;
//
//    /* Set up. */
//
//    UnChainon = UneChaine->premier;
//    UnAutreChainon = UnChainon->succ;
//
//    /* Si UnChainon == NULL ... On est mal !! */
//
//    UneChaine->premier = UneChaine->dernier;
//    UneChaine->dernier = UnChainon;
//
//    /* Invert list of pixels. */
//
//    while ( UnAutreChainon != NULL ) {
//	UnChainonTemp = UnAutreChainon->succ;
//	UnAutreChainon->succ = UnChainon;
//	UnChainon = UnAutreChainon;
//	UnAutreChainon = UnChainonTemp;
//    }
//
//
//    /* Swap parents with sons. */
//
//    for ( i=0 ; i<4 ; ++i) {
//    	numero = UneChaine->tete.famille[i] ;
//	UneChaine->tete.famille[i] = UneChaine->queue.famille[i];
//	UneChaine->queue.famille[i] = numero;
//    } 
//
//    /* Null terminate list of pixels. */
//
//    UneChaine->dernier->succ = NULL;
//
//}
//
//
///*
//    invers_orient
//    This function modifes for each chain of the famille the one with number 'numero',
//    inverting its sign.
//*/
//
//int invers_orient(kbuf,famille,numero,idebug)
//	int famille[],numero,idebug;
//	Chaine	kbuf[];
//{
//    int i,j;
//    Chaine	UneChaine;
//
//    for (i=0 ; i<4 ; i++) { 
//	if (famille[i] == 0) return(1) ; 
//	if ( famille[i] > 0) {
//	    UneChaine = kbuf[ famille[i] -1 ];
//	    for (j=0 ; j<4 ; j++)
//		if (UneChaine->tete.famille[j] == numero) {
//    		    UneChaine->tete.famille[j]= -numero;
//		    return(1);
//		}
//		return(0);
//    	} else {
//	    UneChaine = kbuf[ -(famille[i]) -1 ];
//	    for (j=0 ; j<4 ; j++)
//		if (UneChaine->queue.famille[j] == numero) {
//    		    UneChaine->queue.famille[j]= -numero;
//		    return(1);
//    	    }
//	    return(0);
//    	}
//    }
//    return(0);
//}
//
//
///*
//    inclure
//*/
//
///*	INCLURE :  fonction qui doit inclure la chaine p dans la chaine x . */
///*		  							    */
///*			._______x________. ._______p_______.		    */
///*				     typex typep			    */
///*									    */
///*      type_  =  'F' pour une fin de chaine.				    */
///*	          'D' pour un debut de chaine.				    */
///*      Si typep = typex alors il est necessaire de retourner une des chaine*/
///*									    */
///*      la fonction retourne un pointeur sur la chaine resultat		    */
//
//Chaine inclure(kbuf,UneChaineP,typep,UneChaineX,typex,idebug)
//char typep,typex;
//Chaine UneChaineP,UneChaineX,kbuf[];
//int idebug;
//{ 
//    int numero_x;
//
//    if (typex == typep) {
//    	miroir_(UneChaineP);
//	if ( !(invers_orient(kbuf,UneChaineP->tete.famille,
//				-(UneChaineP->numero),idebug )) )
//    		printf("INCLURE : erreur algo UneChaineP->tete\n"); 
//	if ( !(invers_orient(kbuf,UneChaineP->queue.famille,
//				UneChaineP->numero,idebug )) )
//		printf("INCLURE : erreur algo UneChaineP->queue\n"); 
//    }
//
//    numero_x = UneChaineX->numero;
//
//    if (typex == 'D' ) {
//    	fuschn_(UneChaineP,UneChaineX);
//	UneChaineP->numero = numero_x;
//	kbuf[ numero_x -1 ] =UneChaineP;
//	return( UneChaineP );
//    } else {
//    	fuschn_(UneChaineX,UneChaineP);
//	kbuf[ numero_x -1 ]=UneChaineX;
//	return( UneChaineX );
//    }
//}
//
///*
//    remp_ds_famille
//*/    
///*	REMP_DS_FAMILLE : pour chaque chaine j de "famille" on remplace      */
///*		  	  "n_ancien" dans le lien de filiation ou il         */
///*                         apparait par "n_new" .		             */
//
//remp_ds_famille(kbuf,famille,n_new,n_ancien,idebug)
//int famille[];
//Chaine kbuf[];
//int idebug;
//int n_ancien, n_new;
//{ 
//    Chaine UneChaine;
//    int j;
//
//    for ( j=0 ; j<4 ; j++) {
//    	if (famille[j] == 0) break;
//	if (famille[j] > 0) {
//    	    UneChaine = kbuf[ famille[j] -1 ];
//	    if ( !(sraieul_(UneChaine,n_ancien,n_new)))
//		ssaieul_(UneChaine,abs(n_new));
//    	} else {
//	    UneChaine = kbuf[ -(famille[j]) -1 ]; 
//	    if (!(srfils_ (UneChaine,n_ancien,n_new)))  
//		ssfils_(UneChaine,abs(n_new)) ; 
//	}
//    }
//}
//
///*
//    supp_ds_famille
//    For every chain in famille suppress link with 'number'.
//*/    
//
//supp_ds_famille(kbuf,famille,number,idebug)
//int famille[];
//Chaine kbuf[];
//int idebug;
//int number;
//{ 
//    Chaine UneChaine;
//    int j;
//
//    for ( j=0 ; j<4 ; j++) {
//    	if (famille[j] == 0) break;
//	if (famille[j] > 0) {
//    	    UneChaine = kbuf[ famille[j] -1 ];
//	    ssaieul_(UneChaine,number);
//	} else {
//	    UneChaine = kbuf[ -(famille[j]) -1 ];
//	    ssfils_ (UneChaine,number);
//	}
//    }
//}
//
///*
//    un_seul_lien
//    It is a function which searches the chain 'numero' among parents and sons of present chain.
//    If the chain belongs to the parents, typep='D', if sons 'F'.
//    The function return true if the is just an element in the family, false otherwise.
//*/
//
//int un_seul_lien( numero,UneChaine,typep,idebug)
//Chaine UneChaine;
//int    numero ;
//char   *typep ;
//int    idebug ;
//{
//    
//    if (numero > 0 ) {
//    	*typep = 'D';
//	return( !(UneChaine->tete.famille[1] ) );
//    } else {
//    	*typep = 'F';
//	return( !(UneChaine->queue.famille[1] ) );
//    }
//}
//
///*******************************************************************************************************/
///*******************************************************************************************************/
///*******************************************************************************************************/
//
///*    TSUPTEST   :   Cette fonction permet de tester si la chaine "tete"   */
///*                   qui est de la forme suivante peut etre supprimee      */ 
///*                                                /                        */
///*                                     .________./___                      */
///*                                       tete    \                         */
///*                                                \                        */
///*                                                                         */
///*                  Elle retourne "vrai" si c'est possible.                */
///*                   Hypotheses :  "nb" > 0                               */
///*                                 "famille"  famille de "tete"            */
//
//TsupTest (UneChaine,kbuf,nb,famille,idebug) 
//Chaine UneChaine , kbuf[] ; 
//int nb ;
//int famille[] ;
//int idebug ;   
//{
//    char type  ;
//
//    if (nb >= 2) return (1) ;
//	
//        /*--------------   ici nb == 1  -----------------------------------*/
//
//    if (famille[0] > 0) 
//return( ( !(un_seul_lien( UneChaine->numero,kbuf[famille[0] -1 ],&type, idebug) ) ) );
//    else
//return( ( !(un_seul_lien( -( UneChaine->numero),kbuf[abs(famille[0]) -1 ],&type, idebug) ) ) );
//}
//
///*    REFILIAT   :   Cette fonction retablit la filiation apres la           */
///*                   suppression d'un lien                                   */
//
//refiliat (kbuf,progen,numprogen,idebug)
//Chaine kbuf[] ;
//int progen[] ;
//int numprogen ;  
//int idebug ;   
//{
//    Chaine UneChaine;
//    int j ;
//
//    UneChaine = kbuf[abs(progen[0])-1] ;
//    if (progen[0] > 0) 
//    	for (j=1 ; j< numprogen ; j++) {
//    	    if (progen[j] >0) 
//		if (aieul_b ( UneChaine,progen[j] ) ) 
//			aieul_b ( kbuf[progen[j]-1],progen[0] ) ;
//	    if (progen[j] < 0) 
//		if (aieul_b  ( UneChaine,progen[j] ) ) 
//			fils_b  ( kbuf[abs(progen[j])-1],progen[0] ) ;
//	}
//
//    if (progen[0] < 0)
//    	for (j=1 ; j< numprogen ; j++) {
//    	    if (progen[j] > 0)
//		if (aieul_b( kbuf[progen[j]-1],progen[0] ) ) 
//			fils_b ( UneChaine,progen[j] );
//    	    if (progen[j] < 0)
//    		if (fils_b ( kbuf[abs(progen[j])-1],progen[0] ) ) 
//			fils_b ( UneChaine,progen[j] );
//    	}
//}
//
