
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Libs for rendering
 * 
 * @author chung-pi <cdao@cs.uef.fi>
 */
public class Libs {

    public static String lastFileModified(String dir) {
        File fl = new File(dir);
        File[] files = fl.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isFile();
            }
        });
        long lastMod = Long.MIN_VALUE;
        File choise = null;
        for (File file : files) {
            if (file.lastModified() > lastMod) {
                choise = file;
                lastMod = file.lastModified();
            }
        }
        return choise.getName();
    }

    public static List<String> getListFile(String dir) {
        List<String> results = new ArrayList<String>();
        File[] files = new File(dir).listFiles();
        if (files == null) return results;
        for (File file : files) {
            if (file.isFile()) {
                String split[] = file.getName().split("\\.");
                if (split[1].equals("png")){
                    results.add(split[0]);
                }
            }
        }
        return results;
    }
    
    public static int getMaximumId(String dir) {
        int result = 0;
        File[] files = new File(dir).listFiles();
        if (files == null) return result;
        for (File file : files) {
            if (file.isDirectory()) {
                if (Integer.parseInt(file.getName()) > result){
                    result = Integer.parseInt(file.getName());
                }
            }
        }
        return result;
    }
    
    public static int checkFile(String file){
        File f = new File(file);
        return f.exists() ? 1:0;
    }
}
