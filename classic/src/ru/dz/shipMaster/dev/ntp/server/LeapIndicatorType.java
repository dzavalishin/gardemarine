package ru.dz.shipMaster.dev.ntp.server;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LeapIndicatorType implements Comparable<LeapIndicatorType> {
    public static final LeapIndicatorType NO_WARNING = new LeapIndicatorType(0, "No leap second warning.");

    public static final LeapIndicatorType POSITIVE_LEAP_SECOND = new LeapIndicatorType(1, "Last minute has 61 seconds.");

    public static final LeapIndicatorType NEGATIVE_LEAP_SECOND = new LeapIndicatorType(2, "Last minute has 59 seconds.");

    public static final LeapIndicatorType ALARM_CONDITION = new LeapIndicatorType(3, "Alarm condition (clock not synchronized).");

    private static final LeapIndicatorType[] values = {NO_WARNING, POSITIVE_LEAP_SECOND, NEGATIVE_LEAP_SECOND, ALARM_CONDITION};

    public static final List<LeapIndicatorType> VALUES = Collections.unmodifiableList(Arrays.asList(values));

    private final String name;

    private final int ordinal;

    

    private LeapIndicatorType(int ordinal, String name) {

        super();

        this.ordinal = ordinal;

        this.name = name;

    }

    

    public static LeapIndicatorType getTypeByOrdinal(int type) {

        for (int ii = 0; ii < values.length; ii++) {

            if (values[ii].ordinal == type) {

                return values[ii];

            }

        }

        return NO_WARNING;

    }

    

    public int getOrdinal() {

        return ordinal;

    }

    

    public int compareTo(LeapIndicatorType that) {
        return ordinal - that.ordinal;
    }

    

    public String toString() {
        return name;
    }
}
