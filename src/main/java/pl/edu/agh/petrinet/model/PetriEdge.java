package pl.edu.agh.petrinet.model;

public class PetriEdge {

    private PetriVertex v1;
    private PetriVertex v2;
    private String name;
    private int markersCount;

    public PetriEdge(PetriVertex v1, PetriVertex v2) {
        this.v1 = v1;
        this.v2 = v2;
        this.markersCount = 1;
    }

    public PetriEdge(PetriVertex v1, PetriVertex v2, int markersCount) {
        this.v1 = v1;
        this.v2 = v2;
        this.markersCount = markersCount;
    }

    public PetriEdge(PetriVertex v1, PetriVertex v2, String name) {
        this.v1 = v1;
        this.v2 = v2;
        this.name = name;
        this.markersCount = 1;
    }

    public PetriEdge(PetriVertex v1, PetriVertex v2, String name, int markersCount) {
        this.v1 = v1;
        this.v2 = v2;
        this.name = name;
        this.markersCount = markersCount;
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

    public int getMarkersCount(){return this.markersCount;}

    public void setMarkersCount(int markersCount) {this.markersCount = markersCount;}

//    @Override
//    public String toString() {
//        return "PetriEdge{" +
//                "v1=" + v1 +
//                ", v2=" + v2 +
//                ", name='" + name + '\'' +
//                ", markers=" + markersCount +
//                '}';
//    }

    public String toString(){
        return markersCount == 1 ? "" : new Integer(markersCount).toString();
    }
}
