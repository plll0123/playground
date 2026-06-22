package org.playground.dpop.authentication;

import io.jsonwebtoken.security.EcPrivateJwk;
import io.jsonwebtoken.security.EcPublicJwk;
import io.jsonwebtoken.security.Jwks;

import java.security.*;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;

public class DPoPKeyManager {

    private final ECPublicKey publicKey;
    private final ECPrivateKey privateKey;
    private final EcPublicJwk publicJwk;

    public DPoPKeyManager() {
        try {
            KeyPairGenerator gen = KeyPairGenerator.getInstance("EC");
            gen.initialize(new ECGenParameterSpec("secp256r1")); // P-256
            KeyPair kp = gen.generateKeyPair();

            this.publicKey = (ECPublicKey) kp.getPublic();
            this.privateKey = (ECPrivateKey) kp.getPrivate();

            EcPrivateJwk fullJwk = Jwks.builder().ecKeyPair(kp).build();
            this.publicJwk = fullJwk.toPublicJwk(); // 공개 부분만 (d 제거)

        } catch (Exception e) {
            throw new IllegalStateException("키쌍 생성 실패", e);
        }
    }

    public ECPrivateKey getPrivateKey() { return privateKey; }
    public ECPublicKey getPublicKey() { return publicKey; }

    /** proof 헤더에 박을 공개키 JWK */
    public EcPublicJwk getPublicJwk() { return publicJwk; }

    /** 이 키의 thumbprint (cnf.jkt와 대조용) */
    public String getThumbprint() {
        return publicJwk.thumbprint().toString(); // RFC 7638, base64url
    }

}