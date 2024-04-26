echo "准备下载Tomcat9"
wget https://mirror.bit.edu.cn/apache/tomcat/tomcat-9/v9.0.34/bin/apache-tomcat-9.0.34.tar.gz
echo "正在解压缩Tomcat9"
tar zxf apache-tomcat-9.0.34.tar.gz
echo "防火墙开放8080端口"
firewall-cmd --zone=public --permanent --add-port=8080/tcp
firewall-cmd --reload
echo "启动Tomcat"
cd ./apache-tomcat-9.0.34/bin
./startup.sh
