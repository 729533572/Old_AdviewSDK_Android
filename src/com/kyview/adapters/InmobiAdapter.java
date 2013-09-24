package com.kyview.adapters;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;

import com.inmobi.androidsdk.IMAdListener;
import com.inmobi.androidsdk.IMAdRequest;
import com.inmobi.androidsdk.IMAdRequest.ErrorCode;
import com.inmobi.androidsdk.IMAdView;
import com.kyview.AdViewAdRegistry;
import com.kyview.AdViewLayout;
import com.kyview.AdViewLayout.ViewAdRunnable;
import com.kyview.obj.Ration;
import com.kyview.util.AdViewUtil;
//import android.view.ViewGroup.LayoutParams;
//import android.widget.RelativeLayout;

public class InmobiAdapter extends AdViewAdapter  implements IMAdListener{
	private IMAdRequest mAdRequest;
	
	private static int networkType() {
		return AdViewUtil.NETWORK_TYPE_INMOBI;
	}
	
	public static void load(AdViewAdRegistry registry) {
		try {
			if(Class.forName("com.inmobi.androidsdk.IMAdView") != null) {
			
				registry.registerClass(networkType(), InmobiAdapter.class);
			}
		} catch (ClassNotFoundException e) {}
	}

	public InmobiAdapter() {
	}
	
	@Override
	public void initAdapter(AdViewLayout adViewLayout, Ration ration) {
		// TODO Auto-generated constructor stub
	}


	@Override
	public void handle() {
		// TODO Auto-generated method stub
		AdViewUtil.logInfo("Into Inmobi");
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null) {
			return;
		}

		Activity activity = adViewLayout.activityReference.get();
		if (activity == null) {
			return;
		}
		// set the test mode to true (Make sure you set the test mode to false
		// when distributing to the users)
		IMAdView mIMAdView = new IMAdView(activity, IMAdView.INMOBI_AD_UNIT_320X50, ration.key);
		mAdRequest = new IMAdRequest();
		Map<String,String> reqParams = new HashMap<String,String>();
		reqParams.put("tp","c_adview");
		mAdRequest.setRequestParams(reqParams);



		mIMAdView.setIMAdRequest(mAdRequest);
		mIMAdView.setIMAdListener(this);
		mIMAdView.loadNewAd(mAdRequest);
		//adViewLayout.adViewManager.resetRollover(); 
		//adViewLayout.rotateThreadedDelayed();

	}

	@Override
	public void onAdRequestCompleted(IMAdView arg0) {
		AdViewUtil.logInfo("InMobi success");
		arg0.setIMAdListener(null);
		
		  AdViewLayout adViewLayout = adViewLayoutReference.get();
		  if(adViewLayout == null) 
			  return;
		  super.onSuccessed(adViewLayout, ration);
		  adViewLayout.adViewManager.resetRollover();
		  adViewLayout.handler.post(new ViewAdRunnable(adViewLayout, arg0));
		  adViewLayout.rotateThreadedDelayed(); 		
	}

	@Override
	public void onAdRequestFailed(IMAdView arg0, ErrorCode arg1) {
		AdViewUtil.logInfo("ImMobi failure, errorCode="+arg1);
		arg0.setIMAdListener(null);

		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if(adViewLayout == null) 
			return; 
		super.onFailed(adViewLayout, ration);
		//adViewLayout.rotateThreadedPri(1);		
	}

	@Override
	public void onDismissAdScreen(IMAdView arg0) {
		AdViewUtil.logInfo("ImMobi, onDismissAdScreen");		
	}

	@Override
	public void onLeaveApplication(IMAdView arg0) {
		AdViewUtil.logInfo("ImMobi, onLeaveApplication");		
	}

	@Override
	public void onShowAdScreen(IMAdView arg0) {
		AdViewUtil.logInfo("ImMobi, onShowAdScreen");		
	}



}
