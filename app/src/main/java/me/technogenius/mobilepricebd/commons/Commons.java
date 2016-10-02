package me.technogenius.mobilepricebd.commons;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by sayemkcn on 10/2/16.
 */
public class Commons {

    public static ProgressDialog getProgressDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait..");
        return progressDialog;
    }
}
