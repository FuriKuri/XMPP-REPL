# xmpp-repl
Simple XMPP-Client for the Clojure-REPL.

## Usage

Login: 
```clj
 (login "email@jabber.org" "password")
```
Send Message:
```clj
 (send-message "to-email@jabber.org" "Message")
```
Logout:
```clj
 (logout "email@jabber.org")
```
## License / Used Libraries
Copyright (C) 2012 FuriKuri
Distributed under the Eclipse Public License, the same as Clojure.

Using code from:
 * smack (xmpp) (Apache Licence)
