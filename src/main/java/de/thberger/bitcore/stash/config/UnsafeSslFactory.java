package de.thberger.bitcore.stash.config;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

class UnsafeSslFactory {
    X509TrustManager getUnsafeTrustManager() {
        return new X509TrustManager() {
            @Override
            public void checkClientTrusted( java.security.cert.X509Certificate[] chain, String authType ) throws CertificateException {
                // trust all certs
            }

            @Override
            public void checkServerTrusted( java.security.cert.X509Certificate[] chain, String authType ) throws CertificateException {
                // trust all servers
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[] {};
            }
        };
    }

    SSLSocketFactory createAllTrustingSslFactory() throws KeyManagementException, NoSuchAlgorithmException {
        X509TrustManager unsafeTrustManager = getUnsafeTrustManager();
        final TrustManager[] trustAllCerts = new TrustManager[] { unsafeTrustManager };
        final SSLContext sslContext = SSLContext.getInstance( "SSL" );
        sslContext.init( null, trustAllCerts, new java.security.SecureRandom() );
        return sslContext.getSocketFactory();
    }

}
