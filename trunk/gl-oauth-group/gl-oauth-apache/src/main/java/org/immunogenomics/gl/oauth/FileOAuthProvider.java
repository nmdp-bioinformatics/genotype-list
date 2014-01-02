/*

    gl-oauth  OAuth related projects and samples.
    Copyright (c) 2012-2014 National Marrow Donor Program (NMDP)

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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

/** 
 * Simple AuthorizationDetailsDao that persists the details to a single file.
 * It is not intended to be used for sites with a large number of users.
 * @author mgeorge
 */
public class FileOAuthProvider implements AuthorizationDetailsDao {

    private static class UserToDetailsMap extends HashMap<String, AuthorizationDetails>{
        private static final long serialVersionUID = 1L;
    }
    
    private HashMap<String, UserToDetailsMap> realmToUserToDetails =
            new HashMap<String, UserToDetailsMap>();
    
    /** indicates if any admin users have been configured.  If not, the next user will get "admin" scope. */
    private Set<String> realmsWithAdminSet = new TreeSet<String>();
    private File persistentFile;
    private Logger logger = Logger.getLogger(getClass().getName());

    /**
     * Convenience constructor to store the AuthorizationDetails in the user's
     * home directory.  ${user.home}/oauth-details.txt
     */
    public FileOAuthProvider() {
        String userHome = System.getProperty("user.home");
        this.persistentFile = new File(userHome + "/oauth-details.txt");
        loadFile(persistentFile);
    }
    
    /** 
     * Main constructor that specifies the location of the persisted authorization details file.
     * @param persistentFile
     */
    public FileOAuthProvider(File persistentFile) {
        this.persistentFile = persistentFile;
        loadFile(persistentFile);
    }

    /**
     * Loads authorization details from a classpath resource.
     * @param resourceName
     */
    public FileOAuthProvider(String resourceName) {
        InputStream inputStream = getClass().getResourceAsStream(resourceName);
        LineNumberReader lineNumberReader = new LineNumberReader(new InputStreamReader(inputStream));
        readAndClose(lineNumberReader);
    }

    private void loadFile(File persistentFile) {
        try {
            readAndClose(new LineNumberReader(new FileReader(persistentFile)));
        } catch (FileNotFoundException e) {
            logger.info("FileNotFound for AuthorizationDetails: " + persistentFile);
        }
    }
    

    public synchronized AuthorizationDetails getAuthorization(String userid, String realm) {
        AuthorizationDetails details = forRealm(realm).get(userid);
        if (DemoAuthMXBean.DEMO_USER_ID.equals(userid)) {
            // special handling for demo user.
            if (details == null) {
                // First access by demo userid.
                details = new AuthorizationDetails();
                details.setRealm(realm);
                details.setId(userid);
                addOrUpdate(details);
                return details;
            }
        } else {
            // normal users
            if (!realmsWithAdminSet.contains(realm)) {
                // no user has admin.  So first users gets admin privileges.
                if (details == null) {
                    details = new AuthorizationDetails();
                    details.setRealm(realm);
                    details.setId(userid);
                }
                details.addScope(AuthorizationDetails.ADMIN_SCOPE);
                addOrUpdate(details);
            }
        }
        return details;
    }


    private UserToDetailsMap forRealm(String realm) {
        UserToDetailsMap userToDetailsMap = realmToUserToDetails.get(realm);
        if (userToDetailsMap == null) {
            userToDetailsMap = new UserToDetailsMap();
            realmToUserToDetails.put(realm, userToDetailsMap);
        }
        return userToDetailsMap;
    }

    private void readAndClose(LineNumberReader lineNumberReader) {
        String line;
        try {
            while (null != (line = lineNumberReader.readLine())) {
                line = line.trim();
                if (line.length() > 0) {
                    try {
                        add(new AuthorizationDetails(line));
                    } catch (IllegalArgumentException e) {
                        logger.warning("Unable to parse line: " + line + "\n due to " + e);
                    }
                }
            }
            lineNumberReader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void add(AuthorizationDetails details) {
        String userid = details.getId();
        String realm = details.getRealm();
        add(userid, realm, details);
    }

    private void add(String userid, String realm, AuthorizationDetails details) {
        checkArg("userid", userid);
        checkArg("realm", realm);
        
        if (details.hasScope(AuthorizationDetails.ADMIN_SCOPE)) {
            realmsWithAdminSet.add(realm);
        }
        forRealm(realm).put(userid, details);
    }

    private void checkArg(String name, String arg) {
        if (arg == null || arg.isEmpty()) {
            throw new IllegalArgumentException(name + " is missing");
        }
        if (arg.contains("\t")) {
            throw new IllegalArgumentException(name + " is invalid: " + arg);
        }
    }
    
    private Iterator<AuthorizationDetails> iterator(final String realm) {
        return new Iterator<AuthorizationDetails>() {
            Iterator<Entry<String, UserToDetailsMap>> realmIterator = realmToUserToDetails.entrySet().iterator();
            Iterator<AuthorizationDetails> detailIterator = null;
            @Override
            public boolean hasNext() {
                if (detailIterator == null || !detailIterator.hasNext()) {
                    // check next realm
                    if (realmIterator.hasNext()) {
                        Entry<String, UserToDetailsMap> entry = realmIterator.next();
                        String iterRealm = entry.getKey();
                        if (realm == null || realm.equals(iterRealm)) {
                            detailIterator = entry.getValue().values().iterator();
                        }
                        return hasNext(); // recursive
                    } else {
                        return false; // no more realms to check
                    }
                }
                return detailIterator.hasNext();
            }

            @Override
            public AuthorizationDetails next() {
                return detailIterator.next();
            }
            
            @Override
            public void remove() {
                throw new UnsupportedOperationException("Remove not supported");
            }};
    }

    @Override
    public synchronized String toString() {
        StringBuilder sb = new StringBuilder();
        for (AuthorizationDetails authorizationDetails : iterable(null)) {
            sb.append(authorizationDetails).append("\n");
        }
        return sb.toString();
    }

    @Override
    public synchronized void addOrUpdate(AuthorizationDetails details) {
        add(details);
        writeDetails();
    }

    private void writeDetails() {
        if (persistentFile == null) {
            return;  // No Persistable file configured.
        }
        Writer writer = null;
        try {
            writer = new FileWriter(persistentFile);
            writer = new BufferedWriter(writer);
            for (AuthorizationDetails authorizationDetails : iterable(null)) {
                writer.append(authorizationDetails.toString());
                writer.append("\n");
            }
        } catch (IOException e) {
            logger.warning("Unableto write: " + persistentFile + " due to " + e);
        } finally {
            if (writer != null)
                try {
                    writer.close();
                } catch (IOException e) {
                    logger.warning("Unableto close: " + persistentFile + " due to " + e);
                }
        }
    }

    @Override
    public synchronized int count(String realm) {
        if (realm == null) {
            int total = 0;
            for (Entry<String, UserToDetailsMap> entry : realmToUserToDetails.entrySet()) {
                total += entry.getValue().size();
            }
            return total;
        } else {
            return forRealm(realm).size();
        }
    }

    @Override
    public synchronized List<AuthorizationDetails> subrange(String realm, int start, int end) {
        List<AuthorizationDetails> list = new ArrayList<AuthorizationDetails>(end - start);
        int pos = 0;
        for (AuthorizationDetails details : iterable(realm)) {
            if (pos >= start) {
                if (pos < end) {
                    list.add(details);
                } else {
                    break;
                }
            }
            ++pos;
        }
        return list;
    }

    private Iterable<AuthorizationDetails> iterable(final String realm) {
        return new Iterable<AuthorizationDetails>() {
            @Override
            public Iterator<AuthorizationDetails> iterator() {
                return FileOAuthProvider.this.iterator(realm);
            }
            
        };
    }

}
