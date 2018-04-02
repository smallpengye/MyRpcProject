package com.hzp.Util;

import java.util.Arrays;

/**
 * Created by jarvis on 2017/11/26.
 */
public class Util {
    private static int GBorder = 1;
    // 十六进制转ASCII
    public static String hex2string(String hex){

        StringBuilder sb = new StringBuilder();
        StringBuilder temp = new StringBuilder();

        //49204c6f7665204a617661 split into two characters 49, 20, 4c...
        for( int i=0; i<hex.length()-1; i+=2 ){

            //grab the hex in pairs
            String output = hex.substring(i, (i + 2));
            //convert hex to decimal
            int decimal = Integer.parseInt(output, 16);
            //convert the decimal to character
            sb.append((char)decimal);

            temp.append(decimal);
        }
        return sb.toString();
    }

    // 字节数组转十六进制字符串
    public static String byte2hex(byte [] buffer){
        String hex = "";

        for(int i = 0; i < buffer.length; i++){
            String temp = Integer.toHexString(buffer[i] & 0xFF);
            if(temp.length() == 1){
                temp = "0" + temp;
            }
//            hex = hex + " "+ temp;
            hex = hex + temp;
        }

        return hex;
    }

    // 字节转十六进制字符串
    public static String byte2hex(byte buffer){
        String hex = "";
        String temp = Integer.toHexString(buffer & 0xFF);
        if(temp.length() == 1){
            temp = "0" + temp;
        }
        hex = hex + temp;
        return hex;
    }
    // 0x7d 0x02=>0x7e, 0x7d 0x01=>0x7d
    public static byte[] bitsAfter07(byte[] array) {
        byte[] tempBits = new byte[array.length];

        int j = 0;
        for (int i = 0; i < array.length; i++) {
            byte b = array[i];
            //头的7e已经截取过了  ~ 7e，所以只有尾不判断
            if (i == array.length - 1) {
                tempBits[j] = b;
            } else {
                if (b == 0x7d) {
                    if (0x01 == (array[i + 1])) {
                        tempBits[j] = 0x7d;
                    }
                    if (0x02 == (array[i + 1])) {
                        tempBits[j] = 0x7e;
                    }
                    i = i+1;
                } else {
                    tempBits[j] = b;
                }
            }
            j++;
        }
        byte[] newBits = new byte[j];
        newBits  = Arrays.copyOf(tempBits, j);
        return newBits;
    }

    // 0x7e=>0x7d 0x02, 0x7d=>0x7d 0x01
    public static byte[] bitsBefore07(byte[] array) {
        // 处理加密时出现7e,7d导致的数组下标越界异常
        int myNum = 0;
        for (int m = 1; m < array.length - 1; m++) {
            if (array[m] == 0x7e || array[m] == 0x7d) {
                myNum++;
            }
        }

        byte[] tempBits = new byte[array.length+myNum];

        // 开始加密
        int j = 0;
        for (int i = 0; i < array.length; i++) {
            byte b = array[i];
            // 头尾不判断
            if (i == 0 || i == array.length-1) {
                tempBits[j] = b;
            } else {
                switch (b) {
                    case 0x7e:
                        tempBits[j] = 0x7d;
                        j = j + 1;
                        tempBits[j] = 0x02;
                        break;
                    case 0x7d:
                        tempBits[j] = 0x7d;
                        j = j + 1;
                        tempBits[j] = 0x01;
                        break;
                    default:
                        tempBits[j] = b;
                        break;
                }
            }
            j++;
        }
        byte[] newBits = new byte[j];
        newBits = Arrays.copyOf(tempBits, j);
        return newBits;
    }

    // 判断int数的第n位是0/1
    public static boolean isIntNumberNBitONEInBinary(int number,int nbit){
        boolean result = false;
        if((number%(Math.pow(2, nbit)))/(Math.pow(2, nbit-1)) >= 1.0){
            result = true;
        }
        return result;
    }

    /// 异或校验 获取校验码
    public static byte AndOr(byte[] ptr)
    {
        //从消息头开始异或(去除第一位的标示位)
        byte crc;
        crc = ptr[1];
        for (int j = 2; j < ptr.length; j++)
        {
            crc ^= ptr[j];
        }
        return crc;
    }
    /**
     * 将byte转换为一个长度为8的byte数组，数组每个值代表bit
     */
    public static byte[] getBooleanArray(byte b) {
        byte[] array = new byte[8];
        for (int i = 7; i >= 0; i--) {
            array[i] = (byte)(b & 1);
            b = (byte) (b >> 1);
        }
        return array;
    }

    //16进制转2进制
    public static String hexString2binaryString(String hexString)
    {
        if (hexString == null || hexString.length() % 2 != 0)
            return null;
        String bString = "", tmp;
        for (int i = 0; i < hexString.length(); i++)
        {
            tmp = "0000" + Integer.toBinaryString(Integer.parseInt(hexString.substring(i, i + 1), 16));
            bString += tmp.substring(tmp.length() - 4);
        }
        return bString;
    }


    // bytes time to 2017-11-11 12:12:12
    public static String toFormatTime(byte[] time){
        StringBuffer date = new StringBuffer("20");

        String year = Integer.toHexString(time[0] & 0xFF);
        String month = Integer.toHexString(time[1] & 0xFF);
        String day = Integer.toHexString(time[2]& 0xFF);
        String hour = Integer.toHexString(time[3] & 0xFF);
        String min = Integer.toHexString(time[4] & 0xFF);
        String second = Integer.toHexString(time[5] & 0xFF);
        if(year.length()==1){
            date.append("0"+year+"-");
        }else {
            date.append(year+"-");
        }
        if(month.length()==1){
            date.append("0"+month+"-");
        }else {
            date.append(month+"-");
        }
        if(day.length()==1){
            date.append("0"+day+" ");
        }else {
            date.append(day+" ");
        }
        if(hour.length()==1){
            date.append("0"+hour+":");
        }else {
            date.append(hour+":");
        }
        if(min.length()==1){
            date.append("0"+min+":");
        }else {
            date.append(min+":");
        }
        if(second.length()==1){
            date.append("0"+second);
        }else {
            date.append(second);
        }
        return date.toString();
    }

    public static int getSerialnumber()
    {
        int i = GBorder++;//取消息id,全局变量控制id唯一,如果超过FFFF(65535),那么就得重新开始
        if (i > 65535)
        {
            GBorder = 0;
        }
        return i;
    }

    public static byte[] short2Byte(short a){
        byte[] b = new byte[2];
        b[0] = (byte) (a >> 8);
        b[1] = (byte) (a);
        return b;
    }
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (Util.charToByte(hexChars[pos]) << 4 | Util.charToByte(hexChars[pos + 1]));
        }
        return d;
    }
    public static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }


    //字符串转BCD码
    public static byte[] Str2Bcd(String asc)
    {
        int len = asc.length();
        int mod = len % 2;

        if (mod != 0)
        {
            asc = asc + "0";
            len = asc.length();
        }

        byte[] bOriginalData = new byte[len];
        if (len >= 2)
        {
            len = len / 2;
        }


        byte[] bBCD = new byte[len];

        bOriginalData =  asc.getBytes();

        int j, k;
        for (int p = 0; p < asc.length() / 2; p++)
        {
            if ((bOriginalData[2 * p] >= '0') && (bOriginalData[2 * p] <= '9'))
            {
                j = bOriginalData[2 * p] - '0';
            }
            else if ((bOriginalData[2 * p] >= 'a') && (bOriginalData[2 * p] <= 'z'))
            {
                j = bOriginalData[2 * p] - 'a' + 0x0a;
            }
            else
            {
                j = bOriginalData[2 * p] - 'A' + 0x0a;
            }

            if ((bOriginalData[2 * p + 1] >= '0') && (bOriginalData[2 * p + 1] <= '9'))
            {
                k = bOriginalData[2 * p + 1] - '0';
            }
            else if ((bOriginalData[2 * p + 1] >= 'a') && (bOriginalData[2 * p + 1] <= 'z'))
            {
                k = bOriginalData[2 * p + 1] - 'a' + 0x0a;
            }
            else
            {
                k = bOriginalData[2 * p + 1] - 'A' + 0x0a;
            }

            int a = (j << 4) + k;
            byte b = (byte)a;
            bBCD[p] = b;
        }
        return bBCD;
    }
}




