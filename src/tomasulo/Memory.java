package tomasulo;

import java.util.Arrays;

public class Memory {
	private double[] mem;
	private int len;
	
	public Memory()
	{
		len=64;
		mem=new double[len];
		Arrays.fill(mem, 0.0);
	}
	
	public Memory(int l)
	{
		len=l;
		mem=new double[len];
		Arrays.fill(mem, 0.0);
	}
	
	public double load(int p)
	{
		assert(p>=0&&p<len);
		return mem[p];
	}
	
	public void store(int p,double v)
	{
		assert(p>=0&&p<len);
		mem[p]=v;
	}
}
