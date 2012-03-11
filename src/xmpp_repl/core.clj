(ns xmpp-repl.core
  (:import (org.jivesoftware.smack ChatManagerListener MessageListener XMPPConnection)
           (org.jivesoftware.smack.packet Message)))

(def connections (agent {}))

(defmulti receive-msg (fn [from _] from))
(defmethod receive-msg :default [from msg] (println "Message from" from ":" msg))

(defn regigster-listener [connection]
  (-> connection (.getChatManager)
      (.addChatListener
       (proxy [ChatManagerListener] []
         (chatCreated [chat created]
           (-> chat (.addMessageListener
                     (proxy [MessageListener] []
                       (processMessage [chat msg]
                         (when (.getBody msg)
                           (receive-msg (first (.split (.getFrom msg) "/")) (.getBody msg))))))))))))

(defn login [email password]
  (let [[user server] (.split email "@")
        connection (doto (XMPPConnection. server)
                     (.connect)
                     (.login user password))]
    (regigster-listener connection)
    (send connections conj {email {:connection connection}})))

(defn send-message [to msg]
  (let [packet (doto (Message.)
                 (.setTo to)
                 (.setBody msg))]
    (-> (contact-connection to) (.sendPacket packet))))

(defn logout [email]
  (send connections dissoc email))

(defn contacts [connection]
  (-> connection .getRoster .getEntries))

(defn contains-contact [connection email]
  (some #(= email %1)
        (map #(.getUser %1) (contacts connection))))

(defn connections-as-list []
  (map #(:connection (nth %1 1)) @connections))

(defn contact-connection [email]
  (let [av-cons (filter
                 #(not (nil? %1))
                 (map #(when (contains-contact %1 email) %1) (connections-as-list)))]
    (if (empty? av-cons)
      (first (connections-as-list))
      (first av-cons))))

(defn show-contacts []
  (doseq [connection (connections-as-list)]
    (doseq [client (contacts connection)]
      (println client)
      (println (-> connection .getRoster (.getPresence (.getUser client)))))))

(defmacro add-fav
  ([contact] (let [msg (gensym)
                  fname (.replace (.replace contact "@" "-") "." "-")]
              `(intern *ns* (symbol ~fname) (fn [~msg] (send-message ~contact ~msg)))))
  ([contact & more] `(println "not supported")))