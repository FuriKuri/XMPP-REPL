# xmpp-repl
Simple XMPP-Client for the Clojure-REPL.

## Usage

Login: 

```
 (login "email@jabber.org" "password")
```

Send Message:

```
 (send-message "to-email@jabber.org" "Message")
```

Logout:

```
 (logout "email@jabber.org")
```

## License / Used Libraries
Copyright (C) 2012 FuriKuri.
Distributed under the Eclipse Public License, the same as Clojure.

Using code from:

 - smack (xmpp) (Apache Licence)
