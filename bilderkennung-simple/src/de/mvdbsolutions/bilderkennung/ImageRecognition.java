package de.mvdbsolutions.bilderkennung;

import com.labun.surf.InterestPoint;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by bogi on 23.02.14.
 */
public class ImageRecognition {

    private static Map<String, List<InterestPoint>> imageDatabase = new ConcurrentHashMap<String, List<InterestPoint>>();
    private static List<String> indexToImagename = Collections.synchronizedList(new ArrayList<String>());

    public void loadImagesFromFilesystem(String path){
        Iterator<File> iterator = FileUtils.iterateFiles(new File(path), null, false);
        long interestPointsDatabase = 0;
        while(iterator.hasNext()){
            File datei = iterator.next();
                List<InterestPoint> interestPoints = InterestPoint.loadFromFile(datei.getAbsolutePath());
            interestPointsDatabase += interestPoints.size();
            imageDatabase.put(datei.getName(), interestPoints);
            indexToImagename.add(datei.getName());
            System.out.println("Image: " + datei.getName() + " loaded with " + interestPoints.size() + " Interest-Points.");
        }
        System.out.println("Loaded all images with a total number of : " + interestPointsDatabase + " Interest-Points");
    }

    public int getTotalNumberInterestPoints(){
        int totalNumber = 0;
        for(String bildname : imageDatabase.keySet()){
            List<InterestPoint> interestPoints = imageDatabase.get(bildname);
            totalNumber += interestPoints.size();
        }
        return totalNumber;
    }

    public Map<String, List<InterestPoint>> getImageDatabase() {
        return imageDatabase;
    }
}
