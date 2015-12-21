package com.sino.hzb.play;

import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sino.hzb.play.util.ImageRender;


public class PopupWindowAdapter extends BaseAdapter {

	private Context mContext;
	private List<Map<Object,Object>> tjqs;
	private int size = 0;
	private int selectid;

	public PopupWindowAdapter(Context mContext, List<Map<Object,Object>> tjqs) {
		super();
		this.mContext = mContext;
		this.tjqs = tjqs;
		size = tjqs.size();
		selectid = 0;
	}

	@Override
	public int getCount() {
		return size;
	}

	@Override
	public Object getItem(int position) {
		return tjqs.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.item_everyday_video_popuwindow, null);

			holder = new ViewHolder();
			holder.iView = (ImageView) convertView.findViewById(R.id.iv_avd);
			holder.tv_no = (TextView) convertView.findViewById(R.id.tv_no);
			holder.tv_business_name = (TextView) convertView.findViewById(R.id.tv_business_name);
			holder.tv_coupon_name = (TextView) convertView.findViewById(R.id.tv_coupon_name);
			holder.rl_coupon_is_time = (RelativeLayout) convertView.findViewById(R.id.rl_coupon_is_time);
			holder.rl_avd = (RelativeLayout) convertView.findViewById(R.id.rl_avd);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Map<Object,Object> couponVideo = tjqs.get(position);
		ImageRender.getInstance().setImage(holder.iView, couponVideo.get("img").toString(), R.mipmap.ic_launcher);

		String str = (position + 1) + "";
		if (position < 10) {
			str = "0" + str;
		}
		holder.tv_no.setText(str.toString());
		holder.tv_business_name.setText(couponVideo.get("title").toString());
		holder.tv_coupon_name.setText(couponVideo.get("name").toString());

		holder.rl_avd.setBackground(position == selectid ? mContext.getResources().getDrawable(R.drawable.fanxq_corners_no_bg_stroke_app_color) : null);

		holder.rl_coupon_is_time.setVisibility(couponVideo.get("isTime").toString().equals("1")? View.GONE : View.VISIBLE);
		return convertView;
	}

	class ViewHolder {
		ImageView iView;
		TextView tv_no, tv_business_name, tv_coupon_name;
		RelativeLayout rl_coupon_is_time, rl_avd;
	}

}
