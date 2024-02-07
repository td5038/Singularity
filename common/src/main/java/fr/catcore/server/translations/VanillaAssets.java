package fr.catcore.server.translations;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import dev.architectury.platform.Platform;
import net.minecraft.DetectedVersion;

public final class VanillaAssets {
    private final Map<String, String> hashes;

    private VanillaAssets(Map<String, String> hashes) {
        this.hashes = hashes;
    }

    public String getHash(String resource) {
        return this.hashes.get(resource);
    }

    public URL getUrl(String resource) throws MalformedURLException {
        String hash = this.getHash(resource);
        
        if (hash == null) {
            throw new IllegalArgumentException("missing hash for " + resource);
        }
        
        return new URL("https://resources.download.minecraft.net/" + hash.substring(0,2) + "/" + hash);
    }

    public InputStream openStream(String resource) throws IOException {
        return this.getUrl(resource).openStream();
    }

    public static VanillaAssets get() throws IOException {
        Map<String, String> hashes = new HashMap<>();
        URL resourceManifestURL = VanillaAssets.getResourceManifestURL();
        
        try (BufferedReader read = new BufferedReader(new InputStreamReader(resourceManifestURL.openStream()))) {
            JsonObject jsonObject = JsonParser.parseReader(read).getAsJsonObject();
            JsonObject resourceArray = jsonObject.getAsJsonObject("objects");

            for (Map.Entry<String, JsonElement> entry : resourceArray.entrySet()) {
                if (entry.getKey().equals("pack.mcmeta") || entry.getKey().startsWith("minecraft/lang/")) {
                    hashes.put(entry.getKey(), entry.getValue().getAsJsonObject().get("hash").getAsString());
                }
            }
        }

        return new VanillaAssets(hashes);
    }

    public static URL getResourceManifestURL() throws IOException {
        URL versionURL = VanillaAssets.getVersionURL();
        
        try (BufferedReader read = new BufferedReader(new InputStreamReader(versionURL.openStream()))) {
            JsonObject jsonObject = JsonParser.parseReader(read).getAsJsonObject();
        
            return new URL(jsonObject.getAsJsonObject("assetIndex").get("url").getAsString());
        }
    }

    public static URL getVersionURL() throws IOException {
        URL manifestURL = new URL("https://launchermeta.mojang.com/mc/game/version_manifest.json");
        
        try (BufferedReader read = new BufferedReader(new InputStreamReader(manifestURL.openStream()))) {
            JsonObject jsonObject = JsonParser.parseReader(read).getAsJsonObject();
            JsonArray versions = jsonObject.getAsJsonArray("versions");
            Iterator<JsonElement> iterator = versions.iterator();
            List<JsonObject> versionObjects = new ArrayList<>();
        
            while (iterator.hasNext()) {
                versionObjects.add((JsonObject) iterator.next());
            }
        
            for (JsonObject version : versionObjects) {
                try {
                    if (version.get("id").getAsString().equals(DetectedVersion.BUILT_IN.getName())) {
                        return new URL(version.get("url").getAsString());
                    }
                } catch (NoSuchFieldError error) {
                    if (version.get("id").getAsString().equals(DetectedVersion.tryDetectVersion().getName())) {
                        return new URL(version.get("url").getAsString());
                    }
                } catch (NoClassDefFoundError error) {
                    if (version.get("id").getAsString().equals(Platform.getMod("minecraft").getVersion())) {
                        return new URL(version.get("url").getAsString());
                    }
                }
            }
        }
        
        throw new IOException("couldn't find version url");
    }
}
