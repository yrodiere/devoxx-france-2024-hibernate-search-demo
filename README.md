## Demo

1. Search something
2. Search 

## Code

```java
@VectorField(name = "embedding",
dimension = EmbeddingModelBridge.DIMENSION,
vectorSimilarity = VectorSimilarity.COSINE,
valueBridge = @ValueBridgeRef(type = EmbeddingModelBridge.class))
```
