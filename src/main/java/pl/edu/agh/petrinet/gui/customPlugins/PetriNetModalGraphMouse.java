//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package pl.edu.agh.petrinet.gui.customPlugins;

import edu.uci.ics.jung.visualization.control.*;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse.Mode;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.ItemSelectable;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class PetriNetModalGraphMouse<PetriVertex, PetriEdge> extends AbstractModalGraphMouse implements ModalGraphMouse, ItemSelectable {
    public PetriNetModalGraphMouse() {
        this(1.1F, 0.9090909F);
    }

    public PetriNetModalGraphMouse(float in, float out) {
        super(in, out);
        this.loadPlugins();
        this.setModeKeyListener(new DefaultModalGraphMouse.ModeKeyAdapter(this));
    }

    protected void loadPlugins() {
        this.pickingPlugin = new PickingGraphMousePlugin();
        this.animatedPickingPlugin = new AnimatedPickingGraphMousePlugin();
        this.translatingPlugin = new TranslatingGraphMousePlugin(16);
        this.scalingPlugin = new ScalingGraphMousePlugin(new CrossoverScalingControl(), 0, this.in, this.out);
        this.rotatingPlugin = new RotatingGraphMousePlugin();
        this.shearingPlugin = new ShearingGraphMousePlugin();


        this.add(this.scalingPlugin);
        this.setMode(Mode.TRANSFORMING);
    }

    public static class ModeKeyAdapter extends KeyAdapter {
        private char t = 116;
        private char p = 112;
        protected ModalGraphMouse graphMouse;

        public ModeKeyAdapter(ModalGraphMouse graphMouse) {
            this.graphMouse = graphMouse;
        }

        public ModeKeyAdapter(char t, char p, ModalGraphMouse graphMouse) {
            this.t = t;
            this.p = p;
            this.graphMouse = graphMouse;
        }

        public void keyTyped(KeyEvent event) {
            char keyChar = event.getKeyChar();
            if(keyChar == this.t) {
                ((Component)event.getSource()).setCursor(Cursor.getPredefinedCursor(0));
                this.graphMouse.setMode(Mode.TRANSFORMING);
            } else if(keyChar == this.p) {
                ((Component)event.getSource()).setCursor(Cursor.getPredefinedCursor(12));
                this.graphMouse.setMode(Mode.PICKING);
            }

        }
    }
}
