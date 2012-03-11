(ns xmpp-repl.contact
  (:use [xmpp-repl.connection]))

(defn contacts [connection]
  (-> connection .getRoster .getEntries))

(defn contains-contact [connection email]
  (some #(= email %1)
        (map #(.getUser %1) (contacts connection))))

(defn contact-connection [email]
  (let [av-cons (filter
                 #(not (nil? %1))
                 (map #(when (contains-contact %1 email) %1) (connections-as-list)))]
    (if (empty? av-cons)
      (first (connections-as-list))
      (first av-cons))))