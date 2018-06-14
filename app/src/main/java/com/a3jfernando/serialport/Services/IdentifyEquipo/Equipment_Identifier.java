package com.a3jfernando.serialport.Services.IdentifyEquipo;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import static android.content.Context.WIFI_SERVICE;

/**
 * Created by admi on 6/04/17.
 */

public class Equipment_Identifier {

    private static final String TAG ="" ;
    private Context ourcontext;
    public Equipment_Identifier(Context c) {
        ourcontext = c;
    }


    public String getIMEI(Activity activity) {
        TelephonyManager telephonyManager = (TelephonyManager) activity
                .getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    //get ANDROID ID Device.
    /*--------------------------------------------------------------------------------------------*/

    public String getDeviceUniqueID(Activity activity){
        String device_unique_id = Settings.Secure.getString(activity.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return device_unique_id;
    }

    //get IMEI Device.
    /*--------------------------------------------------------------------------------------------*/
    public String getDeviceID(TelephonyManager phonyManager,Activity activity){

        String id = phonyManager.getDeviceId();
        if (id == null){
            id = "not available";
        }

        int phoneType = phonyManager.getPhoneType();
        switch(phoneType){
            case TelephonyManager.PHONE_TYPE_NONE:
                return "NONE: " + id;
            case TelephonyManager.PHONE_TYPE_GSM:
                return "GSM: IMEI " + id;
            case TelephonyManager.PHONE_TYPE_SIP:
                return "PHONE_TYPE_SIP " + id;
            case TelephonyManager.PHONE_TYPE_CDMA:
                return "CDMA: MEID/ESN " + id;
 /*
  *  for API Level 11 or above
  *  case TelephonyManager.PHONE_TYPE_SIP:
  *   return "SIP";
  */
            default:
                return "UNKNOWN: ID=" + id;
        }

    }

    /*--------------------------------------------------------------------------------------------*/

    //Check the internet connection GET IP Address.
    /*--------------------------------------------------------------------------------------------*/
    public String NetwordDetect() {

        boolean WIFI = false;
        boolean MOBILE = false;
        String IPaddresss = "";
        ConnectivityManager CM = (ConnectivityManager) ourcontext.getSystemService(ourcontext.CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfo = CM.getAllNetworkInfo();

        for (NetworkInfo netInfo : networkInfo) {

            if (netInfo.getTypeName().equalsIgnoreCase("WIFI"))

                if (netInfo.isConnected())

                    WIFI = true;

            if (netInfo.getTypeName().equalsIgnoreCase("MOBILE"))

                if (netInfo.isConnected())

                    MOBILE = true;
        }
        if(WIFI == true)
        {
            IPaddresss = GetDeviceipWiFiData();
        }

        if(MOBILE == true)
        {
            IPaddresss = GetDeviceipMobileData();
        }

        return  IPaddresss;
    }



    public static String GetDeviceipMobileData(){

        /*
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
                 en.hasMoreElements();) {
                NetworkInterface networkinterface = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = networkinterface.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception ex) {
            Log.e("Current IP", ex.toString());
        }
        return null;*/

        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;

    }

    public String GetDeviceipWiFiData()
    {
        WifiManager wifiMgr = (WifiManager) ourcontext.getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        String ipAddress = Formatter.formatIpAddress(ip);
        return ipAddress;

    }

    /*--------------------------------------------------------------------------------------------*/


    //Get local IP Adress
    /*--------------------------------------------------------------------------------------------*/
    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String ip = Formatter.formatIpAddress(inetAddress.hashCode());
                        ///  Log.i(TAG, "***** IP="+ ip);
                        return ip;
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e(TAG, ex.toString());
        }
        return null;



    }
    /*--------------------------------------------------------------------------------------------*/


    //Get android Version
    /*--------------------------------------------------------------------------------------------*/
    public static String getAndroidVersion() {
        String versionName = "";

        try {
            versionName = String.valueOf(Build.VERSION.RELEASE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }

    //Get android MAC Address
    /*--------------------------------------------------------------------------------------------*/
    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(Integer.toHexString(b & 0xFF) + ":");
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            //handle exception
        }
        return "";
    }

     /*--------------------------------------------------------------------------------------------*/


    //Get Device Name
    /*--------------------------------------------------------------------------------------------*/
    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;
        String phrase = "";
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase += Character.toUpperCase(c);
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase += c;
        }
        return phrase;
    }
}
