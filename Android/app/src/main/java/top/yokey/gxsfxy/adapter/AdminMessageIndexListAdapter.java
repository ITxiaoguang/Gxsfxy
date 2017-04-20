package top.yokey.gxsfxy.adapter;

import android.app.Activity;
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
import top.yokey.gxsfxy.activity.admin.AdminMessageActivity;
import top.yokey.gxsfxy.utility.TextUtil;
import top.yokey.gxsfxy.utility.TimeUtil;

public class AdminMessageIndexListAdapter extends RecyclerView.Adapter<AdminMessageIndexListAdapter.ViewHolder> {

    private Activity mActivity;
    private MyApplication mApplication;
    private ArrayList<HashMap<String, String>> mArrayList;

    public AdminMessageIndexListAdapter(MyApplication application, Activity activity, ArrayList<HashMap<String, String>> arrayList) {
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

        try {

            JSONObject jsonObject = new JSONObject(hashMap.get("uid_info"));

            if (TextUtil.isEmpty(jsonObject.getString("user_avatar"))) {
                holder.avatarImageView.setImageResource(R.mipmap.ic_avatar);
            } else {
                ImageLoader.getInstance().displayImage(jsonObject.getString("user_avatar"), holder.avatarImageView);
            }

            holder.nicknameTextView.setText(jsonObject.getString("nick_name"));
            holder.timeTextView.setText(TimeUtil.decode(hashMap.get("message_time")));

            switch (hashMap.get("message_type")) {
                case "text":
                    holder.infoTextView.setText(Html.fromHtml(hashMap.get("message_content"), mApplication.mImageGetter, null));
                    break;
                case "image":
                    holder.infoTextView.setText("[图片]");
                    break;
                case "location":
                    holder.infoTextView.setText("[位置]");
                    String message = hashMap.get("message_content");
                    holder.infoTextView.append(message.substring(0, message.indexOf("|")));
                    break;
            }

            holder.mRelativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mApplication.startActivity(mActivity, new Intent(mActivity, AdminMessageActivity.class));
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup group, int viewType) {
        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.item_list_admin_message, group, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout mRelativeLayout;
        private ImageView avatarImageView;
        private TextView nicknameTextView;
        private TextView timeTextView;
        private TextView infoTextView;

        private ViewHolder(View view) {
            super(view);

            mRelativeLayout = (RelativeLayout) view.findViewById(R.id.mainRelativeLayout);
            avatarImageView = (ImageView) view.findViewById(R.id.avatarImageView);
            nicknameTextView = (TextView) view.findViewById(R.id.nicknameTextView);
            timeTextView = (TextView) view.findViewById(R.id.timeTextView);
            infoTextView = (TextView) view.findViewById(R.id.infoTextView);

        }

    }

}