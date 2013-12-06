package com.kyview.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.kyview.AdViewTargeting;
import com.kyview.AdViewTargeting.RunMode;
import com.kyview.statistics.LogInterface;
import com.kyview.statistics.StatisticsBean;

public class AdViewUtil {
	public static final boolean TEST_SERVER = false; // false
	public static int configVer = 0;
	public static final String COUNTSHOW = "show";
	public static final String COUNTREQUEST = "req";
	public static final String COUNTCLICK = "click";
	public static final String COUNTFAIL = "fail";
	public static final String COUNTSUCCESS = "suc";

	public static final int REQUESTTIMEOUT = 25 * 1000;

	public static final String UPLOADREQ_TIME = "uploadreq_time";
	public static final String UPLOADINTERVAL_TIME = "uploadinterval_time";
	public static final String NEXTUPLOAD_TIME = "nextupload_time";

	public final static String PREFS_STRING_MULTI = "multi";
	public final static String PREFS_STRING_TIMESTAMP = "timestamp";

	public static int adfill_count = 0;
	public static int common_count = 0;
	public static double adfill_precent = 0.1D;

	public static String SERVER_HOST = "report.adview.cn";
	public static String CONFIG_HOST = "config.adview.cn";

	public static String TESTSERVER_HOST = "test2012.adview.cn";// 124.207.233.119";

	public static List<StatisticsBean> statisticsList = null;

	public static StringBuilder sb = null;
	private static LogInterface logInterface = null;

	// public static final String urlConfig =
	// "http://config.adview.cn/agent/agent1_android.php?appid=%s&appver=%d&client=0&simulator=%d&location=%s&time=%d&sdkver=%s";

	public static String urlConfig = null;
	public static String urlImpression = null;
	public static String urlClick = null;
	public static String appReport = null;

	// Don't change anything below this line
	/***********************************************/

	public static final int VERSION = 204;

	public static final String ADVIEW = "AdView SDK v2.0.4";
	public static final String ADVIEW4YUNYUN = "FRADVIEW_2.0.4";
	public static final String ADVIEW_VER = "2.0.4";

	// Could be an enum, but this gives us a slight performance improvement
	// abroad
	public static final int NETWORK_TYPE_ADFILL = 997;
	public static final int NETWORK_TYPE_ADBID = 998;

	public static final int NETWORK_TYPE_ADMOB = 1;
	public static final int NETWORK_TYPE_GREYSTRIP = 2;
	public static final int NETWORK_TYPE_INMOBI = 3;
	public static final int NETWORK_TYPE_MDOTM = 4;
	public static final int NETWORK_TYPE_ZESTADZ = 5;
	public static final int NETWORK_TYPE_MILLENNIAL = 6;
	public static final int NETWORK_TYPE_SMAATO = 7;

	// the below is china
	public static final int NETWORK_TYPE_WOOBOO = 21;
	public static final int NETWORK_TYPE_YOUMI = 22;
	public static final int NETWORK_TYPE_KUAIYOU = 23;
	public static final int NETWORK_TYPE_CASEE = 24;
	public static final int NETWORK_TYPE_WIYUN = 25;
	public static final int NETWORK_TYPE_ADCHINA = 26;
	public static final int NETWORK_TYPE_ADVIEWAD = 28;
	public static final int NETWORK_TYPE_SMARTAD = 29;
	public static final int NETWORK_TYPE_DOMOB = 30;
	public static final int NETWORK_TYPE_VPON = 31;
	public static final int NETWORK_TYPE_ADTOUCH = 32;
	public static final int NETWORK_TYPE_ADWO = 33;
	public static final int NETWORK_TYPE_AIRAD = 34;
	public static final int NETWORK_TYPE_WQ = 35;
	public static final int NETWORK_TYPE_APPMEDIA = 36;
	public static final int NETWORK_TYPE_TINMOO = 37;
	public static final int NETWORK_TYPE_BAIDU = 38;
	public static final int NETWORK_TYPE_LSENSE = 39;
	public static final int NETWORK_TYPE_IZPTEC = 41;
	public static final int NETWORK_TYPE_ADSAGE = 42;
	public static final int NETWORK_TYPE_UMENG = 43;
	public static final int NETWORK_TYPE_FRACTAL = 44;
	public static final int NETWORK_TYPE_LMMOB = 45;
	public static final int NETWORK_TYPE_MOBWIN = 46;
	public static final int NETWORK_TYPE_SUIZONG = 47;
	public static final int NETWORK_TYPE_ADUU = 48;
	public static final int NETWORK_TYPE_MOMARK = 49;
	// public static final int NETWORK_TYPE_YINGGAO=40;
	public static final int NETWORK_TYPE_YUNYUN = 53;
	// public static final int NETWORK_TYPE_YJF=54;
	public static final int NETWORK_TYPE_PUNCHBOX = 57;

	public static final int NETWORK_TYPE_ZHIDIAN = 58;

	public static final int NETWORK_TYPE_CUSTOMIZE = 999;

	public static final int NETWORK_TYPE_DoubleClick = 51;
	public static final int NETWORK_TYPE_ADLANTIS = 52;
	private static int[] widthAndHeight;
	private static double mDensity = -1.0D;

	public static void initConfigUrls(String reportHost) {
		urlConfig = "http://"
				+ CONFIG_HOST
				+ "/agent/agent1_android.php?appid=%s&appver=%s&client=0&simulator=%d&location=%s&time=%d&sdkver=%d";

		urlImpression = "http://"
				+ reportHost
				+ "/agent/agent2.php?appid=%s&nid=%s&type=%d&uuid=%s&country_code=%s&appver=%s&client=0&simulator=%d&keydev=%s&time=%s&sdkver=%d&configVer=%s";

		urlClick = "http://"
				+ reportHost
				+ "/agent/agent3.php?appid=%s&nid=%s&type=%d&uuid=%s&country_code=%s&appver=%s&client=0&simulator=%d&keydev=%s&time=%s&sdkver=%d&configVer=%s";
		appReport = "http://"
				+ reportHost
				+ "/agent/appReport.php?keyAdView=%s&keyDev=%s&typeDev=%s&osVer=%s&resolution=%s&servicePro=%s&netType=%s&channel=%s&platform=%s&time=%s&sdkver=%d&configVer=%s";

	}

	static {
		if (TEST_SERVER) {
			SERVER_HOST = TESTSERVER_HOST;
			CONFIG_HOST = TESTSERVER_HOST;
		}

		initConfigUrls(SERVER_HOST);
	}

	public static long currentSecond() {
		return System.currentTimeMillis() / 1000;
	}

	public static double getDensity(Activity activity) {
		if (mDensity == -1.0D) {
			try {
				int sdkVersion = activity.getPackageManager()
						.getApplicationInfo(activity.getPackageName(), 0).targetSdkVersion;
				if (sdkVersion < 4) {
					mDensity = 1.0D;
				} else {
					DisplayMetrics displayMetrics = new DisplayMetrics();
					activity.getWindowManager().getDefaultDisplay()
							.getMetrics(displayMetrics);
					mDensity = displayMetrics.density;
				}
			} catch (Exception e) {
				logError("", e);
				mDensity = 1.0D;
			}
		}

		return mDensity;
	}

	public static String convertToHex(byte[] data) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			int halfbyte = (data[i] >>> 4) & 0x0F;
			int two_halfs = 0;
			do {
				if ((0 <= halfbyte) && (halfbyte <= 9))
					buf.append((char) ('0' + halfbyte));
				else
					buf.append((char) ('a' + (halfbyte - 10)));
				halfbyte = data[i] & 0x0F;
			} while (two_halfs++ < 1);
		}
		return buf.toString();
	}

	public static int convertToScreenPixels(int dipPixels, double density) {
		double pix = 0;

		pix = density > 0.0D ? dipPixels * density : dipPixels;

		return (int) pix;
	}

	public static boolean checkPermission(Context context, String paramString) {
		PackageManager localPackageManager = context.getPackageManager();
		return localPackageManager.checkPermission(paramString,
				context.getPackageName()) == 0;
	}

	public static String getImsi(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService("phone");
		StringBuffer ImsiStr = new StringBuffer();
		try {
			if (checkPermission(context, "android.permission.READ_PHONE_STATE")) {
				ImsiStr.append(tm.getSubscriberId() == null ? "" : tm
						.getSubscriberId());
			}
			while (ImsiStr.length() < 15)
				ImsiStr.append("0");
		} catch (Exception e) {
			logError("", e);
			ImsiStr.append("000000000000000");
		}
		return ImsiStr.toString();
	}

	public static String getImei(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService("phone");
		StringBuffer tmDevice = new StringBuffer();
		try {
			if (checkPermission(context, "android.permission.READ_PHONE_STATE")) {
				tmDevice.append(tm.getDeviceId());
			}
			while (tmDevice.length() < 15)
				tmDevice.append("0");
		} catch (Exception e) {
			logError("", e);
		}

		return tmDevice.toString().replace("null", "0000");
	}

	public static String getIDByMAC(Context context) {
		String str = null;
		try {
			WifiManager localWifiManager = (WifiManager) context
					.getSystemService("wifi");
			WifiInfo localWifiInfo = localWifiManager.getConnectionInfo();
			str = localWifiInfo.getMacAddress();
		} catch (Exception e) {
			logError("", e);
		}
		return str;
	}

	public static String getAduuNetworkType(Context context) {
		String networkType = "0";
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService("connectivity");
		NetworkInfo networkinfo = connManager.getActiveNetworkInfo();

		if (networkinfo != null) {
			networkType = networkinfo.getTypeName();
			if (networkType.equalsIgnoreCase("wifi")) {
				networkType = "4";
			} else {
				String IMSI = getImsi(context);
				if ((IMSI.startsWith("46000")) || (IMSI.startsWith("46002")))
					networkType = "1";
				else if (IMSI.startsWith("46001"))
					networkType = "2";
				else if (IMSI.startsWith("46003"))
					networkType = "3";
			}
		} else {
			String IMSI = getImsi(context);
			if ((IMSI.startsWith("46000")) || (IMSI.startsWith("46002")))
				networkType = "1";
			else if (IMSI.startsWith("46001"))
				networkType = "2";
			else if (IMSI.startsWith("46003")) {
				networkType = "3";
			}
		}
		return networkType;
	}

	public static int[] getWidthAndHeight(Context context) {
		// private static int[] widthAndHeight;

		if ((widthAndHeight != null) && (widthAndHeight.length > 1)) {
			return widthAndHeight;
		}

		WindowManager WM = (WindowManager) context.getSystemService("window");
		int width = WM.getDefaultDisplay().getWidth();
		int height = WM.getDefaultDisplay().getHeight();
		if (height > width)
			widthAndHeight = new int[] { width, height };
		else {
			widthAndHeight = new int[] { height, width };
		}
		return widthAndHeight;
	}

	static {
		Log.i(AdViewUtil.class.getSimpleName(), "static");
	}

	/****************** Android的五种logcat输出的其中四种 *****************************/
	public static void logWarn(String info, Throwable r) {
		if (AdViewTargeting.getRunMode() == RunMode.TEST)
			Log.w(AdViewUtil.ADVIEW, info, r);
		if (null == sb)
			sb = new StringBuilder();
		sb.append(r.toString() + "\n");
		if (null != logInterface)
			logInterface.onLogChange(sb);
	}

	public static void logDebug(String info) {
		if (AdViewTargeting.getRunMode() == RunMode.TEST)
			Log.d(AdViewUtil.ADVIEW, info);
		if (null == sb)
			sb = new StringBuilder();
		sb.append(info + "\n");
		if (null != logInterface)
			logInterface.onLogChange(sb);

	}

	public static void logError(String info, Throwable r) {
		if (AdViewTargeting.getRunMode() == RunMode.TEST)
			Log.e(AdViewUtil.ADVIEW, info, r);
		if (null == sb)
			sb = new StringBuilder();
		sb.append(r.toString() + "\n");
		if (null != logInterface)
			logInterface.onLogChange(sb);
	}

	public static void logInfo(String info) {
		if (AdViewTargeting.getRunMode() == RunMode.TEST)
			Log.i(AdViewUtil.ADVIEW, info);
		if (null == sb)
			sb = new StringBuilder();
		sb.append(info + "\n");
		if (null != logInterface)
			logInterface.onLogChange(sb);
	}

	public static void setLogInterface(LogInterface logInterface) {
		AdViewUtil.logInterface = logInterface;
	}

	public static void writeLogtoFile(String logName, boolean isPrintTime,
			String text) {
		Date nowtime = new Date();
		SimpleDateFormat dateformat1 = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss.SSS");
		String needWriteFiel = dateformat1.format(nowtime);
		if (!isPrintTime)
			needWriteFiel = "";
		String needWriteMessage = needWriteFiel + " " + text;
		FileWriter filerWriter = null;
		BufferedWriter bufWriter = null;
		File file = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + File.separator + "adview_log");
		if (!file.exists())
			file.mkdirs();
		try {
			File files = new File(Environment.getExternalStorageDirectory()
					.getAbsolutePath()
					+ File.separator
					+ "adview_log"
					+ File.separator + logName + ".txt");
			if (!files.exists())
				files.createNewFile();
			// 后面这个参数代表是不是要接上文件中原来的数据，不进行覆盖
			filerWriter = new FileWriter(files, true);
			bufWriter = new BufferedWriter(filerWriter);
			bufWriter.write(needWriteMessage);
			bufWriter.newLine();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bufWriter != null)
					bufWriter.close();
				if (filerWriter != null)
					filerWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public static void storeInfo(Context context, String keyAdView,
			int rationType, String typeName) {
		String perfsMulti = null;
		boolean isfound = false;
		JSONArray jsonArray = null;
		JSONObject jsonMap = null;
		SharedPreferences adViewPrefs = context.getSharedPreferences(keyAdView,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = adViewPrefs.edit();
		try {
			perfsMulti = adViewPrefs.getString(PREFS_STRING_MULTI, null);
			if (perfsMulti == null) {
				jsonMap = new JSONObject();
				jsonMap.put("type", rationType);
				jsonMap.put(typeName, 1);
			} else {
				jsonArray = new JSONArray(perfsMulti);
				for (int i = 0; i < jsonArray.length(); i++) {
					jsonMap = jsonArray.getJSONObject(i);
					if (jsonMap.getInt("type") == rationType) {
						if (jsonMap.has(typeName))
							jsonMap.put(typeName,
									(jsonMap.getInt(typeName) + 1));
						else
							jsonMap.put(typeName, 1);
						isfound = true;
						break;
					}
				}
				if (!isfound) {
					jsonMap = new JSONObject();
					jsonMap.put("type", rationType);
					jsonMap.put(typeName, 1);
					jsonArray.put(jsonMap);
				}
			}
			if (null == jsonArray) {
				jsonArray = new JSONArray();
				jsonArray.put(jsonMap);
			}
			perfsMulti = jsonArray.toString();
			editor.putString(PREFS_STRING_MULTI, perfsMulti);
			editor.commit();
		} catch (JSONException e) {
			logError("JSONException", e);
		}

	}

	// public static byte[] fileConnect(InputStream is) {
	// try {
	// ByteArrayOutputStream baos = new ByteArrayOutputStream();
	// int ch = 0;
	// while ((ch = is.read()) != -1) {
	// baos.write(ch);
	// }
	// byte[] datas = baos.toByteArray();
	// baos.close();
	// baos = null;
	// is.close();
	// is = null;
	// return datas;
	// } catch (Exception e) {
	// return null;
	// }
	// }

	public static InputStream getInputStream(Context context, String url) {
		URL myFileUrl = null;
		InputStream is = null;
		try {
			myFileUrl = new URL(url);
			long modifysince = 0;
			SharedPreferences imagemodifysince = context.getSharedPreferences(
					"imagemodifysince", Context.MODE_PRIVATE);

			int pos = url.lastIndexOf("/");
			String filename = url.substring(pos + 1);
			File updateDir = null;
			File updateFile = null;
			updateDir = new File(Environment.getExternalStorageDirectory(),
					"Adview/ad/");
			updateFile = new File(updateDir.getPath(), filename);

			if ("mounted".equals(Environment.getExternalStorageState())) {
				if (updateDir.exists()) {
					if (updateFile.exists()) {
						modifysince = imagemodifysince.getLong(filename, 0);
					}
				} else {
					updateDir.mkdirs();
				}
			}

			HttpURLConnection conn = (HttpURLConnection) myFileUrl
					.openConnection();
			if (modifysince > 0)
				conn.setIfModifiedSince(modifysince);
			conn.setDoInput(true);
			conn.setConnectTimeout(REQUESTTIMEOUT);
			conn.setReadTimeout(REQUESTTIMEOUT);

			conn.connect();

			int respCode;
			respCode = conn.getResponseCode();

			long lastModify;
			lastModify = conn.getLastModified();

			if (respCode == 304) {
				is = new FileInputStream(updateFile);
			} else if (respCode == 200) {
				is = conn.getInputStream();

				if (updateDir.exists()) {
					SharedPreferences.Editor editor = imagemodifysince.edit();
					editor.putLong(filename, lastModify);
					editor.commit();

					FileOutputStream fileOutputStream = null;
					fileOutputStream = new FileOutputStream(updateFile);

					byte[] buf = new byte[1024 * 8];
					int ch = -1;

					int count = 0;
					while ((ch = is.read(buf)) != -1) {
						fileOutputStream.write(buf, 0, ch);
						count += ch;
					}
					fileOutputStream.flush();
					fileOutputStream.close();

					is = new FileInputStream(updateFile);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return is;
	}

}