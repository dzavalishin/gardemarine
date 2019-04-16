package ru.dz.shipMaster.dev.ntp.server;

import java.nio.ByteBuffer;

/*
 *     

    public void encode(IoSession session, Object message, ProtocolEncoderOutput out) {

        NtpMessageEncoder encoder = new NtpMessageEncoder();

        ByteBuffer buf = ByteBuffer.allocate(1024);

        encoder.encode(buf.buf(), (NtpMessage)message);

        buf.flip();

        out.write(buf);

    }
 */

public class NtpMessageEncoder {
    

    public void encode(ByteBuffer byteBuffer, NtpMessage message) {

        byte header = 0;

        header = encodeLeapIndicator(message.getLeapIndicator(), header);

        header = encodeVersionNumber(message.getVersionNumber(), header);

        header = encodeMode(message.getMode(), header);

        byteBuffer.put(header);

        byteBuffer.put((byte)(message.getStratum().getOrdinal() & 255));

        byteBuffer.put((byte)(message.getPollInterval() & 255));

        byteBuffer.put((byte)(message.getPrecision() & 255));

        byteBuffer.putInt(message.getRootDelay());

        byteBuffer.putInt(message.getRootDispersion());

        encodeReferenceIdentifier(message.getReferenceIdentifier(), byteBuffer);

        message.getReferenceTimestamp().writeTo(byteBuffer);

        message.getOriginateTimestamp().writeTo(byteBuffer);

        message.getReceiveTimestamp().writeTo(byteBuffer);

        message.getTransmitTimestamp().writeTo(byteBuffer);

    }

    

    private byte encodeLeapIndicator(LeapIndicatorType leapIndicator, byte header) {

        byte twoBits = (byte)(leapIndicator.getOrdinal() & 3);

        return (byte)((twoBits << 6) | header);

    }

    

    private byte encodeVersionNumber(int versionNumber, byte header) {

        byte threeBits = (byte)(versionNumber & 7);

        return (byte)((threeBits << 3) | header);

    }

    

    private byte encodeMode(ModeType mode, byte header) {

        byte threeBits = (byte)(mode.getOrdinal() & 7);

        return (byte)(threeBits | header);

    }

    

    private void encodeReferenceIdentifier(ReferenceIdentifier identifier, ByteBuffer byteBuffer) {

        char[] characters = identifier.getCode().toCharArray();

        for (int ii = 0; ii < characters.length; ii++) {

            byteBuffer.put((byte)characters[ii]);

        }

    }
}
