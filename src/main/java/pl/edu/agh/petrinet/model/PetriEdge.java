package pl.edu.agh.petrinet.model;

public class PetriEdge {

    private PetriVertex v1;
    private PetriVertex v2;
    private String name;

    public PetriEdge(PetriVertex v1, PetriVertex v2) {
        this.v1 = v1;
        this.v2 = v2;
    }

    public PetriEdge(PetriVertex v1, PetriVertex v2, String name) {
        this.v1 = v1;
        this.v2 = v2;
        this.name = name;
    }

    public PetriVertex getV1() {
        return v1;
    }

    public void setV1(PetriVertex v1) {
        this.v1 = v1;
    }

    public PetriVertex getV2() {
        return v2;
    }

    public void setV2(PetriVertex v2) {
        this.v2 = v2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "PetriEdge{" +
                "v1=" + v1 +
                ", v2=" + v2 +
                ", name='" + name + '\'' +
                '}';
    }
}
