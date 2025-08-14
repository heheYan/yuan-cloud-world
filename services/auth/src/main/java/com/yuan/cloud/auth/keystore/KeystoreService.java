package com.yuan.cloud.auth.keystore;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;

@Getter
@Service
@RequiredArgsConstructor
public class KeystoreService {
    private final KeystoreProperties keystoreProperties;
    private final ResourceLoader resourceLoader;

    private PrivateKey privateKey;
    private PublicKey publicKey;

    @PostConstruct
    public void init() throws Exception {
        // Load the keystore file
        Resource resource = resourceLoader.getResource(keystoreProperties.getPath());
        KeyStore keyStore;
        try (InputStream inputStream = resource.getInputStream()) {
            keyStore = KeyStore.getInstance("JKS");
            keyStore.load(inputStream, keystoreProperties.getPassword().toCharArray());
        }

        // Retrieve the private key
        privateKey = (PrivateKey) keyStore.getKey(keystoreProperties.getKeyAlias(), keystoreProperties.getKeyPassword().toCharArray());

        // Retrieve the public key
        Certificate certificate = keyStore.getCertificate(keystoreProperties.getKeyAlias());
        publicKey = certificate.getPublicKey();
    }
}
