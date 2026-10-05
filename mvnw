#!/bin/sh
# ----------------------------------------------------------------------------
# Maven Wrapper startup shell script
# ----------------------------------------------------------------------------

export MAVEN_PROJECTBASEDIR="${MAVEN_PROJECTBASEDIR:-$(dirname "$0")}"
WRAPPER_JAR="$MAVEN_PROJECTBASEDIR/.mvn/wrapper/maven-wrapper.jar"
WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain

if [ -n "$JAVA_HOME" ]; then
    JAVA_EXE="$JAVA_HOME/bin/java"
else
    JAVA_EXE="java"
fi

exec "$JAVA_EXE" -jar "$WRAPPER_JAR" "$@"
