FROM openjdk:11

COPY eAuctionApp-0.0.1-SNAPSHOT.jar eAuctionApp.jar

ENTRYPOINT ["java","-jar","/eAcutionApp.jar"]