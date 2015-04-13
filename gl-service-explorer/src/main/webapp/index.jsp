<!DOCTYPE html>
<!--

    gl-service-explorer  URI-based RESTful service API explorer webapp.
    Copyright (c) 2012-2015 National Marrow Donor Program (NMDP)

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

-->
<%
  String serverURL = org.immunogenomics.gl.explorer.JspSupport.getServerUrl(request);
%>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>Genotype List API explorer</title>

    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link rel="stylesheet" href="style/base.css">
    <link rel="stylesheet" href="style/skeleton.css">
    <link rel="stylesheet" href="style/layout.css">
    <link rel="stylesheet" href="style/gl-service.css">

    <link rel="shortcut icon" href="ico/favicon.ico">
<!--
    <link rel="apple-touch-icon-precomposed" sizes="144x144" href="ico/apple-touch-icon-144-precomposed.png">
    <link rel="apple-touch-icon-precomposed" sizes="114x114" href="ico/apple-touch-icon-114-precomposed.png">
    <link rel="apple-touch-icon-precomposed" sizes="72x72" href="ico/apple-touch-icon-72-precomposed.png">
    <link rel="apple-touch-icon-precomposed" href="ico/apple-touch-icon-57-precomposed.png">
-->

    <script type="text/javascript">
        var serverURL = "<%=serverURL%>";
    </script>
    <script src="js/jquery.min.js" type="text/javascript"></script>
    <script src="js/gl-explorer.js" type="text/javascript"></script>
</head>
<body>
  <div class="container">
    <h1 class="remove-bottom" style="margin-top: 40px">Genotype List API explorer</h1>
    <h5>Namespace <%=serverURL%></h5>
    <hr />

    <form action="/" method="post">

      <div class="sixteen columns">
	<h2>Request</h2>

        <label for="method">Method</label>
        <select name="method" id="method">
          <option value="locus">locus</option>
          <option value="allele">allele</option>
          <option value="allele-list">allele-list</option>
          <option value="haplotype">haplotype</option>
          <option value="genotype">genotype</option>
          <option value="genotye-list">genotype-list</option>
          <option value="multilocus-unphased-genotype">multilocus-unphased-genotype</option>
        </select>
      </div>

      <div class="eight columns">
        <label for="get-args">HTTP GET</label>
        <p>Complete the identifier for the <span class="serviceName">locus</span>.</p>
        <div id="getMethod">
          <span id="glServiceGetURL"><%=serverURL%>locus/</span><input id="get-args" type="text" value="" size="12" style="display:inline;margin-left:5px"/>
        </div>

        <label for="content-type">Content type</label>
        <select name="content-type" id="content-type">
          <option value="gls">GL String (text/plain)</option>
          <option value="html">HTML (text/html)</option>
          <option value="json">JSON (application/json)</option>
          <option value="rdf">RDF (application/rdf+xml)</option>
          <option value="n3">N3 (text/n3)</option>
          <option value="png">QR Code (image/png)</option>
          <option value="xml">XML object graph style (text/xml)</option>
          <option value="xlinkxml">XML refs style (text/xml)</option>
        </select>

        <input id="HTTPGetButton" type="button" value="HTTP GET">
        <p>HTTP GET to return the <span class="serviceName">locus</span> for the specified id in the specified content type.</p>
      </div>

      <div class="eight columns">
        <label for="post-args">HTTP POST</label>
        <p>Enter <span class="servicePrefix">a</span> <span class="serviceName">locus</span> in GL String format.</p>
        <div id="postMethod">
          <span id="glServicePostURL"><%=serverURL%>locus/</span>
        </div>

        <textarea name="post-args" id="post-args" wrap="virtual" style="width: 400px; height: 70px"></textarea>

        <input id="HTTPPostButton" type="button" value="HTTP POST">
        <p>HTTP POST to create a new <span class="serviceName">locus</span>.</p>
      </div>

      <hr />

      <div class="sixteen columns">
        <h2>Response</h2>

        <table id="postHeaders" width="90%">
          <tbody>
            <tr>
              <td><strong>Content-Type</strong></td>
              <td><span class="contentType">text/plain</span></td>
            </tr>
            <tr>
              <td><strong>Location</strong></td>
              <td><span id="location"></span></td>
            </tr>
          </tbody>
        </table>

        <div class=".alert.neutral"></div>

	<p style="margin-top:20px;margin-bottom:0px"><strong>Content</strong><pre><code id="serverResponse"></code></pre></p>
      </div>
      </form>
    </div>
</body>
</html>
