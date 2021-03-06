package ru.dz.shipMaster.config;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import ru.dz.shipMaster.ui.config.Utf8ResourceBundle;

public class Messages {
	private static final String BUNDLE_NAME = "ru.dz.shipMaster.config.messages"; //$NON-NLS-1$

	//private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	private static final ResourceBundle RESOURCE_BUNDLE = Utf8ResourceBundle.getBundle(BUNDLE_NAME);
	
	private Messages() {
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
