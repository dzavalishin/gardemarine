package ru.dz.shipMaster.net.sign;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Simple digital signature implementation.
 * @author dz
 */
public class NetSigner {

	//private final String password;
	private byte[] passwordBytes;

	public NetSigner(String password) throws UnsupportedEncodingException
	{
		//this.password = password;
		setPassword(password);
	}

	public NetSigner()
	{
	}
	
	public void setPassword(String password) throws UnsupportedEncodingException
	{
		passwordBytes = password.getBytes("UTF-8");
	}

	public byte [] sign( byte [] message ) throws NoSuchAlgorithmException
	{
		MessageDigest md = MessageDigest.getInstance("MD5");

		md.update(message);
		md.update(passwordBytes);
		
		return md.digest();
	}
	
	public boolean check(byte [] message, byte [] oldDigest ) throws NoSuchAlgorithmException
	{
		MessageDigest md = MessageDigest.getInstance("MD5");

		md.update(message);
		md.update(passwordBytes);
		
		byte[] newDigest = md.digest();
		
		return newDigest.equals(oldDigest);
	}
	
}
