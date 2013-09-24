package com.kyview.adapters;

import java.text.SimpleDateFormat;

import android.app.Activity;

import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.kyview.AdViewAdRegistry;
import com.kyview.AdViewLayout;
import com.kyview.AdViewLayout.ViewAdRunnable;
import com.kyview.AdViewTargeting;
import com.kyview.obj.Ration;
import com.kyview.util.AdViewUtil;

public class AdMobAdapter extends AdViewAdapter implements AdListener {
	private AdView adView;

	private static int networkType() {
		return AdViewUtil.NETWORK_TYPE_ADMOB;
	}

	public static void load(AdViewAdRegistry registry) {
		try {
			if (Class.forName("com.google.ads.AdView") != null) {
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
		adView = new AdView(activity, AdSize.SMART_BANNER, ration.key);
		adView.setAdListener(this);

		adView.loadAd(requestForAdWhirlLayout(adViewLayout));

	}

	protected AdRequest requestForAdWhirlLayout(AdViewLayout layout) {
		AdRequest result = new AdRequest();
		/*
		 * if(AdViewTargeting.getRunMode()==RunMode.TEST)
		 * result.addTestDevice(AdRequest.TEST_EMULATOR); else
		 * if(AdViewTargeting.getRunMode()==RunMode.NORMAL)
		 * ;//result.setTesting(false); else ;//result.setTesting(false);
		 * result.setGender(genderForAdViewTargeting());
		 * result.setBirthday(birthdayForAdViewTargeting());
		 * result.setKeywords(AdViewTargeting.getKeywordSet());
		 */
		return result;
	}

	@Override
	public void onDismissScreen(Ad arg0) {
		// TODO Auto-generated method stub
		AdViewUtil.logInfo("AdMob onDismissScreen");
	}

	@Override
	public void onFailedToReceiveAd(Ad arg0, ErrorCode arg1) {
		AdViewUtil.logInfo("AdMob fail");
		arg0.setAdListener(null);
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null) {
			return;
		}
		super.onFailed(adViewLayout, ration);
		//adViewLayout.rotateThreadedPri(1);
	}

	@Override
	public void onLeaveApplication(Ad arg0) {
		// TODO Auto-generated method stub
		AdViewUtil.logInfo("AdMob onLeaveApplication");
	}

	@Override
	public void onPresentScreen(Ad arg0) {
		// TODO Auto-generated method stub
		AdViewUtil.logInfo("AdMob onPresentScreen");

		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null) {
			return;
		}
		// adViewLayout.reportImpression();
	}

	@Override
	public void onReceiveAd(Ad arg0) {
		AdViewUtil.logInfo("AdMob success");
		arg0.setAdListener(null);
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null) {
			return;
		}
		if (!(arg0 instanceof AdView)) {
			return;
		}
		super.onSuccessed(adViewLayout, ration);
		adView = (AdView) arg0;
		adViewLayout.adViewManager.resetRollover();
		adViewLayout.handler.post(new ViewAdRunnable(adViewLayout, adView));
		adViewLayout.rotateThreadedDelayed();

	}

	@Override
	public void clean() {
		// TODO Auto-generated method stub
		super.clean();
		
		if(adView!=null) {
			adView.setAdListener(null);
			adView.destroy();
		}
		
		adView=null;
		AdViewUtil.logInfo("release AdMob");
	}

	/*******************************************************************/
	// End of AdMob listeners
}
