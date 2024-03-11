package com.anity.healthpunch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.anity.healthpunch.R;
import com.anity.healthpunch.entity.Score;

import java.util.List;

public class ScoreAdapter extends BaseAdapter {

    private final Context context;
    private List<Score> list;

    public void setList(List list){
        this.list = list;
    }

    public ScoreAdapter(Context context, List<Score> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.listscore_item, null);

            convertView.setPadding(0, 20, 0, 20);
            viewHolder.scoreName =convertView.findViewById(R.id.item_scoreName);
            viewHolder.scoreNum =convertView.findViewById(R.id.scoreNum);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Score score = list.get(position);
        viewHolder.scoreName.setText(score.getCourseName());
        viewHolder.scoreNum.setText(score.getFraction());

        return convertView;
    }


    public final class ViewHolder {
        public TextView scoreName;
        public TextView scoreNum;
    }
}
