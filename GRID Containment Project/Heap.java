/*
 * Heap.java
 * Author: Todd Ebert
 * Date: 12/24/2006
 * Modified: 11-15-2007
 */



public class Heap 
{
	
	public final static int INITIAL_CAPACITY=100;
	private Ordered[] array;
	private int arraySize;
	private int capacity;

	public Heap() 
	{
		capacity=INITIAL_CAPACITY;
		array = new Ordered[capacity+1];
		arraySize=0;
		for(int i=0; i<= capacity; i++)
			array[i]=null;
	}
	
	public Heap(int n) 
	{
		capacity=n;
		array = new Ordered[capacity+1];
		arraySize=0;
		for(int i=0; i <= capacity; i++)
			array[i]=null;
	}

	public Heap(Ordered[] objArray) 
	{

		arraySize = objArray.length;
		capacity=arraySize;
		
		array = new Ordered[capacity+1];
		int i;

		array[0]=null;

		for(i=1; i <= arraySize; i++)
			array[i]=objArray[i-1];

		for(i=arraySize+1; i <= capacity; i++)
			array[i]=null;

		for(i= arraySize/2; i>0; i--)
			percolateDown(i);

	}
	
	//Assumption: objArray.length = capacity
	public void insert(Ordered[] objArray)
	{
		arraySize = capacity;

		for(int i=1; i <= arraySize; i++)
			array[i]=objArray[i-1];

		for(int i= arraySize/2; i>0; i--)
			percolateDown(i);
	}
	
	public static Ordered[] heapSort(Ordered[] array)
	{
		int length;
		if(array == null || (length = array.length) == 0)
			return null;
		
		Ordered[] oa = new Ordered[length];
		
		Heap h = new Heap(array);
		for(int i=0; i < length; i++)
			oa[i] = h.pop();
		
		return oa;
	}
	
	public static Ordered[] heapSort(Lst ordList)
	{
		if(ordList == null) return null;
		
		int length = ordList.size();
		Ordered[] oa = new Ordered[length];
		Ordered or;
		int i=0;
		
		for(ordList.reset();ordList.hasNext();i++,ordList.advance())
		{
			or = (Ordered) ordList.access();
			oa[i] = or;
		}
		
		return Heap.heapSort(oa);
	}


	public void clear(){
		
		for(int i=1; i <= arraySize; i++)
			array[i]=null;
		
		arraySize=0;
	}

	public int size()
	{
		return arraySize;
	}

			
	public void insert(Ordered obj) 
	{
		if(arraySize >= capacity) resize();
			
		int hole = ++arraySize; 

		for(; hole > 1 && obj.compare((Object)(array[hole/2])) < 0; hole /= 2)
			array[hole] = array[hole/2];

		array[hole] = obj;
	}
	
	public Ordered top()
	{
		return array[1];
	}

	//delete least heap element
	public Ordered pop()
	{
		if(arraySize == 0) return null;

		Ordered minItem = array[1];
		array[1] = array[arraySize--];
		array[arraySize+1]=null;
		percolateDown(1);
		return minItem;
	}


	public Ordered remove(Ordered val)
	{
		Ordered foundItem=null;

		for(int i=1; i<= arraySize; i++)
		{
			if(array[i] != null && val.equals(array[i]))
			{
				foundItem=array[i];
				array[i] = array[arraySize--];
				array[arraySize+1]=null;
				percolateDown(i);
				return foundItem;
			}
		}
		
		return foundItem;
	}
	
	public String toString()
	{
		String s="[";
		int i=1;
		
		for(i=1; i<= arraySize; i++)
		{
			s = s + array[i].toString();
			if(i < arraySize) s = s + ",";
		}
		
		s = s + "]";
		return s;
	}


	private void percolateDown(int hole) 
	{
		int child;
		Ordered tmp=array[hole];

		for(; hole*2 <= arraySize; hole = child){
			child = hole*2;
			if(child != arraySize && array[child + 1].compare((Object)(array[child])) < 0)
				child++;
			if(array[child].compare((Object)(tmp)) < 0)
				array[hole] = array[child];
			else
				break;
		}
		array[hole] = tmp;
	}
	
	private void resize()
	{
		Ordered[] tempArray;
		int i;
		
		capacity *= 2;
		tempArray = new Ordered[capacity+1];
		
		tempArray[0]=null;

		for(i=1; i <= arraySize; i++)
			tempArray[i]=array[i];

		for(i=arraySize+1; i <= capacity; i++)
			tempArray[i]=null;
		
		array = tempArray;
	}

}
