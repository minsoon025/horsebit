package com.a406.horsebit.google.domain;

import lombok.Getter;

@Getter
public class OAuthProvider {
    private String jwks;

    public OAuthProvider(OAuthProperties.Provider provider) {
        this.jwks = provider.getJwks();
    }
}
