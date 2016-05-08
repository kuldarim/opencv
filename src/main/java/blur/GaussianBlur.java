package blur;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public class GaussianBlur {


    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    public static void main( String[] args ){

        try {
            System.loadLibrary( Core.NATIVE_LIBRARY_NAME );

            Mat source = Highgui.imread("src/main/resources/blur/source.jpg", Highgui.CV_LOAD_IMAGE_GRAYSCALE);

            Mat result = new Mat();

           // Imgproc.GaussianBlur(source, result, new Size(15.0, 15.0), 15.0, 1.0);

            Imgproc.blur(source, result, new Size(15.0, 15.0));

            //Highgui.imwrite("src/main/resources/blur/gaussian.jpg", result);
            Highgui.imwrite("src/main/resources/blur/blur.jpg", result);



        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

}
