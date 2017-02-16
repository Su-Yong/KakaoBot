package com.suyong.kakaobot;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;


public class SettingPopup extends PopupWindow {
    private Type.Project project;
    private Context context;
    private LinearLayout layout;
    private MainActivity activity;

    public SettingPopup(Type.Project project, MainActivity activity) {
        this.activity = activity;
        this.context = activity;
        this.project = project;

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layout = (LinearLayout) layoutInflater.inflate(R.layout.script_setting_layout, null, false);

        init();
        initListener();
    }

    private void init() {
        LinearLayout frame = new LinearLayout(context);
        CardView card = new CardView(context);

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        card.setClickable(true);
        card.setUseCompatPadding(true);
        card.setContentPadding(MainActivity.dp(8), MainActivity.dp(8), MainActivity.dp(8), MainActivity.dp(8));
        card.setCardElevation(12.f);

        card.addView(layout);
        frame.addView(card);

        this.setContentView(frame);
        this.setWindowLayoutMode(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setOutsideTouchable(true);
    }

    public void setProject(Type.Project project) {
        this.project = project;
    }

    public void initListener() {
        final TextInputEditText titleEdit = (TextInputEditText) layout.findViewById(R.id.title);
        final TextInputEditText subtitleEdit = (TextInputEditText) layout.findViewById(R.id.subtitle);
        ImageButton titleSet = (ImageButton) layout.findViewById(R.id.title_set);
        ImageButton subtitleSet = (ImageButton) layout.findViewById(R.id.subtitle_set);
        final SwitchCompat enableSwitch = (SwitchCompat) layout.findViewById(R.id.script_enable);
        ImageButton deleteButton = (ImageButton) layout.findViewById(R.id.delete_project);

        titleEdit.setText(project.title);
        subtitleEdit.setText(project.subtitle);
        enableSwitch.setChecked(!project.disabled);

        titleSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    FileManager.changeData(project.title, "title", titleEdit.getText().toString());

                    dismiss();
                    activity.reload();
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });

        subtitleSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    FileManager.changeData(project.title, "subtitle", subtitleEdit.getText().toString());

                    dismiss();
                    activity.reload();
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });
        enableSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                try {
                    FileManager.changeData(project.title, "disabled", (!enableSwitch.isChecked()) + "");

                    dismiss();
                    activity.reload();
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    FileManager.delete(project.title);

                    dismiss();
                    activity.reload();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
