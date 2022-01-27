## Setting Up Environment
* Java
* Git
* GoogleCloud
* Docker

### Environment Setup - Windows
* [Install JDK 8](https://www.youtube.com/watch?v=Wp6uS7CmivE)
* [Install and setup git](https://www.youtube.com/watch?v=albr1o7Z1nw)
* Google Cloud - [Google cloud essntials](https://www.youtube.com/watch?v=4D3X6Xl5c_Y&list=PLIivdWyY5sqKh1gDR0WpP9iIOY00IE0xL&index=1) - 300$ free credit given by google cloud
    1. [Google Cloud Project setup](https://www.youtube.com/watch?v=P2ADJdk5mYo)
    2. [Install google cloud sdk](https://www.youtube.com/watch?v=Fc6sJu0xres)  
    3. [Add a google storage bucket](https://www.youtube.com/watch?v=sIni4YO6rnY)
    4. [Add a Service Account](https://cloud.google.com/iam/docs/creating-managing-service-accounts) - Save the key json
    5. add permissions for service account(created in step - 4) to storage bucket(created in step - 3)
* [Install docker](https://www.youtube.com/watch?v=S7NVloq0EBc)- optional
* Mysql - 5.7 - optional
    1. Run myql in docker
    2. Install and run mysql

### Environment Setup - Mac
* [Install JDK 8](https://www.youtube.com/watch?v=y6szNJ4rMZ0)
* [Install and setup git](https://www.youtube.com/watch?v=sJ4zr0a4GAs)
* Google Cloud - [Google cloud essntials](https://www.youtube.com/watch?v=4D3X6Xl5c_Y&list=PLIivdWyY5sqKh1gDR0WpP9iIOY00IE0xL&index=1) - 300$ free credit given by google cloud
    1. [Google Cloud Project setup](https://www.youtube.com/watch?v=P2ADJdk5mYo)
    2. [Install google cloud sdk](https://www.youtube.com/watch?v=EdNWgZ4cOWo)  
    3. [Add a google storage bucket](https://www.youtube.com/watch?v=sIni4YO6rnY)
    4. [Add a Service Account](https://cloud.google.com/iam/docs/creating-managing-service-accounts) - Save the key json
    5. add permissions for service account(created in step - 4) to storage bucket(created in step - 3)
    
* [Install docker](https://www.youtube.com/watch?v=MU8HUVlJTEY) - optional
* Mysql - 5.7 - optional
    1. [Run myql in docker](#Running-mysql-in-docker)
    2. Install and run mysql

## Project setup

**Setup google cloud application [default credentials](https://cloud.google.com/sdk/gcloud/reference/auth/application-default/login)**

* Run the following command
```
gcloud auth application-default login
```

* Clone this project 
* Navigate to heartfulness-starter dir
* Run the following command
```
./gradlew clean build
```

* [Eclipse - STS](https://spring.io/tools)**
    1. [Install gradle plugin](https://www.vogella.com/tutorials/EclipseGradle/article.html#install-eclipse-gradle-buildship-tooling)
    2. [Import the gradle project](https://www.vogella.com/tutorials/EclipseGradle/article.html#import-an-existing-gradle-project)

* Setup your IDE's code formatting rules - [here](#Setting-up-your-IDE-for-Standard-Code-Formatting)

## Project Structure
**API**
* The `heartfulness-starter-api` is the project where we define our proto files. To know more about protos, go [here](https://grpc.io/docs/guides/)

* Proto files for our project are present in `src/main/proto/BlogPost.proto`

**Server**
* The `heartfulness-starter-server` is the project where our server code resides. `BlogPostGRPCServiceImpl.java` is where implementation of `BlogPost` grpc service reside
* This is a spring-boot project
* Spring Boot's entry point Main method is present in `StarterApp.java`
* This project refer's to `heartfulness-starter-api` through `compile project(':heartfulness-starter-api')` in `build.gradle`

**Client**
* `heartfulness-starter-client` is a sample implementation of client for our api. This is 
* This is a spring-boot project
* Spring Boot's entry point Main method is present in `BlogPostClientApplication.java`

## Running server locally in eclipse
After importing project in eclipse select `heartfulness-starter-server` project and click `Run -> Run As -> Spring Boot App`

<img width="1379" alt="Screenshot 2019-08-14 at 5 30 30 PM" src="https://user-images.githubusercontent.com/48065591/63019798-0646ab80-beba-11e9-8870-62605d25c088.png">

Select `StarterApp` and click ok

<img width="1379" alt="Screenshot 2019-08-14 at 5 32 14 PM" src="https://user-images.githubusercontent.com/48065591/63019799-0646ab80-beba-11e9-8143-79c31f38777f.png">

Once the server is up, you should see `gRPC Server started, listening on port 30005.`

<img width="1379" alt="Screenshot 2019-08-14 at 5 32 43 PM" src="https://user-images.githubusercontent.com/48065591/63019800-06df4200-beba-11e9-8e6d-7b79c5e5c1a7.png">

## Process

* For any new feature to be developed, go to master branch
```
git checkout master
```

* Create a new branch
```
git checkout -b <feature-short-description>
```

* Push the branch
```
git push --set-upstream origin <feature-short-description>
```

* Do your changes in the branch commit

* [Create a pull request to merge to master](https://www.youtube.com/watch?v=8Bx9e8uk8ko)


## Some useful commands and links

### Build Code

```
./gradlew clean build
```

**Build with skip tests**

```
./gradlew clean build -x test
```

**Build Docker Image** (Requires docker in your local)

```
./gradlew clean buildImage
```

**Publish docker image** (Requires docker in your local) <br/> 
Version/tag of image is in root directory -> build.gradle -> profileServerVersion
```
gcloud docker -- push gcr.io/unifiedplatform-dev/heartfulness-starter-service:<version>
```

**Build Protobuf for endpoints**<br/>

This needs to be built on every change in protos. The generated out.pb file should be used for publishing the endpoint service. <br/>
[protoc](https://github.com/protocolbuffers/protobuf/releases) needs to be installed and available in path. It should be executable.  

```
protoc --include_imports --include_source_info  -I./heartfulness-starter-api/src/main/proto ./heartfulness-starter-api/src/main/proto/BlogPost.proto --descriptor_set_out out.pb
```

### Create release versions

**Check current version**

```
./gradlew cV
```

**New Minor Version**

```
./gradlew createRelease
```

**New Specific Version**

```
./gradlew createRelease -Prelease.version=<version>
```

#### Running mysql in docker
The following docker command runs mysql in port `3307` and root password - `password`
```
docker container run --name mysqldb -v path/to/mysql/data/dir:/var/lib/mysql -p 3307:3306 -e MYSQL_ROOT_PASSWORD=password -d mysql:5.7
```
**If you are getting error last reached error**


`com.mysql.jdbc.exceptions.jdbc4.CommunicationsException: Communications link failure. 
The last packet sent successfully to the server was 0 milliseconds ago. The driver has not received any packets from the server.`

* add a file with name `my.cnf` in `path/to/mysql/conf/dir` with contents
```
[mysqld]
bind-address = 0.0.0.0
```

* run container with following command

```
docker container run --name mysqldb -v path/to/mysql/data/dir:/var/lib/mysql -v path/to/mysql/conf/dir:/etc/mysql/conf.d:/etc/mysql/conf.d -p 3307:3306 -e MYSQL_ROOT_PASSWORD=password -d mysql:5.7
```

#### Connecting to cloud sql

Follow the instruction in [Google cloud docs](https://cloud.google.com/sql/docs/mysql/connect-external-app)

####  Setting up your IDE for Standard Code Formatting
* We have a standard format file checked in under the root directory, named eclipse_java_formatter.xml
* This file is the standard format that we are running our Java source code through.
* Changes to this file will at best create a huge changelist, so make sure you absolutely know what you are doing.
* We also have a standard import order which follows the Eclipse conventions.

**Eclipse**
* In your workspace settings (Window -> Preferences -> Java -> Code Style -> Formatter), click Import button and browse to profil-service root for eclipse_java_formatter.xml file.
* Once you click "OK" button, every Save operation or ALT+SHIFT+F combination will format your source file according to standard format.

**IntelliJ**
* IntelliJ has a plugin that enables using Eclipse format files as source formatting spec. [here](http://plugins.jetbrains.com/plugin/6546)
* While choosing the plugin version, keep in mind that the eclipse_java_formatter.xml file is exported from Eclipse 4.4 and supports Java 1.8
* you might want to set this plugin to work on every SAVE you do on files: [here](http://stackoverflow.com/questions/946993/intellij-reformat-on-file-save)

#### Useful links
* [GRPC](https://grpc.io/)
* [Protobuf](https://developers.google.com/protocol-buffers/)
* [Endpoints](https://cloud.google.com/endpoints/docs/grpc/about-grpc)
* [Getting started GCP](https://www.coursera.org/learn/gcp-fundamentals)
* [Google cloud IAM](https://cloud.google.com/iam/docs/)
* [Google Kubernetes Engine](https://cloud.google.com/kubernetes-engine/docs/concepts/kubernetes-engine-overview)
* [Google Cloud Build](https://cloud.google.com/cloud-build/docs/)
