package com.sy.common.encript.conf;

import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public abstract class ConvertUtils {
    /**
     * simpleFormat
     */
    private static final DecimalFormat simpleFormat = new DecimalFormat("####");

    /**
     * objectToBoolean
     * @param o Object
     * @return Boolean
     */
    public static final boolean objectToBoolean(Object o){
        return o != null && Boolean.parseBoolean(o.toString());
    }

    /**
     * objectToInt
     * @param o Object
     * @return int
     */
    public static final int objectToInt(Object o){
        if(o instanceof Number)
            return ((Number)o).intValue();
        try{
            if(o == null)
                return -1;
            else
                return Integer.parseInt(o.toString());
        }catch(NumberFormatException e){
            return -1;
        }
    }

    /**
     * objectToShort
     * @param o Object
     * @return short
     */
    public static final short objectToShort(Object o){
        if(o instanceof Number)
            return ((Number)o).shortValue();
        try{
            if(o == null)
                return -1;
            else
                return Short.parseShort(o.toString());
        }catch(NumberFormatException e){
            return -1;
        }
    }

    /**
     * objectToDouble
     * @param o Object
     * @return double
     */
    public static final double objectToDouble(Object o){
        if(o instanceof Number)
            return ((Number)o).doubleValue();
        try{
            if(o == null)
                return -1D;
            else
                return Double.parseDouble(o.toString());
        }catch(NumberFormatException e){
            return -1D;
        }
    }

    /**
     * objectToLong
     * @param o Object
     * @return long
     */
    public static final long objectToLong(Object o)
    {
        if(o instanceof Number)
            return ((Number)o).longValue();
        try{
            if(o == null)
                return -1L;
            else
                return Long.parseLong(o.toString());
        }catch(NumberFormatException e){
            return -1L;
        }
    }

    /**
     * objectToString
     * @param obj Object
     * @param fmt DecimalFormat
     * @return String
     */
    public static final String objectToString(Object obj, DecimalFormat fmt)
    {
        fmt.setDecimalSeparatorAlwaysShown(false);
        if(obj instanceof Double)
            return fmt.format(((Double)obj).doubleValue());
        if(obj instanceof Long)
            return fmt.format(((Long)obj).longValue());
        else
            return obj.toString();
    }

    /**
     * 将byte转换为16进制字符串
     * @param src byte[]
     * @return String
     */
    public static String byteToHexString(byte[] src) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xff;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                sb.append("0");
            }
            sb.append(hv);
        }
        return sb.toString();
    }


    /**
     * 将16进制字符串装换为byte数组
     * @param hexString hexString
     * @return byte[]
     */
    public static byte[] hexStringToBytes(String hexString) {
        String newHexString = hexString.toUpperCase();
        int length = newHexString.length() / 2;
        char[] hexChars = newHexString.toCharArray();
        byte[] b = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            b[i] = (byte) (charToByte(hexChars[pos]) << 4 | (charToByte(hexChars[pos + 1]) & 0xff));
        }
        return b;
    }

    /**
     * charToByte
     * @param c char
     * @return byte
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }


    /**
     * longToSimpleString
     * @param value long
     * @return String
     */
    public static String longToSimpleString(long value){
        return simpleFormat.format(value);
    }

    /**
     * asHex
     * @param hash byte
     * @return String
     */
    public static String asHex(byte[] hash){
        return toHex(hash);
    }

    /**
     * toHex
     * @param input byte
     * @return String
     */
    public static String toHex(byte[] input){
        if(input == null)
            return null;
        StringBuilder output = new StringBuilder(input.length * 2);
        for(int i = 0; i < input.length; i++){
            int current = input[i] & 0xff;
            if(current < 16)
                output.append("0");
            output.append(Integer.toString(current, 16));
        }

        return output.toString();
    }

    /**
     * fromHex
     * @param input String
     * @return byte[]
     */
    public static byte[] fromHex(String input){
        if(input == null)
            return new byte[0];
        byte[] output = new byte[input.length() / 2];
        for(int i = 0; i < output.length; i++)
            output[i] = (byte)Integer.parseInt(input.substring(i * 2, (i + 1) * 2), 16);

        return output;
    }

    /**
     * stringToHexString
     * @param input input
     * @param encoding encoding
     * @return String
     * @throws UnsupportedEncodingException UnsupportedEncodingException
     */
    public static String stringToHexString(String input, String encoding)
        throws UnsupportedEncodingException{
        return input != null ? toHex(input.getBytes(encoding)) : null;
    }

    /**
     * stringToHexString
     * @param input input
     * @return String
     */
    public static String stringToHexString(String input){
        try{
            return stringToHexString(input, "UTF-8");
        }catch(UnsupportedEncodingException e){
            throw new IllegalStateException("UTF-8 encoding is not supported by JVM");
        }
    }

    /**
     * hexStringToString
     * @param input input
     * @param encoding encoding
     * @return String
     * @throws UnsupportedEncodingException UnsupportedEncodingException
     */
    public static String hexStringToString(String input, String encoding)
        throws UnsupportedEncodingException{
        return input != null ? new String(fromHex(input), encoding) : null;
    }

    /**
     * hexStringToString
     * @param input input
     * @return String
     */
    public static String hexStringToString(String input){
        try{
            return hexStringToString(input, "UTF-8");
        }catch(UnsupportedEncodingException e){
            throw new IllegalStateException("UTF-8 encoding is not supported by JVM");
        }
    }

    /**
     * timeZoneToCode
     * @param tz tz
     * @return String
     */
    public static String timeZoneToCode(TimeZone tz){

        return timeZoneToString(tz);
    }

    /**
     * codeToTimeZone
     * @param tzString tzString
     * @return TimeZone
     */
    public static TimeZone codeToTimeZone(String tzString){

        return stringToTimeZone(tzString);
    }

    /**
     * timeZoneToString
     * @param tz tz
     * @return 结果集
     */
    public static String timeZoneToString(TimeZone tz){

        return tz != null ? tz.getID() : "";
    }
    /**
     * stringToTimeZone
     * @param tzString tzString
     * @return 结果集
     */
    public static TimeZone stringToTimeZone(String tzString){

        return TimeZone.getTimeZone(tzString != null ? tzString : "");
    }
    /**
     * localeToCode
     * @param aLocale aLocale
     * @return 结果集
     */
    public static String localeToCode(Locale aLocale){

        return localeToString(aLocale);
    }

    /**
     * localeToString
     * @param loc loc
     * @return 结果集
     */
    public static String localeToString(Locale loc){

        return loc != null ? loc.toString() : "";
    }

    /**
     * dateToSQLDate
     * @param d d
     * @return 结果集
     */
    public static Date dateToSQLDate(java.util.Date d){

        return d != null ? new Date(d.getTime()) : null;
    }
    /**
     * dateToSQLTime
     * @param d d
     * @return 结果集
     */
    public static Time dateToSQLTime(java.util.Date d){

        return d != null ? new Time(d.getTime()) : null;
    }
    /**
     * dateToSQLTimestamp
     * @param d d
     * @return 结果集
     */
    public static Timestamp dateToSQLTimestamp(java.util.Date d){

        return d != null ? new Timestamp(d.getTime()) : null;
    }
    /**
     * sqlTimestampToDate
     * @param t t
     * @return 结果集
     */
    public static java.util.Date sqlTimestampToDate(Timestamp t){

        return t != null ? new java.util.Date(Math.round((double)t.getTime() + (double)t.getNanos() / 1000000D)) : null;
    }
    /**
     * getCurrentDate
     * @return 结果集
     */
    public static Timestamp getCurrentDate(){

        Calendar c = Calendar.getInstance();
        c.set(c.get(1), c.get(2), c.get(5), 0, 0, 0);
        Timestamp t = new Timestamp(c.getTime().getTime());
        t.setNanos(0);
        return t;
    }
    /**
     * getDate
     * @param y y
     * @param m m
     * @param d d
     * @param inclusive inclusive
     * @return 结果集
     */
    public static java.util.Date getDate(int y, int m, int d, boolean inclusive)
    {
        java.util.Date dt = null;
        Calendar c = Calendar.getInstance();
        c.clear();
        if(c.getActualMinimum(1) <= y && y <= c.getActualMaximum(1))
        {
            c.set(1, y);
            if(c.getActualMinimum(2) <= m && m <= c.getActualMaximum(2))
            {
                c.set(2, m);
                if(c.getActualMinimum(5) <= d && d <= c.getActualMaximum(5))
                    c.set(5, d);
            }
            if(inclusive)
            {
                c.add(5, 1);
                c.add(14, -1);
            }
            dt = c.getTime();
        }
        return dt;
    }

    /**
     * getDateStart
     * @param d d
     * @return 结果集
     */
    public static java.util.Date getDateStart(java.util.Date d)
    {

         Calendar c = new GregorianCalendar();
         c.clear();
         Calendar co = new GregorianCalendar();
         co.setTime(d);
         c.set(Calendar.DAY_OF_MONTH,co.get(Calendar.DAY_OF_MONTH));
         c.set(Calendar.MONTH,co.get(Calendar.MONTH));
         c.set(Calendar.YEAR,co.get(Calendar.YEAR));
         return c.getTime();
    }
    /**
     * getDateEnd
     * @param d d
     * @return 结果集
     */
    public static java.util.Date getDateEnd(java.util.Date d)
    {
         Calendar c = Calendar.getInstance();
         c.clear();
         Calendar co = Calendar.getInstance();
         co.setTime(d);
         c.set(Calendar.DAY_OF_MONTH,co.get(Calendar.DAY_OF_MONTH));
         c.set(Calendar.MONTH,co.get(Calendar.MONTH));
         c.set(Calendar.YEAR,co.get(Calendar.YEAR));
         c.add(Calendar.DAY_OF_MONTH,1);
         c.add(Calendar.MILLISECOND,-1);
         return c.getTime();
    }
    /**
     * roundNumber
     * @param rowNumber rowNumber
     * @param roundingPoint roundingPoint
     * @return 结果集
     */
    public static double roundNumber(double rowNumber, int roundingPoint)
    {
        double base = Math.pow(10D, roundingPoint);
        return (double)Math.round(rowNumber * base) / base;
    }


    private ConvertUtils(){}

}
