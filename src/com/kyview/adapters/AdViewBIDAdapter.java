package com.kyview.adapters;

import com.kuaiyou.adbid.AdViewBIDView;
import com.kuaiyou.KyAdBaseView;
import com.kuaiyou.interfaces.OnAdListener;
import com.kyview.AdViewAdRegistry;
import com.kyview.AdViewLayout;
import com.kyview.AdViewTargeting;
import com.kyview.AdViewLayout.ViewAdRunnable;
import com.kyview.AdViewTargeting.RunMode;
import com.kyview.obj.Ration;
import com.kyview.util.AdViewUtil;

public class AdViewBIDAdapter extends AdViewAdapter implements OnAdListener {

	private AdViewBIDView adFillView = null;

	private static int networkType() {
		return AdViewUtil.NETWORK_TYPE_ADBID;
	}

	public static void load(AdViewAdRegistry registry) {
		try {
			if (Class.forName("com.kuaiyou.adbid.AdViewBIDView") != null) {
				registry.registerClass(networkType(), AdViewBIDAdapter.class);
			}
		} catch (ClassNotFoundException e) {
		}
	}

	@Override
	public void handle() {
		AdViewUtil.logInfo("Into AdBid");
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
		AdViewUtil.logInfo("AdBid success");
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null)
			return;
		view.setAnimRotated();
		view.startLayoutAnimation();
		super.onSuccessed(adViewLayout, ration);

		adViewLayout.adViewManager.resetRollover();
		// adViewLayout.handler.post(new ViewAdRunnable(adViewLayout, view));
		adViewLayout.handler.postDelayed(
				new ViewAdRunnable(adViewLayout, view), 400);
		adViewLayout.rotateThreadedDelayed();
		// adViewLayout.AddSubView(view);
	}

	@Override
	public void onConnectFailed(KyAdBaseView view, String msg) {
		AdViewUtil.logInfo("AdBid failure, msg=" + msg);
		view.setBannerAdListener(null);

		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null) {
			return;
		}
		// adViewLayout.rotateAd();
		super.onFailed(adViewLayout, ration);
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
