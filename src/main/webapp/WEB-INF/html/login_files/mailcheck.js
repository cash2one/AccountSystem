/*
 * This class does some simple correction of email address domains.  Eg., if someone
 * types in "tido@hotnail.com", it will ask if the user meant to enter "tido@hotmail.com".
 * The logic comes from https://github.com/Kicksend/mailcheck, although the api has been
 * slightly changed for our convenience (the "watch" function is Dropbox-specific).
 */

/*
 * Mailcheck https://github.com/Kicksend/mailcheck
 * Author
 * Derrick Ko (@derrickko)
 *
 * License
 * Copyright (c) 2012 Receivd, Inc.
 *
 * Licensed under the MIT License.
 *
 * v 1.0
 */
var MailCheck = Class.create({
    DOMAINS: [
        'yahoo.com',
        'google.com',
        'hotmail.com',
        'gmail.com',
        'me.com',
        'aol.com',
        'mac.com',
        'live.com',
        'comcast.net',
        'googlemail.com',
        'msn.com'
    ],

    // Regexs that look like members of DOMAINS, but are actually domains on their own.
    WHITELIST_REGEXS: [
        'ymail.com',
        'yahoo\\.co\\.',
        'yahoo\\.com\\.',
        'hotmail\\.co\\.',
        'hotmail\\.com\\.'
    ],

    THRESHOLD: 2,

    // Dropbox-specific convenience function to run the check and help with the interaction
    // of suggesting and replacing the email.
    initialize: function(email_text_input_id, result_elem_id) {
        this.emailTextInputId = email_text_input_id;
        this.resultElemId = result_elem_id;
        this.listen();
    },

    listen: function() {
        $(this.emailTextInputId).observe('blur', this.onEmailBlur.bind(this));
    },

    onEmailBlur: function() {
        if (!this.emailTextInputId || !this.resultElemId) {
            return;
        }

        this.suggestion = this.suggest($(this.emailTextInputId).getValue());
        if (this.suggestion) {
            var formatted_domain =
                 '<span class="email_warning_area">' + this.suggestion.domain + '</span>';

            var formatted_suggestion = this.suggestion.address + '@' + formatted_domain;

            var text_suggestion =
                new HTML(
                    // TRANSLATORS for example: Did you mean johnsmith@gmail.com?
                    _('Did you mean <a>%(suggested_email)s</a>?')
                        .format({'suggested_email' : formatted_suggestion})
                );

            $(this.resultElemId).__date(text_suggestion);
            $(this.resultElemId).setStyle({display: 'block'});
            $(this.resultElemId).on('click', 'a', this.onLinkClick.bind(this));
        } else {
            $(this.resultElemId).__date();
            $(this.resultElemId).setStyle({display: 'none'});
        }
    },

    onLinkClick: function() {
        $(this.emailTextInputId).setValue(this.suggestion.full);
        $(this.resultElemId).__date();
        $(this.resultElemId).setStyle({display: 'none'});
    },

    // All of the rest of these functions are more or less copied directly from github,
    // with only very slight tweaks.
    suggest: function(email) {
        email = email.toLowerCase();

        var emailParts = this.splitEmail(email);

        var closestDomain = this.findClosestDomain(emailParts.domain);

        for (var i = 0; i < this.WHITELIST_REGEXS.length; i++) {
            var r = RegExp(this.WHITELIST_REGEXS[i]);
            if (r.match(emailParts.domain)) {
                return false;
            }
        }

        if (closestDomain) {
            return {
                address: emailParts.address,
                domain: closestDomain,
                full: emailParts.address + '@' + closestDomain
            };
        } else {
            return false;
        }
    },

    findClosestDomain: function(domain) {
        var dist;
        var minDist = 99;
        var closestDomain = null;

        for (var i = 0; i < this.DOMAINS.length; i++) {
            dist = this.stringDistance(domain, this.DOMAINS[i]);
            if (dist < minDist) {
                minDist = dist;
                closestDomain = this.DOMAINS[i];
            }
        }

        if (minDist <= this.THRESHOLD && closestDomain !== null && closestDomain !== domain) {
            return closestDomain;
        } else {
            return false;
        }
    },

    stringDistance: function(s1, s2) {
        // sift3: http://siderite.blogspot.com/2007/04/super-fast-and-accurate-string-distance.html
        if (s1 == null || s1.length === 0) {
            if (s2 == null || s2.length === 0) {
                return 0;
            } else {
                return s2.length;
            }
        }

        if (s2 == null || s2.length === 0) {
            return s1.length;
        }

        var c = 0;
        var offset1 = 0;
        var offset2 = 0;
        var lcs = 0;
        var maxOffset = 5;

        while ((c + offset1 < s1.length) && (c + offset2 < s2.length)) {
            if (s1.charAt(c + offset1) == s2.charAt(c + offset2)) {
                lcs++;
            } else {
                offset1 = 0;
                offset2 = 0;
                for (var i = 0; i < maxOffset; i++) {
                    if ((c + i < s1.length) && (s1.charAt(c + i) == s2.charAt(c))) {
                        offset1 = i;
                        break;
                    }
                    if ((c + i < s2.length) && (s1.charAt(c) == s2.charAt(c + i))) {
                        offset2 = i;
                        break;
                    }
                }
            }
            c++;
        }
        return (s1.length + s2.length) /2 - lcs;
    },

    splitEmail: function(email) {
        var parts = email.split('@');

        if (parts.length < 2) {
            return false;
        }

        return {
            domain: parts.pop(),
            address: parts.join('@')
        }
    }
});
