package de.mvdbsolutions.bilderkennung;

import com.labun.surf.InterestPoint;
import org.trifort.rootbeer.runtime.Context;
import org.trifort.rootbeer.runtime.Kernel;
import org.trifort.rootbeer.runtime.Rootbeer;
import org.trifort.rootbeer.runtime.StatsRow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bogi on 02.03.14.
 */
public class ImageRecognitionGPU {

    public void start(int numberOfGpuThreads, int numberOfInterestPointsPerWorkingPackage){

        // Just put sample-data into the interest-point
        float [] descriptors = new float[64];
        for(int b = 0; b < 64; b++){
            descriptors[b] = 0.0000002f;
        }

        int numberOfInterestPoints = 4000;
        List<InterestPoint> interestPoints = new ArrayList<InterestPoint>();
        for(int i = 0; i < numberOfInterestPoints; i++){
            interestPoints.add(new InterestPoint(0, 0, 0.003f, 0.004f, 0.0005f, descriptors));
        }

        List<InterestPoint> interestPointsDatabase = new ArrayList<InterestPoint>();
        for(int i = 0; i < numberOfInterestPointsPerWorkingPackage; i++){
            interestPointsDatabase.add(new InterestPoint(0, 0, 0.3f, 0.4f, 0.5f, descriptors));
        }

        List<Kernel> jobs = new ArrayList<Kernel>();
        for(int i = 0; i < numberOfGpuThreads; i++){
            ImageRecognitionGPUKernel imageRecognitionGPUKernel = new ImageRecognitionGPUKernel(interestPoints.toArray(new InterestPoint[]{}), interestPointsDatabase.toArray(new InterestPoint[]{}), i);
            jobs.add(imageRecognitionGPUKernel);
        }

        Rootbeer rootbeer = new Rootbeer();
        long start = System.currentTimeMillis();
        Context context = rootbeer.createDefaultContext();
        rootbeer.run(jobs, context);
        long end = System.currentTimeMillis();
        System.out.println("Finished after: " + (end - start) + "ms");

        for(StatsRow statsRow : context.getStats()){
            System.out.println("Overall-Time: " + statsRow.getOverallTime());
            System.out.println("Execution-Time: " + statsRow.getExecutionTime());
            System.out.println("Serial-Time: " + statsRow.getSerializationTime());
            System.out.println("Deserial-Time: " + statsRow.getDeserializationTime());
            System.out.println("---------------------------------------------------------------");
        }
    }

    public static void main(String [] args){
        new ImageRecognitionGPU().start(Integer.valueOf(args[0]), Integer.valueOf(args[1]));
    }
}
