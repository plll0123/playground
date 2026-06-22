//package org.playground.dpop.authentication;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jws;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.security.Jwk;
//import io.jsonwebtoken.security.Jwks;
//import io.jsonwebtoken.security.PublicJwk;
//
//import java.security.Key;
//import java.util.Map;
//
//public record DPoP(
//        String type,
//        String abs
//) {
//
//    public static final String DPOP_JWT = "dpop+jwt";
//
//    public static void main(String[] args) throws Exception {
//        Jws<Claims> jws = Jwts.parser()
//                .keyLocator(header -> {
//                    if (!DPOP_JWT.equals(header.getType())) {
//                        throw new IllegalArgumentException("not a dpop proof");
//                    }
//                    @SuppressWarnings("unchecked")
//                    var jwkMap = (Map<String, ?>) header.get("jwk");
//                    Jwk<?> jwk = Jwks.builder().add(jwkMap).build();
//                    return ((PublicJwk<?>) jwk).toKey();   // kty 보고 알아서 복원
//                })
//                .build()
//                .parseSignedClaims(DpopGenerator.main(null));
//    }
//}
