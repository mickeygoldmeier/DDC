package com.ddc.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class MessageReceiver extends BroadcastReceiver {

    private MessageListener mListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle data = intent.getExtras();
        Object[] pdus = (Object[]) data.get("pdus");
        for(int i=0; i<pdus.length; i++){
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
            String message = smsMessage.getMessageBody();
            message = message.substring(message.length() - 7, message.length() - 1);
            if(mListener != null)
                mListener.messageReceived(message);
        }
    }

    public void bindListener(MessageListener listener){
        mListener = listener;
    }
}

