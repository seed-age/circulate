Oracle提供的JDK其实已经自带一定程度的热加载功能，但是如果你修改了类名，方法名，或者添加了新类，新方法的话。
Tomcat都需要重新启动来使得刚才的更改生效。
而JRebel和springloaded都能有效地解决这个问题。其中springloaded是开源软件，可以免费使用，尤其难得。
其主页：https://github.com/spring-projects/spring-loaded
1，复制本目录的springloaded jar 到本地目录，比如：c:/temp/springloaded-1.2.5.RELEASE.jar

2. 修改tomcat的应用，禁止tomcat自己的热加载，方法是在WebRoot/META-INF目录下创建context.xml文件，里面包含如下语句，关键便是其中设置reloadable为false
<?xml version="1.0" encoding="UTF-8"?>
<Context antiResourceLocking="false" privileged="true" useHttpOnly="true" reloadable="false" />

3在运行环境中添加springloaded的jar文件，在Eclipse的 Servers 视图中双击tomcat服务器配置，在界面上打开Open launch configuration，在弹出的窗口中，
选择Arguments标签，在vm arguments的末尾添加：
-javaagent:C:/temp/springloaded-1.2.5.RELEASE.jar -noverify
