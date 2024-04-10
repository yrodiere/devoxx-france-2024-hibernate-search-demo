package org.hibernate.demos.quarkus.ai;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.AllMiniLmL6V2QuantizedEmbeddingModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import jakarta.enterprise.context.ApplicationScoped;
import org.hibernate.search.engine.backend.types.VectorSimilarity;
import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.mapping.annotation.ValueBridgeRef;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.VectorField;

@ApplicationScoped
public class EmbeddingModelBridge implements ValueBridge<String, float[]> {
   public static final int DIMENSION = 384;

   EmbeddingModel embeddingModel = new AllMiniLmL6V2QuantizedEmbeddingModel();

   @Override
   public float[] toIndexedValue(String sentence, ValueBridgeToIndexedValueContext context) {
      return toEmbedding(sentence);
   }

   public float[] toEmbedding(String sentence) {
      TextSegment segment = TextSegment.from(sentence);
      Embedding embedding = embeddingModel.embed(segment.text()).content();
      return embedding.vector();
   }
//   @VectorField(name = "embedding",
//           dimension = EmbeddingModelBridge.DIMENSION,
//           vectorSimilarity = VectorSimilarity.COSINE,
//           valueBridge = @ValueBridgeRef(type = EmbeddingModelBridge.class))

}