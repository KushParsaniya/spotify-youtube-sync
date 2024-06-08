package dev.kush.spotifyyoutubesyncbackend.repos;


import dev.kush.spotifyyoutubesyncbackend.dtos.AllOAuth2Info;
import dev.kush.spotifyyoutubesyncbackend.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Integer> {

    @Query("""
            select new dev.kush.spotifyyoutubesyncbackend.dtos.AllOAuth2Info(o, c, r, g, s)
            from Client c
            join c.oAuth2Apps o
            join c.grantType g
            join c.redirectUri r
            join c.scopes s
            where lower(o.appName) = lower(:appName)
            order by r.redirectUriId asc
            """)
    List<AllOAuth2Info> findAllByAppName(@Param("appName") String appName);
}