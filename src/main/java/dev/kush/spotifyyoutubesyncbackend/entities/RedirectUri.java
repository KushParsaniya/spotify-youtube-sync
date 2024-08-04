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
import org.springframework.data.redis.core.RedisHash;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "redirect_uris")
@Getter
@Setter
@NoArgsConstructor
@RedisHash("RedirectUri")
public class RedirectUri implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer redirectUriId;

    @Column(name = "uri")
    private String redirectUri;

    public RedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }
}
