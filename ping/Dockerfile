#基础镜像，如果本地仓库没有，会从远程仓库拉取
FROM openjdk:17
#容器中创建目录
RUN mkdir -p /usr/local/ping
#编译后的jar包copy到容器中创建到目录内
COPY target/ping-1.0-SNAPSHOT.jar /usr/local/ping/ping.jar
#指定容器启动时要执行的命令
ENTRYPOINT ["java","-jar","/usr/local/ping/ping.jar"]