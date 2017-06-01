package tomasulo;

import java.util.Arrays;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

// The core of the algorithm is here

public class TomasuloCore {
    private BooleanProperty b = new SimpleBooleanProperty(true);
    
    public Resource resource;
    
    public Instruction[] insts;
    
    public int num,round;
    
    private int[] tot;
    
    public boolean running;

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
    
    //添加指令
    public void addInst(InstType type,int op1,int op2,int op3)
    {
    	++tot[type.ordinal()];
    	insts[num]=new Instruction(type,op1,op2,op3,tot[type.ordinal()],num,resource);
    	++num;
    }
    
    //开始执行
    public void start()
    {
    	assert(!running);
    	running=true;
    }
    
    //清空内存寄存器，指令最多100条
    public void clear()
    {
    	running=false;
    	insts=new Instruction[100];
    	num=round=0;
    	tot=new int[7];
    	Arrays.fill(tot, 0);
    	resource.clear();
    }
    
    //判断是否结束
    public boolean check()
    {
    	for (int i=0;i<num;++i)
    		if (insts[i].state!=StateType.Done)
    			return false;
    	return true;
    }
    
    //发射一条指令
    private void issue()
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
    
    //执行一轮
    public boolean next()
    {
    	assert(running);
    	++round;
    	resource.check(round);
    	issue();
    	resource.next(round);
    	boolean res=check();
    	if (res)
    		running=false;
    	return res;
    }
    
    //算法调试用
    public void print()
    {
    	System.out.println("-----------"+String.valueOf(round)+"-----------");
    	for(int i = 0; i < num; i++) {
    		System.out.println(insts[i].get());
    	}
    	System.out.println(Arrays.toString(resource.getBuffer(resource.ldBuffer, 3)));
    	System.out.println(Arrays.toString(resource.getBuffer(resource.stBuffer, 3)));
    	System.out.println(Arrays.toString(resource.getBuffer(resource.addBuffer, 3)));
    	System.out.println(Arrays.toString(resource.getBuffer(resource.multBuffer, 3)));
    	System.out.println(Arrays.toString(resource.getFregInfo()));
    	System.out.println(Arrays.toString(resource.getFregValue()));
    	System.out.println(Arrays.toString(resource.getRegValue()));
    }
}
