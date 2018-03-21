package com.ylfcf.ppp.util;

import com.ylfcf.ppp.ui.YLFApplication;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

/**
 * Ԥ��֤�鵽�ͻ��ˣ��п�����֤����µ�ʱ������ϰ汾���ʲ��˷�������
 * Created by Administrator on 2017/11/15.
 */

public class SSLContextUtil {
    public static SSLContext getSSLContext() throws Exception {
        // ����SSLContext����
        SSLContext sslContext = SSLContext.getInstance("TLS");
        // ��assets�м���֤��
        InputStream inStream = YLFApplication.getApplication().getAssets().open("ylfcfapp.crt");

        // ֤�鹤��
        CertificateFactory cerFactory = CertificateFactory.getInstance("X.509");
        Certificate cer = cerFactory.generateCertificate(inStream);

        // ��Կ��
        KeyStore kStore = KeyStore.getInstance("PKCS12");
        kStore.load(null, null);
        kStore.setCertificateEntry("trust", cer);// ����֤�鵽��Կ����

        // ��Կ������
        KeyManagerFactory keyFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyFactory.init(kStore, null);// ������Կ�⵽������

        // ���ι�����
        TrustManagerFactory tFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tFactory.init(kStore);// ������Կ�⵽���ι�����

        // ��ʼ��
        sslContext.init(keyFactory.getKeyManagers(), tFactory.getTrustManagers(), new SecureRandom());
        return sslContext;
    }
}
