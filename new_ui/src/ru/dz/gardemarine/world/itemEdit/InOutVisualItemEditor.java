package ru.dz.gardemarine.world.itemEdit;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyDescriptor;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import ru.dz.gardemarine.ui.VisualHelpers;
import ru.dz.gardemarine.world.IConfig;
import ru.dz.gardemarine.world.IInOut;
import ru.dz.gardemarine.world.IInput;
import ru.dz.gardemarine.world.IOutput;

public class InOutVisualItemEditor extends AbstractVisualItemEditor {

	JMenuItem connect = new JMenuItem();
	JMenuItem disconnect = new JMenuItem();

	JLabel valueLabel = new JLabel();

	public InOutVisualItemEditor(JPanel p,
			IConfig owner, PropertyDescriptor pd) {
		super(owner, pd);

		// This is hack to make setPropertyType work, as it needs actual this.value to be there 
		super.setValue( readValue() );


		setPropertyType(pd.getPropertyType());
		setValue(value);

		valueLabel.setForeground(FG_FIELD);

		specialLabel.addMouseListener(popupListener);


		p.add(nameLabel,consL);
		p.add(valueLabel,consM);
		//p.add(specialLabel,consR);

		JPanel spanel = new JPanel(new BorderLayout());
		p.add(spanel,consR);

		JButton connButton = new JButton(VisualHelpers.loadIcon("bitcons/add.gif")); 
		JButton disconnButton = new JButton(VisualHelpers.loadIcon("bitcons/bstop.gif"));
		
		spanel.add(connButton,BorderLayout.CENTER);
		spanel.add(disconnButton,BorderLayout.EAST);
		
		fixButton(connButton);
		fixButton(disconnButton);

		connButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tryToConnect();				
			}});

		disconnButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				disconnect();				
			}});
		
		spanel.setBackground(BG_TRANSPARENT);
		//spanel.doLayout();
	}

	private void fixButton(JButton b) {
		b.setBorderPainted(false);
		b.setContentAreaFilled(false);
		b.setMargin(new Insets(0,0,0,0));
	}

	public void setValue(Object value) {		
		super.setValue(value);
		valueLabel.setText((value == null) ? "(none)" : value.toString());
		setIoType();
	}
	
	protected static InOutVisualItemEditor connectPoint = null;

	private void tryToConnect()
	{
		doTryConnect();
		nameLabel.setBackground( ( connectPoint == this ) ? Color.DARK_GRAY : BG_NORMAL );
	}

	private void doTryConnect()
	{
		if(( connectPoint == this ) )
			return;

		if( connectPoint == null )
		{
			connectPoint = this;
			return;
		}

		if( connectPoint.isIn && isOut )
		{
			((IOutput)value).setPeer((IInput)connectPoint.value);
			connectPoint = null;
		}
		else if( connectPoint.isOut && isIn )
		{
			((IOutput)connectPoint.value).setPeer((IInput)value);
			connectPoint = null;
		}
	}

	protected void disconnect() {
		connectPoint = null;
		if( isOut )
		{
			((IOutput)value).setPeer(null);
		}
		if( isIn )
		{
			((IInput)value).setPeer(null);
		}
	}




	private void setPropertyType(Class<?> pt) {
		isIn = isOut = false;
		setIoType();

		if( pt.isAssignableFrom(IOutput.class) )
		{
			//specialLabel.setText(">");
			isOut = true; 
			setIoType();
		}
		else if( pt.isAssignableFrom(IInput.class) )
		{
			//specialLabel.setText("<");
			isIn = true;
			setIoType();
		}
		else
			throw new RuntimeException("Impossible type");
	}

	private boolean isOut = false;
	private boolean isIn = false;
	


	private void setIoType() {
		//connect.setEnabled(true);
		disconnect.setEnabled( ((IInOut)value).isConnected() );
	}

}
