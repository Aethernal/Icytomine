package plugins.faubin.cytomine.utils.contours.methode1.swip_chaineExam;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import plugins.faubin.cytomine.utils.contours.methode1.maillons.Chaine;
import plugins.faubin.cytomine.utils.contours.methode1.maillons.LinkerPar;
import plugins.faubin.cytomine.utils.contours.methode1.swip_maillon.Swip_maillon;


public class Swip_ChaineExam {

	public static final int VAL_MAX = 150;
	private static LinkerPar LeLinker;
	
	private static String[] gtotab = new String[VAL_MAX+1];
	
	public static void P_XXXX(int i,int j, Short edge, int[][][] itab, int l1,int l2,Chaine[] kbuf,int idebug)
	{
	    Chaine UneChaine;
	    int sipremier=0;

	    if (idebug!=0) System.out.println("Configuration P_XXXX Line "+i+" Col "+j+"");

	    LeLinker.nb_chaines += 1;
	    if (LeLinker.nb_chaines >= LeLinker.nb_chaines_max) {
	        System.out.println("Too many chains\n");
	        return;
	    }
	    UneChaine = (Chaine) Swip_maillon.opench_();
	    if (UneChaine == null) {
	        System.out.println("There is no more space for chains.\n");
	        return;
	    }
	    kbuf[ LeLinker.nb_chaines - 1] = UneChaine ;
	    itab[ j][l2 ][ 2 ] = LeLinker.nb_chaines;
	    itab[ j][l2 ][ 1 ] = 'P'   ;
	    Swip_maillon.nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
	}

	public static void P_XXXO(int i,int j,Short edge,int [][][] itab, int l1, int l2, Chaine[] kbuf,int idebug){
		    Chaine UneChaine;
		    int sipremier=0;
	
		    if (idebug!=0) System.out.println("Configuration P_XXXO Line "+i+" Col "+j+"");
	
		    itab[ j][l2 ][ 2 ] = itab[ j-1][l2 ][ 2 ];
		    itab[ j][l2 ][ 1 ] = 'O' ;
		    if (itab[ j-1][l2 ][ 1 ] == 'P') {
		        itab[ j-1][l2 ][ 1 ] = 'D';
		        sipremier = 1;
		    } else itab[ j-1][l2 ][ 1 ] = 'X';
		    Swip_maillon.nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
		    if (!(sipremier!=0))
		        if ( ptest5668(i,j,edge) ) {
		            itab[ j][l2 ][ 1 ] = 'F';
		        }
	}
	
	
	
	
	public static void P_XXXF(int i,int j,Short edge,int [][][] itab, int l1, int l2, Chaine[] kbuf,int idebug)

	
	
	{
	    Chaine UneChaine;
	    int sipremier=0;
	
	    if (idebug!=0) System.out.println("Configuration P_XXXF Line "+i+" Col "+j+"");
	
	    LeLinker.nb_chaines += 1;
	    if (LeLinker.nb_chaines >= LeLinker.nb_chaines_max) {
	        System.out.println("Too many chains\n");
	        System.exit(-1);
	    }
	    UneChaine = (Chaine) Swip_maillon.opench_();
	    if (UneChaine == null) {
	        System.out.println("There is no more space for chains.\n");
	        System.exit(-2);
	    }
	    kbuf[ LeLinker.nb_chaines - 1] = UneChaine ;
	    itab[ j][l2 ][ 2 ] = LeLinker.nb_chaines;
	    itab[ j][l2 ][ 1 ] = 'P'   ;
	    Swip_maillon.nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
	    if (itab[ j-1][l2 ][ 1 ] == 'D') {
	        if (Swip_maillon.aieul_( kbuf[  itab[ j][l2][2 ] -1],itab[ j-1][l2 ][ 2 ]) != 0)
	                Swip_maillon.aieul_( kbuf[itab[ j-1][l2 ][ 2 ] -1],itab[ j][l2][2 ]);
	    } else {
	        if (Swip_maillon.aieul_( kbuf[ itab[ j][l2][2 ] -1],-itab[ j-1][l2 ][ 2 ]) != 0)
	                Swip_maillon.fils_ ( kbuf[ itab[ j-1][l2 ][ 2 ] -1],itab[ j][l2][2 ]);
	    }
	}
	
	/****************************************************************************/
	
	public static void P_XXOX(int i,int j,Short edge,int [][][] itab, int l1, int l2, Chaine[] kbuf,int idebug)
	
	
	
	{
	    Chaine UneChaine;
	    int sipremier=0;
	
	    if (idebug!=0) System.out.println("Configuration P_XXOX Line "+i+" Col "+j+"");
	
	    itab[ j][l2 ][ 2 ] = itab[ j+1][l1 ][ 2 ];
	    itab[ j][l2 ][ 1 ] = 'O' ;
	    if (itab[ j+1][l1 ][ 1 ] == 'P') {
	        itab[ j+1][l1 ][ 1 ] = 'D';
	        sipremier = 1;
	    } else itab[ j+1][l1 ][ 1 ] = 'X';
	    Swip_maillon.nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
	    if ( ptest5668(i,j,edge) ) {
	        itab[ j][l2 ][ 1 ] = 'F';
	    }
	}
	
	/****************************************************************************/
	
	public static void P_XXOO(int i,int j,Short edge,int [][][] itab, int l1, int l2, Chaine[] kbuf,int idebug)
	
	
	
	{
	    Chaine UneChaine;
	    int sipremier=0;
	
	    if (idebug!=0) System.out.println("Configuration P_XXOO Line "+i+" Col "+j+"");
	
	    itab[ j][l2 ][ 2 ] = itab[ j-1][l2 ][ 2 ];
	    itab[ j][l2 ][ 1 ] = 'O' ;
	    if (itab[ j-1][l2 ][ 1 ] == 'P') {
	        itab[ j-1][l2 ][ 1 ] = 'D';
	        sipremier = 1;
	    } else itab[ j-1][l2 ][ 1 ] = 'X';
	    Swip_maillon.nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
	    if (itab[ j+1][l1 ][ 1 ] == 'P') itab[ j+1][l1 ][ 1 ] = 'D'; else itab[ j+1][l1 ][ 1 ] = 'F';
	    itab[ j][l2 ][ 1 ] = 'F';
	    if (!(ptest5(i,j,edge) != 0)) {
	        if (itab[ j+1][l1 ][ 1 ] == 'F') {
	            if (Swip_maillon.fils_( kbuf [itab[ j+1][l1 ][ 2 ] -1],-itab[ j][l2][2 ]) != 0)
	                    Swip_maillon.fils_( kbuf [itab[ j][l2][2 ] -1],-itab[ j+1][l1 ][ 2 ]);
	        } else {
	            if (Swip_maillon.aieul_( kbuf [ itab[ j+1][l1 ][ 2 ]-1],-itab[ j][l2][2 ]) != 0)
	                    Swip_maillon.fils_ ( kbuf [itab[ j][l2][2 ] -1],itab[ j+1][l1 ][ 2 ]);
	        }
	    }
	}
	
	/****************************************************************************/
	
	public static void P_XXOF(int i,int j,Short edge,int [][][] itab, int l1, int l2, Chaine[] kbuf,int idebug)
	
	
	
	{
	    Chaine UneChaine;
	    int sipremier=0;
	
	    if (idebug!=0) System.out.println("Configuration P_XXOF Line "+i+" Col "+j+"");
	
	    itab[ j][l2 ][ 2 ] = itab[ j+1][l1 ][ 2 ];
	    itab[ j][l2 ][ 1 ] = 'O' ;
	    if (itab[ j+1][l1 ][ 1 ] == 'P') {
	        itab[ j+1][l1 ][ 1 ] = 'D';
	        sipremier = 1;
	    } else itab[ j+1][l1 ][ 1 ] = 'X';
	    Swip_maillon.nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
	    itab[ j][l2 ][ 1 ] = 'F';
	    if (itab[ j-1][l2 ][ 1 ] == 'F') {
	        if (Swip_maillon.fils_( kbuf [itab[ j-1][l2 ][ 2 ] -1],-itab[ j][l2][2 ]) != 0)
	                Swip_maillon.fils_( kbuf [itab[ j][l2][2 ] -1],-itab[ j-1][l2 ][ 2 ]);
	    } else {
	        if (Swip_maillon.aieul_( kbuf [ itab[ j-1][l2 ][ 2 ]-1],-itab[ j][l2][2 ]) != 0)
	                Swip_maillon.fils_ ( kbuf [itab[ j][l2][2 ] -1],itab[ j-1][l2 ][ 2 ]);
	    }
	}
	
	/****************************************************************************/
	
	public static void P_XXFX(int i,int j,Short edge,int [][][] itab, int l1, int l2, Chaine[] kbuf,int idebug)
	
	
	
	{
	    Chaine UneChaine;
	    int sipremier=0;
	
	    if (idebug!=0) System.out.println("Configuration P_XXFX Line "+i+" Col "+j+"");
	
	    LeLinker.nb_chaines += 1;
	    if (LeLinker.nb_chaines >= LeLinker.nb_chaines_max) {
	        System.out.println("Too many chains\n");
	        System.exit(-1);
	    }
	    UneChaine = (Chaine) Swip_maillon.opench_();
	    if (UneChaine == null) {
	        System.out.println("There is no more space for chains.\n");
	        System.exit(-2);
	    }
	    kbuf[ LeLinker.nb_chaines - 1] = UneChaine ;
	    itab[ j][l2 ][ 2 ] = LeLinker.nb_chaines;
	    itab[ j][l2 ][ 1 ] = 'P'   ;
	    Swip_maillon.nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
	    if (!(ptest5(i,j,edge) != 0 )) {
	        if (itab[ j+1][l1 ][ 1 ] == 'D') {
	            if (Swip_maillon.aieul_( kbuf[  itab[ j][l2][2 ] -1],itab[ j+1][l1 ][ 2 ]) != 0)
	                    Swip_maillon.aieul_( kbuf[itab[ j+1][l1 ][ 2 ] -1],itab[ j][l2][2 ]);
	        } else {
	            if (Swip_maillon.aieul_( kbuf[ itab[ j][l2][2 ] -1],-itab[ j+1][l1 ][ 2 ]) != 0)
	                    Swip_maillon.fils_ ( kbuf[ itab[ j+1][l1 ][ 2 ] -1],itab[ j][l2][2 ]);
	        }
	    }
	}
	
	/****************************************************************************/
	
	public static void P_XXFO(int i,int j,Short edge,int [][][] itab, int l1, int l2, Chaine[] kbuf,int idebug)
	
	
	
	{
	    Chaine UneChaine;
	    int sipremier=0;
	
	    if (idebug!=0) System.out.println("Configuration P_XXFO Line "+i+" Col "+j+"");
	
	    itab[ j][l2 ][ 2 ] = itab[ j-1][l2 ][ 2 ];
	    itab[ j][l2 ][ 1 ] = 'O' ;
	    if (itab[ j-1][l2 ][ 1 ] == 'P') {
	        itab[ j-1][l2 ][ 1 ] = 'D';
	        sipremier = 1;
	    } else itab[ j-1][l2 ][ 1 ] = 'X';
	    Swip_maillon.nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
	    itab[ j][l2 ][ 1 ] = 'F';
	    if (!(ptest5(i,j,edge) != 0)) {
	        if (itab[ j+1][l1 ][ 1 ] == 'F') {
	            if (Swip_maillon.fils_( kbuf [itab[ j+1][l1 ][ 2 ] -1],-itab[ j][l2][2 ]) != 0)
	                    Swip_maillon.fils_( kbuf [itab[ j][l2][2 ] -1],-itab[ j+1][l1 ][ 2 ]);
	        } else {
	            if (Swip_maillon.aieul_( kbuf [ itab[ j+1][l1 ][ 2 ]-1],-itab[ j][l2][2 ]) != 0)
	                    Swip_maillon.fils_ ( kbuf [itab[ j][l2][2 ] -1],itab[ j+1][l1 ][ 2 ]);
	        }
	    }
	}
	
	/****************************************************************************/
	
	public static void P_XXFF(int i,int j,Short edge,int [][][] itab, int l1, int l2, Chaine[] kbuf,int idebug)
	
	
	
	{
	    Chaine UneChaine;
	    int sipremier=0;
	
	    if (idebug!=0) System.out.println("Configuration P_XXFF Line "+i+" Col "+j+"");
	
	    LeLinker.nb_chaines += 1;
	    if (LeLinker.nb_chaines >= LeLinker.nb_chaines_max) {
	        System.out.println("Too many chains\n");
	        System.exit(-1);
	    }
	    UneChaine = (Chaine) Swip_maillon.opench_();
	    if (UneChaine == null) {
	        System.out.println("There is no more space for chains.\n");
	        System.exit(-2);
	    }
	    kbuf[ LeLinker.nb_chaines - 1] = UneChaine ;
	    itab[ j][l2 ][ 2 ] = LeLinker.nb_chaines;
	    itab[ j][l2 ][ 1 ] = 'P'   ;
	    Swip_maillon.nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
	    if (!(ptest5(i,j,edge) != 0)) {
	        if (itab[ j+1][l1 ][ 1 ] == 'D') {
	            if (Swip_maillon.aieul_( kbuf[  itab[ j][l2][2 ] -1],itab[ j+1][l1 ][ 2 ]) != 0)
	                    Swip_maillon.aieul_( kbuf[itab[ j+1][l1 ][ 2 ] -1],itab[ j][l2][2 ]);
	        } else {
	            if (Swip_maillon.aieul_( kbuf[ itab[ j][l2][2 ] -1],-itab[ j+1][l1 ][ 2 ]) != 0)
	                    Swip_maillon.fils_ ( kbuf[ itab[ j+1][l1 ][ 2 ] -1],itab[ j][l2][2 ]);
	        }
	    }
	    if (itab[ j-1][l2 ][ 1 ] == 'D') {
	        if (Swip_maillon.aieul_( kbuf[  itab[ j][l2][2 ] -1],itab[ j-1][l2 ][ 2 ]) != 0)
	                Swip_maillon.aieul_( kbuf[itab[ j-1][l2 ][ 2 ] -1],itab[ j][l2][2 ]);
	    } else {
	        if (Swip_maillon.aieul_( kbuf[ itab[ j][l2][2 ] -1],-itab[ j-1][l2 ][ 2 ]) != 0)
	                Swip_maillon.fils_ ( kbuf[ itab[ j-1][l2 ][ 2 ] -1],itab[ j][l2][2 ]);
	    }
	}
	
	/****************************************************************************/
	
	public static void P_XOXX(int i,int j,Short edge,int [][][] itab, int l1, int l2, Chaine[] kbuf,int idebug)
	
	
	
	{
	    Chaine UneChaine;
	    int sipremier=0;
	
	    if (idebug!=0) System.out.println("Configuration P_XOXX Line "+i+" Col "+j+"");
	
	    itab[ j][l2 ][ 2 ] = itab[ j][l1   ][ 2 ];
	    itab[ j][l2 ][ 1 ] = 'O' ;
	    if (itab[ j][l1   ][ 1 ] == 'P') {
	        itab[ j][l1   ][ 1 ] = 'D';
	        sipremier = 1;
	    } else itab[ j][l1   ][ 1 ] = 'X';
	    Swip_maillon.nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
	    if ( ptest5668(i,j,edge) ) {
	        itab[ j][l2 ][ 1 ] = 'F';
	    }
	}
	
	/****************************************************************************/
	
	public static void P_XFXX(int i,int j,Short edge,int [][][] itab, int l1, int l2, Chaine[] kbuf,int idebug)
	
	
	
	{
	    Chaine UneChaine;
	    int sipremier=0;
	
	    if (idebug!=0) System.out.println("Configuration P_XFXX Line "+i+" Col "+j+"");
	
	    LeLinker.nb_chaines += 1;
	    if (LeLinker.nb_chaines >= LeLinker.nb_chaines_max) {
	        System.out.println("Too many chains\n");
	        System.exit(-1);
	    }
	    UneChaine = (Chaine) Swip_maillon.opench_();
	    if (UneChaine == null) {
	        System.out.println("There is no more space for chains.\n");
	        System.exit(-2);
	    }
	    kbuf[ LeLinker.nb_chaines - 1] = UneChaine ;
	    itab[ j][l2 ][ 2 ] = LeLinker.nb_chaines;
	    itab[ j][l2 ][ 1 ] = 'P'   ;
	    Swip_maillon.nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
	    if (itab[ j][l1   ][ 1 ] == 'D') {
	        if (Swip_maillon.aieul_( kbuf[  itab[ j][l2][2 ] -1],itab[ j][l1   ][ 2 ]) != 0)
	                Swip_maillon.aieul_( kbuf[itab[ j][l1   ][ 2 ] -1],itab[ j][l2][2 ]);
	    } else {
	        if (Swip_maillon.aieul_( kbuf[ itab[ j][l2][2 ] -1],-itab[ j][l1   ][ 2 ]) != 0)
	                Swip_maillon.fils_ ( kbuf[ itab[ j][l1   ][ 2 ] -1],itab[ j][l2][2 ]);
	    }
	}
	
	/****************************************************************************/
	
	public static void P_XFXO(int i,int j,Short edge,int [][][] itab, int l1, int l2, Chaine[] kbuf,int idebug)
	
	
	
	{
	    Chaine UneChaine;
	    int sipremier=0;
	
	    if (idebug!=0) System.out.println("Configuration P_XFXO Line "+i+" Col "+j+"");
	
	    itab[ j][l2 ][ 2 ] = itab[ j-1][l2 ][ 2 ];
	    itab[ j][l2 ][ 1 ] = 'O' ;
	    if (itab[ j-1][l2 ][ 1 ] == 'P') {
	        itab[ j-1][l2 ][ 1 ] = 'D';
	        sipremier = 1;
	    } else itab[ j-1][l2 ][ 1 ] = 'X';
	    itab[ j][l2 ][ 1 ] = 'F';
	    Swip_maillon.nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
	    if (itab[ j][l1   ][ 1 ] == 'F') {
	        if (Swip_maillon.fils_( kbuf [itab[ j][l1   ][ 2 ] -1],-itab[ j][l2][2 ]) != 0)
	                Swip_maillon.fils_( kbuf [itab[ j][l2][2 ] -1],-itab[ j][l1   ][ 2 ]);
	    } else {
	        if (Swip_maillon.aieul_( kbuf [ itab[ j][l1   ][ 2 ]-1],-itab[ j][l2][2 ]) != 0)
	                Swip_maillon.fils_ ( kbuf [itab[ j][l2][2 ] -1],itab[ j][l1   ][ 2 ]);
	    }
	}
	
	/****************************************************************************/
	
	public static void P_XFXF(int i,int j,Short edge,int [][][] itab, int l1, int l2, Chaine[] kbuf,int idebug)
	
	
	
	{
	    Chaine UneChaine;
	    int sipremier=0;
	
	    if (idebug!=0) System.out.println("Configuration P_XFXF Line "+i+" Col "+j+"");
	
	    itab[ j][l2 ][ 2 ] = itab[ j-1][l2][ 2 ];
	    itab[ j][l2 ][ 1 ] = 'F';
	    Swip_maillon.nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
	    if (itab[ j][l1   ][ 1 ] == 'F') {
	        if (Swip_maillon.fils_( kbuf [itab[ j][l1   ][ 2 ] -1],-itab[ j][l2][2 ]) != 0)
	                Swip_maillon.fils_( kbuf [itab[ j][l2][2 ] -1],-itab[ j][l1   ][ 2 ]);
	    } else {
	        if (Swip_maillon.aieul_( kbuf [ itab[ j][l1   ][ 2 ]-1],-itab[ j][l2][2 ]) != 0)
	                Swip_maillon.fils_ ( kbuf [itab[ j][l2][2 ] -1],itab[ j][l1   ][ 2 ]);
	    }
	    if (itab[ j-1][l2 ][ 1 ] == 'F') {
	        if (Swip_maillon.fils_( kbuf [itab[ j-1][l2 ][ 2 ] -1],-itab[ j][l2][2 ]) != 0)
	                Swip_maillon.fils_( kbuf [itab[ j][l2][2 ] -1],-itab[ j-1][l2 ][ 2 ]);
	    } else {
	        if (Swip_maillon.aieul_( kbuf [ itab[ j-1][l2 ][ 2 ]-1],-itab[ j][l2][2 ]) != 0)
	                Swip_maillon.fils_ ( kbuf [itab[ j][l2][2 ] -1],itab[ j-1][l2 ][ 2 ]);
	    }
	}
	
	/****************************************************************************/
	
	public static void P_XFOX(int i,int j,Short edge,int [][][] itab, int l1, int l2, Chaine[] kbuf,int idebug)
	
	
	
	{
	    Chaine UneChaine;
	    int sipremier=0;
	
	    if (idebug!=0) System.out.println("Configuration P_XFOX Line "+i+" Col "+j+"");
	
	    itab[ j][l2 ][ 2 ] = itab[ j+1][l1 ][ 2 ];
	    itab[ j][l2 ][ 1 ] = 'O' ;
	    if (itab[ j+1][l1 ][ 1 ] == 'P') {
	        itab[ j+1][l1 ][ 1 ] = 'D';
	        sipremier = 1;
	    } else itab[ j+1][l1 ][ 1 ] = 'X';
	    Swip_maillon.nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
	    if ( ptest5668(i,j,edge) ) {
	        itab[ j][l2 ][ 1 ] = 'F';
	        if (itab[ j][l1   ][ 1 ] == 'F') {
	            if (Swip_maillon.fils_( kbuf [itab[ j][l1   ][ 2 ] -1],-itab[ j][l2][2 ]) != 0)
	                    Swip_maillon.fils_( kbuf [itab[ j][l2][2 ] -1],-itab[ j][l1   ][ 2 ]);
	        } else {
	            if (Swip_maillon.aieul_( kbuf [ itab[ j][l1   ][ 2 ]-1],-itab[ j][l2][2 ]) != 0)
	                    Swip_maillon.fils_ ( kbuf [itab[ j][l2][2 ] -1],itab[ j][l1   ][ 2 ]);
	        }
	    }
	}
	
	/****************************************************************************/
	
	public static void P_XFOO(int i,int j,Short edge,int [][][] itab, int l1, int l2, Chaine[] kbuf,int idebug)
	
	
	
	{
	    Chaine UneChaine;
	    int sipremier=0;
	
	    if (idebug!=0) System.out.println("Configuration P_XFOO Line "+i+" Col "+j+"");
	
	    itab[ j][l2 ][ 2 ] = itab[ j-1][l2 ][ 2 ];
	    itab[ j][l2 ][ 1 ] = 'O' ;
	    if (itab[ j-1][l2 ][ 1 ] == 'P') {
	        itab[ j-1][l2 ][ 1 ] = 'D';
	        sipremier = 1;
	    } else itab[ j-1][l2 ][ 1 ] = 'X';
	    Swip_maillon.nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
	    if (itab[ j+1][l1 ][ 1 ] == 'P') itab[ j+1][l1 ][ 1 ] = 'D'; else itab[ j+1][l1 ][ 1 ] = 'F';
	    itab[ j][l2 ][ 1 ] = 'F';
	    if (itab[ j][l1   ][ 1 ] == 'F') {
	        if (Swip_maillon.fils_( kbuf [itab[ j][l1   ][ 2 ] -1],-itab[ j][l2][2 ]) != 0)
	                Swip_maillon.fils_( kbuf [itab[ j][l2][2 ] -1],-itab[ j][l1   ][ 2 ]);
	    } else {
	        if (Swip_maillon.aieul_( kbuf [ itab[ j][l1   ][ 2 ]-1],-itab[ j][l2][2 ]) != 0)
	                Swip_maillon.fils_ ( kbuf [itab[ j][l2][2 ] -1],itab[ j][l1   ][ 2 ]);
	    }
	}
	
	/****************************************************************************/
	
	public static void P_XFOF(int i,int j,Short edge,int [][][] itab, int l1, int l2, Chaine[] kbuf,int idebug)
	
	
	
	{
	    Chaine UneChaine;
	    int sipremier=0;
	
	    if (idebug!=0) System.out.println("Configuration P_XFOF Line "+i+" Col "+j+"");
	
	    if (itab[ j+1][l1 ][ 1 ] == 'P') itab[ j+1][l1 ][ 1 ] = 'D'; else itab[ j+1][l1 ][ 1 ] = 'F';
	    itab[ j][l2 ][ 2 ] = itab[ j-1][l2][ 2 ];
	    itab[ j][l2 ][ 1 ] = 'F';
	    Swip_maillon.nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
	    if (itab[ j][l1   ][ 1 ] == 'F') {
	        if (Swip_maillon.fils_( kbuf [itab[ j][l1   ][ 2 ] -1],-itab[ j][l2][2 ]) != 0)
	                Swip_maillon.fils_( kbuf [itab[ j][l2][2 ] -1],-itab[ j][l1   ][ 2 ]);
	    } else {
	        if (Swip_maillon.aieul_( kbuf [ itab[ j][l1   ][ 2 ]-1],-itab[ j][l2][2 ]) != 0)
	                Swip_maillon.fils_ ( kbuf [itab[ j][l2][2 ] -1],itab[ j][l1   ][ 2 ]);
	    }
	    if (itab[ j-1][l2 ][ 1 ] == 'F') {
	        if (Swip_maillon.fils_( kbuf [itab[ j-1][l2 ][ 2 ] -1],-itab[ j][l2][2 ]) != 0)
	                Swip_maillon.fils_( kbuf [itab[ j][l2][2 ] -1],-itab[ j-1][l2 ][ 2 ]);
	    } else {
	        if (Swip_maillon.aieul_( kbuf [ itab[ j-1][l2 ][ 2 ]-1],-itab[ j][l2][2 ]) != 0)
	                Swip_maillon.fils_ ( kbuf [itab[ j][l2][2 ] -1],itab[ j-1][l2 ][ 2 ]);
	    }
	}
	
	/****************************************************************************/
	
	public static void P_XFFO(int i,int j,Short edge,int [][][] itab, int l1, int l2, Chaine[] kbuf,int idebug)
	
	
	
	{
	    Chaine UneChaine;
	    int sipremier=0;
	
	    if (idebug!=0) System.out.println("Configuration P_XFFO Line "+i+" Col "+j+"");
	
	    itab[ j][l2 ][ 2 ] = itab[ j-1][l2 ][ 2 ];
	    itab[ j][l2 ][ 1 ] = 'O' ;
	    if (itab[ j-1][l2 ][ 1 ] == 'P') {
	        itab[ j-1][l2 ][ 1 ] = 'D';
	        sipremier = 1;
	    } else itab[ j-1][l2 ][ 1 ] = 'X';
	    itab[ j][l2 ][ 1 ] = 'F';
	    Swip_maillon.nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
	    if (itab[ j][l1   ][ 1 ] == 'F') {
	        if (Swip_maillon.fils_( kbuf [itab[ j][l1   ][ 2 ] -1],-itab[ j][l2][2 ]) != 0)
	                Swip_maillon.fils_( kbuf [itab[ j][l2][2 ] -1],-itab[ j][l1   ][ 2 ]);
	    } else {
	        if (Swip_maillon.aieul_( kbuf [ itab[ j][l1   ][ 2 ]-1],-itab[ j][l2][2 ]) != 0)
	                Swip_maillon.fils_ ( kbuf [itab[ j][l2][2 ] -1],itab[ j][l1   ][ 2 ]);
	    }
	}
	
	/****************************************************************************/
	
	public static void P_XFFF(int i,int j,Short edge,int [][][] itab, int l1, int l2, Chaine[] kbuf,int idebug)
	
	
	
	{
	    Chaine UneChaine;
	    int sipremier=0;
	
	    if (idebug!=0) System.out.println("Configuration P_XFFF Line "+i+" Col "+j+"");
	
	    itab[ j][l2 ][ 2 ] = itab[ j-1][l2][ 2 ];
	    itab[ j][l2 ][ 1 ] = 'F';
	    Swip_maillon.nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
	    if (itab[ j][l1   ][ 1 ] == 'F') {
	        if (Swip_maillon.fils_( kbuf [itab[ j][l1   ][ 2 ] -1],-itab[ j][l2][2 ]) != 0)
	                Swip_maillon.fils_( kbuf [itab[ j][l2][2 ] -1],-itab[ j][l1   ][ 2 ]);
	    } else {
	        if (Swip_maillon.aieul_( kbuf [ itab[ j][l1   ][ 2 ]-1],-itab[ j][l2][2 ]) != 0)
	                Swip_maillon.fils_ ( kbuf [itab[ j][l2][2 ] -1],itab[ j][l1   ][ 2 ]);
	    }
	    if (itab[ j-1][l2 ][ 1 ] == 'F') {
	        if (Swip_maillon.fils_( kbuf [itab[ j-1][l2 ][ 2 ] -1],-itab[ j][l2][2 ]) != 0)
	                Swip_maillon.fils_( kbuf [itab[ j][l2][2 ] -1],-itab[ j-1][l2 ][ 2 ]);
	    } else {
	        if (Swip_maillon.aieul_( kbuf [ itab[ j-1][l2 ][ 2 ]-1],-itab[ j][l2][2 ]) != 0)
	                Swip_maillon.fils_ ( kbuf [itab[ j][l2][2 ] -1],itab[ j-1][l2 ][ 2 ]);
	    }
	}
	
	/****************************************************************************/
	
	public static void P_OXXX(int i,int j,Short edge,int [][][] itab, int l1, int l2, Chaine[] kbuf,int idebug)
	
	
	
	{
	    Chaine UneChaine;
	    int sipremier=0;
	
	    if (idebug!=0) System.out.println("Configuration P_OXXX Line "+i+" Col "+j+"");
	
	    itab[ j][l2 ][ 2 ] = itab[ j-1][l1 ][ 2 ];
	    itab[ j][l2 ][ 1 ] = 'O' ;
	    if (itab[ j-1][l1 ][ 1 ] == 'P') {
	        itab[ j-1][l1 ][ 1 ] = 'D';
	        sipremier = 1;
	    } else itab[ j-1][l1 ][ 1 ] = 'X';
	    Swip_maillon.nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
	    if ( ptest5668(i,j,edge) ) {
	        itab[ j][l2 ][ 1 ] = 'F';
	    }
	}
	
	/****************************************************************************/
	
	public static void P_OXOX(int i,int j,Short edge,int [][][] itab, int l1, int l2, Chaine[] kbuf,int idebug)
	
	
	
	{
	    Chaine UneChaine;
	    int sipremier=0;
	
	    if (idebug!=0) System.out.println("Configuration P_OXOX Line "+i+" Col "+j+"");
	
	    itab[ j][l2 ][ 2 ] = itab[ j-1][l1 ][ 2 ];
	    itab[ j][l2 ][ 1 ] = 'O' ;
	    if (itab[ j-1][l1 ][ 1 ] == 'P') {
	        itab[ j-1][l1 ][ 1 ] = 'D';
	        sipremier = 1;
	    } else itab[ j-1][l1 ][ 1 ] = 'X';
	    Swip_maillon.nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
	    if (itab[ j+1][l1 ][ 1 ] == 'P') itab[ j+1][l1 ][ 1 ] = 'D'; else itab[ j+1][l1 ][ 1 ] = 'F';
	    itab[ j][l2 ][ 1 ] = 'F';
	    if (itab[j-1][l1][2] != itab[j+1][l1][2])
	        if (!(ptest5(i,j,edge) != 0)) {
	            if (itab[ j+1][l1 ][ 1 ] == 'F') {
	                if (Swip_maillon.fils_( kbuf [itab[ j+1][l1 ][ 2 ] -1],-itab[ j][l2][2 ]) != 0)
	                        Swip_maillon.fils_( kbuf [itab[ j][l2][2 ] -1],-itab[ j+1][l1 ][ 2 ]);
	            } else {
	                if (Swip_maillon.aieul_( kbuf [ itab[ j+1][l1 ][ 2 ]-1],-itab[ j][l2][2 ]) != 0)
	                        Swip_maillon.fils_ ( kbuf [itab[ j][l2][2 ] -1],itab[ j+1][l1 ][ 2 ]);
	            }
	    }
	}
	
	/****************************************************************************/
	
	public static void P_OXFX(int i,int j,Short edge,int [][][] itab, int l1, int l2, Chaine[] kbuf,int idebug)
	
	
	
	{
	    Chaine UneChaine;
	    int sipremier=0;
	
	    if (idebug!=0) System.out.println("Configuration P_OXFX Line "+i+" Col "+j+"");
	
	    itab[ j][l2 ][ 2 ] = itab[ j-1][l1 ][ 2 ];
	    itab[ j][l2 ][ 1 ] = 'O' ;
	    if (itab[ j-1][l1 ][ 1 ] == 'P') {
	        itab[ j-1][l1 ][ 1 ] = 'D';
	        sipremier = 1;
	    } else itab[ j-1][l1 ][ 1 ] = 'X';
	    Swip_maillon.nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
	    itab[ j][l2 ][ 1 ] = 'F';
	    if (itab[j-1][l1][2] != itab[j+1][l1][2])
	        if (!(ptest5(i,j,edge) != 0)) {
	            if (itab[ j+1][l1 ][ 1 ] == 'F') {
	                if (Swip_maillon.fils_( kbuf [itab[ j+1][l1 ][ 2 ] -1],-itab[ j][l2][2 ]) != 0)
	                        Swip_maillon.fils_( kbuf [itab[ j][l2][2 ] -1],-itab[ j+1][l1 ][ 2 ]);
	            } else {
	                if (Swip_maillon.aieul_( kbuf [ itab[ j+1][l1 ][ 2 ]-1],-itab[ j][l2][2 ]) != 0)
	                        Swip_maillon.fils_ ( kbuf [itab[ j][l2][2 ] -1],itab[ j+1][l1 ][ 2 ]);
	            }
	    }
	}
	
	/****************************************************************************/
	
	public static void P_FXXX(int i,int j,Short edge,int [][][] itab, int l1, int l2, Chaine[] kbuf,int idebug)
	
	
	
	{
	    Chaine UneChaine;
	    int sipremier=0;
	
	    if (idebug!=0) System.out.println("Configuration P_FXXX Line "+i+" Col "+j+"");
	
	    LeLinker.nb_chaines += 1;
	    if (LeLinker.nb_chaines >= LeLinker.nb_chaines_max) {
	        System.out.println("Too many chains\n");
	        System.exit(-1);
	    }
	    UneChaine = (Chaine) Swip_maillon.opench_();
	    if (UneChaine == null) {
	        System.out.println("There is no more space for chains.\n");
	        System.exit(-2);
	    }
	    kbuf[ LeLinker.nb_chaines - 1] = UneChaine ;
	    itab[ j][l2 ][ 2 ] = LeLinker.nb_chaines;
	    itab[ j][l2 ][ 1 ] = 'P'   ;
	    Swip_maillon.nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
	    if (itab[ j-1][l1 ][ 1 ] == 'D') {
	        if (Swip_maillon.aieul_( kbuf[  itab[ j][l2][2 ] -1],itab[ j-1][l1 ][ 2 ]) != 0)
	                Swip_maillon.aieul_( kbuf[itab[ j-1][l1 ][ 2 ] -1],itab[ j][l2][2 ]);
	    } else {
	        if (Swip_maillon.aieul_( kbuf[ itab[ j][l2][2 ] -1],-itab[ j-1][l1 ][ 2 ]) != 0)
	                Swip_maillon.fils_ ( kbuf[ itab[ j-1][l1 ][ 2 ] -1],itab[ j][l2][2 ]);
	    }
	}
	
	/****************************************************************************/
	
	public static void P_FXXO(int i,int j,Short edge,int [][][] itab, int l1, int l2, Chaine[] kbuf,int idebug)
	
	
	
	{
	    Chaine UneChaine;
	    int sipremier=0;
	
	    if (idebug!=0) System.out.println("Configuration P_FXXO Line "+i+" Col "+j+"");
	
	    itab[ j][l2 ][ 2 ] = itab[ j-1][l2 ][ 2 ];
	    itab[ j][l2 ][ 1 ] = 'O' ;
	    if (itab[ j-1][l2 ][ 1 ] == 'P') {
	        itab[ j-1][l2 ][ 1 ] = 'D';
	        sipremier = 1;
	    } else itab[ j-1][l2 ][ 1 ] = 'X';
	    Swip_maillon.nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
	    if (!(sipremier != 0))
	        if ( ptest5668(i,j,edge) ) {
	            itab[ j][l2 ][ 1 ] = 'F';
	            if (itab[ j-1][l1 ][ 1 ] == 'F') {
	                if (Swip_maillon.fils_( kbuf [itab[ j-1][l1 ][ 2 ] -1],-itab[ j][l2][2 ]) != 0)
	                        Swip_maillon.fils_( kbuf [itab[ j][l2][2 ] -1],-itab[ j-1][l1 ][ 2 ]);
	            } else {
	                if (Swip_maillon.aieul_( kbuf [ itab[ j-1][l1 ][ 2 ]-1],-itab[ j][l2][2 ]) != 0)
	                        Swip_maillon.fils_ ( kbuf [itab[ j][l2][2 ] -1],itab[ j-1][l1 ][ 2 ]);
	            }
	        }
	}
	
	/****************************************************************************/
	
	public static void P_FXXF(int i,int j,Short edge,int [][][] itab, int l1, int l2, Chaine[] kbuf,int idebug)
	
	
	
	{
	    Chaine UneChaine;
	    int sipremier=0;
	
	    if (idebug!=0) System.out.println("Configuration P_FXXF Line "+i+" Col "+j+"");
	
	    LeLinker.nb_chaines += 1;
	    if (LeLinker.nb_chaines >= LeLinker.nb_chaines_max) {
	        System.out.println("Too many chains\n");
	        System.exit(-1);
	    }
	    UneChaine = (Chaine) Swip_maillon.opench_();
	    if (UneChaine == null) {
	        System.out.println("There is no more space for chains.\n");
	        System.exit(-2);
	    }
	    kbuf[ LeLinker.nb_chaines - 1] = UneChaine ;
	    itab[ j][l2 ][ 2 ] = LeLinker.nb_chaines;
	    itab[ j][l2 ][ 1 ] = 'P'   ;
	    Swip_maillon.nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
	    if (itab[ j-1][l2 ][ 1 ] == 'D') {
	        if (Swip_maillon.aieul_( kbuf[  itab[ j][l2][2 ] -1],itab[ j-1][l2 ][ 2 ]) != 0)
	                Swip_maillon.aieul_( kbuf[itab[ j-1][l2 ][ 2 ] -1],itab[ j][l2][2 ]);
	    } else {
	        if (Swip_maillon.aieul_( kbuf[ itab[ j][l2][2 ] -1],-itab[ j-1][l2 ][ 2 ]) != 0)
	                Swip_maillon.fils_ ( kbuf[ itab[ j-1][l2 ][ 2 ] -1],itab[ j][l2][2 ]);
	    }
	}
	
	/****************************************************************************/
	
	public static void P_FXOX(int i,int j,Short edge,int [][][] itab, int l1, int l2, Chaine[] kbuf,int idebug)
	
	
	
	{
	    Chaine UneChaine;
	    int sipremier=0;
	
	    if (idebug!=0) System.out.println("Configuration P_FXOX Line "+i+" Col "+j+"");
	
	    itab[ j][l2 ][ 2 ] = itab[ j+1][l1 ][ 2 ];
	    itab[ j][l2 ][ 1 ] = 'O' ;
	    if (itab[ j+1][l1 ][ 1 ] == 'P') {
	        itab[ j+1][l1 ][ 1 ] = 'D';
	        sipremier = 1;
	    } else itab[ j+1][l1 ][ 1 ] = 'X';
	    Swip_maillon.nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
	    itab[ j][l2 ][ 1 ] = 'F';
	    if (itab[ j-1][l1 ][ 1 ] == 'F') {
	        if (Swip_maillon.fils_( kbuf [itab[ j-1][l1 ][ 2 ] -1],-itab[ j][l2][2 ]) != 0)
	                Swip_maillon.fils_( kbuf [itab[ j][l2][2 ] -1],-itab[ j-1][l1 ][ 2 ]);
	    } else {
	        if (Swip_maillon.aieul_( kbuf [ itab[ j-1][l1 ][ 2 ]-1],-itab[ j][l2][2 ]) != 0)
	                Swip_maillon.fils_ ( kbuf [itab[ j][l2][2 ] -1],itab[ j-1][l1 ][ 2 ]);
	    }
	}
	
	/****************************************************************************/
	
	public static void P_FXOO(int i,int j,Short edge,int [][][] itab, int l1, int l2, Chaine[] kbuf,int idebug)
	
	
	
	{
	    Chaine UneChaine;
	    int sipremier=0;
	
	    if (idebug!=0) System.out.println("Configuration P_FXOO Line "+i+" Col "+j+"");
	
	    itab[ j][l2 ][ 2 ] = itab[ j-1][l2 ][ 2 ];
	    itab[ j][l2 ][ 1 ] = 'O' ;
	    if (itab[ j-1][l2 ][ 1 ] == 'P') {
	        itab[ j-1][l2 ][ 1 ] = 'D';
	        sipremier = 1;
	    } else itab[ j-1][l2 ][ 1 ] = 'X';
	    Swip_maillon.nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
	    if (itab[ j+1][l1 ][ 1 ] == 'P') itab[ j+1][l1 ][ 1 ] = 'D'; else itab[ j+1][l1 ][ 1 ] = 'F';
	    itab[ j][l2 ][ 1 ] = 'F';
	    if (itab[ j-1][l1 ][ 1 ] == 'F') {
	        if (Swip_maillon.fils_( kbuf [itab[ j-1][l1 ][ 2 ] -1],-itab[ j][l2][2 ]) != 0)
	                Swip_maillon.fils_( kbuf [itab[ j][l2][2 ] -1],-itab[ j-1][l1 ][ 2 ]);
	    } else {
	        if (Swip_maillon.aieul_( kbuf [ itab[ j-1][l1 ][ 2 ]-1],-itab[ j][l2][2 ]) != 0)
	                Swip_maillon.fils_ ( kbuf [itab[ j][l2][2 ] -1],itab[ j-1][l1 ][ 2 ]);
	    }
	    if (itab[j-1][l1][2] != itab[j+1][l1][2])
	        if (!(ptest5(i,j,edge) != 0)) {
	            if (itab[ j+1][l1 ][ 1 ] == 'F') {
	                if (Swip_maillon.fils_( kbuf [itab[ j+1][l1 ][ 2 ] -1],-itab[ j][l2][2 ]) != 0)
	                        Swip_maillon.fils_( kbuf [itab[ j][l2][2 ] -1],-itab[ j+1][l1 ][ 2 ]);
	            } else {
	                if (Swip_maillon.aieul_( kbuf [ itab[ j+1][l1 ][ 2 ]-1],-itab[ j][l2][2 ]) != 0)
	                        Swip_maillon.fils_ ( kbuf [itab[ j][l2][2 ] -1],itab[ j+1][l1 ][ 2 ]);
	            }
	    }
	}
	
	/****************************************************************************/
	
	public static void P_FXOF(int i,int j,Short edge,int [][][] itab, int l1, int l2, Chaine[] kbuf,int idebug)
	
	
	
	{
	    Chaine UneChaine;
	    int sipremier=0;
	
	    if (idebug!=0) System.out.println("Configuration P_FXOF Line "+i+" Col "+j+"");
	
	    itab[ j][l2 ][ 2 ] = itab[ j+1][l1 ][ 2 ];
	    itab[ j][l2 ][ 1 ] = 'O' ;
	    if (itab[ j+1][l1 ][ 1 ] == 'P') {
	        itab[ j+1][l1 ][ 1 ] = 'D';
	        sipremier = 1;
	    } else itab[ j+1][l1 ][ 1 ] = 'X';
	    Swip_maillon.nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
	    itab[ j][l2 ][ 1 ] = 'F';
	    if (itab[ j-1][l2 ][ 1 ] == 'F') {
	        if (Swip_maillon.fils_( kbuf [itab[ j-1][l2 ][ 2 ] -1],-itab[ j][l2][2 ]) != 0)
	                Swip_maillon.fils_( kbuf [itab[ j][l2][2 ] -1],-itab[ j-1][l2 ][ 2 ]);
	    } else {
	        if (Swip_maillon.aieul_( kbuf [ itab[ j-1][l2 ][ 2 ]-1],-itab[ j][l2][2 ]) != 0)
	                Swip_maillon.fils_ ( kbuf [itab[ j][l2][2 ] -1],itab[ j-1][l2 ][ 2 ]);
	    }
	}
	
	/****************************************************************************/
	
	public static void P_FXFX(int i,int j,Short edge,int [][][] itab, int l1, int l2, Chaine[] kbuf,int idebug)
	
	
	
	{
	    Chaine UneChaine;
	    int sipremier=0;
	
	    if (idebug!=0) System.out.println("Configuration P_FXFX Line "+i+" Col "+j+"");
	
	    LeLinker.nb_chaines += 1;
	    if (LeLinker.nb_chaines >= LeLinker.nb_chaines_max) {
	        System.out.println("Too many chains\n");
	        System.exit(-1);
	    }
	    UneChaine = (Chaine) Swip_maillon.opench_();
	    if (UneChaine == null) {
	        System.out.println("There is no more space for chains.\n");
	        System.exit(-2);
	    }
	    kbuf[ LeLinker.nb_chaines - 1] = UneChaine ;
	    itab[ j][l2 ][ 2 ] = LeLinker.nb_chaines;
	    itab[ j][l2 ][ 1 ] = 'P'   ;
	    Swip_maillon.nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
	    if (itab[ j-1][l1 ][ 1 ] == 'D') {
	        if (Swip_maillon.aieul_( kbuf[  itab[ j][l2][2 ] -1],itab[ j-1][l1 ][ 2 ]) != 0)
	                Swip_maillon.aieul_( kbuf[itab[ j-1][l1 ][ 2 ] -1],itab[ j][l2][2 ]);
	    } else {
	        if (Swip_maillon.aieul_( kbuf[ itab[ j][l2][2 ] -1],-itab[ j-1][l1 ][ 2 ]) != 0)
	                Swip_maillon.fils_ ( kbuf[ itab[ j-1][l1 ][ 2 ] -1],itab[ j][l2][2 ]);
	    }
	    if (itab[j-1][l1][2] != itab[j+1][l1][2])
	        if (!(ptest5(i,j,edge) != 0)) {
	            if (itab[ j+1][l1 ][ 1 ] == 'D') {
	                if (Swip_maillon.aieul_( kbuf[  itab[ j][l2][2 ] -1],itab[ j+1][l1 ][ 2 ]) != 0)
	                        Swip_maillon.aieul_( kbuf[itab[ j+1][l1 ][ 2 ] -1],itab[ j][l2][2 ]);
	            } else {
	                if (Swip_maillon.aieul_( kbuf[ itab[ j][l2][2 ] -1],-itab[ j+1][l1 ][ 2 ]) != 0)
	                        Swip_maillon.fils_ ( kbuf[ itab[ j+1][l1 ][ 2 ] -1],itab[ j][l2][2 ]);
	            }
	    }
	}
	
	/****************************************************************************/
	
	public static void P_FXFO(int i,int j,Short edge,int [][][] itab, int l1, int l2, Chaine[] kbuf,int idebug)
	
	
	
	{
	    Chaine UneChaine;
	    int sipremier=0;
	
	    if (idebug!=0) System.out.println("Configuration P_FXFO Line "+i+" Col "+j+"");
	
	    itab[ j][l2 ][ 2 ] = itab[ j-1][l2 ][ 2 ];
	    itab[ j][l2 ][ 1 ] = 'O' ;
	    if (itab[ j-1][l2 ][ 1 ] == 'P') {
	        itab[ j-1][l2 ][ 1 ] = 'D';
	        sipremier = 1;
	    } else itab[ j-1][l2 ][ 1 ] = 'X';
	    Swip_maillon.nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
	    itab[ j][l2 ][ 1 ] = 'F';
	    if (itab[ j-1][l1 ][ 1 ] == 'F') {
	        if (Swip_maillon.fils_( kbuf [itab[ j-1][l1 ][ 2 ] -1],-itab[ j][l2][2 ]) != 0)
	                Swip_maillon.fils_( kbuf [itab[ j][l2][2 ] -1],-itab[ j-1][l1 ][ 2 ]);
	    } else {
	        if (Swip_maillon.aieul_( kbuf [ itab[ j-1][l1 ][ 2 ]-1],-itab[ j][l2][2 ]) != 0)
	                Swip_maillon.fils_ ( kbuf [itab[ j][l2][2 ] -1],itab[ j-1][l1 ][ 2 ]);
	    }
	    if (itab[j-1][l1][2] != itab[j+1][l1][2])
	        if (!(ptest5(i,j,edge) != 0)) {
	            if (itab[ j+1][l1 ][ 1 ] == 'F') {
	                if (Swip_maillon.fils_( kbuf [itab[ j+1][l1 ][ 2 ] -1],-itab[ j][l2][2 ]) != 0)
	                        Swip_maillon.fils_( kbuf [itab[ j][l2][2 ] -1],-itab[ j+1][l1 ][ 2 ]);
	            } else {
	                if (Swip_maillon.aieul_( kbuf [ itab[ j+1][l1 ][ 2 ]-1],-itab[ j][l2][2 ]) != 0)
	                        Swip_maillon.fils_ ( kbuf [itab[ j][l2][2 ] -1],itab[ j+1][l1 ][ 2 ]);
	            }
	    }
	}
	
	/****************************************************************************/
	
	public static void P_FXFF(int i,int j,Short edge,int [][][] itab, int l1, int l2, Chaine[] kbuf,int idebug)
	
	
	
	{
	    Chaine UneChaine;
	    int sipremier=0;
	
	    if (idebug!=0) System.out.println("Configuration P_FXFF Line "+i+" Col "+j+"");
	
	    LeLinker.nb_chaines += 1;
	    if (LeLinker.nb_chaines >= LeLinker.nb_chaines_max) {
	        System.out.println("Too many chains\n");
	        System.exit(-1);
	    }
	    UneChaine = (Chaine) Swip_maillon.opench_();
	    if (UneChaine == null) {
	        System.out.println("There is no more space for chains.\n");
	        System.exit(-2);
	    }
	    kbuf[ LeLinker.nb_chaines - 1] = UneChaine ;
	    itab[ j][l2 ][ 2 ] = LeLinker.nb_chaines;
	    itab[ j][l2 ][ 1 ] = 'P'   ;
	    Swip_maillon.nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
	    if (itab[j-1][l1][2] != itab[j+1][l1][2])
	        if (!(ptest5(i,j,edge) != 0)) {
	            if (itab[ j+1][l1 ][ 1 ] == 'D') {
	                if (Swip_maillon.aieul_( kbuf[  itab[ j][l2][2 ] -1],itab[ j+1][l1 ][ 2 ]) != 0)
	                        Swip_maillon.aieul_( kbuf[itab[ j+1][l1 ][ 2 ] -1],itab[ j][l2][2 ]);
	            } else {
	                if (Swip_maillon.aieul_( kbuf[ itab[ j][l2][2 ] -1],-itab[ j+1][l1 ][ 2 ]) != 0)
	                        Swip_maillon.fils_ ( kbuf[ itab[ j+1][l1 ][ 2 ] -1],itab[ j][l2][2 ]);
	            }
	    }
	    if (itab[ j-1][l2 ][ 1 ] == 'D') {
	        if (Swip_maillon.aieul_( kbuf[  itab[ j][l2][2 ] -1],itab[ j-1][l2 ][ 2 ]) != 0)
	                Swip_maillon.aieul_( kbuf[itab[ j-1][l2 ][ 2 ] -1],itab[ j][l2][2 ]);
	    } else {
	        if (Swip_maillon.aieul_( kbuf[ itab[ j][l2][2 ] -1],-itab[ j-1][l2 ][ 2 ]) != 0)
	                Swip_maillon.fils_ ( kbuf[ itab[ j-1][l2 ][ 2 ] -1],itab[ j][l2][2 ]);
	    }
	}
	
	/****************************************************************************/
	
	public static void P_FOXX(int i,int j,Short edge,int [][][] itab, int l1, int l2, Chaine[] kbuf,int idebug)
	
	
	
	{
	    Chaine UneChaine;
	    int sipremier=0;
	
	    if (idebug!=0) System.out.println("Configuration P_FOXX Line "+i+" Col "+j+"");
	
	    itab[ j][l2 ][ 2 ] = itab[ j][l1   ][ 2 ];
	    itab[ j][l2 ][ 1 ] = 'O' ;
	    if (itab[ j][l1   ][ 1 ] == 'P') {
	        itab[ j][l1   ][ 1 ] = 'D';
	        sipremier = 1;
	    } else itab[ j][l1   ][ 1 ] = 'X';
	    Swip_maillon.nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
	    if ( ptest5668(i,j,edge) ) {
	        itab[ j][l2 ][ 1 ] = 'F';
	        if (itab[ j-1][l1 ][ 1 ] == 'F') {
	            if (Swip_maillon.fils_( kbuf [itab[ j-1][l1 ][ 2 ] -1],-itab[ j][l2][2 ]) != 0)
	                    Swip_maillon.fils_( kbuf [itab[ j][l2][2 ] -1],-itab[ j-1][l1 ][ 2 ]);
	        } else {
	            if (Swip_maillon.aieul_( kbuf [ itab[ j-1][l1 ][ 2 ]-1],-itab[ j][l2][2 ]) != 0)
	                    Swip_maillon.fils_ ( kbuf [itab[ j][l2][2 ] -1],itab[ j-1][l1 ][ 2 ]);
	        }
	    }
	}
	
	/****************************************************************************/
	
	public static void P_FFXF(int i,int j,Short edge,int [][][] itab, int l1, int l2, Chaine[] kbuf,int idebug)
	
	
	
	{
	    Chaine UneChaine;
	    int sipremier=0;
	
	    if (idebug!=0) System.out.println("Configuration P_FFXF Line "+i+" Col "+j+"");
	
	    itab[ j][l2 ][ 2 ] = itab[ j-1][l2][ 2 ];
	    itab[ j][l2 ][ 1 ] = 'F';
	    Swip_maillon.nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
	    if (itab[ j-1][l2 ][ 1 ] == 'F') {
	        if (Swip_maillon.fils_( kbuf [itab[ j-1][l2 ][ 2 ] -1],-itab[ j][l2][2 ]) != 0)
	                Swip_maillon.fils_( kbuf [itab[ j][l2][2 ] -1],-itab[ j-1][l2 ][ 2 ]);
	    } else {
	        if (Swip_maillon.aieul_( kbuf [ itab[ j-1][l2 ][ 2 ]-1],-itab[ j][l2][2 ]) != 0)
	                Swip_maillon.fils_ ( kbuf [itab[ j][l2][2 ] -1],itab[ j-1][l2 ][ 2 ]);
	    }
	}
	
	/****************************************************************************/
	
	public static void P_FFOX(int i,int j,Short edge,int [][][] itab, int l1, int l2, Chaine[] kbuf,int idebug)
	
	
	
	{
	    Chaine UneChaine;
	    int sipremier=0;
	
	    if (idebug!=0) System.out.println("Configuration P_FFOX Line "+i+" Col "+j+"");
	
	    itab[ j][l2 ][ 2 ] = itab[ j+1][l1 ][ 2 ];
	    itab[ j][l2 ][ 1 ] = 'O' ;
	    if (itab[ j+1][l1 ][ 1 ] == 'P') {
	        itab[ j+1][l1 ][ 1 ] = 'D';
	        sipremier = 1;
	    } else itab[ j+1][l1 ][ 1 ] = 'X';
	    Swip_maillon.nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
	    itab[ j][l2 ][ 1 ] = 'F';
	    if (itab[ j][l1   ][ 1 ] == 'F') {
	        if (Swip_maillon.fils_( kbuf [itab[ j][l1   ][ 2 ] -1],-itab[ j][l2][2 ]) != 0)
	                Swip_maillon.fils_( kbuf [itab[ j][l2][2 ] -1],-itab[ j][l1   ][ 2 ]);
	    } else {
	        if (Swip_maillon.aieul_( kbuf [ itab[ j][l1   ][ 2 ]-1],-itab[ j][l2][2 ]) != 0)
	                Swip_maillon.fils_ ( kbuf [itab[ j][l2][2 ] -1],itab[ j][l1   ][ 2 ]);
	    }
	}
	
	/****************************************************************************/
	
	public static void P_FFOF(int i,int j,Short edge,int [][][] itab, int l1, int l2, Chaine[] kbuf,int idebug)
	
	
	
	{
	    Chaine UneChaine;
	    int sipremier=0;
	
	    if (idebug!=0) System.out.println("Configuration P_FFOF Line "+i+" Col "+j+"");
	
	    if (itab[ j+1][l1 ][ 1 ] == 'P') itab[ j+1][l1 ][ 1 ] = 'D'; else itab[ j+1][l1 ][ 1 ] = 'F';
	    itab[ j][l2 ][ 2 ] = itab[ j-1][l2][ 2 ];
	    itab[ j][l2 ][ 1 ] = 'F';
	    Swip_maillon.nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
	    if (itab[ j-1][l2 ][ 1 ] == 'F') {
	        if (Swip_maillon.fils_( kbuf [itab[ j-1][l2 ][ 2 ] -1],-itab[ j][l2][2 ]) != 0)
	                Swip_maillon.fils_( kbuf [itab[ j][l2][2 ] -1],-itab[ j-1][l2 ][ 2 ]);
	    } else {
	        if (Swip_maillon.aieul_( kbuf [ itab[ j-1][l2 ][ 2 ]-1],-itab[ j][l2][2 ]) != 0)
	                Swip_maillon.fils_ ( kbuf [itab[ j][l2][2 ] -1],itab[ j-1][l2 ][ 2 ]);
	    }
	}
	
	/****************************************************************************/
	
	public static void P_FFFX(int i,int j,Short edge,int [][][] itab, int l1, int l2, Chaine[] kbuf,int idebug)
	
	
	
	{
	    Chaine UneChaine;
	    int sipremier=0;
	
	    if (idebug!=0) System.out.println("Configuration P_FFFX Line "+i+" Col "+j+"");
	
	    LeLinker.nb_chaines += 1;
	    if (LeLinker.nb_chaines >= LeLinker.nb_chaines_max) {
	        System.out.println("Too many chains\n");
	        System.exit(-1);
	    }
	    UneChaine = (Chaine) Swip_maillon.opench_();
	    if (UneChaine == null) {
	        System.out.println("There is no more space for chains.\n");
	        System.exit(-2);
	    }
	    kbuf[ LeLinker.nb_chaines - 1] = UneChaine ;
	    itab[ j][l2 ][ 2 ] = LeLinker.nb_chaines;
	    itab[ j][l2 ][ 1 ] = 'P'   ;
	    Swip_maillon.nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
	    if (itab[ j][l1   ][ 1 ] == 'D') {
	        if (Swip_maillon.aieul_( kbuf[  itab[ j][l2][2 ] -1],itab[ j][l1   ][ 2 ]) != 0)
	                Swip_maillon.aieul_( kbuf[itab[ j][l1   ][ 2 ] -1],itab[ j][l2][2 ]);
	    } else {
	        if (Swip_maillon.aieul_( kbuf[ itab[ j][l2][2 ] -1],-itab[ j][l1   ][ 2 ]) != 0)
	                Swip_maillon.fils_ ( kbuf[ itab[ j][l1   ][ 2 ] -1],itab[ j][l2][2 ]);
	    }
	}
	
	/****************************************************************************/
	
	public static void P_FFFF(int i,int j,Short edge,int [][][] itab, int l1, int l2, Chaine[] kbuf,int idebug)
	
	
	
	{
	    Chaine UneChaine;
	    int sipremier=0;
	
	    if (idebug!=0) System.out.println("Configuration P_FFFF Line "+i+" Col "+j+"");
	
	    itab[ j][l2 ][ 2 ] = itab[ j-1][l2][ 2 ];
	    itab[ j][l2 ][ 1 ] = 'F';
	    Swip_maillon.nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
	    if (itab[ j-1][l2 ][ 1 ] == 'F') {
	        if (Swip_maillon.fils_( kbuf [itab[ j-1][l2 ][ 2 ] -1],-itab[ j][l2][2 ]) != 0)
	                Swip_maillon.fils_( kbuf [itab[ j][l2][2 ] -1],-itab[ j-1][l2 ][ 2 ]);
	    } else {
	        if (Swip_maillon.aieul_( kbuf [ itab[ j-1][l2 ][ 2 ]-1],-itab[ j][l2][2 ]) != 0)
	                Swip_maillon.fils_ ( kbuf [itab[ j][l2][2 ] -1],itab[ j-1][l2 ][ 2 ]);
	    }
	}
	
	
	
	public static boolean ptest5668(int i,int j,Short edge){
	    int p5,p6,p8;
	    int jdimx,dimx;

	 
	    dimx = LeLinker.dim_x;
	    jdimx = j * LeLinker.dim_x;
	    edge = (short) (edge + i + jdimx);
	    p5 = edge + dimx;
	    p6 = edge + 1 - dimx;
	    p8 = edge + 1 + dimx;

	    return ( (p5 != 0 && p6 != 0) || (p6 != 0 && p8 != 0));
	}
	
	public static int ptest5(int i,int j,Short edge){
	    return edge + i + (j + 1) * LeLinker.dim_x;
	}
	
	public static void initab_(int l,int itab[][][]){
	    int k ;

	    for (k=0; k<=LeLinker.dim_y + 1; k++) itab[k][l][1] = 'X' ;	
	}
	
	public static void FSA(int i,int j,Short edge,int[][][] itab,int l1,int l2,Chaine[] kbuf,int idebug){
	    int ncov = 0 ; 
	    int ncfv = 0 ;
	    int poids[] = {1,8,4,2} ;
	    int k ,vitab;
	    int V;

	    /*--------- traitement de la ligne l1 -------------*/

	    for (k=(-1) ; k<=1; k++) {
	    	vitab=itab[j+k][l1][1];
	    	if (vitab <= 'F') ncfv += poids[k+1] ;
		else
	    	    if (vitab <= 'P') ncov += poids[k+1];
	    }

	    /*--------- Traitement de la ligne l2   ------------*/

	    vitab=itab[j-1][l2][1];
	    if (vitab <= 'F') ncfv += poids[3] ;
	    else
	        if (vitab <= 'P') ncov += poids[3] ;	

	    V=ncfv*10 + ncov;

	    Class[] cArg = new Class[8];
        
        cArg[0] = int.class;
        cArg[1] = int.class;
        cArg[2] = Short.class;
        cArg[3] = int[][][].class;
        cArg[4] = int.class;
        cArg[5] = int.class;
        cArg[6] = Chaine[].class;
        cArg[7] = int.class;
        
	    Method methode;
		try {
			methode = new Swip_ChaineExam().getClass().getMethod(gtotab[V], cArg);
			methode.invoke(new Swip_ChaineExam(), i,j,edge,itab,l1,l2,kbuf,idebug);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void initgtot_()
	{
	    gtotab[  0]="P_XXXX";
	    gtotab[  2]="P_XXXO";
	    gtotab[ 20]="P_XXXF";
	    gtotab[  4]="P_XXOX";
	    gtotab[  6]="P_XXOO";
	    gtotab[ 24]="P_XXOF";
	    gtotab[ 40]="P_XXFX";
	    gtotab[ 42]="P_XXFO";
	    gtotab[ 60]="P_XXFF";
	    gtotab[  8]="P_XOXX";
	    gtotab[ 80]="P_XFXX";
	    gtotab[ 82]="P_XFXO";
	    gtotab[100]="P_XFXF";
	    gtotab[ 84]="P_XFOX";
	    gtotab[ 86]="P_XFOO";
	    gtotab[104]="P_XFOF";
	    gtotab[120]="P_XFXX";
	    gtotab[122]="P_XFFO";
	    gtotab[140]="P_XFFF";
	    gtotab[  1]="P_OXXX";
	    gtotab[  5]="P_OXOX";
	    gtotab[ 41]="P_OXFX";
	    gtotab[ 10]="P_FXXX";
	    gtotab[ 12]="P_FXXO";
	    gtotab[ 30]="P_FXXF";
	    gtotab[ 14]="P_FXOX";
	    gtotab[ 16]="P_FXOO";
	    gtotab[ 34]="P_FXOF";
	    gtotab[ 50]="P_FXFX";
	    gtotab[ 52]="P_FXFO";
	    gtotab[ 70]="P_FXFF";
	    gtotab[ 18]="P_FOXX";
	    gtotab[ 90]="P_XFXX";
	    gtotab[ 92]="P_XFXO";
	    gtotab[110]="P_FFXF";
	    gtotab[ 94]="P_FFOX";
	    gtotab[ 96]="P_XFOO";
	    gtotab[114]="P_FFOF";
	    gtotab[130]="P_FFFX";
	    gtotab[132]="P_XFFO";
	    gtotab[150]="P_FFFF";
	}
	
}
