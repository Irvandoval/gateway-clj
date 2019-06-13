(ns gateway-clj.handlers.messaging
  (:require [ring.util.response :refer [response]]))

(def smooch-root "https://api.smooch.io")
(def messages-endpoint "/v1.1/apps/{appId}/appusers/{userId}/messages")

(defn inbound-handler [request]
  (println (str "Message received " request))
  (response {:message "Message Sent"}))

