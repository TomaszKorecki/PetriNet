package pl.edu.agh.petrinet.model;

import org.simpleframework.xml.Attribute;

/**
 * Representation of vertex in PetriNet Graph
 */
public abstract class PetriVertex{

    /**
     * ID of current vertex
     */
    @Attribute(name = "ID")
    protected int id;

    /**
     * Name of current vertex
     */
    @Attribute(name = "Name", required = false)
    protected String name;

    /**
     * Constructor
     * @param id    ID of vertex
     */
    public PetriVertex(int id) {
        this.id = id;
    }

    /**
     * Constructor
     * @param id    ID of vertex
     * @param name  Name of Vertex
     */
    public PetriVertex(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Get ID of ucrrent vertext
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * Set ID of current vertex
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Get name of current vertex
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Set name of current vertex
     * @param name
     */
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
