package com.anity.healthpunch.fragment;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.alibaba.fastjson2.JSON;
import com.anity.healthpunch.MyApplication;
import com.anity.healthpunch.R;
import com.anity.healthpunch.entity.Course;

import java.io.IOException;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ScheduleFragment extends Fragment implements Callback {


    public static ScheduleFragment newInstance(int position) {

        ScheduleFragment fragment = new ScheduleFragment();
        Bundle bundle = new Bundle();

        bundle.putInt("position", position);

        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int position = getArguments().getInt("position");
        //获取json数据
        getData(position + 1);

        View view = inflater.inflate(R.layout.fragment_schedule, container, false);


        TextView[] weekDay = new TextView[7];
        weekDay[0] = view.findViewById(R.id.monday);
        weekDay[1] = view.findViewById(R.id.tuesday);
        weekDay[2] = view.findViewById(R.id.wednesday);
        weekDay[3] = view.findViewById(R.id.thursday);
        weekDay[4] = view.findViewById(R.id.friday);

        //TODO
        //开始时间
        MyApplication app = MyApplication.getInstance();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, app.year);
        calendar.set(Calendar.MONTH, app.month-1);
        calendar.set(Calendar.DAY_OF_MONTH, app.dayOfMonth);
        calendar.add(Calendar.DATE, position * 7);

        /*for (int i = 0; i < 7; i++) {
            weekDay[i].setText(calendar.get(Calendar.MONTH) + 1 + "/" + calendar.get(Calendar.DAY_OF_MONTH));
            calendar.add(Calendar.DATE, 1);
        }*/
        //当前时间
        for (int i = 0; i < 5; i++) {
            weekDay[i].setText(calendar.get(Calendar.MONTH) + 1 + "/" + calendar.get(Calendar.DAY_OF_MONTH));
            calendar.add(Calendar.DATE, 1);
        }

        while (MyApplication.getInstance().list == null) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        List<Course> list = MyApplication.getInstance().list;
        MyApplication.getInstance().list = null;


        GridLayout gridLayout;

        //渲染每一列
        //for (int i = 1; i <= 7; i++) {
        for (int i = 1; i <= 5; i++) {
            gridLayout = layoutColumn(i, view);

            //渲染每一行
            for (int j = 1; j <= 5; j++) {
                TextView textView = new TextView(view.getContext());

                textView.setGravity(Gravity.CENTER);
                textView.setTextSize(12);
                textView.setTextColor(Color.BLACK);
                textView.setBackgroundColor(Color.parseColor("#eeeeee"));
                textView.setEllipsize(TextUtils.TruncateAt.END);
                for (int k = 0; k < list.size(); k++) {
                    Course course = list.get(k);
                    if (Integer.parseInt(course.getWeekDay()) == i && checkStartTime(course.getStartTime()) == j) {
                        textView.setText(course.getCourseName() + "\n\n" + course.getClassroomName());
                        textView.setBackgroundDrawable(getResources().getDrawable(R.drawable.schedule_item));
                        textView.setOnClickListener(v -> {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                            dialog.setTitle("详细信息");
                            dialog.setMessage(course.toString());
                            dialog.setPositiveButton("关闭", null);
                            dialog.show();
                        });
                        break;
                    }
                }


                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.setMargins(10, 5, 10, 5);


                params.width = GridLayout.LayoutParams.MATCH_PARENT;
                params.height = 0;

                params.rowSpec = GridLayout.spec(j, 1, 1);

                gridLayout.addView(textView, params);

            }

        }


        return view;
    }

    private void getData(int week) {
        OkHttpClient client = new OkHttpClient.Builder().proxy(Proxy.NO_PROXY).build();
        String url = "http://222.243.161.213:81/hnrjzyxyhd/student/curriculum";

        FormBody.Builder form = new FormBody.Builder();
        form.add("week", String.valueOf(week));
        form.add("kbjcmsid", "");

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
        String kb_data = response.body().string();

        com.alibaba.fastjson2.JSONObject jsonObject = JSON.parseObject(kb_data);
        String Msg = jsonObject.getString("Msg");
        if (!Msg.equals("success~")) {
            Course temp = new Course();
            temp.setWeekDay("3");
            temp.setStartTime("14:30");
            temp.setCourseName("无课！");
            temp.setTeacherName("无课！");
            temp.setClassroomName("无课！");
            List<Course> tempCourse = new ArrayList<>();
            tempCourse.add(temp);
            MyApplication.getInstance().list = tempCourse;
            return;
        }

        com.alibaba.fastjson2.JSONArray data = jsonObject.getJSONArray("data");
        com.alibaba.fastjson2.JSONObject o = (com.alibaba.fastjson2.JSONObject) data.get(0);
        String courses = o.getString("courses");

        MyApplication.getInstance().list = JSON.parseArray(courses, Course.class);

    }


    private int checkStartTime(String startTime) {
        int wz = 0;
        switch (startTime) {
            case "08:00":
                wz = 1;
                break;
            case "10:00":
                wz = 2;
                break;
            case "14:30":
            case "14:00":
                wz = 3;
                break;
            case "16:30":
            case "16:00":
                wz = 4;
                break;
            case "19:30":
            case "19:00":
                wz = 5;
                break;
        }
        return wz;
    }

    private GridLayout layoutColumn(int i, View view) {

        GridLayout gridLayout = view.findViewById(R.id.d1);
        switch (i) {
            case 1:
                gridLayout = view.findViewById(R.id.d1);
                break;
            case 2:
                gridLayout = view.findViewById(R.id.d2);
                break;
            case 3:
                gridLayout = view.findViewById(R.id.d3);
                break;
            case 4:
                gridLayout = view.findViewById(R.id.d4);
                break;
            case 5:
                gridLayout = view.findViewById(R.id.d5);
                break;
/*            case 6:
                gridLayout = view.findViewById(R.id.d6);
                break;
            case 7:
                gridLayout = view.findViewById(R.id.d7);
                break;*/
        }

        return gridLayout;
    }

}