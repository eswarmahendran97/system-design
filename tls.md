# SSL/TLS, Keystore & Truststore Guide

## Keystore vs Truststore

**Keystore** - present in server (send signature)

**Truststore** - present in client (checks signature whether its from trusted CA)

---

## Keystore

### Key Pair Generation
Server generates a private key and a public key combination.

### Certificate Creation
Server creates CSR using its public key (creates .csr file)

CA signs the CSR → generates .crt file (Server public key, CA signature)

### Create JKS
create .jks = Java KeyStore file (Store private key + certificate)

---

## Truststore

You can create your own truststore using keytool but java already have its own -> cacerts

Add the CA certificate (not server certificate) inside it.

---

## TLS Handshake Process

### Client Request
Client makes request to server

### Handshake Process (Asymmetric Encryption)

**Server → Client:** Server returns with certificate (server public key, CA signature) from keystore.

**Client Verification:** Client uses the CA's certificate (from its truststore) to verify this signature.

**Session Key Exchange:** After verification, client sends a session key (random unique key, encrypted by server public key)

**Server Decryption:** Server gets session key by decrypting using private key

### Encrypted Communication
Encrypted communication continues with session key (Symmetric encryption)

---

## Security Warning

If client does not have CA's certificate in truststore, we will see:

```
Connection is not secure... do you want to proceed?
```

---

## mTLS - Mutual TLS

In mTLS, service needs to have both keystore and truststore.

```
Client: keystore + truststore (prove identity + verify server)
Server: keystore + truststore (prove identity + verify client)
```