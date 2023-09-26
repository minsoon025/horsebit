package com.a406.horsebit.google.repository;

import com.a406.horsebit.google.domain.OAuthProvider;

import java.util.Map;
import java.util.TreeMap;

public class InMemoryProviderRepository {
    private final Map<String, OAuthProvider> providers;

    public InMemoryProviderRepository(Map<String, OAuthProvider> providers) {
        this.providers = new TreeMap<>(providers);
    }

    public OAuthProvider findByProviderName(String name) {
        if (!providers.containsKey(name)) {
//            throw new UnsupportedProviderException("지원하지 않는 provider 입니다.");
        }

        return providers.get(name);
    }
}
