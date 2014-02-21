package com.kyview.adapters;

import android.app.Activity;

import com.kyview.AdViewAdRegistry;
import com.kyview.AdViewLayout;
import com.kyview.AdViewLayout.ViewAdRunnable;
import com.kyview.obj.Ration;
import com.kyview.util.AdViewUtil;
import com.mobisage.android.MobiSageAdBanner;
import com.mobisage.android.MobiSageAdBannerListener;
import com.mobisage.android.MobiSageAnimeType;
import com.mobisage.android.MobiSageEnviroment;
import com.mobisage.android.MobiSageManager;

//import com.kyview.AdViewLayout.ViewAdRunnable;

public class MobiSageAdapter extends AdViewAdapter implements
		MobiSageAdBannerListener {
	private MobiSageAdBanner adv;

	private static int networkType() {
		return AdViewUtil.NETWORK_TYPE_ADSAGE;
	}

	public static void load(AdViewAdRegistry registry) {
		try {
			if (Class.forName("com.mobisage.android.MobiSageAdBannerListener") != null) {
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
		MobiSageManager.getInstance().setPublisherID(ration.key);
		adv = new MobiSageAdBanner(activity);// MobiSageAdSize.Size_540X80

		adv.setAdRefreshInterval(MobiSageEnviroment.AdRefreshInterval.Ad_No_Refresh);// Ad_Refresh_15//Ad_No_Refresh
		adv.setAnimeType(MobiSageAnimeType.Anime_LeftToRight);
		adv.setMobiSageAdBannerListener(this);
		// adViewLayout.activeRation = adViewLayout.nextRation;
		// adViewLayout.removeAllViews();
		// RelativeLayout.LayoutParams layoutParams = new
		// RelativeLayout.LayoutParams(
		// LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		// layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		// adViewLayout.addView(adv, layoutParams);
		// adViewLayout.addCloseButton(adViewLayout);
	}

	public void onMobiSageAdViewShow(Object adView) {

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

	@Override
	public void onMobiSageBannerClick(MobiSageAdBanner arg0) {
		// TODO Auto-generated method stub
		AdViewUtil.logInfo("onMobiSageAdViewClick");
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null) {
			return;
		}
		adViewLayout.reportClick();
	}

	@Override
	public void onMobiSageBannerClose(MobiSageAdBanner arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMobiSageBannerError(MobiSageAdBanner arg0) {
		// TODO Auto-generated method stub
		AdViewUtil.logInfo("onMobiSageAdViewError");

		adv.setMobiSageAdBannerListener(null);

		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null)
			return;
		super.onFailed(adViewLayout, ration);
	}

	@Override
	public void onMobiSageBannerHide(MobiSageAdBanner arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMobiSageBannerHideWindow(MobiSageAdBanner arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMobiSageBannerPopupWindow(MobiSageAdBanner arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMobiSageBannerShow(MobiSageAdBanner arg0) {
		// TODO Auto-generated method stub
		AdViewUtil.logInfo("onMobiSageAdViewShow");
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null) {
			return;
		}
		super.onSuccessed(adViewLayout, ration);

		arg0.setMobiSageAdBannerListener(null);
		adViewLayout.adViewManager.resetRollover();
		adViewLayout.handler.post(new ViewAdRunnable(adViewLayout, arg0));
		adViewLayout.rotateThreadedDelayed();

		// adViewLayout.adViewManager.resetRollover();
		// adViewLayout.rotateThreadedDelayed();
		// adViewLayout.reportImpression();
	}

}
