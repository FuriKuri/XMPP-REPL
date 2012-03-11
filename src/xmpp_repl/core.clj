(ns xmpp-repl.core
  (:use [xmpp-repl.connection]
        [xmpp-repl.contact]
        [xmpp-repl.chat])
  (:import (org.jivesoftware.smack.packet Message)))

(defn login [email password]
  (let [connection (create-connection email password)]
    (regigster-listener connection)
    (add-connection email connection)))

(defn send-message [to msg]
  (let [packet (doto (Message.)
                 (.setTo to)
                 (.setBody msg))]
    (-> (contact-connection to) (.sendPacket packet))))

(defn logout [email]
  (remove-connection email))

(defn show-contacts []
  (doseq [connection (connections-as-list)]
    (doseq [client (contacts connection)]
      (println client "->" (-> connection .getRoster (.getPresence (.getUser client)))))))

(defmacro add-fav
  ([contact] (let [msg (gensym)
                  fname (.replace (.replace contact "@" "-") "." "-")]
              `(intern *ns* (symbol ~fname) (fn [~msg] (send-message ~contact ~msg)))))
  ([contact & more] `(println "not supported")))