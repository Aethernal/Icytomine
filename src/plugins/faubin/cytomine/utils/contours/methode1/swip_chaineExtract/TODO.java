///*******************************
//  swip_ChainExtract.c
//  
// Extraction of point chain using the Giraudon's method
//  
//Author : V. Meas-Yedid
//Date of creation : 11/04/2000
//Version : 1.0 
//Date of IP version : 11/04/2000
// ********************************/
//
//#include "taiq.h"
//
//#include "gui_itf.h"
//#include "gui_com.h"
//
//#include "swip_action.h"
//
//#include "i2d.h"
//#include "i2d_defs.h"
//
//#include "fb_toolbox_manage.h"
//#include "fb_struct.h"
//#include "fb_toolbox_com.h"
//
//#include "nr.h"
//#include "nrutil.h"
//#include <math.h>
//#include <time.h>
//#include <sys/times.h>
//
//
//#include "Maillon.h" 
//
//
//extern LinkerPar LeLinker;
//
///* save point */
//typedef struct Point{
//  int x;
//  int y;
//}Point;
//
//
//Point **i2d_ChainExtract(i2d_t i2d_ref, int nb_ima)
//{
//  int lmin=5; /* valeur initiale arbitraire a parametrer*/
//  int nx, ny;
//  Chainon UnChainon; 
//  Chaine UneChaine; 
//  int add,k; 
//  Chaine chaine_suivante( /* LinkerPar,int */);
//  unsigned char *buffer_i2d;
//  short *image_in;
//  int nb_pts[5000],nb_forms,i, nbObj;
//  FILE *f;
//  Point **pts;
//  char *filename;
//  short minx, *keep;
//  
//
//  /* printf("Longueur minimale de la chaine a extraire ?");
//  scanf("%d",&lmin); */
//  lmin=2;
//  nx = I2DGetCols(i2d_ref);
//  ny = I2DGetLines(i2d_ref);
//  buffer_i2d = (unsigned char *)I2DGetImagePtr(i2d_ref);
//
//  image_in = (short *) calloc(nx*ny,sizeof(short)); 
//  for (i = 0; i< nx*ny; i++)  
//    image_in[i] = (short) buffer_i2d[i]; 
//
//  /* Chainage des points */
//  if (!LeLinker_memory_set(image_in,lmin,nx,ny)) return(0); 
//  LeLinker_go(0); 
//  
//  /* Transforme en format lisible par TaiQ */
// 
//  nb_forms=0;
//  minx=5;/**/
//  keep = (short *) malloc(sizeof(short)*5000);
//  for(add=0,UneChaine=chaine_suivante(LeLinker,&add); 
//      UneChaine!=NULL; 
//      UneChaine=chaine_suivante(LeLinker,&add)) { 
//
//    nb_pts[add]=0;
//    keep[add]=0;
//    for(UnChainon=UneChaine->premier; UnChainon!=NULL; 
//	UnChainon=UnChainon->succ) { 
//      nb_pts[add]++; 
//      if (((UnChainon->x)<minx)||((UnChainon->x)>nx-minx)||((UnChainon->y)<minx)||((UnChainon->y)>ny-minx))
//	keep[add]=-1;
//    }
//    if ((nb_pts[add] > lmin) && (keep[add]!=-1))
//      nb_forms++;
//  }
//  pts = (Point **)malloc(nb_forms*sizeof(Point));
// 
//  /*  nb_forms--; */
//  printf("Nombre de formes : %d %d\n",add, nb_forms);
//  /* Save in a file - A faire, nom automatique*/
//  f=fopen("ContourExt.roi","w");
//  fprintf(f,"nbr_sel:%d\n",nb_forms);
//  nbObj = 0;
//  for(add=0,UneChaine=chaine_suivante(LeLinker,&add); 
//      UneChaine!=NULL; 
//      UneChaine=chaine_suivante(LeLinker,&add)) {
//    if (nb_pts[add] > lmin && (keep[add]!=-1)) {
//      nbObj++;
//      pts[nbObj-1] = (Point *)malloc((nb_pts[add]+1)*sizeof(Point));
//      fprintf(f,"type:3\n");
//      fprintf(f,"typepoly:0\n");
//      fprintf(f,"ncp:%i\n",nb_pts[add]);
//      printf("%d numero: %d nb_pts =%d\n", nbObj, add,nb_pts[add]);
//      i=1;
//      pts[nbObj-1][0].x = nb_pts[add];
//      for(UnChainon=UneChaine->premier; UnChainon!=NULL; 
//	  UnChainon=UnChainon->succ) {
//	fprintf(f,"x:%d\n",(int) UnChainon->x);
//	fprintf(f,"y:%d\n",(int) UnChainon->y);
//	pts[nbObj-1][i].x = (int) UnChainon->x;
//	pts[nbObj-1][i].y = (int) UnChainon->y;
//	i++;
//      }
//    
//    }
//  }
//  pts[0][0].y = nbObj;
//  fclose(f);
//
//
//#if 1
//  filename = (char *)malloc(sizeof(char)*(MAXPATHLEN));
//  sprintf(filename, "%s%s%s%s", select_images[0].img_folder,"/",	 select_images[nb_ima].img_prefx,".txt");
//   /*strcpy(filename, i2d_ref->Name); */
//  /*f=fopen("ContourExt.txt","w"); */
//  f = fopen(filename,"w");
//  fprintf(f,"%d\n",nb_forms);
//  nbObj = 0;
//  for(add=0,UneChaine=chaine_suivante(LeLinker,&add); 
//      UneChaine!=NULL; 
//      UneChaine=chaine_suivante(LeLinker,&add)) {
//    if (nb_pts[add] > lmin && (keep[add]!=-1)) {
//      nbObj++; /* Rajout numero */
//      fprintf(f,"%d %d\n",nbObj-1, nb_pts[add]);
//      for(UnChainon=UneChaine->premier; UnChainon!=NULL; 
//	  UnChainon=UnChainon->succ) {
//	fprintf(f," %d ",(int) UnChainon->x);
//	fprintf(f,"%d\n",(int) UnChainon->y);
//      }
//    }
//  }
//  fclose(f);
//  free(filename);
//
//  f=fopen("data.txt","w");
//  /*fprintf(f,"%d\n",nb_forms); */
//
//  for (i=0; i<nbObj; i++) {
//    add = pts[i][0].x;
//    fprintf(f,"%d %d %d\n",pts[i][1].x,pts[i][1].y,i);
//    fprintf(f,"%d %d %d\n",pts[i][add].x,pts[i][add].y,add);
//    
//  }
//  fclose(f);
//#endif
//
//  free(keep);
//  LeLinker_memory_free(); 
//  free(image_in);
//  return(pts);
//}
