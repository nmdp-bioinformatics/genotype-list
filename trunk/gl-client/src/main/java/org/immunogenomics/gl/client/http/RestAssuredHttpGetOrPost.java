package org.immunogenomics.gl.client.http;

import java.io.InputStream;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Header;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;

public class RestAssuredHttpGetOrPost implements HttpGetOrPost {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    /** Convenience for Request.with() including authentication/authorization information. */
    private RequestSpecification with(String bearerToken) {
        if (bearerToken== null) {
            return RestAssured.with();
        } else {
            Header authHeader = new Header("Authenticate", "Bearer " + bearerToken);
            return RestAssured.with().header(authHeader);
        }
    }
    
    @Override
    public InputStream get(String url, String bearerToken) throws GlClientHttpException {
        long start = System.nanoTime();
        Response response = with(bearerToken).get(url);
        long elapsed = System.nanoTime() - start;
        int statusCode = response.statusCode();
        if (logger.isTraceEnabled()) {
            logger.trace("HTTP GET {} status code {} took {} ns", new Object[] { url, statusCode, elapsed });
        }
        if (statusCode != HttpStatus.SC_OK) {
            
        }
        return response.body().asInputStream();
    }

    @Override
    public String post(String url, String body, String bearerToken) throws GlClientHttpException {
        long start = System.nanoTime();
        Response response = with(bearerToken).body(body).contentType("text/plain").post(url);
        long elapsed = System.nanoTime() - start;
        int statusCode = response.statusCode();
        if (logger.isTraceEnabled()) {
            logger.trace("HTTP POST {} status code {} took {} ns", new Object[] { url, statusCode, elapsed });
        }
        if (statusCode == HttpStatus.SC_CREATED || statusCode == HttpStatus.SC_OK) {
            // success
        } else {
            String message = response.getStatusLine();
            String authHeader = response.getHeader("WWW-Authenticate");
            if (authHeader != null) {
                message += "; " + authHeader;
            }
            throw new GlClientHttpException(statusCode, message);
        }
        return response.header("Location");
    }

}
