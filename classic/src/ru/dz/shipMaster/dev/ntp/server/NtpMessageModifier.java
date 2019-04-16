package ru.dz.shipMaster.dev.ntp.server;

public class NtpMessageModifier {
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

    

    public NtpMessage getNtpMessage() {

        return new NtpMessage(leapIndicator, versionNumber, mode, stratumType, pollInterval, precision, rootDelay, rootDispersion, referenceIdentifier, referenceTimestamp, originateTimestamp, receiveTimestamp, transmitTimestamp);

    }

    

    public void setLeapIndicator(LeapIndicatorType leapIndicator) {

        this.leapIndicator = leapIndicator;

    }

    

    public void setMode(ModeType mode) {

        this.mode = mode;

    }

    

    public void setOriginateTimestamp(NtpTimeStamp originateTimestamp) {

        this.originateTimestamp = originateTimestamp;

    }

    

    public void setPollInterval(byte pollInterval) {

        this.pollInterval = pollInterval;

    }

    

    public void setPrecision(byte precision) {

        this.precision = precision;

    }

    

    public void setReceiveTimestamp(NtpTimeStamp receiveTimestamp) {

        this.receiveTimestamp = receiveTimestamp;

    }

    

    public void setReferenceIdentifier(ReferenceIdentifier referenceIdentifier) {

        this.referenceIdentifier = referenceIdentifier;

    }

    

    public void setReferenceTimestamp(NtpTimeStamp referenceTimestamp) {        this.referenceTimestamp = referenceTimestamp;    }
    public void setRootDelay(int rootDelay) {        this.rootDelay = rootDelay;    }  
    public void setRootDispersion(int rootDispersion) {        this.rootDispersion = rootDispersion;    }
    public void setStratum(StratumType stratumType) {        this.stratumType = stratumType;    }
    public void setTransmitTimestamp(NtpTimeStamp transmitTimestamp) {        this.transmitTimestamp = transmitTimestamp;    }   
    public void setVersionNumber(int versionNumber) {      this.versionNumber = versionNumber;    }
}
