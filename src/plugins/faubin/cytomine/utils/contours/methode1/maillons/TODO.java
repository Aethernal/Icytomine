///*ANALYSE *********** X11R4 H INCLUDE *******************ANALYSE
// *
// * NAME
// *        Maillon.h
// *
// * SYNOPSIS
// *        #include "Maillon.h"
// *
// * DESCRIPTION
// *        Declaration of the fcts in Maillon.c
// * AUTHOR
// *      Giraudon Copyright
// * 
// *
// * UPDATES
// *
// *    last:03/05/1991
// *ANALYSE *********** H INCLUDE *******************ANALYSE */
//
//#ifndef Maillon_h 
//#define Maillon_h
//
//typedef struct chainon
//	{
//	short x,y;
//	struct chainon *succ;
//	} chainon, *Chainon;
//
//typedef struct chaine {
//	Chainon premier;
//	Chainon dernier;
//	int numero;
//	char mode;
//	char magic;
//	struct { int famille[4];} tete;
//	struct { int famille[4];} queue;
//	} chaine, *Chaine;
//
//#define TX 1024
//typedef struct linkerpar {
//	Chainon chainons_pile;	   /* Pile d'allocation des chainons */
//	Chaine  chaines_pile;	   /* Pile d'allocation des chaines */
//	Chaine chaines_pile_deb;   /* debut de la pile */
//	Chainon chainons_pile_deb; /* debut de la pile */
//	Chaine  *chaines;	   /* Liste des chaines (tableau) */
//	int 	*bruits;	   /* Tableau pour l'elimination du bruit */
//	int 	ibruit;		   /* Seuil pour l'elimination du bruit */
//	Chaine  *fusions;	   /* Tableau pour la fusion de chaines */
//	int	itab[TX][2][4];    /* 2 lignes de buffers pour les traitements de
//				   la fsa */
//	int	nb_chainons_pile,	/* Taille actuelle
//					de la pile d'allocation des chainons */
//		nb_chaines_pile,	/* Taille actuelle
//					de la pile d'allocation des chaines */
//		nb_chaines_max,	/* Nombre maximal de chaines */
//		nb_chaines,	/* Nombre de chaines */
//		numero;		/* Numero (id) de la chaine a creer */
//
//	int 	dim_x,dim_y;	/* Taille de l'image de travail */
//	short   *edge;
//	} linkerpar, *LinkerPar;
//
//extern Chaine opench_();
//extern int aieul_();
//extern int aieul_b();
//extern int sraieul_();
//extern int *laieul_();
//extern int ssaieul_();
//extern int fils_();
//extern int fils_b();
//extern int srfils_();
//extern int *lfils_();
//extern int ssfils_();
//extern Chainon nwmail_();
//extern fimail_();
//extern remail_();
//extern int nbmail_();
//extern fuschn_();
//extern miroir_();
//extern dump_(); 
//extern getmail_(); 
//extern float getmoy_(); 
//extern contch_();
//extern isclos_();
//extern iscont_();
//extern Chaine nbfils(); 
//extern int invers_orient();
//extern Chaine inclure();
//
//int LeLinker_memory_set(/**** Image*,int ****/);
//void LeLinker_go(/**** int ****/);
//void LeLinker_memory_free();
//
//#endif 
//