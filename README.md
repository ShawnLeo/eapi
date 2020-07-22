# Eapi 接口管理平台
  接口管理、代码生成、Mock数据

* `eapi-server`: 服务端
* `eapi-web`: web端

## Building from Source
You need Java 1.8 and a bash-like shell.

### Building

先构建前端项目
```
cd eapi-web && npm run build
```
启动后端项目

```
gradle bootJar
```
或者导入IDE 启动 EapiApplication
