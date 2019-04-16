package ru.dz.gardemarine.world;

public class MinimalConfig<TItem extends IItem> extends AbstractConfig<IItem> {

	@Override
	public IItem produceItem() {
		return new MinimalItem();
	}

	private IInput<IInterfaceType> in = new Input();
	private IOutput<IInterfaceType> out = new Output();
	
	{
		in.setOwner(this);
		out.setOwner(this);
	}
	
	public IInput<IInterfaceType> getIn() {		return in;	}
	public void setIn(IInput<IInterfaceType> in) {
		if(this.in == in) return;
		
		if(this.in != null)
			this.in.setPeer(null); // disconnect old one
			
		IInput<IInterfaceType> oldIn = this.in;
		this.in = in;
		in.setOwner(this);
		firePropertyChange("in", oldIn, in);
	}

	public IOutput<IInterfaceType> getOut() {		return out;	}
	public void setOut(IOutput<IInterfaceType> out) {
		if(this.out == out) return;

		if(this.out != null)
			this.out.setPeer(null); // disconnect old one
			

		IOutput<IInterfaceType> oldOut = this.out;
		this.out = out;
		out.setOwner(this);
		firePropertyChange("out", oldOut, out);
	}
	
	
	public enum TestEnum { red, blue, green };
	
	private TestEnum test = TestEnum.blue;

	public TestEnum getTest() {		return test;	}
	public void setTest(TestEnum test) {
		TestEnum old = this.test;
		this.test = test;
		firePropertyChange("test", old, test);
	}
	
	
	public enum TestEnum1 { RS485, RS232, TCPIP };
	
	private TestEnum1 test1 = TestEnum1.RS485;

	public TestEnum1 getTest1() {		return test1;	}
	public void setTest1(TestEnum1 test) {
		TestEnum1 old = this.test1;
		this.test1 = test;
		firePropertyChange("test", old, test);
	}
	
}
