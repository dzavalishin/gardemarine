package ru.dz.shipMaster.dev.ntp.server;

public class NtpMessage {
	 

    private LeapIndicatorType leapIndicator;
    private int versionNumber;
    private ModeType mode;
    private StratumType stratumType;
    private byte pollInterval;
    private byte precision;
    private int rootDelay;
    private int rootDispersion;
    private ReferenceIdentifier referenceIdentifier;
    private NtpTimeStamp referenceTimestamp;
    private NtpTimeStamp originateTimestamp;
    private NtpTimeStamp receiveTimestamp;
    private NtpTimeStamp transmitTimestamp;

    

    public NtpMessage(LeapIndicatorType leapIndicator, int versionNumber, ModeType mode, StratumType stratumType, byte pollInterval, byte precision, int rootDelay, int rootDispersion, ReferenceIdentifier referenceIdentifier, NtpTimeStamp referenceTimestamp, NtpTimeStamp originateTimestamp, NtpTimeStamp receiveTimestamp, NtpTimeStamp transmitTimestamp) {

        super();

        this.leapIndicator = leapIndicator;
        this.versionNumber = versionNumber;
        this.mode = mode;
        this.stratumType = stratumType;
        this.pollInterval = pollInterval;
        this.precision = precision;
        this.rootDelay = rootDelay;
        this.rootDispersion = rootDispersion;
        this.referenceIdentifier = referenceIdentifier;
        this.referenceTimestamp = referenceTimestamp;
        this.originateTimestamp = originateTimestamp;
        this.receiveTimestamp = receiveTimestamp;
        this.transmitTimestamp = transmitTimestamp;

    }

    

    public LeapIndicatorType getLeapIndicator() {        return leapIndicator;    }
    public ModeType getMode() {        return mode;    }
    public NtpTimeStamp getOriginateTimestamp() {        return originateTimestamp;    }
    public byte getPollInterval() {        return pollInterval;    }
    public byte getPrecision() {        return precision;    }
    public NtpTimeStamp getReceiveTimestamp() {        return receiveTimestamp;    }
    public ReferenceIdentifier getReferenceIdentifier() {        return referenceIdentifier;    }
    public NtpTimeStamp getReferenceTimestamp() {        return referenceTimestamp;    }
    public int getRootDelay() {        return rootDelay;    }
    public int getRootDispersion() {        return rootDispersion;    }
    public StratumType getStratum() {        return stratumType;    }
    public NtpTimeStamp getTransmitTimestamp() {        return transmitTimestamp;    }
    public int getVersionNumber() {        return versionNumber;    }

}
