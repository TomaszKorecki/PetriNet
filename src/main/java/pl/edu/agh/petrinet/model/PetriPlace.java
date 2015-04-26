package pl.edu.agh.petrinet.model;

public class PetriPlace extends PetriVertex {

    public PetriPlace(int id) {
        super(id);
    }

    public PetriPlace(int id, String name) {
        super(id, name);
    }

    @Override
    public String toString() {
        return "PetriPlace{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
