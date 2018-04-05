package com.administrator.learndemo.map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.administrator.learndemo.R;
import com.amap.api.navi.model.AMapNaviPath;

import java.util.List;

/**
 * Created by 68848 on 2017/12/22.
 */

public class InnerMapRouteAdapter extends BaseAdapter {
    private static final String ROUTE_TITLE="方案";
    private static final String ROURE_MILE="公里";
    private static final String ROUTE_LIGHT = "个红绿灯";
    private Context mContext;
    private List<AMapNaviPath> paths;

    public InnerMapRouteAdapter(Context context, List<AMapNaviPath>paths){
        this.paths=paths;
        this.mContext=context;
    }
    public void refreshData(List<AMapNaviPath> paths){
        this.paths=paths;
    }
    @Override
    public int getCount() {
        return paths.size();
    }

    @Override
    public Object getItem(int position) {
        return paths.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        AMapNaviPath pDrivePath = paths.get(position);
        ViewHolder holder =null;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_inneermap_route,null);
            holder=new ViewHolder();
            holder.titleTextView = (TextView) convertView.findViewById(R.id.innermap_route_id);
            holder.timeTextView = (TextView) convertView.findViewById(R.id.innermap_route_time);
            holder.mileTextView = (TextView) convertView.findViewById(R.id.innermap_route_mile);
            holder.lightTextView = (TextView) convertView.findViewById(R.id.innermap_route_light);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.titleTextView.setText(ROUTE_TITLE + (position + 1));
        holder.timeTextView.setText(AMapUtil.getFriendlyTime((int) pDrivePath.getAllTime()) + "");//返回当前导航路线的总时间。
        holder.mileTextView.setText(AMapUtil.getFriendlyLength((int) pDrivePath.getAllLength()));//返回当前导航路线的总长度。
        holder.lightTextView.setText(pDrivePath.getAllCameras().size() + ROUTE_LIGHT);//获取全路段的所有摄像头
        convertView.setTag(holder);
        return convertView;
    }
    class ViewHolder {
        TextView titleTextView;
        TextView timeTextView;
        TextView mileTextView;
        TextView lightTextView;

    }
}
