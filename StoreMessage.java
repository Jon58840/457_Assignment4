//CPSC 457 - Lab 4
//Jonathan Ng

public class StoreMessage
{
	protected String key;
	protected int level;
	protected int procID;
	
	public StoreMessage() {}
	
	public StoreMessage(String k, int l, int id)
	{
		key = k;
		level = l;
		procID = id;
	}
}