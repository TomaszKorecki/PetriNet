package pl.edu.agh.petrinet.model;

public class PetriTransition extends PetriVertex {

    public PetriTransition(int id) {
        super(id);
    }

    public PetriTransition(int id, String name) {
        super(id, name);
    }

    @Override
    public String toString() {
        return "PetriTransition{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
