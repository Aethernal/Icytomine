package plugins.faubin.cytomine.utils.contours.methode1.swip_chainage;
///*     swip_Linkermemory.c
// *      
// * DESCRIPTION
// *
// *      LeLinker_memory_set, LeLinker_memory_free
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
// *****************************/
//
//
//
//#ifndef DEPEND
//#include <stdio.h>
//#include <math.h>
//#endif DEPEND
//
//
//#include <malloc.h>
//
//#include"Maillon.h"
//
//LinkerPar LeLinker;
//
//#define MOY_CH 100 /* 50 */
//#define MAX_CH 5000 /* 5000 */
//
//LeLinker_memory_set(p_image,lmin, nx, ny)
//     short *p_image;
//     int lmin;
//int nx, ny;
//{
//  /***** memory allocation and variable setting ***********/
//  if((LeLinker=(LinkerPar)calloc(1,sizeof(linkerpar)))==NULL)
//    {perror("LeLinker");return(0);}
//  if((LeLinker->chainons_pile=(Chainon)calloc(MOY_CH*MAX_CH,sizeof(chainon)))==NULL)
//    {perror("LeLinker->chainons_pile");return(0);}
//  if((LeLinker->chaines_pile=(Chaine)calloc(MAX_CH,sizeof(chaine)))==NULL)
//    {perror("LeLinker->chaines_pile");return(0);}
//  if((LeLinker->chaines=(Chaine *)calloc(MAX_CH,sizeof(Chaine)))==NULL)
//    {perror("LeLinker->chaines");return(0);}
//  if((LeLinker->fusions=(Chaine *)calloc(MAX_CH,sizeof(Chaine)))==NULL)
//    {perror("LeLinker->fusions");return(0);}
//  if((LeLinker->bruits=(int *)calloc(MAX_CH,sizeof(int)))==NULL)
//    {perror("LeLinker->bruits");return(0);}
//  
//  /* conservation des adresses de debut de pile */
//  LeLinker->chainons_pile_deb=LeLinker->chainons_pile;
//  LeLinker->chaines_pile_deb=LeLinker->chaines_pile;
//  
//  LeLinker->nb_chainons_pile = (int)(MOY_CH*MAX_CH);
//  LeLinker->nb_chaines_pile = (int)MAX_CH;
//  LeLinker->nb_chaines_max = (int)MAX_CH;
//  LeLinker->ibruit = lmin;
//  LeLinker->dim_x = nx;
//  LeLinker->dim_y = ny;
//  LeLinker->edge = (short *) p_image;
//
//  return(1);
//}
//
//void LeLinker_memory_free()
//{
//  free(LeLinker->chainons_pile_deb), LeLinker->chainons_pile=NULL;
//  free(LeLinker->chaines_pile_deb), LeLinker->chaines_pile=NULL;
//  free(LeLinker->chaines), LeLinker->chaines=NULL;
//  free(LeLinker->fusions), LeLinker->fusions=NULL;
//  free(LeLinker->bruits), LeLinker->bruits=NULL;
//  free(LeLinker);
//}
