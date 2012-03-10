(ns xmpp-repl.core)

(def connections (agent {}))

(defn login [email password]
  (let [[user server] (.split email "@")
        connection (doto (org.jivesoftware.smack.XMPPConnection. server)
                     (.connect)
                     (.login user password))]
    (doseq [x (.getEntries (.getRoster connection))]
      (println x))))