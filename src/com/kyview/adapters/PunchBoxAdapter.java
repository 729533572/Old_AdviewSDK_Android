package com.kyview.adapters;

import android.app.Activity;

import com.kyview.AdViewAdRegistry;
import com.kyview.AdViewLayout;
import com.kyview.AdViewTargeting;
import com.kyview.AdViewTargeting.RunMode;
import com.kyview.obj.Ration;
import com.kyview.util.AdViewUtil;
import com.punchbox.PunchBox;
import com.punchbox.exception.PBException;
import com.punchbox.listener.AdListener;
import com.punchbox.request.FixedAdRequest;
import com.punchbox.view.FixedAdView;

public class PunchBoxAdapter extends AdViewAdapter implements AdListener {
	private Activity context;
	private FixedAdView mFixedadView = null;

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
		PunchBox mPb = PunchBox.getInstance();
		FixedAdRequest request = new FixedAdRequest(context);
		mFixedadView = new FixedAdView(context);
		

		if (AdViewTargeting.getRunMode() == RunMode.TEST)
			mPb.init(context, "22222222-2222-2222-2222-222222222222", "");
		else
			mPb.init(context, ration.key, "");

		mPb.setServerMode(false);

		mFixedadView.setDisplayInterval(Integer.MAX_VALUE);

		mFixedadView.setAdListener(this);
		// 正式请求广告
		mFixedadView.loadAd(request);
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
		//adViewLayout.rotateThreadedPri(1);

	}

	@Override
	public void onPresentScreen() {
		AdViewUtil.logInfo("onPresentScreen");
	}

	@Override
	public void onTouched() {
		AdViewUtil.logInfo("onAdClick");
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null) {
			return;
		}
		adViewLayout.reportClick();
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
		if (mFixedadView != null) {
			AdViewUtil.logInfo("release punchbox");
			mFixedadView.destroy();
			mFixedadView = null;
		}
	}


}
