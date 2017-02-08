package com.suyong.kakaobot;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class FileManager {
    private static final String PROJECT_DIRECTORY = "kakaobot";

    public static ArrayList<Type.Project> getProjectList() {
        ArrayList<Type.Project> result = new ArrayList<>();
        File projectDirectory = new File(Environment.getExternalStorageDirectory(), PROJECT_DIRECTORY);

        for(File file : projectDirectory.listFiles()) {
            File data = new File(file, "data.txt");
            try {
                BufferedReader stream = new BufferedReader(new FileReader(data));

                Type.Project project = new Type.Project();
                String line;
                while((line = stream.readLine()) != null) {
                    if(line.contains("type")) {
                        String s = line.split(":")[1];
                        if (s.toLowerCase().equals("js")) {
                            project.icon = Type.IconType.JS;
                        } else if (s.toLowerCase().equals("python")) {
                            project.icon = Type.IconType.PYTHON;
                        }
                    } else if(line.contains("subtitle")) {
                        project.subtitle = line.split(":")[1];
                    } else if(line.contains("title")) {
                        project.title = line.split(":")[1];
                    } else if(line.contains("disabled")) {
                        String s = line.split(":")[1];
                        if (s.toLowerCase().equals("true")) {
                            project.disabled = true;
                        } else if (s.toLowerCase().equals("false")) {
                            project.disabled = false;
                        }
                    }

                    project.path = file.toString();
                }

                Log.d("check", project.title + " " + project.subtitle);

                result.add(project);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public static String getScriptIndex(Type.IconType type, String name) throws FileNotFoundException {
        File file = null;

        for(Type.Project project : getProjectList()) {
            if(project.title.equals(name)) {
                file = new File(project.path);
            }
        }

        if(file == null) {
            throw new FileNotFoundException("Can't find " + name + " Project: " + file.toString());
        }

        StringBuilder builder = new StringBuilder();
        File script = new File(file, "main." + type.toString());

        if(!script.exists()) {
            throw new FileNotFoundException("Can't find " + name + "'s Script file: " + script.toString());
        }

        try {
            BufferedReader stream = new BufferedReader(new FileReader(script));

            String line;
            while((line = stream.readLine()) != null) {
                builder.append(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return builder.toString();
    }

}
