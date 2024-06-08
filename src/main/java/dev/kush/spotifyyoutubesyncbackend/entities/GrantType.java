package dev.kush.spotifyyoutubesyncbackend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "grant_types")
@Getter
@Setter
@NoArgsConstructor
public class GrantType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer grantTypeId;

    private String grantTypeName;

    public GrantType(String grantTypeName) {
        this.grantTypeName = grantTypeName;
    }
}