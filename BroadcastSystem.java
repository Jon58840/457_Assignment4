//CPSC 457 - Lab 4
//Jonathan Ng

import java.util.ArrayList;

public class BroadcastSystem extends Thread
{
	private static final int NAP_TIME = 2;
	
	protected ArrayList<BroadcastAgent> agents;
	protected ArrayList<StoreMessage> messages;
	
	public BroadcastSystem()
	{
		agents = new ArrayList<BroadcastAgent>();
		messages = new ArrayList<StoreMessage>();
	}
	
	public void napping()
	{
		int sleepTime = (int) (NAP_TIME * Math.random() );
		try { Thread.sleep(sleepTime*1000); }
		catch(InterruptedException e) {}
	}
	
	public void broadcast(StoreMessage message)
	{
		messages.add(message);
	}
	
	public void register(BroadcastAgent agent)
	{
		agents.add(agent);
	}
	
	public void run()
	{
		while (true)
		{
			napping();
		
			while (messages.size() > 0)
			{//Send off all our currently stored messages
		
				StoreMessage currentMessage = messages.get(0);	//Grab first message in queue
				for (int i = 0; i < agents.size(); i++)
				{
					//Skip on self, don't duplicate store
					if (agents.get(i).procID == currentMessage.procID) continue;
					
					//Add our message to each agent for them to deal with when they get around to it
					agents.get(i).receivedMessages.add(currentMessage);
				}
				messages.remove(0);	//Remove the first message and move to next in loop
			}
		}
	}
}