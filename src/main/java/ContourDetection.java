import org.opencv.core.*;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class ContourDetection {

    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    public static void main( String[] args ) {

        // Consider the image for processing
        Mat image = Highgui.imread("src/main/resources/car.png", Imgproc.COLOR_BGR2GRAY);
//        Mat imageHSV = new Mat(image.size(), Core.DEPTH_MASK_8U);
//        Mat imageBlurr = new Mat(image.size(), Core.DEPTH_MASK_8U);
//        Mat imageA = new Mat(image.size(), Core.DEPTH_MASK_ALL);
//        Imgproc.cvtColor(image, imageHSV, Imgproc.COLOR_BGR2GRAY);
//        Imgproc.GaussianBlur(imageHSV, imageBlurr, new Size(5,5), 0);
//        Imgproc.adaptiveThreshold(imageBlurr, imageA, 255,Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY,7, 5);
//
//        Highgui.imwrite("src/main/resources/contour1.jpg",imageBlurr);
//
//        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
//
//        Imgproc.findContours(imageA, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
//
//        Imgproc.drawContours(imageBlurr, contours, 1, new Scalar(0,0,255));
//
//        Highgui.imwrite("src/main/resources/contour2.jpg",image);

        final Mat dst = new Mat(image.rows(), image.cols(), image.type());
        image.copyTo(dst);

        Imgproc.cvtColor(dst, dst, Imgproc.COLOR_BGR2GRAY);

        final List<MatOfPoint> points = new ArrayList<MatOfPoint>();
        final Mat hierarchy = new Mat();
        Imgproc.findContours(dst, points, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        Imgproc.cvtColor(dst, dst, Imgproc.COLOR_GRAY2BGR);

        Highgui.imwrite("src/main/resources/contour1.jpg", dst);
    }
}
