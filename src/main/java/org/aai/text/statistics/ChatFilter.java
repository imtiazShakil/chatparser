package org.aai.text.statistics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ChatFilter {
    private static final ChatFilter INSTANCE = new ChatFilter();
    private static Set<String> stopWordSet;

    private ChatFilter() {
        if (INSTANCE != null) {
            throw new IllegalStateException("ChatFilter Already instantiated");
        }
        stopWordSet = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(ChatFilter.class.getClassLoader().getResourceAsStream("UnnecessarySigns.txt")))) {
            String line = br.readLine();
            while (line != null) {
                if (line.length() > 0) stopWordSet.add(line);
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static ChatFilter getInstance() {
        return INSTANCE;
    }

    public boolean isUnnecessary(String line) {
        return stopWordSet.contains(line.trim());
    }
}
