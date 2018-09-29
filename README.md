# 安装maven

http://maven.apache.org/download.cgi

# 打包

运行shell脚本，比如我们打包测试环境。

```sh build-test.sh```

# 部署

1. 运行成功的话，日志如下。

```bash
$ sh build-test.sh
[INFO] Scanning for projects...
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] Building circulate Maven Webapp 1.0-SNAPSHOT
[INFO] ------------------------------------------------------------------------
[WARNING] The artifact commons-email:commons-email:jar:1.1 has been relocated to org.apache.commons:commons-email:jar:1.1
[WARNING] The artifact javax.xml:jaxrpc:jar:1.1 has been relocated to javax.xml:jaxrpc-api:jar:1.1
[INFO]
[INFO] --- maven-clean-plugin:3.0.0:clean (default-clean) @ circulate ---
......
......
......
[INFO] Packaging webapp
[INFO] Assembling webapp [circulate] in [D:\CodeBase\circulate\target\ROOT]
[INFO] Processing war project
[INFO] Copying webapp resources [D:\CodeBase\circulate\src\main\webapp]
[INFO] Webapp assembled in [54471 msecs]
[INFO] Building war: D:\CodeBase\circulate\target\ROOT.war
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 01:24 min
[INFO] Finished at: 2018-09-29T17:39:35+08:00
[INFO] Final Memory: 39M/587M
[INFO] ------------------------------------------------------------------------
```

2. 拷贝目录`target`下的`ROOT.war`到对应的服务器的Tomcat目录下。