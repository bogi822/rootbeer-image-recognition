package de.mvdbsolutions.bilderkennung;

import com.labun.surf.InterestPoint;
import com.labun.surf.Matcher;
import org.trifort.rootbeer.runtime.Kernel;
import org.trifort.rootbeer.runtime.Rootbeer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by bogi on 02.03.14.
 */
public class ImageRecognitionCPU {

    private ImageRecognition imageRecognition = new ImageRecognition();

    public void start(String pathQueryImage, String pathImageDatabase){

//        // Load Interest-Points from query image
//        List<InterestPoint> interestPointsSuchBild = InterestPoint.loadFromFile(pathQueryImage);
//        System.out.println("Loaded the query-image with: " + interestPointsSuchBild.size() + " number of interest-points.");
//
//        // Load database images
//        imageRecognition.loadImagesFromFilesystem(pathImageDatabase);
//        System.out.println("Loaded the image-database!");
//
//        // Start the computation
//        long start = System.currentTimeMillis();
//        for(String imageName : imageRecognition.getImageDatabase().keySet()){
//            List<InterestPoint> interestPointsDatabaseImage = imageRecognition.getImageDatabase().get(imageName);
//            System.out.println("");
//            System.out.println("-----------------------------------------------------------------");
//            System.out.println("Matching query-image with " + imageName + ". Number of matches: " + Matcher.bruteForceMatching(interestPointsSuchBild, interestPointsDatabaseImage));
//            System.out.println("-----------------------------------------------------------------");
//            System.out.println("");
//        }
//
////        System.out.println("Number of matches: " + Matcher.findMathes(interestPointsSuchBild, imageRecognition.getImageDatabase().get("baum16.jpg.txt")).size()); // 1.046.282.819 bei 16.352 ip db bild
//
//
//        long end = System.currentTimeMillis();
//        System.out.println("Finished after: " + (end - start) + "ms");


        // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! IMPORTANT !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

        // Below comes the same but with sample data. And below works better. Its faster and don't crashes.

        // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! IMPORTANT !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

        // Just put sample-data into the interest-point
        float [] descriptors = new float[64];
        for(int b = 0; b < 64; b++){
            descriptors[b] = 0.0000002f;
        }

        int numberOfInterestPoints = 4000;
        final List<InterestPoint> interestPoints = new ArrayList<InterestPoint>();
        for(int i = 0; i < numberOfInterestPoints; i++){
            interestPoints.add(new InterestPoint(0, 0, 0.003f, 0.004f, 0.0005f, descriptors));
        }

        int anzahlThreads = 4;

        int numberOfInterestPointsDatabase = 24000;
        List<InterestPoint> interestPointsDatabase = new ArrayList<InterestPoint>();
        for(int i = 0; i < numberOfInterestPointsDatabase; i++){
            interestPointsDatabase.add(new InterestPoint(0, 0, 0.3f, 0.4f, 0.5f, descriptors));
        }

//        final List<List<InterestPoint>> interestPointsListe = new ArrayList<List<InterestPoint>>();
//        for(int i = 0; i < anzahlThreads; i++){
//            interestPointsListe.add(new ArrayList<InterestPoint>());
//            interestPointsListe.get(i).addAll(interestPointsDatabase);
//        }

//        for(int a = 0; a < 5; a++){
//        long start = System.currentTimeMillis();
//        final AtomicInteger index = new AtomicInteger(0);
//        final AtomicInteger anzahlFertigerThreads = new AtomicInteger(0);
//        for(int i = 0; i < anzahlThreads; i++){
//            new Thread(){
//                @Override
//                public void run() {
//                    System.out.println("Number of matches: " + Matcher.bruteForceMatching(interestPoints, interestPointsListe.get(index.getAndIncrement())));
//                    anzahlFertigerThreads.incrementAndGet();
//                }
//            }.start();
//        }
//
//        while(anzahlFertigerThreads.get() != anzahlThreads){
//            try {
//                Thread.currentThread().sleep(50);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//            long end = System.currentTimeMillis();
//            System.out.println("Finished after: " + (end - start) + "ms");
//        }

        for(int i = 0; i < 5; i++){
            long start = System.currentTimeMillis();
            System.out.println("Number of matches: " + Matcher.bruteForceMatching(interestPoints, interestPointsDatabase));
            long end = System.currentTimeMillis();
            System.out.println("Finished after: " + (end - start) + "ms");
        }
    }

    public static void main(String [] args){
        new ImageRecognitionCPU().start(args[0], args[1]);
    }
}
