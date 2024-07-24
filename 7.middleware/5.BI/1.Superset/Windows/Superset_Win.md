# Docker desktop 在 Linux 上部署 Superset

1. 获取 Superset 镜像：

	```shell
	$ docker pull amancevice/superset
	```

2. 数据库初始化

	```bash
	$ docker exec -it ffc3a60c5d21 superset db upgrade
	```

3. 创建admin用户，设置初始密码

	```bash
	$ docker exec -it ffc3a60c5d21 superset fab create-admin --username admin --firstname Garry --lastname Yang --email admin@superset.com --password admin
	# 用户名：admin
	# 密码：a
	# firstname 名(Garry)；lastname 姓()
	```

4. Superset初始化

	```bash
	$ docker exec -it ffc superset init
	```

	> 使用docker启动Superset后，重启后要重新初始化

---



