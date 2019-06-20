(ns gateway-clj.core
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [gateway-clj.handlers.messaging :refer [inbound-handler]]
            [gateway-clj.handlers.agent :refer [ws-handler]]
            [org.httpkit.server :refer [run-server]]
            [environ.core :refer [env]]))

(defn agent-handler
  [req]
  (println req))

(defroutes app-routes
  (GET "/" [] "Messaging Gateway POC")
  (POST "/messaging-gateway" req (inbound-handler req))
  (GET "/agent" req (ws-handler req))
  (route/not-found "Not Found"))

(def app (-> app-routes
             (wrap-defaults (assoc-in site-defaults [:security :anti-forgery] false))
             wrap-json-response
             (wrap-json-body {:keywords? true :bigdecimals? true})))

(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) 8000))]
    (run-server app {:port port})))
