package tomasulo;

public class Buffer {
	public int timeLeft;
	public Instruction inst;
	public String name;
	public InstType type;
	public Resource resource;

	public Buffer(String n,InstType t,Resource r)
	{
		timeLeft=-1;inst=null;name=n;type=t;resource=r;
	}

	public void setInst(Instruction i)
	{
		assert(inst==null);
		if (type==InstType.ADDD)
			assert(i.type==InstType.ADDD||i.type==InstType.SUBD);
		if (type==InstType.MULTD)
			assert(i.type==InstType.MULTD||i.type==InstType.DIVD);
		if (type==InstType.LD)
			assert(i.type==InstType.LD);
		if (type==InstType.ST)
			assert(i.type==InstType.ST);
		inst=i;
		timeLeft=-1;
		if (inst.type!=InstType.LD)
		{
			int op;
			if (inst.type!=InstType.ST)
				op=inst.op2;
			else
				op=inst.op1;
			FloatRegister freg=resource.freg[op];
			if (freg.buffer==null)
				inst.vj=freg.value;
			else
				inst.qj=freg.buffer;
		}
		if (inst.type!=InstType.LD&&inst.type!=InstType.ST)
		{
			FloatRegister freg=resource.freg[inst.op3];
			if (freg.buffer==null)
				inst.vk=freg.value;
			else
				inst.qk=freg.buffer;
		}
		if (inst.type!=InstType.ST)
			resource.freg[inst.op1].buffer=this;
	}

	public boolean isRunning()
	{
		if (inst==null||timeLeft==-1)
			return false;
		else
			return true;
	}

	public boolean canStart()
	{
		if (inst==null||timeLeft!=-1)
			return false;
		else
			return inst.canStart();
	}

	public void start()
	{
		assert(inst!=null);
		inst.state=StateType.Run;
		if (inst.type==InstType.MULTD)
			timeLeft=10;
		else
		if (inst.type==InstType.DIVD)
			timeLeft=40;
		else
			timeLeft=2;
	}

	public void next(int round)
	{
		assert(timeLeft>0);
		--timeLeft;
		if (timeLeft==0)
		{
			inst.execRound=round;
		}
	}

	public void write(Buffer buffer,double res)
	{
		if (inst!=null)
			inst.write(buffer,res);
	}

	public void check(int round)
	{
		if (timeLeft==0)
		{
			inst.state=StateType.Done;
			inst.writeRound=round;
			double res=inst.calc();
			if (inst.type!=InstType.ST)
			{
				resource.write(this,res);
				resource.freg[inst.op1].write(this,res);
			}
			inst=null;
			timeLeft=-1;
		}
	}

	public String get()
	{
		switch (type)
		{
		case ADDD:
		case MULTD:
			return Time()+' '+Name()+' '+Busy()+' '+Op()+' '+Vj()+' '+Vk()+' '+Qj()+' '+Qk();
		case LD:
			return Time()+' '+Name()+' '+Busy()+' '+Address();
		case ST:
			return Time()+' '+Name()+' '+Busy()+' '+Address()+' '+Vj()+' '+Qj();
		default:
			return "";
		}
	}

	public void clear()
	{
		timeLeft=-1;inst=null;
	}

	public String Time()
	{
		if (timeLeft>=0)
			return String.valueOf(timeLeft);
		else
			return "";
	}

	public String Name()
	{
		return name;
	}

	public String Busy()
	{
		if (inst!=null)
			return "Yes";
		else
			return "No";
	}

	public String Op()
	{
		if (inst!=null)
			return inst.typeName();
		else
			return "";
	}

	public String Vj()
	{
		if (inst!=null)
			return inst.Vj();
		else
			return "";
	}

	public String Vk()
	{
		if (inst!=null)
			return inst.Vk();
		else
			return "";
	}

	public String Qj()
	{
		if (inst!=null)
			return inst.Qj();
		else
			return "";
	}

	public String Qk()
	{
		if (inst!=null)
			return inst.Qk();
		else
			return "";
	}

	public String Address()
	{
		if (inst!=null)
			return inst.Address();
		else
			return "";
	}
}
