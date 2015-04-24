package plugins.faubin.cytomine.utils.contours.methode1.gradvect;

public class gradvect {

	public static final int TRUE = 1;
	public static final int FALSE = 0;

	public static int gradvc(i2d_t i2din, i2d_t i2dout, int C, int L) {

		int i, j;
		char a1, b1, c1, d1, e1, f1, g1, h1, i1, a2, b2, c2, d2, e2, f2, g2, h2, i2, a3, b3, c3, d3, e3, f3, g3, h3, i3;
		char[] ptrr, ptrg, ptrb, ptrrout;
		float drdx, drdy, dgdx, dgdy, dbdx, dbdy, p, q, t, grad;

		ptrr = I2DGetImagePtr(i2din);
		ptrg = I2DGetImagePtrG(i2din);
		ptrb = I2DGetImagePtrB(i2din);
		ptrrout = I2DGetImagePtr(i2dout);

		a1 = ptrr[0];
		b1 = ptrr[1];
		c1 = ptrr[2];
		d1 = ptrr[C];
		e1 = ptrr[C + 1];
		f1 = ptrr[C + 2];
		g1 = ptrr[2 * C];
		h1 = ptrr[2 * C + 1];
		i1 = ptrr[2 * C + 1];

		a2 = ptrg[0];
		b2 = ptrg[1];
		c2 = ptrg[2];
		d2 = ptrg[C];
		e2 = ptrg[C + 1];
		f2 = ptrg[C + 2];
		g2 = ptrg[2 * C];
		h2 = ptrg[2 * C + 1];
		i2 = ptrg[2 * C + 1];

		a3 = ptrb[0];
		b3 = ptrb[1];
		c3 = ptrb[2];
		d3 = ptrb[C];
		e3 = ptrb[C + 1];
		f3 = ptrb[C + 2];
		g3 = ptrb[2 * C];
		h3 = ptrb[2 * C + 1];
		i3 = ptrb[2 * C + 1];

		for (i = 1; i < L - 1; i++) {
			for (j = 1; j < C - 1; j++) {
				drdx = -(float) a1 - 2 * (float) b1 - (float) c1 + (float) g1
						+ 2 * (float) h1 + (float) i1;
				drdy = -(float) a1 - 2 * (float) d1 - (float) g1 + (float) c1
						+ 2 * (float) f1 + (float) i1;

				dgdx = -(float) a2 - 2 * (float) b2 - (float) c2 + (float) g2
						+ 2 * (float) h2 + (float) i2;
				dgdy = -(float) a2 - 2 * (float) d2 - (float) g2 + (float) c2
						+ 2 * (float) f2 + (float) i2;

				dbdx = -(float) a3 - 2 * (float) b3 - (float) c3 + (float) g3
						+ 2 * (float) h3 + (float) i3;
				dbdy = -(float) a3 - 2 * (float) d3 - (float) g3 + (float) c3
						+ 2 * (float) f3 + (float) i3;

				a1 = b1;
				b1 = c1;
				d1 = e1;
				e1 = f1;
				g1 = h1;
				h1 = i1;
				c1 = ptrr[(i - 1) * C + (j + 1)];
				f1 = ptrr[(i) * C + (j + 1)];
				i1 = ptrr[(i + 1) * C + (j + 1)];

				a2 = b2;
				b2 = c2;
				d2 = e2;
				e2 = f2;
				g2 = h2;
				h2 = i2;
				c2 = ptrg[(i - 1) * C + (j + 1)];
				f2 = ptrg[(i) * C + (j + 1)];
				i2 = ptrg[(i + 1) * C + (j + 1)];

				a3 = b3;
				b3 = c3;
				d3 = e3;
				e3 = f3;
				g3 = h3;
				h3 = i3;
				c3 = ptrb[(i - 1) * C + (j + 1)];
				f3 = ptrb[(i) * C + (j + 1)];
				i3 = ptrb[(i + 1) * C + (j + 1)];

				p = drdx * drdx + dgdx * dgdx + dbdx * dbdx;
				q = drdy * drdy + dgdy * dgdy + dbdy * dbdy;
				t = drdx * drdy + dgdx * dgdy + dbdx * dbdy;

				/* grad=(p+q)/250; *//*
									 * gradient marginal (module des modules des
									 * trois grad)
									 */

				grad = (float) (Math.sqrt(0.5 * (p + q + Math
						.sqrt(((p + q) * (p + q)) - 4.0 * (p * q - t * t))))); /*
																				 * gradient
																				 * de
																				 * DI
																				 * ZENZO
																				 */

				if (grad >= 255.) {
					/* 10-06-04 printf("grad [%d][%d]:%.3f\n",i,j,grad ); */
					grad = 255; /* limitation */
				}

				ptrrout[i * C + j] = (char) grad;

			}
			/*
			 * printf("grad en double=%lf et en int=%d\n",grad,(unsigned
			 * char)grad);
			 */
		}

		return (0);
	}

	private static char[] I2DGetImagePtrB(i2d_t i2din) {
		// TODO Auto-generated method stub
		return null;
	}

	private static char[] I2DGetImagePtrG(i2d_t i2din) {
		// TODO Auto-generated method stub
		return null;
	}

	public static int gradvc_space(int[] i2din, int[] i2din2, int[] i2din3,
			i2d_t i2dout, int C, int L) {

		int i, j;
		int a1, b1, c1, d1, e1, f1, g1, h1, i1, a2, b2, c2, d2, e2, f2, g2, h2, i2, a3, b3, c3, d3, e3, f3, g3, h3, i3;
		int[] ptrr, ptrg, ptrb;
		float[] gradout;
		i2d_u8_t[] ptrrout;
		float drdx, drdy, dgdx, dgdy, dbdx, dbdy, p, q, t, grad;
		float min = Short.MAX_VALUE, max = -1;

		ptrr = i2din;
		ptrg = i2din2;
		ptrb = i2din3;

		// ptrrout=(i2d_u8_t[])I2DGetImagePtr(i2dout);
		gradout = new float[L * C];

		a1 = ptrr[0];
		b1 = ptrr[1];
		c1 = ptrr[2];
		d1 = ptrr[C];
		e1 = ptrr[C + 1];
		f1 = ptrr[C + 2];
		g1 = ptrr[2 * C];
		h1 = ptrr[2 * C + 1];
		i1 = ptrr[2 * C + 1];

		a2 = ptrg[0];
		b2 = ptrg[1];
		c2 = ptrg[2];
		d2 = ptrg[C];
		e2 = ptrg[C + 1];
		f2 = ptrg[C + 2];
		g2 = ptrg[2 * C];
		h2 = ptrg[2 * C + 1];
		i2 = ptrg[2 * C + 1];

		a3 = ptrb[0];
		b3 = ptrb[1];
		c3 = ptrb[2];
		d3 = ptrb[C];
		e3 = ptrb[C + 1];
		f3 = ptrb[C + 2];
		g3 = ptrb[2 * C];
		h3 = ptrb[2 * C + 1];
		i3 = ptrb[2 * C + 1];

		for (i = 1; i < L - 1; i++)
			for (j = 1; j < C - 1; j++) {
				drdx = -(float) a1 - 2 * (float) b1 - (float) c1 + (float) g1
						+ 2 * (float) h1 + (float) i1;
				drdy = -(float) a1 - 2 * (float) d1 - (float) g1 + (float) c1
						+ 2 * (float) f1 + (float) i1;

				dgdx = -(float) a2 - 2 * (float) b2 - (float) c2 + (float) g2
						+ 2 * (float) h2 + (float) i2;
				dgdy = -(float) a2 - 2 * (float) d2 - (float) g2 + (float) c2
						+ 2 * (float) f2 + (float) i2;

				dbdx = -(float) a3 - 2 * (float) b3 - (float) c3 + (float) g3
						+ 2 * (float) h3 + (float) i3;
				dbdy = -(float) a3 - 2 * (float) d3 - (float) g3 + (float) c3
						+ 2 * (float) f3 + (float) i3;

				a1 = b1;
				b1 = c1;
				d1 = e1;
				e1 = f1;
				g1 = h1;
				h1 = i1;
				c1 = ptrr[(i - 1) * C + (j + 1)];
				f1 = ptrr[(i) * C + (j + 1)];
				i1 = ptrr[(i + 1) * C + (j + 1)];

				a2 = b2;
				b2 = c2;
				d2 = e2;
				e2 = f2;
				g2 = h2;
				h2 = i2;
				c2 = ptrg[(i - 1) * C + (j + 1)];
				f2 = ptrg[(i) * C + (j + 1)];
				i2 = ptrg[(i + 1) * C + (j + 1)];

				a3 = b3;
				b3 = c3;
				d3 = e3;
				e3 = f3;
				g3 = h3;
				h3 = i3;
				c3 = ptrb[(i - 1) * C + (j + 1)];
				f3 = ptrb[(i) * C + (j + 1)];
				i3 = ptrb[(i + 1) * C + (j + 1)];

				p = drdx * drdx + dgdx * dgdx + dbdx * dbdx;
				q = drdy * drdy + dgdy * dgdy + dbdy * dbdy;
				t = drdx * drdy + dgdx * dgdy + dbdx * dbdy;

				grad = (float) (Math.sqrt(0.5 * (p + q + Math
						.sqrt(((p + q) * (p + q)) - 4.0 * (p * q - t * t))))); 

				gradout[i * C + j] = grad;
				min = Math.min(min, grad);
				max = Math.max(max, grad);

			}
		System.out.println("max:" + max + " min:" + min + " d\n");

		for (i = 4; i < L - 1; i++)
			for (j = 4; j < C - 1; j++) {
				grad = (float) (gradout[i * C + j] / (max - min) * 255.);
				ptrrout[i * C + j] = (i2d_u8_t) grad;
			}

		return (0);
	}

	public static int color_filter_grad_appel(i2d_t i2din, i2d_t i2dout) {
		int L, C;

		L = I2DGetLines(i2din);
		C = I2DGetCols(i2din);

		if ((gradvc(i2din, i2dout, C, L)) < 0) { /* appel fct gradvect */
			System.out.println("error: gradvc failed");
			return (-1);
		}

		System.out.println("debut aff img seg\n");
		cp_i2d_2_fb(i2dout, FB_NEW, "Image gradient vectoriel",
				fb_da_no_event_proc); /* affichage image finale */
		System.out.println("image sortie affichee\n ");
		return (0);

	}

	public static void i2d_color_space_change(i2d_t i2dorig,int espace_choisi,int[] bufC1, int[] bufC2,int[] bufC3)
	{
	  PFconversion[] convert = changer_espace(espace_choisi);
	  i2d_u8_t[] bufima, bufimaG, bufimaB;
	  int nc, nr;
	  int R, G, B, C1, C2, C3;
	  int i,j;
	
	  bufima = (i2d_u8_t[] )I2DGetImagePtr(i2dorig); 
	  bufimaG = (i2d_u8_t[] )I2DGetImagePtrG(i2dorig); 
	  bufimaB = (i2d_u8_t[] )I2DGetImagePtrB(i2dorig); 
	  nc = I2DGetCols(i2dorig);
	  nr = I2DGetLines(i2dorig);
	
	  for (j=0; j<nr; j++)
	    for (i=0; i<nc; i++) {
	      R = (int) bufima[j*nc+i];
	      G = (int) bufimaG[j*nc+i];
	      B = (int) bufimaB[j*nc+i]; 
	
	      C1 = convert[0](R,G,B);
	      C2 = convert[1](R,G,B);
	      C3 = convert[2](R,G,B);
	
	      bufC1[j*nc+i] = C1;
	      bufC2[j*nc+i] = C2;
	      bufC3[j*nc+i] = C3;
	    }
	}

	int color_filter_gradspace_appel(i2d_t i2din, i2d_t i2dout) {
		int L, C;
		int espace_choisi = 3;
		int[] bufR, bufG, bufB;

		L = I2DGetLines(i2din);
		C = I2DGetCols(i2din);
		bufR = new int[L * C];
		bufG = new int[L * C];
		bufB = new int[L * C];

		i2d_color_space_change(i2din, espace_choisi, bufR, bufG, bufB);

		if ((gradvc_space(bufR, bufG, bufB, i2dout, C, L)) < 0) { 
			System.out.println("error: gradvc failed");
			return (-1);
		}

		System.out.println("debut aff img seg\n");
		cp_i2d_2_fb(i2dout, FB_NEW, "Image gradient vectoriel",
				fb_da_no_event_proc); /* affichage image finale */
		System.out.println("image sortie affichee\n ");

		return (0);

	}
}
