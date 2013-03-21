package org.immunogenomics.gl.client;

import java.io.InputStream;

/** Interface to decouple GlClient from HTTP communication.
 * Mainly, to simply JUnit testing of error handling.
 */
public interface HttpGetOrPost {

    /** Returns the content body as an InputStream.
     * @param url location to get
     * @param bearerToken optional OAuth 2.0 bearer token
     * @return InputStream for content body which should be closed.
     * @throws GlClientHttpException when status code is not OK.
     */
    InputStream get(String url, String bearerToken) throws GlClientHttpException;

    /** Returns the "Location" header as a string.
     * @param url location to get
     * @param body "text/plain" content sent to the server.
     * @param bearerToken optional OAuth 2.0 bearer token
     * @return the "Location" header.
     * @throws GlClientHttpException when status code is not OK.
     */
    String post(String url, String body, String bearerToken) throws GlClientHttpException;
}
