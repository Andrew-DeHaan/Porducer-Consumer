/*
Andrew DeHaan
cs351
5/9/18
This program solves the producer consumer problem
It will run 2 producer threads and 1 consumer thread
This program runs until the user stops the program with ctrl+c
 */
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.Arrays;
public class ProducerConsumer
{
    public static int count = 0;
    public static int MAX = 10;
    public static int BUFFER[] = new int[MAX];
    private static ProducerConsumer threadsync;
    private static Thread thread1,thread2,thread3,thread4;
    private static Semaphore sema = new Semaphore(2,true);
      
    private void busy()
    {
	try
	    {
		Random rand = new Random();
		Thread.sleep(rand.nextInt(1000)+1000);
	    }
	catch(InterruptedException e)
	    {}
    }
    //purpose: Insert number into buffer if there is room
    void insert_number(int number)
    {
	if(count < MAX)
	    {
		System.out.println(Thread.currentThread().getName() + " Adding Number into buffer");	
		BUFFER[count] = number;
		System.out.println(number + " Was Added");
		System.out.println(Thread.currentThread().getName()+" finished Producing");	   
		count++; 
		print();
	    }
	else{
	}
    }
    //purpose: remove a number if the buffer has at least one number
    void remove_number()
    {
	if(count > 0)
	    {
		int removed = BUFFER[count-1];
		BUFFER[count-1] = 0;

		count--;
		System.out.println(removed+ " was removed");
		System.out.println(Thread.currentThread().getName()+" finished Consuming");
		print();
		
	    }
	else{
	}
    }
    private class Producer implements Runnable
    {
	ProducerConsumer pc;
	Producer(String name,ProducerConsumer pc1)
	{
	    super();
	    this.pc = pc1;
	}
	@Override
	public void run()
	{
	    while(true)
		{
		    try
			{
			    sema.acquire();
			}
		    catch(InterruptedException ex)
			{
			    contine;
			}
		    Random rand = new Random();
		    int  n = rand.nextInt(50) + 1;
		    busy();
		    pc.insert_number(n);
		    sema.release();
	    }
	}
    }
    private class Consumer implements Runnable
    {
	ProducerConsumer pc;
	Consumer(String name, ProducerConsumer pc1)
	{
	    super();
	    this.pc = pc1;
	}
	@Override
	public void run()
	{
	    while(true)
		{
		    try
			{
			    sema.acquire();
			}
		    catch(InterruptedException ex)
			{
			    continue;
			}
		    busy();
		    pc.remove_number();
		    sema.release();
		}
	}
    }
    public void print()
    {
	System.out.println("Current buffer contains ");
	System.out.println(java.util.Arrays.toString(BUFFER));	    
	System.out.println("-------------------------------------------");
	
    }
    public void startThreads()
    {
	ProducerConsumer test = new ProducerConsumer();
	thread1 = new Thread(new ProducerConsumer.Producer("Writer #1",test));
	thread2 = new Thread(new ProducerConsumer.Consumer("Reader #1",test));
	thread3 = new Thread(new ProducerConsumer.Producer("Writer #2",test));
	thread1.start();
	thread2.start();
	thread3.start();
    }
public static void main(String[] args)
    {
	threadsync = new ProducerConsumer();
	threadsync.startThreads();
    }
}
