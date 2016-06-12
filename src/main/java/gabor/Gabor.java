package gabor;

import org.opencv.core.*;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Gabor {

    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    public static void main( String[] args ) {

        try {
            System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
            // detect images
            List<Mat> p2 = getImageMats("p2");
            List<Mat> p3 = getImageMats("p3");
            List<Mat> p4 = getImageMats("p4");
            List<Mat> p5 = getImageMats("p5");
            List<Mat> p6 = getImageMats("p6");
            List<Mat> p7 = getImageMats("p7");


//            getStatistics(compareSamePerson6ImagesEach("p2", p2));
//            getStatistics(compareSamePerson6ImagesEach("p3", p3));
//            getStatistics(compareSamePerson6ImagesEach("p4", p4));
//            getStatistics(compareSamePerson6ImagesEach("p5", p5));
//            getStatistics(compareSamePerson6ImagesEach("p6", p6));
//            getStatistics(compareSamePerson6ImagesEach("p7", p7));

//            getStatistics(compareTwoPersonsMats("p2", p2, "p3", p3));
//            getStatistics(compareTwoPersonsMats("p2", p2, "p4", p4));
//            getStatistics(compareTwoPersonsMats("p2", p2, "p5", p5));
//            getStatistics(compareTwoPersonsMats("p2", p2, "p6", p6));
//            getStatistics(compareTwoPersonsMats("p2", p2, "p7", p7));
//
//            getStatistics(compareTwoPersonsMats("p3", p3, "p4", p4));
//            getStatistics(compareTwoPersonsMats("p3", p3, "p5", p5));
//            getStatistics(compareTwoPersonsMats("p3", p3, "p6", p6));
//            getStatistics(compareTwoPersonsMats("p3", p3, "p7", p7));
//
//            getStatistics(compareTwoPersonsMats("p4", p4, "p5", p5));
//            getStatistics(compareTwoPersonsMats("p4", p4, "p6", p6));
//            getStatistics(compareTwoPersonsMats("p4", p4, "p7", p7));
//
//            getStatistics(compareTwoPersonsMats("p5", p5, "p6", p6));
//            getStatistics(compareTwoPersonsMats("p5", p5, "p7", p7));

            getStatistics(compareTwoPersonsMats("p3", p3, "p2", p2));

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static List<Double> compareTwoPersonsMats(String name1, List<Mat> images1, String name2, List<Mat> images2) {
        List<Double> norms = new ArrayList<Double>();
        for (Mat img1 : images1) {
            for (Mat img2 : images2) {
                Double norm = compareTwoImages(img1, img2);
                if (!norm.equals(0.0)) {
                    System.out.println(name1 + name2 + " " + norm);
                    norms.add(norm);
                }
            }
        }

        return norms;
    }

    private static List<Double> compareSamePerson6ImagesEach(String name, List<Mat> images) {
        List<Double> norms = new ArrayList<Double>();
        for (Mat img1 : images) {
            for (Mat img2 : images) {
                Double norm = compareTwoImages(img1, img2);
                if (!norm.equals(0.0)) {
                    System.out.println(name + " " + norm);
                    norms.add(norm);
                }
            }
        }

        return norms;
    }

    private static List<Mat> getImageMats(String folderName) {
        List<Mat> images = new ArrayList<Mat>();
        for (int i = 1; i < 8; i++) {
            String path = "src/main/resources/gabor/" + folderName + "/p" + i + ".jpg";
            images.add(Highgui.imread(path, Highgui.CV_LOAD_IMAGE_GRAYSCALE));
        }

        return images;
    }

    private static Double compareTwoImages(Mat image1, Mat image2) {
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
        return Core.norm(mat1, mat2, Core.NORM_L2);
    }

    private static ArrayList<Mat> createGaborMats(Mat image) {
        ArrayList<Mat> gabors = new ArrayList<Mat>();

        //predefine parameters for Gabor kernel
        List<Double> thetas = Arrays.asList(0.0, 23.0, 45.0, 68.0, 90.0, 113.0, 135.0, 158.0);
        List<Double> lambdas = Arrays.asList(3.0, 6.0, 13.0, 28.0, 58.0);
        Size kSize = new Size(5, 5);

        double sigma = 20;
        double gamma = 0.5;
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
        List<Integer> xes = Arrays.asList(0, 30, 60, 90, 120, 150, 180, 210, 239);
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

    private static void getStatistics(List<Double> list) {
        System.out.println("Average: " + list.stream().mapToDouble(d -> d).average().getAsDouble());
        System.out.println("Max: " + list.stream().max((d1, d2) -> Double.compare(d1, d2)).get());
        System.out.println("Min: " + list.stream().min((d1, d2) -> Double.compare(d1, d2)).get());
    }
}
