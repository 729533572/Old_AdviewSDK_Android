package com.kyview.adapters;

import android.app.Activity;
import android.widget.LinearLayout;

import com.kyview.AdViewAdRegistry;
import com.kyview.AdViewLayout;
import com.kyview.obj.Ration;
import com.kyview.util.AdViewUtil;
import com.otomod.ad.AdSize;
import com.otomod.ad.AdView;
import com.otomod.ad.listener.O2OAdListener;

public class O2omobiAdapter extends AdViewAdapter implements O2OAdListener {
	private Activity context = null;
	private AdView adView = null;

	private static int networkType() {
		return AdViewUtil.NETWORK_TYPE_O2OMOBI;
	}

	public static void load(AdViewAdRegistry registry) {
		try {
			if (Class.forName("com.otomod.ad.listener.O2OAdListener") != null) {
				registry.registerClass(networkType(), O2omobiAdapter.class);
			}
		} catch (ClassNotFoundException e) {
		}
	}

	@Override
	public void handle() {
		// TODO Auto-generated method stub
		adView.isCraousel(false); // true表示开启轮播，false则关闭
		adView.setAdListener(this);
		adView.request();
	}

	@Override
	public void initAdapter(AdViewLayout adViewLayout, Ration ration) {
		// TODO Auto-generated method stub
		context = adViewLayout.activityReference.get();
		LinearLayout layout = new LinearLayout(context);
		adViewLayout.addView(layout);
		adView = AdView.createBanner(context, layout, AdSize.AD_SMART_BANNER,
				ration.key);
	}

	@Override
	public void onAdFailed() {
		// TODO Auto-generated method stub
		AdViewUtil.logInfo("AdViewListener.onAdFailed");
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null) {
			return;
		}
		super.onFailed(adViewLayout, ration);
	}

	@Override
	public void onAdSuccess() {
		// TODO Auto-generated method stub
		AdViewUtil.logInfo("O2omobi success");
		// adView.setAdEventListener(null);

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
	public void onClick() {
		// TODO Auto-generated method stub

		AdViewUtil.logInfo("onAdClick");
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null) {
			return;
		}
		adViewLayout.reportClick();
	}

	@Override
	public void onClose() {
		// TODO Auto-generated method stub
		AdViewUtil.logInfo("onClose");
	}

}
