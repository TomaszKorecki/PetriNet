package pl.edu.agh.petrinet.model;

/**
 * Created by Tomasz on 4/19/2015.
 */
public class PetriTransition extends PetriVertex {

    public PetriTransition(int id){
        super(id);
    }


    public String toString(){
        return "T" + id;
    }
}
