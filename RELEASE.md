# Default build

    mvn clean verify

# Build and deploy into Maven Central

    mvn clean deploy -DskipTests -Dgpg.passphrase=<GPG passphrase to access signing key> -Prelease

