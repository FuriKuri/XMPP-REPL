(ns xmpp-repl.connection
  (:import (org.jivesoftware.smack XMPPConnection)))

(def connections (agent {}))

(defn connections-as-list []
  (map #(:connection (nth %1 1)) @connections))

(defn add-connection [email connection]
  (send connections conj {email {:connection connection}}))

(defn remove-connection [email]
  (send connections dissoc email))

(defn create-connection [email password]
  (let [[user server] (.split email "@")]
    (doto (XMPPConnection. server)
      (.connect)
      (.login user password))))