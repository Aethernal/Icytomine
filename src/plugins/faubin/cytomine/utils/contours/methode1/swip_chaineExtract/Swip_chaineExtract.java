package plugins.faubin.cytomine.utils.contours.methode1.swip_chaineExtract;

import java.awt.Point;
import java.io.File;
import java.io.PrintWriter;

import plugins.faubin.cytomine.utils.contours.methode1.maillons.Chaine;
import plugins.faubin.cytomine.utils.contours.methode1.maillons.Chainon;
import plugins.faubin.cytomine.utils.contours.methode1.maillons.LinkerPar;
import plugins.faubin.cytomine.utils.contours.methode1.swip_chainage.Swip_ChainageMem;

public class Swip_chaineExtract {

	private static final int MAXPATHLEN = 1024;
	public static LinkerPar LeLinker;

	public static Point[][] i2d_ChainExtract(i2d_t i2d_ref, int nb_ima){
	  int lmin=5; /* valeur initiale arbitraire a parametrer*/
	  int nx, ny;
	  Chainon UnChainon; 
	  Chaine UneChaine; 
	  int[] add = new int[1];
	  int k; 
	  Chaine chaine_suivante = new Chaine( /* LinkerPar,int */);
	  char[] buffer_i2d;
	  short[] image_in;
	  int[] nb_pts = new int[5000];
	  int nb_forms,i, nbObj;
	  File f;
	  Point[][] pts;
	  char[] filename;
	  short minx;
	  short[] keep;
	  
	
	  /* System.out.println("Longueur minimale de la chaine a extraire ?");
	  scanf("%d",&lmin); */
	  lmin=2;
	  nx = I2DGetCols(i2d_ref);
	  ny = I2DGetLines(i2d_ref);
	  buffer_i2d = (char[])I2DGetImagePtr(i2d_ref);
	
	  image_in = new short[nx*ny]; 
	  for (i = 0; i< nx*ny; i++)  
	    image_in[i] = (short) buffer_i2d[i]; 
	
	  /* Chainage des points */
	  if (!(Swip_ChainageMem.LeLinker_memory_set(image_in,lmin,nx,ny)!=0)) return(null); 
	  Swip_ChainageMem.LeLinker_go(0); 
	  
	  /* Transforme en format lisible par TaiQ */
	 
	  nb_forms=0;
	  minx=5;/**/
	  keep = new short[5000];
	  for(add[0]=0,UneChaine=chaine_suivante(LeLinker,add); 
	      UneChaine!=null; 
	      UneChaine=chaine_suivante(LeLinker,add)) { 
	
	    nb_pts[add[0]]=0;
	    keep[add[0]]=0;
	    for(UnChainon=UneChaine.premier; UnChainon!=null; 
		UnChainon=UnChainon.succ) { 
	      nb_pts[add[0]]++; 
	      if (((UnChainon.x)<minx)||((UnChainon.x)>nx-minx)||((UnChainon.y)<minx)||((UnChainon.y)>ny-minx))
		keep[add[0]]=-1;
	    }
	    if ((nb_pts[add[0]] > lmin) && (keep[add[0]]!=-1))
	      nb_forms++;
	  }
	  pts = new Point[nb_forms][nb_forms]; //(Point[][])malloc(nb_forms*sizeof(Point));
	 
	  /*  nb_forms--; */
	  System.out.println("Nombre de formes : "+add+" "+nb_forms+"\n");
	  /* Save in a file - A faire, nom automatique*/
	  f=new File("ContourExt.roi");//fopen("ContourExt.roi","w");
	  PrintWriter writer = new PrintWriter(f);
	  writer.write("nbr_sel:"+nb_forms+"\n"); //System.out.println(f,"nbr_sel:%d\n",nb_forms);
	  
	  nbObj = 0;
	  for(add[0]=0,UneChaine=chaine_suivante(LeLinker,add); 
	      UneChaine!=null; 
	      UneChaine=chaine_suivante(LeLinker,add)) {
	    if (nb_pts[add[0]] > lmin && (keep[add[0]]!=-1)) {
	      nbObj++;
	      pts[nbObj-1] = new Point[nb_pts[add[0]]+1]; //(Point *)malloc((nb_pts[add]+1)*sizeof(Point));
	      writer.write("type:3"); //System.out.println(f,"type:3\n");
	      writer.write("typepoly:0\n"); //System.out.println(f,"typepoly:0\n");
	      writer.write("ncp:"+nb_pts[add[0]]+"\n"); //System.out.println(f,"ncp:%i\n",nb_pts[add]);
	      writer.write(""+nbObj+" numero: "+add[0]+" nb_pts ="+nb_pts[add[0]]+"\n"); //System.out.println("%d numero: %d nb_pts =%d\n", nbObj, add,nb_pts[add]);
	      i=1;
	      pts[nbObj-1][0].x = nb_pts[add[0]];
	      for(UnChainon=UneChaine.premier; UnChainon!=null; 
		  UnChainon=UnChainon.succ) {
		writer.write("x:"+UnChainon.x+"\n"); //System.out.println(f,"x:%d\n",(int) UnChainon.x);
		writer.write("y:"+UnChainon.y+"\n"); //System.out.println(f,"y:%d\n",(int) UnChainon.y);
		pts[nbObj-1][i].x = (int) UnChainon.x;
		pts[nbObj-1][i].y = (int) UnChainon.y;
		i++;
	      }
	    
	    }
	  }
	  pts[0][0].y = nbObj;
	  
	
	
//	#if 1
	  filename = new char[MAXPATHLEN]; //(char *)malloc(sizeof(char)*(MAXPATHLEN));
	filename = select_images[0].img_folder + "/" + select_images[nb_ima].img_prefx,".txt"; //sSystem.out.println(filename, "%s%s%s%s", select_images[0].img_folder,"/",	 select_images[nb_ima].img_prefx,".txt");
	   /*strcpy(filename, i2d_ref.Name); */
	  /*f=fopen("ContourExt.txt","w"); */
	  f = new File(""+new String(filename));//fopen(filename,"w");
	  writer = new PrintWriter(f);
	  writer.write(""+nb_forms+"\n"); //System.out.println(f,"%d\n",nb_forms);
	  nbObj = 0;
	  for(add[0]=0,UneChaine=chaine_suivante(LeLinker,add); 
	      UneChaine!=null; 
	      UneChaine=chaine_suivante(LeLinker,add)) {
	    if (nb_pts[add[0]] > lmin && (keep[add[0]]!=-1)) {
	      nbObj++; /* Rajout numero */
	      writer.write(""+(nbObj-1)+" "+nb_pts[add[0]]+"\n"); //System.out.println(f,"%d %d\n",nbObj-1, nb_pts[add[0]]);
	      for(UnChainon=UneChaine.premier; UnChainon!=null; 
		  UnChainon=UnChainon.succ) {
		writer.write(" "+UnChainon.x+" "); //System.out.println(f," %d ",(int) UnChainon.x);
		writer.write(UnChainon.y+"\n"); //System.out.println(f,"%d\n",(int) UnChainon.y);
	      }
	    }
	  }
//	  fclose(f);
//	  free(filename);
	  
	  f= new File("data.txt"); //fopen("data.txt","w");
	  writer = new PrintWriter(f);
	  /*System.out.println(f,"%d\n",nb_forms); */
	
	  for (i=0; i<nbObj; i++) {
	    add[0] = pts[i][0].x;
	    writer.write(""+pts[i][1].x+" "+pts[i][1].y+" "+i+"\n"); //System.out.println(f,"%d %d %d\n",pts[i][1].x,pts[i][1].y,i);
	    writer.write(""+pts[i][add[0]].x+" "+pts[i][add[0]].y+" "+add[0]+"\n"); //System.out.println(f,"%d %d %d\n",pts[i][add[0]].x,pts[i][add[0]].y,add);
	    
	  }
//	  fclose(f);
//	#endif
	
//	  free(keep);
	  LinkerPar.LeLinker_memory_free(); 
//	  free(image_in);
	  return(pts);
	}

	private static char[] I2DGetImagePtr(i2d_t i2d_ref) {
		// TODO Auto-generated method stub
		return null;
	}

	private static int I2DGetLines(i2d_t i2d_ref) {
		// TODO Auto-generated method stub
		return 0;
	}

	private static int I2DGetCols(i2d_t i2d_ref) {
		// TODO Auto-generated method stub
		return 0;
	}

	private static Chaine chaine_suivante(LinkerPar leLinker2, int[] add) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
