package simpletask.main.app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;

/**
 * Class that represents the config.
 */
public class Config {
    /**
     * Path to where the config file is stored (ususally top level dir).
     */
    private final String path;
    /**
     * Map that contains the config info. Keyed on config names with the value being the
     * value of that partiuclar key.
     */
    private Map<ConfigKeys, String> config;

    /**
     * 
     * @param path
     */
    public Config(final String path) {
        this.path = path;
        this.config = new HashMap<>();
        loadWorkspace();
    }
    /**
     * 
     */
    private void loadWorkspace() {
        // Pass the path to the file as a parameter 
        File file = new File(path);
        Scanner sc;
        try {
            sc = new Scanner(file);
            while (sc.hasNextLine()) { 
                parseLine(sc.nextLine());
            }
    
            sc.close();
        } catch (FileNotFoundException ex) {
            System.out.println("File not found, creating empty config now");
            createEmptyFile(file);
        }
    }

    private void createEmptyFile(final File file) {
        try {
            file.createNewFile();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    /**
     * 
     * @param line
     */
    private void parseLine(final String line) {
        String key = line.substring(0, line.indexOf("="));
        config.put(ConfigKeys.valueOf(key.toUpperCase()), line.substring(line.indexOf("=") + 1));
    }

    // public static void main(final String[] args) {
    //     Config c = new Config(".config2");
    //     c.setValue(ConfigKeys.DIR, "Random Valu1e");
    //     c.saveCurrentSettings();
    // }
    /**
     * Saves the current config map to the file on path.
     *
     * @return  True if file saved correctly, false otherwise.
     */
    public boolean saveCurrentSettings() {
        // Pass the path to the file as a parameter 
        FileWriter file;
        Scanner sc;
        try {
            file = new FileWriter(path);
            sc = new Scanner(new File(path));
            while (sc.hasNextLine()) { 
                parseLine(sc.nextLine());
            }
            for (Entry<ConfigKeys, String> entry : config.entrySet()) {
                file.write(entry.getKey() + "=" + entry.getValue() + "\n");
            }
            
            sc.close();
            file.close();
            return true;
        } catch (FileNotFoundException ex) {
            return false;
        } catch (IOException ex) {
            return false;
        }
    }
    /**
     * Sets the value for a particular key
     *
     * @param key   The ConfigKey to set the value of
     * @param value The value to set it to
     */
    public void setValue(final ConfigKeys key, final String value) {
        config.put(key, value);
    }
    
    /**
     * Retrieves the value for a ConfigKey.
     *
     * @param key   Key to retrieve value fo
     * @return      The value for the key
     */
    public String getConfig(final ConfigKeys key) {
        return config.get(key);
    }
}