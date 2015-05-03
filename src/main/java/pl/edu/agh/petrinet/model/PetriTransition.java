package pl.edu.agh.petrinet.model;


public class PetriTransition extends PetriVertex {

    private int specialTypeValue;

    private int currentTimeForTimePetriNet;

    private PetriGraph.Type type;

    public PetriTransition(int id, PetriGraph.Type type) {
        super(id);
        this.type = type;
    }

    public PetriTransition(int id, PetriGraph.Type type, int stv) {
        super(id);
        this.type = type;
        this.specialTypeValue = stv;
        if (type == PetriGraph.Type.TIME) {
            currentTimeForTimePetriNet = stv;
        }
    }

    public PetriTransition(int id, PetriGraph.Type type, String name) {
        super(id, name);
        this.type = type;
    }

    public PetriTransition(int id, PetriGraph.Type type, String name, int stv) {
        super(id, name);
        this.type = type;
        this.specialTypeValue = stv;
        if (type == PetriGraph.Type.TIME) {
            currentTimeForTimePetriNet = stv;
        }
    }

    public void setSpecialTypeValue(int stv) {
        this.specialTypeValue = stv;

        if (type == PetriGraph.Type.TIME) {
            currentTimeForTimePetriNet = stv;
        }
    }

    public int getPriority() {
        if (type == PetriGraph.Type.PRIORYTY) {
            return specialTypeValue;
        }
        return 0;
    }

    public int getTime() {
        if (type == PetriGraph.Type.TIME) {
            return specialTypeValue;
        }
        return 0;
    }

    public void decreaseTime(int time) {
        if (type == PetriGraph.Type.TIME) {
            currentTimeForTimePetriNet -= time;
            if (currentTimeForTimePetriNet <= 0) {
                currentTimeForTimePetriNet = specialTypeValue;
            }
        }
    }

    public int getCurrentTime() {
        if (type == PetriGraph.Type.TIME) {
            return currentTimeForTimePetriNet;
        }
        return 0;
    }

    //    @Override
//    public String toString() {
//        return "PetriTransition{" +
//                "id=" + id +
//                ", name='" + name + '\'' +
//                '}';
//    }

    @Override
    public String toString() {
        return name + "   " + specialTypeValue;
    }
}
