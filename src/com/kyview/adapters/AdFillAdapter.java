package com.kyview.adapters;

import com.kyview.AdViewAdRegistry;
import com.kyview.AdViewLayout;
import com.kyview.AdViewTargeting;
import com.kyview.AdViewTargeting.RunMode;
//import com.kyview.AdViewLayout.ViewAdRunnable;
import com.kuaiyou.adfill.ad.AdFillView;
import com.kuaiyou.adfill.ad.KyAdBaseView;
import com.kuaiyou.adfill.ad.OnAdListener;
import com.kyview.obj.Ration;
import com.kyview.util.AdViewUtil;

public class AdFillAdapter extends AdViewAdapter implements OnAdListener{

	private AdFillView adFillView=null;
	private static int networkType() {
		return AdViewUtil.NETWORK_TYPE_ADFILL;
	}

	public static void load(AdViewAdRegistry registry) {
		try {
			if (Class.forName("com.kuaiyou.adfill.ad.AdFillView") != null) {
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
		AdViewLayout.refreashTime=adViewLayout.extra.cycleTime*1000/2;
		adFillView=new AdFillView(adViewLayout.getContext(),adViewLayout.keyAdView,ration.type,(AdViewTargeting.getRunMode()==RunMode.TEST),AdViewLayout.refreashTime);
		adFillView.setOnAdListener(this);

	}
	@Override
	public void click(int isMissTouch) {
		this.onAdClicked(adFillView,isMissTouch);
	}
	@Override
	public void initAdapter(AdViewLayout adViewLayout, Ration ration) {
	}

	/**************interface start***************/

	@Override
	public void onReceivedAd(KyAdBaseView view) {
		 AdViewUtil.logInfo("AdFill success");
			view.setOnAdListener(null);	
			AdViewLayout adViewLayout = adViewLayoutReference.get();
			if(adViewLayout == null) 
				return;
			view.setAnimRotated();
			view.startLayoutAnimation();
			AdViewUtil.adfill_count += 1;
			((AdFillView)view).reportImpression();//KyAdBaseView
			super.onSuccessed(adViewLayout, ration);
			adViewLayout.adViewManager.resetRollover();
			//adViewLayout.handler.post(new ViewAdRunnable(adViewLayout,view));
			adViewLayout.AddSubView(view);
			adViewLayout.rotateThreadedDelayed();
	}

	@Override
	public void onConnectFailed(KyAdBaseView view,String msg) {
		AdViewUtil.logInfo("AdFill failure, msg="+msg);
		view.setOnAdListener(null);

		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if(adViewLayout == null) {
			return;
		}
		adViewLayout.rotateAd();
	}

	@Override
	public void onAdClicked(KyAdBaseView view,int isMissTouch) {
		view.onAdClick(isMissTouch);
	}
	
	/**************interface end***************/
	
	@Override
	public void clean() {
		if(null!=adFillView.mGifThread){
			adFillView.mGifThread.interrupt();
			adFillView.mGifThread=null;
		}
		super.clean();
	}
}
