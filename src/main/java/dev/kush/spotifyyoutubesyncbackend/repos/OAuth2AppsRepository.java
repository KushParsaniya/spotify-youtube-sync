package dev.kush.spotifyyoutubesyncbackend.repos;


import dev.kush.spotifyyoutubesyncbackend.entities.OAuth2Apps;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OAuth2AppsRepository extends JpaRepository<OAuth2Apps, Integer> {

    @Query("SELECT o FROM OAuth2Apps o WHERE lower(o.appName) = lower(:appName)")
    Optional<OAuth2Apps> findByAppName(@Param("appName") String appName);


}