package org.playground.dpop.server;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.EcPublicJwk;
import io.jsonwebtoken.security.Jwks;
import org.playground.dpop.authentication.DPoPKeyManager;
import org.playground.dpop.authentication.DPoPProofGenerator;

import java.time.Instant;
import java.util.Map;

public class DPoPProofVerifier {

    private static final long MAX_AGE_SECONDS = 60;

    public VerifiedProof verify(String proof, String expectedMethod, String expectedHtu) {
        EcPublicJwk[] capturedJwk = new EcPublicJwk[1]; // 캡처용

        // 헤더의 jwk를 파싱 중에 꺼내 서명 검증 키로 사용 (self-contained)
        Jws<Claims> jws = Jwts.parser()
                .keyLocator(header -> {
                    Object jwkObj = header.get("jwk");
                    if (jwkObj == null) {
                        throw new SecurityException("jwk 헤더 없음");
                    }
                    //noinspection unchecked
                    //kty을 보고 적절한 인스턴스를 생성함
                    //이 경우 EcPublicJwk 타입의 인스턴스가 생성됨
                    EcPublicJwk jwk = (EcPublicJwk) Jwks.builder().add((Map<String, ?>) jwkObj).build();
                    capturedJwk[0] = jwk;   // 캡처
                    return jwk.toKey();
                })
                .build()
                .parseSignedClaims(proof); // 서명/구조/ES256 검증을 jjwt가 처리

        // --- 여기서부터 DPoP 고유 규칙 ---
        // 1. type 확인
        if (!"dpop+jwt".equals(jws.getHeader().getType())) {
            throw new SecurityException("typ 불일치");
        }

        Claims c = jws.getPayload();

        // 2. htu와 htm의 존재 여부 & 비교
        if (!expectedMethod.equals(c.get("htm", String.class))) {
            throw new SecurityException("htm 불일치: " + c.get("htm", String.class));
        }
        if (!expectedHtu.equals(c.get("htu", String.class))) {
            throw new SecurityException("htu 불일치: " + c.get("htu", String.class));
        }

        // 3. iat 윈도우 : 만료 시간 정책에 따른 검증
        if (c.getIssuedAt() == null) throw new SecurityException("iat 없음");
        long iat = c.getIssuedAt().toInstant().getEpochSecond();
        if (Math.abs(Instant.now().getEpochSecond() - iat) > MAX_AGE_SECONDS) {
            throw new SecurityException("proof 만료(iat 윈도우 초과)");
        }

        String jkt = capturedJwk[0].thumbprint().toString();
        String jti = c.getId();
        if (jti == null) {
            throw new SecurityException("jti 없음");
        }

        return new VerifiedProof(jkt, jti);
    }

    public record VerifiedProof(String jkt, String jti) {
    }

}