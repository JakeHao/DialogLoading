package com.github.loadingdialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;


public class DialogLoading {

    private static ProgressDialog dialog;
    private static Handler sHandler = new Handler(Looper.getMainLooper());

    public static void show(Activity activity) {
        show(activity, null, false);
    }

    public static void show(Activity activity, boolean isCancel) {
        show(activity, null, isCancel);
    }

    public static void show(final Activity activity, final CharSequence message, final boolean isCancel) {
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            genDialog(activity, message, isCancel);
        } else {
            sHandler.post(new Runnable() {
                @Override
                public void run() {
                    genDialog(activity, message, isCancel);
                }
            });
        }
    }

    public static void dismiss() {
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            dismissDialog();
        } else {
            sHandler.post(new Runnable() {
                @Override
                public void run() {
                    dismissDialog();
                }
            });
        }
    }

    private static void genDialog(Activity activity, CharSequence message, boolean isCancel) {
        if (dialog != null && dialog.isShowing()) {
            //do nothing
        } else {
            if (activity == null) {
                return;
            }
            dialog = new ProgressDialog(activity, message, isCancel);
            dialog.show();
        }
    }

    private static void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public static void onDestroy() {
        dismiss();
        dialog = null;
    }

    private static class ProgressDialog extends Dialog {

        public ProgressDialog(@NonNull Context context, CharSequence message, boolean isCancel) {
            super(context, R.style.public_dialog_progress);
            View view;
            if (!TextUtils.isEmpty(message)) {
                view = LayoutInflater.from(context).inflate(R.layout.public_dialog_progress_txt, null);
                TextView txt_load = view.findViewById(R.id.txt_load);
                txt_load.setText(message);
            } else {
                view = LayoutInflater.from(context).inflate(R.layout.public_dialog_progress, null);
            }
            setContentView(view);
            setCancelable(isCancel);
            setCanceledOnTouchOutside(isCancel);
        }
    }
}
