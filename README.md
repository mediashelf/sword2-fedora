sword2-fedora
================
sword2-fedora is an implementation of the SWORD 2.0 specification for 
Fedora.

It is currently supports a rudimentary subset of the SWORD spec:

1. retrieval of service documents
2. creating resources via 
	a. binary deposit 
	b. multipart/related
	c. Atom entry
3. generating deposit receipts

Notably absent features:
	
* Replacing the content of a resource
* Deleting the content of a resource
* Adding content to a resource
* in-progress deposit
* on-behalf-of deposit
* Content-negotiation, packaging
	

Build
-----

To build this library use Maven 3:

    mvn clean package
    
    
Installation
------------
Deploy the war to your servlet container. 

Edit sword2fedora.properties (in sword2-fedora/WEB-INF/classes/) with your Fedora and sword2-fedora baseUrls (e.g. http://localhost:8080/fedora/ and http://localhost:8080/sword2-fedora/)


Examples
--------
* Service Document
	
	http://localhost:8080/sword2-fedora/servicedocument/

* Deposit Receipt

	http://localhost:8080/sword2-fedora/edit-media/test:123

