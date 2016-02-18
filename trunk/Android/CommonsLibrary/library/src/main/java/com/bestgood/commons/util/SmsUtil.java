package com.bestgood.commons.util;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SmsUtil {

    /**
     * @throws
     * @Title: createSendIntent
     * @Description: 获取系统发送短信的intent
     * @param: @param context
     * @param: @return
     * @return: PendingIntent
     */
    public static PendingIntent createSendIntent(Context context) {
        Intent sentIntent = new Intent("SENT_SMS_ACTION");
        PendingIntent sentPI = PendingIntent.getBroadcast(context, 0,
                sentIntent, 0);
        return sentPI;
    }

    /**
     * @throws
     * @Title: sendMsgSystem
     * @Description: 调用系统发送短信界面
     * @param: @param context
     * @param: @param number
     * @param: @param msgbody
     * @return: void
     */
    public static void sendMsgSystem(Context context, String number,
                                     String msgbody) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("smsto:" + number));
        intent.putExtra("sms_body", msgbody);
        context.startActivity(intent);
    }

    /**
     * @throws
     * @Title: sendMsgGxws
     * @Description: 公信卫士后台发短信
     * @param: @param context
     * @param: @param number
     * @param: @param msgbody
     * @return: void
     */
    public static void sendMsg(Context context, String number, String msgbody) {
        SmsManager smsManager = SmsManager.getDefault();
        if (msgbody.length() > 70) {
            String[] messages = SmsUtil.splitMsg(msgbody, 70);
            for (String msg : messages) {
                smsManager.sendTextMessage(number, null, msg, null, null);
            }
        } else {
            smsManager.sendTextMessage(number, null, msgbody, null, null);
        }
    }

    /**
     * @throws
     * @Title: splitMsg
     * @Description: 将长短信内容分割
     * @param: @param msg
     * @param: @param num
     * @param: @return
     * @return: String[]
     */
    public static String[] splitMsg(String msg, int num) {
        int len = msg.length();
        if (len <= num)
            return new String[]{msg};

        int count = len / (num - 1);
        count += len > (num - 1) * count ? 1 : 0; // 这里应该值得注意

        String[] result = new String[count];

        int pos = 0;
        int splitLen = num - 1;
        for (int i = 0; i < count; i++) {
            if (i == count - 1)
                splitLen = len - pos;

            result[i] = msg.substring(pos, pos + splitLen);
            pos += splitLen;

        }
        return result;
    }

    /**
     * @throws
     * @Title: createFromPdu
     * @Description: 获取短信内容
     * @param: @param pdu
     * @param: @param from
     * @param: @return
     * @return: SmsMessage
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static SmsMessage createFromPdu(byte[] pdu, String from) {
        SmsMessage message = null;
        if (from != null) {
            try {
                Method m;
                Class internal_clazz;
                if (from.compareTo("CDMA") == 0) {
                    internal_clazz = Class
                            .forName("com.android.internal.telephony.cdma.SmsMessage");
                } else {
                    internal_clazz = Class
                            .forName("com.android.internal.telephony.gsm.SmsMessage");
                }
                m = internal_clazz.getMethod("createFromPdu",
                        new Class[]{pdu.getClass()});
                Object wrappedMessage = m.invoke(internal_clazz,
                        new Object[]{pdu});

                @SuppressWarnings("unused")
                Class smb_clazz = Class
                        .forName("com.android.internal.telephony.SmsMessageBase");
                Constructor[] conses = SmsMessage.class
                        .getDeclaredConstructors();
                for (int i = 0; i < conses.length; i++) {
                    Class[] c = conses[i].getParameterTypes();
                    if (c.length == 1) {
                        conses[1].setAccessible(true);
                        message = (SmsMessage) conses[1]
                                .newInstance(wrappedMessage);
                        break;
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        } else {
            message = SmsMessage.createFromPdu(pdu);
        }
        return message;
    }
}
