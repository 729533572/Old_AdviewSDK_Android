package com.kyview.adapters;

import android.app.Activity;

import com.kyview.AdViewAdRegistry;
import com.kyview.AdViewLayout;
import com.kyview.AdViewTargeting;
import com.kyview.AdViewTargeting.RunMode;
import com.kyview.obj.Ration;
import com.kyview.util.AdViewUtil;
import com.qq.e.ads.AdListener;
import com.qq.e.ads.AdRequest;
import com.qq.e.ads.AdSize;
import com.qq.e.ads.AdView;

public class GdtAdapter extends AdViewAdapter implements AdListener {
	private AdView adv = null;

	private static int networkType() {
		return AdViewUtil.NETWORK_TYPE_GDT;
	}

	public static void load(AdViewAdRegistry registry) {
		try {
			if (Class.forName("com.qq.e.ads.AdView") != null) {
				registry.registerClass(networkType(), GdtAdapter.class);
			}
		} catch (ClassNotFoundException e) {
		}
	}

	public GdtAdapter() {
	}

	@Override
	public void initAdapter(AdViewLayout adViewLayout, Ration ration) {
	}

	@Override
	public void handle() {

		AdViewUtil.logInfo("Into Gdt");
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null)
			return;

		Activity activity = adViewLayout.activityReference.get();
		if (activity == null)
			return;

		adv = new AdView(activity, AdSize.BANNER, ration.key, ration.key2);
		// adViewLayout.adViewManager.resetRollover();
		// adViewLayout.handler.post(new ViewAdRunnable(adViewLayout, adv));
		// adViewLayout.rotateThreadedDelayed();

		AdRequest adr = new AdRequest();
		if (AdViewTargeting.getRunMode() == RunMode.TEST)
			adr.setTestAd(true);
		else
			adr.setTestAd(false);
		adr.setRefresh(0);
		adv.setAdListener(this);
		adv.fetchAd(adr);

		adViewLayout.AddSubView(adv);
	}

	@Override
	public void onAdReceiv() {
		AdViewUtil.logInfo("onAdReceiv");

		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null) {
			return;
		}
		super.onSuccessed(adViewLayout, ration);
		adViewLayout.reportImpression();
		adViewLayout.adViewManager.resetRollover();
		// adViewLayout.handler.post(new ViewAdRunnable(adViewLayout, adView));
		adViewLayout.rotateThreadedDelayed();
	}

	@Override
	public void onNoAd() {
		AdViewUtil.logInfo("onFailedToReceiveAd");
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null) {
			return;
		}
		super.onFailed(adViewLayout, ration);
	}

	@Override
	public void clean() {
		super.clean();
		if (null != adv)
			adv = null;
	}
}
