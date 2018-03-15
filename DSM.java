//CPSC 457 - Lab 4
//Jonathan Ng

import java.util.concurrent.*;

public class DSM
{
	private static final int NAP_TIME = 4;
	
	private LocalMemory myMemory;
	private BroadcastAgent myAgent;
	protected int procID;
	protected TokenRingAgent myRing;
	
	public DSM() {}
	
	public DSM(int id, BroadcastSystem broadcast, TokenRingAgent ring)
	{
		myMemory = new LocalMemory();
		myAgent = new BroadcastAgent(id, myMemory, broadcast);
		myAgent.start();
		procID = id;
		myRing = ring;
	}
	
	public void napping()
	{
		int sleepTime = (int) (NAP_TIME * Math.random() );
		try { Thread.sleep(sleepTime*1000); }
		catch(InterruptedException e) {}
	}
	
	public int[] load(String key)
	{
		return myMemory.load(key);
	}
	
	public void store(String key, int level, int id)
	{/*
		//Multi-token check
		if (key.equals("turn"))
		{
			boolean hasToken = false;
			while (!hasToken)
			{
				//System.out.println("In while loop");
				napping();
				
				for (int i = 0; i < myRing.activeTokens.size(); i++)
				{
					if (myRing.activeTokens.get(i).tokenID == level)
					{
						hasToken = true;
					}
				}
			}
		}
		*/
		
		//Single token check
		boolean hasToken = false;
		while (!hasToken)
		{
			napping();
			if (myRing.activeTokens.size() > 0)
			{
				hasToken = true;
			}
		}
		
		myMemory.store(key, level, id);
		myAgent.broadcast(key, level);
	}
}