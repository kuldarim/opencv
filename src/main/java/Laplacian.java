import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public class Laplacian {
    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    public static void main( String[] args ){

        try {
            System.loadLibrary( Core.NATIVE_LIBRARY_NAME );

            Mat source = Highgui.imread("src/main/resources/test.jpg",  Highgui.CV_LOAD_IMAGE_GRAYSCALE);

            Imgproc.Laplacian(source, source, source.depth());

            Highgui.imwrite("src/main/resources/laplacian.jpg", source);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
