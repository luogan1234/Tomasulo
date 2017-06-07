package tomasulo;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class InstructionTableItem {
    public Instruction originalInst;
    
    public StringProperty typeName = new SimpleStringProperty();
    public StringProperty Vj = new SimpleStringProperty();
    public StringProperty Vk = new SimpleStringProperty();
    public StringProperty Qj = new SimpleStringProperty();
    public StringProperty Qk = new SimpleStringProperty();
    public StringProperty address = new SimpleStringProperty();
    public StringProperty issue = new SimpleStringProperty();
    public StringProperty execComp = new SimpleStringProperty();
    public StringProperty writeResult = new SimpleStringProperty();
    
    public StringProperty op1 = new SimpleStringProperty();
    public StringProperty op2 = new SimpleStringProperty();
    public StringProperty op3 = new SimpleStringProperty();
    
    public InstructionTableItem() {}
    
    public InstructionTableItem(Instruction i) {
        originalInst = i;
        update();
    }
    
    public void update() {
        typeName.set(originalInst.typeName());
        Vj.set(originalInst.Vj());
        Vk.set(originalInst.Vk());
        Qj.set(originalInst.Qj());
        Qk.set(originalInst.Qk());
        address.set(originalInst.Address());
        issue.set(originalInst.Issue());
        execComp.set(originalInst.ExecComp());
        writeResult.set(originalInst.WriteResult());
        
        op1.set(originalInst.Desti());
        op2.set(originalInst.Srcj());
        op3.set(originalInst.Srck());
    }
    
}
