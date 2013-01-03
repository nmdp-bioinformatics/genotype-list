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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class FileOAuthProvider implements OAuthProvider {

    private ConcurrentHashMap<String, AuthorizationDetails> userAndRealmToDetails =
            new ConcurrentHashMap<String, AuthorizationDetails>();

    public FileOAuthProvider(String resourceName) {
        InputStream inputStream = getClass().getResourceAsStream(resourceName);
        LineNumberReader lineNumberReader = new LineNumberReader(new InputStreamReader(inputStream));
        readAndClose(lineNumberReader);
    }

    public AuthorizationDetails getAuthorization(String userid, String realm) {
        AuthorizationDetails details = userAndRealmToDetails.get(toKey(userid, realm));
        return details;
    }

    private void readAndClose(LineNumberReader lineNumberReader) {
        String line;
        try {
            while (null != (line = lineNumberReader.readLine())) {
                line = line.trim();
                if (line.length() > 0) {
                    add(new AuthorizationDetails(line));
                }
            }
            lineNumberReader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void add(AuthorizationDetails details) {
        String userid = details.getId();
        String realm = details.getRealm();
        add(userid, realm, details);
    }

    private void add(String userid, String realm, AuthorizationDetails details) {
        checkArg("userid", userid);
        checkArg("realm", realm);
        String key = toKey(userid, realm);
        userAndRealmToDetails.put(key, details);
    }

    private String toKey(String userid, String realm) {
        return userid + "\t" + realm;
    }

    private void checkArg(String name, String arg) {
        if (arg == null || arg.contains("\t")) {
            throw new IllegalArgumentException(name + " is invalid: " + arg);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Collection<AuthorizationDetails> values = userAndRealmToDetails.values();
        for (AuthorizationDetails authorizationDetails : values) {
            sb.append(authorizationDetails).append("\n");
        }
        return sb.toString();
    }
}
