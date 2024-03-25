package org.hibernate.demos.quarkus.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.ws.rs.client.Client;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "character_movie")
@Indexed
public class Character extends PanacheEntity {
   @FullTextField(analyzer = "english")
   public String name;

   @OneToMany(mappedBy = "character")
   public List<Dialogue> dialogues = new ArrayList<>();
}
