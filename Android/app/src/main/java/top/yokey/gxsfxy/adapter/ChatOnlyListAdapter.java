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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import top.yokey.gxsfxy.R;
import top.yokey.gxsfxy.activity.MyApplication;
import top.yokey.gxsfxy.system.UserAjaxParams;
import top.yokey.gxsfxy.utility.TextUtil;

public class ChatOnlyListAdapter extends RecyclerView.Adapter<ChatOnlyListAdapter.ViewHolder> {

    private Activity mActivity;
    private MyApplication mApplication;
    private onItemClickListener onItemClickListener;
    private ArrayList<HashMap<String, String>> mArrayList;

    public ChatOnlyListAdapter(MyApplication application, Activity activity, ArrayList<HashMap<String, String>> arrayList) {
        this.mActivity = activity;
        this.mArrayList = arrayList;
        this.mApplication = application;
        this.onItemClickListener = null;
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final HashMap<String, String> hashMap = mArrayList.get(position);

        holder.friendAvatarImageView.setVisibility(View.INVISIBLE);
        holder.friendContentRelativeLayout.setVisibility(View.GONE);
        holder.friendContentTextView.setVisibility(View.GONE);
        holder.friendContentImageView.setVisibility(View.GONE);
        holder.friendLocationRelativeLayout.setVisibility(View.GONE);

        holder.myAvatarImageView.setVisibility(View.INVISIBLE);
        holder.myContentRelativeLayout.setVisibility(View.GONE);
        holder.myContentTextView.setVisibility(View.GONE);
        holder.myContentImageView.setVisibility(View.GONE);
        holder.myLocationRelativeLayout.setVisibility(View.GONE);

        if (hashMap.get("message_uid").equals(mApplication.userHashMap.get("user_id"))) {
            //是我发出的
            holder.myAvatarImageView.setVisibility(View.VISIBLE);
            if (TextUtil.isEmpty(mApplication.userHashMap.get("user_avatar"))) {
                holder.myAvatarImageView.setImageResource(R.mipmap.ic_avatar);
            } else {
                ImageLoader.getInstance().displayImage(mApplication.userHashMap.get("user_avatar"), holder.myAvatarImageView);
            }
            switch (hashMap.get("message_type")) {
                case "text":
                    holder.myContentRelativeLayout.setVisibility(View.VISIBLE);
                    holder.myContentTextView.setVisibility(View.VISIBLE);
                    holder.myContentTextView.setText(Html.fromHtml(hashMap.get("message_content"), mApplication.mImageGetter, null));
                    break;
                case "image":
                    holder.myContentRelativeLayout.setVisibility(View.VISIBLE);
                    holder.myContentImageView.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(hashMap.get("message_content"), holder.myContentImageView);
                    break;
                case "location":
                    holder.myLocationRelativeLayout.setVisibility(View.VISIBLE);
                    String content = hashMap.get("message_content").replace("|", "<br><font color='#999999'>") + "</font>";
                    holder.myLocationTextView.setText(Html.fromHtml(content));
                    break;
            }
        } else {
            //是对方发出的
            holder.friendAvatarImageView.setVisibility(View.VISIBLE);
            try {
                JSONObject jsonObject = new JSONObject(hashMap.get("uid_info"));
                if (TextUtil.isEmpty(jsonObject.getString("user_avatar"))) {
                    holder.friendAvatarImageView.setImageResource(R.mipmap.ic_avatar);
                } else {
                    ImageLoader.getInstance().displayImage(jsonObject.getString("user_avatar"), holder.friendAvatarImageView);
                }
                switch (hashMap.get("message_type")) {
                    case "text":
                        holder.friendContentRelativeLayout.setVisibility(View.VISIBLE);
                        holder.friendContentTextView.setVisibility(View.VISIBLE);
                        holder.friendContentTextView.setText(Html.fromHtml(hashMap.get("message_content"), mApplication.mImageGetter, null));
                        break;
                    case "image":
                        holder.friendContentRelativeLayout.setVisibility(View.VISIBLE);
                        holder.friendContentImageView.setVisibility(View.VISIBLE);
                        ImageLoader.getInstance().displayImage(hashMap.get("message_content"), holder.friendContentImageView);
                        break;
                    case "location":
                        holder.friendLocationRelativeLayout.setVisibility(View.VISIBLE);
                        String content = hashMap.get("message_content").replace("|", "<br><font color='#999999'>") + "</font>";
                        holder.friendLocationTextView.setText(Html.fromHtml(content));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        holder.mRelativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (hashMap.get("message_uid").equals(mApplication.userHashMap.get("user_id"))) {
                    new AlertDialog.Builder(mActivity)
                            .setTitle("确认您的选择")
                            .setMessage("删除这条消息")
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    UserAjaxParams ajaxParams = new UserAjaxParams(mApplication, "userMessage", "messageDel");
                                    ajaxParams.put("message_id", hashMap.get("message_id"));
                                    mApplication.mFinalHttp.post(mApplication.apiUrlString + "c=userMessage&a=messageDel", ajaxParams, null);
                                    mArrayList.remove(holder.getAdapterPosition());
                                    notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton("取消", null)
                            .show();
                }
                return false;
            }
        });

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup group, int viewType) {
        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.item_list_chat_only, group, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout mRelativeLayout;

        private ImageView friendAvatarImageView;
        private RelativeLayout friendContentRelativeLayout;
        private TextView friendContentTextView;
        private ImageView friendContentImageView;
        private RelativeLayout friendLocationRelativeLayout;
        private TextView friendLocationTextView;

        private ImageView myAvatarImageView;
        private RelativeLayout myContentRelativeLayout;
        private TextView myContentTextView;
        private ImageView myContentImageView;
        private RelativeLayout myLocationRelativeLayout;
        private TextView myLocationTextView;

        private ViewHolder(View view) {
            super(view);

            mRelativeLayout = (RelativeLayout) view.findViewById(R.id.mainRelativeLayout);

            friendAvatarImageView = (ImageView) view.findViewById(R.id.friendAvatarImageView);
            friendContentRelativeLayout = (RelativeLayout) view.findViewById(R.id.friendContentRelativeLayout);
            friendContentTextView = (TextView) view.findViewById(R.id.friendContentTextView);
            friendContentImageView = (ImageView) view.findViewById(R.id.friendContentImageView);
            friendLocationRelativeLayout = (RelativeLayout) view.findViewById(R.id.friendLocationRelativeLayout);
            friendLocationTextView = (TextView) view.findViewById(R.id.friendLocationTextView);

            myAvatarImageView = (ImageView) view.findViewById(R.id.myAvatarImageView);
            myContentRelativeLayout = (RelativeLayout) view.findViewById(R.id.myContentRelativeLayout);
            myContentTextView = (TextView) view.findViewById(R.id.myContentTextView);
            myContentImageView = (ImageView) view.findViewById(R.id.myContentImageView);
            myLocationRelativeLayout = (RelativeLayout) view.findViewById(R.id.myLocationRelativeLayout);
            myLocationTextView = (TextView) view.findViewById(R.id.myLocationTextView);

        }

    }

    public void setOnItemClickListener(onItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public interface onItemClickListener {
        void onItemClick(String area_id, String area_name);
    }

}