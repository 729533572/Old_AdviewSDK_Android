package com.kyview.adapters;

import android.widget.RelativeLayout;

import com.adzhidian.ui.AdView;
import com.adzhidian.ui.ZhidianManager;
import com.adzhidian.ui.impl.AdListener;
import com.kyview.AdViewAdRegistry;
import com.kyview.AdViewLayout;
import com.kyview.obj.Ration;
import com.kyview.util.AdViewUtil;

public class ZhiDianAdapter extends AdViewAdapter implements AdListener{
	private int adHeight ;
	private int adWidth ;
	private AdView adView=null;
	

	private static int networkType() {
		return AdViewUtil.NETWORK_TYPE_ZHIDIAN;
	}
	
	public static void load(AdViewAdRegistry registry) {
		try {
			if(Class.forName("com.adzhidian.ui.impl.AdListener") != null) {
				registry.registerClass(networkType(), ZhiDianAdapter.class);
			}
		} catch (ClassNotFoundException e) {}
	}
	
	
	@Override
	public void handle() {
		AdViewLayout adViewLayout=adViewLayoutReference.get();
		if(null==adViewLayout)
			return;
		 ZhidianManager.zhiInit(ration.key, "PADVIEW");
		adView = new AdView(adViewLayout.getContext());
		adView.setReceiveAdListener(this);
		adViewLayout.activeRation = adViewLayout.nextRation;
		adViewLayout.removeAllViews();
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				adWidth,adHeight);
		layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		adViewLayout.addView(adView, layoutParams);
		adViewLayout.addCloseButton(adViewLayout);
		
	}

	@Override
	public void initAdapter(AdViewLayout adViewLayout, Ration ration) {
		calcAdSize(adViewLayout);
	}

	private void calcAdSize(AdViewLayout adViewLayout) {
		int width=320;
		int height=48;
		int screenWidth = adViewLayout.adViewManager.width;
		if (screenWidth < 480) {
			width = 320;
			height = 48;
		} else if (screenWidth < 720) {
			width = 480;
			height = 72;
		} else if (screenWidth >= 720) {
			width = 720;
			height = 108;
		}

		adHeight = height;//AdViewUtil.convertToScreenPixels(height, adViewLayout.mDensity);
		adWidth = width;//AdViewUtil.convertToScreenPixels(width, adViewLayout.mDensity);
	}
	
	@Override
	public void onFailedToReceiveAd() {
		AdViewUtil.logInfo("zhidian--onReceiveFail");
		adView.setReceiveAdListener(null);
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if(adViewLayout == null) 
			return;
		super.onFailed(adViewLayout, ration);
	}

	@Override
	public void onReceiveAd() {
		AdViewUtil.logInfo("zhidian--onReceiveAd");
		adView.setReceiveAdListener(null);
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if(adViewLayout == null) 
			return;
		
		super.onSuccessed(adViewLayout, ration);
		adViewLayout.reportImpression();
		adViewLayout.adViewManager.resetRollover();
		adViewLayout.rotateThreadedDelayed();
	}

}
