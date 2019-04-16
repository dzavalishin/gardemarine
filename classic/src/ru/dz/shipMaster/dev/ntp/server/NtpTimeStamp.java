package ru.dz.shipMaster.dev.ntp.server;

import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
public class NtpTimeStamp {




	private static final long NTP_EPOCH_DIFFERENCE = -2208988800000L;

    private static final TimeZone UTC_TIME_ZONE = TimeZone.getTimeZone("UTC");

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z");

    static {

        dateFormat.setTimeZone(UTC_TIME_ZONE);

    }

    private long seconds = 0;

    private long fraction = 0;

    

    public NtpTimeStamp() {

        this(new Date());

    }

    

    public NtpTimeStamp(Date date) {

        super();

        long msSinceStartOfNtpEpoch = date.getTime() - NTP_EPOCH_DIFFERENCE;

        seconds = msSinceStartOfNtpEpoch / 1000;

        fraction = ((msSinceStartOfNtpEpoch % 1000) * 4294967296L) / 1000;

    }

    

    public NtpTimeStamp(ByteBuffer data) {

        super();

        for (int ii = 0; ii < 4; ii++) {

            seconds = 256 * seconds + makePositive(data.get());

        }

        for (int ii = 4; ii < 8; ii++) {

            fraction = 256 * fraction + makePositive(data.get());

        }

    }

    

    public void writeTo(ByteBuffer buffer) {

        byte[] bytes = new byte[8];

        long temp = seconds;

        for (int ii = 3; ii >= 0; ii--) {

            bytes[ii] = (byte)(temp % 256);

            temp = temp / 256;

        }

        temp = fraction;

        for (int ii = 7; ii >= 4; ii--) {

            bytes[ii] = (byte)(temp % 256);

            temp = temp / 256;

        }

        buffer.put(bytes);

    }

    

    public String toString() {
        long msSinceStartOfNtpEpoch = seconds * 1000 + (fraction * 1000) / 4294967296L;
        Date date = new Date(msSinceStartOfNtpEpoch + NTP_EPOCH_DIFFERENCE);

        synchronized (dateFormat) {
            return "org.apache.ntp.message.NtpTimeStamp[ date = " + dateFormat.format(date) + " ]";
        }
    }

    

    public boolean equals(Object o) {
        //$ANALYSIS-IGNORE,codereview.java.rules.comparison.RuleComparisonReferenceEquality
		if (this == o) {
		  return true;
		}
		
        if (!(o instanceof NtpTimeStamp)) {            return false;        }

        NtpTimeStamp that = (NtpTimeStamp)o;
        return (this.seconds == that.seconds) && (this.fraction == that.fraction);
    }

	public int hashCode() { return (int)(seconds+fraction); }
    

    private int makePositive(byte b) {
        int byteAsInt = b;
        return (byteAsInt < 0) ? 256 + byteAsInt : byteAsInt;
    }
}
