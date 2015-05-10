package pl.edu.agh.petrinet.serialization;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * Serializable edge in PetriNet Graph
 */
@Root(name = "Edge")
public class SPetriEdge {

    /**
     * Count of markers going through this edge
     */
    @Attribute(name = "Markers")
    private int MarkersCount;

    /**
     * Type of first vertex
     */
    @Attribute(name = "FromType")
    private String VertexOneType;

    /**
     * Id of first vertex
     */
    @Attribute(name = "FromId")
    private int VertexOneId;

    /**
     * Type of second vertex
     */
    @Attribute(name = "ToType")
    private String VertexTwoType;

    /**
     * Id of second vertex
     */
    @Attribute(name = "ToId")
    private int VertexTwoId;

    /**
     * Constructor
     * @param vot       Type of first vertex
     * @param voi       Id of first vertex
     * @param vtt       Type of second vertex
     * @param vti       Id of second vertex
     * @param m         Markers count
     */
    public SPetriEdge(@Attribute(name = "FromType") String vot, @Attribute(name = "FromId") int voi, @Attribute(name = "ToType") String vtt, @Attribute(name = "ToId") int vti, @Attribute(name = "Markers") int m){
        this.MarkersCount = m;
        this.VertexOneType = vot;
        this.VertexOneId = voi;
        this.VertexTwoType = vtt;
        this.VertexTwoId = vti;
    }

    /**
     * Set markers count
     * @param markersCount
     */
    public void setMarkersCount(int markersCount) {
        MarkersCount = markersCount;
    }

    /**
     * Get markers count
     * @return
     */
    public int getMarkersCount() {
        return MarkersCount;
    }

    /**
     * Set first vertex id
     * @param vertexOneId
     */
    public void setVertexOneId(int vertexOneId) {
        VertexOneId = vertexOneId;
    }

    /**
     * Get first vertex id
     * @return
     */
    public int getVertexOneId() {
        return VertexOneId;
    }

    /**
     * Set first vertex type
     * @param vertexOneType
     */
    public void setVertexOneType(String vertexOneType) {
        VertexOneType = vertexOneType;
    }

    /**
     * Get first vertex type
     * @return
     */
    public String getVertexOneType() {
        return VertexOneType;
    }

    /**
     * Set second vertex ID
     * @param vertexTwoId
     */
    public void setVertexTwoId(int vertexTwoId) {
        VertexTwoId = vertexTwoId;
    }

    /**
     * Get second vertex ID
     * @return
     */
    public int getVertexTwoId() {
        return VertexTwoId;
    }

    /**
     * Set second vertex type
     * @param vertexTwoType
     */
    public void setVertexTwoType(String vertexTwoType) {
        VertexTwoType = vertexTwoType;
    }

    /**
     * Get second vertex type
     * @return
     */
    public String getVertexTwoType() {
        return VertexTwoType;
    }
}
