package tomasulo;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class RegisterTableContent {
    public Resource res;
    
    public StringProperty[] value;
    
    public RegisterTableContent() {}
    
    public RegisterTableContent(Resource r) {
        res = r;
        value = new SimpleStringProperty[r.reg.length];
        for (int i = 0; i < r.reg.length; ++i) {
            value[i] = new SimpleStringProperty();
        }
        update();
    }
    
    public void update() {
        String regValue[] = res.getRegValue();
        for (int i = 0; i < res.reg.length; ++i)
            value[i].set(String.valueOf(regValue[i]));
    }
    
    public void updateFromUI(int i, String s) {
        try {
            int v = Integer.valueOf(s);
            res.reg[i].value = v;
        } catch (NumberFormatException e) {
            System.out.println("NumberFormatException!");
        } finally {
            update();
        }

    }
    
}
