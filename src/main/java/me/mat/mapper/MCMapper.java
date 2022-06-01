package me.mat.mapper;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.mat.jprocessor.JProcessor;
import me.mat.jprocessor.jar.memory.MemoryJar;
import me.mat.jprocessor.mappings.MappingLoadException;
import me.mat.jprocessor.mappings.MappingManager;
import me.mat.jprocessor.mappings.MappingType;
import me.mat.mapper.mojang.MojangAPI;
import me.mat.mapper.mojang.version.Version;
import me.mat.mapper.mojang.version.VersionsManifest;
import me.mat.mapper.util.WebUtil;

import java.io.File;
import java.io.FileNotFoundException;

@RequiredArgsConstructor
public class MCMapper extends Thread {

    @NonNull
    private final String versionID;

    @NonNull
    private final File workspaceDirectory;

    private final File outputFile;

    private final RemapCallback callback;

    @Getter
    private File clientJar;

    @Getter
    private File mappingsFile;

    @Getter
    private Version.Manifest currentVersion;

    @Override
    public void run() {
        // setup the workspace
        String versionID = setupWorkspace();

        // load the client jar into memory
        MemoryJar memoryJar;
        try {
            memoryJar = JProcessor.Jar.load(clientJar);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        // load the mappings into the memory
        MappingManager mappingManager;
        try {
            mappingManager = JProcessor.Mapping.load(memoryJar, mappingsFile, MappingType.PROGUARD);
        } catch (MappingLoadException e) {
            throw new RuntimeException(e);
        }

        // remap the jar
        memoryJar.remap(mappingManager);

        // define the source file that the jar will export to
        File sourceFile = outputFile == null ? new File(workspaceDirectory, versionID + "-Source.jar") : outputFile;

        // if the source file exists
        if (sourceFile.exists()) {

            // log to console that the source file will be overwritten
            System.out.println("[WARN]: " + sourceFile.getName() + " already exists, it will be overwritten");
        }

        // save the jar to the source file
        memoryJar.save(sourceFile);

        // if the callback was provided
        if (callback != null) {

            // call the callback
            callback.onRemapFinish(clientJar, currentVersion);
        }
    }

    String setupWorkspace() {
        // log to console that api is being initialized
        System.out.println("[INFO]: Initializing MojangAPI");

        // initialize the mojang api
        VersionsManifest versionsManifest = MojangAPI.init(workspaceDirectory);

        // get the version that will be unmapped
        Version version = versionsManifest.getVersion(versionID);

        // log to console that the version data is being loaded into memory
        System.out.println("[INFO]: Loading version data");

        // load the version into memory
        currentVersion = version.load(workspaceDirectory);

        // define the version file
        clientJar = new File(workspaceDirectory, version.id + ".jar");

        // if the client file does not exist
        if (!clientJar.exists()) {

            // download the client file
            WebUtil.download(currentVersion.downloads.client.url, clientJar);
        }

        // define the mappings file
        mappingsFile = new File(workspaceDirectory, version.id + ".mappings");

        // if the mappings file does not exist
        if (!mappingsFile.exists()) {

            // download the mappings file
            WebUtil.download(currentVersion.downloads.mappings.url, mappingsFile);
        }

        // return the version id
        return version.id;
    }

}
