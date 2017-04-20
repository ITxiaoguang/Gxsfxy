package top.yokey.gxsfxy.utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import top.yokey.gxsfxy.R;

public class DialogUtil {

    private static Dialog dialog;
    private static ProgressDialog progressDialog;

    //作用：关闭对话框
    public static void cancel() {

        try {
            if (progressDialog != null) {
                progressDialog.cancel();
            }
            if (dialog != null) {
                dialog.cancel();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //作用：显示进度对话框
    public static void progress(Activity activity) {

        try {
            progressDialog = new ProgressDialog(activity);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("处理中...");
            progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //作用：显示进度对话框，自定义内容
    public static void progress(Activity activity, String title) {

        try {
            progressDialog = new ProgressDialog(activity);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage(title);
            progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //作用：显示二维码
    public static void qrCode(Activity activity, CharSequence tips, Bitmap bitmap) {

        try {
            dialog = new AlertDialog.Builder(activity).create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            Window window = dialog.getWindow();
            window.setContentView(R.layout.dialog_qr_code);
            window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            ImageView mImageView = (ImageView) window.findViewById(R.id.mainImageView);
            mImageView.setImageBitmap(bitmap);
            TextView mTextView = (TextView) window.findViewById(R.id.mainTextView);
            mTextView.setText(tips);
            ImageView closeImageView = (ImageView) window.findViewById(R.id.closeImageView);
            closeImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogUtil.cancel();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //作用：显示图片操作
    public static void image(Activity activity, View.OnClickListener take, View.OnClickListener photo) {

        try {
            dialog = new AlertDialog.Builder(activity).create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            Window window = dialog.getWindow();
            window.setContentView(R.layout.dialog_image);
            window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            TextView takeTextView = (TextView) window.findViewById(R.id.takeTextView);
            TextView photoTextView = (TextView) window.findViewById(R.id.photoTextView);
            takeTextView.setOnClickListener(take);
            photoTextView.setOnClickListener(photo);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}