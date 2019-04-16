package ru.dz.shipMaster.dev.ntp.server;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class ReferenceIdentifier implements Comparable<ReferenceIdentifier> {
	 

    public static final ReferenceIdentifier INIT = new ReferenceIdentifier(0, "INIT", "initializing");
    public static final ReferenceIdentifier LOCL = new ReferenceIdentifier(1, "LOCL", "uncalibrated local clock");
    public static final ReferenceIdentifier PPS = new ReferenceIdentifier(2, "PPL", "pulse-per-second source");
    public static final ReferenceIdentifier ACTS = new ReferenceIdentifier(3, "ACTS", "NIST dialup modem service");
    public static final ReferenceIdentifier USNO = new ReferenceIdentifier(4, "USNO", "USNO modem service");
    public static final ReferenceIdentifier PTB = new ReferenceIdentifier(5, "PTB", "PTB (Germany) modem service");
    public static final ReferenceIdentifier TDF = new ReferenceIdentifier(6, "TDF", "Allouis (France) Radio 164 kHz");
    public static final ReferenceIdentifier DCF = new ReferenceIdentifier(7, "DCF", "Mainflingen (Germany) Radio 77.5 kHz");
    public static final ReferenceIdentifier MSF = new ReferenceIdentifier(8, "MSF", "Rugby (UK) Radio 60 kHz");
    public static final ReferenceIdentifier WWV = new ReferenceIdentifier(9, "WWV", "Ft. Collins (US) Radio 2.5, 5, 10, 15, 20 MHz");
    public static final ReferenceIdentifier WWVB = new ReferenceIdentifier(10, "WWVB", "Boulder (US) Radio 60 kHz");
    public static final ReferenceIdentifier WWVH = new ReferenceIdentifier(11, "WWVH", "Kaui Hawaii (US) Radio 2.5, 5, 10, 15 MHz");
    public static final ReferenceIdentifier CHU = new ReferenceIdentifier(12, "CHU", "Ottawa (Canada) Radio 3330, 7335, 14670 kHz");
    public static final ReferenceIdentifier LORC = new ReferenceIdentifier(13, "LORC", "LORAN-C radionavigation system");
    public static final ReferenceIdentifier OMEG = new ReferenceIdentifier(14, "OMEG", "OMEGA radionavigation system");
    public static final ReferenceIdentifier GPS = new ReferenceIdentifier(15, "GPS", "Global Positioning Service");
    public static final ReferenceIdentifier GOES = new ReferenceIdentifier(16, "GOES", "Geostationary Orbit Environment Satellite");
    public static final ReferenceIdentifier CDMA = new ReferenceIdentifier(17, "CDMA", "CDMA mobile cellular/PCS telephone system");
    private static final ReferenceIdentifier[] values = {INIT, LOCL, PPS, ACTS, USNO, PTB, TDF, DCF, MSF, WWV, WWVB, WWVH, CHU, LORC, OMEG, GPS, GOES, CDMA};

    public static final List<ReferenceIdentifier> VALUES = Collections.unmodifiableList(Arrays.asList(values));

    private final int ordinal;

    private final String name;

    private final String code;

    

    private ReferenceIdentifier(int ordinal, String code, String name) {

        super();
        this.ordinal = ordinal;
        this.code = code;
        this.name = name;

    }

    

    public static ReferenceIdentifier getTypeByOrdinal(int type) {

        for (int ii = 0; ii < values.length; ii++) {

            if (values[ii].ordinal == type) {

                return values[ii];

            }

        }

        return LOCL;

    }

    

    public static ReferenceIdentifier getTypeByName(String type) {

        for (int ii = 0; ii < values.length; ii++) {

            if (values[ii].code.equalsIgnoreCase(type)) {

                return values[ii];

            }

        }

        return LOCL;

    }

    

    public int getOrdinal() {

        return ordinal;

    }

    

    public String getCode() {

        return code;

    }

    

    public int compareTo(ReferenceIdentifier that) { return ordinal - that.ordinal; }

    

    public String toString() { return name;  }
}
