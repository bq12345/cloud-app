package org.bq.fragement;


import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.bq.yun.R;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class DeviceFragment extends Fragment {
    private ListView lv;
    private LocationManager locationManager = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RelativeLayout view = (RelativeLayout) inflater.inflate(R.layout.device, null);
        TextView device_uuid = (TextView) view.findViewById(R.id.device_uuid);
        TextView device_hard = (TextView) view.findViewById(R.id.device_hard);
        TextView device_net = (TextView) view.findViewById(R.id.device_net);
        TextView device_display = (TextView) view.findViewById(R.id.device_display);
        TextView device_location = (TextView) view.findViewById(R.id.device_location);
        device_uuid.setText("UUID:" + Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID));
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        try {
            device_hard.setText("设备名:" + getMobileInfo().getString("BOARD")
                    + "\n总内存:" + getTotalMemory() + "M\n当前可用:" + formatSize(getAvailMemory()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        device_net.setText("运营商:" + getProvidersName());
        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;//屏幕宽度[像素]
        int height = metric.heightPixels;//屏幕高度[像素]
        float density = metric.density;//屏幕密度[0.75|1.0|1.5]
        int densityDpi = metric.densityDpi;//屏幕密度[120|160|240]
        device_display.setText("屏幕宽度：" + String.valueOf(width) + "像素\n" + "屏幕高度：" + String.valueOf(height) + "像素\n" + "屏幕密度：" + String.valueOf(density) + "\n" + "屏幕密度：" + String.valueOf(densityDpi));
        setLocation(device_location);
        return view;
    }

    private void setLocation(final TextView tv) {
        tv.setText("正在获取定位信息...");

        final LocationListener locationListener = new LocationListener() {
            // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            // Provider被enable时触发此函数，比如GPS被打开
            @Override
            public void onProviderEnabled(String provider) {

            }

            // Provider被disable时触发此函数，比如GPS被关闭
            @Override
            public void onProviderDisabled(String provider) {

            }

            //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
            @Override
            public void onLocationChanged(final Location location) {
                if (location != null) {
                    tv.setText("当前位置 :\n 纬度: "
                            + location.getLatitude() + "\n 经度: "
                            + location.getLongitude());
                } else {
                    tv.setText("暂时无法获得当前位置");
                }
            }
        };
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);
    }

    public String getProvidersName() {
        String ProvidersName = null;
        TelephonyManager telephonyManager = (TelephonyManager) getActivity()
                .getSystemService(Context.TELEPHONY_SERVICE);
        String IMSI = telephonyManager.getSubscriberId();
        // IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。  
        if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
            ProvidersName = "中国移动";
        } else if (IMSI.startsWith("46001")) {
            ProvidersName = "中国联通";
        } else if (IMSI.startsWith("46003")) {
            ProvidersName = "中国电信";
        }
        return ProvidersName;
    }


    public String formatSize(long size) {
        String suffix = null;
        float fSize = 0;

        if (size >= 1024) {
            suffix = "K";
            fSize = size / 1024;
            if (fSize >= 1024) {
                suffix = "M";
                fSize /= 1024;
            }
            if (fSize >= 1024) {
                suffix = "G";
                fSize /= 1024;
            }
        } else {
            fSize = size;
        }
        java.text.DecimalFormat df = new java.text.DecimalFormat("#0.00");
        StringBuilder resultBuffer = new StringBuilder(df.format(fSize));
        if (suffix != null)
            resultBuffer.append(suffix);
        return resultBuffer.toString();
    }

    public long getAvailMemory() {
        ActivityManager am = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        return mi.availMem;
    }


    public JSONObject getMobileInfo() {
        //StringBuffer sb = new StringBuffer();
        JSONObject mbInfo = new JSONObject();

        //通过反射获取用户硬件信息
        try {

            Field[] fields = Build.class.getDeclaredFields();
            for (Field field : fields) {
                // 暴力反射,获取私有信息
                field.setAccessible(true);
                String name = field.getName();
                String value = field.get(null).toString();
                //sb.append(name + "=" + value);
                //sb.append("\n");
                mbInfo.put(name, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mbInfo;
    }

    /**
     * 获取设备内存大小值
     *
     * @return 内存大小, 单位M
     */
    public static long getTotalMemory() {
        String str1 = "/proc/meminfo";
        String str2;
        String[] arrayOfString;
        long initial_memory = 0;
        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
            str2 = localBufferedReader.readLine();
            if (str2 != null) {
                arrayOfString = str2.split("\\s+");
                initial_memory = Integer.valueOf(arrayOfString[1]).intValue() / 1024;
            }
            localBufferedReader.close();
            return initial_memory;
        } catch (IOException e) {
            return -1;
        }
    }

}

