package dev.kush.spotifyyoutubesyncbackend.services.uri.impl;

import dev.kush.spotifyyoutubesyncbackend.services.uri.UriParamExtractor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class UriParamExtractorImpl implements UriParamExtractor {

    @Override
    public String extractPlayListIdFromPlaylistLink(String link) {
        URI uri = null;
        try {
            uri = new URI(link);
        } catch (URISyntaxException e) {
            log.error("UriParamExtractorImpl :: extractPlayListIdFromPlaylistLink :: {}", e.getMessage());
            throw new RuntimeException(e);
        }
        String query = uri.getQuery();
        Pattern pattern = Pattern.compile("list=([a-zA-Z0-9_-]+)");
        Matcher matcher = pattern.matcher(query);

        if (matcher.find()) {
            return matcher.group(1);
        }
        return null; // or throw an exception if the parameter is mandatory
    }
}
