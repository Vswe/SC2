package util;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Localization {
    public static void main(String[] args) {
        //this code is just for moving stuff around during development, doesn't really matter that it's using absolute paths
        File langDir = new File("C:\\Users\\Vswe\\Dropbox\\Minecraft Modding\\SC2\\src\\main\\resources\\assets\\stevescarts\\lang");

        File change = new File(langDir, "change.txt");
        File fallBack = new File(langDir, "en_US.lang");
        for (File child : langDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith("en_US.lang");
            }
        })) {
            writeDataToFile(getUpdatedData(change, fallBack, child), child);
        }
    }


    private static List<String> getUpdatedData(File change, File fallBack, File target) {
        List<String> changeData = readFile(change);
        List<String> fallBackData = readFile(fallBack);
        List<String> targetData = readFile(target);

        List<String> result = new ArrayList<String>();
        int fallBacks = 0;
        int missingEntries = 0;
        for (String line : changeData) {
            if (line.startsWith("#") || line.startsWith(" ") || line.isEmpty()) {
                result.add(line);
            }else if(!line.startsWith("--") && line.contains("=")) {
                String oldKey = line.split("=")[0];
                String newKey = line.split("=")[1];
                String value = getValueFromKey(targetData, oldKey);
                if (value == null) {
                    fallBacks++;
                    value = getValueFromKey(fallBackData, oldKey);
                    if (value == null) {
                        missingEntries++;
                        value = "Missing Entry";
                    }
                }

                result.add(newKey + "=" + value);
            }
        }

        if (fallBacks > 0) {
            System.err.println("Had to use the fallback file " + fallBacks + " times for " + target.getName());
            if (missingEntries > 0) {
                System.err.println("Found " + missingEntries + " missing entries for " + target.getName());
            }
        }


        return result;
    }

    private static void writeDataToFile(List<String> data, File file) {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file);
            for (String line : data) {
                fileWriter.write(line + "\n");
            }
        }catch (IOException ex) {
            ex.printStackTrace();
        }finally {
            try {
                if (fileWriter != null) {
                    fileWriter.close();
                }
            }catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    private static String getValueFromKey(List<String> data, String key) {
        for (String line : data) {
            if (line.startsWith(key + "=")) {
                return line.substring(line.indexOf("=") + 1);
            }
        }
        return null;
    }

    private static List<String> readFile(File file) {
        List<String> data = new ArrayList<String>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                data.add(line);
            }

        }catch (IOException ex) {
            ex.printStackTrace();
        }finally {
            try {
                if (br != null) {
                    br.close();
                }
            }catch (IOException ex) {
                ex.printStackTrace();
            }

        }
        return data;
    }
}
