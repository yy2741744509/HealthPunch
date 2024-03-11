package com.anity.healthpunch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.anity.healthpunch.adapter.ScoreAdapter;
import com.anity.healthpunch.entity.Score;
import com.anity.healthpunch.entity.Semester;

import java.io.IOException;
import java.net.Proxy;
import java.util.List;
import java.util.stream.Collectors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ScoreActivity extends AppCompatActivity implements Callback {

    private ListView listView;

    private Spinner spinner;

    private static List<String> semesterList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        spinner = findViewById(R.id.score_spinner);
        listView = findViewById(R.id.score_listView);

        getSemester();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String s = semesterList.get(position);
                getScore(s);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void getScore(String xueqi) {
        OkHttpClient client = new OkHttpClient.Builder().proxy(Proxy.NO_PROXY).build();
        String url = "http://222.243.161.213:81/hnrjzyxyhd/student/termGPA?semester=" + xueqi + "&type=1";

        FormBody.Builder form = new FormBody.Builder();
        form.add("semester", xueqi);
        form.add("type", "1");

        Request request = new Request.Builder().post(form.build()).url(url)
                .addHeader("token", MyApplication.getInstance().token)
                .build();

        client.newCall(request).enqueue(this);

    }

    private void getSemester() {
        OkHttpClient client = new OkHttpClient.Builder().proxy(Proxy.NO_PROXY).build();
        String url = "http://222.243.161.213:81/hnrjzyxyhd/semesterList";

        FormBody.Builder form = new FormBody.Builder();

        Request request = new Request.Builder().post(form.build()).url(url)
                .addHeader("token", MyApplication.getInstance().token)
                .build();

        client.newCall(request).enqueue(this);
    }

    @Override
    public void onFailure(Call call, IOException e) {

    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        if (response.request().url().toString().contains("semesterList")) {
            String semester_data = response.body().string();

            JSONObject jsonObject = JSON.parseObject(semester_data);
            String data = jsonObject.getString("data");

            List<Semester> semesters = JSON.parseArray(data, Semester.class);
            semesterList = semesters.stream().map(Semester::getSemesterId).collect(Collectors.toList());
            getScore(semesterList.get(1));

            runOnUiThread(() -> {
                        spinner.setAdapter(new ArrayAdapter(ScoreActivity.this, android.R.layout.simple_list_item_1, semesterList));
                        spinner.setSelection(1);
                    }
            );

        } else {

            String kb_data = response.body().string();

            com.alibaba.fastjson2.JSONObject jsonObject = JSON.parseObject(kb_data);
            String Msg = jsonObject.getString("Msg");
            List<Score> scoreList = null;

            if (!Msg.equals("success")) {
                Score temp = new Score();
                temp.setCourseName("空");
                temp.setFraction("空");
                scoreList.add(temp);
                return;
            }

            com.alibaba.fastjson2.JSONArray data = jsonObject.getJSONArray("data");
            com.alibaba.fastjson2.JSONObject o = (com.alibaba.fastjson2.JSONObject) data.get(0);
            String scores = o.getString("achievement");

            scoreList = JSON.parseArray(scores, Score.class);

            List<Score> finalScoreList = scoreList;
            runOnUiThread(() -> {
                ScoreAdapter adapter = new ScoreAdapter(ScoreActivity.this, finalScoreList);
                listView.setAdapter(adapter);
            });
        }
    }
}