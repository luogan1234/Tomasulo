package tomasulo;

public class Buffer {
	public int timeLeft;
	public Instruction inst;
	public String name;
	public InstType type;
	
	public Buffer(String n,InstType t)
	{
		timeLeft=0;inst=null;name=n;type=t;
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
	}
	
	public boolean isRunning()
	{
		if (inst==null||timeLeft==-1)
			return false;
		else
			return true;
	}
	
	public boolean isBusy()
	{
		if (inst==null)
			return false;
		else
			return true;
	}
	
	public void start()
	{
		assert(inst!=null);
		if (inst.type==InstType.MULTD)
			timeLeft=10;
		else
		if (inst.type==InstType.DIVD)
			timeLeft=40;
		else
			timeLeft=2;
	}
	
	public void next()
	{
		assert(timeLeft>=0);
		--timeLeft;
		if (timeLeft==-1)
			inst=null;
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
