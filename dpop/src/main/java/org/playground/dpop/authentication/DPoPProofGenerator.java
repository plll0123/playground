package org.playground.dpop.authentication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.Signature;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class DPoPProofGenerator {
    private final DPoPKeyManager keyManager;

    public DPoPProofGenerator(DPoPKeyManager keyManager) {
        this.keyManager = keyManager;
    }

    public String createProof(String httpMethod, String httpUri) {
        Instant now = Instant.now();
        return Jwts.builder()
                .header()
                .type("dpop+jwt")              // typ: dpop+jwt
                .add("jwk", keyManager.getPublicJwk()) // ★ 공개키를 헤더에
                .and()
                .claim("htm", httpMethod)
                .claim("htu", httpUri)
                .issuedAt(Date.from(now))            // iat
                .id(UUID.randomUUID().toString())    // jti
                .signWith(keyManager.getPrivateKey(), Jwts.SIG.ES256) // ES256, P1363 자동
                .compact();
    }
}
