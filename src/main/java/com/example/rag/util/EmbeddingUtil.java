package com.example.rag.util;

public class EmbeddingUtil {
    public static double[] embed(String text) {
        // simple embedding based on length and average character code
        if (text == null || text.isEmpty()) {
            return new double[]{0.0, 0.0};
        }
        double length = text.length();
        double avg = text.chars().average().orElse(0.0);
        return new double[]{length, avg};
    }

    public static double cosineSimilarity(double[] v1, double[] v2) {
        if (v1.length != v2.length) {
            throw new IllegalArgumentException("Vectors must be the same length");
        }
        double dot = 0.0;
        double mag1 = 0.0;
        double mag2 = 0.0;
        for (int i = 0; i < v1.length; i++) {
            dot += v1[i] * v2[i];
            mag1 += v1[i] * v1[i];
            mag2 += v2[i] * v2[i];
        }
        if (mag1 == 0 || mag2 == 0) {
            return 0.0;
        }
        return dot / (Math.sqrt(mag1) * Math.sqrt(mag2));
    }
}
