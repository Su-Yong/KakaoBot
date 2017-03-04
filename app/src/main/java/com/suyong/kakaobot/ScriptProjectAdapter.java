package com.suyong.kakaobot;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.mozilla.javascript.tools.debugger.Main;

import java.util.ArrayList;

public class ScriptProjectAdapter extends RecyclerView.Adapter {
    private ArrayList<Type.Project> list = new ArrayList<>();
    private MainActivity activity;

    public ScriptProjectAdapter(ArrayList<Type.Project> list, MainActivity activity) {
        this.list = list;
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.script_list_layout, parent, false);

        return new Holder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder originHolder, final int position) {
        final Holder holder = (Holder) originHolder;
        final Type.Project project = list.get(position);

        switch (project.type) {
            case JS:
                holder.icon.setImageResource(R.drawable.image_javascript);
                break;
            case PYTHON:
                holder.icon.setImageResource(R.drawable.image_python);
                break;
        }

        holder.title.setText(project.title);
        holder.subtitle.setText(project.subtitle);
        holder.subtitle.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        holder.subtitle.setSelected(true);
        holder.setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.settingPopup == null) {
                    holder.settingPopup = new SettingPopup(list.get(position), activity);
                }
                holder.settingPopup.setProject(list.get(position));
                holder.settingPopup.initListener();
                holder.settingPopup.showAsDropDown(view);
            }
        });

        if(project.isError != null) {
            holder.warning.setVisibility(View.VISIBLE);
            holder.warning.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(holder.popup == null) {
                        LinearLayout layout = new LinearLayout(holder.itemView.getContext());
                        CardView card = new CardView(holder.itemView.getContext());
                        TextView text = new TextView(holder.itemView.getContext());

                        text.setText(project.isError.split("SCRIPTSPLITTAG")[1]);
                        card.setClickable(true);
                        card.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                holder.popup.dismiss();
                            }
                        });
                        card.setUseCompatPadding(true);
                        card.setContentPadding(MainActivity.dp(8), MainActivity.dp(8), MainActivity.dp(8), MainActivity.dp(8));
                        card.setCardElevation(12.f);

                        card.addView(text);
                        layout.addView(card);
                        holder.popup = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                        holder.popup.setBackgroundDrawable(new BitmapDrawable());
                        holder.popup.setOutsideTouchable(true);
                    }

                    if(!holder.popup.isShowing()) {
                        holder.popup.showAsDropDown(view);
                    } else {
                        holder.popup.dismiss();
                    }
                }
            });
        }
        if(!project.enable) {
            disableAll((ViewGroup) originHolder.itemView);
        } else {
            enableAll((ViewGroup) originHolder.itemView);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(ArrayList<Type.Project> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    private void disableAll(ViewGroup group) {
        for(int i = 0; i < group.getChildCount(); i++) {
            View child = group.getChildAt(i);
            if(child.getId() != R.id.setting) {
                child.setEnabled(false);
            }
            if (child instanceof ViewGroup) {
                disableAll((ViewGroup) child);
            }
        }
    }
    private void enableAll(ViewGroup group) {
        for(int i = 0; i < group.getChildCount(); i++) {
            View child = group.getChildAt(i);
            if(child.getId() != R.id.setting) {
                child.setEnabled(true);
            }
            if (child instanceof ViewGroup) {
                enableAll((ViewGroup) child);
            }
        }
    }

    public static class Holder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView subtitle;
        public ImageButton setting;
        public ImageButton warning;
        public ImageView icon;
        public PopupWindow popup;
        public SettingPopup settingPopup;

        public Holder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.title);
            subtitle = (TextView) itemView.findViewById(R.id.subtitle);
            icon = (ImageView) itemView.findViewById(R.id.image);
            setting = (ImageButton) itemView.findViewById(R.id.setting);
            warning = (ImageButton) itemView.findViewById(R.id.is_error);
        }
    }
}
