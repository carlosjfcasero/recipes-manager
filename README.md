# This is your readme from your awesome project

## Running the application

In order to be able to consume the application, there are some properties you need to set while starting app:

- **spring.cloud.gcp.credentials.location**: Path to the firebase service account json 

### From IDE

Create a SpringBoot application launcher with the following parameters:

- Main class: `com.cjfc.recipes-manager.Application`

### Running with docker

1. Compile the project: `mvn clean install`
2. Build docker image: `docker build -t carlosjfcasero/recipes-manager:0.1.0-SNAPSHOT .`
3. Run `docker-compose up -d`

NOTE: If you want to run docker image in Rpi run `docker build -t carlosjfcasero/recipes-manager:0.1.0-SNAPSHOT --platform linux/amd64 --platform linux/arm64 --platform linux/arm/v7 .` in step 2 instead