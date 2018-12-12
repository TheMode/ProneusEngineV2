package fr.proneus.engine.motable.manager;

import fr.proneus.engine.graphic.Image;
import fr.proneus.engine.graphic.Texture;
import fr.themode.utils.file.FileUtils;

import java.util.HashMap;
import java.util.Map;

public class TextureManager {

    private Map<String, Texture> texturesByFileId;

    private Map<String, String> textures;

    public TextureManager(Map<String, String> textures) {
        this.texturesByFileId = new HashMap<>();
        this.textures = textures;
    }

    public void load() {
        this.textures.entrySet().forEach(entry -> {
            String fileId = entry.getKey();
            String path = entry.getValue();
            Texture texture = new Texture(new Image(FileUtils.getExternalFile(path)));
            texturesByFileId.put(fileId, texture);
        });
    }

    public Texture getTextureByFileId(String fileId) {
        return texturesByFileId.get(fileId);
    }

}
