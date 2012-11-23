/*
 * FIFOInRouter.java
 * Author: Todd Ebert
 * Date: 10/27/2008
 * Updated: 11/01/2008
 */

/**
 * A FIFORouter is an InRouter that has an infinite-capacity queue structure for 
 * holding Tokens that are not yet allowed to enter the associated Module. 
 */

public abstract class FIFORouter extends InRouter
{
	protected Lst queue;
	
	public FIFORouter(String name)
	{
		super(name);
		queue = new Lst();
	}
	
	public abstract Wire selectWire(Token t);
	
	public Token viewNext() {return (Token) queue.front();}
	public int qLength() {return queue.size();}
	public void enterQueue(Token t) {queue.enter(t);}
	public Token exitQueue() {return (Token) queue.pop();}
	public void clearQueue() {queue.clear();}
}
