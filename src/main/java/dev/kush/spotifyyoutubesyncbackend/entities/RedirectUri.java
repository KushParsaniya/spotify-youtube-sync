package dev.kush.spotifyyoutubesyncbackend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "redirect_uris")
@Getter
@Setter
@NoArgsConstructor
public class RedirectUri {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer redirectUriId;

    @Column(name = "uri")
    private String redirectUri;

    public RedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }
}
