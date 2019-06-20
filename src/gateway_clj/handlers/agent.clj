(ns gateway-clj.handlers.agent
  (:require [org.httpkit.server :refer [send! with-channel on-close on-receive]]))

(defonce channels (atom #{}))

(defn connect! 
  [channel]
  (println "Channel open" channel)
  (swap! channels conj channel))

(defn disconnect!
  [channel status]
  (println "Channel closed")
  (swap! channels #(remove #{channel} %)))

(defn notify-clients
  [msg]
  (println "Message" msg)
  (doseq [channel @channels]
    (send! channel msg)))

(defn ws-handler
  [request]
  (with-channel request channel
    (connect! channel)
    (on-close channel (partial disconnect! channel))
    (on-receive channel #(notify-clients %))))