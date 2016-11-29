package de.fhg.fit.biomos.sensorplatform.util;

/**
 * Created by garagon on 29.11.2016.
 */
public class FloatUtils {
    private static int getExponent(int value)
    {
        if (value < 0)
        { // if exponent should be negative
            return (int) (((value >> 24) & 0xFF) | 0xFFFFFF00);

        }
        return (int) ((value >> 24) & 0x000000FF);

    }

    private static int getMantissa(int value)
    {
        if ((value & 0x0800000) != 0)
        { // if mantissa should be negative
            return (int) ((value & 0x00FFFFFF) | 0xFF000000);
        }
        return (int) (value & 0x00FFFFFF);
    }

    public static float parseSFLOATtoFloat(int value)
    {
        // NaN
        if (value == 0x007FFFFF)
        {
            return Float.NaN;
        }
        // NRes (not at this resolution)
        else if (value == 0x00800000)
        {
            return Float.NaN;
        }
        // +INF
        else if (value == 0x007FFFFE)
        {
            return Float.POSITIVE_INFINITY;
        }
        // -INF
        else if (value == 0x00800002)
        {
            return Float.NEGATIVE_INFINITY;
        }
        // Reserved
        else if (value == 0x00800001)
        {
            return Float.NaN;
        }
        else
        {
            return (float) (((float) getMantissa(value)) * Math.pow(10, getExponent((int)value)));
        }
    }

}
