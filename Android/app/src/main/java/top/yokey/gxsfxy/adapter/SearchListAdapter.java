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
import android.widget.EditText;
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
import top.yokey.gxsfxy.activity.dynamic.ActivityDetailedActivity;
import top.yokey.gxsfxy.activity.dynamic.TopicDetailedActivity;
import top.yokey.gxsfxy.activity.MyApplication;
import top.yokey.gxsfxy.activity.PhotoActivity;
import top.yokey.gxsfxy.activity.dynamic.CircleDetailedActivity;
import top.yokey.gxsfxy.control.CenterTextView;
import top.yokey.gxsfxy.system.UserAjaxParams;
import top.yokey.gxsfxy.utility.DialogUtil;
import top.yokey.gxsfxy.utility.TextUtil;
import top.yokey.gxsfxy.utility.TimeUtil;
import top.yokey.gxsfxy.utility.ToastUtil;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.ViewHolder> {

    private Activity mActivity;
    private MyApplication mApplication;
    private ArrayList<HashMap<String, String>> mArrayList;

    public SearchListAdapter(MyApplication application, Activity activity, ArrayList<HashMap<String, String>> arrayList) {
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

        holder.userLine8View.setVisibility(View.GONE);
        holder.userTitleTextView.setVisibility(View.GONE);
        holder.userLineView.setVisibility(View.GONE);
        holder.userRelativeLayout.setVisibility(View.GONE);

        holder.phoneLine8View.setVisibility(View.GONE);
        holder.phoneTitleTextView.setVisibility(View.GONE);
        holder.phoneLineView.setVisibility(View.GONE);
        holder.phoneRelativeLayout.setVisibility(View.GONE);

        holder.circleLine8View.setVisibility(View.GONE);
        holder.circleTitleTextView.setVisibility(View.GONE);
        holder.circleLineView.setVisibility(View.GONE);
        holder.circleRelativeLayout.setVisibility(View.GONE);

        holder.topicLine8View.setVisibility(View.GONE);
        holder.topicTitleTextView.setVisibility(View.GONE);
        holder.topicLineView.setVisibility(View.GONE);
        holder.topicRelativeLayout.setVisibility(View.GONE);

        holder.activityLine8View.setVisibility(View.GONE);
        holder.activityTitleTextView.setVisibility(View.GONE);
        holder.activityLineView.setVisibility(View.GONE);
        holder.activityRelativeLayout.setVisibility(View.GONE);

        String keyword = hashMap.get("keyword");

        if (hashMap.get("search_type").equals("user")) {
            //控件显示
            if (hashMap.get("search_title").equals("1")) {
                holder.userLine8View.setVisibility(View.VISIBLE);
                holder.userTitleTextView.setVisibility(View.VISIBLE);
                holder.userLineView.setVisibility(View.VISIBLE);
            } else {
                holder.userLine8View.setVisibility(View.GONE);
                holder.userTitleTextView.setVisibility(View.GONE);
                holder.userLineView.setVisibility(View.GONE);
            }
            holder.userRelativeLayout.setVisibility(View.VISIBLE);
            holder.userFollowTextView.setVisibility(View.VISIBLE);
            if (!mApplication.userHashMap.isEmpty()) {
                if (hashMap.get("user_id").equals(mApplication.userHashMap.get("user_id"))) {
                    holder.userFollowTextView.setVisibility(View.GONE);
                    hashMap.put("nick_name", TextUtil.replaceKeyword(hashMap.get("nick_name"), keyword) + "（你）");
                }
            }
            //关键字转换
            if (!TextUtil.isEmpty(keyword)) {
                hashMap.put("nick_name", TextUtil.replaceKeyword(hashMap.get("nick_name"), keyword));
                hashMap.put("user_college", TextUtil.replaceKeyword(hashMap.get("user_college"), keyword));
            }
            //赋值
            if (TextUtil.isEmpty(hashMap.get("user_avatar"))) {
                holder.userAvatarImageView.setImageResource(R.mipmap.ic_avatar);
            } else {
                ImageLoader.getInstance().displayImage(hashMap.get("user_avatar"), holder.userAvatarImageView);
            }
            holder.userNicknameTextView.setText(Html.fromHtml(hashMap.get("nick_name")));
            if (hashMap.get("user_gender").equals("男")) {
                holder.userGenderImageView.setImageResource(R.mipmap.ic_default_boy);
            } else {
                holder.userGenderImageView.setImageResource(R.mipmap.ic_default_girl);
            }
            if (TextUtil.isEmpty(hashMap.get("user_college"))) {
                holder.userCollegeTextView.setText("尚未绑定教务系统账号");
            } else {
                holder.userCollegeTextView.setText(Html.fromHtml(hashMap.get("user_college")));
            }
            if (TextUtil.isEmpty(hashMap.get("user_sign"))) {
                holder.userSignTextView.setText("他很懒，什么都没留下。。。");
            } else {
                holder.userSignTextView.setText(hashMap.get("user_sign"));
            }

            holder.userFollowTextView.setOnClickListener(new View.OnClickListener() {
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

            holder.userRelativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mApplication.startActivityUserCenter(mActivity, hashMap.get("user_id"));
                }
            });

        }

        //电话
        if (hashMap.get("search_type").equals("phone")) {
            //控件显示
            if (hashMap.get("search_title").equals("1")) {
                holder.phoneLine8View.setVisibility(View.VISIBLE);
                holder.phoneTitleTextView.setVisibility(View.VISIBLE);
                holder.phoneLineView.setVisibility(View.VISIBLE);
            } else {
                holder.phoneLine8View.setVisibility(View.GONE);
                holder.phoneTitleTextView.setVisibility(View.GONE);
                holder.phoneLineView.setVisibility(View.GONE);
            }
            holder.phoneRelativeLayout.setVisibility(View.VISIBLE);
            //关键字转换
            String title = hashMap.get("phone_name").substring(0, 1);
            if (!TextUtil.isEmpty(keyword)) {
                hashMap.put("phone_name", TextUtil.replaceKeyword(hashMap.get("phone_name"), keyword));
                hashMap.put("phone_class", TextUtil.replaceKeyword(hashMap.get("phone_class"), keyword));
                hashMap.put("phone_type", TextUtil.replaceKeyword(hashMap.get("phone_type"), keyword));
                hashMap.put("phone_address", TextUtil.replaceKeyword(hashMap.get("phone_address"), keyword));
            }

            holder.phoneMainTextView.setText(title);
            holder.phoneNameTextView.setText(Html.fromHtml(hashMap.get("phone_name")));
            holder.phoneClassTextView.setText(Html.fromHtml(hashMap.get("phone_class")));
            holder.phoneTypeTextView.setText(Html.fromHtml(hashMap.get("phone_type")));
            holder.phoneAddressTextView.setText(Html.fromHtml(hashMap.get("phone_address")));

            holder.phoneRelativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(mActivity)
                            .setTitle("确认您的选择")
                            .setMessage("拨打电话?")
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    mApplication.startCall(mActivity, hashMap.get("phone_number"));
                                }
                            })
                            .setNegativeButton("取消", null)
                            .show();
                }
            });

            holder.phoneRelativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mApplication.startCall(mActivity, hashMap.get("phone_number"));
                    return false;
                }
            });
        }

        //动态
        if (hashMap.get("search_type").equals("dynamic")) {
            //圈子
            if (hashMap.get("dynamic_type").equals("circle")) {
                //控件显示
                if (hashMap.get("search_title").equals("1")) {
                    holder.circleLine8View.setVisibility(View.VISIBLE);
                    holder.circleTitleTextView.setVisibility(View.VISIBLE);
                    holder.circleLineView.setVisibility(View.VISIBLE);
                } else {
                    holder.circleLine8View.setVisibility(View.GONE);
                    holder.circleTitleTextView.setVisibility(View.GONE);
                    holder.circleLineView.setVisibility(View.GONE);
                }
                holder.circleRelativeLayout.setVisibility(View.VISIBLE);
                //关键字转换
                if (!TextUtil.isEmpty(keyword)) {
                    holder.circleEditText.setText(keyword);
                    hashMap.put("dynamic_content", TextUtil.replaceKeyword(hashMap.get("dynamic_content"), TextUtil.replaceFace(holder.circleEditText.getText())));
                    hashMap.put("dynamic_location", TextUtil.replaceKeyword(hashMap.get("dynamic_location"), keyword));
                }
                try {
                    //用户信息
                    JSONObject jsonObject = new JSONObject(hashMap.get("user_info"));
                    if (TextUtil.isEmpty(jsonObject.getString("user_avatar"))) {
                        holder.circleAvatarImageView.setImageResource(R.mipmap.ic_avatar);
                    } else {
                        ImageLoader.getInstance().displayImage(jsonObject.getString("user_avatar"), holder.circleAvatarImageView);
                    }
                    holder.circleNicknameTextView.setText(jsonObject.getString("nick_name"));
                    if (jsonObject.getString("user_gender").equals("男")) {
                        holder.circleGenderImageView.setImageResource(R.mipmap.ic_default_boy);
                    } else {
                        holder.circleGenderImageView.setImageResource(R.mipmap.ic_default_girl);
                    }
                    holder.circleDeviceTextView.setText("来自：");
                    holder.circleDeviceTextView.append(hashMap.get("dynamic_device"));
                    holder.circleTimeTextView.setText(TimeUtil.decode(hashMap.get("dynamic_time")));
                    //内容
                    holder.circleOneLinearLayout.setVisibility(View.GONE);
                    holder.circleTwoLinearLayout.setVisibility(View.GONE);
                    holder.circleThrLinearLayout.setVisibility(View.GONE);
                    holder.circleContentTextView.setVisibility(View.GONE);
                    JSONArray jsonArray = new JSONArray(hashMap.get("image_info"));
                    if (!TextUtil.isEmpty(hashMap.get("dynamic_content"))) {
                        holder.circleContentTextView.setVisibility(View.VISIBLE);
                        holder.circleContentTextView.setText(Html.fromHtml(hashMap.get("dynamic_content"), mApplication.mImageGetter, null));
                    }
                    if (jsonArray.length() > 6) {
                        holder.circleThrLinearLayout.setVisibility(View.VISIBLE);
                        holder.circleTwoLinearLayout.setVisibility(View.VISIBLE);
                        holder.circleOneLinearLayout.setVisibility(View.VISIBLE);
                    } else if (jsonArray.length() > 3) {
                        holder.circleTwoLinearLayout.setVisibility(View.VISIBLE);
                        holder.circleOneLinearLayout.setVisibility(View.VISIBLE);
                    } else if (jsonArray.length() > 0) {
                        holder.circleOneLinearLayout.setVisibility(View.VISIBLE);
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        final int pos = i;
                        if (i < 9) {
                            ImageLoader.getInstance().displayImage(jsonArray.getString(i), holder.circleImageView[i]);
                            holder.circleImageView[i].setOnClickListener(new View.OnClickListener() {
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
                        holder.circleImageView[i].setImageBitmap(null);
                        holder.circleImageView[i].setOnClickListener(null);
                    }
                    holder.circleLocationTextView.setText(Html.fromHtml(hashMap.get("dynamic_location")));
                    if (hashMap.get("dynamic_comment").equals("0")) {
                        holder.circleCommentTextView.setText("评论");
                    } else {
                        holder.circleCommentTextView.setText(hashMap.get("dynamic_comment"));
                    }
                    if (hashMap.get("dynamic_praise").equals("0")) {
                        holder.circlePraiseTextView.setText("赞");
                    } else {
                        holder.circlePraiseTextView.setText(hashMap.get("dynamic_praise"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                holder.circleMainRelativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mActivity, CircleDetailedActivity.class);
                        intent.putExtra("id", hashMap.get("dynamic_id"));
                        mApplication.startActivity(mActivity, intent);
                    }
                });

                holder.circleForwardTextView.setOnClickListener(new View.OnClickListener() {
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

            }
            //话题
            if (hashMap.get("dynamic_type").equals("topic")) {
                //控件显示
                if (hashMap.get("search_title").equals("1")) {
                    holder.topicLine8View.setVisibility(View.VISIBLE);
                    holder.topicTitleTextView.setVisibility(View.VISIBLE);
                    holder.topicLineView.setVisibility(View.VISIBLE);
                } else {
                    holder.topicLine8View.setVisibility(View.GONE);
                    holder.topicTitleTextView.setVisibility(View.GONE);
                    holder.topicLineView.setVisibility(View.GONE);
                }
                holder.topicRelativeLayout.setVisibility(View.VISIBLE);
                //关键字转换
                if (!TextUtil.isEmpty(keyword)) {
                    holder.topicEditText.setText(keyword);
                    hashMap.put("dynamic_title", TextUtil.replaceKeyword(hashMap.get("dynamic_title"), keyword));
                    hashMap.put("dynamic_content", TextUtil.replaceKeyword(hashMap.get("dynamic_content"), TextUtil.replaceFace(holder.topicEditText.getText())));
                }
                try {
                    //用户信息
                    JSONObject jsonObject = new JSONObject(hashMap.get("user_info"));
                    if (TextUtil.isEmpty(jsonObject.getString("user_avatar"))) {
                        holder.topicAvatarImageView.setImageResource(R.mipmap.ic_avatar);
                    } else {
                        ImageLoader.getInstance().displayImage(jsonObject.getString("user_avatar"), holder.topicAvatarImageView);
                    }
                    //内容
                    String title = "# " + hashMap.get("dynamic_title") + " #";
                    holder.topicNameTextView.setText(Html.fromHtml(title));
                    holder.topicContentTextView.setText(Html.fromHtml(hashMap.get("dynamic_content")));
                    //下面的
                    String info = jsonObject.getString("nick_name") + " 发表于 "
                            + TimeUtil.decode(hashMap.get("dynamic_time"));
                    holder.topicInfoTextView.setText(Html.fromHtml(info));
                    if (hashMap.get("dynamic_comment").equals("0")) {
                        holder.topicCommentTextView.setText("评论");
                    } else {
                        holder.topicCommentTextView.setText(hashMap.get("dynamic_comment"));
                    }
                    if (hashMap.get("dynamic_praise").equals("0")) {
                        holder.topicPraiseTextView.setText("赞");
                    } else {
                        holder.topicPraiseTextView.setText(hashMap.get("dynamic_praise"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                holder.topicMainRelativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mActivity, TopicDetailedActivity.class);
                        intent.putExtra("id", hashMap.get("dynamic_id"));
                        mApplication.startActivity(mActivity, intent);
                    }
                });
            }
        }

        //活动
        if (hashMap.get("search_type").equals("activity")) {
            //控件显示
            if (hashMap.get("search_title").equals("1")) {
                holder.activityLine8View.setVisibility(View.VISIBLE);
                holder.activityTitleTextView.setVisibility(View.VISIBLE);
                holder.activityLineView.setVisibility(View.VISIBLE);
            } else {
                holder.activityLine8View.setVisibility(View.GONE);
                holder.activityTitleTextView.setVisibility(View.GONE);
                holder.activityLineView.setVisibility(View.GONE);
            }
            holder.activityRelativeLayout.setVisibility(View.VISIBLE);
            //关键字转换
            if (!TextUtil.isEmpty(keyword)) {
                hashMap.put("activity_name", TextUtil.replaceKeyword(hashMap.get("activity_name"), keyword));
            }
            //内容
            ImageLoader.getInstance().displayImage(hashMap.get("activity_image"), holder.activityMainImageView);
            holder.activityNameTextView.setText(Html.fromHtml(hashMap.get("activity_name")));
            holder.activityMainRelativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mActivity, ActivityDetailedActivity.class);
                    intent.putExtra("activity_id", hashMap.get("activity_id"));
                    mApplication.startActivity(mActivity, intent);
                }
            });
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup group, int viewType) {
        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.item_list_search, group, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        //用户
        private View userLine8View;
        private TextView userTitleTextView;
        private View userLineView;
        private RelativeLayout userRelativeLayout;
        private ImageView userAvatarImageView;
        private TextView userNicknameTextView;
        private ImageView userGenderImageView;
        private TextView userCollegeTextView;
        private TextView userFollowTextView;
        private TextView userSignTextView;

        //电话
        private View phoneLine8View;
        private TextView phoneTitleTextView;
        private View phoneLineView;
        private RelativeLayout phoneRelativeLayout;
        private TextView phoneMainTextView;
        private TextView phoneNameTextView;
        private TextView phoneClassTextView;
        private TextView phoneAddressTextView;
        private TextView phoneTypeTextView;

        //圈子
        private View circleLine8View;
        private TextView circleTitleTextView;
        private EditText circleEditText;
        private View circleLineView;
        private RelativeLayout circleRelativeLayout;
        private RelativeLayout circleMainRelativeLayout;
        private ImageView circleAvatarImageView;
        private TextView circleNicknameTextView;
        private ImageView circleGenderImageView;
        private TextView circleDeviceTextView;
        private TextView circleTimeTextView;
        private TextView circleContentTextView;
        private LinearLayout circleOneLinearLayout;
        private LinearLayout circleTwoLinearLayout;
        private LinearLayout circleThrLinearLayout;
        private ImageView[] circleImageView;
        private TextView circleLocationTextView;
        private CenterTextView circleForwardTextView;
        private CenterTextView circleCommentTextView;
        private CenterTextView circlePraiseTextView;

        //话题
        private View topicLine8View;
        private TextView topicTitleTextView;
        private EditText topicEditText;
        private View topicLineView;
        private RelativeLayout topicRelativeLayout;
        private RelativeLayout topicMainRelativeLayout;
        private ImageView topicAvatarImageView;
        private TextView topicNameTextView;
        private TextView topicContentTextView;
        private TextView topicInfoTextView;
        private CenterTextView topicCommentTextView;
        private CenterTextView topicPraiseTextView;

        //活动
        private View activityLine8View;
        private TextView activityTitleTextView;
        private View activityLineView;
        private RelativeLayout activityRelativeLayout;
        private RelativeLayout activityMainRelativeLayout;
        private ImageView activityMainImageView;
        private TextView activityNameTextView;

        private ViewHolder(View view) {
            super(view);

            //用户
            userLine8View = view.findViewById(R.id.userLine8View);
            userTitleTextView = (TextView) view.findViewById(R.id.userTitleTextView);
            userLineView = view.findViewById(R.id.userLineView);
            userRelativeLayout = (RelativeLayout) view.findViewById(R.id.userRelativeLayout);
            userAvatarImageView = (ImageView) view.findViewById(R.id.userAvatarImageView);
            userNicknameTextView = (TextView) view.findViewById(R.id.userNicknameTextView);
            userGenderImageView = (ImageView) view.findViewById(R.id.userGenderImageView);
            userCollegeTextView = (TextView) view.findViewById(R.id.userCollegeTextView);
            userFollowTextView = (TextView) view.findViewById(R.id.userFollowTextView);
            userSignTextView = (TextView) view.findViewById(R.id.userSignTextView);

            //电话
            phoneLine8View = view.findViewById(R.id.phoneLine8View);
            phoneTitleTextView = (TextView) view.findViewById(R.id.phoneTitleTextView);
            phoneLineView = view.findViewById(R.id.phoneLineView);
            phoneRelativeLayout = (RelativeLayout) view.findViewById(R.id.phoneRelativeLayout);
            phoneMainTextView = (TextView) view.findViewById(R.id.phoneMainTextView);
            phoneNameTextView = (TextView) view.findViewById(R.id.phoneNameTextView);
            phoneClassTextView = (TextView) view.findViewById(R.id.phoneClassTextView);
            phoneAddressTextView = (TextView) view.findViewById(R.id.phoneAddressTextView);
            phoneTypeTextView = (TextView) view.findViewById(R.id.phoneTypeTextView);

            //动态
            circleLine8View = view.findViewById(R.id.circleLine8View);
            circleTitleTextView = (TextView) view.findViewById(R.id.circleTitleTextView);
            circleEditText = (EditText) view.findViewById(R.id.circleEditText);
            circleLineView = view.findViewById(R.id.circleLineView);
            circleRelativeLayout = (RelativeLayout) view.findViewById(R.id.circleRelativeLayout);
            circleMainRelativeLayout = (RelativeLayout) view.findViewById(R.id.circleMainRelativeLayout);
            circleAvatarImageView = (ImageView) view.findViewById(R.id.circleAvatarImageView);
            circleNicknameTextView = (TextView) view.findViewById(R.id.circleNicknameTextView);
            circleGenderImageView = (ImageView) view.findViewById(R.id.circleGenderImageView);
            circleDeviceTextView = (TextView) view.findViewById(R.id.circleDeviceTextView);
            circleTimeTextView = (TextView) view.findViewById(R.id.circleTimeTextView);
            circleContentTextView = (TextView) view.findViewById(R.id.circleContentTextView);
            circleOneLinearLayout = (LinearLayout) view.findViewById(R.id.circleOneLinearLayout);
            circleTwoLinearLayout = (LinearLayout) view.findViewById(R.id.circleTwoLinearLayout);
            circleThrLinearLayout = (LinearLayout) view.findViewById(R.id.circleThrLinearLayout);
            circleImageView = new ImageView[9];
            circleImageView[0] = (ImageView) view.findViewById(R.id.circleOneImageView);
            circleImageView[1] = (ImageView) view.findViewById(R.id.circleTwoImageView);
            circleImageView[2] = (ImageView) view.findViewById(R.id.circleThrImageView);
            circleImageView[3] = (ImageView) view.findViewById(R.id.circleFouImageView);
            circleImageView[4] = (ImageView) view.findViewById(R.id.circleFivImageView);
            circleImageView[5] = (ImageView) view.findViewById(R.id.circleSixImageView);
            circleImageView[6] = (ImageView) view.findViewById(R.id.circleSevImageView);
            circleImageView[7] = (ImageView) view.findViewById(R.id.circleEigImageView);
            circleImageView[8] = (ImageView) view.findViewById(R.id.circleNigImageView);
            circleLocationTextView = (TextView) view.findViewById(R.id.circleLocationTextView);
            circleForwardTextView = (CenterTextView) view.findViewById(R.id.circleForwardTextView);
            circleCommentTextView = (CenterTextView) view.findViewById(R.id.circleCommentTextView);
            circlePraiseTextView = (CenterTextView) view.findViewById(R.id.circlePraiseTextView);

            //话题
            topicLine8View = view.findViewById(R.id.topicLine8View);
            topicTitleTextView = (TextView) view.findViewById(R.id.topicTitleTextView);
            topicEditText = (EditText) view.findViewById(R.id.topicEditText);
            topicLineView = view.findViewById(R.id.topicLineView);
            topicRelativeLayout = (RelativeLayout) view.findViewById(R.id.topicRelativeLayout);
            topicMainRelativeLayout = (RelativeLayout) view.findViewById(R.id.topicMainRelativeLayout);
            topicAvatarImageView = (ImageView) view.findViewById(R.id.topicAvatarImageView);
            topicNameTextView = (TextView) view.findViewById(R.id.topicNameTextView);
            topicContentTextView = (TextView) view.findViewById(R.id.topicContentTextView);
            topicInfoTextView = (TextView) view.findViewById(R.id.topicInfoTextView);
            topicCommentTextView = (CenterTextView) view.findViewById(R.id.topicCommentTextView);
            topicPraiseTextView = (CenterTextView) view.findViewById(R.id.topicPraiseTextView);

            //活动
            activityLine8View = view.findViewById(R.id.activityLine8View);
            activityTitleTextView = (TextView) view.findViewById(R.id.activityTitleTextView);
            activityLineView = view.findViewById(R.id.activityLineView);
            activityRelativeLayout = (RelativeLayout) view.findViewById(R.id.activityRelativeLayout);
            activityMainRelativeLayout = (RelativeLayout) view.findViewById(R.id.activityMainRelativeLayout);
            activityMainImageView = (ImageView) view.findViewById(R.id.activityMainImageView);
            activityNameTextView = (TextView) view.findViewById(R.id.activityNameTextView);

        }

    }

}