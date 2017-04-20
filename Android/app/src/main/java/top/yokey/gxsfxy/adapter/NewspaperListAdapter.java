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
import top.yokey.gxsfxy.activity.home.NewsDetailedActivity;
import top.yokey.gxsfxy.activity.MyApplication;
import top.yokey.gxsfxy.utility.TextUtil;

public class NewspaperListAdapter extends RecyclerView.Adapter<NewspaperListAdapter.ViewHolder> {

    private Activity mActivity;
    private MyApplication mApplication;
    private ArrayList<HashMap<String, String>> mArrayList;

    public NewspaperListAdapter(MyApplication application, Activity activity, ArrayList<HashMap<String, String>> arrayList) {
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

        ImageLoader.getInstance().displayImage(hashMap.get("news_image_1"), holder.horImageView[0]);
        holder.horNameTextView[0].setText(hashMap.get("news_title_1"));

        if (TextUtil.isEmpty(hashMap.get("news_id_2"))) {
            holder.horRelativeLayout[1].setVisibility(View.INVISIBLE);
        } else {
            holder.horRelativeLayout[1].setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(hashMap.get("news_image_2"), holder.horImageView[1]);
            holder.horNameTextView[1].setText(hashMap.get("news_title_2"));
        }

        holder.horRelativeLayout[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, NewsDetailedActivity.class);
                intent.putExtra("title", "校报内容");
                intent.putExtra("news_id", hashMap.get("news_id_1"));
                intent.putExtra("news_link", hashMap.get("news_link_1"));
                intent.putExtra("news_from", hashMap.get("news_from_1"));
                intent.putExtra("news_time", hashMap.get("news_time_1"));
                intent.putExtra("news_title", hashMap.get("news_title_1"));
                mApplication.startActivity(mActivity, intent);
            }
        });

        holder.horRelativeLayout[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, NewsDetailedActivity.class);
                intent.putExtra("title", "校报内容");
                intent.putExtra("news_id", hashMap.get("news_id_2"));
                intent.putExtra("news_link", hashMap.get("news_link_2"));
                intent.putExtra("news_from", hashMap.get("news_from_2"));
                intent.putExtra("news_time", hashMap.get("news_time_2"));
                intent.putExtra("news_title", hashMap.get("news_title_2"));
                mApplication.startActivity(mActivity, intent);
            }
        });

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup group, int viewType) {
        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.item_list_newspaper, group, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout[] horRelativeLayout;
        private ImageView[] horImageView;
        private TextView[] horNameTextView;

        public ViewHolder(View view) {
            super(view);

            horRelativeLayout = new RelativeLayout[2];
            horRelativeLayout[0] = (RelativeLayout) view.findViewById(R.id.hor1RelativeLayout);
            horRelativeLayout[1] = (RelativeLayout) view.findViewById(R.id.hor2RelativeLayout);
            horImageView = new ImageView[2];
            horImageView[0] = (ImageView) view.findViewById(R.id.hor1ImageView);
            horImageView[1] = (ImageView) view.findViewById(R.id.hor2ImageView);
            horNameTextView = new TextView[2];
            horNameTextView[0] = (TextView) view.findViewById(R.id.hor1NameTextView);
            horNameTextView[1] = (TextView) view.findViewById(R.id.hor2NameTextView);

        }

    }

}