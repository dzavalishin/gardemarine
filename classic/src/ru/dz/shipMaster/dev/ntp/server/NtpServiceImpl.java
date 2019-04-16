package ru.dz.shipMaster.dev.ntp.server;

public class NtpServiceImpl implements NtpService {
    

    public NtpMessage getReplyFor(NtpMessage request) {

        NtpMessageModifier modifier = new NtpMessageModifier();

        modifier.setLeapIndicator(LeapIndicatorType.NO_WARNING);
        modifier.setVersionNumber(4);
        modifier.setMode(ModeType.SERVER);
        modifier.setStratum(StratumType.PRIMARY_REFERENCE);
        modifier.setPollInterval((byte)4);
        modifier.setPrecision((byte)250);
        modifier.setRootDelay(0);
        modifier.setRootDispersion(0);
        modifier.setReferenceIdentifier(ReferenceIdentifier.LOCL);

        NtpTimeStamp now = new NtpTimeStamp();

        modifier.setReferenceTimestamp(now);
        modifier.setOriginateTimestamp(request.getTransmitTimestamp());
        modifier.setReceiveTimestamp(request.getReceiveTimestamp());
        modifier.setTransmitTimestamp(now);

        return modifier.getNtpMessage();

    }
}
