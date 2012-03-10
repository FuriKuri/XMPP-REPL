(ns xmpp-repl.core
  (:import (org.jivesoftware.smack ChatManagerListener MessageListener)
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
        connection (doto (org.jivesoftware.smack.XMPPConnection. server)
                     (.connect)
                     (.login user password))]
    (regigster-listener connection)
    (send connections conj {email {:connection connection}})))

(defn send-message [to msg]
  (let [packet (doto (Message.)
                 (.setTo to)
                 (.setBody msg))]
    (-> (:connection (@connections "furichan@jabber.org")) (.sendPacket packet))))

(defn logout [email]
  (send connections dissoc email))

(defn show-contacts []
  (doseq [connection @connections]
    (let [roster (.getRoster (:connection (nth connection 1)))]
      (doseq [client (-> roster .getEntries)]
        (println client)
        (println (-> roster (.getPresence (.getUser client))))))))

;; defmulti über macro [&users body]