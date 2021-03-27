package fr.uge.confroid.utlis;

import android.util.Log;

import java.io.*;

public class FileUtils {

    /**
     * @param file
     * @param content
     */
    public static void writeFile(File file, String content) {
        try {
            FileWriter fileWriter = new FileWriter(file, false);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(content);
            bufferedWriter.close();
        } catch (IOException e) {
            Log.e("IOException", "");
        }
    }

    /**
     * @param file
     * @return string file
     */


    public static String readFile(File file) {
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder content = new StringBuilder();
            String line = bufferedReader.readLine();
            while (line != null) {
                content.append(line);
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
            return content.toString();
        } catch (IOException e) {
            Log.e("IOException", "");
            return null;
        }
    }

}
