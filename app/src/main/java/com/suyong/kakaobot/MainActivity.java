package com.suyong.kakaobot;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.suyong.kakaobot.script.JSScriptEngine;
import com.suyong.kakaobot.script.PythonScriptEngine;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.os.Build.VERSION_CODES.M;
import static com.suyong.kakaobot.FileManager.getProjectList;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_READ = 0;
    private static final int PERMISSION_WRITE = 1;
    private static final int PERMISSION_INTERNET = 2;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener((view) -> {

            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
                requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, }, PERMISSION_READ);
            }
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
                requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE, }, PERMISSION_WRITE);
            }
            if (checkSelfPermission(Manifest.permission.INTERNET) != PERMISSION_GRANTED) {
                requestPermissions(new String[] {Manifest.permission.INTERNET, }, PERMISSION_INTERNET);
            }
        }

        initRecyclerView();
        initEngines();
    }

    private void initRecyclerView() {
        ArrayList<Type.Project> list = getProjectList();

        ScriptProjectAdapter adapter = new ScriptProjectAdapter(list);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.project_list);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    private void initEngines() {
        ArrayList<Type.Project> list = getProjectList();
        for(Type.Project project : list) {
            if(!project.disabled) {
                switch (project.icon) {
                    case JS:
                        JSScriptEngine jsScriptEngine = new JSScriptEngine();
                        try {
                            jsScriptEngine.setScriptSource(FileManager.getScriptIndex(Type.IconType.JS, project.title));
                            KakaoTalkListener.addJsEngine(jsScriptEngine);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            Snackbar.make(fab, getString(R.string.file_not_found), Snackbar.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Snackbar.make(fab, getString(R.string.unknown_error), Snackbar.LENGTH_SHORT).show();
                        }
                        break;
                    case PYTHON:
                        PythonScriptEngine pythonScriptEngine = new PythonScriptEngine();
                        try {
                            pythonScriptEngine.setScriptSource(FileManager.getScriptIndex(Type.IconType.PYTHON, project.title));
                            KakaoTalkListener.addPythonEngine(pythonScriptEngine);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            Snackbar.make(fab, getString(R.string.file_not_found), Snackbar.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Snackbar.make(fab, getString(R.string.unknown_error), Snackbar.LENGTH_SHORT).show();
                        }
                        break;
                }

                Log.d("KakaoBot/initEngine", project.title + ": Load succeed");
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case PERMISSION_READ:
                if(grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED) {
                    initRecyclerView();
                } else {
                    requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, }, PERMISSION_READ);
                    Toast.makeText(this, getString(R.string.no_read_permission), Toast.LENGTH_SHORT).show();
                }
                break;
            case PERMISSION_WRITE:
                if(grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED) {
                    // TODO
                } else {
                    // TODO
                }
                break;
            case PERMISSION_INTERNET:
                if(grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED) {
                    // TODO
                } else {
                    // TODO
                }
                break;
        }
    }
}
