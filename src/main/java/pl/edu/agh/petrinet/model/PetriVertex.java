package pl.edu.agh.petrinet.model;

/**
 * Created by Tomasz on 4/19/2015.
 */
public abstract class PetriVertex {
    protected String name;
    protected int id;

    public PetriVertex(int id){
        this.id = id;
    }

    public PetriVertex(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public String toString(){
        return name + " " + id;
    }
}
