package top.yokey.gxsfxy.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;
import top.yokey.gxsfxy.activity.LoadActivity;
import top.yokey.gxsfxy.activity.MainActivity;
import top.yokey.gxsfxy.activity.admin.AdminMainActivity;
import top.yokey.gxsfxy.activity.dynamic.CircleDetailedActivity;
import top.yokey.gxsfxy.activity.dynamic.TopicDetailedActivity;
import top.yokey.gxsfxy.activity.message.MessageActivity;
import top.yokey.gxsfxy.activity.mine.FollowMineActivity;
import top.yokey.gxsfxy.activity.message.ChatOnlyActivity;
import top.yokey.gxsfxy.system.UserAjaxParams;
import top.yokey.gxsfxy.utility.TextUtil;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent data) {

        String alert = data.getStringExtra(JPushInterface.EXTRA_ALERT);
        String extra = data.getStringExtra(JPushInterface.EXTRA_EXTRA);
        String message = data.getStringExtra(JPushInterface.EXTRA_MESSAGE);

        if (!TextUtil.isEmpty(message)) {
            if (message.equals("message")) {
                if (MainActivity.mActivity != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(extra);
                        if (jsonObject.getString("message_uid").equals(ChatOnlyActivity.user_id)) {
                            ChatOnlyActivity.mArrayList.add(new HashMap<>(TextUtil.jsonObjectToHashMap(extra)));
                            ChatOnlyActivity.mAdapter.notifyDataSetChanged();
                            if (ChatOnlyActivity.tipsTextView.getVisibility() == View.VISIBLE) {
                                ChatOnlyActivity.tipsTextView.setVisibility(View.GONE);
                            }
                            if (ChatOnlyActivity.bottomBoolean) {
                                ChatOnlyActivity.mListView.smoothScrollToPosition(ChatOnlyActivity.mArrayList.size());
                            } else {
                                String number = (Integer.parseInt(ChatOnlyActivity.numberTextView.getText().toString()) + 1) + "";
                                ChatOnlyActivity.numberTextView.setText(number);
                                if (ChatOnlyActivity.numberTextView.getVisibility() == View.GONE) {
                                    ChatOnlyActivity.numberTextView.startAnimation(MainActivity.mApplication.showAlphaAnimation);
                                    ChatOnlyActivity.numberTextView.setVisibility(View.VISIBLE);
                                }
                            }
                        } else {
                            if (!MessageActivity.inBoolean) {
                                if (MainActivity.mApplication.messageNotifyBoolean) {
                                    UserAjaxParams ajaxParams = new UserAjaxParams(MainActivity.mApplication, "userMessage", "notifyMine");
                                    ajaxParams.put("user_id", jsonObject.getString("message_uid"));
                                    MainActivity.mApplication.mNotificationManager.cancelAll();
                                    MainActivity.mApplication.mFinalHttp.post(MainActivity.mApplication.apiUrlString + "c=userMessage&a=notifyMine", ajaxParams, null);
                                }
                            }
                        }
                        //更新消息列表
                        JSONObject userJsonObject = new JSONObject(jsonObject.getString("uid_info"));
                        MainActivity.mApplication.mSharedPreferencesEditor.putBoolean("message_list_remove_" + userJsonObject.getString("user_id"), false).apply();
                        MessageActivity.updateMessageList(userJsonObject.getString("user_id"), userJsonObject.getString("nick_name"), userJsonObject.getString("user_avatar"),
                                jsonObject.getString("message_type"), jsonObject.getString("message_content"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return;
            } else {
                if (MainActivity.mActivity != null) {
                    if (!MainActivity.mApplication.messagePushCheckBoxBoolean) {
                        MainActivity.mApplication.mNotificationManager.cancelAll();
                    }
                }
            }
        } else {
            if (MainActivity.mActivity != null) {
                if (!MainActivity.mApplication.messagePushCheckBoxBoolean) {
                    MainActivity.mApplication.mNotificationManager.cancelAll();
                }
            }
        }

        if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(data.getAction())) {

            if (MainActivity.mActivity != null) {

                if (alert.contains("有人回复了你评论的圈子")) {
                    handlerCommentReplyCircle(extra);
                    return;
                }

                if (alert.contains("有人回复了你评论的话题")) {
                    handlerCommentReplyTopic(extra);
                    return;
                }

                if (alert.contains("有人评论了你发表的圈子")) {
                    handlerCommentCircle(extra);
                    return;
                }

                if (alert.contains("有人评论了你发表的话题")) {
                    handlerCommentTopic(extra);
                    return;
                }

                if (alert.contains("您有新的聊天消息")) {
                    handlerMessage(extra);
                    return;
                }

                if (alert.contains("有人关注你了")) {
                    handlerFollowMine();
                    return;
                }

                if (alert.contains("有新用户注册")) {
                    handlerNewsUser();
                }

            } else {

                handlerLoad(context);

            }

        }

    }

    private void handlerCommentReplyCircle(String extra) {

        try {
            JSONObject jsonObject = new JSONObject(extra);
            Intent intent = new Intent(MainActivity.mActivity, CircleDetailedActivity.class);
            intent.putExtra("id", jsonObject.getString("dynamic_id"));
            MainActivity.mApplication.startActivity(MainActivity.mActivity, intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void handlerCommentReplyTopic(String extra) {

        try {
            JSONObject jsonObject = new JSONObject(extra);
            Intent intent = new Intent(MainActivity.mActivity, TopicDetailedActivity.class);
            intent.putExtra("id", jsonObject.getString("dynamic_id"));
            MainActivity.mApplication.startActivity(MainActivity.mActivity, intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void handlerCommentCircle(String extra) {

        try {
            JSONObject jsonObject = new JSONObject(extra);
            Intent intent = new Intent(MainActivity.mActivity, CircleDetailedActivity.class);
            intent.putExtra("id", jsonObject.getString("dynamic_id"));
            MainActivity.mApplication.startActivity(MainActivity.mActivity, intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void handlerCommentTopic(String extra) {

        try {
            JSONObject jsonObject = new JSONObject(extra);
            Intent intent = new Intent(MainActivity.mActivity, TopicDetailedActivity.class);
            intent.putExtra("id", jsonObject.getString("dynamic_id"));
            MainActivity.mApplication.startActivity(MainActivity.mActivity, intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void handlerMessage(String extra) {

        try {
            JSONObject jsonObject = new JSONObject(extra);
            Intent intent = new Intent(MainActivity.mActivity, ChatOnlyActivity.class);
            intent.putExtra("user_id", jsonObject.getString("user_id"));
            if (ChatOnlyActivity.mActivity != null) {
                MainActivity.mApplication.finishActivity(ChatOnlyActivity.mActivity);
            }
            MainActivity.mApplication.startActivity(MainActivity.mActivity, intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void handlerLoad(Context context) {

        Intent intent = new Intent(context, LoadActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);

    }

    private void handlerFollowMine() {

        Intent intent = new Intent(MainActivity.mActivity, FollowMineActivity.class);
        intent.putExtra("user_id", MainActivity.mApplication.userHashMap.get("user_id"));
        MainActivity.mApplication.startActivity(MainActivity.mActivity, intent);

    }

    private void handlerNewsUser() {

        if (MainActivity.mApplication.userHashMap.isEmpty()) {
            return;
        }

        if (MainActivity.mApplication.userHashMap.get("user_power").equals("用户")) {
            return;
        }

        MainActivity.mApplication.startActivityWithLoginSuccess(MainActivity.mActivity,
                new Intent(MainActivity.mActivity, AdminMainActivity.class));

    }

}