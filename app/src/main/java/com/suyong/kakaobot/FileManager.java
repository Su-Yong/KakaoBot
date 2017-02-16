package com.suyong.kakaobot;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
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
                builder.append("\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return builder.toString();
    }

    public static void createProject(String title, String subtitle, Type.IconType type) throws IOException {
        File projectDirectory = new File(Environment.getExternalStorageDirectory(), PROJECT_DIRECTORY);
        File directory = new File(projectDirectory, title);
        File dataFile = new File(directory, "data.txt");
        File scriptFile = new File(directory, "main." + type.toString());

        directory.mkdirs();
        dataFile.createNewFile();
        scriptFile.createNewFile();

        BufferedWriter writer = new BufferedWriter(new FileWriter(dataFile));

        writer.write("type:" + type.toString()); writer.newLine();
        writer.write("title:" + title); writer.newLine();
        writer.write("subtitle:" + subtitle); writer.newLine();
        writer.write("disabled:false"); writer.newLine();

        writer.close();
        writer = new BufferedWriter(new FileWriter(scriptFile));

        writer.write("function talkReceivedHook(room, message, sender, isGroup) {"); writer.newLine();
        writer.write("  if(message == \"Hello\") {"); writer.newLine();
        writer.write("    KakaoTalk.send(room, \"Hello, \" + sender + \"!\");"); writer.newLine();
        writer.write("  }"); writer.newLine();
        writer.write("}");

        writer.close();
    }

    public static void changeData(String project, String key, String value) throws Exception {
        File projectDirectory = new File(Environment.getExternalStorageDirectory(), PROJECT_DIRECTORY);

        for(File file : projectDirectory.listFiles()) {
            File data = new File(file, "data.txt");

            BufferedReader stream = new BufferedReader(new FileReader(data));

            String line;
            int i = 0;
            String[] str = new String[4];
            boolean check = false;
            while((line = stream.readLine()) != null) {
                if(!line.split(":")[0].equals(key)) {
                    str[i] = line;
                } else {
                    str[i] = "check";
                }

                if(line.contains("title") && line.split(":")[1].equals(project)) {
                    check = true;
                }

                i++;
                Log.d("KakaoBot/FileManager", line.split(":")[1]);
            }
            stream.close();

            if(check) {
                BufferedWriter writer = new BufferedWriter(new FileWriter(data));
                for(String l : str) {
                    if(l.equals("check")) {
                        writer.write(key + ":" + value);
                    } else {
                        writer.write(l);
                    }
                    writer.newLine();

                    Log.d("KakaoBot/FileManager", l);
                }

                writer.close();
                return;
            }
        }

        throw new Exception("Can't find project");
    }

    public static void delete(String project) throws Exception {
        File projectDirectory = new File(Environment.getExternalStorageDirectory(), PROJECT_DIRECTORY);

        for(File file : projectDirectory.listFiles()) {
            File data = new File(file, "data.txt");

            BufferedReader stream = new BufferedReader(new FileReader(data));

            String line;
            boolean check = false;
            while((line = stream.readLine()) != null) {
                if(line.contains("title") && line.split(":")[1].equals(project)) {
                    check = true;
                }
            }
            stream.close();

            if(check) {
                deleteFile(file.getParentFile());

                return;
            }
        }

        throw new Exception("Can't find project");
    }

    private static void deleteFile(File file) {
        if(file.isDirectory()) {
            for(File f : file.listFiles()) {
                deleteFile(f);
            }
        } else {
            file.delete();
        }
    }
}
