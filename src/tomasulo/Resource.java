package tomasulo;

public class Resource {
	public Register[] reg;
	public FloatRegister[] freg;
	public Memory mem;
	public Buffer[] ldBuffer={new Buffer("Load1",InstType.LD,this),new Buffer("Load2",InstType.LD,this),new Buffer("Load3",InstType.LD,this)};
	public Buffer[] stBuffer={new Buffer("Store1",InstType.ST,this),new Buffer("Store2",InstType.ST,this),new Buffer("Store3",InstType.ST,this)};
	public Buffer[] addBuffer={new Buffer("Add1",InstType.ADDD,this),new Buffer("Add2",InstType.ADDD,this),new Buffer("Add3",InstType.ADDD,this)};
	public Buffer[] multBuffer={new Buffer("Mult1",InstType.MULTD,this),new Buffer("Mult2",InstType.MULTD,this)};
	
	public Resource()
	{
		reg=new Register[11];
		freg=new FloatRegister[11];
		for (int i=0;i<11;++i)
		{
			reg[i]=new Register();
			freg[i]=new FloatRegister();
		}
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
	
	public void next(Buffer[] buffer,int n,int round)
	{
		int o=-1,i;
		for (i=0;i<n;++i)
			if (buffer[i].isRunning())
				buffer[i].next(round);
		for (i=0;i<n;++i)
			if (buffer[i].canStart()&&(o==-1||buffer[i].inst.order<buffer[o].inst.order))
				o=i;
		if (o!=-1)
			buffer[o].start();
	}
	
	public void check(Buffer[] buffer,int n,int round)
	{
		int i;
		for (i=0;i<n;++i)
			buffer[i].check(round);
	}
	
	public void next(int round)
	{
		next(ldBuffer,3,round);
		next(stBuffer,3,round);
		next(addBuffer,3,round);
		next(multBuffer,2,round);
	}
	
	public void check(int round)
	{
		check(ldBuffer,3,round);
		check(stBuffer,3,round);
		check(addBuffer,3,round);
		check(multBuffer,2,round);
	}
	
	public void write(Buffer[] buffer,int n,Buffer b,double res)
	{
		for (int i=0;i<n;++i)
			buffer[i].write(b,res);
	}
	
	public void write(Buffer buffer,double res)
	{
		write(ldBuffer,3,buffer,res);
		write(stBuffer,3,buffer,res);
		write(addBuffer,3,buffer,res);
		write(multBuffer,2,buffer,res);
	}
	
	public void print(Buffer[] buffer,int n)
	{
		for (int i=0;i<n;++i)
			buffer[i].print();
	}
	
	public void print()
	{
		int i;
		print(ldBuffer,3);
		print(stBuffer,3);
		print(addBuffer,3);
		print(multBuffer,2);
		System.out.println("freg:");
		for (i=0;i<11;++i)
			System.out.print(freg[i].info()+' ');
		System.out.println();
		for (i=0;i<11;++i)
			System.out.print(String.valueOf(freg[i].value)+' ');
		System.out.println();
		System.out.println("reg:");
		for (i=0;i<11;++i)
			System.out.print(String.valueOf(reg[i].value)+' ');
		System.out.println();
	}
}
