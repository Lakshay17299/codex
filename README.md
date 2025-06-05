# MCP Spring Boot + MongoDB RAG Tutorial

This sample project outlines the steps to build a minimal RAG (Retrieval Augmented Generation) application using Spring Boot, MongoDB, and the MongoDB Graph feature.

## Prerequisites
- JDK 17 or later
- Maven
- MongoDB server (with Graph features enabled)

## Project Structure
```
├── pom.xml
└── src
    └── main
        ├── java
        │   └── com
        │       └── example
        │           └── rag
        │               └── RagApplication.java
        └── resources
            └── application.properties
```

## Setup Steps
1. **Initialize the Spring Boot project**
   ```bash
   mvn archetype:generate -DgroupId=com.example.rag \
     -DartifactId=mcp-spring-boot-mongo-rag -Dversion=1.0-SNAPSHOT \
     -DinteractiveMode=false
   ```

2. **Add Spring Boot and MongoDB dependencies** to `pom.xml`:
   ```xml
   <dependencies>
       <dependency>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-starter-web</artifactId>
       </dependency>
       <dependency>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-starter-data-mongodb</artifactId>
       </dependency>
   </dependencies>
   ```

3. **Configure MongoDB**
   Create `src/main/resources/application.properties` and set your connection URI:
   ```properties
   spring.data.mongodb.uri=mongodb://localhost:27017/ragdb
   ```

4. **Create `RagApplication`** with a simple REST endpoint that queries MongoDB using the Graph API.

5. **Run the application**:
   ```bash
   mvn spring-boot:run
   ```

## New Features

The project now includes a very simple similarity search and chatbot endpoint, along with a demo of `$graphLookup`.

- `POST /rag` &ndash; add a document. Each document stores a basic embedding.
- `GET /rag/search?q=text` &ndash; fetch the document and its linked neighbors using `$graphLookup`.
- `GET /rag/similarity?q=your+query` &ndash; returns the top matching documents using cosine similarity.
- `GET /rag/chat?q=your+question` &ndash; a toy chat endpoint that replies with the text of the most similar document.
- `GET /rag/chat-graph?q=your+question` &ndash; uses a graph lookup to traverse linked docs for a basic conversation.
- `GET /rag/chat-llm?q=your+question` &ndash; if no context is found in the graph, forwards the question to OpenRouter's LLM.
- Previous LLM answers are stored so repeated questions can be answered directly from MongoDB without another API call.
- Graph facts are verbalized via a `LangGraphService` so answers read naturally instead of returning raw triples.
- `POST /rag/fact?text=RCB+won+IPL+2025` &ndash; store a simple fact. Statements like
  "RCB won IPL 2025" automatically create nodes and relations in the graph.
- Questions like `Who won IPL 2025?` are answered from these relations before
  calling the LLM.


Embeddings are generated using a trivial length/character average approach in `EmbeddingUtil` to keep the example self-contained.

Set the `OPENROUTER_API_KEY` environment variable to enable the OpenRouter fallback.

If you encounter a `PKIX path building` error when the service contacts
`openrouter.ai`, the application configures an SSL context that trusts all
certificates to simplify local testing.

This example gives you a starting point to explore MongoDB Graph RAG in a Spring Boot application.
