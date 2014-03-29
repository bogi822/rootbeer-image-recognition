package de.mvdbsolutions.bilderkennung;

import com.labun.surf.InterestPoint;
import com.labun.surf.Matcher;
import org.trifort.rootbeer.runtime.Kernel;

/**
 * Created by bogi on 02.03.14.
 */
public class ImageRecognitionGPUKernel implements Kernel {

    private InterestPoint [] interestPointsQueryImage;
    private InterestPoint [] interestPointsDatabase;
    private int index;

    public ImageRecognitionGPUKernel(InterestPoint[] interestPointsQueryImage, InterestPoint[] interestPointsDatabase, int index){
        this.interestPointsQueryImage = interestPointsQueryImage;
        this.interestPointsDatabase = interestPointsDatabase;
        this.index = index;
    }

    public void gpuMethod() {
        int anzahlMatches = 0;
        float distance, delta;
        float[] v1, v2;

        for (InterestPoint p1 : interestPointsQueryImage) {
            float bestDistance = Float.MAX_VALUE;
            float secondBestDistance = Float.MAX_VALUE;
            for (InterestPoint p2 : interestPointsDatabase) {
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
    }
}
