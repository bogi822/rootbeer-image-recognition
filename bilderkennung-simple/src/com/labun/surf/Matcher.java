package com.labun.surf;

import java.util.List;

public class Matcher {

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
