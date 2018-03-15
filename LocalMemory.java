//CPSC 457 - Lab 4
//Jonathan Ng

import java.util.Arrays;

public class LocalMemory
{
	private static final int NUM_OF_LEVELS = 10;
	private static final int NUM_OF_PROCESSES = 4;
	
	int[] turn;
	int[] flag;
	
	public LocalMemory()
	{
		turn = new int[NUM_OF_LEVELS];
		flag = new int[NUM_OF_PROCESSES];
		Arrays.fill(turn, -1);
		Arrays.fill(flag, -1);
	}
	
	public int[] load(String key)
	{
		if (key.equals("turn"))
		{
			return turn;
		}
		else if (key.equals("flag"))
		{
			return flag;
		}
		
		return null;
	}
	
	public void store(String key, int level, int id)
	{
		if (key.equals("turn"))
		{
			turn[level] = id;
		}
		else if (key.equals("flag"))
		{
			flag[id] = level;
		}
	}
}