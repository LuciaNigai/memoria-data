package com.lucia.memoria.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExternalAPIConfig {
    @Value("${FREE_DICTIONARY_API}")
    private  String freeDictionaryUrl;
    @Value("${GOOGLE_TRANSLATE_API}")
    private  String googleTranslate;

    public String getFreeDictionaryUrl() {
        return freeDictionaryUrl;
    }

    public String getGoogleTranslate() {
        return googleTranslate;
    }
}
