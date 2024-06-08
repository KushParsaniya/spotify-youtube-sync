package dev.kush.spotifyyoutubesyncbackend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "oauth2_apps")
@Getter
@Setter
@NoArgsConstructor
public class OAuth2Apps {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer appId;

    private String appName;

    private String authTokenUrl;

    private String accessTokenUrl;

    private String refreshTokenUrl;

    public OAuth2Apps(String appName, String authTokenUrl, String accessTokenUrl, String refreshTokenUrl) {
        this.appName = appName;
        this.authTokenUrl = authTokenUrl;
        this.accessTokenUrl = accessTokenUrl;
        this.refreshTokenUrl = refreshTokenUrl;
    }
}
