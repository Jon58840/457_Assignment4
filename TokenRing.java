//CPSC 457 - Lab 4
//Jonathan Ng

import java.util.ArrayList;

public class TokenRing
{
	int ringID;
	Token myToken;
	
	public TokenRing() {}
	
	public TokenRing(int id, Processor[] procs)
	{
		ringID = id;
		myToken = new Token(id);
		
		int startingProcess = id % procs.length;
		//I want to spread out starting locations of the tokens
		//Modulus operator so that I stay within range of indices
		
		System.out.println("TokenRing " + ringID + " is sending token to process " + startingProcess);
		procs[startingProcess].myRing.receivingTokens.add(myToken);
	}
}