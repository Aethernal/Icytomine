package plugins.faubin.cytomine.utils.contours.methode1.swip_chainage;

import plugins.faubin.cytomine.utils.contours.methode1.maillons.Chaine;
import plugins.faubin.cytomine.utils.contours.methode1.maillons.LinkerPar;
import plugins.faubin.cytomine.utils.contours.methode1.swip_chaineExam.Swip_ChaineExam;
import plugins.faubin.cytomine.utils.contours.methode1.swip_maillon.Swip_maillon;

public class Swip_Chainage {
	
	static LinkerPar LeLinker; 
	
	public void bruitage(Chaine[] kbuf, int nomcha, int s, Integer nbsuppres, int idebug) { 
		int i, red = 1; 
		Integer taille, nbref = 0, nbrea = 0; 
		int[] parent;
		int[] enfant; 
		Chaine[] kfus = LeLinker.fusions; 
		Integer[] kbruit = LeLinker.bruits; 
	 
		for (i = 0; i < nomcha; i++) { 
			if (kbruit[i] > 0 && kbuf[i] != null &&  
					(taille = kbruit[i]) <= s) { 
				enfant = Swip_maillon.lfils_(kbuf[i], nbref); 
				parent = Swip_maillon.laieul_(kbuf[i], nbrea); 
	 
				if ((nbref == 0) && (nbrea == 0)) { 
					kbuf[i] = null; 
					kbruit[i] = 0; 
					kfus[i] = null; 
					(nbsuppres)++; 
					continue; 
				} 
				if (nbref == 0) { 
					supp_bruit(kbuf, i, nbrea, parent,  
							nbsuppres, idebug); 
					continue; 
				} 
				if (nbrea == 0) { 
					supp_bruit(kbuf, i, nbref, enfant,  
							nbsuppres, idebug); 
					continue; 
				} 
				if (nbrea == 1 && nbref == 1 && 
				    (Math.abs(enfant[0]) == (kbuf[i]).numero) && 
				    (Math.abs(parent[0]) == (kbuf[i]).numero)) { 
					kbuf[i] = null; 
					kbruit[i] = 0; 
					kfus[i] = null; 
					(nbsuppres)++; 
				} 
			} 
		} 
	} 
	
	public void LeLinker_go(int idebug) { 
			short		edge; 
			Chaine[] 		kbuf; 
			Integer 	kbruit;	/* Tableau pour l'elimination du bruit */ 
			Chaine  	kfus;		/* Tableau pour la fusion de chaines */ 
			int         ibruit; 
			int 		lx,ly; 
			int         i, j, l1, l2, l3, nc; 
			int         nbsuppres = 0, s_nbsup = 0; 
			int         nb_bruit = 0, nb_fusion = 0; 
		 
			LeLinker = new LinkerPar();
			
			LeLinker.nb_chaines = 0; 
			LeLinker.numero = 0; 
			nc = 0; 
			edge = LeLinker.edge; 
			lx = LeLinker.dim_x; 
			ly = LeLinker.dim_y; 
			ibruit = LeLinker.ibruit; 
			kbuf = LeLinker.chaines; 
		 
			/* Internal initialization. */ 
		 
			l1 = 0; 
			l2 = 1; 
//			initgtot_(); //TODO
			for (i=0;i<LeLinker.nb_chaines_max;i++) kbuf[i] = null; 
		 
		 
			/* First stage: Finite State Automata (FSA). */ 
			
			/* Initialize line l1 of itab. */ 
			// 
				Swip_ChaineExam.initab_(l1, LeLinker.itab); 
			 
				/* For every scan line... */ 
			 
				for (i = 0; i < lx; i++) { 
			 
					/* Initialize present state line of itab (it is dirty). */ 
			 
					Swip_ChaineExam.initab_(l2, LeLinker.itab); 
			 
					/* For every pixel of the line... */ 
			 
					for (j = 1; j < ly-1; j++) { 
			 
						/* If edge pixel call FSA. */ 
			 
						if ((edge + i + j * lx)!=0) { 
							Swip_ChaineExam.FSA(i, j, edge, LeLinker.itab, l1, l2, kbuf, idebug); 
							nc = nc + 1; 
						} 
					} 
			 
					/* Swap l1 with l2. */ 
			 
					l3 = l1; 
					l1 = l2; 
					l2 = l3; 
				}
				
				init_kbruit_kfus(kbuf, LeLinker.nb_chaines, ibruit); 
				 
				for (i = 1; i <= ibruit; i++){ 
					s_nbsup = nbsuppres; 
					fusion(kbuf, LeLinker.nb_chaines, nbsuppres, ibruit, idebug); 
					nb_fusion += nbsuppres - s_nbsup; 
					s_nbsup = nbsuppres; 
					bruitage(kbuf, LeLinker.nb_chaines, i, nbsuppres, idebug); 
					nb_bruit += nbsuppres - s_nbsup; 
				} 
				s_nbsup = nbsuppres; 
			 
				fusion(kbuf, LeLinker.nb_chaines, nbsuppres, ibruit, idebug); 
			 
				nb_fusion += nbsuppres - s_nbsup; 
				
				
				
		}
	
	public void init_kbruit_kfus(Chaine[] kbuf, int nomcha, int ibruit){ 
		int         taille, i; 
		Integer[] 	kbruit;	/* Tableau pour l'elimination du bruit */ 
		Chaine[]  	kfus;		/* Tableau pour la fusion de chaines */ 
	 
		kbruit = LeLinker.bruits; 
		kfus = LeLinker.fusions; 
		for (i = 0; i < nomcha; ++i) { 
			if (kbuf[i] == null) { 
				kbruit[i] = 0; 
				kfus[i] = null; 
			} else { 
				if ((taille = Swip_maillon.nbmail_(kbuf[i])) <= ibruit) 
					kbruit[i] = taille; 
				else 
					kbruit[i] = -taille; 
				kfus[i] = kbuf[i]; 
			} 
		} 
	} 
	
	public void supp_bruit(Chaine[] kbuf, int i, int nb, int[] parent, Integer nbsuppres, int idebug){ 
		int j, number; 
		Chaine kfus[] = LeLinker.fusions; 
		Integer[]	kbruit = LeLinker.bruits; 
	 
		if (Swip_maillon.TsupTest(kbuf[i], kbuf, nb, parent, idebug)) { 
			Swip_maillon.supp_ds_famille(kbuf, parent, kbuf[i].numero, idebug); 
			Swip_maillon.refiliat(kbuf, parent, nb, idebug); 
			kbuf[i] = null; 
			kbruit[i] = 0; 
			kfus[i] = null; 
			kfus[Math.abs(parent[0]) - 1] = kbuf[Math.abs(parent[0]) - 1]; 
			(nbsuppres)++; 
		} else 
			kbruit[i] = -kbruit[i]; 
	}

	public void fusion(Chaine[] kbuf, int nomcha, Integer nbsuppres, int s, int idebug) { 
		int i, bruit_total; 
		char[] bout = new char[4]; 
		Chaine UneChaine,UneAutreChaine; // nbaieul(); 
		Character typep = new Character(' '); 
		int j, numberp, nb_parents; 
		Integer parents;
		
		Chaine[] kfus = LeLinker.fusions; 
		Integer[] kbruit = LeLinker.bruits; 
	 
		for (i = 0; i < nomcha; i++) { 
		    if (kfus[i] != null) { 
			if ((UneChaine = kbuf[i]) != null) { 
	 
			    /* Si une chaine n'a qu'un seul lien avec une autre chaine 
			    a sa queue, elle peuvent fusionner par la queue */  
	 
			    while ((UneAutreChaine = Swip_maillon.nbfils(UneChaine, kbuf, idebug)) != null  
		&& Swip_maillon.un_seul_lien(UneChaine.queue.famille[0], UneAutreChaine, typep, idebug)) { 
	 
				numberp = UneAutreChaine.numero; 
	 
				/* Unlink chain from chains database */ 
	 
				kbuf[numberp - 1] = null; 
	 
				UneChaine =  
				Swip_maillon.inclure(kbuf, UneAutreChaine, typep, UneChaine, 'F', idebug); 
	 
				(nbsuppres)++; 
	 
				Swip_maillon.remp_ds_famille(kbuf,  UneChaine.queue.famille,  -(UneChaine.numero), numberp, idebug); 
	 
				/* Compute total length. */ 
	 
				bruit_total = Math.abs(kbruit[i]) + Math.abs(kbruit[numberp-1]); 
	 
				/* 
				 * If length is greater than error 
				 * threshold, mark chain as 
				 * "proteced".  
				 */ 
	 
				if (bruit_total > s) 
					kbruit[i] = -bruit_total; 
				else 
					kbruit[i] = bruit_total; 
	 
				/* Make fused chain disappear. */ 
	 
				kbruit[numberp - 1] = 0; 
				kfus[numberp - 1] = null; 
			    } 
	 
			    /* Si une chaine n'a qu'un seul lien avec une autre chaine 
			    a sa tete, elle peuvent fusionner par la tete */  
	 
			    while ((UneAutreChaine = Swip_maillon.nbaieul(UneChaine, kbuf, idebug)) != null   
		&& Swip_maillon.un_seul_lien(UneChaine.tete.famille[0], UneAutreChaine, typep, idebug)) { 
				numberp = UneAutreChaine.numero; 
	 
				/* Unlink chain from chains database */ 
	 
				kbuf[numberp - 1] = null; 
	 
				UneChaine =  
				Swip_maillon.inclure(kbuf, UneAutreChaine, typep, UneChaine, 'D', idebug); 
	 
				(nbsuppres)++; 
	 
				Swip_maillon.remp_ds_famille(kbuf, UneChaine.tete.famille,  
					UneChaine.numero, numberp, idebug); 
	 
				/* Compute total length. */ 
	 
				bruit_total = Math.abs(kbruit[i]) + Math.abs(kbruit[numberp-1]); 
	 
	 
				/* 
				 * If length is greater than error 
				 * threshold, mark chain as 
				 * "proteced".  
				 */ 
	 
				if (bruit_total > s) 
					kbruit[i] = -bruit_total; 
				else 
					kbruit[i] = bruit_total; 
	 
				/* Make fused chain disappear. */ 
	 
				kbruit[numberp - 1] = 0; 
				kfus[numberp - 1] = null; 
			    } 
	 
			    kfus[i] = null; 
			} 
		    } 
		} 
	} 

}
