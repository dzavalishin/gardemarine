package ru.dz.shipMaster.dev.ntp.server;

public interface NtpService {
	 public NtpMessage getReplyFor(NtpMessage request);
}
