package dev.kush.spotifyyoutubesyncbackend.repos;


import dev.kush.spotifyyoutubesyncbackend.entities.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserTokenRepository extends JpaRepository<UserToken, Integer> {

    @Query("select ut from UserToken ut where ut.user.userId = :userId")
    Optional<UserToken> findByUserId(@Param("userId") Integer userId);

    //    @Query("select ut from UserToken ut where ut.user.spotifyUserId =:spotifyUserId")
    @Query("select ut from UserToken ut join ut.user u where u.spotifyUserId = :spotifyUserId")
    Optional<UserToken> findBySpotifyUserId(@Param("spotifyUserId") String spotifyUserId);

    //    @Query("select ut from UserToken ut where ut.user.youtubeUserId =:youtubeUserId")
    @Query("select ut from UserToken ut join ut.user u where u.youtubeUserId = :youtubeUserId")
    Optional<UserToken> findByYoutubeUserId(@Param("youtubeUserId") String youtubeUserId);


}