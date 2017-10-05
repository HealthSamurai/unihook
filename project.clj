(defproject unihook "0.1.0-SNAPSHOT"
  :description "unihook"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.apache.kafka/kafka-clients "0.10.2.1"]
                 [cheshire "5.6.3"]
                 [http-kit "2.2.0"]
                 [json-schema "0.2.0-RC3"]
                 [clj-time "0.13.0"]
                 [clj-jwt "0.1.1" :exclusions [joda-time]]
                 [org.clojure/clojure "1.9.0-alpha16"]
                 [ring "1.5.1"]
                 [ring/ring-defaults "0.2.3"]
                 [com.draines/postal "2.0.2"]

                 [org.clojure/tools.logging "0.3.1"]
                 [ch.qos.logback/logback-classic "1.2.2"]]
  :profiles {:uberjar {:aot :all :omit-source true}}
  :uberjar-name "app.jar")
