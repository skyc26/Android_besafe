package net.android.besafeapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.utils.DistanceUtil;


import net.android.besafeapp.R;

import java.text.DecimalFormat;
import java.util.List;

public class SearchAddressAdapter extends BaseAdapter {
    private LatLng currentLatLng;
    private List<PoiInfo> oList;
    private Context ocontext;
    private LayoutInflater oInflater;

    public SearchAddressAdapter(List<PoiInfo> oList, Context context, LatLng currentLatLng) {
        this.oList = oList;
        this.ocontext = context;
        this.currentLatLng = currentLatLng;
        this.oInflater = LayoutInflater.from(ocontext);
    }

    @Override
    public int getCount() {
        return oList.size();
    }

    @Override
    public Object getItem(int position) {
        return oList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = oInflater.inflate(R.layout.item_search_address, null);
            viewHolder = new ViewHolder();
            //得到各个控件的对象
            viewHolder.iv_point = convertView.findViewById(R.id.iv_point);
            viewHolder.tv_name = convertView.findViewById(R.id.tv_name);
            viewHolder.tv_address = convertView.findViewById(R.id.tv_address);
            viewHolder.tv_distance = convertView.findViewById(R.id.tv_distance);
            //绑定viewHolder对象
            convertView.setTag(viewHolder);

        } else {
            //取出viewHolder对象
            viewHolder = (ViewHolder) convertView.getTag();
        }
        PoiInfo poiInfo = oList.get(position);
        LatLng latLng = poiInfo.getLocation();
        //用当前所在位置算出距离
        double distance= DistanceUtil.getDistance(currentLatLng, latLng);

        viewHolder.tv_name.setText(poiInfo.name);
        viewHolder.tv_address.setText(poiInfo.address);
        viewHolder.tv_distance.setText(formatDistance(distance));
        if (position == 0) {
            viewHolder.iv_point.setImageResource(R.drawable.point_orange);
            viewHolder.tv_name.setTextColor(ocontext.getResources().getColor(R.color.orange));
        }else{
            viewHolder.iv_point.setImageResource(R.drawable.point_gray);
            viewHolder.tv_name.setTextColor(ocontext.getResources().getColor(R.color.black));
        }
        return convertView;
    }

    /**
     * 存放控件
     */
    class ViewHolder {
        ImageView iv_point;
        TextView tv_name, tv_address, tv_distance;
    }


    private String formatDistance(double distance){
        String str;
        if(distance>=1000){
            DecimalFormat df = new DecimalFormat("#.00");
            double b = distance/1000;
            str=df.format(b)+"千米";
        }else{
            DecimalFormat df = new DecimalFormat("######0");
            str = df.format(distance)+"米";
        }
        return str;
    }

}
