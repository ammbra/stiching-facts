package org.acme.experience.stichingfacts.client.model;

import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@NoArgsConstructor
@Embeddable
public class Status {
   public boolean verified;
   public int sentCount;
   public String feedback;
}
