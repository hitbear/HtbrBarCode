package com.htbr.barcode.utils;

import com.htbrkt.ParseResult;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.core.net.MailTo;

import com.htbr.barcode.R;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;

public class Parser {
    public static ParseResult parse(Context context, String qrString){
        //At first transform the string to lower case
        String qrStringLower = qrString.toLowerCase(Locale.ROOT);
        if (qrStringLower.startsWith("http")){
            Intent intent = openWebPage(qrString);
            return new ParseResult(context.getResources().getString(R.string.open_url), intent);
        } else if (qrStringLower.startsWith("wifi")){
            Intent intent = new Intent();
            return new ParseResult(context.getResources().getString(R.string.connect_wifi), intent);
        } else if (qrStringLower.startsWith("mailto")){
            String to = "";
            MailTo mailTo = MailTo.parse(qrString);
            Intent intent = composeEmail(mailTo);
            return new ParseResult(context.getResources().getString(R.string.write_mail), intent);
        } else if (qrStringLower.startsWith("tel")){
            Intent intent = dialPhoneNumber(qrString);
            return new ParseResult(context.getResources().getString(R.string.call_number), intent);
        } else if (qrStringLower.startsWith("sms")){
            Intent intent = writeSMS(qrString);
            return new ParseResult(context.getResources().getString(R.string.write_sms), intent);
        } else if (qrStringLower.startsWith("mecard")){
            Intent intent = new Intent();
            return new ParseResult(context.getResources().getString(R.string.show_contact), intent);
        } else if (qrStringLower.startsWith("bitcoin")){
            Intent intent = new Intent();
            return new ParseResult(context.getResources().getString(R.string.show_blockchain), intent);
        }
        return new ParseResult("", new Intent());

        //ToDo:
        // Calendar events
        // Covid Certificats
        // Create Alarm
        // MMS
        //

    }

    //public static Intent composeEmail(String[] addresses, String subject, Uri attachment) {
    public static Intent composeEmail(MailTo mailTo) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("*/*");
        try {
            if (mailTo.getTo() != null){
                intent.putExtra(Intent.EXTRA_EMAIL, mailTo.getTo().split(";"));
            }
            if (mailTo.getSubject() != null) {
                intent.putExtra(Intent.EXTRA_SUBJECT, mailTo.getSubject());
            }
            if (mailTo.getCc() != null) {
                intent.putExtra(Intent.EXTRA_CC, mailTo.getCc().split(";"));
            }
            if (mailTo.getBcc() != null) {
                intent.putExtra(Intent.EXTRA_BCC, mailTo.getBcc().split(";"));
            }
            if (mailTo.getBody() != null) {
                intent.putExtra(Intent.EXTRA_TEXT, mailTo.getBody());
            }
            //ToDo: EXTRA STREAM = attachment
        } catch (Exception e){
            Log.e("Parser", "Error MailTo");
        }
        return intent;
    }

    public static Intent openWebPage(String url) {
        Uri webpage = Uri.parse(url);
        return new Intent(Intent.ACTION_VIEW, webpage);
    }

    public static Intent dialPhoneNumber(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(phoneNumber));
        return intent;
    }

    public static Intent writeSMS(String qrString){
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
        String[] parts = qrString.split(":");
        intent.setType("vnd.android-dir/mms-sms");
        if (parts.length > 1) {
            intent.setData(Uri.parse("smsto:" + parts[1]));
        }
        else {
            intent.setData(Uri.parse("sms"));
        }
        if (parts.length > 2) {
            intent.putExtra(Intent.EXTRA_TEXT, parts[2]);
        }


        return intent;
    }


}
