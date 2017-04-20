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
import top.yokey.gxsfxy.activity.MyApplication;
import top.yokey.gxsfxy.activity.dynamic.TopicDetailedActivity;
import top.yokey.gxsfxy.control.CenterTextView;
import top.yokey.gxsfxy.system.UserAjaxParams;
import top.yokey.gxsfxy.utility.TextUtil;
import top.yokey.gxsfxy.utility.TimeUtil;
import top.yokey.gxsfxy.utility.ToastUtil;

public class TopicListAdapter extends RecyclerView.Adapter<TopicListAdapter.ViewHolder> {

    private Activity mActivity;
    private MyApplication mApplication;
    private onItemDelClickListener itemDelClickListener;
    private ArrayList<HashMap<String, String>> mArrayList;

    public TopicListAdapter(MyApplication application, Activity activity, ArrayList<HashMap<String, String>> arrayList) {
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
            //用户信息
            JSONObject jsonObject = new JSONObject(hashMap.get("user_info"));
            if (TextUtil.isEmpty(jsonObject.getString("user_avatar"))) {
                holder.avatarImageView.setImageResource(R.mipmap.ic_avatar);
            } else {
                ImageLoader.getInstance().displayImage(jsonObject.getString("user_avatar"), holder.avatarImageView);
            }
            if (mApplication.userHashMap.isEmpty()) {
                holder.delImageView.setVisibility(View.GONE);
            } else {
                if (jsonObject.getString("user_id").equals(mApplication.userHashMap.get("user_id"))) {
                    holder.delImageView.setVisibility(View.VISIBLE);
                } else {
                    holder.delImageView.setVisibility(View.GONE);
                }
            }
            //内容
            String title = "# <font color='#115FB2'>" + hashMap.get("dynamic_title") + "</font> #";
            holder.titleTextView.setText(Html.fromHtml(title));
            holder.contentTextView.setText(Html.fromHtml(hashMap.get("dynamic_content"), mApplication.mImageGetter, null));
            //下面的
            String info = jsonObject.getString("nick_name") + " 发表于 "
                    + TimeUtil.decode(hashMap.get("dynamic_time"));
            holder.infoTextView.setText(Html.fromHtml(info));
            if (hashMap.get("dynamic_comment").equals("0")) {
                holder.commentTextView.setText("评论");
            } else {
                holder.commentTextView.setText(hashMap.get("dynamic_comment"));
            }
            if (hashMap.get("dynamic_praise").equals("0")) {
                holder.praiseTextView.setText("赞");
            } else {
                holder.praiseTextView.setText(hashMap.get("dynamic_praise"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.delImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mApplication.userHashMap.isEmpty()) {
                    new AlertDialog.Builder(mActivity).setTitle("确认您的选择")
                            .setMessage("删除这个话题")
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    UserAjaxParams ajaxParams = new UserAjaxParams(mApplication, "userDynamic", "dynamicDel");
                                    ajaxParams.put("dynamic_id", hashMap.get("dynamic_id"));
                                    mApplication.mFinalHttp.post(mApplication.apiUrlString + "c=userDynamic&a=dynamicDel", ajaxParams, null);
                                    mArrayList.remove(holder.getAdapterPosition());
                                    ToastUtil.showSuccess(mActivity);
                                    notifyDataSetChanged();
                                    if (itemDelClickListener != null) {
                                        itemDelClickListener.onItemDelClick();
                                    }
                                }
                            })
                            .setNegativeButton("取消", null)
                            .show();
                }
            }
        });

        holder.mRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, TopicDetailedActivity.class);
                intent.putExtra("id", hashMap.get("dynamic_id"));
                mApplication.startActivity(mActivity, intent);
            }
        });

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup group, int viewType) {
        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.item_list_topic, group, false);
        return new ViewHolder(view);
    }

    public void setOnItemDelClickListener(onItemDelClickListener listener) {
        this.itemDelClickListener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout mRelativeLayout;
        private ImageView avatarImageView;
        private TextView titleTextView;
        private TextView contentTextView;
        private TextView infoTextView;
        private ImageView delImageView;

        private CenterTextView commentTextView;
        private CenterTextView praiseTextView;

        private ViewHolder(View view) {
            super(view);

            mRelativeLayout = (RelativeLayout) view.findViewById(R.id.mainRelativeLayout);
            avatarImageView = (ImageView) view.findViewById(R.id.avatarImageView);
            delImageView = (ImageView) view.findViewById(R.id.delImageView);
            titleTextView = (TextView) view.findViewById(R.id.titleTextView);
            contentTextView = (TextView) view.findViewById(R.id.contentTextView);
            infoTextView = (TextView) view.findViewById(R.id.infoTextView);
            commentTextView = (CenterTextView) view.findViewById(R.id.commentTextView);
            praiseTextView = (CenterTextView) view.findViewById(R.id.praiseTextView);

        }

    }

    public interface onItemDelClickListener {
        void onItemDelClick();
    }

}