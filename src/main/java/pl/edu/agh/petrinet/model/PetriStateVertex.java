package pl.edu.agh.petrinet.model;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by rakiop on 26.04.15.
 */
public class PetriStateVertex {

    private int[] placeStatus;

    private int level;

    private List<Integer> route;


    public PetriStateVertex(int[] status, int level){
        this.placeStatus = status;
        this.level = level;
        route = new LinkedList<>();
    }

    public PetriStateVertex(int[] status, int level, List<Integer> route){
        this.placeStatus = status;
        this.level = level;
        this.route = new LinkedList<>();

        for(Integer id : route){
            this.route.add(id);
        }
    }

    public int getLevel(){
        return this.level;
    }

    public int getPlaceMarksCount(int i){
        return placeStatus[i];
    }

    public int[] getPlaceMarksCounts(){
        return placeStatus;
    }

    public void addToRoute(int tId){
        route.add(tId);
    }

    public List<Integer> getRoute(){
        return route;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof PetriStateVertex == false)
            return false;

        int[] state = ((PetriStateVertex) obj).getPlaceMarksCounts();
        for(int i =0; i < placeStatus.length; i++){
            if(state[i] != placeStatus[i])
                return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return Arrays.toString(this.placeStatus);
    }
}
