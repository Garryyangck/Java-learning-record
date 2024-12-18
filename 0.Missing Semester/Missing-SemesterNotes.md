# 1. 课程概览与shell
## *使用shell*
### 教材部分
- shell根据空格分割命令并进行解i析，如果要传入的参数中包含空格，一种方式是应用单双引号，另一种方法是应用转义字符`\`进行处理（`My\ Photos`）
### 课程视频部分

## *在shell中导航*
### 教材部分
1. shell中的路径是被分割的目录，在Linux和MacOS中使用 `/` 分割，而在Windows上是 `\` 
2. `pwd`：获取当前工作目录
3. `cd`：切换目录
4. `.` 表示当前目录， `..` 表示上级目录(在使用`cd`和`ls`命令时都可以用)
5. `ls`：查看指定目录下含有哪些文件
6. 大多数的命令都可以接受标记选项，通常以`-<命令字母>`的形式表示，例如：
~~~bash
missing:~$ ls --help
-l				#使用较长格式列出信息

missing:~$ ls -l /home
总用量 4
drwxr-xr-x 29 binisalegend binisalegend 4096 8月  11 00:05 binisalegend
~~~
其中，`d`表示`missing`是一个目录，而后面的字母三个为一组。`rwx`、`r-x`、`r-x`分别表示文件所有者(`missing`)、用户组(`user`)以及`其他所有人`所具有的权限：即只有`missing`作为文件所有者具有可写(`write`)权限，用户组和其他访问者均只有可读(`read`)和可执行(`execute`)权限
7. 其他命令：`mv` -> 重命名或移动文件、`cp` -> 拷贝文件、`mkdir` -> 新建文件夹，`touch`新建文件

  > `mv` 是 Linux 和 Unix 系统中用于移动文件或重命名文件的命令。下面是 `mv` 命令的基本用法：
  >
  > ### 移动文件或目录：
  >
  > ```bash
  > mv [选项] 源目标 目标目录
  > ```
  >
  > - `[选项]`：可选的参数，例如 `-i`（交互模式，确认是否覆盖目标文件）、`-u`（仅在源文件比目标文件新或目标文件不存在时进行移动）等。
  > - `源目标`：要移动的文件或目录的路径。
  > - `目标目录`：目标目录的路径。
  >
  > **例子：**
  >
  > ```bash
  > mv file.txt /path/to/target-directory/			#移动
  > ```
  >
  > ![image-20240128215556282](C:/Users/杨宸楷/AppData/Roaming/Typora/typora-user-images/image-20240128215556282.png)
  >
  > ### 重命名文件或目录：
  >
  > ```bash
  > mv [选项] 原文件名 新文件名
  > ```
  >
  > - `[选项]`：可选的参数，例如 `-i`（交互模式，确认是否覆盖目标文件）等。
  > - `原文件名`：要重命名的文件或目录的名称。
  > - `新文件名`：文件或目录的新名称。
  >
  > **例子：**
  >
  > ```bash
  > mv oldfile.txt newfile.txt		#改名
  > ```
  >
  > ![image-20240128215350008](C:/Users/杨宸楷/AppData/Roaming/Typora/typora-user-images/image-20240128215350008.png)
  >
  > ### 注意事项：
  >
  > - 如果目标目录不存在，`mv` 命令会将源文件或目录移动到目标目录并重命名为目标文件名。
  > - 如果目标文件已经存在，会被覆盖（除非使用 `-i` 等选项进行确认）。
  >
  > 请谨慎使用 `mv` 命令，特别是在操作重要文件时，以防止数据丢失。
  >
  > ----
  >
  > `cp`的用法同mv，只不过是
  >
  > ```bash
  > 1.复制文件
  > cp [选项] 源目录 目标目录
  > 
  > 2.复制文件夹
  > cp [选项] -r 源文件 目标文件
  > ```

8. 如果想了解一个函数或者是指令的输入输出形式和工作信息，可以`man <指令>`(manual paper<说明书>的缩写)，**并用`q`退出**

	> ```bash
	> -$ man ls	#ls的说明书
	> ```

### 课程视频部分
1. `cd ~`可以直接返回到home目录下，`cd -`可以快速返回到上一步所在的目录
2. `rm <文件路径>`：只能删除单个文件；`rmdir <文件目录路径>`： 删除一个目录(dictionary)
3. 类似`mkdir my photos`这样的指令会创建两个文件夹分别问my和photos，如果想要创建一个名为my photos的文件夹则需要用双引号引用`mkdir "my photos"`或者使用转义空格`mkdir my\ photos`
3. 比如`/usr/bin`中的所有文件(file)都可以执行`rwxr-xr-x`，因为里面的程序对于所有人而言都应该可以执行
3. 文件有`w`权力，但是其目录(directories)没有`w`权力，那么你只能修改该文件，甚至清空它，但是不能删除它。因为删除涉及到更改目录的内容，但是你又没有目录的`w`权力
3. 目录的`x`权力意味着搜索权力，即能不能进入该目录。想要`cd`该目录，必须有它及它的所有父目录的`x`权力
## *在程序间创建连接*
### 教材部分
1. `< 文件名` 和 `> 文件名` 可以将程序的输入输出重定义到文件，具体如下：
```bash
missing:~$ echo hello > hello.txt
missing:~$ cat hello.txt
hello
missing:~$ cat < hello.txt
hello
missing:~$ cat < hello.txt > helloooootxt
missing:~$ cat hellooooo.txt
hello
missing:-$ cat < hello.txt >> helloooootxt	#使用 >> 是追加内容，而不会清空
missing:~$ cat hellooooo.txt
hello
hello
```
2. 可以借助 `|` 将一个程序的输出与另外一个程序的输入联系起来，例如：
~~~bash
missing:~$ ls -l / | tail -n1  #第一个命令输出所包含全部文件夹的详细信息，第二个命令则输出文件中的最后<number>行
drwxr-xr-x   2 root root       4096 12月 18  2022 work
~~~
### 课程视频部分
1. `>` 相当于在进行一个**覆盖操作**，而 `>>` 则是在进行**追加操作**
2. 吐了吐了...老是看到老师在讲课的时候突然命令行里就能出现上一步的命令内容，查了半天发现最后就是一个上键...直接人傻掉，不过也顺便查到了很多常用的linux快捷键：
~~~
1. Ctrl + L：清空终端中所有的内容并返回当前的目录
2. Ctrl + C：强制中断某个程序的执行
3. Ctrl + D：结束键盘输入（不知道和上面那个有啥区别）
4. !!：重复执行上一步的命令
5. Ctrl Shift C/V：复制粘贴
6. Ctrl + A：快速回到当前行最左一个字符
~~~
3. `|` 被称作"pipe characater"(管道字符)：将程序的输出放在左侧，将程序的输入放在右侧。这也可以对前面的`ls -l / | tail -n1`进行解释，即`ls -l`产生了一个输出，而`tail -2`则是一个需要输入的命令
## *一个功能全面又强大的工具*
### 教材部分
1. 对于类Unix系统，根用户(root user)几乎不受任何限制，需要应用`sudo`(super user do，super user即root用户，课理解为window中的管理员)命令来执行。而有一些操作是只有根用户才能够进行的，例如向`sysfs`文件中写入内容
### 课程视频部分
1. `/sys`目录中存放着电脑的各种内核参数，只有root用户才能对这些内核参数进行更改，除去`sudo`命令之外，我们也可以应用`sudo su`进入root命令行界面来进行操作与更改

2. ```bash
	yanggarry@yanggarry-virtual-machine://sys/class/backlight$ sudo su	//切换到 root 权限的命令
	[sudo] password for yanggarry: ********
	root@yanggarry-virtual-machine:/sys/class/backlight#		#注意此处不是 $ ，而是 # 了，表示现在是 root 权限
	root@yanggarry-virtual-machine:/sys/class/backlight# exit	#使用 exit 命令退出 root 权限
	exit
	yanggarry@yanggarry-virtual-machine://sys/class/backlight$ 
	```

3. tee命令

	> `tee` 命令在 Linux 和 Unix 操作系统中用于从标准输入读取数据，并将其写入文件以及标准输出。这个命令的基本语法如下：
	>
	> ```bash
	> -$ command | tee [选项] 文件
	> ```
	>
	> - `command`：产生输出的命令或程序。
	> - `|`：管道操作符，将命令的输出传递给 `tee`。
	> - `tee`：将输入同时写入文件和标准输出的命令。
	> - `[选项]`：可选的参数，例如 `-a`（追加到文件而不是覆盖）等。
	> - `文件`：要写入的文件名。
	>
	> 
	>
	> **例子：**
	>
	> ```bash
	> -$ echo "Hello, World!" | tee output.txt
	> Hello, World!
	> ```
	>
	> 上述命令将字符串 "Hello, World!" 写入 `output.txt` 文件，并同时在标准输出上显示。
	>
	> ### 选项：
	>
	> - **`-a` 选项**：用于追加内容到文件而不是覆盖文件。例如：
	>
	>   ```bash
	>   -$ echo "Additional Text" | tee -a output.txt
	>   Additional Text
	>   ```
	>   
	>   上述命令将 "Additional Text" 追加到 `output.txt` 文件的末尾。
	>
	> ### 作用：
	>
	> `tee` 的主要作用是**在将接收到的输入输出到目标文件的同时，还会在终端中同时输出该结果**

	
## *课后练习*
1. `touch <新文件名>`：本质上是改变一个文件的时间戳（修改时间），也可以新建一个文件
2. 如果直接访问一个文件夹要用完整的路径例如`/home/binisalegend/missing/semester`，但是实际上发现好像`./semester`就可以
3. ~~写入文件的时候可以使用`>>`来写入文件，暂时看起来的作用是换行
4. `rm -rf <文件路径>`：删除文件
5. 在执行文件遇到权限不足的情况时可以用`ls -l <文件名>`来查看文件权限，然后用 `chmod [权限名称] <文件名>` 来更改文件权限，其中可读权限为4、可写权限为2、可执行权限为1，即如果想分别设置所有者、用户和其他人的权限为全部、可读可写、可读，则语句为 `chmod 764 semester`。具体相关细节详见以下：![capture-2023-08-11-20-57-14](photos/capture-2023-08-11-20-57-14.jpg)
6. 之后的这个问题很有意思，相当于把smester文件执行的内容输出中的一条拷贝到last-modified文件中，具体命令如下：
~~~bash
./semester | grep last-modified > last-modified.txt
~~~
![image-20240129115236201](C:/Users/杨宸楷/AppData/Roaming/Typora/typora-user-images/image-20240129115236201.png)

可以理解成`./semester`是运行这个文件，用 `|` 让后面的`grep last-modified`同时执行，即注意`grep`命令抓取last-modified所在的输出并将其添加到`last-modified.txt`文件中

# 2. shell工具和脚本
- [x] 这节教材没看明白(听课一开始也似懂非懂 所以直接听到啥记点啥)
## *shell脚本*
1. 赋值语句的表示即为`foo=bar`，而访问变量中存储的数值的语法即为`$foo`。值得注意的是，在bash种如果输入命令`foo = bar`是不能正确执行命令的，因为系统会调用`foo`并把`=`和`bar`作为函数的参数。这是本人第一次遇到在语句中**增添空格会导致程序执行失败**的例子，务必多加小心
2. 同时要注意双引号`""`和单引号`''`的区别，即**双引号中会访问变量实际存储的数据值**，而单引号则会全部保留原有的数据格式(变量不会被转义)，演示如下：
~~~bash
~/missing$ echo "Value is $foo"
Value is bar
~/missing$ echo 'value is $foo'
Value is $foo
~~~
3. 一种在权限不足的情况下重新尝试命令的方法是运用 `!!` 命令，即可以在执行某个命令系统提示"Permission denied"后，使用`sudo !!`来**快速运用sudo权限来再一次执行命令**

5. 在bash脚本中有很多特殊的参数来表示参数，错误代码和相关变量，其中一些如下：
	- `$0` - 脚本名
	
	- `$1` 到 `$9` - 脚本的参数。 `$1` 是第一个参数，依此类推。
	
	- `$@` - 所有参数
	
	- `$#` - 参数个数
	
	- `$?` - **前一个命令的返回值**（做检查）
	
		```shell
		yanggarry@yanggarry-virtual-machine:~/Templates/theMissingSemester/lecture02_Shell_tools
		$ ls
		mcd.sh  test
		yanggarry@yanggarry-virtual-machine:~/Templates/theMissingSemester/lecture02_Shell_tools
		$ echo Hello
		Hello
		yanggarry@yanggarry-virtual-machine:~/Templates/theMissingSemester/lecture02_Shell_tools
		$ echo $?
		0	# echo Hello 的返回值为0，代表成功
		yanggarry@yanggarry-virtual-machine:~/Templates/theMissingSemester/lecture02_Shell_tools
		$ grep foobar mcd.sh # 在 mcd.sh 中寻找foobar字符串，失败
		yanggarry@yanggarry-virtual-machine:~/Templates/theMissingSemester/lecture02_Shell_tools
		$ echo $?	#失败返回1
		1
		```
	
	- `$$` - 当前脚本的进程识别码
	
	- `$_` - **上一条命令的最后一个参数**。如果你正在使用的是交互式 shell，你可以通过按下 `Esc` 之后键入 . 来获取这个值。
	
		```shell
		yanggarry@yanggarry-virtual-machine:~/Templates/theMissingSemester/lecture02_Shell_tools
		$ mkdir test
		yanggarry@yanggarry-virtual-machine:~/Templates/theMissingSemester/lecture02_Shell_tools
		$ cd $_ #上一条命令的最后一个参数为 test，因此该命令等效于：cd /test
		yanggarry@yanggarry-virtual-machine:~/Templates/theMissingSemester/lecture02_Shell_tools/test
		$ pwd
		/home/yanggarry/Templates/theMissingSemester/lecture02_Shell_tools/test #当前在test路径下
		```
	
5. 使用`echo $?`检查错误命令时，**输出0表示一切正常没有错误**，输出1表示前面的命令语句出现了问题，其中`true`后`echo $?`返回始终为0，`false`后始终返回1(这个感觉有点奇怪好像Linux里面true和false是小写)

6. 在bash中也有"或"和"与"运算符，它们都属于**短路运算符**。其中`||`或运算符在符号前语句没有0错误代码时(即`echo $?`返回不为0，语句有问题)便会执行符号后的语句，反之，如果符号前语句错误代码为0，则符号后语句会被短路不会被执行，同时如果错误代码均不为0则不输出且错误代码返回1；而`&&`与运算符则只在前后错误代码均为0时才会执行语句。示例如下：![Pasted image 20230813222634](photos/Pasted image 20230813222634.png)

8. 可以使用分号在一行中打印多个语句，不管执行什么都会全部执行所有语句：
~~~
>>> false ; echo "This will always print"
This will always print
~~~
9. 我们可以以变量的形式获取一个命令的输出，这种方式通常被称作命令替换(command substitution), 例如：![Pasted image 20230813232317](photos/Pasted image 20230813232317.png)
	其中，有一些点值得注意：在扩展字符串的时候记得用双引号而不是单引号，原因详见2.2；同时在调用的时候要使用`$ <变量名称>`，可以类比一下C语言中格式化赋予变量值的操作。 ^1f484b
    同时，还有一种相对冷门的类似特性被称为进程替换(process substitution)。其通过**`<(<命令语句>)`来将执行命令的结果保存到一个临时文件中**，同时可以**将两个命令所得结果进行串联**，例如```cat <(ls) <(ls ..)```会打印当前文件目录和上级文件目录中的全部内容名称
    下面是一组应用命令替换和进程替换的例子：
~~~bash
#/missing/example.sh

echo "Starting program at $(date)" # date会被替换成日期和时间
echo "Running program $0 with $# arguments with pid $$"

for file in "$@"; do
    grep foobar "$file" > /dev/null 2> /dev/null #grep用于在某个文件中中搜索子字符串
    # 如果模式没有找到，则grep退出状态为 1
    # 我们将标准输出流和标准错误流重定向到Null，因为我们并不关心这些信息
    if [[ $? -ne 0 ]]; then  # -ne表示不等于
        echo "File $file does not have any foobar, adding one"
        echo "# foobar" >> "$file"  
    fi  # fi表示循环语句结束
done
~~~
程序运行后得到：
~~~bash
~/missing$ ./example.sh mcd.sh
Starting program at 2023年 08月 14日 星期一 18:12:03 CST
Running program ./example.sh with 1 arguments with pid 20563
File mcd.sh does not have any foobar, adding one
~~~
10. 当我们需要提供形式类似的参数时，可以应用shell的通配(globbing)，它可以基于文件扩展名展开表达式，其中主要包含通配符 `*` 和 `?` 以及大括号 `{}` 。其中 **`?`用来匹配一个任意字符**，而 **`*` 可以匹配任意多个字符**；而大括号 `{}` 可以展开一系列包含一段公共子串的指令，这种方式在批量移动和转换文件时非常方便。以下是一些示例：
~~~bash
convert image.{png,jpg}
# 会展开为
convert image.png image.jpg

cp /path/to/project/{foo,bar,baz}.sh /newpath
# 会展开为
cp /path/to/project/foo.sh /path/to/project/bar.sh /path/to/project/baz.sh /newpath

# 也可以结合通配使用
mv *{.py,.sh} folder
# 会移动所有 *.py 和 *.sh 文件

mkdir foo bar

# 下面命令会创建foo/a, foo/b, ... foo/h, bar/a, bar/b, ... bar/h这些文件
touch {foo,bar}/{a..h}  # ..表示省略中间的部分，注意不要随意添加空格
touch foo/x bar/y
# 比较文件夹 foo 和 bar 中包含文件的不同
diff <(ls foo) <(ls bar) #流程替换，相当于将两个输出共同存放到一个临时文件中进行储存比较
# 输出
# < x
# ---
# > y
~~~
11. 同时，脚本并不一定只有在bash中才能使用，我们也可以将其写在诸如python等文件中，通过`shebang`来为内核提供去运行脚本所需的编辑器位置，这时我们通常要在文件开头第一行使用`env`命令，通过环境变量搜索来进行定位，例如`#!/usr/bin/env python`。应用这种方式我们就可以使用任意语言来运行程序了。

## *shell工具*
1. 有时我们在`man`一个函数时由于其雕有了Linux的官方文件，导致其通常会列出该函数的所有用法，显示过于冗长。因此我们可以应用一个`tldr <函数名>`来相对更简洁的获取函数的具体应用方式。

2. 在shell中，我们可以借助`find`命令在仅提供一定模糊信息的情况下来寻找一个文件(夹)所在的位置。其中，`-name` 用来指定所寻找的文件名；`-type` 用来指定所寻找的文件类型(**如`d`表示文件夹。`f`表示文件等**)；`-path`用来指定在何位置路径寻找(可以用* ？等符号来指定有一定相同之处的多个路径)；`-mtime`用来指定修改时间(如`-1`表示最后一天修改的文件内容)等。
   同时，在执行结束`find`语句后可以**应用 `-exec` 直接执行一些相关的命令**。例如`find . -m "*.tmp" =exec rm {} \` 即寻找所有扩展名为tmp格式的文件并且删除。
   PS: 省流简化版：`fd ".*py"` 
   
3. 还有另一个查找文件的命令`locate <文件名>`，会输出所的文件的绝对路径，即简历路径下该文件的索引。同时还可以应用`sudo updatedb`命令来更新数据库。**（不能使用通配符）**

4. 在试图获取文件内容时，我们可以应用 `grep <要搜索的字符串> <文件名>` 函数来执行这个操作，系统会输出包含这个字符串的行；同时我们也可以**用 `grep -R <要搜索的字符串> <文件目录>` 来令程序在整个目录中遍历搜索**。
   还有很多选项如：`-C` (注意是大写) ：获取查找结果的上下文；`-v` 将对结果进行反选，也就是输出不匹配的结果。举例来说， **`grep -C 5` 会输出匹配结果前后五行。**
   
5. 同时还有一系列的获取文件内容的方式，例如 `rg(ripgrep) <搜索内容> <文件名或者搜索路径>` 等，其中`rg`会以更为清晰明了的形式(颜色突出)展现搜索结果，包括文件名、搜索内容所在的行数

6. 我们还有一系列的来查找执行过的命令的方法，一种常用的方式便是使用上箭头，另一种方式则是应用`history <搜索的命令数>`命令，同时也可以利用管道结合`grep`来具有目的性的搜索我们所执行过的指令，即 `history <命令数> | grep <相关字符串>`  。
   我们也可以采用 `Ctrl + R`快捷键来对命令历史记录进行回溯搜索，在按下快捷键后可以通过输入相关字符串来进行精确搜索
   
7. 自己配置了一个zsh环境，个人感觉很好用吧(至少对于我这种喜欢漂亮窗口字体的人来说很好用)，同时也提供了一系列命令提示和代码高亮的功能，具体配置方法可以看下这几个帖子：
   [zsh 简单介绍 - LiuChengloong - 博客园 (cnblogs.com)](https://www.cnblogs.com/manastudent/p/6568287.html)
   [使用 Zsh 作为 Ubuntu 的默认 Shell (regding.github.io)](https://regding.github.io/ubuntu-zsh#:~:text=Zsh%201%20%E5%AE%89%E8%A3%85%20Zsh%20%24%20sudo%20apt-get%20install,%E8%8B%A5%E8%BE%93%E5%87%BA%E4%B8%BA%20%2Fbin%2Fzsh%20%E6%88%96%E8%80%85%20%2Fusr%2Fbin%2Fzsh%20%E5%88%99%E8%A1%A8%E7%A4%BA%E5%BD%93%E5%89%8D%E9%BB%98%E8%AE%A4%20Shell%20%E6%98%AF%20Zsh)
   
   > ![image-20240130000927591](C:/Users/杨宸楷/AppData/Roaming/Typora/typora-user-images/image-20240130000927591.png)
   >
   > ```shell
   > sh -c "$(curl -fsSL https://install.ohmyz.sh/)"
   > 
   > Cloning Oh My Zsh...
   > remote: Enumerating objects: 1379, done.
   > remote: Counting objects: 100% (1379/1379), done.
   > remote: Compressing objects: 100% (1325/1325), done.
   > remote: Total 1379 (delta 30), reused 1133 (delta 27), pack-reused 0
   > Receiving objects: 100% (1379/1379), 3.19 MiB | 559.00 KiB/s, done.
   > Resolving deltas: 100% (30/30), done.
   > From https://github.com/ohmyzsh/ohmyzsh
   >  * [new branch]      master     -> origin/master
   > Branch 'master' set up to track remote branch 'master' from 'origin'.
   > Already on 'master'
   > /home/yanggarry
   > 
   > Looking for an existing zsh config...
   > Found /home/yanggarry/.zshrc. Backing up to /home/yanggarry/.zshrc.pre-oh-my-zsh
   > Using the Oh My Zsh template file and adding it to /home/yanggarry/.zshrc.
   > 
   >          __                                     __   
   >   ____  / /_     ____ ___  __  __   ____  _____/ /_  
   >  / __ \/ __ \   / __ `__ \/ / / /  /_  / / ___/ __ \ 
   > / /_/ / / / /  / / / / / / /_/ /    / /_(__  ) / / / 
   > \____/_/ /_/  /_/ /_/ /_/\__, /    /___/____/_/ /_/  
   >                         /____/                       ....is now installed!
   > 
   > 
   > Before you scream Oh My Zsh! look over the `.zshrc` file to select plugins, themes, and options.
   > 
   > • Follow us on Twitter: @ohmyzsh
   > • Join our Discord community: Discord server
   > • Get stickers, t-shirts, coffee mugs and more: Planet Argon Shop
   > ```
   
8. 最后是一些相对零散的用来进行快速的列表导航的小工具，例如 `ls -R` 可以快速产生文件夹内的文件结构，应用 `tree` 可以以更清楚的方式来展示文件夹内的各种关系等。

## *课后练习*
1. Linux中以 `.` 开头的文件为隐藏文件，可以使用 `ls -all (ls -a)` 命令来显示全部隐藏文件。同时常用的几个命令有：`-g` 不显示文件的所有者，`--human-readable (-h)` 可以将文件的大小按照”MB KB GB"这样易读的形式显示出来，`-lt` 将文件按照访问时间排序并输出详细信息，`ls --color=auto` 可以根据文件类型显示出不同的颜色(如果在zsh中安装了相关的插件就不需要了)。

2. 首先，这个题一定要用到前面的命令替换[[#^1f484b]]，即以变量的形式获取一个命令的输出，在本题中如 `echo "$(pwd)"` 可以获取当前的地址。
   着重理解一下下面这条语句：
   
   ```zsh
   cd "$(cat "$HOME/marco_history.log")"
   ```
   其中 `cd` 后以及 `cat` 后的 `""` 均表明要进入的目录是后面这一段（需要执行引号内的命令——双引号），`$()` 表示命令执行后的内容将会以变量形式调用。
   还有另一种感觉很神奇的思路：
   ```zsh
   #!/bin/zsh
	marco(){
   	export MARCO=$(pwd)
   }
   
	polo(){
      cd "$MARCO"
   }
   ```
   自己理解的大概思路是定义一个**储存当前地址的环境变量 `NARCO`** ，然后直接调用
   
3. 这个其实个人不太理解，从网上查了一下相关的先理解一下题示代码吧，包括[[#^4dd971]]这里也有一样的东西，就是那个 `2> 和 >&2` ，原题示代码和相关知识点理解如下：
   
   > ```shell
   > ./buggy.sh 2>> ./err.log #表示将标准错误流增加到err.log中
   > #同理可得 1>>
   > 
   > ./buggy.sh >> out.log 2>> ./err.log #注意这样写会默认为 ./buggy.sh 1>> out.log 2>> ./err.log
   > 
   > ./buggy.sh &>> out.log 2>> ./err.log #则会将stdout和stderr都输出到 out.log 中
   > ```
   >
   > 
   >
   > ```shell
   > >&2 echo "ERROR!" #表示将后面的echo改为 --标准错误(stderr)
   > ```
   >
   > 
   
   
   
   ~~~zsh
   #!/bin/env zsh
    
   n=$(( RANDOM % 100 ))
   if [[ n -eq 42 ]];then
        echo "Somethong went wrong!"
        >&2 echo "The error was using magic numbers"
        exit 1
   fi
    
   echo "Everything went according to the plan"
   ~~~
   - 每个文件在执行后都会产生至少三个文件描述符，分别是：**0--标准输入(stdin)  1--标准输出(stdout)  2--标准错误(stderr)**
   - 举例来说，假如一个文件夹内只有一个文件`hello1.txt`，当输入命令 `ls {hello1,hello2}.txt` 就会报错 `ls: 无法访问 'hello2.txt': 没有那个文件或目录\nhello1.txt` ，这时我们可以使用命令 `ls {hello1,hello2}.txt >right.txt 2>wrong.txt` ，这样终端中就不会输出内容(因为返回值都重定向到相应的文件中了)，而 `cat` 两个文件内容分别为 `hello1.txt` 和  `ls: 无法访问 'hello2.txt': 没有那个文件或目录` 。
   - 而 `&` 是一个描述符，如果1和2前面不加&，会被zsh视作一个普通文件。在这种情况下，**`(1)>&2` 就表示将标准输出重定向到标准错误中去**，如果标准错误被定义到例如某个log文件，那么标准输出也被定义到那个log文件中去，终端上看不到任何信息。
    然后我们可以在理解源代码的基础上思考如何解题了。又是经典的有思路但一行代码写不出来.... 首先要明确的是在一个脚本文件中可以运行另一个脚本并且将其运行结果进行重定向。然后我们可以引入一个计数器 `count` 初始化为0，应用 `echo > out.log` 命令清除已有的日志；接下来，因为我们要让程序运行到直到随机数产生42才结束，故我们设置一个无限循环，在循环中让程序不断运行，并将其标准输出和错误都重定向到out.log文件中，当运行出现错误即 `$? -ne 0` 时推出新循环，返回count。于是我们得出了以下程序，注意zsh中循环的语法，可以替换为for，until等语句：
~~~zsh
  #!/bin/env zsh

  echo > out.log
  count=0

  while true;do
	  ./example1.sh &>> out.log
	  if [[ $? -ne 0 ]];then
		  cat out.log
		  echo "Example1 went wrong after running $count times!"
		  break
	  fi
  done
~~~
4. 这个就不多bb了好吧，直接解释代码就行了，代码如下：
~~~
~/missing find . -type f -name "*.html" | xargs -d '\n' tar -cvzf html.zip
~~~
管道前面的部分都很好理解，**`xargs` 通俗理解就是可以将一个命令转化传给另一个命令**，`-d` 后面可以指定一个定界符；对于 `tar` 的解释可以查看这个帖子[tar中的参数 cvf,xvf,cvzf,zxvf的区别_tar xvf_想要一百块的博客-CSDN博客](https://blog.csdn.net/qq_42862247/article/details/118031287)，重要的内容如下：
- 参数：
```bash
-c: 建立压缩档案
-x：解压
-t：查看内容
-r：向压缩归档文件末尾追加文件
-u：更新原压缩包中的文件
```

- 这五个是独立的命令，压缩解压都要用到其中一个，可以和别的命令连用但只能用其中一个。下面的参数是根据需要在压缩或解压档案时可选的。

```bash
-z：有gzip属性的
-j：有bz2属性的
-Z：有compress属性的
-v：显示所有过程
-O：将文件解开到标准输出
```
`–delete` 从存档中删除 注意是两个减号。
下面的参数`-f`是必须的  
`-f`: 使用档案名字，切记，这个参数是最后一个参数，后面只能接档案名。
# 3. 编辑器(Vim)
当这一节开始时，我开始意识到，前面的一个我看似理解的指令 `vim ~/.zshrc` 实际上便是用vim编辑器来编辑一个文件，而此时请自动忽略前面所有关于vim中快捷键的内容，包括切换模式等，她们都是在我不了解vim的时候擅自使用后产生的一些自我理解，忘掉他们！Then we start...
PS: 以下内容大部分都可以通过 `vimtutor` 来进行练习

## *Vim的哲学*
Vim的思路是，在进行编程时将手从键盘移动到鼠标是一种极其繁琐费时的操作，所以在Vim中所有操作都可以通过键盘来实现。
因为用的不太熟练，也不明白原理，所以把可能每次都需要配置一下的命令记录在这里，可以把Esc和Ctrl键重定义到CapsLock键（借用了这位大佬的思路[将 Caps Lock 映射为 Escape 和 Ctrl | weirane’s blog (ruo-chen.wang)](https://blog.ruo-chen.wang/2020/04/map-capslock-to-esc-and-ctrl.html#%E4%B9%8B%E5%89%8D%E7%9A%84%E9%85%8D%E7%BD%AE)）：

~~~shell
#将CapsLock定义为Ctrl
setxkbmap -option ctrl:nocaps
#将CapsLock定义为Esc
xcape -e 'Control_L=Escape'
~~~
## *编辑模式*
vim编辑器中通常处在标准模式(normal)，还有一系列其他的模式如插入模式(insert): 用来编辑插入代码(`i`)、替换模式(replace): 用于覆盖文本，将前面的代码向前推(`R`)、一般可视化模式(visual)(`V`)、可视化行模式(visual-line)(`shift + V`)、可视化块模式(visual-block)(`Ctrl + V`)、命令行模式(command-line)(`:`)等。(返回normal模式时按下`Esc`)

## *基本操作*
这其中的一大部分操作是通过命令行模式(command-line)来实现的，即在标准模式下按下 `:` 后输入命令。可以将其类比成上两节中的shell，只不过这个命令行窗口属于vim。命令行窗口有一系列可执行的命令，例如：
~~~vim
:q(quit) :qa 退出 退出所有窗口
:w(write) 保存
:wq 保存并退出
:help <command> 显示对command的帮助文档
:e <filename> 打开要编辑的文件
:ls 打开现在已经打开的文件列表（缓存）
:sp 将同一个文件(缓冲区)在上下两个窗口中打开
~~~

## *Vim接口作为编程语言的一些特性*
vim正常情况下处于标准模式，在标准模式下，有很多快捷键可以让我们很方便快捷的对文件进行移动、检索、编辑等一系列操作，具体的一些实例如下：(救命心态真的会崩太多了沃日)
- 移动命令
~~~vim
h j k l: 左下上右移动光标
w: 将光标移动到下一个单词
b: 将光标向前移动到单词开头
e: 将光标移动到单词末尾
0 $: 将光标移动到行首和行末
^: 将光标移动到一行中第一个非空字符
Ctrl + U/D: 将整个页面向上或向下翻动
gg/G: 移动到整个文件的第一行和最后一行
L M H: 将光标移动到当前显示界面的最下方/中间和最上方
f/F<字符> t/T<字符>: 寻找到在当前光标所在位置之前/之后的第一个所求字符，f在其之上，t在其之前/后
/: 进行搜索 使用N/n分别进行向上和向下的查找
%: 用来找到如中括号等符号所对应的另一半
~~~
- 编辑命令
~~~vim
i：插入命令，在当前位置切换到Insert模式，可以插入字符
O/o：在光标所在行上/下新建一行，并切换到输入模式
x：删除光标所在当前字符
r/R + <替换字符>：用替换字符对一个或多个字符进行替换
u/Ctrl+R：撤销，分别等同于Ctrl+Z和Ctrl+Shift+Z
~：可以将小写字母转换为大写字母(可结合可视化模式使用)
~~~
   以下的四个常用命令都可以结合**可视化模式(Visual Mode)来进行，分别用v、V和Ctrl+V来进入基本、行和可视化块模式**，并结合移动键来更便捷的实现选中操作。
~~~vim
d + <移动命令>：删除(如dw表示删除一个单词，de表示删除到这个单词的末尾，dd表示删除整行)
c + <移动命令>：基本和上面命令效果相同，只是删除之后进入插入模式
y&p + <移动命令>: 操作方式与上面相同，分别表示复制和粘贴
~~~
- 计数命令
以上命令都可以结合数字来确定一个命令执行的次数。例如`4j`表示向下移动四行，`3e`向前移动三个单词，`7dw`表示删除7个单词等。有用的一点是vim中左侧标出了其他行与当前的差值行数，就可以根据这个应用`<number> j/k`来进行快速导航了。
- 修饰命令
修饰符主要有 `i` 和 `a` ，分别表示“在内“和”周围“的含义。以下是一些示例：
~~~vim
ci( : 改变当前括号内的内容
di[ : 删除方括号内的内容
da' : 删除一个单引号字符串，包括周围的单引号
~~~

## *demo示例*
1. 可以借助 `G` 命令快速跳转文件的最后一行以添加新的内容
2. 借助 `/` 搜索命令可以快速跳转到你想编辑的位置
3. `e` 移到词末，`w` 向前移动一个单词，`$` 移到行末等命令在定位并修改一些细小错误中非常有用
4. `.` 可以重复执行刚刚进行过的编辑命令等
4. 使用`:sp`分屏，并使用`Ctrl + w` + 上下左右切换屏幕

# 4. 数据整理

^cd331a

- [x] 破防了破防了家人们谁懂啊，真的这节课就感觉一个字没听懂我真的服辣(50分钟的课我听了37分钟我甚至不知道我该从哪里记起)，没办法，放链接吧...
      视频链接：[Lecture 4: Data Wrangling (2020) - YouTube](https://www.youtube.com/watch?v=sz_dsktIjt4)
      教材链接：[数据整理 · the missing semester of your cs education (missing-semester-cn.github.io)](https://missing-semester-cn.github.io/2020/data-wrangling/)
- 哦莫，Java课讲这个了，这下不得不听了（睡），移步那边吧~[[Java程序设计笔记#^b2609e]]
# 5. 命令行环境
## *任务控制(Job Control)*
### 结束进程
当我们在命令行环境中按下 **`Ctrl + C`，shell会为我们打断一个正在进行的进程**。深层来讲，终端会向程序发送一个 `SIGINT --> signal interrupt` 信号告诉程序自行停止。我们可以通过 `man signal` 指令来查看这些信号以及其对应的数字表示符和简短的描述。
我们可以使用以下代码来简单验证一下两个信号指令 `SIGINT` 和 `SIGQUIT` ：

~~~python
#!/usr/bin/env python
import signal, time

def handler(signum, time):
    print("\nI got a SIGINT, but I am not stopping")

signal.signal(signal.SIGINT, handler) #此处提供解决SIGINT的方法，即调用handler函数
i = 0
while True:
    time.sleep(.1)
    print("\r{}".format(i), end="")
    i += 1
~~~
> - 之所以程序接收到SIGINT不停止，是因为其**被程序捕获了，会执行对应的handler操作**，而由于无法捕获SIGQUIT的方法，因此**接收到SIGQUIT命令时，会执行其默认的操作，即终止程序**

当我们按下 `Ctrl + C` 时，代码会捕获SIGINT指令并且返回语句，但是计数程序并不会停止运行；而当我们再次按下 `Ctrl + \` 时，程序并不会捕获到SIGQUIT指令，指令生效后程序会有如下提示并且立即停止：
![Pasted image 20230826232551](photos/Pasted image 20230826232551.png)
而对于如下情况，当我们按下 `Ctrl + Z` 时，相当于终端发送了一个 SIGTSTP(Terminal Stop) 信号让正在运行的进程停止，但这个程序停止在后台并且可以通过 **`fg` 让它在前台继续运行和 `bg` 让它在后台继续运行**（也就是说可以通过 `Ctrl + Z` 和 `bg` 命令一起使用来让一个运行中的进程在后台运行
![Pasted image 20230826233231](photos/Pasted image 20230826233231.png)

### 暂停和后台执行进程
`jobs` 命令可以显示出当前正在运行和后台运行或者暂停的进程数并将它们标号，可以通过 `命令 + %<命令编号>` 来对某个进程进行操作(调用最近的进程也可以用 `$!`)
在**进程命令后加 `&` 符号**也可以让一个进程直接在后台启动运行，这样我们就可以继续在shell的窗口进行一系列其他的操作
然而，进入后台的进程依然是这个终端的子进程，也就是在关闭终端的时候这个进程也会被终止，如果想要避免这种情况可以在进程命令前加上 **`nohup`** 指令来实现，即**防止关闭终端时终止后台的子进程**
我们也可以用 **`kill <-SIGHUP> / <-STOP> %<命令编号>`** 来终止一个进程

## *终端多路复用(Terminal Multiplexers || tmux)*
- 像 `tmux` 这样的终端多路复用器可以让我们**分出多个终端窗口**，这样我们可以同时与多个shell对话进行交互。 `tmux` 有三个基本思想分别是 “**Sessions Windows Panes --> 会话 窗口 窗格”** （会话 > 窗口 > 窗格）

- 在shell中直接输入 `tmux` 命令就可以新建一个tmux会话**( 可以通过** **`tmux new -t <窗口名>` 命令来新建一个会话并给tmux会话进行命名)**，看起来和我们平时使用的zsh没有什么不同，但我们可以在其中运行一些相关的进程，并通过 **`Ctrl + B` 后再按下 `D` 来返回到原本的zsh窗口**，在**zsh中使用 `tmux a(attach) -t <tmux会话名称>` 命令来返回到刚刚的tmux会话**，也可以在**tmux中使用 `exit` 命令或者是 `Ctrl + D` 快捷键来彻底退出当前的tmux会话**

- 在一个tmux会话中，我们可以使用 **`Ctrl + B` 再按下 `C(create)` 来新建一个窗口**，这个操作**相当于tmux为我们新建了一个与shell的对话窗口**，我们可以在新的窗口中执行一系列其他命令。同时，我们还可以用 **`Ctrl + B` 再按下 `P(privious) || N(next) || <窗口编号>` 来进行窗口之间的切换** 

- **使用`Ctrl + B` + `,` 的方式更改窗口名字**

- 同时，我们可以用 **`Ctrl + B` 再按 `"` 或者 `%` 来新建一个窗格**，实现类似于**上下和左右分屏的效果**，可以使用 **`Ctrl + B` 加上上下左右箭头键在一系列窗格中切换**。使用 `Ctrl + B` 再按 `Z` 可以切换当前面板在全屏和普通模式， **`Ctrl + B` 加上空格键可以在不同窗格排布中切换**

## *别名(Aliases)*
alias是一种相当于可以自定义快捷键的的指令，可以为一个长命令设置一个别名，它的语法如下：
~~~shell
alias aliad_name="command_to_alias arg1 arg2"
#注意等号两边没有空格
~~~
alias命令可以在很多种情况发挥作用，除了直接定义像 `alias gs="git status"` 这样的别名以外，我们还可以对一些我们容易打错的命令来使用它，例如 `alias sl=ls` ，包括对一个命令进行更加细致的规划如 `alias mv="mv -i"` 等等。我们也可以用 `alias <别名>` 来查看一个别名代表了什么具体内容
## *配置文件(Dotfiles)*
很多程序的控制都是通过被称为点文件(Dotfiles)来控制的，例如 `~/.vimrc ~/.zshrc` 等， 他们通常是隐藏文件，`ls` 并不会显示他们
我们可以通过对这些文件的编辑和修改来修改相关的配置，例如添加上一小节的alias到bashrc文件中就可以在每次启动bash时都执行相关的命令
- [ ] (这部分没弄懂)我们还可以通过建立一个dotfiles文件夹来整理所有的配置文件，通过符号链接将他们链接到他们需要的位置，并将其传递到如Github这样的云端，以便我们可以在各种设备上配置我们的终端
## *远端设备(Remote machines)*
我们通常使用ssh(safe shell)来远程连接服务器，ssh最基本的运行格式是
~~~shell
ssh <用户名> @ <主机IP地址> (需要远程执行的命令)
~~~
当然，使用这种方式需要我们每次链接都输入密码，这显然有些不太方便，我们可以通过**SSH密钥**来解决这个问题，首先我们需要**让主机产生SSH密钥**，然后将产生的密钥**传递给远程主机**，以我自己的虚拟机为例，具体命令如下：
~~~shell
ssh-keygen -o -a 100 -t ed25519 -f ~/.ssh/id_ed25519
cat .ssh/id_ed25519.pub | ssh <用户名> @ <主机IP地址> tee .ssh/authorized_keys
# 我们还可以用以下命令来保存密钥的密码
ssh-copy-id <用户名> @ <主机IP地址>
~~~
执行完以上命令后，我们就可以直接通过ssh命令连接主机而不用每次都输入密码了
我们还可以用一些和本身命令行相近的命令来使他们在ssh使用中生效，例如复制文件命令
~~~shell
# tee命令：
cat 本地文件 | ssh <用户名> @ <主机IP地址> tee 远端文件
# scp命令：
scp 本地文件路径 <用户名> @ <主机IP地址>: 远端文件路径
#以下是shell原本的命令 做个对照
cp notes.md notes.md
~~~
如果我们想复制大量文件，则可以使用另一个更好的命令 `rsync`
~~~
ryscp -avP . <用户名> @ <主机IP地址>:<文件夹名>
~~~
我们还可以通过配置文件进行对ssh的自定义配置，文件及其地址为 `~/.ssh/config`，例如我们可以通过以下配置来自定义远程主机名，这样我们就不需要在每次连接时输入繁琐的主机名和地址
~~~
Host <自定义主机名>
  HostName 主机IP地址
  User 用户名
  IdentityFile SSH密钥地址
~~~
同时，我们在远程连接的主机中如果展开了一个tmux，我们可以在其中运行程序执行命令，并且退出主机远程连接，当我们重新连接时一切都不会有变化
# 6. 版本控制(Git)
版本控制系统是用来追踪源代码或者其他文件或文件夹集合的更改工具，他们有助于追踪某个文件集的修改历史纪录，同时他们也能促进团队协作，因为可以有很多人共同协作更改某个文件集。同时，我们可以查看自己程序的不同版本来对比所出现的问题，可以查看有谁对某个东西提交了更改并且我们自己也可以对这些文件集进行修订；在多种版本控制工具中Git已经成为了最被大众所接受的工具之一
## *Git的数据模型*
这一部分主要讲述了Git的工作机制，主要通过关联快照和历史记录建模，以及对象内存寻址的运行机制来实现，这一部分具体请看官方教程文档（中文文档地址：[版本控制(Git) · the missing semester of your cs education (missing-semester-cn.github.io)](https://missing-semester-cn.github.io/2020/version-control/)）
## *Git的命令行接口*
### 基础
- 首先我们可以使用`git init`命令来初始化一个文件夹使其变成一个Git仓库，当我们执行命令以后，系统会在该文件夹中新建一个名为`.git`的隐藏文件夹用来储存本地磁盘中的存储库(`repository`)数据
- `git help <命令名>`可以对某个特定命令给出具体解释，类似Linux的 `man` 命令
- 当我们输入 `git status` 命令时可以看到当前仓库的（快照）状态，`Untracked files` 表示该文件经过了改动或者是新建的，即Git在对比过已有的系统快照后发现了不同，没有进行记录，这时我们可以使用 `git add <文件名/文件夹名>` 来将这个未被追踪的文件添加到系统快照中
  接下来，我们可以使用 `git commit <-m "备注">` 来将我们所做的改动提交到暂存区中，注意既可以用 `-m` 命令来在后面直接添加提交备注，也可以跳转到提交界面添加备注，在提交成功后终端会显示提交的信息以及该快照的哈希值（一个Git用来记录文件上传信息的16位数字），我们可以使用 `git cat-file -p <hash numbers>` 来获取刚刚commit的信息内容（`git log` 也可以获取暂存日志虽然，这个好像可以输出上传文件中的内容）
- `git add :/` 可以将所有的未追踪文件都添加到系统快照中，`git commit -a` 可以将所有没有被追踪的文件都添加到缓存区中（原谅某个笨比一次次打超长的文件名...）
- `git log` 可以用来获取版本历史纪录，但是老师说这个扁平化的看起来很烦，推荐了一个 `git log --all --graph --decorate` 来让版本更新记录图标化显示（虽然命令很长且我没看出什么区别，但我相信zsh插件会记住它的）
- 通常Git会默认创建一个 `master` 分支作为库中代码开发主要分支，代表着项目的**最新快照版本(即最后一次commit)**。我们可以将 `master` 视作一个指向当前提交的指针，当我们继续添加提交时，这个指针也会发生变化来指向新的提交；而在log中提示的 `HEAD` 则通常表示你现在**正在处于**的提交状态（通常是最近的提交，如果需要切换可以使用 `git check <parts of hash numbers>` ，这会将当前工作目录的状态切换回你所指向的提交时的状态，我们可以在最新状态之前对文件或者目录进行修改，但是在返回master目录时checkout会提示报错，此时可以用 `-f` 命令来强制执行）
- `git checkout <文件名>` 是对checkout命令的另一种使用，他会丢弃掉当前对文件所做的一切更改并且将当前文件的状态返回到 HEAD 快照所在的状态 
- `git diff (<hash numbers> <hash numbers>) <文件名>` 命令可以查看修改前后的不同之处，包括在不同工作目录下对文件修改前后的不同之处
### 分支和合并
1. 使用 `git branch <新建分支名>` 可以查看现有的分支或者新建一个分支，这个新建的分支会指向当前提交的内容，例如如果我们新建一个名为cat的分支，我们再次应用 `git log --all --graph --decorate` 命令时就会看到当前HEAD仍然会指向原有的master分支，但是在后面会新建一个cat分支（使用 `git checkout <分支名>` 可以切换到新的分支，同时会发现此时HEAD指向了我们新建的分支，所显示的内容是master分支的最新提交内容）
2. `git commit` 中可以应用 `:x` 命令来快速上交暂存备注
3. `git branch -vv` 以非常详细的方式展现已有的分支信息
4. 不理解这个为什么不能早点教，害我一直看着很长一段log发愁：`git log --all --graph --decorate --oneline` 可以在一行中显示暂存日志，基本上是只显示哈希码和后面的暂存提交备注
5. 当我们在一个分支中对某个程序进行了修改，再使用 `git checkout <分支名>` 切换回其他分支时会发现发刚刚作出改动的内容并不会显示在当前的工作目录中，这样我们就可以实现在并行的开发线中来回跳转
6. 可以应用 `git branch <分支名> ; git checkout <分支名>` 来实现新建一个分支并且跳转到新建的工作表目录中去，简化版命令是 `git check -b <分支名>` （盲猜-b全拼是-branch）
7. 当我们在多个分支工作目录中对代码进行改动后，我们同通常会需要将功能进行合并，这是我们可以先切换回master分支中，应用 `git merge <分支名> ...` 来将不同分支中的内容进行合并，指向master的指针HEAD会指向master和cat，前后对比图如下：![[Pasted image 20230903202528.png]]
在执行合并操作的时候，Git可能会对一些合并不是太灵敏，这时会出现合并冲突问题（merge conflict），这是需要我们手动在文件中进行符合我们预期的修订后重新add更改，然后执行 `git merge --continue` 命令继续执行合并操作
![[Pasted image 20230903210606.png]]
### 远程操作
实际上，对于Git的远程操作主要是为了能够和他人一起协作，以及将自己的本地储存库上传到像GitHub、gitee等云端仓库
1. `git remote` 命令可以查看当前所有已经连接的远程仓库
2. `git remote add <远程连接名> <远程仓库http地址>` 可以远程连接一个新的仓库
3. `git push <远程连接名> <local branch> : <remote branch>` 用来将本地仓库推送到远程仓库中去，当我们仅将一个本地分支推送到基本固定的一个远程仓库分支时，可以使用 `git branch --set-upstream-to=<远程仓库名>/<分支名>` 可以配置一个固定的本地和远程分支连接，这样下次我们只需要使用 `git push` 即可完成推送工作
4. `git clone <URL> <要复制到的文件夹>` 可以从远端仓库下载内容到本地
5. `git fetch` 可以从远端获取对象、索引、推送记录等信息，检索对远端存储库的更改并且将这些信息保存到本地计算机中；同时还有一个命令 `git pull` 可以等同于命令 `git fetch; git merge` ，即从远端拉取内容并且与本地分支进行合并
### 撤销操作
`git commit --amend` 可以重新编辑提交的内容和信息
`git restore` 可以撤销在工作区中的很多操作
`git checkout <文件名>` 可以丢弃在某个文件中所做的修改
### 一些高级一点的操作
`git clone --shallow` 可以用于当我们要下载一个内容较大的仓库，这样我们就不会保存一系列版本历史记录信息，能更快地从远程仓库拉取到我们本地
`git add -p <文件名>` 让我们可以以交互式的方式来选择具体上传什么，例如我们可以选择只对一处更改进行上传并添加快照；`git diff --cached` 可以让我们查看到即将上传的修改是什么
`git blame` 可以让我们查看到谁对某行或者某个文件做出了修改，以及相关的一些信息
`git stash` 可以使我们返回到上一个操作我们所处的工作目录状态，暂时移除工作目录下的修改内容；再使用一次 `git stash <Filename>` 就会取消对文件修改的隐藏操作
`git bisect`: 通过二分查找搜索历史记录，比如跳转到某个时间的提交快照
这个个人感觉很有用，可以通过配置 `.gitignore` 文件来忽略某些文件，例如一些系统文件、程序编译文件等，通过添加文件名或者类似 `*.exe*` 这类东西来使在查看 `git status` 时忽略他们 
## *笨狗的小记：如何上传本地文件到github/gitee库*
1. 初始化：`git init`：初始化，将一个新文件夹变成git可管理的仓库
2. 连接github库：`git remote add github <Github库的https链接>`
   连接gitee库：`git remote add gitee <gitee库的https链接>`
3. 可以使用 `git remote -v` 查看已经连接的远程仓库
3. 本地文件修改上传：`git status`：查看当前文件夹修改的状态
   1. `git add <文件名> or <文件路径>`：将新增或者删减文件上传到本地暂存库
   2. `git commit -m "备注信息"`：改动内容上传到本地库
5. 将本地仓库拉取到GitHub：`git push -u <远程仓库名称> master`：拉取本地库
5. Tips: 
   1. `git branch`：查看库中的分支(前面带`*`的表示现在正处于的branch)
   2. `git check <分支名>`：切换到其他的分支
   3. 可以通过配置 `.git/config` 文件，通过添加URl来配置远程仓库



## *Garry 的 git 连接 gitee 小记*

1. 选择一个本地的文件夹，使用`git init`初始化

2. 创建全局的姓名和邮箱命名，用于分辨是谁提交

	```bash
	git config --global user.name "Garry"
	git config --global user.email "1011250192@qq.com"
	```

3. 添加公钥

	git bash中输入：

	```bash
	ssh-keygen -t rsa
	```

	创建本地公钥，获取公钥：

	```bash
	cat ~/.ssh/id_rsa.pub
	```

	把获取的公钥复制到gitee个人界面的公钥管理中

	检验是否连接成功

	```bash
	ssh -T git@gitee.com
	```

4. 可以先创建好仓库，然后复制远程仓库到本地

4. gitee中创建仓库，复制仓库的ssh地址

	将本地仓库连接到远程仓库

	```bash
	git remote add origin git@giteexxxxx/xxxxx.git
	```

	查看当前连接的远程仓库

	```bash
	git remote -v
	```

5. 本地文件修改上传：`git status`：查看当前文件夹修改的状态

	1. `git add <文件名> or <文件路径>`：将新增或者删减文件上传到本地暂存库
	2. `git commit -m "备注信息"`：改动内容上传到本地库

6. 将本地仓库拉取到GitHub：`git push -u <远程仓库名称> master`：拉取本地库 



# Missing Semester已完结，请开始你的JavaBasic学习吧