package com.example.sanketk.popularmoives.model;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sanketk.popularmoives.BuildConfig;
import com.example.sanketk.popularmoives.R;

/**
 * Created by sanketk on 12-Aug-18.
 */

public class Helper {

    public static boolean isNetworkAvailble(Context ctx) {
        NetworkInfo activeNetwork = ((ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static Dialog showProgressDialog(Context context) {
        if (context == null) {
            return null;
        }
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.vw_custom_progress_bar);
        Glide.with(context).load(Integer.valueOf(R.drawable.loader)).asGif().into((ImageView) dialog.findViewById(R.id.wvLoad));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        return dialog;
    }

    public static void showOkDialog(Context context, String message) {
        if (context != null) {
            AlertDialog.Builder build = new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_ok, null);
            build.setView(view);
            final AlertDialog okCancelDialog = build.create();
            okCancelDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            TextView tvMessage = (TextView) view.findViewById(R.id.tvMessage);
            TextView btnYes = (TextView) view.findViewById(R.id.btnYes);
            btnYes.setText("OK");
            btnYes.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    okCancelDialog.dismiss();
                }
            });
            tvTitle.setText(R.string.app_name);
            if (TextUtils.isEmpty(message)) {
                tvMessage.setText(BuildConfig.FLAVOR);
            } else {
                tvMessage.setText(message);
            }
            okCancelDialog.show();
            okCancelDialog.setCanceledOnTouchOutside(false);
        }
    }
}
