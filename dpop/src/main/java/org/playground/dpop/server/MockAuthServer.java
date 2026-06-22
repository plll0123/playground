package org.playground.dpop.server;

import io.jsonwebtoken.Jwts;

import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class MockAuthServer {

    private static final String TOKEN_ENDPOINT = "https://auth.example.com/token";

    private final DPoPProofVerifier verifier;
    private final javax.crypto.SecretKey serverKey; // 서버가 access token 서명용

    public MockAuthServer(DPoPProofVerifier verifier) {
        this.verifier = verifier;
        // 실무라면 안정적인 키를 보관. 여기선 데모용 HMAC 키 생성.
        this.serverKey = Jwts.SIG.HS256.key().build();
    }

    public String issueToken(String dpopProof) {
        DPoPProofVerifier.VerifiedProof vp = verifier.verify(dpopProof, "POST", TOKEN_ENDPOINT);
        Instant now = Instant.now();
        return Jwts.builder()
                .header()
                .type("at+jwt")
                .and()
                .subject("user-123")
                .issuer("https://localhost")
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(60 * 60)))
                .id(UUID.randomUUID().toString())
                .claim("cnf", Map.of("jkt", vp.jkt()))
                .signWith(serverKey, Jwts.SIG.HS256)
                .compact();
    }

}