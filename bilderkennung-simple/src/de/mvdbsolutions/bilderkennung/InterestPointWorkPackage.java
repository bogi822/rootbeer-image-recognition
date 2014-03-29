package de.mvdbsolutions.bilderkennung;

import com.labun.surf.InterestPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bogi on 02.03.14.
 */
public class InterestPointWorkPackage {
    private List<List<InterestPoint>> interestPointsListen = new ArrayList<List<InterestPoint>>();
    private List<String> bilder = new ArrayList<String>();
    private int maximaleGroesseArbeitspaket = 0;
    private int aktuelleGroesseArbeitspaket = 0;
    private int aktuellerListenIndex = -1;

    public InterestPointWorkPackage(int maximaleGroesseArbeitspaket) {
        this.maximaleGroesseArbeitspaket = maximaleGroesseArbeitspaket;
    }

    public void addInterestPoint(String bildname, InterestPoint interestPoint){
        if(!bilder.contains(bildname)){
            bilder.add(bildname);
            aktuellerListenIndex++;
            interestPointsListen.add(new ArrayList<InterestPoint>());
        }
        interestPointsListen.get(aktuellerListenIndex).add(interestPoint);
        aktuelleGroesseArbeitspaket++;
    }

    public InterestPoint [][] getInterestPointListAsArray(){
        InterestPoint [][] interestPoints = new InterestPoint[interestPointsListen.size()][];
        int index = 0;
        for(List<InterestPoint> interestPointListe : interestPointsListen){
            interestPoints[index] = new InterestPoint[interestPointListe.size()];
            int arrayIndex = 0;
            for(InterestPoint interestPoint : interestPointListe){
                interestPoints[index][arrayIndex] = interestPoint;
                arrayIndex++;
            }
            index++;
        }
        return interestPoints;
    }

    public boolean isWorkingPackageFull(){
        return maximaleGroesseArbeitspaket == aktuelleGroesseArbeitspaket;
    }

    public List<List<InterestPoint>> getInterestPointsLists() {
        return interestPointsListen;
    }

    public List<String> getImages() {
        return bilder;
    }
}
