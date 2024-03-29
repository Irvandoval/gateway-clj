(defproject gateway-clj "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [lein-ring "0.12.1"]
                 [compojure "1.6.1"]
                 [ring/ring-defaults "0.3.2"]
                 [ring/ring-json "0.4.0"]
                 [http-kit "2.3.0"]
                 [org.clojure/data.json "0.2.6"]
                 [environ "1.1.0"]]
  :plugins [[lein-ring "0.12.5"]]
  :ring {:handler gateway-clj.core/app
         :port 8000}
  :uberjar-name "gateway-clj-standalone.jar"
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.2"]]}
   :prod {:env {:production true}}})
