FROM tmaxrg/tibero6
USER root
COPY license.xml /home/tibero/tibero6/license/license.xml
COPY create_user.sql /
ADD start.sh /
RUN chmod +x /start.sh
CMD ["./start.sh"]
