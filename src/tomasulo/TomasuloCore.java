package tomasulo;

import java.util.Arrays;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

// The core of the algorithm is here

public class TomasuloCore {
    private BooleanProperty b = new SimpleBooleanProperty(true);
    
    public Resource resource;
    
    public Instruction[] insts;
    
    private int num;
    
    private int[] tot;

    public final boolean getB() { return b.get(); }
 
    public final void setB(boolean value) { b.set(value); }
 
    public BooleanProperty bProperty() { return b; }
    
    public void action() {
        b.set(!b.get());
    }
    
    public TomasuloCore()
    {
    	resource=new Resource();
    	num=0;
    }
    
    public void newInsts()
    {
    	insts=new Instruction[100];
    	num=0;
    	tot=new int[7];
    	Arrays.fill(tot, 0);
    }
    
    public void addInst(InstType type,int op1,int op2,int op3)
    {
    	++tot[type.ordinal()];
    	insts[num]=new Instruction(type,op1,op2,op3,tot[type.ordinal()],resource);
    	++num;
    }
    
    public boolean check()
    {
    	for (int i=0;i<num;++i)
    		if (insts[i].state!=StateType.Done)
    			return false;
    	return true;
    }
    
    public void issue()
    {
    	int o;
    	for (int i=0;i<num;++i)
    		if (insts[i].state==StateType.Sleep)
    		{
    			switch (insts[i].type)
    			{
				case ADDD:
				case SUBD:
					o=resource.addBusy();
					if (o>-1)
					{
						resource.addBuffer[o].setInst(insts[i]);
						insts[i].state=StateType.Wait;
						return;
					}
					break;
				case MULTD:
				case DIVD:
					o=resource.multBusy();
					if (o>-1)
					{
						resource.multBuffer[o].setInst(insts[i]);
						insts[i].state=StateType.Wait;
						return;
					}
					break;
				case LD:
					o=resource.ldBusy();
					if (o>-1)
					{
						resource.ldBuffer[o].setInst(insts[i]);
						insts[i].state=StateType.Wait;
						return;
					}
					break;
				case ST:
					o=resource.stBusy();
					if (o>-1)
					{
						resource.stBuffer[o].setInst(insts[i]);
						insts[i].state=StateType.Wait;
						return;
					}
					break;
				default:
					break;
    			}
    		}
    }
    
    public boolean next()
    {
    	resource.next();
    	issue();
    	return check();
    }
    
    
    
    
}
