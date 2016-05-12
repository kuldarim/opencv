package gabor;

import org.opencv.core.*;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Gabor {

    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    public static void main( String[] args ) {

        try {
            System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
            // read images
            Mat image1 = Highgui.imread("src/main/resources/gabor/p1/p3.jpg", Highgui.CV_LOAD_IMAGE_GRAYSCALE);
            Mat image2 = Highgui.imread("src/main/resources/gabor/p1/p7.jpg", Highgui.CV_LOAD_IMAGE_GRAYSCALE);
            // detect images
            compareTwoImages(image1, image2);
//            detectFace(image2);
//            // resize to make all the same size
//            Imgproc.resize(image1, image1, new Size(240.0, 240.0));
//            Imgproc.resize(image2, image2, new Size(240.0, 240.0));
//            // create gabor filters
//            ArrayList<Mat> gabors1 = createGaborMats(image1);
//            ArrayList<Mat> gabors2 = createGaborMats(image2);
//            // create feature mats
//            Mat mat1 = createFeatureMat(gabors1);
//            Mat mat2 = createFeatureMat(gabors2);
//
//            // Highgui.imwrite("src/main/resources/gabor/x1.jpg", mat1);
//            // Highgui.imwrite("src/main/resources/gabor/x2.jpg", mat2);
//            //calculate the distance between matrices
           // System.out.println(Core.norm(mat1, mat2, Core.NORM_L2));

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void compareTwoImages(Mat image1, Mat image2) {
        Mat face1 = detectFace(image1, "img1");
        Mat face2 = detectFace(image2, "img2");
        // resize to make all the same size
        Imgproc.resize(face1, face1, new Size(240.0, 240.0));
        Imgproc.resize(face2, face2, new Size(240.0, 240.0));
        // create gabor filters
        ArrayList<Mat> gabors1 = createGaborMats(face1);
        ArrayList<Mat> gabors2 = createGaborMats(face2);
        // create feature mats
        Mat mat1 = createFeatureMat(gabors1);
        Mat mat2 = createFeatureMat(gabors2);

         Highgui.imwrite("src/main/resources/gabor/x1.jpg", mat1);
         Highgui.imwrite("src/main/resources/gabor/x2.jpg", mat2);
        //calculate the distance between matrices
        System.out.println(Core.norm(mat1, mat2, Core.NORM_L2));
    }

    private static ArrayList<Mat> createGaborMats(Mat image) {
        ArrayList<Mat> gabors = new ArrayList<Mat>();

        //predefine parameters for Gabor kernel
        List<Double> thetas = Arrays.asList(0.0, 23.0, 45.0, 68.0, 90.0, 113.0, 135.0, 158.0);
        List<Double> lambdas = Arrays.asList(3.0, 6.0, 13.0, 28.0, 58.0);
        Size kSize = new Size(5, 5);

        double sigma = 20;
        double gamma = 0.7;
        double psi =  0;

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
        List<Integer> xes = Arrays.asList(
                0, 1, 5, 8, 13, 17, 30,
                33, 46, 48, 57, 60, 67,
                69, 81, 85, 86, 88, 90,
                91, 94, 97, 101, 105,
                120, 122, 134, 145,
                150, 155, 161, 171,
                180, 185, 191, 201,
                210, 222, 232, 239
        );
        List<Integer> yks = Arrays.asList(0, 48, 96, 146, 192, 239);

        Mat mat = new Mat(40, 40, CvType.CV_8UC1);
        int matX = 0;

        for (int i = 0; i < gabors.size(); i++) {
            Mat gabor = gabors.get(i);
            Highgui.imwrite("src/main/resources/gabor/kernel/result" + (i + 1) + ".jpg", gabor);

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

    private static Mat detectFace (Mat img, String filename) {
        CascadeClassifier faceDetector = new CascadeClassifier("src/main/resources/haars/haarcascade_frontalface_default.xml");

        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(img, faceDetections);

        Mat cropped = new Mat(img, faceDetections.toArray()[0]);
        Highgui.imwrite("src/main/resources/gabor/cropped/" + filename + ".jpg", cropped);
        return img;
    }
}
