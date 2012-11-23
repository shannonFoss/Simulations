/*
 * FiniteDistribution.java
 * Author: Todd Ebert
 * Date: 10/25/2008
 * Updated: 11/01/2008
 * Class description. used to represent finite probability distributions whose range space
 * is assumed to be a set of nonnegative integers.
 */

import java.lang.Math;


public class FiniteDistribution
{
	public static final int DISTRIBUTION_SIZE_LIMIT = 100000;
	private static final int INITIAL_ARRAY_SIZE = 5;
	private double totalMass; //total probability mass (does not have to add up to one)
	private int n; //size of distribution
	private double[] mass; //distribution of mass 
	
	//assumption: maxObservableIndex >= 0
	public FiniteDistribution(int maxObservableIndex)
	{
		int arraySize = maxObservableIndex+1;
		n = -1;
		totalMass = 0.0;
		mass = new double[arraySize];
		for(int i=0; i < arraySize; i++)
			mass[i] = 0.0;
	}
	
	public FiniteDistribution()
	{
		int arraySize = INITIAL_ARRAY_SIZE;
		n = -1;
		totalMass = 0.0;
		mass = new double[arraySize];
		for(int i=0; i < arraySize; i++)
			mass[i] = 0.0;
	}
	
	//index >= 0
	public void addMass(double m, int index)
	{
		if(index >= FiniteDistribution.DISTRIBUTION_SIZE_LIMIT) return;
		if(index >= mass.length) resize(2*index);
		if(index > n) n = index;
		totalMass += m;
		if(index < 0 || index > mass.length)
			System.out.println(mass.length + " " + index);
		mass[index] += m;
	}
	
	//assumption: 0 <= index <= size()
	//returns the total mass accumulated at the given index. 
	//Note: typically this value is greater than 1; i.e. the mass has not been normalized by the total mass of the distribution
	public double getMass(int index)
	{
		return mass[index];
	}
	
	public double totalMass()
	{
		return totalMass;
	}
	
    //assumptions: 0 <= index <= size()
	public double density(int index)
	{
		if(totalMass == 0.0) return 0.0;
		return mass[index]/totalMass;
	}
	
	//Size of distribution is one more than the largest index n
	//that has nonzero mass.
	public int size()
	{
		return n+1;
	}
	
	public double cdf(double x)
	{
		if(x < 0) return 0.0;
		if(x > n) return 0.0;
		if(x == ((double) n)) return 1.0;
		if(totalMass == 0.0) return 0.0;
		
		int i;
		double total=0.0;
		
		for(i=0; i <= x; i++) 
			total += mass[i];
		return total/totalMass;
	}
	
	public double mean()
	{
		if(totalMass == 0.0) return 0.0;
		double weightedMass=0.0;
		for(int i=1; i <= n; i++)
			weightedMass += i*mass[i];
		return weightedMass/totalMass;
	}
	
	public double secondMoment()
	{
		if(totalMass == 0.0) return 0.0;
		double weightedMass=0.0;
		for(int i=1; i <= n; i++)
			weightedMass += ((double)i)*((double)i)*mass[i];
		return weightedMass/totalMass;
	}
	
	double variance()
	{
		return secondMoment() - (mean()*mean());
	}
	
	double std()
	{
		return Math.sqrt(variance());
	}
	
	public int mode()
	{
		int mostFreq=0;
		double maxMass=0.0;
		double m;
		for(int i=0; i <= n; i++) 
		{
			if((m=mass[i]) > maxMass)
			{
				maxMass = m;
				mostFreq = i;
			}
		}
		return mostFreq;
	}
	
	private void resize(int newArraySize)
	{
		if(newArraySize > FiniteDistribution.DISTRIBUTION_SIZE_LIMIT)
			newArraySize = FiniteDistribution.DISTRIBUTION_SIZE_LIMIT;
		double[] tempArray;
		int i;
		int oldArraySize = mass.length;
		
		tempArray = new double[newArraySize];

		for(i=0; i < oldArraySize; i++)
			tempArray[i]=mass[i];

		for(i=oldArraySize; i < newArraySize; i++)
			tempArray[i]=0.0;
		
		mass = tempArray;
	}
}
