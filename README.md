# goos-auction

auction sniper example

## setup test environment

download XMPP server implementation ,Openfire.
http://www.igniterealtime.org/projects/openfire/

when I was wrote this, the version is 3.7.1.

When you use openjdk, It's prefered to using .tgz version of openfire.
.deb version depends to sun-jdk package, so service script wont to run.
To install sun-jdk for Ubuntu 11.10 is harmfull now, there are many confusing steps.

just unpack .tgz, then running "bin/openfire start".