package org.acme.experience.stichingfacts.client.model;

import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Id;

@NoArgsConstructor
@Embeddable
public class User {

    @Id
    public String _id;

    @Embedded
    public Name name;
    public String photo;
}

@NoArgsConstructor
@Embeddable
class Name {
    public String first;
    public String last;
}