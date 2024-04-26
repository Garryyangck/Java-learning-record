wget https://dlcdn.apache.org/tomcat/tomcat-9/v9.0.88/bin/apache-tomcat-9.0.88.tar.gz
mkdir -p ./tomcat
tar zxvf apache-tomcat-9.0.88.tar.gz -C ./tomcat
sudo firewall-cmd --zone=public --permanent --add-port=8080/tcp
sudo firewall-cmd --reload
cd ./tomcat/apache-tomcat-9.0.88/bin
./startup.sh
netstat -tupln | grep 8080
echo "脚本执行成功"
