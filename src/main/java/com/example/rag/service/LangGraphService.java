package com.example.rag.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LangGraphService {
    @Autowired
    private OpenRouterService openRouterService;

    /**
     * Generate a natural sentence describing the given triple using the LLM.
     */
    public String verbalize(String subject, String verb, String object) {
        String prompt = "Use the following subject, verb and object to " +
                "create a short natural sentence: " + subject + " " + verb + " " + object;
        return openRouterService.chat(prompt);
    }
}
