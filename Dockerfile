FROM azul/zulu-openjdk:15
EXPOSE 8042:8042
RUN mkdir /app
COPY ./build/install/molinfo /app/
WORKDIR /app/bin
CMD ["./molinfo"]
