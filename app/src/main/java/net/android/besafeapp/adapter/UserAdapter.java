package net.android.besafeapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import net.android.besafeapp.R;
import net.android.besafeapp.db.Information;

import java.util.List;

public class UserAdapter extends BaseQuickAdapter<Information, BaseViewHolder> {
    private Context context;

    public UserAdapter(Context context, List<Information> inviteList) {
        super(R.layout.item_info, inviteList);
        this.context = context;
    }

    //主要布局调整都在重写的这个方法里
    @Override
    protected void convert(BaseViewHolder viewHolder, Information bean) {
        TextView tvName =   viewHolder.getView(R.id.tv_name);
        TextView tvCategory =  viewHolder.getView(R.id.tv_category);
        TextView tvAddress =  viewHolder.getView(R.id.tv_address);
        TextView tvTime =  viewHolder.getView(R.id.tv_time);
        tvName.setText(bean.getName());
        tvCategory.setText(bean.getCategory());
        tvAddress.setText(bean.getAddress());
        tvTime.setText(bean.getTime());
    }
}