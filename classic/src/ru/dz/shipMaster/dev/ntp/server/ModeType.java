package ru.dz.shipMaster.dev.ntp.server;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class ModeType implements Comparable<ModeType> {
    public static final ModeType RESERVED = new ModeType(0, "Reserved mode.");
    public static final ModeType SYMMETRIC_ACTIVE = new ModeType(1, "Symmetric active mode.");
    public static final ModeType RESERVED_PASSIVE = new ModeType(2, "Symmetric passive mode.");
    public static final ModeType CLIENT = new ModeType(3, "Client mode.");
    public static final ModeType SERVER = new ModeType(4, "Server mode.");
    public static final ModeType BROADCAST = new ModeType(5, "Broadcast mode.");
    public static final ModeType RESERVED_FOR_NTP_CONTROL = new ModeType(6, "Reserved for NTP control message.");
    public static final ModeType RESERVED_FOR_PRIVATE_USE = new ModeType(7, "Reserved for private use.");

    private static final ModeType[] values = {RESERVED, SYMMETRIC_ACTIVE, RESERVED_PASSIVE, CLIENT, SERVER, BROADCAST, RESERVED_FOR_NTP_CONTROL, RESERVED_FOR_PRIVATE_USE};

    public static final List<ModeType> VALUES = Collections.unmodifiableList(Arrays.asList(values));

    private final String name;

    private final int ordinal;

    

    private ModeType(int ordinal, String name) {

        super();

        this.ordinal = ordinal;

        this.name = name;

    }

    

    public static ModeType getTypeByOrdinal(int type) {

        for (int ii = 0; ii < values.length; ii++) {

            if (values[ii].ordinal == type) {

                return values[ii];

            }

        }

        return SERVER;

    }

    

    public int getOrdinal() {

        return ordinal;

    }

    

    public int compareTo(ModeType that) {
        return ordinal - that.ordinal;
    }

    

    public String toString() {        return name;    }
}
