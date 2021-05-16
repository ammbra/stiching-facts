package org.acme.experience.stichingfacts.client.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class Fact {

    public String _id;

    public String type;
    public String text;
    public String source;

    public Status status;
    public boolean deleted;
    public String user;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", shape = JsonFormat.Shape.STRING)
    public Date updatedAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", shape = JsonFormat.Shape.STRING)
    public Date createdAt;

    public boolean used;

}
