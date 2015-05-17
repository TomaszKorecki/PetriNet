package pl.edu.agh.petrinet.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * Place representation of Petri net
 */
@Root(name = "Place")
public class PetriPlace extends PetriVertex {

    /**
     * Current count of markers in place
     */
    private int marksersCount;

    /**
     * Start up count of markers in place
     */
    @Attribute(name = "Markers")
    private transient int startupMarkersCount;

    /**
     * Constructor
     * @param id        ID of Place
     */
    public PetriPlace(@Attribute(name="ID") int id) {
        super(id);
        marksersCount = startupMarkersCount = 0;
    }

    /**
     * Constructor
     * @param id                    ID of Place
     * @param startupMarkersCount   Startup markers count
     */
    public PetriPlace(@Attribute(name="ID") int id, @Attribute(name="Markers") int startupMarkersCount) {
        super(id);
        this.marksersCount = this.startupMarkersCount = startupMarkersCount;
    }

    /**
     * Constructor
     * @param id        ID of Place
     * @param name      Name of Place
     */
    public PetriPlace(@Attribute(name="ID") int id, @Attribute(name="Name") String name) {
        super(id, name);
        marksersCount = startupMarkersCount = 0;
    }

    /**
     * Constructor
     * @param id                    ID of Place
     * @param name                  Name of Place
     * @param startupMarkersCount   Startup markers count
     */
    public PetriPlace(@Attribute(name="ID")int id, @Attribute(name="Name") String name, @Attribute(name="Markers") int startupMarkersCount) {
        super(id, name);
        this.marksersCount = this.startupMarkersCount = startupMarkersCount;
    }

    /**
     * Get current count of markers
     * @return
     */
    public int getMarksersCount() {
        return this.marksersCount;
    }

    /**
     * Add number of markers
     * @param i     Markers count
     * @return
     */
    public int changeMarkersCount(int i) {
        return marksersCount += i;
    }

    /**
     * Set startup markers count as current
     */
    public void resetMarkersCount() {
        marksersCount = startupMarkersCount;
    }

    /**
     * Set new startup markers count
     * @param i     markers count
     */
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
        return "P" + id + "   " + marksersCount;
    }
}
