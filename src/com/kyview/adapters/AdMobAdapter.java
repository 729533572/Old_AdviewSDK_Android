package com.kyview.adapters;

import java.text.SimpleDateFormat;

import android.app.Activity;

import com.google.ads.AdRequest;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest.Builder;
import com.google.android.gms.ads.AdView;
import com.kyview.AdViewAdRegistry;
import com.kyview.AdViewLayout;
import com.kyview.AdViewLayout.ViewAdRunnable;
import com.kyview.AdViewTargeting;
import com.kyview.obj.Ration;
import com.kyview.util.AdViewUtil;

public class AdMobAdapter extends AdViewAdapter {
	private AdView adView;

	private static int networkType() {
		return AdViewUtil.NETWORK_TYPE_ADMOB;
	}

	public static void load(AdViewAdRegistry registry) {
		try {
			if (Class.forName("com.google.android.gms.ads.AdView") != null) {
				registry.registerClass(networkType(), AdMobAdapter.class);
			}
		} catch (ClassNotFoundException e) {
		}
	}

	public AdMobAdapter() {
	}

	@Override
	public void initAdapter(AdViewLayout adViewLayout, Ration ration) {
		// TODO Auto-generated constructor stub
	}

	protected String birthdayForAdViewTargeting() {
		return (AdViewTargeting.getBirthDate() != null) ? new SimpleDateFormat(
				"yyyyMMdd").format(AdViewTargeting.getBirthDate().getTime())
				: null;
	}

	protected AdRequest.Gender genderForAdViewTargeting() {
		switch (AdViewTargeting.getGender()) {
		case MALE:
			return AdRequest.Gender.MALE;
		case FEMALE:
			return AdRequest.Gender.FEMALE;
		default:
			return null;
		}
	}

	@Override
	public void handle() {
		AdViewUtil.logInfo("Into AdMob");
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null) {
			return;
		}

		Activity activity = adViewLayout.activityReference.get();
		if (activity == null) {
			return;
		}
		adView = new AdView(activity);
		adView.setAdSize(com.google.android.gms.ads.AdSize.SMART_BANNER);
		adView.setAdUnitId(ration.key);
		// Create an ad request.
		Builder adRequestBuilder = new Builder();

		// Optionally populate the ad request builder.
		// adRequestBuilder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR);

		// Add the AdView to the view hierarchy.

		// Start loading the ad.

		adView.setAdListener(new AdListener() {
			@Override
			public void onAdFailedToLoad(int errorCode) {
				// TODO Auto-generated method stub
				super.onAdFailedToLoad(errorCode);
				AdViewUtil.logInfo("AdMob onAdFailedToLoa" + errorCode);
				adView.setAdListener(null);
				AdViewLayout adViewLayout = adViewLayoutReference.get();
				if (adViewLayout == null) {
					return;
				}
				AdMobAdapter.super.onFailed(adViewLayout, ration);
			}

			@Override
			public void onAdClosed() {
				// TODO Auto-generated method stub
				super.onAdClosed();
				AdViewUtil.logInfo("AdMob onAdClosed");
			}

			@Override
			public void onAdLeftApplication() {
				// TODO Auto-generated method stub
				super.onAdLeftApplication();
				AdViewUtil.logInfo("AdMob onAdLeftApplication");
			}

			@Override
			public void onAdLoaded() {
				// TODO Auto-generated method stub
				super.onAdLoaded();
				AdViewUtil.logInfo("AdMob onAdLoaded");
				if (null == adView)
					return;
				adView.pause();
				AdViewLayout adViewLayout = adViewLayoutReference.get();
				if (adViewLayout == null) {
					return;
				}
				AdMobAdapter.super.onSuccessed(adViewLayout, ration);
				adViewLayout.adViewManager.resetRollover();
				adViewLayout.handler.post(new ViewAdRunnable(adViewLayout,
						adView));
				adViewLayout.rotateThreadedDelayed();
			}

			@Override
			public void onAdOpened() {
				// TODO Auto-generated method stub
				super.onAdOpened();
				AdViewUtil.logInfo("AdMob onAdOpened");
			}
		});

		adView.loadAd(adRequestBuilder.build());
		// adViewLayout.AddSubView(adView);
	}

	@Override
	public void clean() {
		// TODO Auto-generated method stub
		super.clean();

		if (adView != null) {
			adView.setAdListener(null);
			adView.destroy();
		}

		adView = null;
		AdViewUtil.logInfo("release AdMob");
	}

	/*******************************************************************/
	// End of AdMob listeners
}
