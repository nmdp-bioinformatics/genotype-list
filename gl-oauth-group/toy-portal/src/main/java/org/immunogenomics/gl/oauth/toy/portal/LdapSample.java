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
package org.immunogenomics.gl.oauth.toy.portal;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.immunogenomics.gl.oauth.AuthorizationDetails;

public class LdapSample {

    public static void main(String[] args) throws Exception {
        DirContext dirContext = getDirectoryContext();

        SearchControls sc = new SearchControls();
        sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
        List<String> users = searchUsers(dirContext);
        for (String user : users) {
            searchGroups(dirContext, user);
        }

        LdapAuthorizationProvider ap = new LdapAuthorizationProvider(dirContext);
        AuthorizationDetails authorization = ap.getAuthorization("adam", "tdb.realm");
        System.out.println(authorization);
        System.out.println(ap.getAuthorization("sam", "tdb.realm"));
        System.out.println(ap.getAuthorization("jdoe", "tdb.realm"));

        dirContext.close();
    }

    public static DirContext getDirectoryContext() throws NamingException {
        Hashtable<String, String> env = new Hashtable<String, String>();

        String sp = "com.sun.jndi.ldap.LdapCtxFactory";
        env.put(Context.INITIAL_CONTEXT_FACTORY, sp);

        String ldapUrl = "ldap://d0160228:10389/dc=example,dc=com";
        env.put(Context.PROVIDER_URL, ldapUrl);

        DirContext dirContext = new InitialDirContext(env);
        return dirContext;
    }

    private static void searchGroups(DirContext dctx, String dn) throws NamingException {
        BasicAttributes matchingAttrs = new BasicAttributes();
        matchingAttrs.put("uniqueMember", dn);
        matchingAttrs.put("uniqueMember", dn + ",ou=Users,dc=example,dc=com");

        NamingEnumeration<SearchResult> results = dctx.search("ou=Groups", matchingAttrs);
        while (results.hasMore()) {
            SearchResult sr = results.next();
            Attributes attrs = sr.getAttributes();
            System.out.println(dn + " in group " + cn(attrs));
        }
    }

    private static String userid(Attributes attrs) throws NamingException {
        return str(attrs, "userid");
    }

    private static List<String> searchUsers(DirContext dctx) throws NamingException {
        List<String> list = new ArrayList<String>();
        BasicAttributes matchingAttrs = new BasicAttributes();
        // matchingAttrs.put("sn", "Doe");

        NamingEnumeration<SearchResult> results = dctx.search("ou=Users", matchingAttrs);
        while (results.hasMore()) {
            SearchResult sr = results.next();
            Attributes attrs = sr.getAttributes();
            System.out.println(cn(attrs) + ": " + userid(attrs) + " : " + sr.getNameInNamespace());
            list.add(sr.getNameInNamespace());
        }
        return list;
    }

    private static String cn(Attributes attrs) throws NamingException {
        return str(attrs, "cn");
    }

    private static String str(Attributes attributes, String name) throws NamingException {
        Attribute attribute = attributes.get(name);
        if (attribute == null)
            return null;
        Object obj = attribute.get();
        return obj == null ? null : obj.toString();
    }
}
