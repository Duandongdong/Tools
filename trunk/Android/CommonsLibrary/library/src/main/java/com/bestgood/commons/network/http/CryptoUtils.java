package com.bestgood.commons.network.http;

import android.content.Context;
import android.text.TextUtils;

import com.bestgood.commons.permission.RxPermissionReadUtils;
import com.bestgood.commons.util.config.AppConfig;
import com.bestgood.commons.util.log.Logger;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by dengdingchun on 16/4/26.
 */
class CryptoUtils {

    static String getPhoneNumber(final Context context) {
        String phoneNumber = RxPermissionReadUtils.readPhoneNumber(context);
        if (TextUtils.isEmpty(phoneNumber)) {
            phoneNumber = "0000000000";
        } else if (phoneNumber.length() < 10) {
            phoneNumber = phoneNumber + createZeroString(10 - phoneNumber.length());
        }
        return phoneNumber;
    }

    /**
     * 加密请求参数
     *
     * @param entityContent
     * @param phoneNumber
     * @return
     */
    static String encrypt(String entityContent, String phoneNumber) {
//        Logger.t(getClass().getSimpleName()).i("request decrypt entity = %s", entityContent);

//        String pk = dealNumber(phoneNumber);
//        long md5StrStart = System.currentTimeMillis();
//        String key = getMD5Str("czgjService10" + getCurrentYear() + pk);
//        Logger.t(TAG_TIME).i("Get Key MD5 Str Time:%s", System.currentTimeMillis() - md5StrStart);
//        Logger.i("key = %s", key);
//
//        long encryptRequestTime = System.currentTimeMillis();
//        CZJAes jj = new CZJAes(key);
//        entityContent = jj.encrypt(entityContent, "utf-8");
//        Logger.t(TAG_TIME).i("encrypt request time:%s", System.currentTimeMillis() - encryptRequestTime);
//
//        Logger.t(getClass().getSimpleName()).i("request encrypt entity = %s", entityContent);

        return entityContent;
    }

    static String decrypt(String responseStr, String phoneNumber) {
        if (AppConfig.isResponseDecrypt()) {//帕克停车项目不需要加密解密

            String pk = dealNumber(phoneNumber);
            String key = getMD5Str("czgjService10" + getCurrentYear() + pk);

            CryptoAes jj = new CryptoAes(key);
            responseStr = jj.decrypt(responseStr, "utf-8");

        }

        return responseStr;
    }


    /**
     * MD5 加密
     */
    private static String getMD5Str(String str) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            Logger.e(e, "getMD5Str(%s)", str);
        }
        if (messageDigest == null) {
            return "";
        }
        byte[] byteArray = messageDigest.digest();
        StringBuilder md5StrBuff = new StringBuilder();
        for (byte aByte : byteArray) {
            if (Integer.toHexString(0xFF & aByte).length() == 1) {
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & aByte));
            } else {
                md5StrBuff.append(Integer.toHexString(0xFF & aByte));
            }
        }
        return md5StrBuff.toString();
    }


    private static String dealNumber(String pn) {
        if (TextUtils.isEmpty(pn)) {
            return "";
        }
        String res = new StringBuilder(pn).reverse().toString();
        return res.substring(0, res.length() > 10 ? 10 : res.length());
    }

    private static String getCurrentYear() {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        return String.valueOf(calendar.get(Calendar.YEAR) - 1);
    }

    private static String createZeroString(int size) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append("0");
        }
        return sb.toString();
    }
}