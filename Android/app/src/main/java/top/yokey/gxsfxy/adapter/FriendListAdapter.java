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

import net.tsz.afinal.http.AjaxCallBack;

import java.util.ArrayList;
import java.util.HashMap;

import top.yokey.gxsfxy.R;
import top.yokey.gxsfxy.activity.MyApplication;
import top.yokey.gxsfxy.activity.home.HomeActivity;
import top.yokey.gxsfxy.system.UserAjaxParams;
import top.yokey.gxsfxy.utility.DialogUtil;
import top.yokey.gxsfxy.utility.TextUtil;
import top.yokey.gxsfxy.utility.ToastUtil;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.ViewHolder> {

    private Activity mActivity;
    private MyApplication mApplication;
    private ArrayList<HashMap<String, String>> mArrayList;

    public FriendListAdapter(MyApplication application, Activity activity, ArrayList<HashMap<String, String>> arrayList) {
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
        }

        holder.nicknameTextView.setText(Html.fromHtml(hashMap.get("nick_name")));

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
            if (TextUtil.isEmpty(hashMap.get("user_sign"))) {
                holder.collegeTextView.setText("他很懒，什么都没留下");
            } else {
                holder.collegeTextView.setText(hashMap.get("user_sign"));
            }
        } else {
            holder.collegeTextView.setText(hashMap.get("user_college"));
        }

        holder.followTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtil.isEmpty(mApplication.userTokenString)) {
                    mApplication.startActivityLogin(mActivity);
                } else {
                    DialogUtil.progress(mActivity);
                    UserAjaxParams ajaxParams = new UserAjaxParams(mApplication, "userFollow", "follow");
                    ajaxParams.put("user_id", hashMap.get("user_id"));
                    mApplication.mFinalHttp.post(mApplication.apiUrlString + "c=userFollow&a=follow", ajaxParams, new AjaxCallBack<Object>() {
                        @Override
                        public void onSuccess(Object o) {
                            super.onSuccess(o);
                            DialogUtil.cancel();
                            if (mApplication.getJsonSuccess(o.toString())) {
                                ToastUtil.show(mActivity, "关注成功");
                            } else {
                                ToastUtil.show(mActivity, mApplication.getJsonError(o.toString()));
                            }
                        }

                        @Override
                        public void onFailure(Throwable t, int errorNo, String strMsg) {
                            super.onFailure(t, errorNo, strMsg);
                            ToastUtil.showFailure(mActivity);
                            DialogUtil.cancel();
                        }
                    });
                }
            }
        });

        holder.mRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mApplication.startActivityUserCenter(mActivity, hashMap.get("user_id"));
            }
        });

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup group, int viewType) {
        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.item_list_friend, group, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout mRelativeLayout;
        private ImageView avatarImageView;
        private TextView nicknameTextView;
        private ImageView genderImageView;
        private TextView collegeTextView;
        private TextView followTextView;

        private ViewHolder(View view) {
            super(view);

            mRelativeLayout = (RelativeLayout) view.findViewById(R.id.mainRelativeLayout);
            avatarImageView = (ImageView) view.findViewById(R.id.avatarImageView);
            nicknameTextView = (TextView) view.findViewById(R.id.nicknameTextView);
            genderImageView = (ImageView) view.findViewById(R.id.genderImageView);
            collegeTextView = (TextView) view.findViewById(R.id.collegeTextView);
            followTextView = (TextView) view.findViewById(R.id.followTextView);

        }

    }

}