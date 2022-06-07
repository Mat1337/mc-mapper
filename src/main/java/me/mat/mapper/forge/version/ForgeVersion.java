package me.mat.mapper.forge.version;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
public final class ForgeVersion {

    @NonNull
    @SerializedName("snapshot")
    public final String[] snapshot;

    @NonNull
    @SerializedName("stable")
    public final String[] stable;

    /**
     * Gets the best release for the current version
     *
     * @return {@link String}
     */

    public String getRelease() {
        if (stable == null || stable.length == 0) {
            if (snapshot == null || snapshot.length == 0) {
                throw new RuntimeException("Failed to fetch a release");
            }
            return snapshot[0];
        }
        return stable[0];
    }

}
