package top.yokey.gxsfxy.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import top.yokey.gxsfxy.R;
import top.yokey.gxsfxy.activity.MyApplication;
import top.yokey.gxsfxy.utility.TextUtil;

public class PhoneListAdapter extends RecyclerView.Adapter<PhoneListAdapter.ViewHolder> {

    private Activity mActivity;
    private MyApplication mApplication;
    private ArrayList<HashMap<String, String>> mArrayList;

    public PhoneListAdapter(MyApplication application, Activity activity, ArrayList<HashMap<String, String>> arrayList) {
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
        String title = hashMap.get("phone_name").substring(0, 1);

        if (!TextUtil.isEmpty(keyword)) {
            hashMap.put("phone_name", TextUtil.replaceKeyword(hashMap.get("phone_name"), keyword));
            hashMap.put("phone_class", TextUtil.replaceKeyword(hashMap.get("phone_class"), keyword));
            hashMap.put("phone_type", TextUtil.replaceKeyword(hashMap.get("phone_type"), keyword));
            hashMap.put("phone_address", TextUtil.replaceKeyword(hashMap.get("phone_address"), keyword));
        }

        holder.mTextView.setText(title);
        holder.nameTextView.setText(Html.fromHtml(hashMap.get("phone_name")));
        holder.classTextView.setText(Html.fromHtml(hashMap.get("phone_class")));
        holder.typeTextView.setText(Html.fromHtml(hashMap.get("phone_type")));
        holder.addressTextView.setText(Html.fromHtml(hashMap.get("phone_address")));

        holder.mRelativeLayout.setOnClickListener(new View.OnClickListener() {
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

        holder.mRelativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mApplication.startCall(mActivity, hashMap.get("phone_number"));
                return false;
            }
        });

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup group, int viewType) {
        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.item_list_phone, group, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout mRelativeLayout;
        private TextView mTextView;
        private TextView nameTextView;
        private TextView classTextView;
        private TextView addressTextView;
        private TextView typeTextView;

        private ViewHolder(View view) {
            super(view);

            mRelativeLayout = (RelativeLayout) view.findViewById(R.id.mainRelativeLayout);
            mTextView = (TextView) view.findViewById(R.id.mainTextView);
            nameTextView = (TextView) view.findViewById(R.id.nameTextView);
            classTextView = (TextView) view.findViewById(R.id.classTextView);
            addressTextView = (TextView) view.findViewById(R.id.addressTextView);
            typeTextView = (TextView) view.findViewById(R.id.typeTextView);

        }

    }

}