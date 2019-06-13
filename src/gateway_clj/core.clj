(ns gateway-clj.core
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [gateway-clj.handlers.messaging :refer [inbound-handler]]))

(defroutes app-routes
  (GET "/" [] "Messaging Gateway POC")
  (POST "/messaging-gateway" req (inbound-handler req))
  (route/not-found "Not Found"))

(def app (-> app-routes
             (wrap-defaults (assoc-in site-defaults [:security :anti-forgery] false))
             wrap-json-response
             (wrap-json-body {:keywords? true :bigdecimals? true})))