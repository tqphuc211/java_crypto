package sercure;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;

public class test {

    public static void main(String[] args) throws IOException, GeneralSecurityException {
        String sign = "TB2LddZy//XbjXvzVgssRYI52LHKf3cMPx/0EeOHCFsWpmsddrA1jQZqzYvCzWBXlZk8A8RN8blVQwaDvPukZ/9kRYg6ckZI3I2nIZxLTlM+Iev5iiEj6mmL48bLsh4LtwG27KiqWMwEptyzTGmOXqXabBzQexHVxBUQCUAklAE=";
        String msg = "{\"id\":3,\"public_key\":\"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDQgUuOJRRrYuokLDVVuoJFh1XjlUd17fjzaSpY3TVU8xtKKC9mD5L63o5T1xuWtbhK/VPe9nnZTW6KZrgUt2DRZQDt5qn0/4sFkXQ3G2Aji+EHRvcplnW8LctHCvqf2FqR1L7KDkwsVsVkWTJa9lbjCxWJq7qeIp+1CDJUmp50dwIDAQAB\"}";
//        String msg = "{\"id\":2,\"public_key\":\"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDQgUuOJRRrYuokLDVVuoJFh1XjlUd17fjzaSpY3TVU8xtKKC9mD5L63o5T1xuWtbhK/VPe9nnZTW6KZrgUt2DRZQDt5qn0/4sFkXQ3G2Aji+EHRvcplnW8LctHCvqf2FqR1L7KDkwsVsVkWTJa9lbjCxWJq7qeIp+1CDJUmp50dwIDAQAB\"}";

        byte[] bSign = Base64.getDecoder().decode(sign);

        boolean correct = Signing.rsaVerify((RSAPublicKey) RSA.getPublicKey1(), msg.getBytes(), bSign);
    }
}
