# Etape 1 : A partir d'une image openjdk11, on valorise les args et variables d'environnement
FROM registry-docker.apps.eul.sncf.fr/hub/eclipse-temurin:17-jdk-alpine
ARG VERSION
ARG ARTIFACT_ID="autotest-services"
ARG APP="/app"
ENV IMG_NAME="$ARTIFACT_ID-$VERSION.jar"
ENV DEFAULT_JAVA_OPTS="-Djava.io.tmpdir=/tmp/ -Dfile.encoding=UTF8 -Duser.language=fr -Duser.region=FR -Duser.country=FR -Duser.variant=FR -Duser.timezone=UTC --add-opens=java.base/java.time=ALL-UNNAMED"
ENV JAVA_OPTS=""

# Etape 2 : Avec l'utilisateur root on met à jour la liste des paquets et on installe les paquets requis
USER root
RUN apk update && \
    apk add --no-cache libcap less vim netcat-openbsd curl iputils net-tools busybox-extras procps bind-tools findutils-locate && \
    rm /var/cache/apk/*


# Etape 3 : On cree le dossier /app avec les bons droits
RUN mkdir -p $APP
RUN chown -R openjdk:docker $APP && chmod 755 $APP

# Etape 4 : avec l'utilisateur openjdk on copie le jar dans le dossier /app avec les bons droits
USER openjdk
COPY --chown=openjdk:openjdk $ARTIFACT_ID/build/libs/$IMG_NAME $APP/$IMG_NAME

# Etape 5 : On lance le jar avec les bons paramètres à l'aide d'un script .sh
WORKDIR $APP
RUN echo ' ' >> launch.sh && echo 'exec java $DEFAULT_JAVA_OPTS $JAVA_OPTS -jar $IMG_NAME' >> launch.sh
ENTRYPOINT ["sh", "launch.sh"]
