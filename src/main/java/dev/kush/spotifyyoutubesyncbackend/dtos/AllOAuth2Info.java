package dev.kush.spotifyyoutubesyncbackend.dtos;


import dev.kush.spotifyyoutubesyncbackend.entities.Client;
import dev.kush.spotifyyoutubesyncbackend.entities.GrantType;
import dev.kush.spotifyyoutubesyncbackend.entities.OAuth2Apps;
import dev.kush.spotifyyoutubesyncbackend.entities.RedirectUri;
import dev.kush.spotifyyoutubesyncbackend.entities.Scope;

import java.io.Serial;
import java.io.Serializable;

public record AllOAuth2Info (
        OAuth2Apps oAuth2Apps,
        Client client,
        RedirectUri redirectUri,
        GrantType grantType,
        Scope scope
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}
