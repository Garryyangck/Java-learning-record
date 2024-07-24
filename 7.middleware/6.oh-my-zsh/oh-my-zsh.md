> 安装 oh-my-zsh 参考文献：https://regding.github.io/ubuntu-zsh#:~:text=Zsh%201%20%E5%AE%89%E8%A3%85%20Zsh%20%24%20sudo%20apt-get%20install,%E8%8B%A5%E8%BE%93%E5%87%BA%E4%B8%BA%20%2Fbin%2Fzsh%20%E6%88%96%E8%80%85%20%2Fusr%2Fbin%2Fzsh%20%E5%88%99%E8%A1%A8%E7%A4%BA%E5%BD%93%E5%89%8D%E9%BB%98%E8%AE%A4%20Shell%20%E6%98%AF%20Zsh
>
> 安装插件参考文献：https://zhuanlan.zhihu.com/p/441676276

# 安装 oh-my-zsh

1. 安装 Zsh

	```bash
	sudo apt-get install zsh
	
	zsh --version
	```

2. 将 Zsh 设为默认 Shell

	```bash
	sudo chsh -s $(which zsh)
	
	# 注销当前用户重新登录
	
	echo $SHELL  # 若输出为 /bin/zsh 或者 /usr/bin/zsh 则表示当前默认 Shell 是 Zsh
	```

3. 安装 oh-my-zsh

	```bash
	# 通过 curl
	sh -c "$(curl -fsSL https://raw.github.com/robbyrussell/oh-my-zsh/master/tools/install.sh)"
	 
	# 通过 wget
	sh -c "$(wget https://raw.github.com/robbyrussell/oh-my-zsh/master/tools/install.sh -O -)"
	```

---

# 安装主题

1. oh-my-zsh 所有主题的中央仓库：https://github.com/ohmyzsh/ohmyzsh/wiki/Themes

	> 我感觉还是 agnoster 最好看，其它的都一般

2. 安装 agnoster 的官方 WARN：

	> Additional setup:
	>
	> - Install one of the [patched fonts from Vim-Powerline](https://github.com/powerline/fonts) or [patch your own](https://github.com/powerline/fontpatcher) for the special characters.
	> - *Optionally* set `DEFAULT_USER` to your regular username to hide the “user@hostname” info when you’re logged in as yourself on your local machine. Or add `prompt_context(){}` to `~/.zshrc` to always hide the “user@hostname” info.
	>
	> 就是说你要下载特定的字体 Powerline，否则无法正常显示（但是在 XShell 里没有这个问题）

3. 修改配置文件 `.zshrc`，使用 `vim ~/.zshrc`

	```bash
	# 在第11行的位置修改为：
	ZSH_THEME="agnoster"
	```

4. 然后更新 `.zshrc`

	```bash
	source ~/.zshrc
	```

> 后面下载 Powerline 字体，去掉 `用户名@主机名` 的操作直接见参考文献即可

# 安装插件

1. 把插件下载到本地的 `~/.oh-my-zsh/custom/plugins` 目录

	```bash
	git clone https://github.com/zsh-users/zsh-autosuggestions ${ZSH_CUSTOM:-~/.oh-my-zsh/custom}/plugins/zsh-autosuggestions
	
	git clone https://github.com/zsh-users/zsh-syntax-highlighting.git ${ZSH_CUSTOM:-~/.oh-my-zsh/custom}/plugins/zsh-syntax-highlighting 
	```

2. 修改 `.zshrc`

	```bash
	plugins=(git zsh-completions zsh-autosuggestions zsh-syntax-highlighting)
	```

3. 更新 `.zshrc`

---

