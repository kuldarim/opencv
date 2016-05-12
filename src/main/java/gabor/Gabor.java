package gabor;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Gabor {

    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    public static void main( String[] args ) {

        try {
            System.loadLibrary( Core.NATIVE_LIBRARY_NAME );



            Mat image1 = Highgui.imread("src/main/resources/gabor/f1.jpg", Highgui.CV_LOAD_IMAGE_GRAYSCALE);
            Mat image2 = Highgui.imread("src/main/resources/gabor/f4.jpg", Highgui.CV_LOAD_IMAGE_GRAYSCALE);

            Imgproc.resize(image1, image1, new Size(240.0, 240.0));
            Imgproc.resize(image2, image2, new Size(240.0, 240.0));

            ArrayList<Mat> gabors1 = createGaborMats(image1);
            ArrayList<Mat> gabors2 = createGaborMats(image2);

            Mat mat1 = createFeatureMat(gabors1);
            Mat mat2 = createFeatureMat(gabors2);

            Highgui.imwrite("src/main/resources/gabor/x1.jpg", mat1);
            Highgui.imwrite("src/main/resources/gabor/x2.jpg", mat2);

            System.out.println(Core.norm(mat1, mat2, Core.NORM_L2));

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static ArrayList<Mat> createGaborMats(Mat image) {
        ArrayList<Mat> gabors = new ArrayList<Mat>();

        //predefine parameters for Gabor kernel
        List<Double> thetas = Arrays.asList(0.0, 23.0, 45.0, 68.0, 90.0, 113.0, 135.0, 158.0);
        List<Double> lambdas = Arrays.asList(3.0, 6.0, 13.0, 28.0, 58.0);
        Size kSize = new Size(2, 2);

        double sigma = 20;
        double gamma = 0.5;
        double psi =  Math.PI / 2;

        for (Double theta: thetas) {
            for (Double lambda: lambdas) {
                    /*
                    ers:
                        ksize – Size of the filter returned.
                        sigma – Standard deviation of the gaussian envelope.
                        theta – Orientation of the normal to the parallel stripes of a Gabor function.
                        lambd – Wavelength of the sinusoidal factor.
                        gamma – Spatial aspect ratio.
                        psi – Phase offset.
                        ktype – Type of filter coefficients. It can be CV_32F or CV_64F .
                     */
                // the filters kernel
                Mat kernel = Imgproc.getGaborKernel(kSize, sigma, theta, lambda, gamma, psi, CvType.CV_32F);
                // apply filters on my image. The result is stored in gabor
                Mat gabor = new Mat (image.width(), image.height(), CvType.CV_8UC1);
                Imgproc.filter2D(image, gabor, -1, kernel);

                gabors.add(gabor);
            }
        }

        return gabors;
    }

    private static Mat createFeatureMat(ArrayList<Mat> gabors) {
        // downsampling 40 points 8 * 5
        List<Integer> xes = Arrays.asList(0, 30, 60, 90, 120, 150, 180, 210, 239);
        List<Integer> yks = Arrays.asList(0, 48, 96, 146, 192, 239);

        Mat mat = new Mat(40, 40, CvType.CV_8UC1);
        int matX = 0;

        for (int i = 0; i < gabors.size(); i++) {
            Mat gabor = gabors.get(i);
            Highgui.imwrite("src/main/resources/gabor/result" + (i + 1) + ".jpg", gabor);

            int matY = 0;
            for (Integer x : xes) {
                for (Integer y : yks) {
                    mat.put(matX, matY, gabor.get(x, y)[0]);
                    matY++;
                }
            }
            matX++;
        }

        return mat;
    }
}
