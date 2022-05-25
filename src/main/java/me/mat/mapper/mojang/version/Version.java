package me.mat.mapper.mojang.version;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import me.mat.mapper.util.WebUtil;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@AllArgsConstructor
public final class Version {

    private static final Gson GSON = new GsonBuilder().serializeNulls().setPrettyPrinting().create();

    @NonNull
    @SerializedName("id")
    public final String id;

    @NonNull
    @SerializedName("type")
    public final String type;

    @NonNull
    @SerializedName("url")
    public final String url;

    @NonNull
    @SerializedName("time")
    public final String time;

    @NonNull
    @SerializedName("releaseTime")
    public final String releaseTime;

    @NonNull
    @SerializedName("sha1")
    public final String sha1;

    @NonNull
    @SerializedName("complianceLevel")
    public final int complianceLevel;

    /**
     * Loads the manifest for the current version
     *
     * @param workspace workspace directory
     * @return {@link Manifest}
     */

    public Manifest load(File workspace) {
        // define a file to the version manifest
        File file = new File(workspace, id + ".json");

        // if the file does not exist
        if (!file.exists()) {

            // log to console that the version manifest is being downloaded
            System.out.println("[INFO]: Downloading the manifest for version '" + id + "'");

            // download the version manifest
            WebUtil.download(url, file);
        }

        // load the manifest into memory and return it
        return Manifest.load(file);
    }

    @AllArgsConstructor
    public static final class Manifest {

        @NonNull
        @SerializedName("assetIndex")
        public final AssetIndex assetIndex;

        @NonNull
        @SerializedName("assets")
        public final String assets;

        @NonNull
        @SerializedName("downloads")
        public final Downloads downloads;

        @NonNull
        @SerializedName("id")
        public final String id;

        @NonNull
        @SerializedName("javaVersion")
        public final JavaVersion javaVersion;

        @NonNull
        @SerializedName("libraries")
        public final Library[] libraries;

        @NonNull
        @SerializedName("logging")
        public final Logging logging;

        @NonNull
        @SerializedName("mainClass")
        public final String mainClass;

        /**
         * Loads the manifest from
         * the provided file
         *
         * @param file file that you want to load the manifest from
         * @return @{@link Manifest}
         */

        public static Manifest load(File file) {
            try (FileReader fileReader = new FileReader(file)) {
                return GSON.fromJson(fileReader, Manifest.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @AllArgsConstructor
        public static final class AssetIndex {

            @NonNull
            @SerializedName("id")
            public final String id;

            @NonNull
            @SerializedName("sha1")
            public final String sha1;

            @NonNull
            @SerializedName("size")
            public final long size;

            @NonNull
            @SerializedName("totalSize")
            public final long totalSize;

            @NonNull
            @SerializedName("url")
            public final String url;

        }

        @AllArgsConstructor
        public static final class Library {

            @NonNull
            @SerializedName("downloads")
            public final Downloads downloads;

            @NonNull
            @SerializedName("name")
            public final String name;

            @SerializedName("natives")
            public final Natives natives;

            @SerializedName("rules")
            public final Rule[] rules;

            @AllArgsConstructor
            public static final class Downloads {

                @NonNull
                @SerializedName("artifact")
                public final Artifact artifact;

                @SerializedName("classifiers")
                public final Classifiers classifiers;

                @AllArgsConstructor
                public static final class Artifact {

                    @NonNull
                    @SerializedName("path")
                    public final String path;

                    @NonNull
                    @SerializedName("sha1")
                    public final String sha1;

                    @NonNull
                    @SerializedName("size")
                    public final long size;

                    @NonNull
                    @SerializedName("url")
                    public final String url;

                }

                @AllArgsConstructor
                public static final class Classifiers {

                    @SerializedName("javadoc")
                    public final Classifier javadoc;

                    @SerializedName("natives-linux")
                    public final Classifier nativesLinux;

                    @SerializedName("natives-macos")
                    public final Classifier nativesMacos;

                    @SerializedName("natives-windows")
                    public final Classifier nativesWindows;

                    @SerializedName("sources")
                    public final Classifier sources;

                    @AllArgsConstructor
                    public static final class Classifier {

                        @NonNull
                        @SerializedName("path")
                        public final String path;

                        @NonNull
                        @SerializedName("sha1")
                        public final String sha1;

                        @NonNull
                        @SerializedName("size")
                        public final long size;

                        @NonNull
                        @SerializedName("url")
                        public final String url;

                    }

                }

            }

            @AllArgsConstructor
            public static final class Rule {

                @NonNull
                @SerializedName("action")
                public final String action;

                @SerializedName("os")
                public final OS os;

                @AllArgsConstructor
                public static final class OS {

                    @NonNull
                    @SerializedName("name")
                    public final String name;

                }

            }

        }

        @AllArgsConstructor
        public static final class Downloads {

            @NonNull
            @SerializedName("client")
            public final Download client;

            @SerializedName("client_mappings")
            public final Download mappings;

            @AllArgsConstructor
            public static final class Download {

                @NonNull
                @SerializedName("sha1")
                public final String sha1;

                @NonNull
                @SerializedName("size")
                public final long size;

                @NonNull
                @SerializedName("url")
                public final String url;

            }

        }

        @AllArgsConstructor
        public static final class Natives {

            @SerializedName("windows")
            public final String windows;

            @SerializedName("osx")
            public final String osx;

            @SerializedName("linux")
            public final String linux;


        }

        @AllArgsConstructor
        public static final class JavaVersion {

            @NonNull
            @SerializedName("java-runtime-beta")
            public final String javaRuntimeBeta;

            @NonNull
            @SerializedName("majorVersion")
            public final String majorVersion;

        }

        @AllArgsConstructor
        public static final class Logging {

            @NonNull
            @SerializedName("client")
            public final Log client;

            @AllArgsConstructor
            public static final class Log {

                @NonNull
                @SerializedName("argument")
                public final String argument;

                @NonNull
                @SerializedName("file")
                public final Log.File file;

                @NonNull
                @SerializedName("type")
                public final String type;

                @AllArgsConstructor
                public static final class File {

                    @NonNull
                    @SerializedName("id")
                    public final String id;

                    @NonNull
                    @SerializedName("sha1")
                    public final String sha1;

                    @NonNull
                    @SerializedName("size")
                    public final int size;

                    @NonNull
                    @SerializedName("url")
                    public final String url;

                }

            }

        }

    }

}