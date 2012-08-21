/*
 * FIFORelay.java
 * Author: Todd Ebert
 * Date: 10/27/2008
 * Updated: 11/01/2008
 */

/**
 * A FIFORelay is an InRelay that has an infinite-capacity queue structure for 
 * holding Tokens that are not yet allowed to enter the associated Module. 
 */
public class FIFORelay extends InRelay
{
	protected Lst queue;
	
	public FIFORelay(String name)
	{
		super(name);
		queue = new Lst();
	}
	
	public Token viewNext() {return (Token) queue.front();}
	public int qLength() {return queue.size();}
	public void enterQueue(Token t) {queue.enter(t);}
	public Token exitQueue() {return (Token) queue.pop();}
	public void clearQueue() {queue.clear();}
}
