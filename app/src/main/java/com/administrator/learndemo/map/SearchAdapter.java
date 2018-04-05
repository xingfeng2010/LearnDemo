package com.administrator.learndemo.map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.administrator.learndemo.R;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Administrator on 2018/1/5.
 */

public class SearchAdapter extends BaseAdapter {
    private Context mContext;
    private List<SearchPoi> mSearchPoiList;

    public SearchAdapter(List<SearchPoi> list, Context context) {
        mContext = context;
        mSearchPoiList = list;
    }

    @Override
    public int getCount() {
        return mSearchPoiList.size();
    }

    @Override
    public Object getItem(int position) {
        return mSearchPoiList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.search_item_layout, null);
            viewHolder.addressName = (TextView) convertView.findViewById(R.id.addressName);
            viewHolder.addressDistrict = (TextView) convertView.findViewById(R.id.addressDistrict);
            viewHolder.distance = (TextView) convertView.findViewById(R.id.distance);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.itemClick(position);
            }
        });

        viewHolder.addressName.setText(mSearchPoiList.get(position).getAddrname());
        viewHolder.addressDistrict.setText(mSearchPoiList.get(position).getDistrict());
        viewHolder.distance.setText(getDistanceStr(mSearchPoiList.get(position).getDistance()));
        return convertView;
    }

    static class ViewHolder {
        public TextView addressName;
        public TextView addressDistrict;
        public TextView distance;
    }

    public static String getDistanceStr(double distance) {
        if(distance > 1000) {
            float distanceValue = Math.round((distance / 100f)) / 10f;
            DecimalFormat decimalFormat = new DecimalFormat("#0.0");//构造方法的字符格式这里如果小数不足2位,会以0补足.
            String distanceString = decimalFormat.format(distanceValue);//format 返回的是字符串
            return distanceString + "公里";
        }
        else {
            long distanceValue = Math.round(distance);
            return distanceValue + "米";
        }
    }

    ClickListener mClickListener;

    public void setClickListener(ClickListener mClickListener) {
        this.mClickListener = mClickListener;
    }

    public interface ClickListener{
        void itemClick(int position);
    }
}
