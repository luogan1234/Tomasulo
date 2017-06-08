package tomasulo;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class MemoryTableItem {
    public Memory originalMem;
    public int index;
   
    public IntegerProperty address = new SimpleIntegerProperty();
    public DoubleProperty value = new SimpleDoubleProperty();
    
    public MemoryTableItem() {}
    
    public MemoryTableItem(Memory m, int i) {
        originalMem = m;
        index = i;
        address.set(index);
        update();
    }
    
    public void update() {
        value.set(originalMem.mem[index]);
    }
    
    public void updateFromUI(Number n) {
        value.setValue(n);
        originalMem.mem[index] = n.doubleValue();
    }
    
}
