package pl.edu.agh.petrinet.model;

/**
 * Created by Tomasz on 4/19/2015.
 */
public class PetriPlace extends PetriVertex {

    public PetriPlace(int id){
        super(id);
    }

    public PetriPlace(int id, String name){
       super(id, name);
    }

    public String toString(){
        return "P" + id;
    }
}
