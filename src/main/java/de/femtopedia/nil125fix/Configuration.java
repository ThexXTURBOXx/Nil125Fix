package de.femtopedia.nil125fix;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Properties;

public class Configuration {

    private final Path path;
    private final Properties config = new Properties();

    private boolean changed;

    public Configuration(String path, Iterable<String> keys) throws IOException {
        this(Paths.get(path), keys);
    }

    public Configuration(Path path, Iterable<String> keys) throws IOException {
        this.path = path;

        if (!Files.exists(this.path)) {
            changed = true;
            Files.createDirectories(this.path.getParent());
            Files.createFile(this.path);
        }

        for (String key : keys)
            config.setProperty(key, true + "");

        Properties tmp = new Properties();
        tmp.load(Files.newBufferedReader(this.path));
        for (String key : keys) {
            if (!tmp.containsKey(key)) {
                changed = true;
                break;
            }
        }
        config.putAll(tmp);
    }

    public boolean isEnabled(String cfg) {
        return Boolean.parseBoolean(get(cfg, true + ""));
    }

    public String get(String option, String defaultValue) {
        return config.getProperty(option, defaultValue);
    }

    public void setEnabled(String option) {
        set(option, true + "");
    }

    public void setDisabled(String option) {
        set(option, false + "");
    }

    public void set(String option, String value) {
        config.setProperty(option, value);
        changed = true;
    }

    public boolean isChanged() {
        return changed;
    }

    public void saveIfChanged() throws IOException {
        if (isChanged()) save();
    }

    public void save() throws IOException {
        try (BufferedWriter bw = Files.newBufferedWriter(path);
             PrintWriter pw = new PrintWriter(bw)) {
            config.entrySet().stream()
                    .sorted(Comparator.comparing(e -> e.getKey().toString()))
                    .forEach(e -> pw.println(e.getKey() + "=" + e.getValue()));
            changed = false;
        }
    }

}
