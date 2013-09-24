package com.kyview.adapters;

import android.app.Activity;

import com.izp.views.IZPDelegate;
import com.izp.views.IZPView;
import com.kyview.AdViewAdRegistry;
import com.kyview.AdViewLayout;
import com.kyview.AdViewTargeting;
import com.kyview.AdViewTargeting.RunMode;
import com.kyview.obj.Ration;
import com.kyview.util.AdViewUtil;



public class IzpAdapter extends AdViewAdapter implements IZPDelegate{
	

	private static int networkType() {
		return AdViewUtil.NETWORK_TYPE_IZPTEC;
	}
	
	public static void load(AdViewAdRegistry registry) {
		try {
			if(Class.forName("com.izp.views.IZPView") != null) {
				registry.registerClass(networkType(), IzpAdapter.class);
			}
		} catch (ClassNotFoundException e) {}
	}

	public IzpAdapter() {
	}
	
	@Override
	public void initAdapter(AdViewLayout adViewLayout, Ration ration) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handle() {
		// TODO Auto-generated method stub
		
		AdViewUtil.logInfo("Into Izp");
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null) {
			return;
		}

		Activity activity = adViewLayout.activityReference.get();
		if (activity == null) {
			return;
		}

		IZPView adView=new IZPView(activity);
		adView.productID=ration.key;
		adView.adType="1";
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			adView.isDev=true;
		else
			adView.isDev=false;
		adView.delegate=this;
		adView.startAdExchange();

		adViewLayout.adViewManager.resetRollover();
		adViewLayout.pushSubViewForIzp(adView);
		//adViewLayout.handler.post(new ViewAdRunnable(adViewLayout, adView));
		
	}

	@Override
	public boolean shouldRequestFreshAd(IZPView view) {
		AdViewUtil.logInfo("shouldRequestFreshAd");
		return true;
	}

	@Override
	public void didReceiveFreshAd(IZPView view, int adCount)
	{
		AdViewUtil.logInfo("didReceiveFreshAd, adCount="+adCount);
	}

	@Override
	public boolean shouldShowFreshAd(IZPView view)
	{
		AdViewUtil.logInfo("shouldShowFreshAd");
		return true;
	}

	@Override
	public void didShowFreshAd(IZPView view)
	{
		AdViewUtil.logInfo("didShowFreshAd");
		view.stopAdExchange();
		view.delegate=null;
		
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		  if(adViewLayout == null) {
			  return;
		  }
		  super.onSuccessed(adViewLayout, ration);
		adViewLayout.adViewManager.resetRollover();  
		adViewLayout.rotateThreadedDelayed();
	}	

	@Override
	public void willLeaveApplication(IZPView view)
	{
		AdViewUtil.logInfo("willLeaveApplication");
	}

	@Override
	public void errorReport(IZPView view, int errorCode,String errorInfo)
	{
		AdViewUtil.logInfo("errorReport");
		view.stopAdExchange();
		view.delegate=null;
		
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		  if(adViewLayout == null) {
			 return;
		  }
		  super.onFailed(adViewLayout, ration);
		 //adViewLayout.rotateThreadedPri(1);
	}	

	@Override
	public void didStopFullScreenAd(IZPView view)
	{
		AdViewUtil.logInfo("didStopFullScreenAd");
	}



}
