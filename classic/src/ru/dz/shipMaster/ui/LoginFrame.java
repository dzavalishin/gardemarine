package ru.dz.shipMaster.ui;

import java.awt.Frame;
import java.awt.GridBagConstraints;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import ru.dz.shipMaster.config.ConfigurationFactory;
import ru.dz.shipMaster.config.items.CliUser;
import ru.dz.shipMaster.ui.misc.VisualHelpers;

@SuppressWarnings("serial")
public class LoginFrame extends SelectFrame<CliUser> {

	public LoginFrame(Frame frame) {
		super(ConfigurationFactory.getConfiguration().getUserItems(), frame, "Log in please");
        //this.setIconImage(VisualHelpers.getApplicationIconImage());

		selectButton.setText("Login");
		refuseButton.setText("Don't login");
	}

	private JPasswordField pwd;
	@Override
	protected void addImplementationComponents(JPanel topPanel, GridBagConstraints constraints) {
		pwd = new JPasswordField(14);
		topPanel.add(new JLabel("Password:"),constraints);
		topPanel.add(pwd,constraints);
	}

	@SuppressWarnings("deprecation")
	protected void select() {
		int selectedIndex = list.getSelectedIndex();
		CliUser user = items.elementAt(selectedIndex);
		
		final String uPassword = user.getPassword().trim();
		final String enteredPassword = pwd.getText().trim();
		if(!uPassword.equalsIgnoreCase(enteredPassword))
		{
			VisualHelpers.showMessageDialog(panel, "Incorrect password");
			return;
		}
		
		setVisible(false);
		dispose();
		
		result = user;
	}
	
	
}
