package com.suyong.kakaobot;

import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mozilla.javascript.json.JsonParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;


public class FileManager {
    private static final String PROJECT_DIRECTORY_PATH = "kakaobot";
    private static File PROJECT_DIRECTORY = new File(Environment.getExternalStorageDirectory(), PROJECT_DIRECTORY_PATH);

    public static void init() {
        PROJECT_DIRECTORY.mkdirs();
    }

    public static Type.Project getProject(String title) {
        Type.Project result = new Type.Project();

        for(File file : PROJECT_DIRECTORY.listFiles()) {
            Type.Project data = getProjectFile(file);
            if (data.title.equals(title)) {
                result = data;

                return result;
            }
        }

        return result;
    }

    public static File getProjectFile(Type.Project project) {
        File result = null;

        for(File file : PROJECT_DIRECTORY.listFiles()) {
            Type.Project data = getProjectFile(new File(file, "data.json"));
            if (data.title.equals(project.title)) {
                result = file;

                return result;
            }
        }

        return result;
    }

    private static Type.Project getProjectFile(File file) {
        StringBuilder builder = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch(IOException err) {}

        Type.Project result = new Type.Project();
        if(builder.toString() != null) {
            try {
                JSONObject object = new JSONObject(builder.toString());

                result.type = object.getString("type").equals(Type.ProjectType.JS.toString()) ? Type.ProjectType.JS : Type.ProjectType.PYTHON;
                result.title = object.getString("title");
                result.subtitle = object.getString("subtitle");
                result.enable = object.getBoolean("enable");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public static void createProject(Type.Project project) {
        JSONObject object = new JSONObject();
        try {
            object.put("type", project.type.toString());
            object.put("title", project.title);
            object.put("subtitle", project.subtitle);
            object.put("enable", project.enable);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String str = object.toString();
        Log.v("json", str);

        File root = new File(PROJECT_DIRECTORY, project.title);
        File file = new File(root, "data.json");
        root.mkdirs();
        try {
            file.createNewFile();

            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(str);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProjectScript(Type.Project project) {
        File file = new File(getProjectFile(project), "main." + project.type.toString());
        StringBuilder builder = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));

            String line;
            while((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return builder.toString();
    }

    public static ArrayList<Type.Project> getProjectList() {
        ArrayList<Type.Project> result = new ArrayList<>();

        for(File file : PROJECT_DIRECTORY.listFiles()) {
            result.add(getProjectFile(new File(file, "data.json")));
        }

        return result;
    }

    public static File getProjectScriptFile(Type.Project project) {
        return new File(getProjectFile(project), "main." + project.type.toString());
    }

    public static File getProjectDataFile(Type.Project project) {
        return new File(getProjectFile(project), "data.json");
    }

    public static void saveData(Type.Project project, String key, Object value) {
        File root = getProjectFile(project);
        File data = new File(root, "data.json");

        try {
            BufferedReader reader = new BufferedReader(new FileReader(data));
            BufferedWriter writer = new BufferedWriter(new FileWriter(data));

            String line;
            JSONObject json = new JSONObject();
            while((line = reader.readLine()) != null) {
                JSONObject object = new JSONObject(line);
                if(object.has(key)) {
                    object.put(key, value);

                    json = object;
                    break;
                }
            }
            writer.write(json.toString());
        } catch(IOException err) {}
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static Object readData(Type.Project project, String key) {
        File root = getProjectFile(project);
        File data = new File(root, "data.json");

        try {
            BufferedReader reader = new BufferedReader(new FileReader(data));
            BufferedWriter writer = new BufferedWriter(new FileWriter(data));

            String line;
            JSONObject json = new JSONObject();
            while((line = reader.readLine()) != null) {
                JSONObject object = new JSONObject(line);
                if(object.has(key)) {
                    return object.get(key);
                }
            }
            writer.write(json.toString());
        } catch(IOException err) {}
        catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void delete(Type.Project project) {
        delete(getProjectFile(project));
    }

    private static void delete(File file) {
        try {
            for(File f : file.listFiles()) {
                if (file.isDirectory()) {
                    delete(f);
                } else {
                    f.delete();
                }
            }
        } catch (NullPointerException e) {}

        file.delete();
    }
}
