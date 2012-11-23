/*
A QPort is a Port that has a Queue for holding Tokens until the Port is opened.
*/
public interface QPort
{
	//Assume: qLength > 0
	public Token viewNext();
	public int qLength();
	public void enterQueue(Token t);
	//Assume: qLength > 0
	public Token exitQueue();
	public void clearQueue();
}
