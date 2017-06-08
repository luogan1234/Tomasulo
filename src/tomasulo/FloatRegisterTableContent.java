package tomasulo;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class FloatRegisterTableContent {
    public Resource res;
    
    public StringProperty[] value;
    public StringProperty[] info;
    
    public FloatRegisterTableContent() {}
    
    public FloatRegisterTableContent(Resource r) {
        res = r;
        value = new SimpleStringProperty[r.freg.length];
        info = new SimpleStringProperty[r.freg.length];
        for (int i = 0; i < r.freg.length; ++i) {
            value[i] = new SimpleStringProperty();
            info[i] = new SimpleStringProperty();
        }
        update();
    }
    
    public void update() {
        String regValue[] = res.getFregValue();
        String regInfo[] = res.getFregInfo();
        for (int i = 0; i < res.freg.length; ++i) {
            value[i].set(regValue[i]);
            info[i].set(regInfo[i]);
        }
    }
    
    public void updateFromUI(int i, String s) {
        try {
            double v = Double.valueOf(s);
            res.freg[i].value = v;
        } catch (NumberFormatException e) {
            System.out.println("NumberFormatException!");
        } finally {
            update();
        }

    }
    
}
