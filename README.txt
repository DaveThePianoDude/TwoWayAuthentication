03/11/2016

Whew!  Worked with Patrick Santos, Rob Sakmyster, and Anthony Popp (Pope) today to try to get a stand-

alone EJB invocation with 2-way handshake working.  It appears there is still an issue with Wildfly (which

Tony mentioned might be the case).  We're getting an "unexpected byte" error.


The punchline is that there doesn't need to be much done in the way of actual code changes to GMS

any time next week.  What needs to happen is an update to one of the configuration files.  It's mostly

setting System.Environment variables, really.



