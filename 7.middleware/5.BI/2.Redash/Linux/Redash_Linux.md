# Docker 在 Linux 上部署 Redash

> 参考文献：https://www.dazdata.com/docs/opensource/opensouce_install/installer_introduce/

1. 选定 Redash 安装目录这里假定是 `/opt/redash`

	```bash
	sudo mkdir /opt/redash
	sudo chown -R ${USER} /opt/redash
	cd /opt/redash
	```

2. 创建 `env` 文件，写环境变量，根据自己环境调整环境变量的值

	> env 文件非常重要，没有的话后面使用 `docker-compose up -d` 时会报错找不到 env 文件

	```bash
	#/opt/redash/env/内容
	PYTHONUNBUFFERED=0
	REDASH_LOG_LEVEL=INFO
	REDASH_REDIS_URL=redis://redis:6379/0
	POSTGRES_PASSWORD=aaa123456
	REDASH_COOKIE_SECRET=wo3urion23i4un2l34jm2l34k
	REDASH_SECRET_KEY=u2o34nlfksjelruirk
	REDASH_DATABASE_URL="postgresql://postgres:aaa123456@postgres/postgres"
	ORACLE_HOME="/usr/lib/oracle/12.2/client64"
	LD_LIBRARY_PATH="/usr/lib/oracle/12.2/client64/lib"
	REDASH_FEATURE_ALLOW_CUSTOM_JS_VISUALIZATIONS="true"
	REDASH_ADDITIONAL_QUERY_RUNNERS="redash.query_runner.oracle,redash.query_runner.python"
	```

3. 在安装目录创建 `docker-compose.yml`，注意 image 的值需要修改为使用的 redash 镜像 id。

	> 没有 docker-compose.yml 就不可能进行 docker-compose 操作

	```yaml
	version: "2"
	x-redash-service: &redash-service
	  image: dazdata/redash:v10-21.11.19  # 最新版就是 dazdata/redash:v10-21.11.19
	  depends_on:
	    - postgres
	    - redis
	  env_file: /opt/redash/env
	  restart: always
	services:
	  server:
	    <<: *redash-service
	    command: server
	    environment:
	      REDASH_WEB_WORKERS: 4
	  scheduler:
	    <<: *redash-service
	    command: scheduler
	  worker:
	    <<: *redash-service
	    command: worker
	    environment:
	      WORKERS_COUNT: 4
	  redis:
	    image: redis:5.0-alpine
	    restart: always
	  postgres:
	    image: postgres:12-alpine
	    env_file: /opt/redash/env
	    volumes:
	      - /opt/redash/postgres-data:/var/lib/postgresql/data
	    restart: always
	  nginx:
	    image: dazdata/redash-nginx:latest
	    ports:
	      - "5000:80"  # 映射到本机的 5000 端口
	    depends_on:
	      - server
	    links:
	      - server:redash
	    restart: always
	```

4. 在安装目录，拉起 Redash。

	> docker compose up -d：后台执行，拉取所有需要的 images，并根据配置创建 containers

	```bash
	sudo docker compose up -d
	```

5. 初始化数据库，然后通过浏览器访问服务器 5000 端口即可

	> 不要忘了初始化数据库，不然啥都没有！

	```bash
	sudo docker compose run --rm server create_db
	```
	
6. 第一次打开发现响应速度太慢，经常报错服务端error，重启一下就流畅了

  ```bash
  cd /opt/redash
  
  docker-compose stop
  
  docker-compose start
  ```

---

