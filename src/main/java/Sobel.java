import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public class Sobel {
    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    public static void main(String[] args) {
        System.out.println("Welcome to OpenCV " + Core.VERSION);

        try {
            System.loadLibrary( Core.NATIVE_LIBRARY_NAME );

            Mat source = Highgui.imread("src/main/resources/test.jpg",  Highgui.CV_LOAD_IMAGE_GRAYSCALE);

            Imgproc.Sobel(source, source, source.depth(), 2, 2);

            Highgui.imwrite("src/main/resources/sobel.jpg", source);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
