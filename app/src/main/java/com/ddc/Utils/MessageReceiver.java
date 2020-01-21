package com.ddc.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;

public class MessageReceiver extends BroadcastReceiver {

    private MessageListener mListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        String message = "";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                message = smsMessage.getMessageBody();
                break;
            }
        } else {
            Bundle data = intent.getExtras();
            Object[] pdus = (Object[]) data.get("pdus");
            for (int i = 0; i < pdus.length; i++) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
                message = smsMessage.getMessageBody();
                break;
            }
        }
        message = message.substring(message.length() - 7, message.length() - 1);
        if (mListener != null)
            mListener.messageReceived(message);
    }

    public void bindListener(MessageListener listener){
        mListener = listener;
    }
}

