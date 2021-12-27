package com.htbr.barcode.utils;

import com.htbrkt.ParseResult;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.htbr.barcode.R;

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
            String[] title = {"Abundance"};
            Intent intent = composeEmail(title,"subject", null);
            return new ParseResult(context.getResources().getString(R.string.write_mail), intent);
        } else if (qrStringLower.startsWith("tel")){
            Intent intent = dialPhoneNumber(qrString);
            return new ParseResult(context.getResources().getString(R.string.call_number), intent);
        } else if (qrStringLower.startsWith("sms")){
            Intent intent = new Intent();
            //ToDo: call sms method
            return new ParseResult(context.getResources().getString(R.string.write_sms), intent);
        } else if (qrStringLower.startsWith("mecard")){
            Intent intent = new Intent();
            return new ParseResult(context.getResources().getString(R.string.show_contact), intent);
        } else if (qrStringLower.startsWith("bitcoin")){
            Intent intent = new Intent();
            return new ParseResult(context.getResources().getString(R.string.show_blockchain), intent);
        }
        return null;

        //ToDo:
        // Calendar events
        // Covid Certificats
        // Create Alarm
        //
        //

    }

    public static Intent composeEmail(String[] addresses, String subject, Uri attachment) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        if (subject != null){
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        }
        if (attachment != null){
            intent.putExtra(Intent.EXTRA_STREAM, attachment);
        }
        return intent;
    }

    public static Intent openWebPage(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        return intent;
    }

    public static Intent dialPhoneNumber(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        return intent;
    }

    public static Intent writeSMS(String sms, String number){
        Intent intent = new Intent();
        intent.setData(Uri.parse("sms:"));
        intent.setType("vnd.android-dir/mms-sms");
        intent.putExtra(Intent.EXTRA_TEXT, "");
        intent.putExtra("address",  number);
        return intent;
    }


}
