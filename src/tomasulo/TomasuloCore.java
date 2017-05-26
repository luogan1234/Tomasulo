package tomasulo;

import java.util.Arrays;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

// The core of the algorithm is here

public class TomasuloCore {
    private BooleanProperty b = new SimpleBooleanProperty(true);
    
    public Resource resource;
    
    public Instruction[] insts;
    
    private int num,round;
    
    private int[] tot;
    
    private boolean running;

    public final boolean getB() { return b.get(); }
 
    public final void setB(boolean value) { b.set(value); }
 
    public BooleanProperty bProperty() { return b; }
    
    public void action() {
        b.set(!b.get());
    }
    
    public TomasuloCore()
    {
    	resource=new Resource();
    	num=0;running=false;
    }
    
    public void newInsts()
    {
    	insts=new Instruction[100];
    	num=round=0;
    	tot=new int[7];
    	Arrays.fill(tot, 0);
    }
    
    public void addInst(InstType type,int op1,int op2,int op3)
    {
    	++tot[type.ordinal()];
    	insts[num]=new Instruction(type,op1,op2,op3,tot[type.ordinal()],num,resource);
    	++num;
    }
    
    public void start()
    {
    	assert(!running);
    	running=true;
    }
    
    public void stop()
    {
    	assert(running);
    	running=false;
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
    	int o=-1;
    	for (int i=0;i<num;++i)
    		if (insts[i].state==StateType.Sleep)
    		{
    			switch (insts[i].type)
    			{
				case ADDD:
				case SUBD:
					o=resource.addBusy();
					if (o>-1)
						resource.addBuffer[o].setInst(insts[i]);
					break;
				case MULTD:
				case DIVD:
					o=resource.multBusy();
					if (o>-1)
						resource.multBuffer[o].setInst(insts[i]);
					break;
				case LD:
					o=resource.ldBusy();
					if (o>-1)
						resource.ldBuffer[o].setInst(insts[i]);
					break;
				case ST:
					o=resource.stBusy();
					if (o>-1)
						resource.stBuffer[o].setInst(insts[i]);
					break;
				default:
					break;
    			}
    			if (o>-1)
    			{
    				insts[i].issueRound=round;
					insts[i].state=StateType.Wait;
    			}
    			return;
    		}
    }
    
    public boolean next()
    {
    	assert(running);
    	++round;
    	resource.check(round);
    	resource.next(round);
    	issue();
    	boolean res=check();
    	if (res)
    		running=false;
    	return res;
    }
}
