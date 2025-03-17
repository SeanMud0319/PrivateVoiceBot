package com.nontage.utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

public class SimpleYaml {
    private final String filePath;
    private final Map<String, String> data = new LinkedHashMap<>();

    public SimpleYaml(String name, String resourcePath) {
        String baseDir = getProgramDirectory();
        String pluginFolder = baseDir + File.separator + name;
        new File(pluginFolder).mkdirs();
        this.filePath = pluginFolder + File.separator + resourcePath;
        extractResource(resourcePath, this.filePath);
        loadYaml();
    }

    private void loadYaml() {
        data.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                String[] parts = line.split(":", 2);
                if (parts.length == 2) {
                    data.put(parts[0].trim(), parts[1].trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String getProgramDirectory() {
        try {
            return new File(SimpleYaml.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
        } catch (Exception e) {
            return new File("").getAbsolutePath();
        }
    }
    private void extractResource(String resourcePath, String outputPath) {
        File file = new File(outputPath);
        if (file.exists()) return;
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
            if (input == null) {
                System.err.println("Resource not found: " + resourcePath);
                return;
            }
            Files.copy(input, Paths.get(outputPath), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Extracted " + resourcePath + " to " + outputPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        loadYaml();
    }

    public String getString(String key) {
        return data.getOrDefault(key, "").replaceAll("\"", "");
    }


    public int getInt(String key) {
        try {
            return Integer.parseInt(data.getOrDefault(key, "0"));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public boolean getBoolean(String key) {
        return Boolean.parseBoolean(data.getOrDefault(key, "false"));
    }

    public void set(String key, Object value) {
        data.put(key, String.valueOf(value));
    }

    public void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Map.Entry<String, String> entry : data.entrySet()) {
                writer.write(entry.getKey() + ": " + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public <K, V> Map<K, V> getMap(String key, Class<K> keyClass, Class<V> valueClass) {
        String value = getString(key);
        Map<K, V> map = new HashMap<>();
        if (!value.isEmpty()) {
            String[] pairs = value.split(",");
            for (String pair : pairs) {
                String[] kv = pair.split(":");
                if (kv.length == 2) {
                    try {
                        K mapKey = parseValue(kv[0].trim(), keyClass);
                        V mapValue = parseValue(kv[1].trim(), valueClass);
                        if (mapKey != null && mapValue != null) {
                            map.put(mapKey, mapValue);
                        }
                    } catch (Exception ignored) {}
                }
            }
        }
        return map;
    }

    public <K, V> void setMap(String key, Map<K, V> map) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            sb.append(entry.getKey()).append(":").append(entry.getValue()).append(",");
        }
        if (sb.length() > 0) sb.setLength(sb.length() - 1);
        set(key, sb.toString());
    }

    private <T> T parseValue(String value, Class<T> clazz) {
        try {
            if (clazz == String.class) {
                return clazz.cast(value);
            } else if (clazz == Integer.class) {
                return clazz.cast(Integer.parseInt(value));
            } else if (clazz == Long.class) {
                return clazz.cast(Long.parseLong(value));
            } else if (clazz == Double.class) {
                return clazz.cast(Double.parseDouble(value));
            } else if (clazz == Boolean.class) {
                return clazz.cast(Boolean.parseBoolean(value));
            } else if (clazz == UUID.class) {
                return clazz.cast(UUID.fromString(value));
            }
        } catch (Exception ignored) {}
        return null;
    }

    public String getFilePath() {
        return filePath;
    }
}