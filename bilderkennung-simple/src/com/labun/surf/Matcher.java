package com.labun.surf;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Matcher {

    /**
     * Finds matching points using the sign of laplacian and a linear nearest
     * neighbor search.
     */
    public static int findMathes(InterestPoint [] ipts1, InterestPoint [] ipts2) {
        float distance, bestDistance, secondBest;
        com.labun.surf.InterestPoint bestMatch;
        int descSize = 64; // TODO: use parameter
        float delta;
        float[] v1, v2;
        int numberOfMatches = 0;

        for (InterestPoint p1 : ipts1) {
            bestDistance = secondBest = Float.MAX_VALUE;
            bestMatch = null;

            for (InterestPoint p2 : ipts2) {

                // (NB: There is no check fo sign of laplacian in OpenSURF)
                if (p1.sign == p2.sign){

                    // Compare descriptors (based on calculating of squared distance between two vectors)
                    distance = 0;
                    v1 = p1.descriptor;
                    v2 = p2.descriptor;
                    boolean doBreak = false;
                    for (int i = 0; i < descSize; i++) {
                        delta = v1[i] - v2[i];
                        distance += delta * delta;
                        if (distance >= secondBest) {
                            doBreak = true;
                            break;
                        }
                    }

                    if(doBreak){
                        continue;
                    }

                    if (distance < bestDistance) {
                        secondBest = bestDistance;
                        bestDistance = distance;
                        bestMatch = p2;
                    } else { // distance < secondBest
                        secondBest = distance;
                    }

                    // Threshold values in other implementations:
                    // OpenSURF:                    0.65
                    // OpenCV-2.0.0 (find_obj.cpp): 0.6
                    // Orig. SURF:                  0.5
                    if (bestDistance < 0.6f * secondBest) {

                        // Matching point found.
                        numberOfMatches++;
                        // Store the change in position (p1 -> p2) into the
                        // matchingPoint:
                        bestMatch.dx = bestMatch.x - p1.x;
                        bestMatch.dy = bestMatch.y - p1.y;
                    }
                }
            }
        }
        return numberOfMatches;
    }

    /**
     * Finds matching points using the sign of laplacian and a linear nearest
     * neighbor search.
     */
    public static Map<InterestPoint, InterestPoint> findMathes(List<InterestPoint> ipts1, List<InterestPoint> ipts2) {

        System.out.println("Vergleiche: " + ipts1.size() + " mit " + ipts2.size());
        System.out.println("N x M: " + ipts1.size() * ipts2.size());

        Map<InterestPoint, InterestPoint> res = new HashMap<InterestPoint, InterestPoint>();
        float distance, bestDistance, secondBest, delta;
        InterestPoint bestMatch;
        long anzahlVergleiche = 0;
        long anzahlContinue = 0;
        long uebersprungen = 0;
        long anzahlErsteForLoop = 0;
        long anzahlZweiteForLoop = 0;
        long anzahlEquals = 0;
        float[] v1, v2;

        for (InterestPoint p1 : ipts1) {
            anzahlErsteForLoop++;
            bestDistance = secondBest = Float.MAX_VALUE;
            bestMatch = null;

            ipts2Loop: for (InterestPoint p2 : ipts2) {
                anzahlZweiteForLoop++;
                if (p1.sign == p2.sign){
                    anzahlEquals++;
                    distance = 0;
                    v1 = p1.descriptor;
                    v2 = p2.descriptor;
                    for (int i = 0; i < 64; i++) {
                        delta = v1[i] - v2[i];
                        distance += delta * delta;
                        anzahlVergleiche++;
                        if (distance >= secondBest) {
                            anzahlContinue++;
                            continue ipts2Loop;
                        }
                    }
                    if (distance < bestDistance) {
                        secondBest = bestDistance;
                        bestDistance = distance;
                        bestMatch = p2;
                    } else {
                        secondBest = distance;
                    }
                } else {
                    uebersprungen++;
                }
            }

            if (bestDistance < 0.6f * secondBest) {
                res.put(p1, bestMatch);
                bestMatch.dx = bestMatch.x - p1.x;
                bestMatch.dy = bestMatch.y - p1.y;
            }
        }
        System.out.println("Anzahl Vergleiche: " + anzahlVergleiche);
        System.out.println("Anzahl Continue: " + anzahlContinue);
        System.out.println("Anzahl übersprungen: " + uebersprungen);
        System.out.println("Anzahl 1. For-Loop: " + anzahlErsteForLoop);
        System.out.println("Anzahl 2. For-Loop: " + anzahlZweiteForLoop);
        System.out.println("Anzahl Equals: " + anzahlEquals);
        return res;
    }

    /**
     * Brute-Force.
     */
    public static int bruteForceMatching(List<InterestPoint> ipts1, List<InterestPoint> ipts2) {
        int anzahlMatches = 0;
        float distance, delta;
        float[] v1, v2;

        for (InterestPoint p1 : ipts1) {
            float bestDistance = Float.MAX_VALUE;
            float secondBestDistance = Float.MAX_VALUE;
            for (InterestPoint p2 : ipts2) {
                if (p1.sign == p2.sign){
                    distance = 0;
                    v1 = p1.descriptor;
                    v2 = p2.descriptor;
                    for (int i = 0; i < 64; i++) {
                        delta = v1[i] - v2[i];
                        distance += delta * delta;
                    }
                    if(distance < bestDistance){
                        secondBestDistance = bestDistance;
                        bestDistance = distance;
                    }
                }
            }

            // Ermittle die beste und die 2. beste distanz
            if (bestDistance < 0.6f * secondBestDistance) {
                anzahlMatches++;
            }
        }
        return anzahlMatches;
    }

    /**
     * Brute-Force.
     */
    public static int bruteForceMatching(InterestPoint [] ipts1, InterestPoint [] ipts2) {
        int anzahlMatches = 0;
        float distance, delta;
        float[] v1, v2;

        for (InterestPoint p1 : ipts1) {
            float bestDistance = Float.MAX_VALUE;
            float secondBestDistance = Float.MAX_VALUE;
            for (InterestPoint p2 : ipts2) {
                if (p1.sign == p2.sign){
                    distance = 0;
                    v1 = p1.descriptor;
                    v2 = p2.descriptor;
                    for (int i = 0; i < 64; i++) {
                        delta = v1[i] - v2[i];
                        distance += delta * delta;
                    }
                    if(distance < bestDistance){
                        secondBestDistance = bestDistance;
                        bestDistance = distance;
                    }
                }
            }

            // Ermittle die beste und die 2. beste distanz
            if (bestDistance < 0.6f * secondBestDistance) {
                anzahlMatches++;
            }
        }
        return anzahlMatches;
    }
}
