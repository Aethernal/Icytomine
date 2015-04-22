package plugins.faubin.cytomine.utils.threshold;

import icy.image.IcyBufferedImage;
import icy.image.IcyBufferedImageUtil;
import icy.sequence.Sequence;

import java.awt.image.BufferedImage;

public class Otsu {

	public static int threshold(int[] histo, int width, int height) {

		/*
		 * Init some variables
		 */
		double[] valueProbability = new double[histo.length];

		/*
		 * calculate the total number of pixels in the image
		 */
		long sum = width * height;

		/*
		 * Calculate the probability of occurrence of grey-level
		 */
		for (int i = 0; i < histo.length; i++)
			valueProbability[i] = ((double) (histo[i])) / (double) (sum);

		/*
		 * Calculate muT ad define in otsu's algorithm
		 */
		double mu_T = 0.0;
		for (int i = 0; i < histo.length; i++)
			mu_T += i * valueProbability[i];

		/*
		 * Calculate sigmaT as define in otsu's algorithm
		 */
		double sigma_T = 0.0;
		for (int i = 0; i < histo.length; i++)
			sigma_T += (i - mu_T) * (i - mu_T) * valueProbability[i];

		/*
		 * Calculate index
		 */
		int minInd = -1;
		int maxInd = -1;
		for (int i = 0; i < valueProbability.length; i++) {
			if ((valueProbability[i] > 0) && (minInd < 0))
				minInd = i;

			if (valueProbability[i] > 0)
				maxInd = i;

		}

		/*
		 * Calculate nu
		 */
		double[] nu = new double[histo.length];
		double w0 = 0.0;
		double mu_t = 0.0;
		double nuMax = -1.0;
		int threshold = -1;

		for (int i = minInd; i < maxInd; i++) {

			w0 += valueProbability[i];
			mu_t += i * valueProbability[i];

			/*
			 * Calculate w1, mu0 and mu1
			 */
			double w1 = 1.0 - w0;
			double mu0 = mu_t / w0;
			double mu1 = (mu_T - mu_t) / w1;

			/*
			 * Calculate sigma_B
			 */
			double sigma_B = w0 * w1 * (mu1 - mu0) * (mu1 - mu0);

			nu[i] = sigma_B / sigma_T;

			/*
			 * Seek the best nu
			 */
			if (nuMax <= nu[i]) {
				nuMax = nu[i];
				threshold = i;
			}

		}

		return threshold;
	}

	public static Sequence seuillage(Sequence sequence, int seuil) {
		Sequence result = null;
		BufferedImage image = new BufferedImage(sequence.getWidth(),
				sequence.getHeight(), BufferedImage.TYPE_INT_RGB);
		BufferedImage toModify = IcyBufferedImageUtil.toBufferedImage(
				sequence.getFirstImage(), image);

		for (int i = 0; i < sequence.getHeight(); i++) {
			for (int j = 0; j < sequence.getWidth(); j++) {
				int rgb = image.getRGB(j, i);
				int r = (rgb >> 16) & 0xFF;
				int g = (rgb >> 8) & 0xFF;
				int b = (rgb & 0xFF);
				int grayValue = (r + g + b) / 3;

				if (grayValue < seuil) {
					image.setRGB(j, i, 16777215);
				} else {
					image.setRGB(j, i, 0);
				}
			}
		}

		result = new Sequence(IcyBufferedImage.createFrom(image));

		return result;
	}

}
