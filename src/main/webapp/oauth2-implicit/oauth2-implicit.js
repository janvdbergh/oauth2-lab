function getLoginUrl() {
    return "TBD";
}

function openLoginPage() {
    window.open(getLoginUrl(), "login");
}

function setOAuthParameters(parameters) {
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
            Authorization: 'Bearer TBD'
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
