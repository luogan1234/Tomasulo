package tomasulo;

public class Resource {
	public Register[] reg;
	public FloatRegister[] freg;
	public Memory mem;
	public Buffer[] ldBuffer={new Buffer("Load1",InstType.LD),new Buffer("Load2",InstType.LD),new Buffer("Load3",InstType.LD)};
	public Buffer[] stBuffer={new Buffer("Store1",InstType.ST),new Buffer("Store2",InstType.ST),new Buffer("Store3",InstType.ST)};
	public Buffer[] addBuffer={new Buffer("Add1",InstType.ADDD),new Buffer("Add2",InstType.ADDD),new Buffer("Add3",InstType.ADDD)};
	public Buffer[] multBuffer={new Buffer("Mult1",InstType.MULTD),new Buffer("Mult2",InstType.MULTD)};
	
	public Resource()
	{
		reg=new Register[11];
		freg=new FloatRegister[11];
		mem=new Memory();
	}
	
	public int ldBusy()
	{
		for (int i=0;i<3;++i)
			if (ldBuffer[i].inst==null)
				return i;
		return -1;
	}
	
	public int stBusy()
	{
		for (int i=0;i<3;++i)
			if (stBuffer[i].inst==null)
				return i;
		return -1;
	}
	
	public int addBusy()
	{
		for (int i=0;i<3;++i)
			if (addBuffer[i].inst==null)
				return i;
		return -1;
	}
	
	public int multBusy()
	{
		for (int i=0;i<2;++i)
			if (multBuffer[i].inst==null)
				return i;
		return -1;
	}
	
	public void nextLd()
	{
		int o=-1,i;
		for (i=0;i<3;++i)
			if (ldBuffer[i].isRunning())
				o=i;
		if (o!=-1)
			ldBuffer[o].next();
		else
			for (i=0;i<3;++i)
				if (ldBuffer[i].isBusy()&&)
	}
	
	public void next()
	{
		nextLd();
		nextSt();
		nextAdd();
		nextMult();
	}
}
