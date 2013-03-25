package org.immunogenomics.gl.client.http;

/**
 * RuntimeException used to report HTTP failures.
 * @author mgeorge
 *
 */
public class GlClientHttpException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    
    private int statusCode;
    
    public GlClientHttpException(int statusCode) {
        this.statusCode = statusCode;
    }

    public GlClientHttpException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public GlClientHttpException(int statusCode, Throwable cause) {
        super(cause);
        this.statusCode = statusCode;
    }

    public GlClientHttpException(int statusCode, String message, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }
    
    @Override
    public String toString() {
        return super.toString();
    }

    /** HTTP Status Code */
    public int getStatusCode() {
        return statusCode;
    }

}
