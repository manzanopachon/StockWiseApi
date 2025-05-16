#!/bin/bash

# Instalar Java usando sdkman
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
sdk install java 17.0.9-tem

# Compilar el proyecto
./mvnw clean package
