(ns xmpp-repl.chat
  (:import (org.jivesoftware.smack ChatManagerListener MessageListener XMPPConnection)))

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