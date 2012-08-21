//Lst.java
//Author: Todd Ebert
//Date: October 29th, 2008

public class Lst
{
	protected Link head;
	protected Link tail;
	protected Link cur;
	protected int size;
	
	public Lst()
	{
		size = 0;
		head = tail = cur = null;
	}
	
	public Object access() {return cur.data;}
	public void advance() {cur = cur.next;}
	
	public Lst append(Lst l2)
	{
		Lst a = new Lst();
		for(reset(); hasNext(); advance())
			a.enter(access());
	 
		for(l2.reset(); l2.hasNext(); l2.advance())
			a.enter(l2.access());
	
		return a;
	}
	
	public Object back()
	{
		return tail.data;
	}
	
	public void clear() 
	{
		size = 0;
		head = tail = cur = null;
	}
	
	public void enter(Object obj) 
	{
		if(tail == null)
			push(obj);
		else
		{
			size++;
			tail.next = new Link(obj, null);
			tail = tail.next;
		}
	}
	
	public void enter(Lst l)
	{
		for(l.reset();l.hasNext(); l.advance())
			enter(l.access());
	}
	
	public Object exit()
	{
		if(size <= 1) return pop();
	
		Link target = tail;
	
		Object data = target.data;
		Link curLink = head;
		Link prevLink = null;

		//size > 1 list
		while(curLink != target)
		{
			prevLink = curLink;
			curLink = curLink.next;
		}
	
		tail = prevLink;
		tail.next = null;
		size--;
		return data;
	}
	
	public Object find(Object obj)
	{
		Link pcur;
		Object object;
	
		for(pcur=head; pcur != null; pcur=pcur.next)
		{
			object = pcur.data;
			if(obj.equals(object)) return object;
		}
	
		return null;
	}
	
	public int frequency(Object obj)
	{
		Link pcur;
		Object object;
		int count = 0;
	
		for(pcur=head; pcur != null; pcur=pcur.next)
		{
			object = pcur.data;
			if(obj.equals(object)) count++;
		}
	
		return count;
	}
	
	public Object front()
	{
		return head.data;
	}
	
	public boolean hasNext()
	{
		return cur != null;
	}
	
	public boolean has(Object obj)
	{
		return find(obj) != null;
	}
	
	public Lst intersect(Lst l2)
	{
		//Map current elements into tables
		MapTable mt = l2.toMapTable();
		Object obj;
		Lst a = new Lst();
	
		for(reset(); hasNext(); advance())
		{
			obj = access();
			if(mt.has(obj))
				a.enter(obj);
		}

		return a.unique();
	}
	
	
	public  Lst merge(Lst l2)
	{
		//Map current elements into a table
		MapTable mt = new MapTable(size + l2.size());
		Object obj;
	
		Lst a = new Lst();
	
		for(reset(); hasNext(); advance())
		{
			obj = access();
			if(!mt.has(obj))
			{
				mt.insert(obj);
				a.enter(obj);
			}
		}
	 
		for(l2.reset(); l2.hasNext(); l2.advance())
		{
			obj = l2.access();
			if(!mt.has(obj))
			{
				mt.insert(obj);
				a.enter(obj);
			}
		}
	
		return a;
	}
	
	public Object pop()
	{
		Object data;

		//size 0 list
		if(head == null)
			return null;
		
		//size 1 list
		if(size == 1)
		{
			data = head.data;
			head = tail = null;
			size--;
			return data;
		}

		//size > 1 list
	
		data = head.data;
		head = head.next;
		size--;
		return data;
	}

	public void push(Object obj) 
	{
		size++;
		head = new Link(obj, head);
		
		if(tail == null)
			tail = head;
	}
	
	//Removes the first object equal to obj if it exists, and then returns a reference to the 	object.
	public Object remove(Object obj)
	{
		Link pcur = null;
		Link prev = null;
		Object data = null;
		boolean found = false;

		for(pcur = head; pcur != null; pcur=pcur.next)
		{
			data = pcur.data;
			if(obj.equals(data))
			{
				found = true;
				break;
			}
			prev=pcur;
		}
	
		if(!found) return null;
		
		if(prev == null) return pop();
		
		//adjust the current link and tail if needed
		if(tail == pcur)
			tail = prev;

		size--;
		prev.next = pcur.next;
		return data;
	}
	
	public void reset() {cur = head;}
	
	public int size() {return size;}
	
	public MapTable toMapTable()
	{
		MapTable mt = new MapTable(size);
		Object obj;
		
		Link lk;
		for(lk = head;lk != null; lk=lk.next)
		{
			obj = lk.data;
			if(!mt.has(obj)) mt.insert(obj);
		}
	
		return mt;
	}
	
	public Object[] toArray()
	{
		if(size == 0) return null;
		Object[] array = new Object[size];
		Link lk;
		int i;
		for(i=0,lk = head; lk != null; lk=lk.next,i++)
			array[i] = lk.data;
		return array;
	}
	
	
	public String toString()
	{
		String s = new String("");
		s += "[";
		int i=1;
		Link pcur;
		
		for(pcur = head; pcur != null; i++, pcur=pcur.next)
		{
			s += pcur.data.toString();
			if(i < size) s += ",";
		}
		
		s += "]";
		return s;
	}
	
	public Lst unique()
	{
		MapTable mt = new MapTable(size);
		Lst u = new Lst();
		Link lk;
		Object obj;
	
		for(lk = head; lk != null; lk = lk.next)
		{
			obj = lk.data;
			if(!mt.has(obj))
			{
				mt.insert(obj);
				u.enter(obj);
			}
		}
		return u;
	}
	
	protected class Link
	{
		public Object data;
		public Link next;
		
		public Link(Object data, Link next)
		{
			this.data = data;
			this.next = next;
		}
	}
}
