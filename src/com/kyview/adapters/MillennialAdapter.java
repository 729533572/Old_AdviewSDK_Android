package com.kyview.adapters;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.RelativeLayout;

import com.kyview.AdViewAdRegistry;
import com.kyview.AdViewLayout;
import com.kyview.obj.Ration;
import com.kyview.util.AdViewUtil;
import com.millennialmedia.android.MMAd;
import com.millennialmedia.android.MMAdView;
import com.millennialmedia.android.MMException;
import com.millennialmedia.android.MMSDK;
import com.millennialmedia.android.RequestListener;

public class MillennialAdapter extends AdViewAdapter implements RequestListener {
	private static final int IAB_LEADERBOARD_WIDTH = 728;
	private static final int IAB_LEADERBOARD_HEIGHT = 90;
	private static final int MED_BANNER_WIDTH = 480;
	private static final int MED_BANNER_HEIGHT = 60;
	private static final int BANNER_AD_WIDTH = 320;
	private static final int BANNER_AD_HEIGHT = 50;

	private int placementWidth = BANNER_AD_WIDTH;
	private int placementHeight = BANNER_AD_HEIGHT;

	private int layoutWidth = 0;
	private int layoutHeight = 0;

	private static int networkType() {
		return AdViewUtil.NETWORK_TYPE_MILLENNIAL;
	}

	public static void load(AdViewAdRegistry registry) {
		try {
			if (Class.forName("com.millennialmedia.android.MMAdView") != null) {
				registry.registerClass(networkType(), MillennialAdapter.class);
			}
		} catch (ClassNotFoundException e) {
		}
	}

	public MillennialAdapter() {
	}

	@Override
	public void initAdapter(AdViewLayout adViewLayout, Ration ration) {
		if (canFit(IAB_LEADERBOARD_WIDTH)) {
			placementWidth = IAB_LEADERBOARD_WIDTH;
			placementHeight = IAB_LEADERBOARD_HEIGHT;
		} else if (canFit(MED_BANNER_WIDTH)) {
			placementWidth = MED_BANNER_WIDTH;
			placementHeight = MED_BANNER_HEIGHT;
		}
		layoutWidth = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, placementWidth,
				adViewLayout.activityReference.get().getResources()
						.getDisplayMetrics());
		layoutHeight = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, placementHeight,
				adViewLayout.activityReference.get().getResources()
						.getDisplayMetrics());
	}

	@Override
	public void handle() {
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null) {
			return;
		}
		Activity activity = adViewLayout.activityReference.get();

		MMAdView adView = new MMAdView(activity);
		adView.setApid(ration.key);
		adView.setId(MMSDK.getDefaultAdId());

		adView.setWidth(placementWidth);
		adView.setHeight(placementHeight);

		adView.setListener(this);
		adView.getAd();

		adViewLayout.activeRation = adViewLayout.nextRation;
		adViewLayout.removeAllViews();
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				layoutWidth, layoutHeight);
		layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		adViewLayout.addView(adView, layoutParams);
		adViewLayout.addCloseButton(adViewLayout);

	}

	protected boolean canFit(int adWidth) {
		int adWidthPx = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, adWidth,
				adViewLayoutReference.get().activityReference.get()
						.getResources().getDisplayMetrics());
		DisplayMetrics metrics = adViewLayoutReference.get().activityReference
				.get().getResources().getDisplayMetrics();
		return metrics.widthPixels >= adWidthPx;
	}

	@Override
	public void MMAdOverlayLaunched(MMAd arg0) {
		// TODO Auto-generated method stub
		AdViewUtil.logInfo("Millennial Ad Overlay Launched");
	}

	@Override
	public void MMAdRequestIsCaching(MMAd arg0) {
		AdViewUtil.logInfo("MMAdRequestIsCaching");
	}

	@Override
	public void requestCompleted(MMAd arg0) {
		AdViewUtil.logInfo("Millennial success");
		arg0.setListener(null);

		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null) {
			return;
		}
		super.onSuccessed(adViewLayout, ration);
		adViewLayout.reportImpression();
		adViewLayout.adViewManager.resetRollover();
		adViewLayout.rotateThreadedDelayed();

	}

	@Override
	public void requestFailed(MMAd arg0, MMException arg1) {
		AdViewUtil.logInfo("Millennial failure");
		arg0.setListener(null);
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null)
			return;
		super.onFailed(adViewLayout, ration);
		// adViewLayout.rotateThreadedPri(1);

	}

	@Override
	public void MMAdOverlayClosed(MMAd arg0) {

	}

	@Override
	public void onSingleTap(MMAd arg0) {
	}

}
