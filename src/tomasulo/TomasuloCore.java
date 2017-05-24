package tomasulo;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

// The core of the algorithm is here

public class TomasuloCore {
    private BooleanProperty b = new SimpleBooleanProperty(true);

    public final boolean getB() { return b.get(); }
 
    public final void setB(boolean value) { b.set(value); }
 
    public BooleanProperty bProperty() { return b; }
    
    public void action() {
        b.set(!b.get());
    }
}
