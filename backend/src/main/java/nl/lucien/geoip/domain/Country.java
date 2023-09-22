package nl.lucien.geoip.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import lombok.Builder;
import lombok.Data;

@Data 
@Builder
public class Country {

    @Id
    @Column("code")
    private String code; 
    
    @Column("full_name")
    private String name;
}
