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
        holder.subtitle.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        holder.subtitle.setSelected(true);

        if(project.isError != null) {
            holder.warning.setVisibility(View.VISIBLE);
            holder.warning.setOnClickListener((view) -> {
                if(holder.popup == null) {
                    LinearLayout layout = new LinearLayout(holder.itemView.getContext());
                    CardView card = new CardView(holder.itemView.getContext());
                    TextView text = new TextView(holder.itemView.getContext());

                    text.setText(project.isError.split("SCRIPTSPLITTAG")[1]);
                    card.setClickable(true);
                    card.setOnClickListener(view1 -> {
                        holder.popup.dismiss();
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
            });
        }
        if(project.disabled) {
            disableAll((ViewGroup) originHolder.itemView);
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
            child.setEnabled(false);
            if (child instanceof ViewGroup) {
                disableAll((ViewGroup) child);
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
