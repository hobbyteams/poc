FROM openjdk:17
WORKDIR /

ADD build/distributions/poc1-1.0-SNAPSHOT.tar /

EXPOSE 8080
WORKDIR /poc1-1.0-SNAPSHOT
CMD ["./bin/poc1"]
