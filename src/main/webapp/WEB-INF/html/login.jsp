<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html xml:lang="en"
	class=" js no-flexbox canvas canvastext no-touch postmessage no-websqldatabase indexeddb hashchange history draganddrop websockets rgba hsla multiplebgs backgroundsize borderimage borderradius boxshadow textshadow opacity cssanimations csscolumns cssgradients no-cssreflections csstransforms csstransforms3d csstransitions fontface generatedcontent video audio localstorage sessionstorage webworkers applicationcache"
	xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>
<script async="" type="text/javascript" src="/WEB-INF/html/login_files/zxcvbn.js"></script>
<script src="/WEB-INF/html/login_files/ga.js" async="" type="text/javascript"></script>
<script type="text/javascript">
	window.ST = +new Date();
</script>
<meta content="text/html; charset=UTF-8" http-equiv="content-type">
<meta
	content="Dropbox is a free service that lets you bring your photos, docs, and videos anywhere and share them easily. Never email yourself a file again!"
	name="description">
<meta
	content="online storage, free storage, file sharing, share files,
               awesome, cloud storage, online backup, cross platform, sync, sharing, mac,
               windows, os x, linux, backup, collaboration, file versioning, file revisions,
               remote access, undelete"
	name="keywords">
<meta content="Dropbox - Sign in - Simplify your life"
	property="og:title">
<meta
	content="Dropbox is a free service that lets you bring your photos, docs, and videos anywhere and share them easily. Never email yourself a file again!"
	property="og:description">
<meta content="/static/31443/images/dropbox_icon_64px.png"
	property="og:image">
<meta
	content="https://www.dropbox.com/login?cont=https%3A//www.dropbox.com/1/oauth/authorize%3Foauth_token%3D77l2denqckvjuti&amp;signup_tag=oauth&amp;signup_data=86275"
	property="og:url">
<meta content="website" property="og:type">
<meta content="Dropbox" property="og:site_name">
<meta content="210019893730" property="fb:app_id">
<meta content="@Dropbox" name="twitter:site">
<meta
	content="https://www.dropbox.com/login?cont=https%3A//www.dropbox.com/1/oauth/authorize%3Foauth_token%3D77l2denqckvjuti&amp;signup_tag=oauth&amp;signup_data=86275"
	name="twitter:url">
<meta content="Dropbox - Sign in - Simplify your life"
	name="twitter:title">
<meta
	content="Dropbox is a free service that lets you bring your photos, docs, and videos anywhere and share them easily. Never email yourself a file again!"
	name="twitter:description">
<meta content="/static/31443/images/dropbox_icon_64px.png"
	name="twitter:image">
<meta content="TnuSyOnBMNmtugbpL1ZvW2PbSF9LKvoTzrvOGS9h-b0"
	name="google-site-verification">
<meta content="EZKIczQcM1-DVUMz8heu1dIhNtxNbLqbaA9-HbOnCQ4"
	name="google-site-verification">
<meta
	content="tz8iotmk-pkhui406y41y5bfmfxdwmaa4a-yc0hm6r0fga7s6j0j27qmgqkmc7oovihzghbzhbdjk-uiyrz438nxsjdbj3fggwgl8oq2nf4ko8gi7j4z7t78kegbidl4"
	name="norton-safeweb-site-verification">
<title>Dropbox - Sign in - Simplify your life</title>
<link href="https://www.dropbox.com/static/31443/images/favicon.ico"
	rel="shortcut icon">
<link href="/WEB-INF/html/login_files/main.css" type="text/css" rel="stylesheet">
<link
	href="https://www.dropbox.com/static/31443/images/dropbox_webclip.png"
	rel="apple-touch-icon">
<link href="https://www.dropbox.com/w3c/p3p.xml" rel="P3Pv1">
<script type="text/javascript">
	var Constants = {		BLOCK_CLUSTER : 'dl\x2dweb\x2edropbox\x2ecom',
		PUBSERVER : 'dl\x2edropbox\x2ecom',
		WEBSERVER : 'www\x2edropbox\x2ecom',
		NOTSERVER : 'notify1\x2edropbox\x2ecom\x3a80',
		LIVE_TRANSCODE_SERVER : 'showbox\x2dtr\x2edropbox\x2ecom',
		DISABLE_VIDEO_ICONS : false,
		DISABLE_VIDEOS_IN_LIGHTBOX : false,
		block : 'dl\x2dweb\x2edropbox\x2ecom',
		protocol : 'https',
		uid : '',
		email : '',
		sess_id : '192791531664656283533547274075429675206',
		root_ns : 0,
		SVN_REV : '31443',
		TOKEN : '',
		IS_PROD : 1,
		WIT_ENABLED : 0,
		WEB_TIMING_ENABLED : 0,
		upload_debug : false,
		tcn : 'touch',
		date_format : 'M\x2fd\x2fyyyy',
		time_format : 'h\x3amm\x20a',
		datetime_format : 'M\x2fd\x2fyyyy\x20h\x3amm\x20a',
		ADMIN : 0,
		can_undo : 0,
		referrer : '',
		TWO_ITEM_LIST : '\x25\x28x\x29s\x20and\x20\x25\x28y\x29s',
		THREE_ITEM_LIST : '\x25\x28x\x29s\x2c\x20\x25\x28y\x29s\x2c\x20and\x20\x25\x28z\x29s',
		LOCALES : [ [ "en", "English" ], [ "es", "Espa\u00f1ol" ],
				[ "fr", "Fran\u00e7ais" ], [ "de", "Deutsch" ],
				[ "ja", "\u65e5\u672c\u8a9e" ], [ "ko", "\ud55c\uad6d\uc5b4" ] ],
		USER_LOCALE : 'en',
		EMAIL_VERIFIED : 0
	};
</script>
<script type="text/javascript">
	function global_report_exception(e, f, l, tb, force) {
		if (!window.reported_exception || force) {
			var stack_str = "";
			try {
				if (!tb) {
					var stack = get_stack_rep();
					stack.pop(); // remove global_report_exception
					stack.pop(); // remove onerror handler
					stack_str = stack.join("\n");
				}
			} catch (e) {
			}
			var dbr = new Ajax.Request("/jse", {
				parameters : {
					'e' : e,
					'f' : f || window.location.href,
					'l' : l,
					'loc' : window.location.href,
					'ref' : Constants.referrer,
					'tb' : tb || stack_str,
					'trace' : Trace && Trace.get()
				}
			});
			window.reported_exception = true;
		}
	}

	// this constant gets set to true at the bottom of dropbox.js
	window.LoadedJsSuccessfully = false;
	window.onerror = function(e, f, l) {
		global_report_exception(e, f, l);
	};

	var old_onload = window.onload;
	window.onload = function() {
		if (!window.LoadedJsSuccessfully) {
			new Image().src = '/jse?e=failed+to+load+script&loc='
					+ encodeURIComponent(window.location.href);
		}
		old_onload && old_onload();
	}
</script>
<script src="/WEB-INF/html/login_files/dropbox-coffee-mini.js" type="text/javascript"></script>
<style type="text/css">
.hny-netph {
	display: none;
}
</style>
<!--[if lte IE 8]><style>.sick-input input[type=password] {
padding-top: 7px !important;
padding-bottom: 3px !important;
                }
.sick-input.small input {
padding-top: 4px !important;
padding-bottom: 2px !important;
                }
.sick-input.small input[type=password] {
padding-top: 6px !important;
padding-bottom: 0px !important;
                }</style><![endif]-->
<!--[if lt IE 9]><script src="/static/javascript/external/html5shiv.js"></script><![endif]-->
<script type="text/javascript">
	document.observe('dom:loaded', function() {
		LoginAndRegister.init();
	});
</script>
<script>
	if (self != top) {
		top.location.replace(self.location.href);
		setTimeout(
				function() {
					document.body.innerHTML = ("<img src='https://www.dropbox.com/static/images/logo.png' onClick='top.location.href=window.location.href' />");
				}, 1);
	}

	var _gaq = _gaq || [];
	_gaq.push([ '_setAccount', 'UA-279179-2' ]);
	_gaq.push([ '_setDomainName', document.domain ]);
	_gaq.push([ '_trackPageview' ]);

	// Defer so this doesn't block onload.
	window
			.setTimeout(
					function() {
						var ga = document.createElement('script');
						ga.type = 'text/javascript';
						ga.async = true;
						var src_prefix = ('https:' == document.location.protocol ? 'https://ssl'
								: 'http://www');
						ga.src = src_prefix + '.google-analytics.com/ga.js';
						var s = document.getElementsByTagName('script')[0];
						s.parentNode.insertBefore(ga, s);
					}, 0);
</script>
</head>
<body style="min-height: 777px;" class="en None gecko">
	<div style="display: none;" id="modal-behind"></div>
	<div style="display: none;" id="modal">
		<div id="modal-box">
			<a href="#" id="modal-x"
				onclick="javascript: Modal.hide(); Event.stop(event); return false;"></a>
			<h2 id="modal-title"></h2>
			<div id="modal-content"></div>
		</div>
	</div>
	<div style="display: none;" id="modal-overlay"
		onclick="Modal.hide(); Event.stop(event); return false;"></div>
	<div id="floaters"></div>
	<div style="display: none" id="trash-can"></div>
	<div style="display: none" id="grave-yard"></div>
	<div style="display: none;" class="external-drop-indicator top"></div>
	<div style="display: none;" class="external-drop-indicator right"></div>
	<div style="display: none;" class="external-drop-indicator bottom"></div>
	<div style="display: none;" class="external-drop-indicator left"></div>
	<div style="display: none; margin-bottom: -10px" id="translate-div">
		<form action="/translation_suggest"
			onsubmit="TranslationSuggest.submit_suggest(event); return false;"
			method="POST" id="translation-suggest-form">
			<input name="locale" value="en" type="hidden"><input
				name="locale_url"
				value="https://www.dropbox.com/login?cont=https%3A//www.dropbox.com/1/oauth/authorize%3Foauth_token%3D77l2denqckvjuti&amp;signup_tag=oauth&amp;signup_data=86275"
				type="hidden"><input name="msg_id" value=""
				id="translation-msg-id" type="hidden">
			<p class="clean">
				<label for="bad_text">Please paste or type the improper
					translation</label>
			</p>
			<div id="part-one">
				<p>
					<span style="display: none" id="bad-i18n-text-error"
						class="error-message">We could not find that string on this
						page.</span>
					<textarea style="width: 1px;" autocomplete="off"
						class="act_as_block textinput" rows="3" cols="40"
						id="bad-i18n-text" name="bad_text"></textarea>
				</p>
				<div style="display: none;" id="bad-i18n-text-complete"
					class="autocomplete"></div>
			</div>
			<div id="part-two">
				<div style="margin-bottom: 10px" id="translation-msg-display"
					class="emo hotbox"></div>
				<p class="clean">Original English text</p>
				<div style="margin-bottom: 10px" id="translation-orig-msg-display"
					class="green-hotbox"></div>
				<p class="clean">
					<label for="suggested_text">Suggested translation</label>
				</p>
				<p>
					<textarea style="width: 1px;" rows="3" cols="40"
						name="suggested_text" class="textinput act_as_block"></textarea>
				</p>
				<p class="clean">
					<label for="explanation">Explanation of the problem</label>
				</p>
				<p>
					<textarea style="width: 1px;" rows="3" cols="40" name="explanation"
						class="textinput act_as_block"></textarea>
				</p>
				<div class="modal-buttons">
					<input
						onclick="TranslationSuggest.start_wizard(event);return false;"
						id="translation-back-button" value="Back" class="freshbutton"
						type="button"><input
						onclick="TranslationSuggest.submit_suggest(event); return false;"
						value="Suggest translation" class="freshbutton-blue" type="submit">
				</div>
			</div>
		</form>
	</div>
	<div id="notify-wrapper">
		<span class="server-error" style="display: none;" id="notify"><span
			id="notify-msg">An application wants to link to your Dropbox.
				Please sign in or register.</span></span>
	</div>
	<div id="outer-frame">
		<div id="page-header"></div>
		<div id="page-logo-header" class="">
			<a href="https://www.dropbox.com/"><img
				data-hi-res="/static/images/new_logo_2x.png"
				src="/WEB-INF/html/login_files/new_logo.png" alt="Dropbox home" id="db-logo"></a>
			<div id="header-border-div"></div>
		</div>
		<div style="display: none;" id="modal-locale-selector">
			<ul>
				<li><a data-locale="en" class="locale-option">English</a></li>
				<li><a data-locale="es" class="locale-option">EspaÃ±ol</a></li>
				<li><a data-locale="fr" class="locale-option">FranÃ§ais</a></li>
				<li><a data-locale="de" class="locale-option">Deutsch</a></li>
				<li><a data-locale="ja" class="locale-option">æ¥æ¬èª</a></li>
				<li><a data-locale="ko" class="locale-option">íêµ­ì´</a></li>
			</ul>
		</div>
		<div id="page-content">
			<div id="login-and-register-container">

				<div id="login-container">

					<div class="splash">
						<img src="/WEB-INF/html/login_files/login.jpg" id="login_kite"
							alt="A graphic depicting a stick figure flying a kite in a park">

					</div>

					<form action="/login" method="post">

						<div class="alternative-option">
							(or <a id="register-link">create an account</a>)
						</div>

						<div class="title-text">Sign in</div>

						<input name="cont"
							value="https://www.dropbox.com/1/oauth/authorize?oauth_token=77l2denqckvjuti"
							type="hidden"> <input name="signup_tag" value="oauth"
							type="hidden"> <input name="signup_data" value="86275"
							type="hidden">






						<div id="login-partial">

							<div id="email-field" class="sick-input">

								<span class="error-bubble force-no-break" style="display: none;">
									<form:error name="login_email">
										<div class="error-bubble-arrow-border"></div>
										<div class="error-bubble-arrow"></div>
									</form:error>
								</span> <label id="email-label" for="login_email">Email</label> <input
									name="login_email" id="login_email" tabindex="1" type="text">
							</div>


							<div id="password-field" class="sick-input">

								<span class="error-bubble force-no-break" style="display: none;">
									<form:error name="login_password">
										<div class="error-bubble-arrow-border"></div>
										<div class="error-bubble-arrow"></div>
									</form:error>
								</span> <label id="password-label" for="login_password">Password</label>
								<input name="login_password" id="login_password" tabindex="2"
									type="password">
							</div>

							<div id="login-footer" class="clearfix">
								<input id="login_submit" name="login_submit"
									class="freshbutton-blue one-submit-at-a-time" value="Sign in"
									tabindex="4" type="submit">
								<div id="remember-me">
									<input name="remember_me" id="remember_me" tabindex="3"
										type="checkbox"> <label for="remember_me">Remember
										me</label>
								</div>
							</div>
						</div>
						<br class="clear"> <a id="forgot-link"
							href="https://www.dropbox.com/forgot">Forgot your password?</a>
					</form>

				</div>

				<div id="register-container">
					<div class="splash">
						<img src="/WEB-INF/html/login_files/create.jpg"
							alt="A graphic depicting a Dropbox box arriving home">
					</div>

					<form action="/register" method="post">
						<div id="register-title">
							<div class="alternative-option">
								(or <a id="login-link">sign in</a>)
							</div>

							<div class="title-text">Create an account</div>
						</div>

						<input name="cont"
							value="https://www.dropbox.com/1/oauth/authorize?oauth_token=77l2denqckvjuti"
							type="hidden"> <input name="signup_tag" value="oauth"
							type="hidden"> <input name="signup_data" value="86275"
							type="hidden">








						<div id="register-partial">

							<div id="fname-field" class="sick-input">

								<span class="error-bubble force-no-break" style="display: none;">
									<form:error name="fname">
										<div class="error-bubble-arrow-border"></div>
										<div class="error-bubble-arrow"></div>
									</form:error>
								</span> <input name="fname" id="fname" tabindex="5" type="text">
								<label id="fname-label" for="fname">First name</label>
							</div>


							<div id="lname-field" class="sick-input">

								<span class="error-bubble force-no-break" style="display: none;">
									<form:error name="lname">
										<div class="error-bubble-arrow-border"></div>
										<div class="error-bubble-arrow"></div>
									</form:error>
								</span> <input name="lname" id="lname" tabindex="6" type="text">
								<label id="lname-label" for="lname">Last name</label>
							</div>


							<div class="register-spacer"></div>


							<div id="new-email-field" class="sick-input clearfix">

								<span class="error-bubble force-no-break" style="display: none;">
									<form:error name="email">
										<div class="error-bubble-arrow-border"></div>
										<div class="error-bubble-arrow"></div>
									</form:error>
								</span> <input id="email" name="email" tabindex="7" type="text">
								<label id="new-email-label" for="email">Email</label>
								<script type="text/javascript" charset="utf-8"
									src="/WEB-INF/html/login_files/mailcheck.js">
									
								</script>
								<script type="text/javascript">
									new MailCheck('email',
											'email_check_warning');
								</script>
								<div id="email_check_warning" style="display: none;"></div>
							</div>


							<div id="new-password-field" class="sick-input">

								<span class="error-bubble force-no-break" style="display: none;">
									<form:error name="password">
										<div class="error-bubble-arrow-border"></div>
										<div class="error-bubble-arrow"></div>
									</form:error>
								</span> <input id="password" name="password" tabindex="8"
									type="password"> <label id="new-password-label"
									for="password">Password</label>
								<script type="text/javascript" charset="utf-8"
									src="/WEB-INF/html/login_files/password_strength.js">
									
								</script>
								<script type="text/javascript">
									Util.smartLoad(function() {
										PasswordStrength.watch("password");
									});
								</script>
								<div class="password_strength_container">
									<div class="password_strength_bg"></div>
									<div class="password_strength"></div>
									<div style="left: 25%;" class="password_strength_separator"></div>
									<div style="left: 50%;" class="password_strength_separator"></div>
									<div style="left: 75%;" class="password_strength_separator"></div>
									<a style="display: none;" class="password_strength_icon"
										href="#"><img class="sprite sprite_web s_web_information "
										src="/WEB-INF/html/login_files/icon_spacer.gif"></a>
									<div class="password_strength_desc">&nbsp;</div>
									<div class="clearfix"></div>
								</div>
							</div>

							<div id="register-footer">

								<div id="tos-agree-field">

									<input name="tos_agree" id="tos_agree" tabindex="9"
										type="checkbox"> <label for="tos_agree"> I
										agree to <a href="https://www.dropbox.com/terms"
										target="_blank">Dropbox Terms</a>
									</label> <span class="error-bubble force-no-break"
										style="display: none;"> <form:error name="tos_agree">
											<div class="error-bubble-arrow-border"></div>
											<div class="error-bubble-arrow"></div>
										</form:error></span>

								</div>

								<input id="register-submit" name="register-submit"
									class="freshbutton-blue one-submit-at-a-time"
									value="Create account" tabindex="10" type="submit">
							</div>
						</div>
						<br class="clear">
					</form>
				</div>
			</div>
		</div>
		<div id="page-full-footer">
			<div class="footer-col">
				<ul>
					<li class="header">Dropbox</li>
					<li><a href="https://www.dropbox.com/install">Install</a></li>
					<li><a href="https://www.dropbox.com/mobile">Mobile</a></li>
					<li><a href="https://www.dropbox.com/pricing">Pricing</a></li>
					<li><a href="https://www.dropbox.com/teams">Teams</a></li>
					<li><a href="https://www.dropbox.com/tour">Tour</a></li>
				</ul>
				<span style="color: white;">PYXL</span>
			</div>
			<div class="footer-col">
				<ul>
					<li class="header">Community</li>
					<li><a href="https://www.dropbox.com/referrals"
						onmouseup="javascript: MCLog.log('referrals_via_footer');">Referrals</a></li>
					<li><a href="http://twitter.com/dropbox" target="_blank">Twitter</a></li>
					<li><a href="http://facebook.com/Dropbox" target="_blank">Facebook</a></li>
					<li><a href="https://www.dropbox.com/developers">Developers</a></li>
				</ul>
			</div>
			<div class="footer-col">
				<ul>
					<li class="header">About us</li>
					<li><a href="http://blog.dropbox.com/">Dropbox Blog</a></li>
					<li><a href="https://www.dropbox.com/about">Our team</a></li>
					<li><a href="https://www.dropbox.com/news">News</a></li>
					<li><a href="https://www.dropbox.com/jobs">Jobs</a></li>
				</ul>
			</div>
			<div class="footer-col">
				<ul>
					<li class="header">Support</li>
					<li><a href="https://www.dropbox.com/help">Help Center</a></li>
					<li><a href="https://www.dropbox.com/terms">Privacy &amp;
							Terms</a></li>
					<li><a href="https://www.dropbox.com/dmca">Copyright</a></li>
					<li><a href="https://www.dropbox.com/contact">Contact us</a></li>
				</ul>
			</div>
			<div id="locale-container" class="ui-button">
				<span id="locale-link" class="link-span"><img
					src="/WEB-INF/html/login_files/icon_spacer.gif" style=""
					class="sprite sprite_web s_web_world_grey">&nbsp;<a>English</a>&nbsp;<img
					src="/WEB-INF/html/login_files/icon_spacer.gif" style=""
					class="sprite sprite_web s_web_sort-uptick-off"></span>
				<div id="locale-menu" class="sub-nav chat-bubble-bottom">
					<ul>
						<li><a data-locale="en" class="locale-option">English</a></li>
						<li><a data-locale="es" class="locale-option">EspaÃ±ol</a></li>
						<li><a data-locale="fr" class="locale-option">FranÃ§ais</a></li>
						<li><a data-locale="de" class="locale-option">Deutsch</a></li>
						<li><a data-locale="ja" class="locale-option">æ¥æ¬èª</a></li>
						<li><a data-locale="ko" class="locale-option">íêµ­ì´</a></li>
					</ul>
					<div class="chat-bubble-arrow-border"></div>
					<div class="chat-bubble-arrow"></div>
				</div>
			</div>
			<div class="clear"></div>
		</div>
		<noscript>
			<p class="center">The Dropbox website requires JavaScript.</p>
		</noscript>
	</div>
	<!--[if IE]><iframe onload="document.observe('dom:loaded', function() { HashKeeper.reloading=false; });" style="display: none" name="hashkeeper" src="/blank" height="1" width="1" id="hashkeeper"></iframe><![endif]-->
	<div style="position: absolute; top: 0; left: 0; font-family: Courier"
		id="ieconsole"></div>
	<div
		style="position: absolute; top: -10000px; width: 0px; height: 0px; left: 0;"
		id="FB_HiddenContainer"></div>
	<div id="notice-container" class="clearfix">
		<script type="text/javascript">document.observe("dom:loaded", function() {
Notify.server_error('An\x20application\x20wants\x20to\x20link\x20to\x20your\x20Dropbox\x2e\x20Please\x20sign\x20in\x20or\x20register\x2e');
                    });</script>
	</div>
	<script>

        document.observe("dom:loaded", function() {
           TranslationSuggest.attach_autocomplete();
           Util.focus('');
           Util.check_cookies_enabled();
           WebTimingLogger.init();
        });
        

        document.observe("dom:loaded", function() {
            TranslationSuggest.update_i18n_messages({});
        });
        </script>
</body>
</html>