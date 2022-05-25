package me.mat.mapper;

import me.mat.mapper.mojang.version.Version;

import java.io.File;

public interface RemapCallback {

    void onRemapFinish(File jarFile, Version.Manifest currentVersion);

}
