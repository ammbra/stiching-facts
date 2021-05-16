package org.acme.experience.stichingfacts.client.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@NoArgsConstructor
@Entity
public class EnhancedFact extends Fact {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Getter
    @Setter
    private String factId;

    @Getter
    @Setter
    private Double randomness;

    @Embedded
    @Getter
    @Setter
    private User user;

    @Getter
    @Setter
    private String type;

    @Getter
    @Setter
    private String source;

    @Embedded
    @Getter
    @Setter
    private Status status;

    public EnhancedFact(Fact fact) {
        this.factId = this._id = fact._id;
        this.type = fact.type;
        this.source = fact.source;
        this.status = fact.status;
    }
}
