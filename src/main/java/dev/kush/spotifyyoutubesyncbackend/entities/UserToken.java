package dev.kush.spotifyyoutubesyncbackend.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_tokens")
@Getter
@Setter
@NoArgsConstructor
public class UserToken implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userTokenId;

    private String accessToken;

    private String refreshToken;

    private String tokenType;

    private LocalDateTime createdAt;

    private LocalDateTime expiryAt;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_id")
    private OAuth2Apps oAuth2Apps;

    public UserToken(String accessToken, String refreshToken, String tokenType, LocalDateTime createdAt, LocalDateTime expiryAt, User user, OAuth2Apps oAuth2Apps) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = tokenType;
        this.createdAt = createdAt;
        this.expiryAt = expiryAt;
        this.user = user;
        this.oAuth2Apps = oAuth2Apps;
    }
}
