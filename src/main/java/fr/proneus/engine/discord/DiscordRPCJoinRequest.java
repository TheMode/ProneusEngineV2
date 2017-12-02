package fr.proneus.engine.discord;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import fr.proneus.engine.graphic.Image;

/**
 * Created by Adamaq01 on 02/12/2017.
 */
public class DiscordRPCJoinRequest {

    private String userId;
    private String username;
    private String discriminator;
    private Image avatar;

    public DiscordRPCJoinRequest(String userId, String username, String discriminator, String avatar) {
        this.userId = userId;
        this.username = username;
        this.discriminator = discriminator;
        try {
            this.avatar = new Image(Unirest.get("https://cdn.discordapp.com/avatars/" + userId + "/" + avatar + ".png").header("data", "image/png;base64").asBinary().getBody());
        } catch (UnirestException e) {
            this.avatar = null;
            System.out.println("Avatar not loaded");
        }
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getDiscriminator() {
        return discriminator;
    }

    public Image getAvatar() {
        return avatar;
    }
}
