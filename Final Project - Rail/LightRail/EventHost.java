/**
 * An EventHost is any Object for which Events occur within that object.
 * Typical examples include Modules, Ports, and Wires. An EventHost must have the 
 * capability of processing the given Event.  
 */
public interface EventHost
{
	void process(Event e);
}
