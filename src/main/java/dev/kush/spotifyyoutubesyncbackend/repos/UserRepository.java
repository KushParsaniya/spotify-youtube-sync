package dev.kush.spotifyyoutubesyncbackend.repos;


import dev.kush.spotifyyoutubesyncbackend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {


    @Query("select count(u) > 0 from User u where u.username = :username and u.spotifyUserId = :spotifyUserId")
    boolean existByUserNameAndSpotifyUserId(@Param("username") String username, @Param("spotifyUserId") String spotifyUserId);

    @Query("select u from User u where u.username = :username and u.spotifyUserId = :spotifyUserId")
    Optional<User> findByUsernameAndSpotifyUserId(@Param("username") String username, @Param("spotifyUserId") String spotifyUserId);
}