/*

    gl-service-explorer  URI-based RESTful service API explorer webapp.
    Copyright (c) 2012-13 National Marrow Donor Program (NMDP)

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
var apiData = {
    methodSelected: "",
    contentType: "",
    contentExtension: "",
    getArgs: "",
    postArgs: "",

    toString: function() {
        log(this.methodSelected + ", " + this.contentExtension + ", " + this.contentType + ", " +
            this.getArgs + ", " + this.postArgs);
    }

};

$(document).ready(function() {
    log("All Ready");
    updateApiData();
    $("#HTTPGetButton").click(function() {
        var args = $("#get-args").val();
        if(args == undefined || args.length < 1) {
            alert("Please specify an identifier for HTTP GET request.");
            return;
        }
        updateApiData();
        apiData.toString();
        getData();
    });

    $("#HTTPPostButton").click(function() {
        var args = $("#post-args").val();
        if(args == undefined || args.length < 1) {
            alert("Please provide a glstring for HTTP POST request.");
            return;
        }
        updateApiData();
        apiData.toString();
        postData();
    });

    $("#content-type").change(function () {
        updateApiData();
        apiData.toString();
    });

    $("#method").change(function () {
        updateApiData();
        apiData.toString();
    });

});

function getData() {
    if(apiData.contentExtension == "png") {
        handleQRCode();
        return;
    } 
    var ajaxRequest = $.ajax({
        url: serverURL + apiData.methodSelected + "/" + apiData.getArgs + "." + apiData.contentExtension,
        type: 'GET',
        contentType: apiData.contentType,
        processData: false,
        success: function(response, statusText, jqXHR) {
            var res = jqXHR.responseText;
            if ((apiData.contentExtension == "html") ||
                (apiData.contentExtension == "hml") ||
                (apiData.contentExtension == "rdf") ||
                (apiData.contentExtension == "xml") ||
                (apiData.contentExtension == "xlinkxml") ||
                (apiData.contentExtension == "n3")) {
                res = htmlEncode(res);
            }
            $("#serverResponse").removeClass("errorResponse");
            $("#location").html("");
            $("#serverResponse").html(res);
        },

        error: function(jqXHR, statusText, errorThrown) {
            handleError(jqXHR, statusText, errorThrown);
        }
    });
}

function postData() {
    var ajaxRequest = $.ajax({
        url: serverURL + apiData.methodSelected,
        type: 'POST',
        contentType: apiData.contentType,
        beforeSend: function (request)
        {
            request.setRequestHeader("Authenticate", "Bearer " + bearerToken);
        },
        data: apiData.postArgs,
        cache: false,
        processData: false,
        success: function(responseText, statusText, jqXHR) {
            logResponse(jqXHR);
            var loc = jqXHR.getResponseHeader('Location');
            $("#serverResponse").removeClass("errorResponse");
            $("#serverResponse").html(responseText);
            $(".contentType").html("");
            $("#location").html(loc);
        },

        error: function(jqXHR, statusText, errorThrown) {
            handleError(jqXHR, statusText, errorThrown);
        }
    });
}

function handleError(jqXHR, statusText, errorThrown) {
    logResponse(jqXHR);
    $(".contentType").html("");
    $("#location").html("");
    $("#serverResponse").html(statusText + '(' + errorThrown + "): " + jqXHR.responseText);
    $("#serverResponse").addClass("errorResponse");
}

function htmlEncode(value) {
    return $('<div/>').text(value).html();
}

function logResponse(response) {
    log(response.status + ": " + response.statusText);
    log(response.getAllResponseHeaders());
}

function updateDoc() {
    $("#glServiceGetURL").html(serverURL + apiData.methodSelected + "/");
    $("#glServicePostURL").html(serverURL + apiData.methodSelected + "/");
    $(".serviceName").html(apiData.methodSelected.replace("-", " "));
    if (apiData.methodSelected.lastIndexOf("a", 0) === 0) {
        $(".servicePrefix").html("an");
    }
    else {
        $(".servicePrefix").html("a");
    }
}

function updateContentType() {
    var contentType = $("#content-type :selected").text();
    var start = contentType.indexOf("(");
    var end = contentType.indexOf(")");
    apiData.contentType = contentType.substring(start + 1, end);
    $(".contentType").html(apiData.contentType);
}

function updateApiData() {
    apiData.methodSelected = $("#method :selected").text();
    apiData.contentExtension = $("#content-type :selected").val();
    apiData.getArgs = $("#get-args").val().replace(/^\s+|\s+$/g, '');
    apiData.postArgs = $("#post-args").val().replace(/^\s+|\s+$/g, '')

    updateDoc();
    updateContentType();
}

var googleURL = "http://chart.apis.google.com/chart?cht=qr&chs=128x128&chld=L&choe=UTF-8&chl=";

function handleQRCode() {
    var glURL =  serverURL + apiData.methodSelected + "/" + apiData.getArgs;
    var qrURL = googleURL +  encodeURIComponent(glURL);
    $("#serverResponse").html("");
    $("#serverResponse").append('<img src="' + qrURL + '"/>');
}

function log(data) {
    if (typeof console != 'undefined') {
        if (console && console.log) {
            console.log(data);
        }
    }
}

