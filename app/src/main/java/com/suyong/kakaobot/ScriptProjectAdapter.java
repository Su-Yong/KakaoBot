package com.suyong.kakaobot;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ScriptProjectAdapter extends RecyclerView.Adapter {
    private ArrayList<Type.Project> list = new ArrayList<>();

    public ScriptProjectAdapter(ArrayList<Type.Project> list) {
        this.list = list;
        Log.d("KakaoBot/Recycler", list.get(0).toString());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.script_list_layout, parent, false);

        return new Holder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder originHolder, int position) {
        Holder holder = (Holder) originHolder;
        Type.Project project = list.get(position);

        switch (project.icon) {
            case JS:
                holder.icon.setImageResource(R.drawable.image_javascript);
                break;
            case PYTHON:
                holder.icon.setImageResource(R.drawable.image_python);
                break;
        }

        holder.title.setText(project.title);
        holder.subtitle.setText(project.subtitle);

        Log.d("Recycler", project.disabled + "");
        if(project.disabled) {
            disableAll((ViewGroup) originHolder.itemView);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void disableAll(ViewGroup group) {
        for(int i = 0; i < group.getChildCount(); i++) {
            View child = group.getChildAt(i);
            child.setEnabled(false);
            if (child instanceof ViewGroup) {
                disableAll((ViewGroup) child);
            }
        }
    }

    public static class Holder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView subtitle;
        public ImageView icon;

        public Holder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.title);
            subtitle = (TextView) itemView.findViewById(R.id.subtitle);
            icon = (ImageView) itemView.findViewById(R.id.image);
        }
    }
}
