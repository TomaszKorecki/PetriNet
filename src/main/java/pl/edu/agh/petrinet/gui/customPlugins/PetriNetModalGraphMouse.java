//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package pl.edu.agh.petrinet.gui.customPlugins;

import edu.uci.ics.jung.visualization.control.*;
import pl.edu.agh.petrinet.model.PetriGraph;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class PetriNetModalGraphMouse<PetriVertex, PetriEdge> extends AbstractModalGraphMouse implements ModalGraphMouse, ItemSelectable {

    protected PetriNetEditingEdgeMousePlugin petriNetEditingEdgeMousePlugin;
    private PetriGraph petriGraph;
    private MouseModeChangedCallback modeChangedCallback;

    public PetriNetModalGraphMouse(PetriGraph petriGraph, MouseModeChangedCallback mouseModeChangedCallback) {
        this(1.1F, 0.9090909F, petriGraph, mouseModeChangedCallback);
    }

    public PetriNetModalGraphMouse(float in, float out, PetriGraph petriGraph, MouseModeChangedCallback mouseModeChangedCallback) {
        super(in, out);
        this.petriGraph = petriGraph;
        this.loadPlugins();
        this.setModeKeyListener(new PetriNetModalGraphMouse.ModeKeyAdapter(this));
        this.modeChangedCallback = mouseModeChangedCallback;
    }

    public void setModeChangedCallback(MouseModeChangedCallback callback){
        this.modeChangedCallback = callback;
    }

    protected void loadPlugins() {
        this.pickingPlugin = new PickingGraphMousePlugin();
        this.animatedPickingPlugin = new AnimatedPickingGraphMousePlugin();
        this.translatingPlugin = new TranslatingGraphMousePlugin(16);
        this.scalingPlugin = new ScalingGraphMousePlugin(new CrossoverScalingControl(), 0, this.in, this.out);
        this.rotatingPlugin = new RotatingGraphMousePlugin();
        this.shearingPlugin = new ShearingGraphMousePlugin();
        this.petriNetEditingEdgeMousePlugin = new PetriNetEditingEdgeMousePlugin(petriGraph);

        this.add(this.scalingPlugin);
        this.setMode(Mode.TRANSFORMING);
    }

    public void setMode(Mode mode) {
        if (this.mode != mode) {
            this.mode = mode;
            if (mode == Mode.TRANSFORMING) {
                this.setTransformingMode();
            } else if (mode == Mode.PICKING) {
                this.setPickingMode();
            } else if (mode == Mode.EDITING) {
                this.setEditingMode();
            }
            System.out.println("Mode setup on  " + mode.toString());
        }
    }

    protected void setPickingMode() {
        this.remove(this.translatingPlugin);
        this.remove(this.rotatingPlugin);
        this.remove(this.shearingPlugin);
        this.remove(this.petriNetEditingEdgeMousePlugin);
        this.add(this.pickingPlugin);
        this.add(this.animatedPickingPlugin);

        if(modeChangedCallback != null)
            this.modeChangedCallback.modeChanged(Mode.PICKING);
    }

    protected void setTransformingMode() {
        this.remove(this.pickingPlugin);
        this.remove(this.animatedPickingPlugin);
        this.remove(this.petriNetEditingEdgeMousePlugin);
        this.add(this.translatingPlugin);
        this.add(this.rotatingPlugin);
        this.add(this.shearingPlugin);

        if(modeChangedCallback != null)
            this.modeChangedCallback.modeChanged(Mode.TRANSFORMING);
    }

    protected void setEditingMode() {
        this.remove(this.pickingPlugin);
        this.remove(this.animatedPickingPlugin);
        this.remove(this.translatingPlugin);
        this.remove(this.rotatingPlugin);
        this.remove(this.shearingPlugin);
        this.add(this.petriNetEditingEdgeMousePlugin);

        if(modeChangedCallback != null)
            this.modeChangedCallback.modeChanged(Mode.EDITING);
    }


    public static class ModeKeyAdapter extends KeyAdapter {
        private char t = 116;
        private char p = 112;
        private char e = 101;
        protected ModalGraphMouse graphMouse;

        public ModeKeyAdapter(ModalGraphMouse graphMouse) {
            this.graphMouse = graphMouse;
        }

        public ModeKeyAdapter(char t, char p, char e, ModalGraphMouse graphMouse) {
            this.t = t;
            this.p = p;
            this.e = e;
            this.graphMouse = graphMouse;
        }

        public void keyTyped(KeyEvent event) {
            char keyChar = event.getKeyChar();
            if (keyChar == this.t) {
                ((Component) event.getSource()).setCursor(Cursor.getPredefinedCursor(0));
                this.graphMouse.setMode(Mode.TRANSFORMING);
            } else if (keyChar == this.p) {
                ((Component) event.getSource()).setCursor(Cursor.getPredefinedCursor(12));
                this.graphMouse.setMode(Mode.PICKING);
            } else if (keyChar == this.e) {
                ((Component) event.getSource()).setCursor(Cursor.getPredefinedCursor(1));
                this.graphMouse.setMode(Mode.EDITING);
            }
        }
    }
}

