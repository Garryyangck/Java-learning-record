# Docker Desktop下载攻略！

==（Win11家庭版）==

1. Docker Desktop国内镜像windows系统==安装包==：https://smartidedl.blob.core.chinacloudapi.cn/docker/20210926/Docker-win.exe

	> 下载之后的.exe别急着运行，先看下面

2. 控制面板-程序与系统-启用Windows服务中==没有Hyper-V的问题==

	> 问题原因：使用的是家庭版
	>
	> 解决方法：参考文章https://blog.csdn.net/m0_37802038/article/details/129893827
	>
	> 讲下述代码复制在txt文本里，并重命名为Hyper.cmd，右键以管理员方式运行，最后输入“Y”重启电脑
	>
	> ```powershell
	> pushd "%~dp0"
	> dir /b %SystemRoot%\servicing\Packages\*Hyper-V*.mum >hyper-v.txt
	> for /f %%i in ('findstr /i . hyper-v.txt 2^>nul') do dism /online /norestart /add-package:"%SystemRoot%\servicing\Packages\%%i"
	> del hyper-v.txt
	> Dism /online /enable-feature /featurename:Microsoft-Hyper-V-All /LimitAccess /ALL
	> ```

3. 运行第一步下载的.exe，一路next，最后重启电脑

4. 重启后发现==`WSL 2 installation is incomplete`报错==

	> 问题原因：WSL版本太老
	>
	> 解决方法：下载一个WSL更新包https://wslstorestorage.blob.core.windows.net/wslblob/wsl_update_x64.msi
	>
	> 参考文章https://zhuanlan.zhihu.com/p/368254618

5. 重新启动docker desktop，正常运行

6. ==配置阿里云镜像加速地址==

	> 参考文章https://zhuanlan.zhihu.com/p/441965046，主要看后半部分

---

