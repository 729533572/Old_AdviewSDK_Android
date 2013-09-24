package com.kyview.adapters;

import android.app.Activity;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.kyview.AdViewAdRegistry;
import com.kyview.AdViewLayout;
import com.kyview.obj.Ration;
import com.kyview.util.AdViewUtil;
import com.mobisage.android.IMobiSageAdViewListener;
import com.mobisage.android.MobiSageAdBanner;
import com.mobisage.android.MobiSageAnimeType;
import com.mobisage.android.MobiSageEnviroment;

//import com.kyview.AdViewLayout.ViewAdRunnable;

public class MobiSageAdapter extends AdViewAdapter implements
		IMobiSageAdViewListener {
	private MobiSageAdBanner adv;

	private static int networkType() {
		return AdViewUtil.NETWORK_TYPE_ADSAGE;
	}

	public static void load(AdViewAdRegistry registry) {
		try {
			if (Class.forName("com.mobisage.android.IMobiSageAdViewListener") != null) {
				registry.registerClass(networkType(), MobiSageAdapter.class);
			}
		} catch (ClassNotFoundException e) {
		}
	}

	public MobiSageAdapter() {

	}

	@Override
	public void initAdapter(AdViewLayout adViewLayout, Ration ration) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handle() {
		// TODO Auto-generated method stub

		AdViewUtil.logInfo("Into MobiSage");
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null) {
			return;
		}

		Activity activity = adViewLayout.activityReference.get();
		if (activity == null) {
			return;
		}
		adv = new MobiSageAdBanner(activity, ration.key, null, null);// MobiSageAdSize.Size_540X80
		adv.setAdRefreshInterval(MobiSageEnviroment.AdRefreshInterval.Ad_No_Refresh);// Ad_Refresh_15//Ad_No_Refresh
		adv.setAnimeType(MobiSageAnimeType.Anime_LeftToRight);
		adv.setMobiSageAdViewListener(this);
		adViewLayout.activeRation = adViewLayout.nextRation;
		adViewLayout.removeAllViews();
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		adViewLayout.addView(adv, layoutParams);
		adViewLayout.addCloseButton(adViewLayout);
	}

	public void onMobiSageAdViewShow(Object adView) {
		AdViewUtil.logInfo("onMobiSageAdViewShow");
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null) {
			return;
		}
		super.onSuccessed(adViewLayout, ration);
		adv.setMobiSageAdViewListener(null);
		adViewLayout.adViewManager.resetRollover();
		adViewLayout.rotateThreadedDelayed();
		adViewLayout.reportImpression();
	}

	public void onMobiSageAdViewUpdate(Object adView) {
		AdViewUtil.logInfo("onMobiSageAdViewUpdate");
	}

	public void onMobiSageAdViewHide(Object adView) {
		AdViewUtil.logInfo("onMobiSageAdViewHide");
	}

	public void onMobiSageAdViewError(Object adView) {
		AdViewUtil.logInfo("onMobiSageAdViewError");

		adv.setMobiSageAdViewListener(null);

		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null)
			return;
		super.onFailed(adViewLayout, ration);
		//adViewLayout.rotateThreadedPri(1);
	}

	@Override
	public void onMobiSageAdViewClick(Object arg0) {
		AdViewUtil.logInfo("onMobiSageAdViewClick");
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null) {
			return;
		}
		adViewLayout.reportClick();
	}

	@Override
	public void onMobiSageAdViewClose(Object arg0) {
		
	}

	@Override
	public void clean() {
		// TODO Auto-generated method stub
		super.clean();
		if (adv != null) {
			adv.destoryAdView();
			adv = null;
		}
	}

}
