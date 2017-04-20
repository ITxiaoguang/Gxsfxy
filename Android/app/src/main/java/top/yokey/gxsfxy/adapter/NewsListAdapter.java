package top.yokey.gxsfxy.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;

import top.yokey.gxsfxy.R;
import top.yokey.gxsfxy.activity.MyApplication;
import top.yokey.gxsfxy.activity.home.NewsDetailedActivity;
import top.yokey.gxsfxy.utility.TextUtil;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.ViewHolder> {

    private Activity mActivity;
    private MyApplication mApplication;
    private ArrayList<HashMap<String, String>> mArrayList;

    public NewsListAdapter(MyApplication application, Activity activity, ArrayList<HashMap<String, String>> arrayList) {
        this.mActivity = activity;
        this.mArrayList = arrayList;
        this.mApplication = application;
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final HashMap<String, String> hashMap = mArrayList.get(position);

        holder.mImageView.setImageResource(R.mipmap.ic_launcher);
        holder.titleTextView.setText(hashMap.get("news_title"));
        holder.commentTextView.setText(hashMap.get("news_comment"));
        holder.praiseTextView.setText(hashMap.get("news_praise"));
        holder.clickTextView.setText(hashMap.get("news_click"));
        holder.clickTextView.append(" 点击");

        if (mApplication.saveFlowCheckBoxBoolean) {
            holder.mImageView.setImageResource(R.mipmap.bg_default_img);
        } else {
            if (TextUtil.isEmpty(hashMap.get("news_image"))) {
                holder.mImageView.setImageResource(R.mipmap.bg_default_img);
            } else {
                ImageLoader.getInstance().displayImage(hashMap.get("news_image"), holder.mImageView);
            }
        }

        holder.mRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, NewsDetailedActivity.class);
                intent.putExtra("title", "新闻详细内容");
                intent.putExtra("news_id", hashMap.get("news_id"));
                intent.putExtra("news_link", hashMap.get("news_link"));
                intent.putExtra("news_from", hashMap.get("news_from"));
                intent.putExtra("news_time", hashMap.get("news_time"));
                intent.putExtra("news_image", hashMap.get("news_image"));
                intent.putExtra("news_title", hashMap.get("news_title"));
                mApplication.startActivity(mActivity, intent);
            }
        });

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup group, int viewType) {
        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.item_list_news, group, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout mRelativeLayout;
        private ImageView mImageView;
        private TextView titleTextView;
        private TextView commentTextView;
        private TextView praiseTextView;
        private TextView clickTextView;

        private ViewHolder(View view) {
            super(view);

            mRelativeLayout = (RelativeLayout) view.findViewById(R.id.mainRelativeLayout);
            mImageView = (ImageView) view.findViewById(R.id.mainImageView);
            titleTextView = (TextView) view.findViewById(R.id.titleTextView);
            commentTextView = (TextView) view.findViewById(R.id.commentTextView);
            praiseTextView = (TextView) view.findViewById(R.id.praiseTextView);
            clickTextView = (TextView) view.findViewById(R.id.clickTextView);

        }

    }

}