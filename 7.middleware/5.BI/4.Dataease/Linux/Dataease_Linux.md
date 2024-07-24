# Docker 在 Linux 上部署 Dataease

> 参考文献：https://hub.docker.com/r/wojiushixiaobai/dataease

1. 拉取 Github 库

	```bash
	# 准备一台 2核4G 以上 Linux 服务器，自行安装 Docker 后执行下面命令：
	
	git clone --depth=1 https://github.com/wojiushixiaobai/dataease
	
	cd ~/dataease
	docker compose up -d
	
	# 用户名: admin
	# 密码: DataEase@123456
	```

---

