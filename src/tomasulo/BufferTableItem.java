package tomasulo;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class BufferTableItem {
    public Buffer originalBuffer;
    
    public StringProperty time = new SimpleStringProperty();
    public StringProperty name = new SimpleStringProperty();
    public StringProperty busy = new SimpleStringProperty();
    public StringProperty Vj = new SimpleStringProperty();
    public StringProperty Vk = new SimpleStringProperty();
    public StringProperty Qj = new SimpleStringProperty();
    public StringProperty Qk = new SimpleStringProperty();
    public StringProperty address = new SimpleStringProperty();
    public StringProperty op = new SimpleStringProperty();
    
    public BufferTableItem() {}
    
    public BufferTableItem(Buffer b) {
        originalBuffer = b;
        update();
    }
    
    public void update() {
        time.set(originalBuffer.Time());
        name.set(originalBuffer.Name());
        busy.set(originalBuffer.Busy());
        Vj.set(originalBuffer.Vj());
        Vk.set(originalBuffer.Vk());
        Qj.set(originalBuffer.Qj());
        Qk.set(originalBuffer.Qk());
        address.set(originalBuffer.Address());
        op.set(originalBuffer.Op());
    }
    
}
