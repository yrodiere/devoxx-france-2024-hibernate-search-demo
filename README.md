## Demo

1. Search something
2. Search on an embedding (character)
3. Search

## Code

```java
@VectorField(name = "embedding",
dimension = EmbeddingModelBridge.DIMENSION,
vectorSimilarity = VectorSimilarity.COSINE,
valueBridge = @ValueBridgeRef(type = EmbeddingModelBridge.class))
```

* Search "greeting" (knn and )
* Search "magic"
* Add Devoxx
* Add I love coding java 
