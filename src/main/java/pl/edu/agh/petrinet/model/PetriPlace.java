package pl.edu.agh.petrinet.model;

/**
 * Place representation of Petri net
 */
public class PetriPlace extends PetriVertex {

    /**
     * Current count of markers in place
     */
    private int marksersCount;

    /**
     * Start up count of markers in place
     */
    private int startupMarkersCount;

    /**
     * Constructor
     * @param id        ID of Place
     */
    public PetriPlace(int id) {
        super(id);
        marksersCount = startupMarkersCount = 0;
    }

    /**
     * Constructor
     * @param id                    ID of Place
     * @param startupMarkersCount   Startup markers count
     */
    public PetriPlace(int id, int startupMarkersCount) {
        super(id);
        this.marksersCount = this.startupMarkersCount = startupMarkersCount;
    }

    /**
     * Constructor
     * @param id        ID of Place
     * @param name      Name of Place
     */
    public PetriPlace(int id, String name) {
        super(id, name);
        marksersCount = startupMarkersCount = 0;
    }

    /**
     * Constructor
     * @param id                    ID of Place
     * @param name                  Name of Place
     * @param startupMarkersCount   Startup markers count
     */
    public PetriPlace(int id, String name, int startupMarkersCount) {
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
        return name + "   " + marksersCount;
    }
}
