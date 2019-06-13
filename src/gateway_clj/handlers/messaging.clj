(ns gateway-clj.handlers.messaging
  (:require [ring.util.response :refer [response]]
            [org.httpkit.client :as http]
            [clojure.data.json :as json]))

(def smooch-root "https://api.smooch.io")
(def messages-endpoint "/v1.1/apps/5cfff112a777d40010a21442/appusers/")

(defn log 
  [message body]
  (println (str message body)))

(defn request
  [type from to text]
  (let [{:keys [status headers body error] :as resp} @(http/request {:method :post
                                                                     :url (str smooch-root messages-endpoint to "/messages")
                                                                     :headers {"content-type" "application/json"
                                                                               "authorization" "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6ImFwcF81ZDAxMmY2YmFmMjFhNDAwMTFkODhkMTkifQ.eyJzY29wZSI6ImFwcCIsImlhdCI6MTU2MDQ1MTAwNH0.pYSJ6u9vQ-Iw3y1KYNjyEPbhb4kNJxOjrOe-HvmsRPo"}
                                                                     :body (json/write-str {:text text
                                                                                            :role "appMaker"
                                                                                            :type "text"
                                                                                            :metadata {:type type
                                                                                                       :from from
                                                                                                       :to to}})})]
    (if error
      (log "Failed, exception: " error)
      (log "HTTP POST success: " body))))

(defn send-message
  [body]
  (let [user-id (get-in body [:appUser :_id])
        messages (get-in body [:messages])
        last-message (last messages)
        metadata (get-in last-message [:metadata :type])
        from (get-in last-message [:authorId])]
    (request (:type metadata) from (:to metadata) (:text last-message))))

(defn inbound-handler [request]
  (let [body (get-in request [:body])]
    (log "Message received " body)
    (send-message body)
    (response {:message "Message Sent"})))

