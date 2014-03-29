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

    private ImageRecognition imageRecognition = new ImageRecognition();

    public void start(String pathQueryImage, String pathImageDatabase, int numberOfGpuThreads, int numberOfInterestPointsDatabase){

//        // Load Interest-Points from query image
//        List<InterestPoint> interestPointsSuchBild = InterestPoint.loadFromFile(pathQueryImage);
//        System.out.println("Loaded the query-image with: " + interestPointsSuchBild.size() + " number of interest-points.");
//
//        List<InterestPoint> interestPointsDatenbankBild = InterestPoint.loadFromFile(pathImageDatabase);
//        System.out.println("Loaded the database-image with: " + interestPointsDatenbankBild.size() + " number of interest-points.");
//
//        List<List<InterestPoint>> interestPointListen = new ArrayList<List<InterestPoint>>(numberOfGpuThreads);
//        for(int i = 0; i < numberOfGpuThreads; i++){
//            interestPointListen.add(new ArrayList<InterestPoint>());
//        }
//
//        int index = 0;
//        for(InterestPoint interestPoint : interestPointsDatenbankBild){
//            int aktuelleListenIndex = index % numberOfGpuThreads;
//            interestPointListen.get(aktuelleListenIndex).add(interestPoint);
//            index++;
//        }
//
//        // Start the computation
//        List<Kernel> jobs = new ArrayList<Kernel>();
//        for(index = 0; index < numberOfGpuThreads; index++){
//            ImageRecognitionGPUKernel imageRecognitionGPUKernel = new ImageRecognitionGPUKernel(interestPointsSuchBild.toArray(new InterestPoint[]{}),
//                    interestPointListen.get(index).toArray(new InterestPoint[]{}), index);
//            jobs.add(imageRecognitionGPUKernel);
//        }
//
//        Rootbeer rootbeer = new Rootbeer();
//        long start = System.currentTimeMillis();
//        Context context = rootbeer.createDefaultContext();
//        rootbeer.run(jobs, context);
//        long end = System.currentTimeMillis();
//        System.out.println("Finished after: " + (end - start) + "ms");
//
//        for(StatsRow statsRow : context.getStats()){
//            System.out.println("Overall-Time: " + statsRow.getOverallTime());
//            System.out.println("Execution-Time: " + statsRow.getExecutionTime());
//            System.out.println("Serial-Time: " + statsRow.getSerializationTime());
//            System.out.println("Deserial-Time: " + statsRow.getDeserializationTime());
//            System.out.println("---------------------------------------------------------------");
//        }

//        // Load Interest-Points from query image
//        List<InterestPoint> interestPointsSuchBild = InterestPoint.loadFromFile(pathQueryImage);
//        System.out.println("Loaded the query-image!");
//
//        // Load database images
//        imageRecognition.loadImagesFromFilesystem(pathImageDatabase);
//        System.out.println("Loaded the image-database!");
//
//        // Determine the size of the working-packages for each kernel / thread
//        int packgeSize;
//        if(imageRecognition.getTotalNumberInterestPoints() % numberOfGpuThreads == 0){
//            packgeSize = imageRecognition.getTotalNumberInterestPoints() / numberOfGpuThreads;
//        } else {
//            packgeSize = (imageRecognition.getTotalNumberInterestPoints() / numberOfGpuThreads) + 1;
//        }
//
//        // Create the work-package with the determined package-size
//        final List<InterestPointWorkPackage> workingPackages = new ArrayList<InterestPointWorkPackage>();
//        workingPackages.add(new InterestPointWorkPackage(packgeSize));
//        int currentWorkingPackage = 0;
//        for(String imageName : imageRecognition.getImageDatabase().keySet()){
//            List<InterestPoint> interestPoints = imageRecognition.getImageDatabase().get(imageName);
//            for(InterestPoint interestPoint : interestPoints){
//
//                // Get the current working-package
//                InterestPointWorkPackage interestPointArbeitspaket = workingPackages.get(currentWorkingPackage);
//                interestPointArbeitspaket.addInterestPoint(imageName, interestPoint);
//
//                // If the current working-package is full, increase the index
//                if(interestPointArbeitspaket.isWorkingPackageFull()){
//                    workingPackages.add(new InterestPointWorkPackage(packgeSize));
//                    currentWorkingPackage++;
//                }
//            }
//        }
//        System.out.println("Created working packages!");
//
//        // Check if the last working-package is empty. If its empty - delete it
//        if(workingPackages.get(workingPackages.size() - 1).getInterestPointsLists().size() == 0){
//            workingPackages.remove(workingPackages.size() - 1);
//        }
//
//        // Migrate the interest-points from list to array
//        final InterestPoint [] interestPointsSuchBildArray = interestPointsSuchBild.toArray(new InterestPoint[]{});
//
//        // Create different kernel-jobs and provide the interest-points of the query-picture and
//        // the interest-points of the database
//        List<Kernel> jobs = new ArrayList<Kernel>();
//        for(int index = 0; index < numberOfGpuThreads; index++){
//
//            // Get the working-package for the current index
//            InterestPoint [][] interestPointsDatenbankBilder = workingPackages.get(index).getInterestPointListAsArray();
//
//            // Create the kernel / job
//            ImageRecognitionGPUKernel imageRecognitionGPUKernel = new ImageRecognitionGPUKernel(interestPointsSuchBildArray, interestPointsDatenbankBilder[0], index);
//            jobs.add(imageRecognitionGPUKernel);
//        }
//        System.out.println("Created the kernels!");
//
//        // Start the computation
//        long start = System.currentTimeMillis();
//        Rootbeer rootbeer = new Rootbeer();
//        rootbeer.run(jobs);
//        long end = System.currentTimeMillis();
//        System.out.println("Finished after: " + (end - start) + "ms");


        // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! IMPORTANT !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

        // Below comes the same but with sample data. And below works better. Its faster and don't crashes.

//        // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! IMPORTANT !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//        // Just put sample-data into the interest-point
//        float [] descriptors = new float[64];
//        for(int b = 0; b < 64; b++){
//            descriptors[b] = 0.0000002f;
//        }
//
//        int numberOfInterestPoints = 4000;
//        List<InterestPoint> interestPoints = new ArrayList<InterestPoint>();
//        for(int i = 0; i < numberOfInterestPoints; i++){
//            interestPoints.add(new InterestPoint(0, 0, 0.003f, 0.004f, 0.0005f, descriptors));
//        }
//
//        List<InterestPoint> interestPointsDatabase = new ArrayList<InterestPoint>();
//        for(int i = 0; i < numberOfInterestPointsDatabase; i++){
//            interestPointsDatabase.add(new InterestPoint(0, 0, 0.3f, 0.4f, 0.5f, descriptors));
//        }
//
//        List<Kernel> jobs = new ArrayList<Kernel>();
//        for(int i = 0; i < numberOfGpuThreads; i++){
//            ImageRecognitionGPUKernel imageRecognitionGPUKernel = new ImageRecognitionGPUKernel(interestPoints.toArray(new InterestPoint[]{}), interestPointsDatabase.toArray(new InterestPoint[]{}), i);
//            jobs.add(imageRecognitionGPUKernel);
//        }
//
//        Rootbeer rootbeer = new Rootbeer();
//        long start = System.currentTimeMillis();
//        Context context = rootbeer.createDefaultContext();
//        rootbeer.run(jobs, context);
//        long end = System.currentTimeMillis();
//        System.out.println("Finished after: " + (end - start) + "ms");
//        for(StatsRow statsRow : context.getStats()){
//            System.out.println("Overall-Time: " + statsRow.getOverallTime());
//            System.out.println("Execution-Time: " + statsRow.getExecutionTime());
//            System.out.println("Serial-Time: " + statsRow.getSerializationTime());
//            System.out.println("Deserial-Time: " + statsRow.getDeserializationTime());
//            System.out.println("---------------------------------------------------------------");
//        }
    }

    public static void main(String [] args){
        new ImageRecognitionGPU().start(args[0], args[1], Integer.valueOf(args[2]), Integer.valueOf(args[3]));
    }
}
