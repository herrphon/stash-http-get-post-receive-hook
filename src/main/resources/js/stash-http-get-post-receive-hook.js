var aeffle = {};

aeffle.getTextInput = function (id) {
    'use strict';
    var input, inputName = {
        url: 'input#url',
        useAuth: 'input#useAuth',
        user: 'input#user',
        pass: 'input#pass'
    };

    if (id > 1) {
        inputName.url += id;
        inputName.useAuth += id;
        inputName.user += id;
        inputName.pass += id;
    }

    input = {
        url: AJS.$(inputName.url),
        useAuth: AJS.$(inputName.useAuth),
        user: AJS.$(inputName.user),
        pass: AJS.$(inputName.pass)
    };

    input.setInputEditableStatus = function (isEnabled) {
        var classAttribute = (isEnabled ? 'text disabled' : 'text'),
            disabledAttribute = (isEnabled ? null : 'true');

        this.user.attr('disabled', disabledAttribute).attr('class', classAttribute);
        this.pass.attr('disabled', disabledAttribute).attr('class', classAttribute);
    };
    input.enable = function () {input.setInputEditableStatus(true); };
    input.disable = function () {input.setInputEditableStatus(false); };

    input.update = function () {
        if (input.useAuth.prop('checked')) {
            input.enable();
        } else {
            input.disable();
        }
    };

    return input;
};

aeffle.addChangeEventTo = function (id) {
    'use strict';
    var input = aeffle.getTextInput(id);
    input.useAuth.on('change', input.update);
    input.useAuth.trigger('change');
    input.url.css('max-width', '500px');
};





aeffle.showSavedData = function () {
    'use strict';
    var url = document.URL +
              "/de.aeffle.stash.plugin.stash-http-get-post-receive-hook:http-get-post-receive-hook" +
              "/settings";

    AJS.$.getJSON(url, function (data) {
        var niceString = JSON.stringify(data, null, 4);
        alert(niceString);
    });
};
