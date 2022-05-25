package me.mat.mapper.launch;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import me.mat.mapper.MCMapper;

import java.io.File;

public class LaunchWrapper {

    public static void main(String[] args) {
        OptionParser optionParser = new OptionParser();

        OptionSpec<String> versionOption = optionParser.accepts("version").withOptionalArg();
        OptionSpec<File> outputOption = optionParser.accepts("output").withOptionalArg().ofType(File.class);
        OptionSpec<File> workspaceDirectoryOption = optionParser.accepts("workspace").withRequiredArg().ofType(File.class);
        OptionSet optionSet = optionParser.parse(args);

        File workspaceDirectory = workspaceDirectoryOption.value(optionSet);
        if (workspaceDirectory == null) {
            workspaceDirectory = new File("workspace");
        }

        String version = versionOption.value(optionSet);
        if (version == null) {
            version = "latest";
        }

        new MCMapper(
                version,
                workspaceDirectory,
                outputOption.value(optionSet),
                null
        ).start();
    }

}
