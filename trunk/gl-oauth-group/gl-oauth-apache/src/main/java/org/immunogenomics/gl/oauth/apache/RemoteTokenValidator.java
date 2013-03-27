/*

    gl-oauth  OAuth related projects and samples.
    Copyright (c) 2012-2013 National Marrow Donor Program (NMDP)

    This library is free software; you can redistribute it and/or modify it
    under the terms of the GNU Lesser General Public License as published
    by the Free Software Foundation; either version 3 of the License, or (at
    your option) any later version.

    This library is distributed in the hope that it will be useful, but WITHOUT
    ANY WARRANTY; with out even the implied warranty of MERCHANTABILITY or
    FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
    License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this library;  if not, write to the Free Software Foundation,
    Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307  USA.

    > http://www.fsf.org/licensing/licenses/lgpl.html
    > http://www.opensource.org/licenses/lgpl-license.php

 */
package org.immunogenomics.gl.oauth.apache;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.immunogenomics.gl.oauth.AccessTokenDetails;
import org.immunogenomics.gl.oauth.TokenValidateUtil;
import org.immunogenomics.gl.oauth.TokenValidator;

/**
 * Validates a token against a remote authorization server.
 * 
 */
public class RemoteTokenValidator implements TokenValidator {

    private final String validateUrl;
    private final DefaultHttpClient httpClient = new DefaultHttpClient();

    public RemoteTokenValidator(String validateUrl) {
        this.validateUrl = validateUrl;
    }

    public AccessTokenDetails validate(String token) {
        String url = TokenValidateUtil.encodeValidateTokenUrl(validateUrl, token);
        HttpPost httpPost = new HttpPost(url);
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        try {
            String content = httpClient.execute(httpPost, responseHandler);
            return new AccessTokenDetails(content);
        } catch (ClientProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            httpPost.releaseConnection();
        }
    }

    public void close() {
        httpClient.getConnectionManager().shutdown();
    }

    /** Testing main method. */
    public static void main(String[] args) {
        if (args.length == 0) {
            args =
                    new String[] { "http://localhost:9090/toy-portal/oauth/validate", "Xinvalid", "Xexpired", "Xvalid",
                            "Xadmin", "Xhigh" };
        }
        String url = args[0];
        RemoteTokenValidator validator = new RemoteTokenValidator(url);
        for (int i = 1; i < args.length; ++i) {
            String token = args[i];
            try {
                AccessTokenDetails details = validator.validate(token);
                System.out.println(token + "\t" + details);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
