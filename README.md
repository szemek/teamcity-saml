[ ![Download](https://api.bintray.com/packages/szemek/generic/teamcity-saml/images/download.svg) ](https://bintray.com/szemek/generic/teamcity-saml/_latestVersion)

# teamcity-saml

SAML 2.0 authentication plugin 

## Installation

Get the latest version of plugin from [ ![Download](https://api.bintray.com/packages/szemek/generic/teamcity-saml/images/download.svg) ](https://bintray.com/szemek/generic/teamcity-saml/_latestVersion) and follow instruction [https://confluence.jetbrains.com/display/TCD10/Installing+Additional+Plugins](https://confluence.jetbrains.com/display/TCD10/Installing+Additional+Plugins).

## Configuration

Login as administrator and go to Administration > Authentication

Switch to advanced mode and add module **HTTP-SAML 2.0**.

If you don't enable "Allow creating new users on the first login" only users that already exist in Teamcity can login.
It means that you need account with the same name as NameID from SAMLResponse.

You can decide if you want to hide user/password form on login screen (worth leaving unchecked when you test it).

You can consider to disable also "Allow user registration from the login page" in "Built-in" module.

## Development

Plugin can be build and deployed by `gradle deployToTeamcity`

Teamcity instance for testing can be setup by `docker-compose up`

## Acknowledgements

Most of the code comes from @pwielgolaski's [teamcity-oauth](https://github.com/pwielgolaski/teamcity-oauth) project.
