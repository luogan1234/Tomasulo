package tomasulo;

public class Instruction {
	public InstType type;
	public int op1,op2,op3,num,issueRound,execRound,writeRound;
	public Resource resource;
	public StateType state;
	
	public Instruction(InstType t,int o1,int o2,int o3,int n,Resource r)
	{
		type=t;op1=o1;op2=o2;op3=o3;num=n;resource=r;state=StateType.Sleep;
		issueRound=execRound=writeRound=0;
	}
	
	public String typeName()
	{
		String name;
		switch (type)
		{
		case ADDD:
			name="ADDD";
			break;
		case DIVD:
			name="DIVD";
			break;
		case LD:
			name="LD";
			break;
		case MULTD:
			name="MULTD";
			break;
		case NOP:
			name="NOP";
			break;
		case ST:
			name="ST";
			break;
		case SUBD:
			name="SUBD";
			break;
		default:
			return "";
		}
		return name+String.valueOf(num);
	}
	
	public String Vj()
	{
		assert(type!=InstType.NOP&&type!=InstType.LD&&type!=InstType.ST);
		if (resource.freg[op2].info=="")
			return String.valueOf(resource.freg[op2].value);
		else
			return "";
	}
	
	public String Vk()
	{
		assert(type!=InstType.NOP&&type!=InstType.LD&&type!=InstType.ST);
		if (resource.freg[op3].info=="")
			return String.valueOf(resource.freg[op3].value);
		else
			return "";
	}
	
	public String Qj()
	{
		assert(type!=InstType.NOP&&type!=InstType.LD&&type!=InstType.ST);
		return resource.freg[op2].info;
	}
	
	public String Qk()
	{
		assert(type!=InstType.NOP&&type!=InstType.LD&&type!=InstType.ST);
		return resource.freg[op3].info;
	}
	
	public String Address()
	{
		assert(type==InstType.LD&&type==InstType.ST);
		return String.valueOf(resource.reg[op3].value+op2);
	}
	
	public String Issue()
	{
		if (issueRound>0)
			return String.valueOf(issueRound);
		else
			return "";
	}
	
	public String ExecComp()
	{
		if (execRound>0)
			return String.valueOf(execRound);
		else
			return "";
	}
	
	public String WriteResult()
	{
		if (writeRound>0)
			return String.valueOf(writeRound);
		else
			return "";
	}
}
