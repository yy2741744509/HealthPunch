package com.anity.healthpunch;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.anity.healthpunch.adapter.ViewPagerAdapter;

import java.util.Calendar;
import java.util.Date;

public class CourseActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private Spinner select_spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_course);

        select_spinner = findViewById(R.id.select_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.week, R.layout.spinner_layout);
        adapter.setDropDownViewResource(R.layout.spinner_item);

        //因为还有一个带箭头的背景，将背景设置为透明即可
        select_spinner.setBackgroundColor(0x0);
        select_spinner.setAdapter(adapter);
        select_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        viewPager = findViewById(R.id.viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                select_spinner.setSelection(position, true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        initViewPager();
    }

    private void initViewPager() {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        //当前时间
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);


        //TODO
        //开始时间
        MyApplication app = MyApplication.getInstance();

        Calendar cal = Calendar.getInstance();
        calendar.set(Calendar.YEAR, app.year);
        calendar.set(Calendar.MONTH, app.month-1);
        calendar.set(Calendar.DAY_OF_MONTH, app.dayOfMonth);

        Long atPresentTime = calendar.getTime().getTime();
        Long targetTime = cal.getTime().getTime();
        //如果当前时间小于目标时间
        if (atPresentTime < targetTime) {
            viewPager.setCurrentItem(0);
            select_spinner.setSelection(0, true);
        } else {
            long day = (atPresentTime - targetTime) / 1000 / 60 / 60 / 24;
            int weekDay = Math.toIntExact((day+1) / 7);
            if (weekDay < 22) {
                viewPager.setCurrentItem(weekDay);
                select_spinner.setSelection(weekDay, true);
            } else {
                viewPager.setCurrentItem(21);
                select_spinner.setSelection(21, true);
            }
        }

    }


}