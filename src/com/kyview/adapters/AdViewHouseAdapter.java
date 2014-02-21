package com.kyview.adapters;

import android.graphics.Color;

import com.kuaiyou.KyAdBaseView;
import com.kuaiyou.interfaces.OnAdListener;
import com.kuaiyou.kyview.KyAdView;
import com.kyview.AdViewAdRegistry;
import com.kyview.AdViewLayout;
import com.kyview.AdViewLayout.ViewAdRunnable;
import com.kyview.AdViewTargeting;
import com.kyview.AdViewTargeting.RunMode;
import com.kyview.obj.Extra;
import com.kyview.obj.Ration;
import com.kyview.util.AdViewUtil;

public class AdViewHouseAdapter extends AdViewAdapter implements OnAdListener {
	private static int networkType() {
		return AdViewUtil.NETWORK_TYPE_ADVIEWAD;
	}

	public static void load(AdViewAdRegistry registry) {
		try {
			if (Class.forName("com.kuaiyou.kyview.KyAdView") != null) {
				registry.registerClass(networkType(), AdViewHouseAdapter.class);
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public AdViewHouseAdapter() {
	}

	@Override
	public void initAdapter(AdViewLayout adViewLayout, Ration ration) {
	}

	@Override
	public void handle() {
		AdViewUtil.logInfo("Into AdViewHouse");
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null) {
			return;
		}
		// public KyAdView(Context context,String appId,String address,int
		// backGroundColor,int textColor,String logo) {

		Extra extra = adViewLayout.extra;
		KyAdView kyAdView = null;
		int backGroundColor = Color.rgb(extra.bgRed, extra.bgGreen,
				extra.bgBlue);
		int textColorr = Color.rgb(extra.fgRed, extra.fgGreen, extra.fgBlue);
		kyAdView = new KyAdView(adViewLayout.getContext(),
				adViewLayout.keyAdView, ration.key2, ration.type,
				backGroundColor, textColorr, ration.logo,
				(AdViewTargeting.getRunMode() == RunMode.TEST));
		kyAdView.setBannerAdListener(this);
		kyAdView.setHorizontalScrollBarEnabled(false);
		kyAdView.setVerticalScrollBarEnabled(false);

	}

	@Override
	public void onConnectFailed(KyAdBaseView view, String msg) {
		AdViewUtil.logInfo("AdViewHouse failure");
		view.setBannerAdListener(null);

		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null) {
			return;
		}
		super.onFailed(adViewLayout, ration);
	}

	@Override
	public void onReceivedAd(KyAdBaseView view) {
		AdViewUtil.logInfo("AdViewHouse success");
		view.setBannerAdListener(null);

		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null) {
			return;
		}
		view.setAnimRotated();
		view.startLayoutAnimation();
		super.onSuccessed(adViewLayout, ration);
		adViewLayout.adViewManager.resetRollover();
		adViewLayout.handler.post(new ViewAdRunnable(adViewLayout, view));
		adViewLayout.rotateThreadedDelayed();

	}

	@Override
	public void onAdClicked(KyAdBaseView view) {
		AdViewUtil.logInfo("AdViewHouse clicked");
	}

	@Override
	public void onAdClose(KyAdBaseView view) {
		// TODO Auto-generated method stub
		
	}

}