package dev.kush.spotifyyoutubesyncbackend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "clients")
@Getter
@Setter
@NoArgsConstructor
@RedisHash
public class Client implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String clientId;

    private String clientSecret;

    @ManyToMany
    @JoinTable(
            name = "clients_grant_types",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "grant_type_id")
    )
    private List<GrantType> grantType;

    @ManyToMany
    @JoinTable(
            name = "clients_redirect_uris",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "redirect_uri_id")
    )
    private List<RedirectUri> redirectUri;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_id")
    private OAuth2Apps oAuth2Apps;

    @OneToMany(mappedBy = "client")
    private List<Scope> scopes;

    public Client(String clientId, String clientSecret, List<GrantType> grantType, List<RedirectUri> redirectUri, OAuth2Apps oAuth2Apps) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.grantType = grantType;
        this.redirectUri = redirectUri;
        this.oAuth2Apps = oAuth2Apps;
    }
}
