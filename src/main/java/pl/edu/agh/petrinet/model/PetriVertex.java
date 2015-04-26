package pl.edu.agh.petrinet.model;

public abstract class PetriVertex {

    protected int id;
    protected String name;

    public PetriVertex(int id) {
        this.id = id;
    }

    public PetriVertex(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "PetriVertex{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
