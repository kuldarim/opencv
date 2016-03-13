import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;


public class App {

    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    public static void main(String[] args) {
        System.out.println("Welcome to OpenCV " + Core.VERSION);

        Mat image = Highgui.imread("src/main/resources/cat.jpeg", Highgui.CV_LOAD_IMAGE_GRAYSCALE);

        Highgui.imwrite("src/main/resources/gray_cat.jpeg", image);
    }
}
