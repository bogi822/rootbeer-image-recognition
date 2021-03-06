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
    private int result;

    public ImageRecognitionGPUKernel(InterestPoint[] interestPointsQueryImage, InterestPoint[] interestPointsDatabase, int index){
      this.interestPointsQueryImage = interestPointsQueryImage;
      this.interestPointsDatabase = interestPointsDatabase;
      this.index = index;
    }

    public void gpuMethod() {
      result = Matcher.bruteForceMatching(interestPointsQueryImage, interestPointsDatabase) + 1;
    }
    
    public int getResult(){
      return result;
    }
}
