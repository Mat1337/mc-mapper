package me.mat.mapper.mojang;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.mat.mapper.mojang.version.VersionsManifest;
import me.mat.mapper.util.WebUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

@RequiredArgsConstructor
public enum MojangAPI {

    VERSIONS("mc/game/version_manifest.json");

    private static final String END_POINT = "https://launchermeta.mojang.com/";

    @NonNull
    private final String path;

    /**
     * Initializes the API
     *
     * @param workspace base folder that the api will initialize to
     * @return {@link VersionsManifest}
     */

    public static VersionsManifest init(File workspace) {
        // define a file that the versions should be stored into
        File file = new File(workspace, "versions.json");

        // if the file does not exist
        if (!file.exists()) {

            // log to console that the versions manifest is being downloaded
            System.out.println("[INFO]: Downloading the versions manifest");

            // download the manifest file
            WebUtil.download(VERSIONS.toString(), file);
        }

        // return the same file
        return VersionsManifest.load(file);
    }

    @Override
    public String toString() {
        return END_POINT + path;
    }

}
