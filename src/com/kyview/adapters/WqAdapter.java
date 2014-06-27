package com.kyview.adapters;

import android.app.Activity;
import android.widget.RelativeLayout;

import com.kyview.AdViewAdRegistry;
import com.kyview.AdViewLayout;
import com.kyview.AdViewLayout.ViewAdRunnable;
import com.kyview.obj.Ration;
import com.kyview.util.AdViewUtil;
import com.wqmobile.sdk.WQAdEventListener;
import com.wqmobile.sdk.WQAdView;

public class WqAdapter extends AdViewAdapter implements WQAdEventListener {

	private static int networkType() {
		return AdViewUtil.NETWORK_TYPE_WQ;
	}

	public static void load(AdViewAdRegistry registry) {
		try {
			if (Class.forName("com.wqmobile.sdk.WQAdView") != null) {
				registry.registerClass(networkType(), WqAdapter.class);
			}
		} catch (ClassNotFoundException e) {
		}
	}

	public WqAdapter() {
	}

	@Override
	public void initAdapter(AdViewLayout adViewLayout, Ration ration) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handle() {
		// TODO Auto-generated method stub
		AdViewUtil.logInfo("Into WQ");
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null) {
			return;
		}
		Activity activity = adViewLayout.activityReference.get();
		if (activity == null) {
			return;
		}

		WQAdView adView = new WQAdView(activity);
		adView.setAdEventListener(this);

		adView.setAdPlatform("adviewc633659b4fda54", AdViewUtil.ADVIEW_VER);
		adView.init(ration.key, ration.key2);
		adView.setRefreshable(true);
		adViewLayout.AddSubView(adView);

		// adViewLayout.activeRation = adViewLayout.nextRation;
		// adViewLayout.removeAllViews();
		// RelativeLayout.LayoutParams layoutParams = new
		// RelativeLayout.LayoutParams(
		// RelativeLayout.LayoutParams.WRAP_CONTENT,
		// RelativeLayout.LayoutParams.WRAP_CONTENT);
		// layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		// adViewLayout.addView(adView, layoutParams);
		// adViewLayout.addCloseButton(adViewLayout);
	}

	@Override
	public void onWQAdReceived(WQAdView adView) {

		AdViewUtil.logInfo("onWQAdReceived");
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null) {
			return;
		}
		// adViewLayout.adViewManager.resetRollover();
		// adViewLayout.handler.post(new ViewAdRunnable(adViewLayout, adView));
		// adViewLayout.rotateThreadedDelayed();
		// adViewLayout.AddSubView(adView);
		super.onSuccessed(adViewLayout, ration);
		adViewLayout.adViewManager.resetRollover();
		adViewLayout.rotateThreadedDelayed();
	}

	@Override
	public void onWQAdFailed(WQAdView adView) {
		AdViewUtil.logInfo("onWQAdFailed");
		adView.setAdEventListener(null);

		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null) {
			return;
		}
		super.onFailed(adViewLayout, ration);
	}

	@Override
	public void onWQAdDismiss(WQAdView arg0) {
	}

	@Override
	public void onWQAdClick(WQAdView arg0) {
		AdViewUtil.logInfo("onAdClick");
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null) {
			return;
		}
		adViewLayout.reportClick();

	}

	@Override
	public void onWQAdView(WQAdView arg0) {
		AdViewUtil.logInfo("onWQAdView");
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null) {
			return;
		}
		adViewLayout.reportImpression();
	}

}
