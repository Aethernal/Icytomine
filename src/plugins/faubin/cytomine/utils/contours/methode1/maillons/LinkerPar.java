package plugins.faubin.cytomine.utils.contours.methode1.maillons;

import java.util.Stack;

public class LinkerPar {
	
	public static final int TX = 1024;
	
	public Stack<Chainon> chainons_pile;	   /* Pile d'allocation des chainons */
	public Stack<Chaine>  chaines_pile;	   /* Pile d'allocation des chaines */
	public Chaine chaines_pile_deb;   /* debut de la pile */
	public Chainon chainons_pile_deb; /* debut de la pile */
	public Chaine[]  chaines;	   /* Liste des chaines (tableau) */
	public Integer[] 	bruits; /* Tableau pour l'elimination du bruit */
	public int 	ibruit;		   /* Seuil pour l'elimination du bruit */
	public Chaine[]  fusions;	   /* Tableau pour la fusion de chaines */
	public int[][][]	itab = new int[TX][2][4];    /* 2 lignes de buffers pour les traitements de
				   la fsa */
	public int	nb_chainons_pile,	/* Taille actuelle
					de la pile d'allocation des chainons */
		nb_chaines_pile,	/* Taille actuelle
					de la pile d'allocation des chaines */
		nb_chaines_max;		/* Numero (id) de la chaine a creer */

	public int nb_chaines;

	public int numero;

	public int 	dim_x,dim_y;	/* Taille de l'image de travail */
	public short   edge;
}
