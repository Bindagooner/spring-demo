package neospider.mngr.mvc.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

@Configuration
@Slf4j
public class SslConfiguration {

    @Bean
    public SSLSocketFactory getSslSocketFactory(@Value("${ssl.custom.trust-store}") String file,
                                                @Value("${ssl.custom.trust-store-password}") String password)
            throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, KeyManagementException {
        // load trustStore
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        try (FileInputStream instream = new FileInputStream(ResourceUtils.getFile(file))) {
            trustStore.load(instream, password.toCharArray());
        }

        // X509TrustManager
        TrustManagerFactory trustManagerFactory = TrustManagerFactory
                .getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(trustStore);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        X509TrustManager trustManager = (X509TrustManager) trustManagers[0];

        //init the SSLContext
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[] { trustManager }, null);

        // SSLSocketFactory
        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
        return sslSocketFactory;
    }


}
