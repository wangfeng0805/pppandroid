package com.ylfcf.ppp.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

/**
 * Created by Administrator on 2017/11/8.
 */

public class HttpsConnection {
    private static final int REQUEST_TIME_OUT = 20 * 1000;
    private static final int READ_TIME_OUT = 20 * 1000;

    public static String getConnection(String paramURL) throws Exception{
        return null;
    }

    public static String postConnection(String url,String param) throws Exception{
        PrintWriter printWriter = null;
        BufferedReader bufferedReader = null;
        HttpsURLConnection httpsConn = null;
                StringBuffer responseResult = new StringBuffer();
        param = encryptPrams(param);
        try {
            //����SSLContext
            SSLContext sslcontext = SSLContextUtil.getSSLContext();

            //������
            URL requestUrl = new URL(url);
            httpsConn = (HttpsURLConnection)requestUrl.openConnection();

            //�����׽ӹ���
            httpsConn.setSSLSocketFactory(sslcontext.getSocketFactory());

            httpsConn.setConnectTimeout(REQUEST_TIME_OUT);
            httpsConn.setReadTimeout(READ_TIME_OUT);

            // ����ͨ�õ���������
            httpsConn.setRequestProperty("accept", "*/*");
            httpsConn.setRequestProperty("connection", "Keep-Alive");
            httpsConn.setRequestProperty("Content-Length", String.valueOf(param.length()));

            httpsConn.setRequestMethod("POST");
            httpsConn.setDoOutput(true);
            httpsConn.setDoInput(true);

            // ��ȡURLConnection�����Ӧ�������
            printWriter = new PrintWriter(httpsConn.getOutputStream());
            // �����������
            printWriter.write(param);
            // flush������Ļ���
            printWriter.flush();

            //��ȡ������
            BufferedReader in = new BufferedReader(new InputStreamReader(httpsConn.getInputStream()));
            int code = httpsConn.getResponseCode();
            if (HttpsURLConnection.HTTP_OK == code){
                // ����BufferedReader����������ȡURL��ResponseData
                bufferedReader = new BufferedReader(new InputStreamReader(httpsConn.getInputStream()));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    responseResult.append(line);
                }
                return responseResult.toString();
            }
            return null;
        } catch (KeyManagementException e) {
            throw new Exception(e);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception(e);
        } catch (MalformedURLException e) {
            throw new Exception(e);
        } catch (ProtocolException e) {
            throw new Exception(e);
        } catch (IOException e) {
            throw new Exception(e);
        }finally{
            httpsConn.disconnect();
            try {
                if (printWriter != null) {
                    printWriter.close();
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * ���������м��ܴ���
     * @param params
     * @return
     */
    private static String encryptPrams(String params) throws Exception{
        if(params == null || (params.contains("version_code") & params.contains("os_type"))){
            return params;
        }
        StringBuffer sb = new StringBuffer();
        String[] parmsStrArr1 = params.split("&");
        for (int i=0;i<parmsStrArr1.length;i++) {
            String[] keyvaluesArr = parmsStrArr1[i].split("=");
            if(keyvaluesArr[0].startsWith("_") && keyvaluesArr[0].endsWith("_") && !"_URL_".equals(keyvaluesArr[0])){
                sb.append(keyvaluesArr[0]).append("=").append(keyvaluesArr[1]);
            }else{
                sb.append(keyvaluesArr[0]).append("=").append(URLEncoder.encode(SimpleCrypto.encryptAES(keyvaluesArr[1],"akD#K2$k=s2kh?DL"),"utf-8")).append("&");
            }
        }
        sb.append("_FROM_=").append(URLEncoder.encode(SimpleCrypto.encryptAES("android","akD#K2$k=s2kh?DL"),"utf-8"));
        return sb.toString();
    }
}
