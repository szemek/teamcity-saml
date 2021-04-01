[![Build Status](https://travis-ci.org/szemek/teamcity-saml.svg?branch=master)](https://travis-ci.org/szemek/teamcity-saml)

# teamcity-saml

SAML 2.0 authentication plugin 

## Development

Plugin can be build and deployed by `gradle deployToTeamcity`

Teamcity instance for testing can be setup by `docker-compose up`

## Configuration

Login as administrator and go to Administration > Authentication

Switch to advanced mode and add module **HTTP-SAML 2.0**.

If you don't enable "Allow creating new users on the first login" only users that already exist in Teamcity can login.
It means that you need account with the same name as NameID from SAMLResponse.

You can decide if you want to hide user/password form on login screen (worth leaving unchecked when you test it).

You can consider to disable also "Allow user registration from the login page" in "Built-in" module.

### CORS support

If during authentication you will see similar error like below:

`403 Forbidden: Responding with 403 status code due to failed CSRF check: request's "Origin" header value "null" does not match Host/X-Forwarded-Host header values or server's CORS-trusted hosts, consider adding "Origin: http://localhost:8111" header.`

then follow instruction [https://confluence.jetbrains.com/display/TCD10/REST+API#RESTAPI-CORSSupport](https://confluence.jetbrains.com/display/TCD10/REST+API#RESTAPI-CORSSupport).


## Acknowledgements

Most of the code comes from @pwielgolaski's [teamcity-oauth](https://github.com/pwielgolaski/teamcity-oauth) project.
