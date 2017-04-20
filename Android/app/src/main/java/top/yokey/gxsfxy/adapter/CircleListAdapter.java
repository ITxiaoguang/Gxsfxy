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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import top.yokey.gxsfxy.R;
import top.yokey.gxsfxy.activity.MyApplication;
import top.yokey.gxsfxy.activity.PhotoActivity;
import top.yokey.gxsfxy.activity.dynamic.CircleDetailedActivity;
import top.yokey.gxsfxy.control.CenterTextView;
import top.yokey.gxsfxy.system.UserAjaxParams;
import top.yokey.gxsfxy.utility.TextUtil;
import top.yokey.gxsfxy.utility.TimeUtil;
import top.yokey.gxsfxy.utility.ToastUtil;

public class CircleListAdapter extends RecyclerView.Adapter<CircleListAdapter.ViewHolder> {

    private Activity mActivity;
    private MyApplication mApplication;
    private onItemDelClickListener itemDelClickListener;
    private ArrayList<HashMap<String, String>> mArrayList;

    public CircleListAdapter(MyApplication application, Activity activity, ArrayList<HashMap<String, String>> arrayList) {
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

            holder.nicknameTextView.setText(jsonObject.getString("nick_name"));

            if (jsonObject.getString("user_gender").equals("男")) {
                holder.genderImageView.setImageResource(R.mipmap.ic_default_boy);
            } else {
                holder.genderImageView.setImageResource(R.mipmap.ic_default_girl);
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

            holder.deviceTextView.setText("来自：");
            holder.deviceTextView.append(hashMap.get("dynamic_device"));
            holder.timeTextView.setText(TimeUtil.decode(hashMap.get("dynamic_time")));

            //内容
            holder.oneLinearLayout.setVisibility(View.GONE);
            holder.twoLinearLayout.setVisibility(View.GONE);
            holder.thrLinearLayout.setVisibility(View.GONE);
            holder.contentTextView.setVisibility(View.GONE);

            JSONArray jsonArray = new JSONArray(hashMap.get("image_info"));

            if (!TextUtil.isEmpty(hashMap.get("dynamic_content"))) {
                holder.contentTextView.setVisibility(View.VISIBLE);
                holder.contentTextView.setText(Html.fromHtml(hashMap.get("dynamic_content"), mApplication.mImageGetter, null));
            }

            if (jsonArray.length() > 6) {
                holder.thrLinearLayout.setVisibility(View.VISIBLE);
                holder.twoLinearLayout.setVisibility(View.VISIBLE);
                holder.oneLinearLayout.setVisibility(View.VISIBLE);
            } else if (jsonArray.length() > 3) {
                holder.twoLinearLayout.setVisibility(View.VISIBLE);
                holder.oneLinearLayout.setVisibility(View.VISIBLE);
            } else if (jsonArray.length() > 0) {
                holder.oneLinearLayout.setVisibility(View.VISIBLE);
            }

            for (int i = 0; i < jsonArray.length(); i++) {
                final int pos = i;
                if (i < 9) {
                    ImageLoader.getInstance().displayImage(jsonArray.getString(i), holder.mImageView[i]);
                    holder.mImageView[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(mActivity, PhotoActivity.class);
                            intent.putExtra("title", "查看图片");
                            intent.putExtra("position", pos);
                            intent.putExtra("image", hashMap.get("image_info"));
                            mApplication.startActivity(mActivity, intent);
                        }
                    });
                }
            }

            for (int i = jsonArray.length(); i < 9; i++) {
                holder.mImageView[i].setImageBitmap(null);
                holder.mImageView[i].setOnClickListener(null);
            }

            holder.locationTextView.setText(hashMap.get("dynamic_location"));

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

            holder.forwardTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (TextUtil.isEmpty(mApplication.userTokenString)) {
                        mApplication.startActivityLogin(mActivity);
                    } else {
                        new AlertDialog.Builder(mActivity)
                                .setTitle("确认您的选择")
                                .setMessage("转发到自己的圈子?")
                                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        UserAjaxParams ajaxParams = new UserAjaxParams(mApplication, "userDynamic", "circleForward");
                                        ajaxParams.put("id", hashMap.get("dynamic_id"));
                                        mApplication.mFinalHttp.post(mApplication.apiUrlString + "c=userDynamic&a=circleForward", ajaxParams, new AjaxCallBack<Object>() {
                                            @Override
                                            public void onSuccess(Object o) {
                                                super.onSuccess(o);
                                                if (mApplication.getJsonSuccess(o.toString())) {
                                                    ToastUtil.show(mActivity, "转发成功");
                                                } else {
                                                    ToastUtil.show(mActivity, mApplication.getJsonError(o.toString()));
                                                }
                                            }

                                            @Override
                                            public void onFailure(Throwable t, int errorNo, String strMsg) {
                                                super.onFailure(t, errorNo, strMsg);
                                                ToastUtil.showFailureNetwork(mActivity);
                                            }
                                        });
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                })
                                .show();
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
            holder.forwardTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ToastUtil.show(mActivity, "暂时无法转发");
                }
            });
        }

        holder.delImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mApplication.userHashMap.isEmpty()) {
                    new AlertDialog.Builder(mActivity).setTitle("确认您的选择")
                            .setMessage("删除这条动态")
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
                Intent intent = new Intent(mActivity, CircleDetailedActivity.class);
                intent.putExtra("id", hashMap.get("dynamic_id"));
                mApplication.startActivity(mActivity, intent);
            }
        });

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup group, int viewType) {
        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.item_list_circle, group, false);
        return new ViewHolder(view);
    }

    public void setOnItemDelClickListener(onItemDelClickListener listener) {
        this.itemDelClickListener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout mRelativeLayout;
        private ImageView avatarImageView;
        private TextView nicknameTextView;
        private ImageView genderImageView;
        private TextView deviceTextView;
        private TextView timeTextView;
        private ImageView delImageView;
        private TextView contentTextView;

        private LinearLayout oneLinearLayout;
        private LinearLayout twoLinearLayout;
        private LinearLayout thrLinearLayout;
        private ImageView[] mImageView;
        private TextView locationTextView;

        private CenterTextView forwardTextView;
        private CenterTextView commentTextView;
        private CenterTextView praiseTextView;

        private ViewHolder(View view) {
            super(view);

            mRelativeLayout = (RelativeLayout) view.findViewById(R.id.mainRelativeLayout);
            avatarImageView = (ImageView) view.findViewById(R.id.avatarImageView);
            nicknameTextView = (TextView) view.findViewById(R.id.nicknameTextView);
            genderImageView = (ImageView) view.findViewById(R.id.genderImageView);
            deviceTextView = (TextView) view.findViewById(R.id.deviceTextView);
            timeTextView = (TextView) view.findViewById(R.id.timeTextView);
            delImageView = (ImageView) view.findViewById(R.id.delImageView);
            contentTextView = (TextView) view.findViewById(R.id.contentTextView);

            oneLinearLayout = (LinearLayout) view.findViewById(R.id.oneLinearLayout);
            twoLinearLayout = (LinearLayout) view.findViewById(R.id.twoLinearLayout);
            thrLinearLayout = (LinearLayout) view.findViewById(R.id.thrLinearLayout);
            mImageView = new ImageView[9];
            mImageView[0] = (ImageView) view.findViewById(R.id.oneImageView);
            mImageView[1] = (ImageView) view.findViewById(R.id.twoImageView);
            mImageView[2] = (ImageView) view.findViewById(R.id.thrImageView);
            mImageView[3] = (ImageView) view.findViewById(R.id.fouImageView);
            mImageView[4] = (ImageView) view.findViewById(R.id.fivImageView);
            mImageView[5] = (ImageView) view.findViewById(R.id.sixImageView);
            mImageView[6] = (ImageView) view.findViewById(R.id.sevImageView);
            mImageView[7] = (ImageView) view.findViewById(R.id.eigImageView);
            mImageView[8] = (ImageView) view.findViewById(R.id.nigImageView);
            locationTextView = (TextView) view.findViewById(R.id.locationTextView);

            forwardTextView = (CenterTextView) view.findViewById(R.id.forwardTextView);
            commentTextView = (CenterTextView) view.findViewById(R.id.commentTextView);
            praiseTextView = (CenterTextView) view.findViewById(R.id.praiseTextView);

        }

    }

    public interface onItemDelClickListener {
        void onItemDelClick();
    }

}