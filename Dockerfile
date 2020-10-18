FROM openjdk:8-jre
COPY svc /svc
EXPOSE 80 443 9000
CMD /svc/bin/start -Dhttp.port=80 -Dhttps.port=443
