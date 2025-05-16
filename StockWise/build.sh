#!/bin/bash

# Instalar Java (temporal en el entorno de Render)
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
sdk install java 17.0.9-tem

# Construir con Maven
./mvnw clean package
