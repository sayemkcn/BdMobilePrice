package net.toracode.mobilepricebd.commons;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

/**
 * Created by sayemkcn on 10/2/16.
 */
public class Commons {

    public static ProgressDialog getProgressDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait..");
        return progressDialog;
    }

    public static void shareText(Context context,String message,String url){
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
//        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, message+" "+url);
        context.startActivity(Intent.createChooser(sharingIntent, "Share via.."));
    }
}
