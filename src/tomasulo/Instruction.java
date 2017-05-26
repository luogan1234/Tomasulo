package tomasulo;

public class Instruction {
	public InstType type;
	public int op1,op2,op3,num,order,issueRound,execRound,writeRound;
	public Resource resource;
	public StateType state;
	public double vj,vk;
	public Buffer qj,qk;
	
	public Instruction(InstType t,int o1,int o2,int o3,int n,int p,Resource r)
	{
		type=t;op1=o1;op2=o2;op3=o3;num=n;order=p;resource=r;
		issueRound=execRound=writeRound=0;state=StateType.Sleep;
		vj=vk=0.0;qj=qk=null;
	}
	
	public boolean canStart()
	{
		if (qj==null&&qk==null)
			return true;
		else
			return false;
	}
	
	public void write(Buffer buffer,double res)
	{
		if (qj==buffer)
		{
			qj=null;vj=res;
		}
		if (qk==buffer)
		{
			qk=null;vk=res;
		}
	}
	
	public double calc()
	{
		switch (type)
		{
		case ADDD:
			return vj+vk;
		case DIVD:
			assert(vk!=0);
			return vj/vk;
		case LD:
			return resource.mem.load(op2+resource.reg[op3].value);
		case MULTD:
			return vj*vk;
		case ST:
			resource.mem.store(op2+resource.reg[op3].value, vj);
			return 0.0;
		case SUBD:
			return vj-vk;
		default:
			return 0.0;
		}
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
		if (qj==null)
			return String.valueOf(vj);
		else
			return "";
	}
	
	public String Vk()
	{
		assert(type!=InstType.NOP&&type!=InstType.LD&&type!=InstType.ST);
		if (qk==null)
			return String.valueOf(vk);
		else
			return "";
	}
	
	public String Qj()
	{
		assert(type!=InstType.NOP&&type!=InstType.LD);
		return qj.name;
	}
	
	public String Qk()
	{
		assert(type!=InstType.NOP&&type!=InstType.LD&&type!=InstType.ST);
		return qk.name;
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
