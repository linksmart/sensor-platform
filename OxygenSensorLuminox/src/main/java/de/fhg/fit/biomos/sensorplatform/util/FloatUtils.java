package de.fhg.fit.biomos.sensorplatform.util;

/**
 * Created by garagon on 29.11.2016.
 */
public class FloatUtils {

    public static float valueTemp(String data) {
        //return FloatUtils.parseSFLOATtoFloat((int)Long.parseLong(data.substring(8,16),16));
        return (int)Long.parseLong(data.substring(7,11).replace(".",""),16)*0.1f;
        //return 23.4f;
    }
    public static float valueOxyg(String data) {
        return (int)Long.parseLong(data.substring(0,5).replace(".",""),16)*0.01f;
    }
    public static float valuePress(String data) {
        return (int)Long.parseLong(data.substring(12,16).replace(".",""),16)*1.0f;
    }

    public static String toHexString(byte[] ba) {
        StringBuilder str = new StringBuilder();
        for(int i = 0; i < ba.length; i++)
            str.append(String.format("%x", ba[i]));
        return str.toString();
    }
    public static String fromHexString(String hex) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < hex.length(); i+=2) {
            str.append((char)Integer.parseInt(hex.substring(i, i + 2), 16));
        }
        return str.toString();
    }

 /*   public static float valueTemp(int[] data) {
        //return FloatUtils.parseSFLOATtoFloat((int)Long.parseLong(data.substring(8,16),16));
        return 0.1f;//(int)Long.parseLong(data.substring(7,11).replace(".",""))*0.1f;
        //return 23.4f;
    }
    public static float valueOxyg(int[] data) {
        return 0.1f;// (int)Long.parseLong(data.substring(0,5).replace(".",""))*0.01f;
    }
    public static float valuePress(int[] data) {
        return 0.1f;// (int)Long.parseLong(data.substring(12,16).replace(".",""))*1.0f;
    }

*/
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
