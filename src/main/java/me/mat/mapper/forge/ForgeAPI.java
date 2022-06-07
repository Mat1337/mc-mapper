package me.mat.mapper.forge;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.mat.mapper.forge.version.ForgeVersion;
import me.mat.mapper.forge.version.ForgeVersionManifest;
import me.mat.mapper.util.UnzipUtility;
import me.mat.mapper.util.WebUtil;
import net.minecraftforge.srgutils.IMappingFile;
import net.minecraftforge.srgutils.IRenamer;

import java.io.File;
import java.io.IOException;

@RequiredArgsConstructor
public enum ForgeAPI {


    MAPPINGS(
            "mcp_stable/%RELEASE%-%VERSION%/mcp_stable-%RELEASE%-%VERSION%.zip",
            "mcp/%VERSION%/mcp-%VERSION%-srg.zip"
    );

    @NonNull
    final String mcp_stable;

    @NonNull
    final String mcp_srg;

    public File setup(File directory, String version) {
        // load the manifest
        ForgeVersionManifest versionManifest = ForgeVersionManifest.load();

        // get the version
        ForgeVersion forgeVersion = versionManifest.getVersion(version);

        // define the mcp file
        File mcpFile = new File(directory, "mcp.zip");

        // if the mcp file does not exist
        if (!mcpFile.exists()) {

            // download the mcp zip
            WebUtil.download(getMCPStableLink(forgeVersion.getRelease(), version), mcpFile);
        }

        // define the srg file
        File srgFile = new File(directory, "srg.zip");

        // if the srg file does not exist
        if (!srgFile.exists()) {

            // download the mcp zip
            WebUtil.download(getSrgLink(version), srgFile);
        }

        // unzip the files
        try {
            UnzipUtility.unzip(srgFile.getAbsolutePath(), directory.getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        File joinedSRG = new File(directory, "joined.srg");
        File srgMcpFile = new File(directory, "srg-mcp.srg");

        try {
            IMappingFile mappingFile = IMappingFile.load(joinedSRG);

            McpNames map = McpNames.load(mcpFile);

            IMappingFile ret = mappingFile.rename(new IRenamer() {
                @Override
                public String rename(IMappingFile.IField value) {
                    return map.rename(value.getMapped());
                }

                @Override
                public String rename(IMappingFile.IMethod value) {
                    return map.rename(value.getMapped());
                }
            });

            ret.write(srgMcpFile.toPath(), IMappingFile.Format.SRG, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // return the manifest
        return srgMcpFile;
    }

    private String getMCPStableLink(String release, String version) {
        return this + mcp_stable
                .replaceAll("%RELEASE%", release)
                .replaceAll("%VERSION%", version);
    }

    private String getSrgLink(String version) {
        return this + mcp_srg.replaceAll("%VERSION%", version);
    }

    @Override
    public String toString() {
        return "https://maven.minecraftforge.net/de/oceanlabs/mcp/";
    }

}
