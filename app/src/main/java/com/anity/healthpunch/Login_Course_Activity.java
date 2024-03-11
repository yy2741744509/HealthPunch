package com.anity.healthpunch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson2.JSONObject;

import java.io.IOException;
import java.net.Proxy;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Login_Course_Activity extends AppCompatActivity implements TextWatcher, View.OnClickListener, Callback {

    private OkHttpClient client;

    private EditText et_username, et_password;

    private Button btn_login;

    private CheckBox cb_remember;

    private SharedPreferences preferences;
    private String username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_course_login);

        client = new OkHttpClient.Builder().proxy(Proxy.NO_PROXY).build();
        et_username = findViewById(R.id.et_courseUsername);
        et_password = findViewById(R.id.et_coursePassword);
        cb_remember = findViewById(R.id.cb_courseRemember);
        btn_login = findViewById(R.id.btn_courseLogin);

        et_username.addTextChangedListener(this);
        btn_login.setOnClickListener(this);

        //该方法是静态的,可以直接调用,生成一个”config.xml“的偏好文件，且其mode为默认私有
        preferences = getSharedPreferences("courseConfig", Context.MODE_PRIVATE);

        //重载数据
        reload();
        //自动登录
        login();
    }

    @Override
    public void onClick(View v) {
        btn_login.setEnabled(false);
        if (v.getId() == R.id.btn_courseLogin) {
            login();
        }
    }

    private void reload() {
        boolean isChecked = preferences.getBoolean("courseIsChecked", false);
        if (isChecked) {
            String username = preferences.getString("courseUsername", "");
            String password = preferences.getString("coursePassword", "");
            et_username.setText(username);
            et_password.setText(password);
            cb_remember.setChecked(true);
        }
    }

    private void login() {
        username = et_username.getText().toString().trim();
        password = et_password.getText().toString().trim();
        if ("".equals(username) || "".equals(password)){
            return;
        }

        FormBody.Builder builder = new FormBody.Builder();
        builder.add("userNo", username);
        builder.add("pwd", password);
        builder.add("encode", "1");
        builder.add("captchaData", "");
        builder.add("codeVal", "");

        FormBody body = builder.build();

        String url = "http://222.243.161.213:81/hnrjzyxyhd/login";
        Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1 Edg/114.0.0.0")
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(this);

        btn_login.setEnabled(true);

    }


    @Override
    public void onResponse(Call call, Response response) throws IOException {
        String json = response.body().string();
        JSONObject jsonObject = JSONObject.parseObject(json);
        int code = Integer.parseInt(jsonObject.getString("code"));
        String msg = jsonObject.getString("Msg");
        runOnUiThread(() -> Toast.makeText(this, msg, Toast.LENGTH_SHORT).show());
        if (code == 1) {
            JSONObject data = jsonObject.getJSONObject("data");
            String token = data.getString("token");

            if (cb_remember.isChecked()) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("courseUsername", username);
                editor.putString("coursePassword", password);

                editor.putBoolean("courseIsChecked", true);
                editor.commit();
            }

            MyApplication.getInstance().token = token;

            Intent intent = new Intent(this, CourseActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);

        }
    }


    @Override
    public void onFailure(Call call, IOException e) {
        runOnUiThread(() -> Toast.makeText(this, "无法连接到服务器~", Toast.LENGTH_SHORT).show());
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        int username_length = et_username.getText().length();
        if (username_length == 12) {
            et_password.requestFocus();
        }
    }
}