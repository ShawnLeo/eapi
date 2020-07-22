# Eapi 接口管理平台

  接口管理、代码生成、Mock数据

gradle > 4.10 & JDK > 1.8 

* `eapi-server`: 服务端
* `eapi-web`: web端

## Quick Start

### Gradle Quick Start：
```
cd eapi-server && gradle bootRun
```
  App running at:
  - Local:   http://localhost:7050 

### Building then Start:
#### Step 1: Start
```
gradle bootJar
```
#### Step 2: Start
```
cd eapi-server/build/libs && java -jar  eapi-server-0.0.1-SNAPSHOT.jar
```


### Import IDE  Start:

#### Step 1: Start Web
```
cd eapi-web && npm run serve
```
#### Step 2: Start Server
 Run Java Application EapiApplication
 
   App running at:
   - Local:   http://localhost:7060 
