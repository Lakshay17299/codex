# MCP Spring Boot + MongoDB RAG Tutorial

This sample project outlines the steps to build a minimal RAG (Retrieval Augmented Generation) application using Spring Boot, MongoDB, and the MongoDB Graph feature.

## Prerequisites
- JDK 17 or later
- Maven
- MongoDB server (with Graph features enabled)

## Project Structure
```
mcp-spring-boot-mongo-rag
├── pom.xml
└── src
    └── main
        └── java
            └── com
                └── example
                    └── rag
                        └── RagApplication.java
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

3. **Create `RagApplication`** with a simple REST endpoint that queries MongoDB using the Graph API.

4. **Run the application**:
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

Embeddings are generated using a trivial length/character average approach in `EmbeddingUtil` to keep the example self-contained.

This example gives you a starting point to explore MongoDB Graph RAG in a Spring Boot application.
