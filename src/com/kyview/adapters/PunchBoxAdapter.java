package com.kyview.adapters;

import android.app.Activity;

import com.kyview.AdViewAdRegistry;
import com.kyview.AdViewLayout;
import com.kyview.AdViewTargeting;
import com.kyview.AdViewTargeting.RunMode;
import com.kyview.obj.Ration;
import com.kyview.util.AdViewUtil;
import com.punchbox.ads.AdRequest;
import com.punchbox.ads.AdView;
import com.punchbox.exception.PBException;
import com.punchbox.listener.AdListener;

public class PunchBoxAdapter extends AdViewAdapter implements AdListener {
	private Activity context;
	private AdView mFixedadView = null;

	private static int networkType() {
		return AdViewUtil.NETWORK_TYPE_PUNCHBOX;
	}

	public static void load(AdViewAdRegistry registry) {
		try {
			if (Class.forName("com.punchbox.listener.AdListener") != null) {
				registry.registerClass(networkType(), PunchBoxAdapter.class);
			}
		} catch (ClassNotFoundException e) {
		}
	}

	@Override
	public void handle() {
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (null == adViewLayout)
			return;
		// AdView mPb = PunchBox.getInstance(context);
		// FixedAdRequest request = new FixedAdRequest(context);
		mFixedadView = new AdView(context, ration.key2);

		if (AdViewTargeting.getRunMode() == RunMode.TEST)
			mFixedadView.setPublisherId("100011-0A2E90-7CDB-6FD3-A9B983ABBBBA");
		else
			mFixedadView.setPublisherId(ration.key);

		// mFixedadView.setServerMode(false);
		mFixedadView.setRequestInterval(500);
		mFixedadView.setDisplayTime(500);

		mFixedadView.setAdListener(this);
		// 正式请求广告
		mFixedadView.loadAd(new AdRequest());
		adViewLayout.AddSubView(mFixedadView);

	}

	@Override
	public void initAdapter(AdViewLayout adViewLayout, Ration ration) {
		context = adViewLayout.activityReference.get();
	}

	@Override
	public void onDismissScreen() {
		AdViewUtil.logInfo("onDismissScreen");
	}

	@Override
	public void onFailedToReceiveAd(PBException arg0) {
		AdViewUtil.logInfo("AdViewListener.onAdFailed, reason=" + arg0);
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null) {
			return;
		}
		super.onFailed(adViewLayout, ration);
		// adViewLayout.rotateThreadedPri(1);

	}

	@Override
	public void onPresentScreen() {
		AdViewUtil.logInfo("onPresentScreen");
	}

	@Override
	public void onReceiveAd() {
		AdViewUtil.logInfo("onReceiveAd");
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null) {
			return;
		}
		super.onSuccessed(adViewLayout, ration);
		adViewLayout.adViewManager.resetRollover();
		adViewLayout.rotateThreadedDelayed();
		adViewLayout.reportImpression();
	}

	@Override
	public void clean() {
		super.clean();
		try {
			if (mFixedadView != null) {
				AdViewUtil.logInfo("release punchbox");
				mFixedadView.destroy();
				mFixedadView = null;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
