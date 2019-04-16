package ru.dz.shipMaster.ui.config.filter;

import java.util.Set;
import java.util.TreeSet;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import ru.dz.shipMaster.data.filter.ApproximationPoint;
import ru.dz.shipMaster.data.filter.ApproximatorFilter;
import ru.dz.shipMaster.ui.misc.VisualHelpers;

public class CieApproximatorFilter extends CieGeneralFilter {

	private ApproximatorFilter bean;
	
	private JTextArea pointsListField = new JTextArea(6,20);

	public CieApproximatorFilter(ApproximatorFilter afbean) {
		super(afbean);
		bean = afbean;
		
		panel.add(new JLabel("Points:"),consL);
		panel.add( new JScrollPane( pointsListField ),consR);

	}
	
	@Override
	public String getTypeName() {		return "CieApproximatorFilter";	}

	@Override
	protected void informContainer() {		
		bean.informContainers(); 
		}

	@Override
	public void displayPropertiesDialog() {
		super.displayPropertiesDialog();
	}

	@Override
	public void applySettings() {
		super.applySettings();
		
		String text = pointsListField.getText();
		
		String[] split = text.split("\\n");
		
		Set<ApproximationPoint> points = new TreeSet<ApproximationPoint>();
		
		for( String line : split )
		{
			String[] splitLine = line.split(",");
			
			if(splitLine.length != 2)
			{
				VisualHelpers.showMessageDialog(panel, "Each line must have 'number, number' format");
				throw new NumberFormatException();
			}
			
			double in = Double.parseDouble(splitLine[0]);
			double out = Double.parseDouble(splitLine[1]);
			
			points.add(new ApproximationPoint(in, out));
		}
		
		bean.setPoints(points);
	}
	
	@Override
	public void discardSettings() {
		super.discardSettings();
	
		Set<ApproximationPoint> points = bean.getPoints();
		
		StringBuilder sb = new StringBuilder();
		
		for( ApproximationPoint p : points )
		{
			sb.append(String.format("%g, %g\n", p.getIn(), p.getOut()));
		}
		pointsListField.setText(sb.toString());
	}
}
