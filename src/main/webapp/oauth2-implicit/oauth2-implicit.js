var SCOPE = "https://www.google.com/m8/feeds";
var CLIENT_ID = "CLIENT_ID";
var REDIRECT_URI = "http://localhost:8080/oauth2-implicit/callback.html";
var AUTHORIZATION_ENDPOINT = "https://accounts.google.com/o/oauth2/auth";

var state;
var accessToken;

function getLoginUrl() {
    state = generateSecureRandomState();

    return AUTHORIZATION_ENDPOINT +
        "?response_type=token" +
        "&client_id=" + encodeURIComponent(CLIENT_ID) +
        "&redirect_uri=" + encodeURIComponent(REDIRECT_URI) +
        "&scope=" + encodeURIComponent(SCOPE) +
        "&state=" + encodeURIComponent(state);
}

function openLoginPage() {
    window.open(getLoginUrl(), "login");
}

function setOAuthParameters(parameters) {
    if (parameters.state != state) {
        alert("Invalid state!");
        return;
    }
    if (!!parameters.error) {
        alert("OAuth 2 error: " + error);
        return;
    }

    accessToken = parameters.access_token;
    loadContacts();
}

function generateSecureRandomState() {
    if (window.crypto && window.crypto.getRandomValues) {
        var array = new Uint32Array(1);
        window.crypto.getRandomValues(array);
        return '' + array[0];
    } else {
        // Fallback for incompatible browsers (IE).
        return Math.floor(Math.random() * 1000000000);
    }
}

function loadContacts() {
    $.ajax({
        type: 'GET',
        url: 'https://www.googleapis.com/m8/feeds/contacts/default/full',
        headers: {
            Authorization: 'Bearer ' + accessToken
        },
        success: function (data) {
            displayContacts(data);
        },
        error: function (data) {
            alert("Error!");
        }
    });
}

function displayContacts(data) {
    var contacts = $("#contacts");
    contacts.empty();

    var entries = $("entry", data);
    for (var i = 0; i < entries.length; i++) {
        var name = $("title", entries[i]).text();
        contacts.append(
            $("<div>").text(name)
        );
    }
}
