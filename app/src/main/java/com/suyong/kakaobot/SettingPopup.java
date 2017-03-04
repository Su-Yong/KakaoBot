package com.suyong.kakaobot;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;


public class SettingPopup extends PopupWindow {
    private Type.Project project;
    private Context context;
    private ScrollView layout;
    private MainActivity activity;

    public SettingPopup(Type.Project project, MainActivity activity) {
        this.activity = activity;
        this.context = activity;
        this.project = project;

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layout = (ScrollView) layoutInflater.inflate(R.layout.script_setting_layout, null, false);

        init();
        initListener();
    }

    private void init() {
        LinearLayout frame = new LinearLayout(context);
        CardView card = new CardView(context);

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
        ImageButton refreshButton = (ImageButton) layout.findViewById(R.id.refresh_project);

        titleEdit.setText(project.title);
        subtitleEdit.setText(project.subtitle);
        enableSwitch.setChecked(project.enable);

        titleSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    FileManager.saveData(project, "title", titleEdit.getText().toString());

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
                    FileManager.saveData(project, "subtitle", subtitleEdit.getText().toString());

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
                    FileManager.saveData(project, "disabled", (!enableSwitch.isChecked()) + "");

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
                AlertDialog dialog = new AlertDialog.Builder(context).create();
                dialog.setTitle(context.getString(R.string.delete_check));

                dialog.setButton(DialogInterface.BUTTON_POSITIVE, context.getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            FileManager.delete(project);

                            activity.reload();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // null
                    }
                });
                dialog.show();

                dismiss();
            }
        });
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                activity.reloadProject(project);
            }
        });
    }
}
