//CPSC 457 - Lab 4
//Jonathan Ng

import java.util.ArrayList;

public class TokenRingAgent extends Thread
{
	private static final int NAP_TIME = 4;
	
	private int tokenID;
	private boolean active;
	private int processorID;
	private int ringPredecessorID;
	private int ringSuccessorID;
	
	protected ArrayList<Token> activeTokens;
	protected ArrayList<Token> receivingTokens;
	protected Processor[] allProcesses;
	
	public TokenRingAgent() {}
	
	public TokenRingAgent(boolean status, Processor[] procs, int myID, int prevID, int nextID)
	{
		active = status;
		processorID = myID;
		ringPredecessorID = prevID;
		ringSuccessorID = nextID;
		
		activeTokens = new ArrayList<Token>();
		receivingTokens = new ArrayList<Token>();
		
		allProcesses = procs;
	}
	
	public void napping()
	{
		int sleepTime = (int) (NAP_TIME * Math.random() );
		try { Thread.sleep(sleepTime*1000); }
		catch(InterruptedException e) {}
	}
	
	
	public void ReceiveToken()
	{
		activeTokens.add(receivingTokens.get(0));
		//System.out.println("TokenRingAgent " + processorID + " is receiving token " + receivingTokens.get(0).tokenID);
	}

	public void SendToken(Token t)
	{
		allProcesses[ringSuccessorID].myRing.receivingTokens.add(t);
	}
	
	public void run()
	{
		while (true)
		{
			napping();
			
			while (activeTokens.size() > 0)
			{
				Token currentToken = activeTokens.get(0);
				SendToken(currentToken);
				activeTokens.remove(0);
			}
			
			while (receivingTokens.size() > 0)
			{
				ReceiveToken();
				receivingTokens.remove(0);
			}
		}
	}
}