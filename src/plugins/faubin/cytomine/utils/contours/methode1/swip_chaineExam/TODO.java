package plugins.faubin.cytomine.utils.contours.methode1.swip_chaineExam;
///*****************************
//swip_ChaineExam.c
//
//Extraction et Chainage des points de contour
//
//* DESCRIPTION
//*     initgtot_,ptest5668,initab_,ptest5,FSA
//
//Author :  Giraudon, Copyright
//Date of creation : 03/05/1991
//Version : 1.0 
//Date of IP version : 06/04/2000
//******************************/
//
//
//#include "Maillon.h"
//
//#define VAL_MAX 150     /* Maximum value of configuration code.*/
//
//extern LinkerPar LeLinker;
//
//int (*(gtotab[VAL_MAX+1]))();
//
///****************************************************************************/
//
//P_XXXX(int i,int j, short *edge, int [][2][4] itab, int l1,int l2,Chaine[] kbuf,int idebug)
//{
//    Chaine UneChaine;
//    int sipremier=0;
//
//    if (idebug!=0) System.out.println("Configuration P_XXXX Line %3d Col %3d\n",i,j);
//
//    LeLinker->nb_chaines += 1;
//    if (LeLinker->nb_chaines >= LeLinker->nb_chaines_max) {
//        printf("Too many chains\n");
//        exit(-1);
//    }
//    UneChaine = (Chaine) opench_();
//    if (UneChaine == NULL) {
//        printf("There is no more space for chains.\n");
//        exit(-2);
//    }
//    kbuf[ LeLinker->nb_chaines - 1] = UneChaine ;
//    itab[ j][l2 ][ 2 ] = LeLinker->nb_chaines;
//    itab[ j][l2 ][ 1 ] = 'P'   ;
//    nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
//}
//
///****************************************************************************/
//
//P_XXXO(i,j,edge,itab,l1,l2,kbuf,idebug)
//int i,j,itab[][2][4],l1,l2,idebug;
//short *edge;
//Chaine kbuf[];
//{
//    Chaine UneChaine;
//    int sipremier=0;
//
//    if (idebug!=0) printf("Configuration P_XXXO Line %3d Col %3d\n",i,j);
//
//    itab[ j][l2 ][ 2 ] = itab[ j-1][l2 ][ 2 ];
//    itab[ j][l2 ][ 1 ] = 'O' ;
//    if (itab[ j-1][l2 ][ 1 ] == 'P') {
//        itab[ j-1][l2 ][ 1 ] = 'D';
//        sipremier = 1;
//    } else itab[ j-1][l2 ][ 1 ] = 'X';
//    nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
//    if (!(sipremier))
//        if ( ptest5668(i,j,edge) ) {
//            itab[ j][l2 ][ 1 ] = 'F';
//        }
//}
//
///****************************************************************************/
//
//P_XXXF(i,j,edge,itab,l1,l2,kbuf,idebug)
//int i,j,itab[][2][4],l1,l2,idebug;
//short *edge;
//Chaine kbuf[];
//{
//    Chaine UneChaine;
//    int sipremier=0;
//
//    if (idebug!=0) printf("Configuration P_XXXF Line %3d Col %3d\n",i,j);
//
//    LeLinker->nb_chaines += 1;
//    if (LeLinker->nb_chaines >= LeLinker->nb_chaines_max) {
//        printf("Too many chains\n");
//        exit(-1);
//    }
//    UneChaine = (Chaine) opench_();
//    if (UneChaine == NULL) {
//        printf("There is no more space for chains.\n");
//        exit(-2);
//    }
//    kbuf[ LeLinker->nb_chaines - 1] = UneChaine ;
//    itab[ j][l2 ][ 2 ] = LeLinker->nb_chaines;
//    itab[ j][l2 ][ 1 ] = 'P'   ;
//    nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
//    if (itab[ j-1][l2 ][ 1 ] == 'D') {
//        if (aieul_( kbuf[  itab[ j][l2][2 ] -1],itab[ j-1][l2 ][ 2 ]))
//                aieul_( kbuf[itab[ j-1][l2 ][ 2 ] -1],itab[ j][l2][2 ]);
//    } else {
//        if (aieul_( kbuf[ itab[ j][l2][2 ] -1],-itab[ j-1][l2 ][ 2 ]))
//                fils_ ( kbuf[ itab[ j-1][l2 ][ 2 ] -1],itab[ j][l2][2 ]);
//    }
//}
//
///****************************************************************************/
//
//P_XXOX(i,j,edge,itab,l1,l2,kbuf,idebug)
//int i,j,itab[][2][4],l1,l2,idebug;
//short *edge;
//Chaine kbuf[];
//{
//    Chaine UneChaine;
//    int sipremier=0;
//
//    if (idebug!=0) printf("Configuration P_XXOX Line %3d Col %3d\n",i,j);
//
//    itab[ j][l2 ][ 2 ] = itab[ j+1][l1 ][ 2 ];
//    itab[ j][l2 ][ 1 ] = 'O' ;
//    if (itab[ j+1][l1 ][ 1 ] == 'P') {
//        itab[ j+1][l1 ][ 1 ] = 'D';
//        sipremier = 1;
//    } else itab[ j+1][l1 ][ 1 ] = 'X';
//    nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
//    if ( ptest5668(i,j,edge) ) {
//        itab[ j][l2 ][ 1 ] = 'F';
//    }
//}
//
///****************************************************************************/
//
//P_XXOO(i,j,edge,itab,l1,l2,kbuf,idebug)
//int i,j,itab[][2][4],l1,l2,idebug;
//short *edge;
//Chaine kbuf[];
//{
//    Chaine UneChaine;
//    int sipremier=0;
//
//    if (idebug!=0) printf("Configuration P_XXOO Line %3d Col %3d\n",i,j);
//
//    itab[ j][l2 ][ 2 ] = itab[ j-1][l2 ][ 2 ];
//    itab[ j][l2 ][ 1 ] = 'O' ;
//    if (itab[ j-1][l2 ][ 1 ] == 'P') {
//        itab[ j-1][l2 ][ 1 ] = 'D';
//        sipremier = 1;
//    } else itab[ j-1][l2 ][ 1 ] = 'X';
//    nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
//    if (itab[ j+1][l1 ][ 1 ] == 'P') itab[ j+1][l1 ][ 1 ] = 'D'; else itab[ j+1][l1 ][ 1 ] = 'F';
//    itab[ j][l2 ][ 1 ] = 'F';
//    if (!ptest5(i,j,edge)) {
//        if (itab[ j+1][l1 ][ 1 ] == 'F') {
//            if (fils_( kbuf [itab[ j+1][l1 ][ 2 ] -1],-itab[ j][l2][2 ]))
//                    fils_( kbuf [itab[ j][l2][2 ] -1],-itab[ j+1][l1 ][ 2 ]);
//        } else {
//            if (aieul_( kbuf [ itab[ j+1][l1 ][ 2 ]-1],-itab[ j][l2][2 ]))
//                    fils_ ( kbuf [itab[ j][l2][2 ] -1],itab[ j+1][l1 ][ 2 ]);
//        }
//    }
//}
//
///****************************************************************************/
//
//P_XXOF(i,j,edge,itab,l1,l2,kbuf,idebug)
//int i,j,itab[][2][4],l1,l2,idebug;
//short *edge;
//Chaine kbuf[];
//{
//    Chaine UneChaine;
//    int sipremier=0;
//
//    if (idebug!=0) printf("Configuration P_XXOF Line %3d Col %3d\n",i,j);
//
//    itab[ j][l2 ][ 2 ] = itab[ j+1][l1 ][ 2 ];
//    itab[ j][l2 ][ 1 ] = 'O' ;
//    if (itab[ j+1][l1 ][ 1 ] == 'P') {
//        itab[ j+1][l1 ][ 1 ] = 'D';
//        sipremier = 1;
//    } else itab[ j+1][l1 ][ 1 ] = 'X';
//    nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
//    itab[ j][l2 ][ 1 ] = 'F';
//    if (itab[ j-1][l2 ][ 1 ] == 'F') {
//        if (fils_( kbuf [itab[ j-1][l2 ][ 2 ] -1],-itab[ j][l2][2 ]))
//                fils_( kbuf [itab[ j][l2][2 ] -1],-itab[ j-1][l2 ][ 2 ]);
//    } else {
//        if (aieul_( kbuf [ itab[ j-1][l2 ][ 2 ]-1],-itab[ j][l2][2 ]))
//                fils_ ( kbuf [itab[ j][l2][2 ] -1],itab[ j-1][l2 ][ 2 ]);
//    }
//}
//
///****************************************************************************/
//
//P_XXFX(i,j,edge,itab,l1,l2,kbuf,idebug)
//int i,j,itab[][2][4],l1,l2,idebug;
//short *edge;
//Chaine kbuf[];
//{
//    Chaine UneChaine;
//    int sipremier=0;
//
//    if (idebug!=0) printf("Configuration P_XXFX Line %3d Col %3d\n",i,j);
//
//    LeLinker->nb_chaines += 1;
//    if (LeLinker->nb_chaines >= LeLinker->nb_chaines_max) {
//        printf("Too many chains\n");
//        exit(-1);
//    }
//    UneChaine = (Chaine) opench_();
//    if (UneChaine == NULL) {
//        printf("There is no more space for chains.\n");
//        exit(-2);
//    }
//    kbuf[ LeLinker->nb_chaines - 1] = UneChaine ;
//    itab[ j][l2 ][ 2 ] = LeLinker->nb_chaines;
//    itab[ j][l2 ][ 1 ] = 'P'   ;
//    nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
//    if (!ptest5(i,j,edge)) {
//        if (itab[ j+1][l1 ][ 1 ] == 'D') {
//            if (aieul_( kbuf[  itab[ j][l2][2 ] -1],itab[ j+1][l1 ][ 2 ]))
//                    aieul_( kbuf[itab[ j+1][l1 ][ 2 ] -1],itab[ j][l2][2 ]);
//        } else {
//            if (aieul_( kbuf[ itab[ j][l2][2 ] -1],-itab[ j+1][l1 ][ 2 ]))
//                    fils_ ( kbuf[ itab[ j+1][l1 ][ 2 ] -1],itab[ j][l2][2 ]);
//        }
//    }
//}
//
///****************************************************************************/
//
//P_XXFO(i,j,edge,itab,l1,l2,kbuf,idebug)
//int i,j,itab[][2][4],l1,l2,idebug;
//short *edge;
//Chaine kbuf[];
//{
//    Chaine UneChaine;
//    int sipremier=0;
//
//    if (idebug!=0) printf("Configuration P_XXFO Line %3d Col %3d\n",i,j);
//
//    itab[ j][l2 ][ 2 ] = itab[ j-1][l2 ][ 2 ];
//    itab[ j][l2 ][ 1 ] = 'O' ;
//    if (itab[ j-1][l2 ][ 1 ] == 'P') {
//        itab[ j-1][l2 ][ 1 ] = 'D';
//        sipremier = 1;
//    } else itab[ j-1][l2 ][ 1 ] = 'X';
//    nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
//    itab[ j][l2 ][ 1 ] = 'F';
//    if (!ptest5(i,j,edge)) {
//        if (itab[ j+1][l1 ][ 1 ] == 'F') {
//            if (fils_( kbuf [itab[ j+1][l1 ][ 2 ] -1],-itab[ j][l2][2 ]))
//                    fils_( kbuf [itab[ j][l2][2 ] -1],-itab[ j+1][l1 ][ 2 ]);
//        } else {
//            if (aieul_( kbuf [ itab[ j+1][l1 ][ 2 ]-1],-itab[ j][l2][2 ]))
//                    fils_ ( kbuf [itab[ j][l2][2 ] -1],itab[ j+1][l1 ][ 2 ]);
//        }
//    }
//}
//
///****************************************************************************/
//
//P_XXFF(i,j,edge,itab,l1,l2,kbuf,idebug)
//int i,j,itab[][2][4],l1,l2,idebug;
//short *edge;
//Chaine kbuf[];
//{
//    Chaine UneChaine;
//    int sipremier=0;
//
//    if (idebug!=0) printf("Configuration P_XXFF Line %3d Col %3d\n",i,j);
//
//    LeLinker->nb_chaines += 1;
//    if (LeLinker->nb_chaines >= LeLinker->nb_chaines_max) {
//        printf("Too many chains\n");
//        exit(-1);
//    }
//    UneChaine = (Chaine) opench_();
//    if (UneChaine == NULL) {
//        printf("There is no more space for chains.\n");
//        exit(-2);
//    }
//    kbuf[ LeLinker->nb_chaines - 1] = UneChaine ;
//    itab[ j][l2 ][ 2 ] = LeLinker->nb_chaines;
//    itab[ j][l2 ][ 1 ] = 'P'   ;
//    nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
//    if (!ptest5(i,j,edge)) {
//        if (itab[ j+1][l1 ][ 1 ] == 'D') {
//            if (aieul_( kbuf[  itab[ j][l2][2 ] -1],itab[ j+1][l1 ][ 2 ]))
//                    aieul_( kbuf[itab[ j+1][l1 ][ 2 ] -1],itab[ j][l2][2 ]);
//        } else {
//            if (aieul_( kbuf[ itab[ j][l2][2 ] -1],-itab[ j+1][l1 ][ 2 ]))
//                    fils_ ( kbuf[ itab[ j+1][l1 ][ 2 ] -1],itab[ j][l2][2 ]);
//        }
//    }
//    if (itab[ j-1][l2 ][ 1 ] == 'D') {
//        if (aieul_( kbuf[  itab[ j][l2][2 ] -1],itab[ j-1][l2 ][ 2 ]))
//                aieul_( kbuf[itab[ j-1][l2 ][ 2 ] -1],itab[ j][l2][2 ]);
//    } else {
//        if (aieul_( kbuf[ itab[ j][l2][2 ] -1],-itab[ j-1][l2 ][ 2 ]))
//                fils_ ( kbuf[ itab[ j-1][l2 ][ 2 ] -1],itab[ j][l2][2 ]);
//    }
//}
//
///****************************************************************************/
//
//P_XOXX(i,j,edge,itab,l1,l2,kbuf,idebug)
//int i,j,itab[][2][4],l1,l2,idebug;
//short *edge;
//Chaine kbuf[];
//{
//    Chaine UneChaine;
//    int sipremier=0;
//
//    if (idebug!=0) printf("Configuration P_XOXX Line %3d Col %3d\n",i,j);
//
//    itab[ j][l2 ][ 2 ] = itab[ j][l1   ][ 2 ];
//    itab[ j][l2 ][ 1 ] = 'O' ;
//    if (itab[ j][l1   ][ 1 ] == 'P') {
//        itab[ j][l1   ][ 1 ] = 'D';
//        sipremier = 1;
//    } else itab[ j][l1   ][ 1 ] = 'X';
//    nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
//    if ( ptest5668(i,j,edge) ) {
//        itab[ j][l2 ][ 1 ] = 'F';
//    }
//}
//
///****************************************************************************/
//
//P_XFXX(i,j,edge,itab,l1,l2,kbuf,idebug)
//int i,j,itab[][2][4],l1,l2,idebug;
//short *edge;
//Chaine kbuf[];
//{
//    Chaine UneChaine;
//    int sipremier=0;
//
//    if (idebug!=0) printf("Configuration P_XFXX Line %3d Col %3d\n",i,j);
//
//    LeLinker->nb_chaines += 1;
//    if (LeLinker->nb_chaines >= LeLinker->nb_chaines_max) {
//        printf("Too many chains\n");
//        exit(-1);
//    }
//    UneChaine = (Chaine) opench_();
//    if (UneChaine == NULL) {
//        printf("There is no more space for chains.\n");
//        exit(-2);
//    }
//    kbuf[ LeLinker->nb_chaines - 1] = UneChaine ;
//    itab[ j][l2 ][ 2 ] = LeLinker->nb_chaines;
//    itab[ j][l2 ][ 1 ] = 'P'   ;
//    nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
//    if (itab[ j][l1   ][ 1 ] == 'D') {
//        if (aieul_( kbuf[  itab[ j][l2][2 ] -1],itab[ j][l1   ][ 2 ]))
//                aieul_( kbuf[itab[ j][l1   ][ 2 ] -1],itab[ j][l2][2 ]);
//    } else {
//        if (aieul_( kbuf[ itab[ j][l2][2 ] -1],-itab[ j][l1   ][ 2 ]))
//                fils_ ( kbuf[ itab[ j][l1   ][ 2 ] -1],itab[ j][l2][2 ]);
//    }
//}
//
///****************************************************************************/
//
//P_XFXO(i,j,edge,itab,l1,l2,kbuf,idebug)
//int i,j,itab[][2][4],l1,l2,idebug;
//short *edge;
//Chaine kbuf[];
//{
//    Chaine UneChaine;
//    int sipremier=0;
//
//    if (idebug!=0) printf("Configuration P_XFXO Line %3d Col %3d\n",i,j);
//
//    itab[ j][l2 ][ 2 ] = itab[ j-1][l2 ][ 2 ];
//    itab[ j][l2 ][ 1 ] = 'O' ;
//    if (itab[ j-1][l2 ][ 1 ] == 'P') {
//        itab[ j-1][l2 ][ 1 ] = 'D';
//        sipremier = 1;
//    } else itab[ j-1][l2 ][ 1 ] = 'X';
//    itab[ j][l2 ][ 1 ] = 'F';
//    nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
//    if (itab[ j][l1   ][ 1 ] == 'F') {
//        if (fils_( kbuf [itab[ j][l1   ][ 2 ] -1],-itab[ j][l2][2 ]))
//                fils_( kbuf [itab[ j][l2][2 ] -1],-itab[ j][l1   ][ 2 ]);
//    } else {
//        if (aieul_( kbuf [ itab[ j][l1   ][ 2 ]-1],-itab[ j][l2][2 ]))
//                fils_ ( kbuf [itab[ j][l2][2 ] -1],itab[ j][l1   ][ 2 ]);
//    }
//}
//
///****************************************************************************/
//
//P_XFXF(i,j,edge,itab,l1,l2,kbuf,idebug)
//int i,j,itab[][2][4],l1,l2,idebug;
//short *edge;
//Chaine kbuf[];
//{
//    Chaine UneChaine;
//    int sipremier=0;
//
//    if (idebug!=0) printf("Configuration P_XFXF Line %3d Col %3d\n",i,j);
//
//    itab[ j][l2 ][ 2 ] = itab[ j-1][l2][ 2 ];
//    itab[ j][l2 ][ 1 ] = 'F';
//    nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
//    if (itab[ j][l1   ][ 1 ] == 'F') {
//        if (fils_( kbuf [itab[ j][l1   ][ 2 ] -1],-itab[ j][l2][2 ]))
//                fils_( kbuf [itab[ j][l2][2 ] -1],-itab[ j][l1   ][ 2 ]);
//    } else {
//        if (aieul_( kbuf [ itab[ j][l1   ][ 2 ]-1],-itab[ j][l2][2 ]))
//                fils_ ( kbuf [itab[ j][l2][2 ] -1],itab[ j][l1   ][ 2 ]);
//    }
//    if (itab[ j-1][l2 ][ 1 ] == 'F') {
//        if (fils_( kbuf [itab[ j-1][l2 ][ 2 ] -1],-itab[ j][l2][2 ]))
//                fils_( kbuf [itab[ j][l2][2 ] -1],-itab[ j-1][l2 ][ 2 ]);
//    } else {
//        if (aieul_( kbuf [ itab[ j-1][l2 ][ 2 ]-1],-itab[ j][l2][2 ]))
//                fils_ ( kbuf [itab[ j][l2][2 ] -1],itab[ j-1][l2 ][ 2 ]);
//    }
//}
//
///****************************************************************************/
//
//P_XFOX(i,j,edge,itab,l1,l2,kbuf,idebug)
//int i,j,itab[][2][4],l1,l2,idebug;
//short *edge;
//Chaine kbuf[];
//{
//    Chaine UneChaine;
//    int sipremier=0;
//
//    if (idebug!=0) printf("Configuration P_XFOX Line %3d Col %3d\n",i,j);
//
//    itab[ j][l2 ][ 2 ] = itab[ j+1][l1 ][ 2 ];
//    itab[ j][l2 ][ 1 ] = 'O' ;
//    if (itab[ j+1][l1 ][ 1 ] == 'P') {
//        itab[ j+1][l1 ][ 1 ] = 'D';
//        sipremier = 1;
//    } else itab[ j+1][l1 ][ 1 ] = 'X';
//    nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
//    if ( ptest5668(i,j,edge) ) {
//        itab[ j][l2 ][ 1 ] = 'F';
//        if (itab[ j][l1   ][ 1 ] == 'F') {
//            if (fils_( kbuf [itab[ j][l1   ][ 2 ] -1],-itab[ j][l2][2 ]))
//                    fils_( kbuf [itab[ j][l2][2 ] -1],-itab[ j][l1   ][ 2 ]);
//        } else {
//            if (aieul_( kbuf [ itab[ j][l1   ][ 2 ]-1],-itab[ j][l2][2 ]))
//                    fils_ ( kbuf [itab[ j][l2][2 ] -1],itab[ j][l1   ][ 2 ]);
//        }
//    }
//}
//
///****************************************************************************/
//
//P_XFOO(i,j,edge,itab,l1,l2,kbuf,idebug)
//int i,j,itab[][2][4],l1,l2,idebug;
//short *edge;
//Chaine kbuf[];
//{
//    Chaine UneChaine;
//    int sipremier=0;
//
//    if (idebug!=0) printf("Configuration P_XFOO Line %3d Col %3d\n",i,j);
//
//    itab[ j][l2 ][ 2 ] = itab[ j-1][l2 ][ 2 ];
//    itab[ j][l2 ][ 1 ] = 'O' ;
//    if (itab[ j-1][l2 ][ 1 ] == 'P') {
//        itab[ j-1][l2 ][ 1 ] = 'D';
//        sipremier = 1;
//    } else itab[ j-1][l2 ][ 1 ] = 'X';
//    nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
//    if (itab[ j+1][l1 ][ 1 ] == 'P') itab[ j+1][l1 ][ 1 ] = 'D'; else itab[ j+1][l1 ][ 1 ] = 'F';
//    itab[ j][l2 ][ 1 ] = 'F';
//    if (itab[ j][l1   ][ 1 ] == 'F') {
//        if (fils_( kbuf [itab[ j][l1   ][ 2 ] -1],-itab[ j][l2][2 ]))
//                fils_( kbuf [itab[ j][l2][2 ] -1],-itab[ j][l1   ][ 2 ]);
//    } else {
//        if (aieul_( kbuf [ itab[ j][l1   ][ 2 ]-1],-itab[ j][l2][2 ]))
//                fils_ ( kbuf [itab[ j][l2][2 ] -1],itab[ j][l1   ][ 2 ]);
//    }
//}
//
///****************************************************************************/
//
//P_XFOF(i,j,edge,itab,l1,l2,kbuf,idebug)
//int i,j,itab[][2][4],l1,l2,idebug;
//short *edge;
//Chaine kbuf[];
//{
//    Chaine UneChaine;
//    int sipremier=0;
//
//    if (idebug!=0) printf("Configuration P_XFOF Line %3d Col %3d\n",i,j);
//
//    if (itab[ j+1][l1 ][ 1 ] == 'P') itab[ j+1][l1 ][ 1 ] = 'D'; else itab[ j+1][l1 ][ 1 ] = 'F';
//    itab[ j][l2 ][ 2 ] = itab[ j-1][l2][ 2 ];
//    itab[ j][l2 ][ 1 ] = 'F';
//    nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
//    if (itab[ j][l1   ][ 1 ] == 'F') {
//        if (fils_( kbuf [itab[ j][l1   ][ 2 ] -1],-itab[ j][l2][2 ]))
//                fils_( kbuf [itab[ j][l2][2 ] -1],-itab[ j][l1   ][ 2 ]);
//    } else {
//        if (aieul_( kbuf [ itab[ j][l1   ][ 2 ]-1],-itab[ j][l2][2 ]))
//                fils_ ( kbuf [itab[ j][l2][2 ] -1],itab[ j][l1   ][ 2 ]);
//    }
//    if (itab[ j-1][l2 ][ 1 ] == 'F') {
//        if (fils_( kbuf [itab[ j-1][l2 ][ 2 ] -1],-itab[ j][l2][2 ]))
//                fils_( kbuf [itab[ j][l2][2 ] -1],-itab[ j-1][l2 ][ 2 ]);
//    } else {
//        if (aieul_( kbuf [ itab[ j-1][l2 ][ 2 ]-1],-itab[ j][l2][2 ]))
//                fils_ ( kbuf [itab[ j][l2][2 ] -1],itab[ j-1][l2 ][ 2 ]);
//    }
//}
//
///****************************************************************************/
//
//P_XFFO(i,j,edge,itab,l1,l2,kbuf,idebug)
//int i,j,itab[][2][4],l1,l2,idebug;
//short *edge;
//Chaine kbuf[];
//{
//    Chaine UneChaine;
//    int sipremier=0;
//
//    if (idebug!=0) printf("Configuration P_XFFO Line %3d Col %3d\n",i,j);
//
//    itab[ j][l2 ][ 2 ] = itab[ j-1][l2 ][ 2 ];
//    itab[ j][l2 ][ 1 ] = 'O' ;
//    if (itab[ j-1][l2 ][ 1 ] == 'P') {
//        itab[ j-1][l2 ][ 1 ] = 'D';
//        sipremier = 1;
//    } else itab[ j-1][l2 ][ 1 ] = 'X';
//    itab[ j][l2 ][ 1 ] = 'F';
//    nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
//    if (itab[ j][l1   ][ 1 ] == 'F') {
//        if (fils_( kbuf [itab[ j][l1   ][ 2 ] -1],-itab[ j][l2][2 ]))
//                fils_( kbuf [itab[ j][l2][2 ] -1],-itab[ j][l1   ][ 2 ]);
//    } else {
//        if (aieul_( kbuf [ itab[ j][l1   ][ 2 ]-1],-itab[ j][l2][2 ]))
//                fils_ ( kbuf [itab[ j][l2][2 ] -1],itab[ j][l1   ][ 2 ]);
//    }
//}
//
///****************************************************************************/
//
//P_XFFF(i,j,edge,itab,l1,l2,kbuf,idebug)
//int i,j,itab[][2][4],l1,l2,idebug;
//short *edge;
//Chaine kbuf[];
//{
//    Chaine UneChaine;
//    int sipremier=0;
//
//    if (idebug!=0) printf("Configuration P_XFFF Line %3d Col %3d\n",i,j);
//
//    itab[ j][l2 ][ 2 ] = itab[ j-1][l2][ 2 ];
//    itab[ j][l2 ][ 1 ] = 'F';
//    nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
//    if (itab[ j][l1   ][ 1 ] == 'F') {
//        if (fils_( kbuf [itab[ j][l1   ][ 2 ] -1],-itab[ j][l2][2 ]))
//                fils_( kbuf [itab[ j][l2][2 ] -1],-itab[ j][l1   ][ 2 ]);
//    } else {
//        if (aieul_( kbuf [ itab[ j][l1   ][ 2 ]-1],-itab[ j][l2][2 ]))
//                fils_ ( kbuf [itab[ j][l2][2 ] -1],itab[ j][l1   ][ 2 ]);
//    }
//    if (itab[ j-1][l2 ][ 1 ] == 'F') {
//        if (fils_( kbuf [itab[ j-1][l2 ][ 2 ] -1],-itab[ j][l2][2 ]))
//                fils_( kbuf [itab[ j][l2][2 ] -1],-itab[ j-1][l2 ][ 2 ]);
//    } else {
//        if (aieul_( kbuf [ itab[ j-1][l2 ][ 2 ]-1],-itab[ j][l2][2 ]))
//                fils_ ( kbuf [itab[ j][l2][2 ] -1],itab[ j-1][l2 ][ 2 ]);
//    }
//}
//
///****************************************************************************/
//
//P_OXXX(i,j,edge,itab,l1,l2,kbuf,idebug)
//int i,j,itab[][2][4],l1,l2,idebug;
//short *edge;
//Chaine kbuf[];
//{
//    Chaine UneChaine;
//    int sipremier=0;
//
//    if (idebug!=0) printf("Configuration P_OXXX Line %3d Col %3d\n",i,j);
//
//    itab[ j][l2 ][ 2 ] = itab[ j-1][l1 ][ 2 ];
//    itab[ j][l2 ][ 1 ] = 'O' ;
//    if (itab[ j-1][l1 ][ 1 ] == 'P') {
//        itab[ j-1][l1 ][ 1 ] = 'D';
//        sipremier = 1;
//    } else itab[ j-1][l1 ][ 1 ] = 'X';
//    nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
//    if ( ptest5668(i,j,edge) ) {
//        itab[ j][l2 ][ 1 ] = 'F';
//    }
//}
//
///****************************************************************************/
//
//P_OXOX(i,j,edge,itab,l1,l2,kbuf,idebug)
//int i,j,itab[][2][4],l1,l2,idebug;
//short *edge;
//Chaine kbuf[];
//{
//    Chaine UneChaine;
//    int sipremier=0;
//
//    if (idebug!=0) printf("Configuration P_OXOX Line %3d Col %3d\n",i,j);
//
//    itab[ j][l2 ][ 2 ] = itab[ j-1][l1 ][ 2 ];
//    itab[ j][l2 ][ 1 ] = 'O' ;
//    if (itab[ j-1][l1 ][ 1 ] == 'P') {
//        itab[ j-1][l1 ][ 1 ] = 'D';
//        sipremier = 1;
//    } else itab[ j-1][l1 ][ 1 ] = 'X';
//    nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
//    if (itab[ j+1][l1 ][ 1 ] == 'P') itab[ j+1][l1 ][ 1 ] = 'D'; else itab[ j+1][l1 ][ 1 ] = 'F';
//    itab[ j][l2 ][ 1 ] = 'F';
//    if (itab[j-1][l1][2] != itab[j+1][l1][2])
//        if (!ptest5(i,j,edge)) {
//            if (itab[ j+1][l1 ][ 1 ] == 'F') {
//                if (fils_( kbuf [itab[ j+1][l1 ][ 2 ] -1],-itab[ j][l2][2 ]))
//                        fils_( kbuf [itab[ j][l2][2 ] -1],-itab[ j+1][l1 ][ 2 ]);
//            } else {
//                if (aieul_( kbuf [ itab[ j+1][l1 ][ 2 ]-1],-itab[ j][l2][2 ]))
//                        fils_ ( kbuf [itab[ j][l2][2 ] -1],itab[ j+1][l1 ][ 2 ]);
//            }
//    }
//}
//
///****************************************************************************/
//
//P_OXFX(i,j,edge,itab,l1,l2,kbuf,idebug)
//int i,j,itab[][2][4],l1,l2,idebug;
//short *edge;
//Chaine kbuf[];
//{
//    Chaine UneChaine;
//    int sipremier=0;
//
//    if (idebug!=0) printf("Configuration P_OXFX Line %3d Col %3d\n",i,j);
//
//    itab[ j][l2 ][ 2 ] = itab[ j-1][l1 ][ 2 ];
//    itab[ j][l2 ][ 1 ] = 'O' ;
//    if (itab[ j-1][l1 ][ 1 ] == 'P') {
//        itab[ j-1][l1 ][ 1 ] = 'D';
//        sipremier = 1;
//    } else itab[ j-1][l1 ][ 1 ] = 'X';
//    nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
//    itab[ j][l2 ][ 1 ] = 'F';
//    if (itab[j-1][l1][2] != itab[j+1][l1][2])
//        if (!ptest5(i,j,edge)) {
//            if (itab[ j+1][l1 ][ 1 ] == 'F') {
//                if (fils_( kbuf [itab[ j+1][l1 ][ 2 ] -1],-itab[ j][l2][2 ]))
//                        fils_( kbuf [itab[ j][l2][2 ] -1],-itab[ j+1][l1 ][ 2 ]);
//            } else {
//                if (aieul_( kbuf [ itab[ j+1][l1 ][ 2 ]-1],-itab[ j][l2][2 ]))
//                        fils_ ( kbuf [itab[ j][l2][2 ] -1],itab[ j+1][l1 ][ 2 ]);
//            }
//    }
//}
//
///****************************************************************************/
//
//P_FXXX(i,j,edge,itab,l1,l2,kbuf,idebug)
//int i,j,itab[][2][4],l1,l2,idebug;
//short *edge;
//Chaine kbuf[];
//{
//    Chaine UneChaine;
//    int sipremier=0;
//
//    if (idebug!=0) printf("Configuration P_FXXX Line %3d Col %3d\n",i,j);
//
//    LeLinker->nb_chaines += 1;
//    if (LeLinker->nb_chaines >= LeLinker->nb_chaines_max) {
//        printf("Too many chains\n");
//        exit(-1);
//    }
//    UneChaine = (Chaine) opench_();
//    if (UneChaine == NULL) {
//        printf("There is no more space for chains.\n");
//        exit(-2);
//    }
//    kbuf[ LeLinker->nb_chaines - 1] = UneChaine ;
//    itab[ j][l2 ][ 2 ] = LeLinker->nb_chaines;
//    itab[ j][l2 ][ 1 ] = 'P'   ;
//    nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
//    if (itab[ j-1][l1 ][ 1 ] == 'D') {
//        if (aieul_( kbuf[  itab[ j][l2][2 ] -1],itab[ j-1][l1 ][ 2 ]))
//                aieul_( kbuf[itab[ j-1][l1 ][ 2 ] -1],itab[ j][l2][2 ]);
//    } else {
//        if (aieul_( kbuf[ itab[ j][l2][2 ] -1],-itab[ j-1][l1 ][ 2 ]))
//                fils_ ( kbuf[ itab[ j-1][l1 ][ 2 ] -1],itab[ j][l2][2 ]);
//    }
//}
//
///****************************************************************************/
//
//P_FXXO(i,j,edge,itab,l1,l2,kbuf,idebug)
//int i,j,itab[][2][4],l1,l2,idebug;
//short *edge;
//Chaine kbuf[];
//{
//    Chaine UneChaine;
//    int sipremier=0;
//
//    if (idebug!=0) printf("Configuration P_FXXO Line %3d Col %3d\n",i,j);
//
//    itab[ j][l2 ][ 2 ] = itab[ j-1][l2 ][ 2 ];
//    itab[ j][l2 ][ 1 ] = 'O' ;
//    if (itab[ j-1][l2 ][ 1 ] == 'P') {
//        itab[ j-1][l2 ][ 1 ] = 'D';
//        sipremier = 1;
//    } else itab[ j-1][l2 ][ 1 ] = 'X';
//    nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
//    if (!(sipremier))
//        if ( ptest5668(i,j,edge) ) {
//            itab[ j][l2 ][ 1 ] = 'F';
//            if (itab[ j-1][l1 ][ 1 ] == 'F') {
//                if (fils_( kbuf [itab[ j-1][l1 ][ 2 ] -1],-itab[ j][l2][2 ]))
//                        fils_( kbuf [itab[ j][l2][2 ] -1],-itab[ j-1][l1 ][ 2 ]);
//            } else {
//                if (aieul_( kbuf [ itab[ j-1][l1 ][ 2 ]-1],-itab[ j][l2][2 ]))
//                        fils_ ( kbuf [itab[ j][l2][2 ] -1],itab[ j-1][l1 ][ 2 ]);
//            }
//        }
//}
//
///****************************************************************************/
//
//P_FXXF(i,j,edge,itab,l1,l2,kbuf,idebug)
//int i,j,itab[][2][4],l1,l2,idebug;
//short *edge;
//Chaine kbuf[];
//{
//    Chaine UneChaine;
//    int sipremier=0;
//
//    if (idebug!=0) printf("Configuration P_FXXF Line %3d Col %3d\n",i,j);
//
//    LeLinker->nb_chaines += 1;
//    if (LeLinker->nb_chaines >= LeLinker->nb_chaines_max) {
//        printf("Too many chains\n");
//        exit(-1);
//    }
//    UneChaine = (Chaine) opench_();
//    if (UneChaine == NULL) {
//        printf("There is no more space for chains.\n");
//        exit(-2);
//    }
//    kbuf[ LeLinker->nb_chaines - 1] = UneChaine ;
//    itab[ j][l2 ][ 2 ] = LeLinker->nb_chaines;
//    itab[ j][l2 ][ 1 ] = 'P'   ;
//    nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
//    if (itab[ j-1][l2 ][ 1 ] == 'D') {
//        if (aieul_( kbuf[  itab[ j][l2][2 ] -1],itab[ j-1][l2 ][ 2 ]))
//                aieul_( kbuf[itab[ j-1][l2 ][ 2 ] -1],itab[ j][l2][2 ]);
//    } else {
//        if (aieul_( kbuf[ itab[ j][l2][2 ] -1],-itab[ j-1][l2 ][ 2 ]))
//                fils_ ( kbuf[ itab[ j-1][l2 ][ 2 ] -1],itab[ j][l2][2 ]);
//    }
//}
//
///****************************************************************************/
//
//P_FXOX(i,j,edge,itab,l1,l2,kbuf,idebug)
//int i,j,itab[][2][4],l1,l2,idebug;
//short *edge;
//Chaine kbuf[];
//{
//    Chaine UneChaine;
//    int sipremier=0;
//
//    if (idebug!=0) printf("Configuration P_FXOX Line %3d Col %3d\n",i,j);
//
//    itab[ j][l2 ][ 2 ] = itab[ j+1][l1 ][ 2 ];
//    itab[ j][l2 ][ 1 ] = 'O' ;
//    if (itab[ j+1][l1 ][ 1 ] == 'P') {
//        itab[ j+1][l1 ][ 1 ] = 'D';
//        sipremier = 1;
//    } else itab[ j+1][l1 ][ 1 ] = 'X';
//    nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
//    itab[ j][l2 ][ 1 ] = 'F';
//    if (itab[ j-1][l1 ][ 1 ] == 'F') {
//        if (fils_( kbuf [itab[ j-1][l1 ][ 2 ] -1],-itab[ j][l2][2 ]))
//                fils_( kbuf [itab[ j][l2][2 ] -1],-itab[ j-1][l1 ][ 2 ]);
//    } else {
//        if (aieul_( kbuf [ itab[ j-1][l1 ][ 2 ]-1],-itab[ j][l2][2 ]))
//                fils_ ( kbuf [itab[ j][l2][2 ] -1],itab[ j-1][l1 ][ 2 ]);
//    }
//}
//
///****************************************************************************/
//
//P_FXOO(i,j,edge,itab,l1,l2,kbuf,idebug)
//int i,j,itab[][2][4],l1,l2,idebug;
//short *edge;
//Chaine kbuf[];
//{
//    Chaine UneChaine;
//    int sipremier=0;
//
//    if (idebug!=0) printf("Configuration P_FXOO Line %3d Col %3d\n",i,j);
//
//    itab[ j][l2 ][ 2 ] = itab[ j-1][l2 ][ 2 ];
//    itab[ j][l2 ][ 1 ] = 'O' ;
//    if (itab[ j-1][l2 ][ 1 ] == 'P') {
//        itab[ j-1][l2 ][ 1 ] = 'D';
//        sipremier = 1;
//    } else itab[ j-1][l2 ][ 1 ] = 'X';
//    nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
//    if (itab[ j+1][l1 ][ 1 ] == 'P') itab[ j+1][l1 ][ 1 ] = 'D'; else itab[ j+1][l1 ][ 1 ] = 'F';
//    itab[ j][l2 ][ 1 ] = 'F';
//    if (itab[ j-1][l1 ][ 1 ] == 'F') {
//        if (fils_( kbuf [itab[ j-1][l1 ][ 2 ] -1],-itab[ j][l2][2 ]))
//                fils_( kbuf [itab[ j][l2][2 ] -1],-itab[ j-1][l1 ][ 2 ]);
//    } else {
//        if (aieul_( kbuf [ itab[ j-1][l1 ][ 2 ]-1],-itab[ j][l2][2 ]))
//                fils_ ( kbuf [itab[ j][l2][2 ] -1],itab[ j-1][l1 ][ 2 ]);
//    }
//    if (itab[j-1][l1][2] != itab[j+1][l1][2])
//        if (!ptest5(i,j,edge)) {
//            if (itab[ j+1][l1 ][ 1 ] == 'F') {
//                if (fils_( kbuf [itab[ j+1][l1 ][ 2 ] -1],-itab[ j][l2][2 ]))
//                        fils_( kbuf [itab[ j][l2][2 ] -1],-itab[ j+1][l1 ][ 2 ]);
//            } else {
//                if (aieul_( kbuf [ itab[ j+1][l1 ][ 2 ]-1],-itab[ j][l2][2 ]))
//                        fils_ ( kbuf [itab[ j][l2][2 ] -1],itab[ j+1][l1 ][ 2 ]);
//            }
//    }
//}
//
///****************************************************************************/
//
//P_FXOF(i,j,edge,itab,l1,l2,kbuf,idebug)
//int i,j,itab[][2][4],l1,l2,idebug;
//short *edge;
//Chaine kbuf[];
//{
//    Chaine UneChaine;
//    int sipremier=0;
//
//    if (idebug!=0) printf("Configuration P_FXOF Line %3d Col %3d\n",i,j);
//
//    itab[ j][l2 ][ 2 ] = itab[ j+1][l1 ][ 2 ];
//    itab[ j][l2 ][ 1 ] = 'O' ;
//    if (itab[ j+1][l1 ][ 1 ] == 'P') {
//        itab[ j+1][l1 ][ 1 ] = 'D';
//        sipremier = 1;
//    } else itab[ j+1][l1 ][ 1 ] = 'X';
//    nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
//    itab[ j][l2 ][ 1 ] = 'F';
//    if (itab[ j-1][l2 ][ 1 ] == 'F') {
//        if (fils_( kbuf [itab[ j-1][l2 ][ 2 ] -1],-itab[ j][l2][2 ]))
//                fils_( kbuf [itab[ j][l2][2 ] -1],-itab[ j-1][l2 ][ 2 ]);
//    } else {
//        if (aieul_( kbuf [ itab[ j-1][l2 ][ 2 ]-1],-itab[ j][l2][2 ]))
//                fils_ ( kbuf [itab[ j][l2][2 ] -1],itab[ j-1][l2 ][ 2 ]);
//    }
//}
//
///****************************************************************************/
//
//P_FXFX(i,j,edge,itab,l1,l2,kbuf,idebug)
//int i,j,itab[][2][4],l1,l2,idebug;
//short *edge;
//Chaine kbuf[];
//{
//    Chaine UneChaine;
//    int sipremier=0;
//
//    if (idebug!=0) printf("Configuration P_FXFX Line %3d Col %3d\n",i,j);
//
//    LeLinker->nb_chaines += 1;
//    if (LeLinker->nb_chaines >= LeLinker->nb_chaines_max) {
//        printf("Too many chains\n");
//        exit(-1);
//    }
//    UneChaine = (Chaine) opench_();
//    if (UneChaine == NULL) {
//        printf("There is no more space for chains.\n");
//        exit(-2);
//    }
//    kbuf[ LeLinker->nb_chaines - 1] = UneChaine ;
//    itab[ j][l2 ][ 2 ] = LeLinker->nb_chaines;
//    itab[ j][l2 ][ 1 ] = 'P'   ;
//    nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
//    if (itab[ j-1][l1 ][ 1 ] == 'D') {
//        if (aieul_( kbuf[  itab[ j][l2][2 ] -1],itab[ j-1][l1 ][ 2 ]))
//                aieul_( kbuf[itab[ j-1][l1 ][ 2 ] -1],itab[ j][l2][2 ]);
//    } else {
//        if (aieul_( kbuf[ itab[ j][l2][2 ] -1],-itab[ j-1][l1 ][ 2 ]))
//                fils_ ( kbuf[ itab[ j-1][l1 ][ 2 ] -1],itab[ j][l2][2 ]);
//    }
//    if (itab[j-1][l1][2] != itab[j+1][l1][2])
//        if (!ptest5(i,j,edge)) {
//            if (itab[ j+1][l1 ][ 1 ] == 'D') {
//                if (aieul_( kbuf[  itab[ j][l2][2 ] -1],itab[ j+1][l1 ][ 2 ]))
//                        aieul_( kbuf[itab[ j+1][l1 ][ 2 ] -1],itab[ j][l2][2 ]);
//            } else {
//                if (aieul_( kbuf[ itab[ j][l2][2 ] -1],-itab[ j+1][l1 ][ 2 ]))
//                        fils_ ( kbuf[ itab[ j+1][l1 ][ 2 ] -1],itab[ j][l2][2 ]);
//            }
//    }
//}
//
///****************************************************************************/
//
//P_FXFO(i,j,edge,itab,l1,l2,kbuf,idebug)
//int i,j,itab[][2][4],l1,l2,idebug;
//short *edge;
//Chaine kbuf[];
//{
//    Chaine UneChaine;
//    int sipremier=0;
//
//    if (idebug!=0) printf("Configuration P_FXFO Line %3d Col %3d\n",i,j);
//
//    itab[ j][l2 ][ 2 ] = itab[ j-1][l2 ][ 2 ];
//    itab[ j][l2 ][ 1 ] = 'O' ;
//    if (itab[ j-1][l2 ][ 1 ] == 'P') {
//        itab[ j-1][l2 ][ 1 ] = 'D';
//        sipremier = 1;
//    } else itab[ j-1][l2 ][ 1 ] = 'X';
//    nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
//    itab[ j][l2 ][ 1 ] = 'F';
//    if (itab[ j-1][l1 ][ 1 ] == 'F') {
//        if (fils_( kbuf [itab[ j-1][l1 ][ 2 ] -1],-itab[ j][l2][2 ]))
//                fils_( kbuf [itab[ j][l2][2 ] -1],-itab[ j-1][l1 ][ 2 ]);
//    } else {
//        if (aieul_( kbuf [ itab[ j-1][l1 ][ 2 ]-1],-itab[ j][l2][2 ]))
//                fils_ ( kbuf [itab[ j][l2][2 ] -1],itab[ j-1][l1 ][ 2 ]);
//    }
//    if (itab[j-1][l1][2] != itab[j+1][l1][2])
//        if (!ptest5(i,j,edge)) {
//            if (itab[ j+1][l1 ][ 1 ] == 'F') {
//                if (fils_( kbuf [itab[ j+1][l1 ][ 2 ] -1],-itab[ j][l2][2 ]))
//                        fils_( kbuf [itab[ j][l2][2 ] -1],-itab[ j+1][l1 ][ 2 ]);
//            } else {
//                if (aieul_( kbuf [ itab[ j+1][l1 ][ 2 ]-1],-itab[ j][l2][2 ]))
//                        fils_ ( kbuf [itab[ j][l2][2 ] -1],itab[ j+1][l1 ][ 2 ]);
//            }
//    }
//}
//
///****************************************************************************/
//
//P_FXFF(i,j,edge,itab,l1,l2,kbuf,idebug)
//int i,j,itab[][2][4],l1,l2,idebug;
//short *edge;
//Chaine kbuf[];
//{
//    Chaine UneChaine;
//    int sipremier=0;
//
//    if (idebug!=0) printf("Configuration P_FXFF Line %3d Col %3d\n",i,j);
//
//    LeLinker->nb_chaines += 1;
//    if (LeLinker->nb_chaines >= LeLinker->nb_chaines_max) {
//        printf("Too many chains\n");
//        exit(-1);
//    }
//    UneChaine = (Chaine) opench_();
//    if (UneChaine == NULL) {
//        printf("There is no more space for chains.\n");
//        exit(-2);
//    }
//    kbuf[ LeLinker->nb_chaines - 1] = UneChaine ;
//    itab[ j][l2 ][ 2 ] = LeLinker->nb_chaines;
//    itab[ j][l2 ][ 1 ] = 'P'   ;
//    nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
//    if (itab[j-1][l1][2] != itab[j+1][l1][2])
//        if (!ptest5(i,j,edge)) {
//            if (itab[ j+1][l1 ][ 1 ] == 'D') {
//                if (aieul_( kbuf[  itab[ j][l2][2 ] -1],itab[ j+1][l1 ][ 2 ]))
//                        aieul_( kbuf[itab[ j+1][l1 ][ 2 ] -1],itab[ j][l2][2 ]);
//            } else {
//                if (aieul_( kbuf[ itab[ j][l2][2 ] -1],-itab[ j+1][l1 ][ 2 ]))
//                        fils_ ( kbuf[ itab[ j+1][l1 ][ 2 ] -1],itab[ j][l2][2 ]);
//            }
//    }
//    if (itab[ j-1][l2 ][ 1 ] == 'D') {
//        if (aieul_( kbuf[  itab[ j][l2][2 ] -1],itab[ j-1][l2 ][ 2 ]))
//                aieul_( kbuf[itab[ j-1][l2 ][ 2 ] -1],itab[ j][l2][2 ]);
//    } else {
//        if (aieul_( kbuf[ itab[ j][l2][2 ] -1],-itab[ j-1][l2 ][ 2 ]))
//                fils_ ( kbuf[ itab[ j-1][l2 ][ 2 ] -1],itab[ j][l2][2 ]);
//    }
//}
//
///****************************************************************************/
//
//P_FOXX(i,j,edge,itab,l1,l2,kbuf,idebug)
//int i,j,itab[][2][4],l1,l2,idebug;
//short *edge;
//Chaine kbuf[];
//{
//    Chaine UneChaine;
//    int sipremier=0;
//
//    if (idebug!=0) printf("Configuration P_FOXX Line %3d Col %3d\n",i,j);
//
//    itab[ j][l2 ][ 2 ] = itab[ j][l1   ][ 2 ];
//    itab[ j][l2 ][ 1 ] = 'O' ;
//    if (itab[ j][l1   ][ 1 ] == 'P') {
//        itab[ j][l1   ][ 1 ] = 'D';
//        sipremier = 1;
//    } else itab[ j][l1   ][ 1 ] = 'X';
//    nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
//    if ( ptest5668(i,j,edge) ) {
//        itab[ j][l2 ][ 1 ] = 'F';
//        if (itab[ j-1][l1 ][ 1 ] == 'F') {
//            if (fils_( kbuf [itab[ j-1][l1 ][ 2 ] -1],-itab[ j][l2][2 ]))
//                    fils_( kbuf [itab[ j][l2][2 ] -1],-itab[ j-1][l1 ][ 2 ]);
//        } else {
//            if (aieul_( kbuf [ itab[ j-1][l1 ][ 2 ]-1],-itab[ j][l2][2 ]))
//                    fils_ ( kbuf [itab[ j][l2][2 ] -1],itab[ j-1][l1 ][ 2 ]);
//        }
//    }
//}
//
///****************************************************************************/
//
//P_FFXF(i,j,edge,itab,l1,l2,kbuf,idebug)
//int i,j,itab[][2][4],l1,l2,idebug;
//short *edge;
//Chaine kbuf[];
//{
//    Chaine UneChaine;
//    int sipremier=0;
//
//    if (idebug!=0) printf("Configuration P_FFXF Line %3d Col %3d\n",i,j);
//
//    itab[ j][l2 ][ 2 ] = itab[ j-1][l2][ 2 ];
//    itab[ j][l2 ][ 1 ] = 'F';
//    nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
//    if (itab[ j-1][l2 ][ 1 ] == 'F') {
//        if (fils_( kbuf [itab[ j-1][l2 ][ 2 ] -1],-itab[ j][l2][2 ]))
//                fils_( kbuf [itab[ j][l2][2 ] -1],-itab[ j-1][l2 ][ 2 ]);
//    } else {
//        if (aieul_( kbuf [ itab[ j-1][l2 ][ 2 ]-1],-itab[ j][l2][2 ]))
//                fils_ ( kbuf [itab[ j][l2][2 ] -1],itab[ j-1][l2 ][ 2 ]);
//    }
//}
//
///****************************************************************************/
//
//P_FFOX(i,j,edge,itab,l1,l2,kbuf,idebug)
//int i,j,itab[][2][4],l1,l2,idebug;
//short *edge;
//Chaine kbuf[];
//{
//    Chaine UneChaine;
//    int sipremier=0;
//
//    if (idebug!=0) printf("Configuration P_FFOX Line %3d Col %3d\n",i,j);
//
//    itab[ j][l2 ][ 2 ] = itab[ j+1][l1 ][ 2 ];
//    itab[ j][l2 ][ 1 ] = 'O' ;
//    if (itab[ j+1][l1 ][ 1 ] == 'P') {
//        itab[ j+1][l1 ][ 1 ] = 'D';
//        sipremier = 1;
//    } else itab[ j+1][l1 ][ 1 ] = 'X';
//    nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
//    itab[ j][l2 ][ 1 ] = 'F';
//    if (itab[ j][l1   ][ 1 ] == 'F') {
//        if (fils_( kbuf [itab[ j][l1   ][ 2 ] -1],-itab[ j][l2][2 ]))
//                fils_( kbuf [itab[ j][l2][2 ] -1],-itab[ j][l1   ][ 2 ]);
//    } else {
//        if (aieul_( kbuf [ itab[ j][l1   ][ 2 ]-1],-itab[ j][l2][2 ]))
//                fils_ ( kbuf [itab[ j][l2][2 ] -1],itab[ j][l1   ][ 2 ]);
//    }
//}
//
///****************************************************************************/
//
//P_FFOF(i,j,edge,itab,l1,l2,kbuf,idebug)
//int i,j,itab[][2][4],l1,l2,idebug;
//short *edge;
//Chaine kbuf[];
//{
//    Chaine UneChaine;
//    int sipremier=0;
//
//    if (idebug!=0) printf("Configuration P_FFOF Line %3d Col %3d\n",i,j);
//
//    if (itab[ j+1][l1 ][ 1 ] == 'P') itab[ j+1][l1 ][ 1 ] = 'D'; else itab[ j+1][l1 ][ 1 ] = 'F';
//    itab[ j][l2 ][ 2 ] = itab[ j-1][l2][ 2 ];
//    itab[ j][l2 ][ 1 ] = 'F';
//    nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
//    if (itab[ j-1][l2 ][ 1 ] == 'F') {
//        if (fils_( kbuf [itab[ j-1][l2 ][ 2 ] -1],-itab[ j][l2][2 ]))
//                fils_( kbuf [itab[ j][l2][2 ] -1],-itab[ j-1][l2 ][ 2 ]);
//    } else {
//        if (aieul_( kbuf [ itab[ j-1][l2 ][ 2 ]-1],-itab[ j][l2][2 ]))
//                fils_ ( kbuf [itab[ j][l2][2 ] -1],itab[ j-1][l2 ][ 2 ]);
//    }
//}
//
///****************************************************************************/
//
//P_FFFX(i,j,edge,itab,l1,l2,kbuf,idebug)
//int i,j,itab[][2][4],l1,l2,idebug;
//short *edge;
//Chaine kbuf[];
//{
//    Chaine UneChaine;
//    int sipremier=0;
//
//    if (idebug!=0) printf("Configuration P_FFFX Line %3d Col %3d\n",i,j);
//
//    LeLinker->nb_chaines += 1;
//    if (LeLinker->nb_chaines >= LeLinker->nb_chaines_max) {
//        printf("Too many chains\n");
//        exit(-1);
//    }
//    UneChaine = (Chaine) opench_();
//    if (UneChaine == NULL) {
//        printf("There is no more space for chains.\n");
//        exit(-2);
//    }
//    kbuf[ LeLinker->nb_chaines - 1] = UneChaine ;
//    itab[ j][l2 ][ 2 ] = LeLinker->nb_chaines;
//    itab[ j][l2 ][ 1 ] = 'P'   ;
//    nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
//    if (itab[ j][l1   ][ 1 ] == 'D') {
//        if (aieul_( kbuf[  itab[ j][l2][2 ] -1],itab[ j][l1   ][ 2 ]))
//                aieul_( kbuf[itab[ j][l1   ][ 2 ] -1],itab[ j][l2][2 ]);
//    } else {
//        if (aieul_( kbuf[ itab[ j][l2][2 ] -1],-itab[ j][l1   ][ 2 ]))
//                fils_ ( kbuf[ itab[ j][l1   ][ 2 ] -1],itab[ j][l2][2 ]);
//    }
//}
//
///****************************************************************************/
//
//P_FFFF(i,j,edge,itab,l1,l2,kbuf,idebug)
//int i,j,itab[][2][4],l1,l2,idebug;
//short *edge;
//Chaine kbuf[];
//{
//    Chaine UneChaine;
//    int sipremier=0;
//
//    if (idebug!=0) printf("Configuration P_FFFF Line %3d Col %3d\n",i,j);
//
//    itab[ j][l2 ][ 2 ] = itab[ j-1][l2][ 2 ];
//    itab[ j][l2 ][ 1 ] = 'F';
//    nwmail_( kbuf[ itab[ j][l2][2 ] -1 ], i,j);
//    if (itab[ j-1][l2 ][ 1 ] == 'F') {
//        if (fils_( kbuf [itab[ j-1][l2 ][ 2 ] -1],-itab[ j][l2][2 ]))
//                fils_( kbuf [itab[ j][l2][2 ] -1],-itab[ j-1][l2 ][ 2 ]);
//    } else {
//        if (aieul_( kbuf [ itab[ j-1][l2 ][ 2 ]-1],-itab[ j][l2][2 ]))
//                fils_ ( kbuf [itab[ j][l2][2 ] -1],itab[ j-1][l2 ][ 2 ]);
//    }
//}
//
///* Init_gtotab initializes gtotab function address table.*/
//
//initgtot_()
//{
//    gtotab[  0]=P_XXXX;
//    gtotab[  2]=P_XXXO;
//    gtotab[ 20]=P_XXXF;
//    gtotab[  4]=P_XXOX;
//    gtotab[  6]=P_XXOO;
//    gtotab[ 24]=P_XXOF;
//    gtotab[ 40]=P_XXFX;
//    gtotab[ 42]=P_XXFO;
//    gtotab[ 60]=P_XXFF;
//    gtotab[  8]=P_XOXX;
//    gtotab[ 80]=P_XFXX;
//    gtotab[ 82]=P_XFXO;
//    gtotab[100]=P_XFXF;
//    gtotab[ 84]=P_XFOX;
//    gtotab[ 86]=P_XFOO;
//    gtotab[104]=P_XFOF;
//    gtotab[120]=P_XFXX;
//    gtotab[122]=P_XFFO;
//    gtotab[140]=P_XFFF;
//    gtotab[  1]=P_OXXX;
//    gtotab[  5]=P_OXOX;
//    gtotab[ 41]=P_OXFX;
//    gtotab[ 10]=P_FXXX;
//    gtotab[ 12]=P_FXXO;
//    gtotab[ 30]=P_FXXF;
//    gtotab[ 14]=P_FXOX;
//    gtotab[ 16]=P_FXOO;
//    gtotab[ 34]=P_FXOF;
//    gtotab[ 50]=P_FXFX;
//    gtotab[ 52]=P_FXFO;
//    gtotab[ 70]=P_FXFF;
//    gtotab[ 18]=P_FOXX;
//    gtotab[ 90]=P_XFXX;
//    gtotab[ 92]=P_XFXO;
//    gtotab[110]=P_FFXF;
//    gtotab[ 94]=P_FFOX;
//    gtotab[ 96]=P_XFOO;
//    gtotab[114]=P_FFOF;
//    gtotab[130]=P_FFFX;
//    gtotab[132]=P_XFFO;
//    gtotab[150]=P_FFFF;
//}
//
//int ptest5668(i,j,edge)
//register int i,j;
//register short *edge;
//{
//    register int p5,p6,p8;
//    register int jdimx,dimx;
//
// 
//    dimx = LeLinker->dim_x;
//    jdimx = j * LeLinker->dim_x;
//    edge += i + jdimx;
//    p5= *(edge + dimx);
//    p6= *(edge + 1 - dimx);
//    p8= *(edge + 1 + dimx);
//
//    return ( (p5 && p6) || (p6 && p8));
//}
//
//int ptest5(int i,int j,edge)
//register short *edge;
//{
//    return *(edge + i + (j + 1) * LeLinker->dim_x);
//}
//
///* Initialize line l of itab with 'X'es. */
//
//initab_(l,itab)
//int l;
//int itab[][2][4];
//{
//    register int k ;
//
//    for (k=0; k<=LeLinker->dim_y + 1; k++) itab[k][l][1] = 'X' ;	
//}
//
///*
//    Examine the surroundings of current pixel in itab, in order to determine the pixel
//    configuration. j is the colounm address of current pixel.
//*/
//
//FSA(int i,int j,edge,itab,int l1,int l2,kbuf,int idebug)
//short *edge;
//int itab[][2][4];
//Chaine kbuf[];
//{
//    int ncov = 0 ; 
//    int ncfv = 0 ;
//    static int poids[] = {1,8,4,2} ;
//    register int k ,vitab;
//    int V;
//
//    /*--------- traitement de la ligne l1 -------------*/
//
//    for (k=(-1) ; k<=1; k++) {
//    	vitab=itab[j+k][l1][1];
//    	if (vitab <= 'F') ncfv += poids[k+1] ;
//	else
//    	    if (vitab <= 'P') ncov += poids[k+1];
//    }
//
//    /*--------- Traitement de la ligne l2   ------------*/
//
//    vitab=itab[j-1][l2][1];
//    if (vitab <= 'F') ncfv += poids[3] ;
//    else
//        if (vitab <= 'P') ncov += poids[3] ;	
//
//    V=ncfv*10 + ncov;
//
//    (*(gtotab[V]))(i,j,edge,itab,l1,l2,kbuf,idebug);
//}
//
