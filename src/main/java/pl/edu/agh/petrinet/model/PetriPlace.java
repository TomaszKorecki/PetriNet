package pl.edu.agh.petrinet.model;

public class PetriPlace extends PetriVertex {

    private int marksersCount;

    public PetriPlace(int id) {
        super(id);
        marksersCount = 0;
    }

    public PetriPlace(int id, String name) {
        super(id, name);
        marksersCount = 0;
    }

    @Override
    public String toString() {
        return "PetriPlace{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
