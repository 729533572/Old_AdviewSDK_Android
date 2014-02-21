package com.kyview.adapters;

import com.kuaiyou.KyAdBaseView;
import com.kuaiyou.adbid.AdViewBIDView;
import com.kuaiyou.interfaces.OnAdListener;
import com.kyview.AdViewAdRegistry;
import com.kyview.AdViewLayout;
import com.kyview.AdViewTargeting;
import com.kyview.AdViewLayout.ViewAdRunnable;
import com.kyview.AdViewTargeting.RunMode;
import com.kyview.obj.Ration;
import com.kyview.util.AdViewUtil;

public class AdFillAdapter extends AdViewAdapter implements OnAdListener {

	private AdViewBIDView adFillView = null;

	private static int networkType() {
		return AdViewUtil.NETWORK_TYPE_ADFILL;
	}

	public static void load(AdViewAdRegistry registry) {
		try {
			if (Class.forName("com.kuaiyou.adbid.AdViewBIDView") != null) {
				registry.registerClass(networkType(), AdFillAdapter.class);
			}
		} catch (ClassNotFoundException e) {
		}
	}

	@Override
	public void handle() {
		AdViewUtil.logInfo("Into AdFill");
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null)
			return;
		AdViewLayout.refreashTime = adViewLayout.extra.cycleTime * 1000 / 2;
		adFillView = new AdViewBIDView(adViewLayout.getContext(),
				adViewLayout.keyAdView, ration.type,
				(AdViewTargeting.getRunMode() == RunMode.TEST),
				AdViewLayout.refreashTime);
		adFillView.setShowCloseBtn(false);
		adFillView.setBannerAdListener(this);

	}


	@Override
	public void initAdapter(AdViewLayout adViewLayout, Ration ration) {
	}

	/************** interface start ***************/

	@Override
	public void onReceivedAd(KyAdBaseView view) {
		AdViewUtil.logInfo("AdFill success");
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null)
			return;
		view.setAnimRotated();
		view.startLayoutAnimation();
		AdViewUtil.adfill_count += 1;
		super.onSuccessed(adViewLayout, ration);
		adViewLayout.adViewManager.resetRollover();
		adViewLayout.handler.post(new ViewAdRunnable(adViewLayout, view));
		adViewLayout.rotateThreadedDelayed();
		// adViewLayout.AddSubView(view);
	}

	@Override
	public void onConnectFailed(KyAdBaseView view, String msg) {
		AdViewUtil.logInfo("AdFill failure, msg=" + msg);
		view.setBannerAdListener(null);

		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null) {
			return;
		}
		adViewLayout.rotateAd();
	}

	@Override
	public void onAdClicked(KyAdBaseView view) {
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null) {
			return;
		}
		adViewLayout.reportClick();
	}

	/************** interface end ***************/

	@Override
	public void clean() {
		super.clean();
	}

	@Override
	public void onAdClose(KyAdBaseView view) {
		// TODO Auto-generated method stub
		
	}

}
