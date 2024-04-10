package org.hibernate.demos.quarkus.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import org.hibernate.Length;
import org.hibernate.demos.quarkus.ai.EmbeddingModelBridge;
import org.hibernate.search.engine.backend.types.VectorSimilarity;
import org.hibernate.search.mapper.pojo.bridge.mapping.annotation.ValueBridgeRef;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.VectorField;

@Entity(name = "dialogue")
@Indexed
public class Dialogue extends PanacheEntity {

   @ManyToOne
   @IndexedEmbedded
   public Character character;

   @FullTextField(analyzer = "english")
   @VectorField(name = "embedding",
           dimension = EmbeddingModelBridge.DIMENSION,
           vectorSimilarity = VectorSimilarity.COSINE,
           valueBridge = @ValueBridgeRef(type = EmbeddingModelBridge.class))
   @Column(length = Length.LONG)
   public String text;
}
