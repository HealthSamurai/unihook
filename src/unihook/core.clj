(ns unihook.core
  (:require [cheshire.core :as json]
            [org.httpkit.server :as http-kit]
            [clojure.string :as str])
  (:import org.apache.kafka.clients.consumer.KafkaConsumer
           [org.apache.kafka.clients.producer KafkaProducer ProducerRecord]
           [org.apache.kafka.common.serialization ByteArrayDeserializer ByteArraySerializer])
  (:gen-class))

(def bootstrap-server (or (System/getenv "BOOTSTRAP_SERVER") "localhost:9092"))

(defonce *producer (atom nil))

(def p-cfg {"value.serializer" ByteArraySerializer
            "key.serializer" ByteArraySerializer
            "bootstrap.servers" bootstrap-server})

(defn get-producer [cfg]
  (if-let [p @*producer]
    p (reset! *producer (KafkaProducer. cfg))))

(defn kafka-send [topic payload]
  (.send (get-producer p-cfg)
         (ProducerRecord. topic (.getBytes (json/generate-string payload)))))

(def prefix (str/lower-case (or (System/getenv "TOPIC_PREFIX") "unihook")))

(defn sanitize [x]
  (str/replace x #"[^0-9a-zA-Z]+" "_"))

(defn handle [{body :body :as req}]
  (let [topic (str prefix (sanitize (:uri req)))]
    (println "Send to " topic)
    (kafka-send topic 
                (cond-> (-> req
                            (dissoc :async-channel)
                            (assoc :ts (java.util.Date.)))
                  body (assoc :body (slurp body)))))
  {:status 200
   :headers {"Content-Type" "text/xml"}
   :body "<Response></Response>"})

(defonce server (atom nil))

(defn stop []
  (when-let [s @server]
    (println "Stoping server")
    (@server)
    (reset! server nil)))

(defn restart []
  (stop)
  (let [p (or (Integer/parseInt (System/getenv "PORT")) "8888")]
    (println "Start server on " p " with kafka " bootstrap-server)
    (reset! server (http-kit/run-server #(handle %) {:port p}))))


(defn -main [& args]
  (restart))

(comment
  (restart)
  (stop)

  @server

  (kafka-send "test_topic" {:message "Hello again 222"
                           :revision 2
                           :date (str (java.util.Date. ))})

  )
