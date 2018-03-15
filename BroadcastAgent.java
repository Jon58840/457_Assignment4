//CPSC 457 - Lab 4
//Jonathan Ng

import java.util.ArrayList;

public class BroadcastAgent extends Thread
{
	private static final int NAP_TIME = 2;
	
	protected LocalMemory myMemory;
	protected BroadcastSystem myBroadcast;
	protected int procID;
	
	protected ArrayList<StoreMessage> sendMessages;
	protected ArrayList<StoreMessage> receivedMessages;
	
	public BroadcastAgent() {}
	
	public BroadcastAgent(int id, LocalMemory memory, BroadcastSystem broadcast)
	{
		procID = id;
		myMemory = memory;
		
		sendMessages = new ArrayList<StoreMessage>();
		receivedMessages = new ArrayList<StoreMessage>();
		
		myBroadcast = broadcast;
		broadcast.register(this);
	}
	
	public void napping()
	{
		int sleepTime = (int) (NAP_TIME * Math.random() );
		try { Thread.sleep(sleepTime*1000); }
		catch(InterruptedException e) {}
	}
	
	public void broadcast(String key, int level)
	{
		//System.out.println("BroadcastAgent " + procID + " has a message to broadcast.");
		StoreMessage message = new StoreMessage(key, level, procID);
		sendMessages.add(message);
	}
	
	public void receive()
	{
		StoreMessage currentMessage = receivedMessages.get(0);
		myMemory.store(currentMessage.key, currentMessage.level, currentMessage.procID);
	}
	
	public void run()
	{
		while (true)
		{
			napping();
			
			while (sendMessages.size() > 0)
			{
				//System.out.println("BroadcastAgent " + procID + " is broadcasting a message. sendMessages size is " + sendMessages.size());
				myBroadcast.broadcast(sendMessages.get(0));
				sendMessages.remove(0);
			}
			
			while (receivedMessages.size() > 0)
			{
				//System.out.println("BroadcastAgent " + procID + " is storing a received message.");
				receive();
				receivedMessages.remove(0);
			}
		}
	}
}