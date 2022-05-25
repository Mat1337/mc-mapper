package me.mat.mapper.mojang.version;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class VersionsManifest {

    private static final Gson GSON = new GsonBuilder().serializeNulls().setPrettyPrinting().create();

    private final Map<String, Version> versionMap = new HashMap<>();

    @Getter
    @NonNull
    public final Version latestRelease;

    @Getter
    @NonNull
    public final Version latestSnapshot;

    private VersionsManifest(Manifest manifest) {
        // load all the versions
        Stream.of(manifest.versions).forEach(version -> versionMap.put(version.id, version));

        // fetch the latest versions
        this.latestRelease = versionMap.get(manifest.latest.release);
        this.latestSnapshot = versionMap.get(manifest.latest.snapshot);
    }

    /**
     * Fetches a version matching
     * the provided version id
     *
     * @param id id of the version that you want to fetch
     * @return {@link Version}
     */

    public Version getVersion(String id) {
        if (id.equals("latest")) {
            return latestRelease;
        } else if (id.equals("latest-snapShot")) {
            return latestSnapshot;
        }
        return versionMap.getOrDefault(id, null);
    }

    /**
     * Loads the VersionsManifest from
     * the provided file
     *
     * @param file file that you want to load the manifest from
     * @return {@link VersionsManifest}
     */

    public static VersionsManifest load(File file) {
        try (FileReader fileReader = new FileReader(file)) {
            return new VersionsManifest(GSON.fromJson(fileReader, Manifest.class));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @AllArgsConstructor
    private static final class Manifest {

        @NonNull
        @SerializedName("latest")
        public Latest latest;

        @NonNull
        @SerializedName("versions")
        public Version[] versions;

        @AllArgsConstructor
        private static final class Latest {

            @NonNull
            @SerializedName("release")
            public final String release;

            @NonNull
            @SerializedName("snapshot")
            public final String snapshot;

        }

    }

}