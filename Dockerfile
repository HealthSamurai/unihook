FROM java:8
EXPOSE 8080

ENV TOPIC_PREFIX="unihook"
ENV PORT=8888
ENV BOOTSTRAP_SERVER=localhost:9092

ADD target/app.jar /app.jar

CMD java -cp /app.jar clojure.main -m unihook.core
