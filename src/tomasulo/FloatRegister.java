package tomasulo;

public class FloatRegister {
	public double value;
	public Buffer buffer;

	public FloatRegister()
	{
		value=0;buffer=null;
	}

	public void write(Buffer b,double res)
	{
		value=res;
		if (buffer==b)
			buffer=null;
	}

	public String info()
	{
		if (buffer==null)
			return "";
		else
			return buffer.name;
	}
}
