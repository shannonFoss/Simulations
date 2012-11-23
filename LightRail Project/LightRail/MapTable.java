//MapTable.java
//Author: Todd Ebert
//Date: October 23rd, 2008
//Updated: 


public class MapTable
{
	private Object[] array;
	private int load;
	private Object dummy;

	private static final int A = 16807;
	private static final int M = 2147483647;

	private static int lastIndex;

	private static final int[] prime = {5,11,31,37,67,131,257,521,1031,2053,4099,8209,16411,32771,65537,
131101,262147,524309,1048583,2097169,4194319,8388617,16777259,32452843,65170099,100711433,370248451,2147483647};

	//maxData represents the maximum amount of data that will be hashed to the table
	public MapTable(int maxData) 
	{
		load = 0;
		int threshold = 2*maxData+1;
		int size = findPrime(threshold);
	
		array = new Object[size];
		int i;
		for(i=0; i < size; i++)
			array[i] = null;
	}
	
	public MapTable()
	{
		load = 0;
		int size = prime[0];
	
		array = new Object[size];
		int i;
		for(i=0; i < size; i++)
			array[i] = null;
	}
	
	public void clear()
	{
		load = 0;
		for(int i=0; i < array.length; i++)
			array[i] = null;
	}
	
	//If the user plans to use the remove() method, then he or she should first set a "dummy"
	//object that is used to replace removed objects. This dummy object should be an object that 
	//no one will search for in the table. 
	public void setDummy(Object obj)
	{
		dummy = obj;
	}

	
	//Assume: obj is not in the table. 
	public void insert(Object obj) 
	{
		if(load >= array.length/2) resize();
		load++;
		
		int index = seedIndex(obj.hashCode());
	
		if(array[index] == null) 
		{
			array[index] = obj;
			return;
		}
		
		while(true)
		{
			index = nextIndex();
			if(array[index] == null) 
			{
				array[index] = obj;
				return;
			}
		}
	}
	
	public String toString()
	{
		String s = new String("");
		s += "[";
		int i;
		int size = array.length;
		
		for(i=0; i < size; i++)
		{
			if(array[i] != null && (dummy == null || !dummy.equals(array[i])))
				s += array[i].toString();
			else if(array[i] != null && dummy != null && dummy.equals(array[i]))
				s += array[i].toString() + " (\"removed\")";
			else s += "[null]";	
			if(i < size-1) s += ",";
		}
		
		s += "]";
		return s;
	}
	
	public Object find(Object obj)
	{
		int index = seedIndex(obj.hashCode());
			
		Object found;
	
		if((found=array[index]) == null) 
			return null;
	
		if(found.equals(obj))
			return found;
		
		while(true)
		{
			index = nextIndex();
		
			if((found=array[index]) == null) 
				return null;
	
			if(found.equals(obj))
				return found;
		}
	}
	
	public boolean has(Object obj)
	{
		return find(obj) != null;
	}
	
	//Returns the number of objects (including removed objects) currently in the table.
	public int load() {return load;}

	public Lst toList()
	{
		Lst l = new Lst();
		
		Object obj;
		int i;
	
		for(i=0; i < array.length; i++)
			if(((obj=array[i]) != null) && (dummy == null || !dummy.equals(obj)))
				l.enter(obj);
	
		return l;
	}
	
	public Object remove(Object obj)
	{
		int index = seedIndex(obj.hashCode());
			
		Object found;
	
		if((found=array[index]) == null) 
			return null;
	
		if(found.equals(obj))
		{
			array[index] = dummy;
			return found;
		}
		
		while(true)
		{
			index = nextIndex();
		
			if((found=array[index]) == null) 
				return null;
	
			if(found.equals(obj))
			{
				array[index] = dummy;
				return found;
			}
		}
	}
	
	public int tableSize() {return array.length;}
	
	//Return the least prime number in the prime array that exceeds n
	private int findPrime(int n)
	{
		int left = 0;
		int right = 28;
		int mid;
		int size;
	
		while(right - left > 1)
		{
			mid = (right+left)/2;
			if(n <= prime[mid])
				right = mid;
			else
				left = mid+1;
		}
		if((size=prime[left]) >= n) return size;
		return prime[right];
	}
	
	private int seedIndex(int initIndex)
	{
		lastIndex = initIndex;
		return lastIndex % array.length;
	}

	private int nextIndex()
	{
		lastIndex = (A*lastIndex % M);
		if(lastIndex < 0) lastIndex = 0-lastIndex;
		return lastIndex % array.length;
	}
	
	//Assumption: Current load is at least 1.
	private void resize()
	{
		int i;
		
		Object[] a = toList().toArray();
		if(a == null) //The table is currently empty
		{
			load = 0;
			for(i=0; i < array.length; i++)
				array[i] = null;
			return;
		}
		
		//double the size
		int size = findPrime(2*a.length);
		array = new Object[size];
		for(i=0; i < size; i++)
			array[i] = null;
			
		load = 0;

		for(i=0; i < a.length; i++)
			insert(a[i]);
	}
}



