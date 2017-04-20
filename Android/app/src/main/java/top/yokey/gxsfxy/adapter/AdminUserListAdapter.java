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

import java.util.ArrayList;
import java.util.HashMap;

import top.yokey.gxsfxy.R;
import top.yokey.gxsfxy.activity.MyApplication;
import top.yokey.gxsfxy.activity.admin.AdminUserDetailedActivity;
import top.yokey.gxsfxy.utility.TextUtil;
import top.yokey.gxsfxy.utility.TimeUtil;

public class AdminUserListAdapter extends RecyclerView.Adapter<AdminUserListAdapter.ViewHolder> {

    private Activity mActivity;
    private MyApplication mApplication;
    private ArrayList<HashMap<String, String>> mArrayList;

    public AdminUserListAdapter(MyApplication application, Activity activity, ArrayList<HashMap<String, String>> arrayList) {
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

        String keyword = hashMap.get("keyword");

        if (!TextUtil.isEmpty(keyword)) {
            hashMap.put("nick_name", TextUtil.replaceKeyword(hashMap.get("nick_name"), keyword));
            hashMap.put("user_college", TextUtil.replaceKeyword(hashMap.get("user_college"), keyword));
        }

        holder.nicknameTextView.setText(Html.fromHtml(hashMap.get("nick_name")));
        holder.timeTextView.setText(TimeUtil.decode(hashMap.get("reg_time")));
        holder.timeTextView.append(" 注册");

        if (TextUtil.isEmpty(hashMap.get("user_avatar"))) {
            holder.avatarImageView.setImageResource(R.mipmap.ic_avatar);
        } else {
            ImageLoader.getInstance().displayImage(hashMap.get("user_avatar"), holder.avatarImageView);
        }

        if (hashMap.get("user_gender").equals("男")) {
            holder.genderImageView.setImageResource(R.mipmap.ic_default_boy);
        } else {
            holder.genderImageView.setImageResource(R.mipmap.ic_default_girl);
        }

        if (TextUtil.isEmpty(hashMap.get("user_college"))) {
            holder.collegeTextView.setText(hashMap.get("user_mobile"));
        } else {
            holder.collegeTextView.setText(hashMap.get("user_college"));
        }

        holder.mRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, AdminUserDetailedActivity.class);
                intent.putExtra("user_id", hashMap.get("user_id"));
                mApplication.startActivity(mActivity, intent);
            }
        });

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup group, int viewType) {
        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.item_list_admin_user, group, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout mRelativeLayout;
        private ImageView avatarImageView;
        private TextView nicknameTextView;
        private ImageView genderImageView;
        private TextView collegeTextView;
        private TextView timeTextView;

        private ViewHolder(View view) {
            super(view);

            mRelativeLayout = (RelativeLayout) view.findViewById(R.id.mainRelativeLayout);
            avatarImageView = (ImageView) view.findViewById(R.id.avatarImageView);
            nicknameTextView = (TextView) view.findViewById(R.id.nicknameTextView);
            genderImageView = (ImageView) view.findViewById(R.id.genderImageView);
            collegeTextView = (TextView) view.findViewById(R.id.collegeTextView);
            timeTextView = (TextView) view.findViewById(R.id.timeTextView);

        }

    }

}