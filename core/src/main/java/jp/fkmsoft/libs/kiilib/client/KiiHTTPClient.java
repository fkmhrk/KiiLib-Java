package jp.fkmsoft.libs.kiilib.client;

import org.json.JSONObject;

import java.io.InputStream;
import java.util.Map;

/**
 * HTTP client
 */
public interface KiiHTTPClient {
    interface Method {
        int GET = 1;
        int POST = 2;
        int PUT = 3;
        int DELETE = 4;
    }
    
    interface ResponseHandler {
        void onResponse(int status, JSONObject response, String etag);
        
        void onException(Exception e);
    }
    
    /**
     * Sends JSONObject request
     * @param method HTTP method
     * @param url target URL
     * @param token access token
     * @param contentType contentType. If method is GET/DELETE, must be null
     * @param headers HTTP header
     * @param body request body. If method is GET/DELETE, must be null
     * @param handler response handler
     */
    void sendJsonRequest(int method, String url, String token, String contentType, Map<String, String> headers, JSONObject body, ResponseHandler handler);
    
    /**
     * Sends text/plain request
     * @param method HTTP method
     * @param url target URL
     * @param token access token
     * @param headers HTTP header
     * @param body request body. If method is GET/DELETE, must be null
     * @param handler response handler
     */
    void sendPlainTextRequest(int method, String url, String token, Map<String, String> headers, String body, ResponseHandler handler);
    
    /**
     * Sends stream request
     * @param method HTTP method
     * @param url target URL
     * @param token access token
     * @param contentType contentType. If method is GET/DELETE, must be null
     * @param headers HTTP header
     * @param body request body. If method is GET/DELETE, must be null
     * @param handler response handler
     */
    void sendStreamRequest(int method, String url, String token, String contentType, Map<String, String> headers, InputStream body, ResponseHandler handler);
}
