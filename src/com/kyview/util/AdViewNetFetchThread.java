package com.kyview.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.Handler;
import android.os.Message;

public class AdViewNetFetchThread extends Thread {
    private FetchListener mEventListener;
    private String		mUrlString;
    private String		mPostString = null;
    
    private static final int MSG_TYPE_GET = 0;
    
    public static final int FETCH_OK = 0;
    public static final int FETCH_ERROR = -1;
    
    public static final int REASON_GENERAL = 0;
    public static final int REASON_NET = 1;
    
    public static final int FETCH_UI_START = 100 + 1;
    
    public static final int TYPE_TEXT = 0;
    public static final int TYPE_BINARY = 1;
    
    public static final String NetEncoding = "utf-8";
    
    public static final int	mTimeOut = 25*1000;		//25s
    
    private int mContentType = TYPE_TEXT;
    
    @SuppressWarnings("unused")
	private int mFetchId = 0;
    
    private Object mLockObj = new Object();
    
    private Object mUserObject = null;

    public void setFetchListener(FetchListener listener) {
    	synchronized(mLockObj) {
    		mEventListener = listener;
    	}
    }

    AdViewNetFetchThread(String urlString, String postStr) {
		mUrlString = urlString;
		mPostString = postStr;
	}
    
    public void setContentType(int type) {
    	mContentType = type;
    }
    
    public void setFetchId(int fId) {
    	mFetchId = fId;
    }
    
    public void setPostString(String postStr) {
    	mPostString = postStr;
    }
    
    public void setUserObject(Object obj) {
    	mUserObject = obj;
    }
    
    public Object getUserObject() {
    	return mUserObject;
    }
 	
	@Override
	public void run() {
		runFetch(mUrlString, mPostString);
	}
	
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			synchronized(mLockObj) {
				if (null == mEventListener) return;	//won't process
			
				switch (msg.what) {
				case MSG_TYPE_GET:
					mEventListener.notifyFetchStatus(AdViewNetFetchThread.this,
							msg.arg1, msg.obj);
					break;
				}
			}
		}
	};
	    
	private void sendFetchMsg(AdViewNetFetchThread fetcher, int status, Object value) {
		Message msg = new Message();
		msg.what = MSG_TYPE_GET;
		msg.arg1 = status;
		msg.arg2 = 0;
		msg.obj = value;
		mHandler.sendMessage(msg);
	}	
	
	private void notifyStatus(int status) {
		sendFetchMsg(this, status, null);
	}	
	
	private void notifyError(int reason) {
		sendFetchMsg(this, FETCH_ERROR, Integer.valueOf(reason));
	}
	
	private void runFetch(String urlString, String postData) {
        try {
        	notifyStatus(FETCH_UI_START);
        	URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            int respCode;
            if (null != conn) {
            	conn.setConnectTimeout(mTimeOut);
            	conn.setReadTimeout(mTimeOut);
            	/* if get image */
                conn.setUseCaches(false);
                
                if (null != postData) {
                	conn.setRequestMethod("POST");
                	conn.setRequestProperty("Content-Length", ""+postData.length());
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    DataOutputStream  out = new DataOutputStream(conn.getOutputStream()); 
                    out.write(postData.getBytes());
                    out.flush();
                    out.close();               	
                }
                
                respCode = conn.getResponseCode();
                if (respCode == HttpURLConnection.HTTP_OK) {
                	String typeStr = conn.getHeaderField("Content-Type");
//                	AdViewUtil.logInfo(typeStr);
                	if (typeStr.toLowerCase().contains("text")
                			|| typeStr.toLowerCase().contains("json"))
                		setContentType(TYPE_TEXT);
                	else setContentType(TYPE_BINARY);
                	/* main */
                	sendFetchMsg(this, FETCH_OK, getContentObject(conn.getInputStream()));
                	
//                	AdViewUtil.logInfo(mFetchId+"");
                } else {
                	notifyError(REASON_NET);
                }
                conn.disconnect();
            } else
            	notifyError(REASON_NET);
        } catch (IOException e) {
        	AdViewUtil.logInfo(e.toString());
        	notifyError(REASON_NET);
        }
	}
	
	public Object getContentObject(InputStream inStream) throws IOException {
		if (TYPE_BINARY == mContentType)
			return getContentData(inStream);
		return getContentString(inStream);
	}
	
	public static byte[] getContentData(InputStream inStream) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int ch = 0;
		while((ch = inStream.read()) != -1){
			baos.write(ch);
		}
		byte[] datas = baos.toByteArray();
		baos.close();
		baos = null;
		return datas;
	}
	
	public static String getContentString(InputStream inStream) throws IOException {
		StringBuilder builder = new StringBuilder(); 
        BufferedReader bufferedReader2 = new BufferedReader( 
                new InputStreamReader(inStream, NetEncoding));
        for (String s = bufferedReader2.readLine(); s != null; s = bufferedReader2 
                .readLine()) { 
            builder.append(s); 
        }
        return builder.toString();
	}	
}

interface FetchListener {
	public abstract void notifyFetchStatus(AdViewNetFetchThread fetcher, int status, Object value);
}