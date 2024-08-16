### Nginx 的作用

1. HTTP 的==反向代理器==。何谓反向？代理客户端称为正向，而反向就是代理服务器。比如多台服务器构成的集群中，我们可以使用反向代理，将请求均匀分配到每一个节点上，实现==负载均衡==。当然，Nginx 也可以==缓存==某些热点服务器的资源，这样可以提升访问效率。

	==正向代理 and 反向代理==

	> ![image-20240602191410063](nginx.assets/image-20240602191410063.png)

	可以看到，正向代理代理客户端，相当于==客户端不直接把请求发给服务器，而是先发给正向代理==，然后由正向代理再进行发送。相对应的，反向代理代理的是服务器，它将==收到的请求进行负载均衡，然后分发给合适的服务器==。

2. ==动态静态资源扩展==。

	比如一些图片或者 gif 的静态资源，Nginx 无需将此请求发送给服务器了，==直接在自己的本地搜寻此静态资源==，有就直接返回，没有再从服务器获取然后缓存到本地。

---



### Nginx 的优点

1. ==高并发==，最多支持千万级别的并发量，这远远超过了一般中小厂的业务量。
2. 可扩展性好，生态圈好。
3. ==可靠性高==。启动 Nginx 服务后连续好几年都不要再重新启动，其它的一些服务往往几个月甚至几周就要重启一次。
4. ==热部署==。可以在不停止服务的前提下，升级 Nginx 的业务逻辑。
5. ==开源、可商用==。

---



### Nginx 常用命令

```bash
nginx -s stop # 关闭当前运行的nginx服务
nginx -c /etc/nginx/nginx.conf # 使用/etc/nginx/nginx.conf该配置文件启动服务
nginx -t # 测试你当前的配置文件有没有问题，并能查看当前的配置文件是什么
nginx -s quit # 优雅停止
nginx -s reload 
```

---



### Nginx 配置文件

默认配置文件位置：`/etc/nginx/nginx.conf`

```bash
# 运行用户，默认是nginx
user  nginx;
# nginx进程数,一般设置为和cpu核数一样
worker_processes  1;

# 全局错误日志路径
error_log  /var/log/nginx/error.log warn;
# 进程pid路径
pid        /var/run/nginx.pid;


events {
# 最大连接数
    worker_connections  1024;
}


# 设置http服务器
http {
    include       /etc/nginx/mime.types;
    default_type  application/octet-stream;
# 设置日志的格式
    log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
                      '$status $body_bytes_sent "$http_referer" '
                      '"$http_user_agent" "$http_x_forwarded_for"';
# 访问日志的路径
    access_log  /var/log/nginx/access.log  main;

# 开启高效传输模式
    sendfile        on;
    #tcp_nopush     on;
# 长连接超时时间，单位是秒
    keepalive_timeout  65;
#传输时是否压缩，压缩的话需要解压，但是传的大小就小了
    #gzip  on;
#加载其他的配置文件，一带多
    include /etc/nginx/conf.d/*.conf;
}

```

该配置文件引入了`/etc/nginx/conf.d/*.conf`。

```bash
server {
    listen       80;
    server_name  localhost;

    #access_log  /var/log/nginx/host.access.log  main;

    location / {
        root   /usr/share/nginx/html; # 静态资源存放目录
        index  index.html index.htm; # 默认访问的静态资源
    }

    #error_page  404              /404.html;

    # redirect server error pages to the static page /50x.html
    #
    error_page   500 502 503 504 /50x.html; # 出错时，对于错误跳转到的静态资源
    error_page   404 /40x.html;
    location = /50x.html {
        root   /usr/share/nginx/html;
    }

    # proxy the PHP scripts to Apache listening on 127.0.0.1:80
    #
    #location ~ \.php$ {
    #    proxy_pass   http://127.0.0.1;
    #}

    # pass the PHP scripts to FastCGI server listening on 127.0.0.1:9000
    #
    #location ~ \.php$ {
    #    root           html;
    #    fastcgi_pass   127.0.0.1:9000;
    #    fastcgi_index  index.php;
    #    fastcgi_param  SCRIPT_FILENAME  /scripts$fastcgi_script_name;
    #    include        fastcgi_params;
    #}

    # deny access to .htaccess files, if Apache's document root
    # concurs with nginx's one
    #
    #location ~ /\.ht {
    #    deny  all;
    #}
}
```

也就是说，只要把静态资源放到`/usr/share/nginx/html`下，就可以直接通过`ip地址+静态资源名`在浏览器上访问该静态资源。

---



