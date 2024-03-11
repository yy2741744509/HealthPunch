package com.anity.healthpunch;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.anity.healthpunch.utils.Md5;
import com.anity.healthpunch.utils.PermissionUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.net.URISyntaxException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener, CompoundButton.OnCheckedChangeListener, Callback, AMapLocationListener {

    //请求权限码
    private static final int REQUEST_PERMISSIONS = 9527;
    private static final int INSTALL_REQUEST_CODE = 1;
    //声明AMapLocationClient类对象
    private AMapLocationClient mLocationClient;
    //天气数据
    private StringBuilder responseWeatherData = null;

    private OkHttpClient client;

    private ListView tv_info;


    private String id = null;
    private String dk_JSESSIONID = null;
    private String token = null;
    private Double jd = null;
    private Double wd = null;
    private String sheng = null;
    private String shi = null;

    private String cityid = null;
    private String shengid = null;
    private String dkdz = null;
    private String streetDz = null;
    private static final String VERSION = "1.3.1";
    private String content;
    private String version = null;
    private LinearLayout business;
    private LinearLayout about;
    private SharedPreferences preferences;
    private ProgressDialog progressDialog;
    private Boolean versionFlag = false;
    private RadioButton mrzb, abt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initDK();

        responseWeatherData = new StringBuilder();
        client = new OkHttpClient.Builder().proxy(Proxy.NO_PROXY).build();
        //该方法是静态的,可以直接调用,生成一个”config.xml“的偏好文件，且其mode为默认私有
        preferences = getSharedPreferences("config", Context.MODE_PRIVATE);

        //检查版本更新
        //checkUpdate();
        //初始化定位
        //initLocation();

        setWeather("430302");
        //检查Android版本
        //checkingAndroidVersion();
    }

    private void initDK() {

        tv_info = findViewById(R.id.tv_info);
        business = findViewById(R.id.business);
        about = findViewById(R.id.about);
        mrzb = findViewById(R.id.mrzb);
        abt = findViewById(R.id.abt);
        //contact_qq = findViewById(R.id.contact_qq);
        TextView contact_kfqq = findViewById(R.id.contact_kfqq);

        TextView donate = findViewById(R.id.donate);
        TextView checkUpdate = findViewById(R.id.checkUpdate);
        TextView getMiwen = findViewById(R.id.getMiwen);
        TextView selectSchedule = findViewById(R.id.selectSchedule);
        TextView getScore = findViewById(R.id.getScore);
        TextView app_version = findViewById(R.id.app_version);
        app_version.setText("软件版本：V" + VERSION);

        mrzb.setOnCheckedChangeListener(this);
        abt.setOnCheckedChangeListener(this);
        //contact_qq.setOnClickListener(this);
        contact_kfqq.setOnClickListener(this);
        checkUpdate.setOnClickListener(this);
        selectSchedule.setOnClickListener(this);
        getScore.setOnClickListener(this);
        getMiwen.setOnClickListener(this);
        donate.setOnClickListener(this);

    }

    /**
     * 判断versionFlags ，最新版为true
     */
    private Boolean checkUpdate() {

        Request request = new Request.Builder().url("https://anity.cn/sd/check.txt").get().build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                showMsg("无网络连接！");
                System.exit(0);
            }

            @RequiresApi(api = Build.VERSION_CODES.R)
            @Override
            public void onResponse(Call call, Response response) {
                try {
                    String string = response.body().string();
                    version = string.substring(string.indexOf("版本《") + 3, string.indexOf("》版本"));
                    content = string.substring(string.indexOf("更新内容【") + 5, string.indexOf("】更新内容"));
                    String msg = string.substring(string.indexOf("公告[") + 3, string.indexOf("]公告"));
                    String[] notice = msg.split(",");

                    // 1代表开启公告
                    if (notice[0] != null && notice[0].equals("1")) {
                        runOnUiThread(() -> {
                            AlertDialog.Builder noticeDialog = new AlertDialog.Builder(MainActivity.this);
                            noticeDialog.setTitle("公告");
                            noticeDialog.setMessage(notice[1]);
                            noticeDialog.setPositiveButton("关闭", null);
                            noticeDialog.show();
                        });

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //如果是最新版
                if (VERSION.compareTo(version) == 0) {
                    versionFlag = true;
                    return;
                }
                runOnUiThread(() -> {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                    dialog.setTitle("提示");
                    dialog.setMessage(content);
                    dialog.setPositiveButton("更新", (dialog13, which) -> {
                        dialog13.dismiss();
                        //开始下载apk
                        //检查是否拥有 MANAGE_EXTERNAL_STORAGE 权限，没有则跳转到设置页面
                        if (!Environment.isExternalStorageManager()) {
                            Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setData(Uri.fromParts("package", getOpPackageName(), null));
                            startActivity(intent);
                        } else {
                            downloadAPK();
                        }
                    });
                    dialog.setNegativeButton("退出", (dialog1, which) -> System.exit(0));
                    //点击返回键不可取消
                    dialog.setOnKeyListener((dialog12, keyCode, event) -> keyCode == KeyEvent.KEYCODE_BACK);
                    //点击区域外不可取消
                    dialog.setCancelable(false);
                    dialog.create();
                    dialog.show();
                });
            }
        });
        return versionFlag;
    }

    private void downloadAPK() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("提示");
        progressDialog.setMessage("\n正在下载安装包，请勿退出");
        progressDialog.setMax(100);
        progressDialog.setProgress(0);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        //按返回键不可取消
        progressDialog.setOnKeyListener((dialog, keyCode, event) -> keyCode == KeyEvent.KEYCODE_BACK);
        //点击区域外不可取消
        progressDialog.setCancelable(false);
        progressDialog.create();
        progressDialog.show();

        int REQUEST_CODE_CONTACT = 101;
        String[] permissions = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS
        };
        //验证是否许可权限

        if (!PermissionUtil.checkPermission(this, permissions, REQUEST_CODE_CONTACT)) {

            //这里就是权限打开之后自己要操作的逻辑
            OkHttpClient client = new OkHttpClient.Builder().proxy(Proxy.NO_PROXY).build();
            Request request = new Request.Builder().get().url("https://anity.cn/sd/dkzs.apk").build();
            client.newCall(request).enqueue(this);
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.contact_kfqq:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin=2741744509&version=1&src_type=web&web_src=oicqzone.com")));
                break;
            case R.id.checkUpdate:
                if (checkUpdate()) {
                    showMsg("已经是最新版本啦~");
                }
                break;
            case R.id.selectSchedule:
                //TODO
                //showMsg("正在开发中~");
                Intent intent = new Intent(this, Login_Course_Activity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;

            case R.id.getScore:
                startActivity(new Intent(this, Login_Score_Activity.class));
                break;
            case R.id.donate:
                try {
                    startActivity(Intent.parseUri("intent://platformapi/startapp?saId=10000007&"
                                    + "clientVersion=3.7.0.0718&qrcode=https%3A%2F%2Fqr.alipay.com%2Ftsx08597iced3cjjlvqqcef%3F_s"
                                    + "%3Dweb-other&_t=1472443966571#Intent;"
                                    + "scheme=alipayqr;package=com.eg.android.AlipayGphone;end"
                            , Intent.URI_INTENT_SCHEME));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.getMiwen:
                startActivity(new Intent(this, GetMiwenActivity.class));
                break;
        }


    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (mrzb.isChecked()) {
            business.setVisibility(View.VISIBLE);
            about.setVisibility(View.GONE);

        } if (abt.isChecked()){
            business.setVisibility(View.GONE);
            about.setVisibility(View.VISIBLE);
        }
    }

    private void initTextList() {
        String url = "http://excerpt.rubaoo.com/toolman/getMiniNews";
        Request getRequest = new Request.Builder().url(url).get().build();
        Call call = client.newCall(getRequest);
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        String url = call.request().url().toString();

        if (url.contains("Validate")) {


        }else if (url.contains("https://restapi.amap.com")) {
            String json = response.body().string();

            try {
                JSONObject object = new JSONObject(json);
                String infocode = object.getString("infocode");

                //返回状态说明,10000代表正确
                if ("10000".equals(infocode)) {
                    JSONArray lives = object.getJSONArray("lives");
                    JSONObject arrayObject = lives.getJSONObject(0);

                    String weather = arrayObject.getString("weather");
                    String temperature = arrayObject.getString("temperature");
                    String winddirection = arrayObject.getString("winddirection");
                    String humidity = arrayObject.getString("humidity");
                    String reporttime = arrayObject.getString("reporttime");

                    responseWeatherData.append("天气：").append(weather).append("\n");
                    responseWeatherData.append("实时气温：").append(temperature).append("℃").append("\n");
                    responseWeatherData.append("风向：").append(winddirection).append("\n");
                    responseWeatherData.append("空气湿度：").append(humidity).append("\n");
                    responseWeatherData.append("发布时间：").append(reporttime);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                initTextList();
            }


        } else if ("http://excerpt.rubaoo.com/toolman/getMiniNews".equals(url)) {
            String string = response.body().string();
            try {
                JSONObject jsonObject = new JSONObject(new JSONTokener(string));
                JSONObject data = jsonObject.getJSONObject("data");
                JSONArray jsonArray = data.getJSONArray("news");
                String weiyu = data.getString("weiyu");
                int length = jsonArray.length();

                String[] array = new String[length + 2];
                array[0] = responseWeatherData.toString();
                array[1] = weiyu;
                for (int i = 0; i < length; i++) {
                    array[i + 2] = jsonArray.getString(i);
                }
                runOnUiThread(() -> tv_info.setAdapter(new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, array)));
            } catch (JSONException e) {
                e.printStackTrace();
            }


        } else if (url.contains(".apk") && url.contains("anity")) {
            //保存文件的本地路径
            String fileSavePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";

            ResponseBody responseBody = response.body();
            long length = responseBody.contentLength();

            InputStream is = responseBody.byteStream();

            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File dir = new File(fileSavePath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
            }

            File apkFile = new File(fileSavePath, "校园助手.apk");
            if (apkFile.exists()) {
                apkFile.deleteOnExit();
            }
            FileOutputStream fos = new FileOutputStream(apkFile);

            int count = 0;
            byte[] buffer = new byte[1024];
            int numread;

            while ((numread = is.read(buffer)) != -1) {
                count += numread;
                // 计算进度条当前位置
                // 更新进度条
                progressDialog.setProgress((int) (((float) count / length) * 100));

                fos.write(buffer, 0, numread);
            }
            runOnUiThread(() -> Toast.makeText(this, "安装包位置:/Download/校园助手.apk", Toast.LENGTH_LONG).show());
            fos.close();
            is.close();

            //下载完后安装APK

            //Android 11 之后获取MANAGE_EXTERNAL_STORAGE 权限
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                checkAndInstall();
            } else {
                installAPK();
            }

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private void checkAndInstall() {
        //检查是否拥有 MANAGE_EXTERNAL_STORAGE 权限，没有则跳转到设置页面
        if (!Environment.isExternalStorageManager()) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.fromParts("package", getOpPackageName(), null));
            startActivity(intent);
        } else {
            //如果有权限，直接安装，没有权限则获取权限
            if (PermissionUtil.checkPermission(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, INSTALL_REQUEST_CODE)) {
                installAPK();
            }
        }
    }

    @Override
    public void onFailure(Call call, IOException e) {
        runOnUiThread(() -> showMsg("哎呀，网络跑丢了！"));
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }


    /**
     * 初始化定位
     */
    private void initLocation() {
        //初始化定位
        try {
            Context context = getApplicationContext();
            AMapLocationClient.updatePrivacyShow(context, true, true);
            AMapLocationClient.updatePrivacyAgree(context, true);
            mLocationClient = new AMapLocationClient(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mLocationClient != null) {

            //设置定位回调监听
            mLocationClient.setLocationListener(this);

            //初始化AMapLocationClientOption对象
            AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
            //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //获取最近3s内精度最高的一次定位结果：
            //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
            mLocationOption.setOnceLocationLatest(true);
            //设置是否返回地址信息（默认返回地址信息）
            mLocationOption.setNeedAddress(true);
            //设置定位请求超时时间，单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
            mLocationOption.setHttpTimeOut(20000);
            //关闭缓存机制，高精度定位会产生缓存。
            mLocationOption.setLocationCacheEnable(false);

            //给定位客户端对象设置定位参数
            mLocationClient.setLocationOption(mLocationOption);

        }
    }


    /**
     * 动态请求权限
     */
    @AfterPermissionGranted(REQUEST_PERMISSIONS)
    private void requestPermission() {
        String[] permissions = {
                //允许程序通过访问网络来大致确定自己设备的位置
                Manifest.permission.ACCESS_COARSE_LOCATION,
                //允许通过访问信息源来精确的获得设备的地理位置
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        if (EasyPermissions.hasPermissions(this, permissions)) {
            //true 有权限 开始定位

            //启动定位
            mLocationClient.startLocation();

        } else {
            //false 无权限
            EasyPermissions.requestPermissions(this, "打卡需要权限，拒绝后可能无法正常使用APP", REQUEST_PERMISSIONS, permissions);
        }
    }


    /**
     * 请求权限结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_PERMISSIONS:
                //设置权限请求结果
                EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
                break;
            case INSTALL_REQUEST_CODE:
                if (PermissionUtil.checkGrant(grantResults)) {
                    installAPK();
                }
                break;
        }
    }


    /**
     * 检查Android版本
     */
    private void checkingAndroidVersion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Android6.0及以上先获取权限再定位
            requestPermission();
        } else {
            //Android6.0以下直接定位
            mLocationClient.startLocation();
        }
    }


    /**
     * 接收异步返回的定位结果
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //区域编码
                cityid = aMapLocation.getAdCode();
                shengid = cityid.substring(0, 2) + "0000";
                jd = aMapLocation.getLongitude();
                wd = aMapLocation.getLatitude();
                sheng = aMapLocation.getProvince();
                shi = aMapLocation.getCity();
                String jdName = aMapLocation.getStreet();
                dkdz = aMapLocation.getAddress();
                streetDz = jdName + aMapLocation.getStreetNum();
                //地址
                responseWeatherData.append("地址：").append(dkdz).append("\n");


                //设置天气
                setWeather(cityid);

            } else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
                responseWeatherData.append(aMapLocation.getErrorInfo());

            }
            //停止定位后，本地定位服务并不会被销毁
            mLocationClient.stopLocation();

        }
    }


    /**
     * 天气查询
     */
    private void setWeather(String city) {
        String url = "https://restapi.amap.com/v3/weather/weatherInfo?key=dae59eaea9d0fdbb57399c52330dd10f&city=" + city + "&extensions=base&outputJSON";
        Request request = new Request.Builder().url(url).build();

        Call call = client.newCall(request);
        call.enqueue(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁定位客户端，同时销毁本地定位服务。
        mLocationClient.onDestroy();
        mLocationClient = null;

    }


    private void installAPK() {
        String apkPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/校园助手.apk";
        //获取应用包管理器
        PackageManager pm = getPackageManager();
        //获取apk文件的包信息
        PackageInfo packageArchiveInfo = pm.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);
        if (packageArchiveInfo == null) {
            showMsg("安装包已损坏！");
            return;
        }
        //installer
        Uri uri = Uri.parse(apkPath);
        //兼容Android7.0，把访问文件的Uri方式改为FileProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //通过FileProvider 获得文件的Uri访问方式
            uri = FileProvider.getUriForFile(this, getString(R.string.file_provider), new File(apkPath));
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //Intent 的接受者将被准许读取Intent 携带的URI数据
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        //设置Uri的数据类型为APK文件
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        //启动系统自带的应用安装程序
        startActivity(intent);

    }


    /**
     * Toast提示
     *
     * @param msg 提示内容
     */
    private void showMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }
}