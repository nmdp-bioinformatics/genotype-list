<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="xml" omit-xml-declaration="yes" indent="yes"
  doctype-public="-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN" 
  doctype-system="http://java.sun.com/dtd/web-app_2_3.dtd" />

 <xsl:preserve-space elements="filter"/>

	<xsl:template match="filter[1]">
  <xsl:comment>filter inserted by update-web-xml.xsl from glsw-oauth project</xsl:comment>
    <filter>
      <filter-name>BearerTokenFilter</filter-name>
      <filter-class>org.immunogenomics.glsw.oauth.GlBearerTokenFilter</filter-class>
      <init-param>
        <param-name>realm</param-name>
        <param-value>immunogenomics</param-value>
      </init-param>
      <init-param>
        <param-name>validateUrl</param-name>
        <param-value>JMX</param-value>
        <description>URL used to validate the token. "JMX" to use JMXBean</description>
      </init-param>
    </filter>

    <filter>
        <filter-name>DenialOfServiceFilter</filter-name>
        <filter-class>org.immunogenomics.gl.web.dosfilter.DenialOfServiceFilter</filter-class>
        <init-param>
            <param-name>cleanInterval</param-name>
            <param-value>20</param-value>
            <description>Minimum minutes before a clean occurs</description>
        </init-param>
        <init-param>
            <param-name>freeHitCount</param-name>
            <param-value>100</param-value>
            <description>the number of hits that are not restricted</description>
        </init-param>
        <init-param>
            <param-name>anonymousHitsPerMinute</param-name>
            <param-value>30</param-value>
            <description>maximum number of hits per minute for anonymous access</description>
        </init-param>
        <init-param>
            <param-name>throttleDelay</param-name>
            <param-value>51</param-value>
            <description>the delay in milliseconds to wait before providing a response</description>
        </init-param>
        <init-param>
            <param-name>authorizationAttribName</param-name>
            <param-value>authorizationScopes</param-value>
            <description>the name of a request attribute that is required for authorized access</description>
        </init-param>
    </filter>
    <xsl:copy-of select="."></xsl:copy-of>
    </xsl:template>
    
    
    <xsl:template match="filter-mapping[1]">
    <xsl:comment>filter-mapping inserted by update-web-xml.xsl</xsl:comment>
    
    <filter-mapping>
      <filter-name>BearerTokenFilter</filter-name>
      <url-pattern>/*</url-pattern>
    </filter-mapping>
    <filter-mapping>
        <filter-name>DenialOfServiceFilter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>
      <xsl:copy-of select="."></xsl:copy-of>
    </xsl:template>
	<!-- standard copy template -->
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*"/>
			<xsl:apply-templates/>
		</xsl:copy>
	</xsl:template>	
</xsl:stylesheet>