//CPSC 457 - Lab 4
//Jonathan Ng

import java.util.Date;

public class Processor extends Thread
{
	private static final int NUM_OF_LEVELS = 10;
	private static final int NUM_OF_PROCESSES = 2;
	private static final int NUM_OF_TOKENS= 1;
	
	protected DSM myDSM;
	protected int procID;
	
	protected TokenRingAgent myRing;
	private int critCount;
	
	private long startTime;
	
	public Processor() {}
	
	public Processor(int id, BroadcastSystem broadcast, Processor[] procs)
	{
		procID = id;
		if (id == 0)
		{//Token id 0 needs to loop back to the end
			myRing = new TokenRingAgent(true, procs, id, NUM_OF_PROCESSES - 1, id + 1);
		}
		else if (id == NUM_OF_PROCESSES - 1)
		{//Last token needs to loop back to the beginning
			myRing = new TokenRingAgent(true, procs, id, id - 1, 0);
		}
		else
		{
			myRing = new TokenRingAgent(true, procs, id, id - 1, id + 1);
		}
		myRing.start();
		myDSM = new DSM(id, broadcast, myRing);
		critCount = 0;
		
		startTime = new Date().getTime();
		System.out.println("startTime is  " + startTime);
		//Each process technically as a different start time, but only by a couple of milliseconds
		//As getting through to the critical section is on the order of seconds and minutes
		//This slight difference is actually negligible
	}
	
	public void napping(int napTime)
	{
		int sleepTime = (int) (napTime * Math.random() );
		try { Thread.sleep(sleepTime*1000); }
		catch(InterruptedException e) {}
	}
	
	private long getCurrentTime()
	{
		long currTime = new Date().getTime();
		long seconds = (currTime - startTime) / 1000;
		return seconds;
	}
	
	public void run()
	{
		while (true)
		{
			//In the lock
			boolean flagCheck = false;
			for (int i = 0; i < NUM_OF_LEVELS - 2; i++)
			{
				//System.out.println("Process " + procID + " is advancing to level " + i);
				
				myDSM.store("flag", i, procID);
				myDSM.store("turn", i, procID);
				
				//napping(1);
				
				int[] flag = myDSM.load("flag");
				for (int k = 0; k < flag.length; k++)
				{
					if (k == procID) continue;
					
					if (flag[k] >= i)
					{
						flagCheck = true;
					}
					else
					{
						flagCheck = false;
					}
				}
				
				int[] turn = myDSM.load("turn");
				while (flagCheck && (turn[i] == procID))
				{
					napping(4);
				}
			}
			//Out of the lock
			critCount++;
			System.out.println("Process " + procID + " is in the critical section. This is time #" + critCount);
			napping(4);
			System.out.println("Process " + procID + " is exiting the critical section. " + getCurrentTime() + " seconds have elapsed.");
			myDSM.store("flag", -1, procID);
			napping(8);
		}
	}
	
	public static void main(String[] args)
	{
		Processor[] processors = new Processor[NUM_OF_PROCESSES];
		TokenRing[] tokens = new TokenRing[NUM_OF_TOKENS];
		BroadcastSystem myBroadcast = new BroadcastSystem();
		myBroadcast.start();
		
		for (int i = 0; i < processors.length; i++)
		{
			processors[i] = new Processor(i, myBroadcast, processors);
			processors[i].start();
		}
		
		for (int i = 0; i < tokens.length; i++)
		{
			tokens[i] = new TokenRing(i, processors);
		}
	}
}