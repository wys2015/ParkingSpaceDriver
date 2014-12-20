package com.zhcs.ui.adapter;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.zhcs.driverBean.SpaceInfoBean;
import com.zhcs.parklist.GeoDistance;
import com.zhcs.parklist.LocationService;
import com.zhcs.regAndLogin.R;

public class CarInfoAdapter extends BaseAdapter {

	private List<SpaceInfoBean> mSpaceInfoList;
	private LayoutInflater mInflater;
	public static double lat = 0.0;
	public static double lng = 0.0;

	public CarInfoAdapter(Context context, List<SpaceInfoBean> spaceInfoBeans) {
		mInflater = LayoutInflater.from(context);
		mSpaceInfoList = spaceInfoBeans;
		Log.e("mSpaceInfoList", mSpaceInfoList.toString());
	}

	private Comparator<SpaceInfoBean> mDistanceComp = new Comparator<SpaceInfoBean>() {
		@Override
		public int compare(SpaceInfoBean lhs, SpaceInfoBean rhs) {
			return 0;
		}
	};

	private Comparator<SpaceInfoBean> mDateComp = new Comparator<SpaceInfoBean>() {
		@Override
		public int compare(SpaceInfoBean lhs, SpaceInfoBean rhs) {
			return lhs.getStart().compareTo(rhs.getStart());
		}
	};

	private Comparator<SpaceInfoBean> mPriceComp = new Comparator<SpaceInfoBean>() {
		@Override
		public int compare(SpaceInfoBean lhs, SpaceInfoBean rhs) {
			return lhs.getPrice() - rhs.getPrice();
		}
	};

	public static enum Order {
		Date, Price, Distance
	}

	public void switchOrder(Order mOrder) {
		Comparator<SpaceInfoBean> comparator = null;
		switch (mOrder) {
		case Date:
			comparator = mDateComp;
			break;
		case Price:
			comparator = mPriceComp;
			break;
		case Distance:
			comparator = mDistanceComp;
			break;
		default:
			break;
		}
		Collections.sort(mSpaceInfoList, comparator);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mSpaceInfoList.size();
	}

	@Override
	public SpaceInfoBean getItem(int position) {
		return mSpaceInfoList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.listitem_carinfo, parent,
					false);
		}
		TextView mCarDate = (TextView) convertView.findViewById(R.id.car_date);
		TextView mCarPrice = (TextView) convertView
				.findViewById(R.id.car_price);
		TextView mCarDistance = (TextView) convertView
				.findViewById(R.id.car_distance);
		Button mSubscripe = (Button) convertView.findViewById(R.id.subscribe);

		mCarDate.setText(getDate(position));
		mCarPrice.setText(getItem(position).getPrice() + "Ԫ/Сʱ");
		mCarDistance.setText(getDistance(position));
		mSubscripe.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
		return convertView;
	}

	private CharSequence getDate(int position) {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm");  
		StringBuilder sb = new StringBuilder();
		sb.append(format.format(getItem(position).getStart()));
		sb.append("--");
		sb.append(format.format(getItem(position).getEnd()));
		return sb.toString();
	}

	private CharSequence getDistance(int position) {
		lat = LocationService.getLatitude();
		lng = LocationService.getLongitude();
		double lat_park = ((double)getItem(position).getLat()) / 1000000;
		double lng_park = ((double)getItem(position).getLng()) / 1000000;
		Log.e("la1,ln1,la2,ln2", " "+lat+" "+lng+" "+lat_park+" "+lng_park);
		double distance = GeoDistance.computeCompareDistance(lat, lng, lat_park, lng_park);
		return String.valueOf(distance) + "m";
	}

}
