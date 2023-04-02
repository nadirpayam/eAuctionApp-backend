FROM openjdk:11

COPY target/eAuctionApp-0.0.1-SNAPSHOT.jar eAuctionApp.jar

ENTRYPOINT ["java","-jar","/eAuctionApp.jar"]

