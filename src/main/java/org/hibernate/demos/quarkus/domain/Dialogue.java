package org.hibernate.demos.quarkus.domain;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import org.hibernate.Length;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;


@Entity(name = "dialogue")
@Indexed
public class Dialogue extends PanacheEntity {

   @FullTextField(analyzer = "english")
   @Column(length = Length.LONG)
   public String text;

   @ManyToOne
   @IndexedEmbedded
   public Person character;
}
