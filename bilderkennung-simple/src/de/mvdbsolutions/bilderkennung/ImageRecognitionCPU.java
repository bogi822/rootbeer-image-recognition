package de.mvdbsolutions.bilderkennung;

import com.labun.surf.InterestPoint;
import com.labun.surf.Matcher;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bogi on 02.03.14.
 */
public class ImageRecognitionCPU {

    public void start(){

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

        int numberOfInterestPointsDatabase = 100000;
        List<InterestPoint> interestPointsDatabase = new ArrayList<InterestPoint>();
        for(int i = 0; i < numberOfInterestPointsDatabase; i++){
            interestPointsDatabase.add(new InterestPoint(0, 0, 0.3f, 0.4f, 0.5f, descriptors));
        }

        for(int i = 0; i < 5; i++){
            long start = System.currentTimeMillis();
            System.out.println("Number of matches: " + Matcher.bruteForceMatching(interestPoints, interestPointsDatabase));
            long end = System.currentTimeMillis();
            System.out.println("Finished after: " + (end - start) + "ms");
        }
    }

    public static void main(String [] args){
        new ImageRecognitionCPU().start();
    }
}
