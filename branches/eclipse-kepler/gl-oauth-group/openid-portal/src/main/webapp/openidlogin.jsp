<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"   "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Immunogenomics OpenID Login</title>

    <!-- Simple OpenID Selector -->
    <link rel="stylesheet" href="<c:url value='/css/openid.css'/>" />
    <script type="text/javascript" src="<c:url value='/js/jquery-1.2.6.min.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/js/openid-jquery.js'/>"></script>

    <script type="text/javascript">
    $(document).ready(function() {
        openid.init('openid_identifier');
     //   openid.setDemoMode(true); Stops form submission for client javascript-only test purposes
    });
    </script>
    <!-- /Simple OpenID Selector -->

    <style type="text/css">
        /* Basic page formatting. */
        body {
            font-family:"Helvetica Neue", Helvetica, Arial, sans-serif;
        }
        
#header {
    background: #648C1D;
    text-align: center;
    padding: 5px;
    margin-bottom:5px;
}

#header a {
    color: #fff;
    font-size: 20px;
    font-family: Helvetica, sans-serif;
}

#openid_form fieldset {
    background: #e0e7d3;
    padding: 8px;
    padding-bottom: 22px;
    border: 2;
    width: 560px;
}


#policy {
    font-size: smaller;
}
        
    </style>
</head>

<body>
<div id="header">
    <a href="/">Immunogenomics Portal</a>
</div>
<c:if test="${not empty param.login_error}">
  <font color="red">
    Your login attempt was not successful, try again.<br/><br/>
    Reason: <c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}"/>.
  </font>
</c:if>

<!-- Simple OpenID Selector -->
<form action="<c:url value='j_spring_openid_security_check'/>" method="post" id="openid_form">
    <input type="hidden" name="action" value="verify" />

    <fieldset id="openid_fieldset">
            <legend>Sign-in with your Yahoo or Google Account</legend>

            <div id="openid_choice">
                <p>Please click your account provider:</p>
                <div id="openid_btns"></div>

            </div>

            <div id="openid_input_area">
                <input id="openid_identifier" name="openid_identifier" type="text" value="http://" />
                <input id="openid_submit" type="submit" value="Sign-In"/>
            </div>
            <noscript>
            <p>OpenID is a service that allows you to log-on to many different websites using a single identity.
            Find out <a href="http://openid.net/what/">more about OpenID</a> and <a href="http://openid.net/get/">how to get an OpenID enabled account</a>.</p>
            </noscript>
            <div id="policy">Your email address will be used for authorization and to notify you of significant changes to the services.</div>
    </fieldset>
</form>
<!-- /Simple OpenID Selector -->

</body>
</html>
