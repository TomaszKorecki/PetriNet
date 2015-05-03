package pl.edu.agh.petrinet.model;

public class PetriPlace extends PetriVertex {

    private int marksersCount;

    private int startupMarkersCount;

    public PetriPlace(int id) {
        super(id);
        marksersCount = startupMarkersCount = 0;
    }

    public PetriPlace(int id, int startupMarkersCount) {
        super(id);
        this.marksersCount = this.startupMarkersCount = startupMarkersCount;
    }

    public PetriPlace(int id, String name) {
        super(id, name);
        marksersCount = startupMarkersCount = 0;
    }

    public PetriPlace(int id, String name, int startupMarkersCount) {
        super(id, name);
        this.marksersCount = this.startupMarkersCount = startupMarkersCount;
    }

    public int getMarksersCount() {
        return this.marksersCount;
    }

    public int changeMarkersCount(int i) {
        return marksersCount += i;
    }

    public void resetMarkersCount() {
        marksersCount = startupMarkersCount;
    }

    public void setStartupMarkersCount(int i) {
        this.startupMarkersCount = i;
        resetMarkersCount();
    }

    //    @Override
//    public String toString() {
//        return "PetriPlace{" +
//                "id=" + id +
//                ", name='" + name + '\'' +
//                '}';
//    }

    @Override
    public String toString() {
        return name + "   " + marksersCount;
    }
}
