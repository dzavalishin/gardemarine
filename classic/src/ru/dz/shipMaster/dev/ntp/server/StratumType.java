package ru.dz.shipMaster.dev.ntp.server;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class StratumType implements Comparable<StratumType> {
    public static final StratumType UNSPECIFIED = new StratumType(0, "Unspecified or unavailable.");

    public static final StratumType PRIMARY_REFERENCE = new StratumType(1, "Primary reference.");

    public static final StratumType SECONDARY_REFERENCE = new StratumType(2, "Secondary reference.");

    private static final StratumType[] values = {UNSPECIFIED, PRIMARY_REFERENCE, SECONDARY_REFERENCE};

    public static final List<StratumType> VALUES = Collections.unmodifiableList(Arrays.asList(values));

    private final int ordinal;

    private final String name;

    

    private StratumType(int ordinal, String name) {

        super();

        this.ordinal = ordinal;

        this.name = name;

    }

    

    public static StratumType getTypeByOrdinal(int type) {

        for (int ii = 0; ii < values.length; ii++) {

            if (values[ii].ordinal == type) {

                return values[ii];

            }

        }

        return UNSPECIFIED;

    }

    

    public int getOrdinal() {
        return ordinal;
    }

    

    public int compareTo(StratumType that) {
        return ordinal - that.ordinal;
    }

    

    public String toString() {
        return name;
    }
}
