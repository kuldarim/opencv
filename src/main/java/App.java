import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import java.util.ArrayList;


public class App {

    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    private final static String DAY = "DAY";
    private final static String NIGHT = "NIGHT";

    private static ArrayList<Double> nightValues, dayValues;
    private static Double nightMean, dayMean, dayDispersion, nightDispersion;

    public static void main(String[] args) {
        System.out.println("Welcome to OpenCV " + Core.VERSION);

        nightValues = new ArrayList<Double>();
        dayValues = new ArrayList<Double>();

        getArrayOfPixelColorValues(DAY);
        getArrayOfPixelColorValues(NIGHT);

        nightMean = calculateMean(nightValues);
        dayMean = calculateMean(dayValues);

        System.out.println(nightMean);
        System.out.println(dayMean);

        dayDispersion = calculateDispersion(dayValues, dayMean);
        nightDispersion = calculateDispersion(nightValues, nightMean);

        System.out.println(dayDispersion);
        System.out.println(nightDispersion);

    }

    private static void getArrayOfPixelColorValues(String type) {
        String path = "";
        ArrayList<Double> array = null;
        if (type.equals(DAY)) {
            path = "day/d";
            array = dayValues;
        } else if (type.equals(NIGHT)) {
            path = "night/n";
            array = nightValues;
        }

        for (int i = 1; i <= 10; i++) {
            Mat image = Highgui.imread("src/main/resources/" + path + i + ".jpg", Highgui.CV_LOAD_IMAGE_GRAYSCALE);
            for (Double d : image.get(300,600)) {
                array.add(d);
                System.out.println(d);
            }
        }
    }

    private static Double calculateMean(ArrayList<Double> list) {
        Double average = 0.0;
        for (Double v : list) {
            average = average + v;
        }

        return average / list.size();
    }
    
    private static Double calculateDispersion(ArrayList<Double> list, Double mean) {
        Double dispersion = 0.0;
        for (Double v : list) {
            dispersion = dispersion + Math.pow(mean - v, 2);
        }

        return dispersion / list.size();
    }
}
