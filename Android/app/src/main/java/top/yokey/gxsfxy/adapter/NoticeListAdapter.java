package top.yokey.gxsfxy.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import top.yokey.gxsfxy.R;
import top.yokey.gxsfxy.activity.MyApplication;
import top.yokey.gxsfxy.activity.home.NewsDetailedActivity;
import top.yokey.gxsfxy.utility.TimeUtil;

public class NoticeListAdapter extends RecyclerView.Adapter<NoticeListAdapter.ViewHolder> {

    private Activity mActivity;
    private MyApplication mApplication;
    private ArrayList<HashMap<String, String>> mArrayList;

    public NoticeListAdapter(MyApplication application, Activity activity, ArrayList<HashMap<String, String>> arrayList) {
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

        holder.titleTextView.setText(hashMap.get("news_title"));
        holder.commentTextView.setText(hashMap.get("news_comment"));
        holder.praiseTextView.setText(hashMap.get("news_praise"));
        holder.timeTextView.setText(TimeUtil.decode(hashMap.get("news_time")));

        holder.mRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, NewsDetailedActivity.class);
                intent.putExtra("title", "公告详细内容");
                intent.putExtra("news_id", hashMap.get("news_id"));
                intent.putExtra("news_link", hashMap.get("news_link"));
                intent.putExtra("news_from", hashMap.get("news_from"));
                intent.putExtra("news_time", hashMap.get("news_time"));
                intent.putExtra("news_title", hashMap.get("news_title"));
                mApplication.startActivity(mActivity, intent);
            }
        });

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup group, int viewType) {
        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.item_list_notice, group, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout mRelativeLayout;
        private TextView titleTextView;
        private TextView commentTextView;
        private TextView praiseTextView;
        private TextView timeTextView;

        private ViewHolder(View view) {
            super(view);

            mRelativeLayout = (RelativeLayout) view.findViewById(R.id.mainRelativeLayout);
            titleTextView = (TextView) view.findViewById(R.id.titleTextView);
            commentTextView = (TextView) view.findViewById(R.id.commentTextView);
            praiseTextView = (TextView) view.findViewById(R.id.praiseTextView);
            timeTextView = (TextView) view.findViewById(R.id.timeTextView);

        }

    }

}