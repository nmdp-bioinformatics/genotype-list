package org.immunogenomics.gl.oauth;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;

public class BearerTokenFilterHelperTest {

    private static final String WRITE_SCOPE = "write";
    private static final String JUNIT_REALM = "junit";

    @Test
    public void testIssue153NoAuthInfo() throws IOException, ServletException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        TokenValidator tokenValidator = mock(TokenValidator.class);
        ScopeEvaluator scopeEvaluator = mock(ScopeEvaluator.class);
        RequestScope scope = new RequestScope("POST", JUNIT_REALM, WRITE_SCOPE);
        when(scopeEvaluator.analyzeRequest(request)).thenReturn(scope);
        Authorizer authorizer = new DefaultAuthorizer();
        BearerTokenFilterHelper helper = new BearerTokenFilterHelper(tokenValidator, scopeEvaluator, authorizer);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);
        helper.doFilter(request, response, filterChain);
        verify(response).sendError(401, "Unauthorized");
        String authenticateValue = "Bearer realm=\""
                + JUNIT_REALM + "\", scope=\""
                + WRITE_SCOPE + "\"";
        verify(response).setHeader("WWW-Authenticate", authenticateValue);
    }


    @Test
    public void testInvalidRequest() throws IOException, ServletException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("not-bearer token");
        TokenValidator tokenValidator = mock(TokenValidator.class);
        ScopeEvaluator scopeEvaluator = mock(ScopeEvaluator.class);
        RequestScope scope = new RequestScope("POST", JUNIT_REALM, WRITE_SCOPE);
        when(scopeEvaluator.analyzeRequest(request)).thenReturn(scope);
        Authorizer authorizer = new DefaultAuthorizer();
        BearerTokenFilterHelper helper = new BearerTokenFilterHelper(tokenValidator, scopeEvaluator, authorizer);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);
        helper.doFilter(request, response, filterChain);
        verify(response).sendError(400, "Bad Request");
        String authenticateValue = "Bearer realm=\""
                + JUNIT_REALM + "\", scope=\""
                + WRITE_SCOPE + "\", error=\"invalid_request\", error_description=\""
                + BearerTokenFilterHelper.BEARER_TOKEN_REQUIRED + "\"";
        verify(response).setHeader("WWW-Authenticate", authenticateValue);
    }

    @Test
    public void testInvalidToken() throws IOException, ServletException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer invalid-token");
        TokenValidator tokenValidator = mock(TokenValidator.class);
        when(tokenValidator.validate("invalid-token")).thenReturn(null);
        ScopeEvaluator scopeEvaluator = mock(ScopeEvaluator.class);
        RequestScope scope = new RequestScope("POST", JUNIT_REALM, WRITE_SCOPE);
        when(scopeEvaluator.analyzeRequest(request)).thenReturn(scope);
        Authorizer authorizer = new DefaultAuthorizer();
        BearerTokenFilterHelper helper = new BearerTokenFilterHelper(tokenValidator, scopeEvaluator, authorizer);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);
        helper.doFilter(request, response, filterChain);
        verify(response).sendError(401, "Unauthorized");
        String authenticateValue = "Bearer realm=\""
                + JUNIT_REALM + "\", scope=\""
                + WRITE_SCOPE + "\", error=\"invalid_token\", error_description=\""
                + BearerTokenFilterHelper.TOKEN_MISSING_OR_INVALID + "\"";
        verify(response).setHeader("WWW-Authenticate", authenticateValue);
    }

    @Test
    public void testInsufficientScope() throws IOException, ServletException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer simple-token");
        TokenValidator tokenValidator = mock(TokenValidator.class);
        AccessTokenDetails details = new AccessTokenDetails();
        details.setTokenId("1234567");
        details.setRealm(JUNIT_REALM);
        details.setExpiresIn(60);
        String[] scopes = {"read"};
        details.setScopes(scopes);
        when(tokenValidator.validate("simple-token")).thenReturn(details);
        ScopeEvaluator scopeEvaluator = mock(ScopeEvaluator.class);
        RequestScope scope = new RequestScope("POST", JUNIT_REALM, WRITE_SCOPE);
        when(scopeEvaluator.analyzeRequest(request)).thenReturn(scope);
        Authorizer authorizer = new DefaultAuthorizer();
        BearerTokenFilterHelper helper = new BearerTokenFilterHelper(tokenValidator, scopeEvaluator, authorizer);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);
        helper.doFilter(request, response, filterChain);
        verify(response).sendError(403, "Forbidden");
        String authenticateValue = "Bearer realm=\""
                + JUNIT_REALM + "\", scope=\""
                + WRITE_SCOPE + "\", error=\"insufficient_scope\", error_description=\"requires additional scope "
                + WRITE_SCOPE + "\"";
        verify(response).setHeader("WWW-Authenticate", authenticateValue);
    }

}
