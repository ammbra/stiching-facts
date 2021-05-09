package org.acme.experience.stichingfacts.client.model;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@NoArgsConstructor
@Entity
public class EnhancedFact extends Fact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    public Double randomness;

    @Embedded
    public User user;

    public EnhancedFact(Fact fact) {
        this._id = fact._id;
        this.type = fact.type;
        this.text = fact.text;
        this.source = fact.source;
        this.status = fact.status;
        this.createdAt = fact.createdAt;
        this.deleted = fact.deleted;
    }
}
