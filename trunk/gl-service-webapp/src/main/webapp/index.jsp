<!DOCTYPE html>
<!--

    gl-service-webapp  URI-based RESTful service webapp for the gl project.
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

-->
<%@page import="java.util.Properties" %>
<%
  Properties properties = new Properties();
  ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
  properties.load(classLoader.getResourceAsStream("/gl-service.properties"));
  String serverURL = properties.getProperty("org.immunogenomics.gl.service.spark.namespace");
%>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>Genotype List service</title>
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
</head>
<body>
  <div class="container">
    <h1 class="remove-bottom" style="margin-top: 40px">Genotype List service</h1>
    <h5>Version 1.0-SNAPSHOT, Namespace <%=serverURL%></h5>
    <hr />

    <div class="two-thirds column">
      <p>Load IMGT version 3.9.0 alleles:</p>
      <form action="load-imgt-alleles" method="post">
        <input id="submit" type="submit" value="Load IMGT alleles">
      </form>
    </div>

    <div class="two-thirds column">
      <p>Load KIR version 2.4.0 alleles:</p>
      <form action="load-kir-alleles" method="post">
        <input id="submit" type="submit" value="Load KIR alleles">
      </form>
    </div>
  </div>
</body>
</html>
