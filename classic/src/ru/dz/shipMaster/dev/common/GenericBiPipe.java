package ru.dz.shipMaster.dev.common;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import ru.dz.shipMaster.data.misc.CommunicationsException;
import ru.dz.shipMaster.events.ListenerList;

public abstract class GenericBiPipe implements BiPipe {
	private static final Byte NEWLINE = new Byte((byte)'\n');
	protected static final Logger log = Logger.getLogger(GenericBiPipe.class.getName()); 

	// -----------------------------------------------------------
	// Etc. 
	// -----------------------------------------------------------

	@Override
	public String getName() { return getTypeName() + ": " + getEndPointName(); }

	protected boolean connected; 

	@Override
	public boolean isConected() { return connected; }




	// -----------------------------------------------------------
	// Events distribution 
	// -----------------------------------------------------------

	private ListenerList receiveListeners = new ListenerList();

	@Override
	public void addReceiveListener(BiPipeReceiveEventListener l) { receiveListeners.addListener(l); }

	@Override
	public void removeReceiveListener(BiPipeReceiveEventListener l) { receiveListeners.removeListener(l); }

	//protected void kickReceiveEvent(BiPipeReceiveEvent e) { receiveListeners.sendEvent(e); }
	protected void kickReceiveEvent() { receiveListeners.sendEvent(new BiPipeReceiveEvent(this)); }


	private ListenerList errorListeners = new ListenerList();

	@Override
	public void addErrorListener(BiPipeReceiveEventListener l) { errorListeners.addListener(l); }

	@Override
	public void removeErrorListener(BiPipeReceiveEventListener l) { errorListeners.removeListener(l); }

	protected void kickErrorEvent(Exception e) {
		BiPipeErrorEvent event = new BiPipeErrorEvent(this,e);
		errorListeners.sendEvent(event); 
	}

	// -----------------------------------------------------------
	// Threads 
	// -----------------------------------------------------------

	StoppableLoopThread		reader = new StoppableLoopThread() {
		@Override
		protected void step() { readStep(); }

		@Override
		protected void unblock() { /* unused */}
	};


	protected abstract void readStep();


	StoppableLoopThread		writer = new StoppableLoopThread() {
		@Override
		protected void step() { writeStep(); }

		@Override
		protected void unblock() { /* unused */}
	};


	protected abstract void writeStep();


	protected void startThreads()
	{
		reader.start();
		writer.start();
	}

	protected void stopThreads()
	{
		reader.stop();
		writer.stop();
	}

	// -----------------------------------------------------------
	// Queues 
	// -----------------------------------------------------------

	protected BlockingQueue<Byte> recvQueue = new ArrayBlockingQueue<Byte>(2000);
	protected BlockingQueue<byte[]> sendQueue = new ArrayBlockingQueue<byte[]>(100);

	@Override
	public void read(byte[] data) throws CommunicationsException {
		int ret = 0;

		while(ret < data.length)
		{
			try {
				data[ret++] = recvQueue.take();
			} catch (InterruptedException e) {
				throw new CommunicationsException("Read failed", e);
			}
		}
	}

	@Override
	public int readTimed(int timeoutMsec) {
		Byte poll;
		try {
			poll = recvQueue.poll(timeoutMsec, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			return -1;
		}

		if(poll == null) return -1;
		return 0xFF & poll.byteValue();
	}


	public int readUnblocked(byte data[])
	{
		int ret = 0;

		while(ret < data.length)
		{
			Byte b = recvQueue.poll();
			if(b == null)
				break;
			data[ret++] = b;
		}
		return ret;
	}


	@Override
	public String readLineUnblocked() {
		if(!recvQueue.contains(NEWLINE))
			return null;

		StringBuilder sb = new StringBuilder();

		while(true)
		{
			Byte b = recvQueue.poll();
			if(b == null)
			{
				log.log(Level.SEVERE,"premature end of string");
				break;
			}

			// Just skip CR
			if(b == (byte)'\r')
				continue;

			if(b == (byte)'\n')
				break;

			char c = (char)(byte)b;
			sb.append( c );
		}

		return sb.toString();
	}


	@Override
	public int available() { return recvQueue.size(); }

	@Override
	public int peek() {
		Byte b = recvQueue.peek();
		return b == null ? -1 : (0xFF & b.byteValue());
	}









	@Override
	public void write(byte[] data) throws CommunicationsException {
		try {
			sendQueue.put(data);
		} catch (InterruptedException e) {
			throw new CommunicationsException("Write failed", e);
		}
	}

	@Override
	public void write(byte[] data, int size) throws CommunicationsException
	{
		if(size == data.length)
			write(data);
		else
		{
			byte[] b = new byte[1];
			for(int i = 0; i < size; i++)
			{
				b[0] = data[i];
				try {
					sendQueue.put(b);
				} catch (InterruptedException e) {
					throw new CommunicationsException("Write failed", e);
				}
			}
			b = null;
		}
	}

	@Override
	public void write(byte data) throws CommunicationsException {
		byte[] b = new byte[1];
		b[0] = data;
		try {
			sendQueue.put(b);
		} catch (InterruptedException e) {
			throw new CommunicationsException("Write failed", e);
		}
		b = null;
	}

	@Override
	public int writeUnblocked(byte data[])
	{
		if( sendQueue.offer(data) )
			return data.length;
		return 0;
	}

	@Override
	public void flush() {

		synchronized (sendQueue) {
			while(!sendQueue.isEmpty())
			{
				try {
					// Wait no more tnan a second to protect from
					// some wrong implementation of queue drain code
					sendQueue.wait(1000);
				} catch (InterruptedException e) {
					// ignore
				}
			}
		}
	}


	// -----------------------------------------------------------
	// Half duplex support 
	// -----------------------------------------------------------

	private static final int HALF_DUPLEX_TIMEOUT_MSEC = 30; 

	protected volatile boolean halfDuplexMode = false;
	private boolean halfDuplexInSend = false;

	public boolean isHalfDuplexMode() {		return halfDuplexMode;	}

	/**
	 * Enable or disable half duplex mode.
	 *  
	 * @throws NoHalfDuplexException Thrown by default implementation to show no support for half duplex.
	 */
	public void setHalfDuplexMode(boolean halfDuplexMode) throws NoHalfDuplexException {
		doSetHalfDuplexMode(halfDuplexMode);
		this.halfDuplexMode = halfDuplexMode;	
	}

	/**
	 * Implementation must redefine this method if it is to support half duplex mode. 
	 * Redefined method is called before half duplex enable flag is changed and must 
	 * not throw NoHalfDuplexException, of course.
	 *  
	 * @param newHalfDuplexMode Set or reset.
	 * @throws NoHalfDuplexException Thrown by default implementation to show no support for half duplex.
	 */
	protected void doSetHalfDuplexMode(boolean newHalfDuplexMode) 
	throws NoHalfDuplexException {
		// By default we don't support it.
		throw new NoHalfDuplexException();		
	}

	/**
	 * Must be called before initiating write to actual hardware.
	 */
	protected void halfDuplexStartSend()
	{
		halfDuplexInSend  = true;
		if(!halfDuplexMode)
			return;
		while(halfDuplexMode && dataInWas() < HALF_DUPLEX_TIMEOUT_MSEC)
			writer.sleep(HALF_DUPLEX_TIMEOUT_MSEC*2);
	}


	/**
	 * Must be called after write to actual hardware. To be frank, it must be called
	 * after driver/hardware/whatever buffer is drained.
	 */
	protected void halfDuplexEndSend()
	{
		halfDuplexInSend  = false;
	}

	protected boolean halfDuplexDiscardInput()
	{
		return halfDuplexMode && halfDuplexInSend;
	}

	protected void halfDuplexNoteReception()
	{
		lastDataIn = System.currentTimeMillis();
	}

	protected long lastDataIn;
	protected long dataInWas()
	{
		return System.currentTimeMillis() - lastDataIn;
	}


	// -----------------------------------------------------------
	// Tools 
	// -----------------------------------------------------------


	/**
	 * Ready-made writeStep implementation for any device which
	 * is stream-operated on output.
	 */
	protected void writeStep(OutputStream outputStream) {
		byte[] bs;
		bs = getChunkToSend();

		try {
			halfDuplexStartSend();
			outputStream.write(bs,0,bs.length);
			halfDuplexEndSend();
		} catch (IOException e) {
			kickErrorEvent(e);
		}
	}

	/**
	 * Get from queue data to be sent.
	 * @return data or null
	 */
	protected byte[] getChunkToSend() {
		try {
			byte[] bs = sendQueue.take();
			synchronized (sendQueue) {
				sendQueue.notifyAll();

			}
			return bs;
		} catch (InterruptedException e) {
			// Ignore
			return null;
		}
	}




}
