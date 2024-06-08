package dev.kush.spotifyyoutubesyncbackend.repos;


import dev.kush.spotifyyoutubesyncbackend.entities.RedirectUri;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RedirectUriRepository extends JpaRepository<RedirectUri, Integer> {
}