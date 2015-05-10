package pl.edu.agh.petrinet.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * Representation of transition in Petri Net
 */
@Root(name = "Transition")
public class PetriTransition extends PetriVertex {

    /**
     * Special value for certain types of Petri Net
     * Time execution for this transition - for Time Petri net
     * Priority of this trnasition - for Priority Petri net
     */
    @Attribute(name = "TimeOrPriority")
    private int specialTypeValue;

    /**
     * Current value of time execution for Time Petri net
     */
    private transient int currentTimeForTimePetriNet;

    /**
     * Information of Transition type based on Petri Net Type
     */
    private transient PetriGraph.Type type;

    /**
     * Constructor
     * @param id        ID of Transition
     * @param type      Graph type
     */
    public PetriTransition(int id, PetriGraph.Type type) {
        super(id);
        this.type = type;
    }

    /**
     * Constructor
     * @param id        ID of Transition
     * @param type      Graph type
     * @param stv       Special value (Priority or Time)
     */
    public PetriTransition(int id, PetriGraph.Type type, int stv) {
        super(id);
        this.type = type;
        this.specialTypeValue = stv;
        if (type == PetriGraph.Type.TIME) {
            currentTimeForTimePetriNet = stv;
        }
    }

    /**
     * Constructor
     * @param id        ID of Transition
     * @param type      Graph type
     * @param name      Name of vertex
     */
    public PetriTransition(int id, PetriGraph.Type type, String name) {
        super(id, name);
        this.type = type;
    }

    /**
     * Constructor
     * @param id        ID of Transition
     * @param type      Graph type
     * @param name      Name of vertex
     * @param stv       Special value (Priority or Time)
     */
    public PetriTransition(int id, PetriGraph.Type type, String name, int stv) {
        super(id, name);
        this.type = type;
        this.specialTypeValue = stv;
        if (type == PetriGraph.Type.TIME) {
            currentTimeForTimePetriNet = stv;
        }
    }

    public PetriTransition(@Attribute(name="ID") int id, @Attribute(name="TimeOrPriority") int specialTypeValue){
        super(id);
        this.specialTypeValue = specialTypeValue;
    }

    /**
     * Set Graph type
     * @param type
     */
    public void setType(PetriGraph.Type type){
        this.type = type;
    }

    /**
     * Set Priority/Time for this transition
     * @param stv       value
     */
    public void setSpecialTypeValue(int stv) {
        this.specialTypeValue = stv;

        if (type == PetriGraph.Type.TIME) {
            currentTimeForTimePetriNet = stv;
        }
    }

    /**
     * Get priority of this transition
     * @return
     */
    public int getPriority() {
        if (type == PetriGraph.Type.PRIORYTY) {
            return specialTypeValue;
        }
        return 0;
    }

    /**
     * Get full execution time of this transition
     * @return
     */
    public int getTime() {
        if (type == PetriGraph.Type.TIME) {
            return specialTypeValue;
        }
        return 0;
    }


    /**
     * Change current execution time of this transition
     * @param time      time difference
     */
    public void decreaseTime(int time) {
        if (type == PetriGraph.Type.TIME) {
            currentTimeForTimePetriNet -= time;
            if (currentTimeForTimePetriNet <= 0) {
                currentTimeForTimePetriNet = specialTypeValue;
            }
        }
    }

    /**
     * Get left execution time
     * @return  time
     */
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
