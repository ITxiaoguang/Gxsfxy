package top.yokey.gxsfxy.adapter;

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

public class FeedbackListAdapter extends RecyclerView.Adapter<FeedbackListAdapter.ViewHolder> {

    private MyApplication mApplication;
    private ArrayList<HashMap<String, String>> mArrayList;

    public FeedbackListAdapter(MyApplication application, ArrayList<HashMap<String, String>> arrayList) {
        this.mArrayList = arrayList;
        this.mApplication = application;
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        HashMap<String, String> hashMap = mArrayList.get(position);

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
            holder.friendAvatarImageView.setImageResource(R.mipmap.ic_launcher);
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
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup group, int viewType) {
        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.item_list_chat_only, group, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

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

}