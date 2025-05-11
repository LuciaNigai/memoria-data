### Requirements before running the application
1. Have docker installed on your machine

### Running the application
1. **Prod environment** (will create containerized database and application from existing jar file)

    * If you want to customize database credentials you can modify them in  **.env** file (optional)
   
    a. Navigate to the project root directory
    
    b. Build application image from Dockerfile entering the following command in terminal

    ``` shell
        docker build -t memoria . 
    ```
    c. To run the app in production environment navigate to project root and run the following command in terminal

   ``` shell
       docker compose -f docker-compose.yaml -p memoria up -d
   ```
    * It will create and start two containers one for postgres database and another for the app itself.
    * Now you can navigate to http://localhost:8080/home


2. To run the app locally for development purposes

   * If you want to customize database credentials you can modify them in  **dev.env** file (optional)
   
   a. Navigate to the project root directory

   b. Run the following command in terminal, this will create docker container for postgres database that can be used for local development

   ```shell
      docker compose -f docker-compose-dev.yaml -p memoria_dev up -d
   ```

   c. Run the app locally 
   
   by setting the following VM option as the run configuration
      ``` 
      -Dspring.profiles.active=dev
      ```
   
   or by adding the following configuration to application.properties file
   ```shell
   spring.profiles.active=dev
   ```