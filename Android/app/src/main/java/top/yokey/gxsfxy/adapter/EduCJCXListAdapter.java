package top.yokey.gxsfxy.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import top.yokey.gxsfxy.R;

public class EduCJCXListAdapter extends RecyclerView.Adapter<EduCJCXListAdapter.ViewHolder> {

    private Activity mActivity;
    private ArrayList<HashMap<String, String>> mArrayList;

    public EduCJCXListAdapter(Activity activity, ArrayList<HashMap<String, String>> arrayList) {
        this.mActivity = activity;
        this.mArrayList = arrayList;
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final HashMap<String, String> hashMap = mArrayList.get(position);

        holder.nameTextView.setText(hashMap.get("xh"));
        holder.nameTextView.append(".");
        holder.nameTextView.append(hashMap.get("kcmc"));
        holder.valueTextView.setText(hashMap.get("cj"));
        holder.valueTextView.append("分");

        holder.mRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = "序号：" + hashMap.get("xh");
                content += "\n学期：" + hashMap.get("kkxq");
                content += "\n编号：" + hashMap.get("kcbh");
                content += "\n名称：" + hashMap.get("kcmc");
                content += "\n成绩：" + hashMap.get("cj");
                content += "\n学分：" + hashMap.get("xf");
                content += "\n学时：" + hashMap.get("zxs");
                content += "\n考核：" + hashMap.get("khfs");
                content += "\n属性：" + hashMap.get("kcsx");
                content += "\n性质：" + hashMap.get("kcxz");
                new AlertDialog.Builder(mActivity).setTitle("成绩信息").setMessage(content).setPositiveButton("返回", null).show();
            }
        });

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup group, int viewType) {
        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.item_list_edu_cjcx, group, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout mRelativeLayout;
        private TextView nameTextView;
        private TextView valueTextView;

        private ViewHolder(View view) {
            super(view);

            mRelativeLayout = (RelativeLayout) view.findViewById(R.id.mainRelativeLayout);
            nameTextView = (TextView) view.findViewById(R.id.nameTextView);
            valueTextView = (TextView) view.findViewById(R.id.valueTextView);

        }

    }

}