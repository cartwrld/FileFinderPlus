package com.example.javafx;

public class IncCounter
{
	int count = 0;
	
	//only one thread at a time can get in here
	public synchronized int getAndUpdate()
	{
		return count++;
	}

	public synchronized int getAndDec() { return count--; }

	public synchronized void dec() { count--; }

	public synchronized int getCount() { return count; }

	public synchronized void setCount(int n) { count = n; }

}
