package top.yokey.gxsfxy.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import top.yokey.gxsfxy.R;
import top.yokey.gxsfxy.activity.dynamic.ActivityDetailedActivity;
import top.yokey.gxsfxy.activity.dynamic.CircleDetailedActivity;
import top.yokey.gxsfxy.activity.dynamic.TopicDetailedActivity;
import top.yokey.gxsfxy.activity.home.NewsDetailedActivity;
import top.yokey.gxsfxy.activity.MyApplication;
import top.yokey.gxsfxy.system.UserAjaxParams;
import top.yokey.gxsfxy.utility.TextUtil;
import top.yokey.gxsfxy.utility.ToastUtil;

public class UserPraiseListAdapter extends RecyclerView.Adapter<UserPraiseListAdapter.ViewHolder> {

    private Activity mActivity;
    private MyApplication mApplication;
    private onItemDelClickListener itemDelClickListener;
    private ArrayList<HashMap<String, String>> mArrayList;

    public UserPraiseListAdapter(MyApplication application, Activity activity, ArrayList<HashMap<String, String>> arrayList) {
        this.mActivity = activity;
        this.mArrayList = arrayList;
        this.mApplication = application;
        this.itemDelClickListener = null;
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final HashMap<String, String> hashMap = mArrayList.get(position);

        try {

            JSONObject jsonObject = new JSONObject(hashMap.get("user_info"));
            final String user_id = jsonObject.getString("user_id");

            if (TextUtil.isEmpty(jsonObject.getString("user_avatar"))) {
                holder.avatarImageView.setImageResource(R.mipmap.ic_avatar);
            } else {
                ImageLoader.getInstance().displayImage(jsonObject.getString("user_avatar"), holder.avatarImageView);
            }
            if (user_id.equals(mApplication.userHashMap.get("user_id"))) {
                holder.delImageView.setVisibility(View.VISIBLE);
            } else {
                holder.delImageView.setVisibility(View.GONE);
            }

            holder.nicknameTextView.setText(jsonObject.getString("nick_name"));
            holder.timeTextView.setText(hashMap.get("praise_time"));
            jsonObject = new JSONObject(hashMap.get("content_info"));
            holder.contentTextView.setText("");
            holder.nameTextView.setText("");

            switch (hashMap.get("praise_table")) {
                case "news":
                    String news_type = jsonObject.getString("news_type");
                    if (news_type.equals("通知公告")) {
                        holder.contentTextView.setText("赞了通知");
                    } else {
                        holder.contentTextView.setText("赞了新闻");
                    }
                    holder.nameTextView.setText(jsonObject.getString("news_title"));
                    break;
                case "dynamic":
                    String dynamic_type = jsonObject.getString("dynamic_type");
                    if (dynamic_type.equals("circle")) {
                        holder.contentTextView.setText("赞了圈子");
                    }
                    if (dynamic_type.equals("topic")) {
                        holder.contentTextView.setText("赞了话题");
                    }
                    if (TextUtil.isEmpty(jsonObject.getString("dynamic_content"))) {
                        holder.nameTextView.setText("无正文内容");
                    } else {
                        holder.nameTextView.setText(Html.fromHtml(jsonObject.getString("dynamic_content"), mApplication.mImageGetter, null));
                    }
                    break;
                case "activity":
                    holder.contentTextView.setText("赞了活动");
                    holder.nameTextView.setText(jsonObject.getString("activity_name"));
                    break;
            }

            holder.delImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(mActivity)
                            .setTitle("确认您的选择")
                            .setMessage("取消赞?")
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    UserAjaxParams ajaxParams;
                                    switch (hashMap.get("praise_table")) {
                                        case "news":
                                            ajaxParams = new UserAjaxParams(mApplication, "userPraise", "cancelNews");
                                            ajaxParams.put("news_id", hashMap.get("praise_tid"));
                                            mApplication.mFinalHttp.post(mApplication.apiUrlString + "c=userPraise&a=cancelNews", ajaxParams, null);
                                            break;
                                        case "dynamic":
                                            ajaxParams = new UserAjaxParams(mApplication, "userPraise", "cancelDynamic");
                                            ajaxParams.put("dynamic_id", hashMap.get("praise_tid"));
                                            mApplication.mFinalHttp.post(mApplication.apiUrlString + "c=userPraise&a=cancelDynamic", ajaxParams, null);
                                            break;
                                        case "activity":
                                            ajaxParams = new UserAjaxParams(mApplication, "userPraise", "cancelActivity");
                                            ajaxParams.put("activity_id", hashMap.get("praise_tid"));
                                            mApplication.mFinalHttp.post(mApplication.apiUrlString + "c=userPraise&a=cancelActivity", ajaxParams, null);
                                            break;
                                    }
                                    mArrayList.remove(holder.getAdapterPosition());
                                    ToastUtil.show(mActivity, "取消赞成功");
                                    notifyDataSetChanged();
                                    if (itemDelClickListener != null) {
                                        itemDelClickListener.onItemDelClick();
                                    }
                                }
                            })
                            .setNegativeButton("取消", null)
                            .show();
                }
            });

            holder.mRelativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Intent intent;
                        JSONObject jsonObject = new JSONObject(hashMap.get("content_info"));
                        switch (hashMap.get("praise_table")) {
                            case "news":
                                intent = new Intent(mActivity, NewsDetailedActivity.class);
                                String news_type = jsonObject.getString("news_type");
                                if (news_type.equals("通知公告")) {
                                    intent.putExtra("title", "通知公告详细");
                                } else {
                                    intent.putExtra("title", "新闻详细");
                                }
                                intent.putExtra("news_id", jsonObject.getString("news_id"));
                                intent.putExtra("news_link", jsonObject.getString("news_link"));
                                intent.putExtra("news_from", jsonObject.getString("news_from"));
                                intent.putExtra("news_time", jsonObject.getString("news_time"));
                                intent.putExtra("news_title", jsonObject.getString("news_title"));
                                mApplication.startActivity(mActivity, intent);
                                break;
                            case "dynamic":
                                String dynamic_type = jsonObject.getString("dynamic_type");
                                if (dynamic_type.equals("circle")) {
                                    intent = new Intent(mActivity, CircleDetailedActivity.class);
                                    intent.putExtra("id", jsonObject.getString("dynamic_id"));
                                    mApplication.startActivity(mActivity, intent);
                                }
                                if (dynamic_type.equals("topic")) {
                                    intent = new Intent(mActivity, TopicDetailedActivity.class);
                                    intent.putExtra("id", jsonObject.getString("dynamic_id"));
                                    mApplication.startActivity(mActivity, intent);
                                }
                                break;
                            case "activity":
                                intent = new Intent(mActivity, ActivityDetailedActivity.class);
                                intent.putExtra("activity_id", jsonObject.getString("activity_id"));
                                mApplication.startActivity(mActivity, intent);
                                break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup group, int viewType) {
        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.item_list_user_praise, group, false);
        return new ViewHolder(view);
    }

    public void setOnItemDelClickListener(onItemDelClickListener listener) {
        this.itemDelClickListener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout mRelativeLayout;
        private ImageView avatarImageView;
        private TextView nicknameTextView;
        private TextView timeTextView;
        private TextView contentTextView;
        private TextView nameTextView;
        private ImageView delImageView;

        private ViewHolder(View view) {
            super(view);

            mRelativeLayout = (RelativeLayout) view.findViewById(R.id.mainRelativeLayout);
            avatarImageView = (ImageView) view.findViewById(R.id.avatarImageView);
            nicknameTextView = (TextView) view.findViewById(R.id.nicknameTextView);
            timeTextView = (TextView) view.findViewById(R.id.timeTextView);
            contentTextView = (TextView) view.findViewById(R.id.contentTextView);
            nameTextView = (TextView) view.findViewById(R.id.nameTextView);
            delImageView = (ImageView) view.findViewById(R.id.delImageView);

        }

    }

    public interface onItemDelClickListener {
        void onItemDelClick();
    }

}