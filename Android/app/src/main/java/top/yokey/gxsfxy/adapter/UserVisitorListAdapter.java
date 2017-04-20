package top.yokey.gxsfxy.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import top.yokey.gxsfxy.R;
import top.yokey.gxsfxy.activity.MyApplication;
import top.yokey.gxsfxy.system.UserAjaxParams;
import top.yokey.gxsfxy.utility.DialogUtil;
import top.yokey.gxsfxy.utility.TextUtil;
import top.yokey.gxsfxy.utility.ToastUtil;

public class UserVisitorListAdapter extends RecyclerView.Adapter<UserVisitorListAdapter.ViewHolder> {

    private Activity mActivity;
    private MyApplication mApplication;
    private ArrayList<HashMap<String, String>> mArrayList;

    public UserVisitorListAdapter(MyApplication application, Activity activity, ArrayList<HashMap<String, String>> arrayList) {
        this.mActivity = activity;
        this.mArrayList = arrayList;
        this.mApplication = application;
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
            holder.timeTextView.setText(hashMap.get("visitor_time"));
            holder.timeTextView.append(" 访问了你");

            holder.avatarImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mApplication.startActivityUserCenter(mActivity, user_id);
                }
            });

            holder.followTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (TextUtil.isEmpty(mApplication.userTokenString)) {
                        mApplication.startActivityLogin(mActivity);
                    } else {
                        DialogUtil.progress(mActivity);
                        UserAjaxParams ajaxParams = new UserAjaxParams(mApplication, "userFollow", "follow");
                        ajaxParams.put("user_id", user_id);
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

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup group, int viewType) {
        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.item_list_user_visitor, group, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout mRelativeLayout;
        private ImageView avatarImageView;
        private TextView nicknameTextView;
        private ImageView genderImageView;
        private TextView followTextView;
        private TextView timeTextView;

        private ViewHolder(View view) {
            super(view);

            mRelativeLayout = (RelativeLayout) view.findViewById(R.id.mainRelativeLayout);
            avatarImageView = (ImageView) view.findViewById(R.id.avatarImageView);
            nicknameTextView = (TextView) view.findViewById(R.id.nicknameTextView);
            genderImageView = (ImageView) view.findViewById(R.id.genderImageView);
            followTextView = (TextView) view.findViewById(R.id.followTextView);
            timeTextView = (TextView) view.findViewById(R.id.timeTextView);

        }

    }

}