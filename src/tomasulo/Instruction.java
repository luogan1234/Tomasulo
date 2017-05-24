package tomasulo;

public class Instruction {
	public InstType type;
	public int op1,op2,op3;
	
	public Instruction(InstType t,int o1,int o2)
	{
		type=t;op1=o1;op2=o2;op3=-1;
	}
	
	public Instruction(InstType t,int o1,int o2,int o3)
	{
		type=t;op1=o1;op2=o2;op3=o3;
	}
}
