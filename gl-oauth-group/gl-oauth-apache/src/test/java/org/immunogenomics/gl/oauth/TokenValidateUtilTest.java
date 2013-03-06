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
package org.immunogenomics.gl.oauth;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.junit.Test;
import org.mockito.Spy;

public class TokenValidateUtilTest {

    private static final String GOOD_TOKEN = "GOOD-TOKEN";

    @Test
    public void testExtractToken() {
        HttpServletRequest request = mockGoodTokenRequest();
        String token = TokenValidateUtil.extractToken(request);
        assertEquals(GOOD_TOKEN, token);
    }

    /** @return a new mocked HttpRequest with GOOD_TOKEN as the token parameter. */
    private HttpServletRequest mockGoodTokenRequest() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter(TokenValidateUtil.TOKEN_PARAM)).thenReturn(GOOD_TOKEN);
        return request;
    }

    @Test
    public void testGenerateResponse() {
        TokenValidator validator = mock(TokenValidator.class);
        AccessTokenDetails expectedValue = new AccessTokenDetails();
        expectedValue.setId("junit-id");;
        when(validator.validate(GOOD_TOKEN)).thenReturn(expectedValue);
        
        String response = TokenValidateUtil.generateResponse(mockGoodTokenRequest(), validator);
        assertTrue(response.contains(expectedValue.getId()));
    }

    @Test
    public void testSendOAuthError() throws IOException {
        HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper(mock(HttpServletResponse.class)) {
            private int statusCode;
            private String msg;
            private String header;
            private String value;

            @Override
            public void sendError(int sc, String msg) throws IOException {
                this.statusCode = sc;
                this.msg = msg;
                super.sendError(sc, msg);
            }

            @Override
            public void setHeader(String name, String value) {
                this.header = name;
                this.value = value;
                super.setHeader(name, value);
            }
            public String toString() {
                String valueInfo = "none";
                if (value.contains("error=\"invalid_token\"")) {
                    valueInfo = "invalid_token";
                } else if (value.contains("error=\"insufficient_scope\"")) {
                    valueInfo = "need_scope";
                } else if (value.contains("error=\"invalid_request\"")) {
                    valueInfo = "bad_request";
                }
                return statusCode + "|" + msg + "|" + header + "|" + valueInfo;
            }
        };
        RequestScope scope = new RequestScope("GET", "junit-realm", "junit-scope");
        
        AuthorizationException exception = new AuthorizationException(OAuthErrorCode.INVALID_TOKEN, "junit token invalid");
        TokenValidateUtil.sendOAuthError(wrapper, scope, exception);
        assertEquals("401|Unauthorized|WWW-Authenticate|invalid_token", wrapper.toString());

        exception = new AuthorizationException(OAuthErrorCode.INSUFFICIENT_SCOPE, "junit bad scope");
        TokenValidateUtil.sendOAuthError(wrapper, scope, exception);
        assertEquals("403|Forbidden|WWW-Authenticate|need_scope", wrapper.toString());

        exception = new AuthorizationException(OAuthErrorCode.INVALID_REQUEST, "junit bad request");
        TokenValidateUtil.sendOAuthError(wrapper, scope, exception);
        assertEquals("400|Bad Request|WWW-Authenticate|bad_request", wrapper.toString());

    }

    @Test
    public void testEncodeValidateTokenUrl() {
        String validateUrl = "http://localhost/oauth/validate";
        String token = GOOD_TOKEN;
        String validateTokenUrl = TokenValidateUtil.encodeValidateTokenUrl(validateUrl, token);
        assertEquals("http://localhost/oauth/validate?token=GOOD-TOKEN", validateTokenUrl);
    }

}
