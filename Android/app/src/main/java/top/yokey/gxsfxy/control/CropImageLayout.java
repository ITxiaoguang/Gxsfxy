package top.yokey.gxsfxy.control;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import top.yokey.gxsfxy.R;

public class CropImageLayout extends RelativeLayout {

    private CropImageView mZoomImageView;
    private CropImageBorderView mClipImageView;

    public CropImageLayout(Context context) {
        this(context, null);
    }

    public CropImageLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CropImageLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mZoomImageView = new CropImageView(context);
        mClipImageView = new CropImageBorderView(context);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(mZoomImageView, params);
        addView(mClipImageView, params);
        mZoomImageView.setImageResource(R.mipmap.bg_load);
    }

    public Bitmap clip() {
        return mZoomImageView.clip();
    }

    public void setImageBitmap(Bitmap bitmap) {
        mZoomImageView.setImageBitmap(bitmap);
        mZoomImageView.reLayout();
        mZoomImageView.invalidate();
    }

}