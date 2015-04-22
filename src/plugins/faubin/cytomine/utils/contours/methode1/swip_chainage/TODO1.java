///* NAME 
// *     swip_Linker.c 
// *       Extraction et Chainage des points de contour
// *
// * DESCRIPTION 
// * 
// *	LeLinker_go,init_kbruit_kfus,fusion,supp_bruit,bruitage 
// * 
// * AUTHOR 
// * 
// *      Giraudon, Copyright INRIA projet PASTIS 
// *	Gerard.Giraudon@sophia.inria.fr 
// * 
// * 
// * UPDATES 
// * 
// *     03/05/1991  
// * Version : 1.0 
// * Date of IP version : 06/04/2000
// */
// 
// 
// 
//#ifndef DEPEND 
//#include <stdio.h> 
//#include <math.h> 
//#include <ctype.h> 
//#endif DEPEND 
// 
// 
//#include <malloc.h>
//
// 
//#include "Maillon.h" 
// 
//extern LinkerPar LeLinker; 
// 
//void LeLinker_go(idebug) 
//	int             idebug; 
//{ 
//	short		*edge; 
//	Chaine 		*kbuf; 
//	int 		*kbruit;	/* Tableau pour l'elimination du bruit */ 
//	Chaine  	*kfus;		/* Tableau pour la fusion de chaines */ 
//	int             ibruit; 
//	int 		lx,ly; 
//	int             i, j, l1, l2, l3, nc; 
//	int             nbsuppres = 0, s_nbsup = 0; 
//	int             nb_bruit = 0, nb_fusion = 0; 
// 
//	LeLinker->nb_chaines = 0; 
//	LeLinker->numero = 0; 
//	nc = 0; 
//	edge = LeLinker->edge; 
//	lx = LeLinker->dim_x; 
//	ly = LeLinker->dim_y; 
//	ibruit = LeLinker->ibruit; 
//	kbuf = LeLinker->chaines; 
// 
//	/* Internal initialization. */ 
// 
//	l1 = 0; 
//	l2 = 1; 
//	initgtot_(); 
//	for (i=0;i<LeLinker->nb_chaines_max;i++) kbuf[i] = NULL; 
// 
// 
//	/* First stage: Finite State Automata (FSA). */ 
// 
///******** printf("Stage 1:FSA\n"); *****************************/ 
// 
//	/* Initialize line l1 of itab. */ 
// 
//	initab_(l1, LeLinker->itab); 
// 
//	/* For every scan line... */ 
// 
//	for (i = 0; i < lx; i++) { 
// 
//		/* Initialize present state line of itab (it is dirty). */ 
// 
//		initab_(l2, LeLinker->itab); 
// 
//		/* For every pixel of the line... */ 
// 
//		for (j = 1; j < ly-1; j++) { 
// 
//			/* If edge pixel call FSA. */ 
// 
//			if (*(edge + i + j * lx)) { 
//				FSA(i, j, edge, LeLinker->itab, l1, l2, kbuf, idebug); 
//				nc = nc + 1; 
//			} 
//		} 
// 
//		/* Swap l1 with l2. */ 
// 
//		l3 = l1; 
//		l1 = l2; 
//		l2 = l3; 
//	} 
// 
///*******printf("Number of chains          :%d\n", LeLinker->nb_chaines); 
//	printf("Number of processed pixels:%d\n", nc);   ***********************/ 
// 
// 
///*******printf("Stage 2: Fusion & Noise Suppression\n"); ***************/ 
// 
// 
//	init_kbruit_kfus(kbuf, LeLinker->nb_chaines, ibruit); 
// 
//	for (i = 1; i <= ibruit; i++) { 
//		s_nbsup = nbsuppres; 
//		fusion(kbuf, LeLinker->nb_chaines, &nbsuppres, ibruit, idebug); 
//		nb_fusion += nbsuppres - s_nbsup; 
//		s_nbsup = nbsuppres; 
//		bruitage(kbuf, LeLinker->nb_chaines, i, &nbsuppres, idebug); 
//		nb_bruit += nbsuppres - s_nbsup; 
//	} 
//	s_nbsup = nbsuppres; 
// 
//	fusion(kbuf, LeLinker->nb_chaines, &nbsuppres, ibruit, idebug); 
// 
//	nb_fusion += nbsuppres - s_nbsup; 
// 
///********printf("Number of noise =%d\n", nb_bruit); 
//	printf("Number of fusion=%d\n", nb_fusion); 
//	printf("Number of chains=%d\n", (LeLinker->nb_chaines -= nbsuppres)); ********/ 
// 
//	/* 
//	printf("Stage 3: Heaping\n");  
//	tassage (kbuf,nomcha,idebug) ; 
//	nomcha -= nbsuppres; 
//	wrtcnt_(kbuf,nomcha);  
//	*/ 
//	 
//} 
// 
// 
///* 
// * This function initializes the tables kbruit and kfus, meant to make more 
// * efficient the fusion and suppression of chains in case of length >1. Both 
// * tables are indexed by number of chain.  
// * 
// * Kbruit:this table contains the length of the chain with a sign which is + in 
// * case the chain is <= than ibruit, - otherwise. The chains to be processed 
// * for noise elimination are those whose kbruit[i]>0.  
// * 
// * Kfus  :for chains that cannot be fused, kfus[i]!=NULL, otherwise ==NULL. At 
// * start every chain is deemed capable of being fused.  
// */ 
// 
//init_kbruit_kfus(kbuf, nomcha, ibruit) 
//	Chaine 		*kbuf; 
//	int             nomcha, ibruit; 
//{ 
//	int             taille, i; 
//	int 		*kbruit;	/* Tableau pour l'elimination du bruit */ 
//	Chaine  	*kfus;		/* Tableau pour la fusion de chaines */ 
// 
//	kbruit = LeLinker->bruits; 
//	kfus = LeLinker->fusions; 
//	for (i = 0; i < nomcha; ++i) { 
//		if (kbuf[i] == NULL) { 
//			kbruit[i] = 0; 
//			kfus[i] = NULL; 
//		} else { 
//			if ((taille = nbmail_(kbuf[i])) <= ibruit) 
//				kbruit[i] = taille; 
//			else 
//				kbruit[i] = -taille; 
//			kfus[i] = kbuf[i]; 
//		} 
//	} 
//} 
// 
///* 
// * fusion:  
// * 
// * This function performs all possible chain fusion (2 chains can be fused if 
// * they have only one connection point common to them).  
// */ 
// 
//fusion(kbuf, nomcha, nbsuppres, s, idebug) 
//	int             nomcha, *nbsuppres, s, idebug; 
//	Chaine		kbuf[]; 
//{ 
//	int             i, bruit_total; 
//	char            bout[4]; 
//	Chaine		UneChaine,UneAutreChaine,nbaieul(); 
//	char            typep; 
//	int             j, numberp, *parents, nb_parents; 
//	Chaine		*kfus = LeLinker->fusions; 
//	int		*kbruit = LeLinker->bruits; 
// 
//	for (i = 0; i < nomcha; i++) { 
//	    if (kfus[i] != NULL) { 
//		if ((UneChaine = kbuf[i]) != NULL) { 
// 
//		    /* Si une chaine n'a qu'un seul lien avec une autre chaine 
//		    a sa queue, elle peuvent fusionner par la queue */  
// 
//		    while ((UneAutreChaine = nbfils(UneChaine, kbuf, idebug)) != NULL  
//	&& un_seul_lien(UneChaine->queue.famille[0], UneAutreChaine, &typep, idebug)) { 
// 
//			numberp = UneAutreChaine->numero; 
// 
//			/* Unlink chain from chains database */ 
// 
//			kbuf[numberp - 1] = NULL; 
// 
//			UneChaine =  
//			inclure(kbuf, UneAutreChaine, typep, UneChaine, 'F', idebug); 
// 
//			(*nbsuppres)++; 
// 
//			remp_ds_famille(kbuf,  
//					UneChaine->queue.famille,  
//					-(UneChaine->numero), numberp, idebug); 
// 
//			/* Compute total length. */ 
// 
//			bruit_total = abs(kbruit[i]) + abs(kbruit[numberp-1]); 
// 
//			/* 
//			 * If length is greater than error 
//			 * threshold, mark chain as 
//			 * "proteced".  
//			 */ 
// 
//			if (bruit_total > s) 
//				kbruit[i] = -bruit_total; 
//			else 
//				kbruit[i] = bruit_total; 
// 
//			/* Make fused chain disappear. */ 
// 
//			kbruit[numberp - 1] = 0; 
//			kfus[numberp - 1] = NULL; 
//		    } 
// 
//		    /* Si une chaine n'a qu'un seul lien avec une autre chaine 
//		    a sa tete, elle peuvent fusionner par la tete */  
// 
//		    while ((UneAutreChaine = nbaieul(UneChaine, kbuf, idebug)) != NULL   
//	&& un_seul_lien(UneChaine->tete.famille[0], UneAutreChaine, &typep, idebug)) { 
//			numberp = UneAutreChaine->numero; 
// 
//			/* Unlink chain from chains database */ 
// 
//			kbuf[numberp - 1] = NULL; 
// 
//			UneChaine =  
//			inclure(kbuf, UneAutreChaine, typep, UneChaine, 'D', idebug); 
// 
//			(*nbsuppres)++; 
// 
//			remp_ds_famille(kbuf, UneChaine->tete.famille,  
//				UneChaine->numero, numberp, idebug); 
// 
//			/* Compute total length. */ 
// 
//			bruit_total = abs(kbruit[i]) + abs(kbruit[numberp-1]); 
// 
// 
//			/* 
//			 * If length is greater than error 
//			 * threshold, mark chain as 
//			 * "proteced".  
//			 */ 
// 
//			if (bruit_total > s) 
//				kbruit[i] = -bruit_total; 
//			else 
//				kbruit[i] = bruit_total; 
// 
//			/* Make fused chain disappear. */ 
// 
//			kbruit[numberp - 1] = 0; 
//			kfus[numberp - 1] = NULL; 
//		    } 
// 
//		    kfus[i] = NULL; 
//		} 
//	    } 
//	} 
//} 
// 
///* SUPP_BRUIT    :  Cette fonction elinime la chaine  kbuf[i]            */ 
///* Elle met a jour la filiation des 'nb' chaines de     */ 
///* 'famille'					    */ 
// 
//supp_bruit(kbuf, i, nb, famille, nbsuppres, idebug) 
//	Chaine 		kbuf[]; 
//	int             i, nb, *nbsuppres, idebug; 
//	int             famille[]; 
//{ 
//	int             j, number; 
//	Chaine		*kfus = LeLinker->fusions; 
//	int		*kbruit = LeLinker->bruits; 
// 
//	if (TsupTest(kbuf[i], kbuf, nb, famille, idebug)) { 
//		supp_ds_famille(kbuf, famille, kbuf[i]->numero, idebug); 
//		refiliat(kbuf, famille, nb, idebug); 
//		kbuf[i] = NULL; 
//		kbruit[i] = 0; 
//		kfus[i] = NULL; 
//		kfus[abs(famille[0]) - 1] = kbuf[abs(famille[0]) - 1]; 
//		(*nbsuppres)++; 
//	} else 
//		kbruit[i] = -kbruit[i]; 
//} 
// 
///* 
// * bruitage:	this function suppresses all isolated chains of length<=s  
// */ 
// 
//bruitage(kbuf, nomcha, s, nbsuppres, idebug) 
//	Chaine		kbuf[]; 
//	int             nomcha, s, *nbsuppres; 
//	int             idebug; 
//{ 
//	int             i, red = 1; 
//	int             taille, nbref, nbrea; 
//	int            	*parent, *enfant; 
//	Chaine		*kfus = LeLinker->fusions; 
//	int		*kbruit = LeLinker->bruits; 
// 
//	for (i = 0; i < nomcha; i++) { 
//		if (kbruit[i] > 0 && kbuf[i] != NULL &&  
//				(taille = kbruit[i]) <= s) { 
//			enfant = lfils_(kbuf[i], &nbref); 
//			parent = laieul_(kbuf[i], &nbrea); 
// 
//			if ((nbref == 0) && (nbrea == 0)) { 
//				kbuf[i] = NULL; 
//				kbruit[i] = 0; 
//				kfus[i] = NULL; 
//				(*nbsuppres)++; 
//				continue; 
//			} 
//			if (nbref == 0) { 
//				supp_bruit(kbuf, i, nbrea, parent,  
//						nbsuppres, idebug); 
//				continue; 
//			} 
//			if (nbrea == 0) { 
//				supp_bruit(kbuf, i, nbref, enfant,  
//						nbsuppres, idebug); 
//				continue; 
//			} 
//			if (nbrea == 1 && nbref == 1 && 
//			    (abs(enfant[0]) == (kbuf[i])->numero) && 
//			    (abs(parent[0]) == (kbuf[i])->numero)) { 
//				kbuf[i] = NULL; 
//				kbruit[i] = 0; 
//				kfus[i] = NULL; 
//				(*nbsuppres)++; 
//			} 
//		} 
//	} 
//} 
// 
// 
// 
// 
///* TASSAGE   :   Cette fonction compacte le tableau "kbuf" de facon       
// * a ce qu'il ne contienne aucun pointeur NULL entre         
// * 1 et "nomcha - nbsuppress"                               
// *  
// * Elle rend le nombre "nbsupress" de pointeurs NULL       
// * enleves                                                
// * La numerotation de la filiation de chaque chaine      
// * est mise a jour   
// 
// * =====================================> Fonctionnalite deconnectee .... 
//                                    
// */ 
///* 
//tassage(kbuf, nomcha, idebug) 
//	struct maillon *kbuf[]; 
//	int             nomcha; 
//	int             idebug; 
//{ 
//	int            *adrkbuf; 
//	int             i, j, numb; 
//	struct maillon *x, *ext1, *ext2; 
// 
//	j = 0; 
//	adrkbuf = (int *) malloc(sizeof(int) * nomcha); 
//	for (i = 0; i < nomcha; i++) 
//		if (kbuf[i] != NULL) 
//			adrkbuf[i] = ++j; 
// 
//	for (i = 0; i < nomcha; i++) { 
//		x = (struct maillon *) kbuf[i]; 
//		if (x != NULL) { 
//			x->u.t.numero = adrkbuf[i]; 
//			ext1 = x->succ; 
//			ext2 = (x->succ)->succ; 
//			for (j = 0; j < 4; j++) { 
//				numb = ext1->u.e.famille[j]; 
//				if (numb == 0) 
//					break; 
//				if (numb < 0) 
//					ext1->u.e.famille[j] =  
//						-(adrkbuf[(-numb) - 1]); 
//				else 
//					ext1->u.e.famille[j] = adrkbuf[numb-1]; 
//			} 
//			for (j = 0; j < 4; j++) { 
//				numb = ext2->u.e.famille[j]; 
//				if (numb == 0) 
//					break; 
//				if (numb < 0) 
//					ext2->u.e.famille[j] =  
//						-(adrkbuf[(-numb) - 1]); 
//				else 
//					ext2->u.e.famille[j] = adrkbuf[numb-1]; 
//			} 
//			kbuf[adrkbuf[i] - 1] = x; 
//		} 
//	} 
//} 
// 
//*/ 