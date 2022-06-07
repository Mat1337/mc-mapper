package me.mat.mapper.forge.version;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public final class ForgeVersionManifest {

    private static final Gson GSON = new GsonBuilder().serializeNulls().setPrettyPrinting().create();

    private final Map<String, ForgeVersion> versions = new HashMap<>();

    private ForgeVersionManifest(JsonObject object) {
        // load all the versions
        object.entrySet().forEach(entry
                -> versions.put(entry.getKey(), GSON.fromJson(entry.getValue(), ForgeVersion.class)));
    }

    public ForgeVersion getVersion(String version) {
        return versions.getOrDefault(version, null);
    }

    public static ForgeVersionManifest load() {
        try (InputStream inputStream = ForgeVersionManifest.class.getResourceAsStream("/forge_mappings.json");
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream)) {
            return new ForgeVersionManifest(GSON.fromJson(inputStreamReader, JsonObject.class));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
