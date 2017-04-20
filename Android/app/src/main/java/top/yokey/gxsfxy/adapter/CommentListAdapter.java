package top.yokey.gxsfxy.adapter;

import android.app.Activity;
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
import top.yokey.gxsfxy.utility.TextUtil;
import top.yokey.gxsfxy.utility.TimeUtil;

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.ViewHolder> {

    private Activity mActivity;
    private MyApplication mApplication;
    private onItemClickListener itemClickListener;
    private ArrayList<HashMap<String, String>> mArrayList;

    public CommentListAdapter(MyApplication application, Activity activity, ArrayList<HashMap<String, String>> arrayList) {
        this.mActivity = activity;
        this.mArrayList = arrayList;
        this.mApplication = application;
        this.itemClickListener = null;
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
            holder.nicknameTextView.setText(jsonObject.getString("nick_name"));
            if (jsonObject.getString("user_gender").equals("男")) {
                holder.genderImageView.setImageResource(R.mipmap.ic_default_boy);
            } else {
                holder.genderImageView.setImageResource(R.mipmap.ic_default_girl);
            }
            holder.timeTextView.setText("发表于 ");
            holder.timeTextView.append(TimeUtil.decode(hashMap.get("comment_time")));
            holder.contentTextView.setText(Html.fromHtml(hashMap.get("comment_content"), mApplication.mImageGetter, null));
            if (!hashMap.get("comment_rid").equals("-1")) {
                String content;
                holder.replyTextView.setVisibility(View.VISIBLE);
                jsonObject = new JSONObject(hashMap.get("comment_reply"));
                if (jsonObject.toString().contains("comment_content")) {
                    content = "回复@" + jsonObject.getString("nick_name") + "：" + jsonObject.getString("comment_content");
                } else {
                    content = "原评论已删除！";
                }
                holder.replyTextView.setText(Html.fromHtml(content, mApplication.mImageGetter, null));
            } else {
                holder.replyTextView.setVisibility(View.GONE);
            }

            holder.avatarImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mApplication.startActivityUserCenter(mActivity, user_id);
                }
            });

            holder.mRelativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(hashMap.get("comment_id"), holder.nicknameTextView.getText().toString());
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup group, int viewType) {
        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.item_list_comment, group, false);
        return new ViewHolder(view);
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        this.itemClickListener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout mRelativeLayout;
        private ImageView avatarImageView;
        private TextView nicknameTextView;
        private ImageView genderImageView;
        private TextView timeTextView;
        private TextView contentTextView;
        private TextView replyTextView;

        private ViewHolder(View view) {
            super(view);

            mRelativeLayout = (RelativeLayout) view.findViewById(R.id.mainRelativeLayout);
            avatarImageView = (ImageView) view.findViewById(R.id.avatarImageView);
            nicknameTextView = (TextView) view.findViewById(R.id.nicknameTextView);
            genderImageView = (ImageView) view.findViewById(R.id.genderImageView);
            timeTextView = (TextView) view.findViewById(R.id.timeTextView);
            contentTextView = (TextView) view.findViewById(R.id.contentTextView);
            replyTextView = (TextView) view.findViewById(R.id.replyTextView);

        }

    }

    public interface onItemClickListener {
        void onItemClick(String comment_id, String nick_name);
    }

}