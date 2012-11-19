/*

    gl-oauth  OAuth related projects and samples.
    Copyright (c) 2012 National Marrow Donor Program (NMDP)

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

import org.junit.Before;
import org.junit.Test;

public class MemoryTokenStoreTest {

	private MemoryTokenStore tokenStore;
	private AccessTokenDetails authorization;

	@Before
	public void setupTest() {
		tokenStore = new MemoryTokenStore();
		authorization = new AccessTokenDetails();
		authorization.setRealm("junit");
		authorization.setScopes(new String[]{"scope1", "scope2"});
	}

	@Test
	public void testGet() {
		AccessTokenDetails details = tokenStore.get(null);
		assertNull(details);
	}

	@Test
	public void testAdd() {
		String token = tokenStore.add(authorization);
		AccessTokenDetails authorizationDetails = tokenStore.get(token);
		assertEquals(authorization.getRealm(), authorizationDetails.getRealm());
	}

	@Test
	public void testPut() {
		tokenStore.put("Test", authorization);
		AccessTokenDetails authorizationDetails = tokenStore.get("Test");
		assertEquals(authorization.getRealm(), authorizationDetails.getRealm());
	}

	@Test
	public void testDispose() {
		tokenStore.dispose();
	}

}
