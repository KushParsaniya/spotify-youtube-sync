package dev.kush.spotifyyoutubesyncbackend.services.oauth2.impl;


import dev.kush.spotifyyoutubesyncbackend.dtos.AllOAuth2Info;
import dev.kush.spotifyyoutubesyncbackend.repos.ClientRepository;
import dev.kush.spotifyyoutubesyncbackend.services.oauth2.OAuth2Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuth2ServiceImpl implements OAuth2Service {

    private final ClientRepository clientRepository;

    @Cacheable(value = "oAuth2Info", key = "#appName")
    @Override
    public List<AllOAuth2Info> getAllInfoFromAppName(String appName) {
        List<AllOAuth2Info> optionalAllOAuth2Info = clientRepository.findAllByAppName(appName);

        if (optionalAllOAuth2Info.isEmpty()) {
            log.error("OAuth2Service :: getAllInfoFromAppName  --> error app not found");
            // TODO : handle error
            throw new RuntimeException();
        }
        return optionalAllOAuth2Info;
    }
}
