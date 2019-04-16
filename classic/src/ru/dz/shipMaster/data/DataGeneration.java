package ru.dz.shipMaster.data;

/**
 * Data holding object generation number. Used to synchronize data paths so that data travels 
 * from source to final destination synchronoysly. The problem is that DataPump calls performMeasure()
 * of different objects without sorting them first and finding out which feeds which. 
 * 
 * We don't want to sort objects as that will prevent us from runtime changes of object graph.  
 * 
 * So we pass generation around to make sure everybody is being read only after it was updated itself.
 * 
 * @author dz
 *
 */
public class DataGeneration {
	protected int number;
	
	public boolean precedesMe(DataGeneration him)
	{
		return him.number+1 == this.number;
	}

	public boolean followsMe(DataGeneration him)
	{
		return him.number == this.number+1;
	}
	
	//@Override
	public boolean equals(DataGeneration him) {
		return this.number == him.number;
	}
	
	public DataGeneration()
	{
		number = 0;
	}
	
	public DataGeneration getNext()
	{
		DataGeneration out = new DataGeneration();
		out.number = this.number+1;
		return out;
	}

	
	public String toString() { return Integer.toString(number); }
}
