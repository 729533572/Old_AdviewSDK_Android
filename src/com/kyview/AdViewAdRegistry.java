package com.kyview;

import 	java.util.HashMap;

import com.kyview.adapters.AdBaiduAdapter;
import com.kyview.adapters.AdViewAdapter;
import com.kyview.adapters.AdViewHouseAdapter;
import com.kyview.adapters.AdChinaAdapter;
import com.kyview.adapters.AdlantisAdapter;
import com.kyview.adapters.AdMobAdapter;
import com.kyview.adapters.AdTouchAdapter;
//import com.kyview.adapters.AduuInterfaceAdapter;
import com.kyview.adapters.AduuAdapter;
import com.kyview.adapters.AdwoAdapter;
import com.kyview.adapters.AirAdAdapter;
import com.kyview.adapters.AppMediaAdapter;
import com.kyview.adapters.DomobAdapter;
import com.kyview.adapters.DoubleClickAdapter;
import com.kyview.adapters.EventAdapter;
import com.kyview.adapters.FractalAdapter;
import com.kyview.adapters.GreystripeAdapter;
import com.kyview.adapters.InmobiAdapter;
import com.kyview.adapters.IzpAdapter;
import com.kyview.adapters.LmMobAdapter;
import com.kyview.adapters.LsenseAdapter;
import com.kyview.adapters.MdotMAdapter;
import com.kyview.adapters.MillennialAdapter;
import com.kyview.adapters.MobiSageAdapter;
import com.kyview.adapters.MobWinAdapter;
import com.kyview.adapters.MomarkAdapter;
import com.kyview.adapters.SmaatoAdapter;
import com.kyview.adapters.SmartAdAdapter;
import com.kyview.adapters.SuizongInterfaceAdapter;
import com.kyview.adapters.TinmooAdapter;
import com.kyview.adapters.UmengAdapter;
import com.kyview.adapters.VponAdapter;
import com.kyview.adapters.WiyunAdapter;
import com.kyview.adapters.WoobooAdapter;
import com.kyview.adapters.WqAdapter;
import com.kyview.adapters.YoumiAdapter;
import com.kyview.adapters.YunYunAdapter;
import com.kyview.adapters.ZestADZAdapter;


public class AdViewAdRegistry {
	
	private static AdViewAdRegistry mInstance = null;
	
	private HashMap<Integer, Class<? extends AdViewAdapter>> mAdapterMap = null;
	
	private AdViewAdRegistry() {
		mAdapterMap = new HashMap<Integer, Class<? extends AdViewAdapter>>();
	}
	
	public static AdViewAdRegistry getInstance() {
		if (null == mInstance) {
			mInstance = new AdViewAdRegistry();
			
			mInstance.loadAdapters();
		}
		
		return mInstance;
	}
	
	private void loadAdapters() {
		try {AdViewHouseAdapter.load(this);}catch(Error e){}
		try {AdBaiduAdapter.load(this);}catch(Error e){}
		try {AdChinaAdapter.load(this);}catch(Error e){}
		try {AdlantisAdapter.load(this);}catch(Error e){}
		try {AdMobAdapter.load(this);}catch(Error e){}
		try {AdTouchAdapter.load(this);}catch(Error e){}
		//try {AduuInterfaceAdapter.load(this);}catch(Error e){}
		try {AduuAdapter.load(this);}catch(Error e){}
		try {AdwoAdapter.load(this);}catch(Error e){}		
		try {AirAdAdapter.load(this);}catch(Error e){}
		try {AppMediaAdapter.load(this);}catch(Error e){}
		try {DomobAdapter.load(this);}catch(Error e){}
		try {DoubleClickAdapter.load(this);}catch(Error e){}
		try {EventAdapter.load(this);}catch(Error e){}
		try {FractalAdapter.load(this);}catch(Error e){}
		try {GreystripeAdapter.load(this);}catch(Error e){}
		try {InmobiAdapter.load(this);}catch(Error e){}
		try {IzpAdapter.load(this);}catch(Error e){}
		try {LmMobAdapter.load(this);}catch(Error e){}
		try {LsenseAdapter.load(this);}catch(Error e){}
		try {MdotMAdapter.load(this);}catch(Error e){}			
		try {MillennialAdapter.load(this);}catch(Error e){}
		try {MobiSageAdapter.load(this);}catch(Error e){}
		try {MobWinAdapter.load(this);}catch(Error e){}
		try {MomarkAdapter.load(this);}catch(Error e){}
		try {SmaatoAdapter.load(this);}catch(Error e){}
		try {SmartAdAdapter.load(this);}catch(Error e){}	
		try {SuizongInterfaceAdapter.load(this);}catch(Error e){}
		try {TinmooAdapter.load(this);}catch(Error e){}
		try {UmengAdapter.load(this);}catch(Error e){}
		try {VponAdapter.load(this);}catch(Error e){}
		try {WiyunAdapter.load(this);}catch(Error e){}
		try {WoobooAdapter.load(this);	}catch(Error e){}
		try {WqAdapter.load(this);}catch(Error e){}
		try {YoumiAdapter.load(this);}catch(Error e){}
		try {ZestADZAdapter.load(this);}catch(Error e){}	
		try {YunYunAdapter.load(this);}catch(Error e){}	
	
	}
	
	public void registerClass(int adType, Class<? extends AdViewAdapter> adapterClass) {
		mAdapterMap.put(Integer.valueOf(adType), adapterClass);
	}
	
	public Class<? extends AdViewAdapter> adapterClassForAdType(int adType) {
		return mAdapterMap.get(Integer.valueOf(adType));
	}
	
}
