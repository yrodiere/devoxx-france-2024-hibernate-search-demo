package org.hibernate.demos.quarkus.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "character_movie")
public class Person extends PanacheEntity {
   @FullTextField(analyzer = "english")
   public String name;

   @OneToMany(mappedBy = "character")
   public List<Dialogue> dialogues = new ArrayList<>();
}
