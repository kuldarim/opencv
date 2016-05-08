package facedetection;

import org.opencv.core.*;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.awt.EventQueue;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class MyFrame extends JFrame {
    private JPanel contentPane;

    CascadeClassifier faceDetector = new CascadeClassifier(
            MyFrame
                    .class
                    .getResource("/haars/lbpcascade_frontalface.xml")
                    .getPath()
                    .substring(1));

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MyFrame frame = new MyFrame();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public MyFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 650, 490);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        new MyThread().start();
    }

    VideoCap videoCap = new VideoCap();

    public void paint(Graphics g) {
//        g = contentPane.getGraphics();
//        g.drawImage(videoCap.getOneFrame(), 0, 0, this);
//        Highgui.imwrite("src/main/resources/source.jpg", videoCap.getMat());
//        MatOfRect faceDetections = new MatOfRect();
//
//        faceDetector.detectMultiScale(videoCap.getMat(), faceDetections);
//
//        for (Rect rect : faceDetections.toArray()) {
//            System.out.println("ttt");
//        }

        g = contentPane.getGraphics();
        g.drawImage(videoCap.getOneFrame(), 0, 0, this);

        Mat mRgba = new Mat();
        Mat mGrey = new Mat();
        MatOfRect faces = new MatOfRect();
        Mat input = videoCap.getMat();
        input.copyTo(mRgba);
        input.copyTo(mGrey);
        Imgproc.cvtColor(mRgba, mGrey, Imgproc.COLOR_BGR2GRAY);
        Imgproc.equalizeHist(mGrey, mGrey);
        faceDetector.detectMultiScale(mGrey, faces);

        for (Rect rect : faces.toArray()) {
            System.out.println("ttt");
        }
    }

    class MyThread extends Thread {
        @Override
        public void run() {
            for (;;) {
                repaint();
                try { Thread.sleep(30);
                } catch (InterruptedException e) {    }
            }
        }
    }
}
