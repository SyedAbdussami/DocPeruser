package com.AI_Documizer.DocPeruser.Services;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DocumentService {

    private final VectorStore vectorStore;
    private  final ChatModel chatModel;

    public DocumentService(VectorStore vectorStore, ChatModel chatModel){
        this.vectorStore=vectorStore;
        this.chatModel=chatModel;
    }
    private String prompt = """
            Your task is to answer the questions about Distributed Systems. Use the information from the DOCUMENTS
            section to provide accurate answers. If unsure or if the answer isn't found in the DOCUMENTS section, 
            simply state that you don't know the answer.
                        
            QUESTION:
            {input}
                        
            DOCUMENTS:
            {documents}
                        
            """;
    public String getAnswer(String question){
        PromptTemplate template = new PromptTemplate(prompt);
        Map<String, Object> promptsParameters = new HashMap<>();
        promptsParameters.put("input", question);
        promptsParameters.put("documents", findSimilarData(question));
        return chatModel
                .call(template.create(promptsParameters))
                .getResult()
                .getOutput()
                .getContent();
    }

    private String findSimilarData(String question) {
        List<Document> documents = vectorStore.similaritySearch(SearchRequest.query(question).withTopK(5));

        return documents
                .stream()
                .map(document -> document.getContent().toString())
                .collect(Collectors.joining());

    }
}
