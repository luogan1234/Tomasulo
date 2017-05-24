package tomasulo;

import java.util.Arrays;

public class Memory {
	private int[] mem;
	private int len;
	
	public Memory()
	{
		len=65536;
		mem=new int[len];
		Arrays.fill(mem, 0);
	}
	
	public Memory(int l)
	{
		len=l;
		mem=new int[len];
		Arrays.fill(mem, 0);
	}
	
	public int load(int p)
	{
		assert(p>=0&&p<len);
		return mem[p];
	}
	
	public void store(int p,int v)
	{
		assert(p>=0&&p<len);
		mem[p]=v;
	}
}
