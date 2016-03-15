import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public class CannyEdgeDetection {

    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    public static void main( String[] args ){

        try {
            System.loadLibrary( Core.NATIVE_LIBRARY_NAME );

            Mat source = Highgui.imread("src/main/resources/test.jpg", Highgui.CV_LOAD_IMAGE_GRAYSCALE);

            Mat result = new Mat();

            Imgproc.Canny(source, result, 200.0, 200.0);

            Highgui.imwrite("src/main/resources/canny.jpg", result);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
