FROM azul/zulu-openjdk:15
RUN mkdir /app
WORKDIR /app
COPY . /app
RUN ./gradlew installDist

FROM azul/zulu-openjdk:15
EXPOSE 8042:8042
COPY --from=0 /app/build/install/molinfo/ .
CMD ["bin/molinfo"]
