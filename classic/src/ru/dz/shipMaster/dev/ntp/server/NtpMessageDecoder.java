package ru.dz.shipMaster.dev.ntp.server;

import java.nio.ByteBuffer;

/*
 *     public void decode(IoSession session, ByteBuffer in, ProtocolDecoderOutput out) {

        NtpMessageDecoder decoder = new NtpMessageDecoder();

        out.write(decoder.decode(in.buf()));

    }
 */

public class NtpMessageDecoder {
    

    public NtpMessage decode(ByteBuffer request) {

        NtpMessageModifier modifier = new NtpMessageModifier();

        byte header = request.get();

        modifier.setLeapIndicator(parseLeapIndicator(header));

        modifier.setVersionNumber(parseVersionNumber(header));

        modifier.setMode(parseMode(header));

        modifier.setStratum(parseStratum(request));

        modifier.setPollInterval(parsePollInterval(request));

        modifier.setPrecision(parsePrecision(request));

        modifier.setRootDelay(parseRootDelay(request));

        modifier.setRootDispersion(parseRootDispersion(request));

        modifier.setReferenceIdentifier(parseReferenceIdentifier(request));

        modifier.setReferenceTimestamp(new NtpTimeStamp(request));

        modifier.setOriginateTimestamp(new NtpTimeStamp(request));

        byte[] unneededBytes = new byte[8];

        request.get(unneededBytes);

        modifier.setReceiveTimestamp(new NtpTimeStamp());

        modifier.setTransmitTimestamp(new NtpTimeStamp(request));

        return modifier.getNtpMessage();

    }

    

    private LeapIndicatorType parseLeapIndicator(byte header) {

        return LeapIndicatorType.getTypeByOrdinal((header & 192) >>> 6);

    }

    

    private int parseVersionNumber(byte header) {

        return (header & 56) >>> 3;

    }

    

    private ModeType parseMode(byte header) {

        return ModeType.getTypeByOrdinal(header & 7);

    }

    

    private StratumType parseStratum(ByteBuffer request) {

        return StratumType.getTypeByOrdinal(request.get());

    }

    

    private byte parsePollInterval(ByteBuffer bytes) {

        return (byte)Math.round(Math.pow(2, bytes.get()));

    }

    

    private byte parsePrecision(ByteBuffer bytes) {

        return (byte)(1000 * Math.pow(2, bytes.get()));

    }

    

    private ReferenceIdentifier parseReferenceIdentifier(ByteBuffer request) {

        byte[] nextFourBytes = new byte[4];

        request.get(nextFourBytes);

        return ReferenceIdentifier.getTypeByName(new String(nextFourBytes));

    }

    

    private int parseRootDelay(ByteBuffer bytes) {

        int temp = 256 * (256 * (256 * bytes.get() + bytes.get()) + bytes.get()) + bytes.get();

        return 1000 * (temp / 65536);

    }

    

    private int parseRootDispersion(ByteBuffer bytes) {

        int temp = 256 * (256 * (256 * bytes.get() + bytes.get()) + bytes.get()) + bytes.get();

        return 1000 * (temp / 65536);

    }
}
