import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import java.util.ArrayList;


public class App {

    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    private final static String DAY = "DAY";
    private final static String NIGHT = "NIGHT";
    private final static int X = 300;
    private final static int Y = 600;

    private static ArrayList<Double> nightValues, dayValues;
    private static Double nightMean, dayMean, dayVariance, nightVariance;

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

        dayVariance = calculateVariance(dayValues, dayMean);
        nightVariance = calculateVariance(nightValues, nightMean);

        System.out.println(dayVariance);
        System.out.println(nightVariance);

        Mat image = Highgui.imread("src/main/resources/night/n7.jpg", Highgui.CV_LOAD_IMAGE_GRAYSCALE);
        Double x = 0.0;
        for (Double d : image.get(X, Y)) {
            x = d;
        }

        Double nightGaussian = calculateGaussian(x, dayMean, dayVariance);
        Double dayGaussian = calculateGaussian(x, nightMean, nightVariance);

        System.out.println(nightGaussian > dayGaussian ? "\nNIGHT" : "\nDAY");

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
            for (Double d : image.get(X, Y)) {
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
    
    private static Double calculateVariance(ArrayList<Double> list, Double mean) {
        Double Variance = 0.0;
        for (Double v : list) {
            Variance = Variance + Math.pow(mean - v, 2);
        }

        return Variance / list.size();
    }

    private static double calculateGaussian(Double x, Double mean, double variance) {
        return 1 / (variance * Math.sqrt(2 * Math.PI)) * Math.pow(Math.E, (- 1/2) * Math.pow((x - mean)/variance, 2));
    }
}
