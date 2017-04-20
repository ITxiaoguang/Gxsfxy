package top.yokey.gxsfxy.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import net.tsz.afinal.http.AjaxCallBack;

import java.util.List;

import top.yokey.gxsfxy.R;
import top.yokey.gxsfxy.activity.MyApplication;
import top.yokey.gxsfxy.system.FriendBean;
import top.yokey.gxsfxy.system.UserAjaxParams;
import top.yokey.gxsfxy.utility.DialogUtil;
import top.yokey.gxsfxy.utility.TextUtil;
import top.yokey.gxsfxy.utility.ToastUtil;

public class FollowMineListAdapter extends BaseAdapter implements SectionIndexer {

    private Activity mActivity;
    private List<FriendBean> mList;
    private LayoutInflater mInflater;
    private MyApplication mApplication;

    public FollowMineListAdapter(MyApplication application, Activity activity, List<FriendBean> list) {
        this.mList = list;
        this.mActivity = activity;
        this.mApplication = application;
        this.mInflater = LayoutInflater.from(activity);
    }

    public int getCount() {
        return this.mList.size();
    }

    public Object getItem(int position) {
        return mList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup arg2) {

        final holder holder;
        final FriendBean friendBean = mList.get(position);
        final int section = getSectionForPosition(position);

        if (view == null) {
            holder = new holder();
            view = mInflater.inflate(R.layout.item_list_contact, null);
            holder.mRelativeLayout = (RelativeLayout) view.findViewById(R.id.mainRelativeLayout);
            holder.letterTextView = (TextView) view.findViewById(R.id.letterTextView);
            holder.avatarImageView = (ImageView) view.findViewById(R.id.avatarImageView);
            holder.nicknameTextView = (TextView) view.findViewById(R.id.nicknameTextView);
            holder.genderImageView = (ImageView) view.findViewById(R.id.genderImageView);
            holder.signTextView = (TextView) view.findViewById(R.id.signTextView);
            holder.followTextView = (TextView) view.findViewById(R.id.followTextView);
            view.setTag(holder);
        } else {
            holder = (holder) view.getTag();
        }

        if (position == getPositionForSection(section)) {
            holder.letterTextView.setVisibility(View.VISIBLE);
            holder.letterTextView.setText(friendBean.getLetters());
        } else {
            holder.letterTextView.setVisibility(View.GONE);
        }

        if (TextUtil.isEmpty(friendBean.getAvatar())) {
            holder.avatarImageView.setImageResource(R.mipmap.ic_avatar);
        } else {
            ImageLoader.getInstance().displayImage(friendBean.getAvatar(), holder.avatarImageView);
        }

        holder.nicknameTextView.setText(friendBean.getNickname());

        if (friendBean.getGender().equals("男")) {
            holder.genderImageView.setImageResource(R.mipmap.ic_default_boy);
        } else {
            holder.genderImageView.setImageResource(R.mipmap.ic_default_girl);
        }

        if (TextUtil.isEmpty(friendBean.getSign())) {
            holder.signTextView.setText("他很懒,什么都没留下...");
        } else {
            holder.signTextView.setText(friendBean.getSign());
        }

        holder.followTextView.setText("关注他");

        holder.followTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtil.progress(mActivity);
                UserAjaxParams ajaxParams = new UserAjaxParams(mApplication, "userFollow", "follow");
                ajaxParams.put("user_id", friendBean.getId());
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
        });

        holder.mRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mApplication.startActivityUserCenter(mActivity, friendBean.getId());
            }
        });

        holder.mRelativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mApplication.startActivityChatOnly(mActivity, friendBean.getId());
                return false;
            }
        });

        return view;
    }

    public int getSectionForPosition(int position) {
        return mList.get(position).getLetters().charAt(0);
    }

    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = mList.get(i).getLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public Object[] getSections() {
        return null;
    }

    class holder {

        private RelativeLayout mRelativeLayout;
        private TextView letterTextView;
        private ImageView avatarImageView;
        private TextView nicknameTextView;
        private ImageView genderImageView;
        private TextView signTextView;
        private TextView followTextView;

    }

}