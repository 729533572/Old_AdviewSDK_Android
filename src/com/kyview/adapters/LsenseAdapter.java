package com.kyview.adapters;

import android.app.Activity;
import android.graphics.Color;

import com.kyview.AdViewAdRegistry;
import com.kyview.AdViewLayout;
import com.kyview.AdViewLayout.ViewAdRunnable;
import com.kyview.obj.Extra;
import com.kyview.obj.Ration;
import com.kyview.util.AdViewUtil;
import com.l.adlib_android.AdListenerEx;
import com.l.adlib_android.AdView;

public class LsenseAdapter extends AdViewAdapter implements AdListenerEx {
	private AdView adView = null;

	private static int networkType() {
		return AdViewUtil.NETWORK_TYPE_LSENSE;
	}

	public static void load(AdViewAdRegistry registry) {
		try {
			if (Class.forName("com.l.adlib_android.AdView") != null) {
				registry.registerClass(networkType(), LsenseAdapter.class);
			}
		} catch (ClassNotFoundException e) {
		}
	}

	public LsenseAdapter() {
	}

	@Override
	public void initAdapter(AdViewLayout adViewLayout, Ration ration) {
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unused")
	@Override
	public void handle() {
		// TODO Auto-generated method stub
		AdViewUtil.logInfo("Into Lsense");
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null) {
			return;
		}

		Extra extra = adViewLayout.extra;
		int bgColor = Color.rgb(extra.bgRed, extra.bgGreen, extra.bgBlue);
		int fgColor = Color.rgb(extra.fgRed, extra.fgGreen, extra.fgBlue);
		Activity activity = adViewLayout.activityReference.get();
		if (activity == null) {
			return;
		}
		try {
			adView = null;
			adView = new AdView(activity, Integer.valueOf(ration.key),
					AdView.ROTATE3D);
			adView.setOnAdListenerEx(this);

		} catch (IllegalArgumentException e) {
			adViewLayout.rollover();
			return;
		}
	}

	@Override
	public void OnConnectFailed(String arg0) {
		AdViewUtil.logInfo("Lsense failure");
		adView.setOnAdListenerEx(null);

		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null) {
			return;
		}
		super.onFailed(adViewLayout, ration);
		// adViewLayout.rotateThreadedPri(1);
	}

	@Override
	public void OnAcceptAd(int arg0) {
		AdViewUtil.logInfo("Lsense success");
		adView.setOnAdListenerEx(null);
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null)
			return;
		adViewLayout.adViewManager.resetRollover();
		adViewLayout.handler.post(new ViewAdRunnable(adViewLayout, adView));
		adViewLayout.rotateThreadedDelayed();
		super.onSuccessed(adViewLayout, ration);
	}

}
