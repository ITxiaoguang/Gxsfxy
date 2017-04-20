package top.yokey.gxsfxy.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import top.yokey.gxsfxy.utility.TextUtil;
import top.yokey.gxsfxy.utility.TimeUtil;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.ViewHolder> {

    private Activity mActivity;
    private MyApplication mApplication;
    private onItemLongClickListener itemLongClickListener;
    private ArrayList<HashMap<String, String>> mArrayList;

    public MessageListAdapter(MyApplication application, Activity activity, ArrayList<HashMap<String, String>> arrayList) {
        this.mActivity = activity;
        this.mArrayList = arrayList;
        this.mApplication = application;
        this.itemLongClickListener = null;
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final HashMap<String, String> hashMap = mArrayList.get(position);

        if (TextUtil.isEmpty(hashMap.get("user_avatar"))) {
            holder.avatarImageView.setImageResource(R.mipmap.ic_avatar);
        } else {
            ImageLoader.getInstance().displayImage(hashMap.get("user_avatar"), holder.avatarImageView);
        }

        holder.nicknameTextView.setText(hashMap.get("nick_name"));
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

        int number = mApplication.mSharedPreferences.getInt("message_list_number_" + hashMap.get("user_id"), 0);

        if (number == 0) {
            holder.numberTextView.setVisibility(View.GONE);
            holder.numberTextView.setText("0");
        } else {
            String temp;
            holder.numberTextView.setVisibility(View.VISIBLE);
            if (number > 99) {
                temp = "99";
            } else {
                temp = number + "";
            }
            holder.numberTextView.setText(temp);
        }

        holder.mRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mApplication.mSharedPreferencesEditor.putInt("message_list_number_" + hashMap.get("user_id"), 0).apply();
                mApplication.startActivityChatOnly(mActivity, hashMap.get("user_id"));
                notifyDataSetChanged();
            }
        });

        holder.mRelativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(mActivity)
                        .setTitle("确认您的选择")
                        .setMessage("删除这个聊天")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mApplication.mSharedPreferencesEditor.putBoolean("message_list_remove_" + hashMap.get("user_id"), true).apply();
                                mArrayList.remove(holder.getAdapterPosition());
                                notifyDataSetChanged();
                                if (itemLongClickListener != null) {
                                    itemLongClickListener.onItemLongClick();
                                }
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                return false;
            }
        });

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup group, int viewType) {
        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.item_list_message, group, false);
        return new ViewHolder(view);
    }

    public void setOnItemLongClickListener(onItemLongClickListener listener) {
        this.itemLongClickListener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout mRelativeLayout;
        private ImageView avatarImageView;
        private TextView nicknameTextView;
        private TextView timeTextView;
        private TextView infoTextView;
        private TextView numberTextView;

        private ViewHolder(View view) {
            super(view);

            mRelativeLayout = (RelativeLayout) view.findViewById(R.id.mainRelativeLayout);
            avatarImageView = (ImageView) view.findViewById(R.id.avatarImageView);
            nicknameTextView = (TextView) view.findViewById(R.id.nicknameTextView);
            timeTextView = (TextView) view.findViewById(R.id.timeTextView);
            infoTextView = (TextView) view.findViewById(R.id.infoTextView);
            numberTextView = (TextView) view.findViewById(R.id.numberTextView);

        }

    }

    public interface onItemLongClickListener {
        void onItemLongClick();
    }

}