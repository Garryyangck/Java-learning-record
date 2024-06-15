# ***2024.2.1打卡	Day 1***

1. Java基础学完韩顺平Java 2.运行第一个java程序

	- 认识JVM
	- 区分JDK和JRE
	- 认识javac编译器
	- 环境变量path，配置%JAVA_HOME%\bin
	- Java文档注释
	- Java规范，如命名、格式、注释规范

2. 刷题方面采用 *拿捏算法面试系列教程* 顺序开刷，今天刷4题

	- 704 二分查找，就是简单套模板，注意取mid时左右偏移（至少我是这么叫的）

		```java
		int mid = left + (right - left) / 2;	//左偏移，left = mid + 1
		
		if (nums[mid] < target) {
		    left = mid + 1;//
		    continue;
		}
		if (nums[mid] > target) {
		    right = mid;
		    continue;
		}
		```

	- 69 x的平方根，也是二分查找，但是注意：

		```java
		long long mid = (long long)(left) + (long long)(right - left + 1) / 2;
		```

		`right - left + 1`可能超INT_MAX，因此都用long long

		本题除了二分查找还可用牛顿迭代法计算f(x) = $x^{\frac{1}{2}}$，数值分析没白学

	- 27 移除元素，双指针，因为题目说了不用管后面的部分，那么后面的部分就放值为val元素

		```java
		int left = 0, right = num - 1;
		while (left < right) {
		    if (nums[left] == val) {
		        while (left < right && nums[left] == val) {//防止nums[right-1]==val
		            swap(nums[left], nums[right]); 
		            right--; 
		        }
		    }//nums[left] != val
		    left++;
		}
		```
		
	- 11 盛最多水的容器，双指针。之前做过一个接雨水，这个和它思路差不多，就不多赘述了
	
		```java
		while (left < right) {
		    if (height[left] <= height[right]) {
		        ans = max(ans, (right - left) * height[left]);
		        left++;
		    }
		    if (height[left] > height[right]) {
		        ans = max(ans, (right - left) * height[right]);
		        right--;
		    }
		}
		```

#PS：学习的第一天，感觉收获满满，java笔记已同步至gitee:https://gitee.com/Garry-Yang/java-learning，刷题笔记就没必要了，leetcode比我整理得好。。。



# ***2024.2.2打卡	Day 2***

1. Java基础完成：

	- 3.变量入门
	- 4.几种常见数据类型
	- 5.数据类型转换
	- 7.运算符
	- 8.标识符，保留字，键盘输入

2. leetcode刷题：完成5题

	- 26.删除有序数组中的重复项，双指针

		```java
		while (right < num) {
		    if (nums[right - 1] != nums[right]) {
		    	nums[++left] = nums[right];//此处需用赋值运算符，不能交换，会对后续的判断会有影响
		    }
		    right++;
		}
		```

	- 209.长度最小的子数组，双指针

		```java
		int left = 0, right = 0;
		while (left < num && right < num) {
		    if (sum < target && ++right < num) {//注意right不要出界
		    	sum += nums[right];
		    }
		    if (sum >= target) {
		        ans = min(ans, right - left + 1);
		        sum -= nums[left++];
		    }
		}
		```

	- 203.删除链表结点

		- 法一：pre结点法

		```java
		ListNode* pre = new ListNode(0, head);
		ListNode* ans = pre;
		while (pre->next) {
		    if (pre->next->val == val) {
		        pre->next = pre->next->next;
		        continue;
		    }
		    pre = pre->next;
		}
		return ans->next;
		```

		- 法二：递归

		```java
		ListNode* removeElements(ListNode* head, int val) {
		    if (head == nullptr)   return nullptr;
		    if (head->val == val) {
		        return removeElements(head->next, val);
		    }
		    head->next = removeElements(head->next, val);
		    return head;
		}
		```

	- 876.链表的中间结点，快慢指针法

		- 偶数时为第二个

			```java
			ListNode* fast = head, * slow = head;
			while (fast->next) {
			    slow = slow->next;
			    fast = fast->next;
			    if (fast->next) {
			        fast = fast->next;
			    }
			}
			return slow;
			```

		- 偶数时为第一个

			```java
			while (fast->next->next) {
			    slow = slow->next;
			    fast = fast->next->next;
			}
			```

	- 19.删除链表倒数第n个结点，快慢指针，快指针快n个结点

		```java
		ListNode* removeNthFromEnd(ListNode* head, int n) {
		    ListNode* ans = new ListNode(0, head);
		    ListNode* fast = ans, * slow = ans;//初始化为ans
		    for (int i = 0; i < n; i++) {//快指针快n个结点
		        fast = fast->next;
		    }
		    while (fast->next) {
		        fast = fast->next;
		        slow = slow->next;
		    }
		    slow->next = slow->next->next;
		    return ans->next;
		}
		```




# ***2024.2.3打卡	Day 3***

1. Java基础完成：

	  - 9.进制转换与位运算

	  - 12.switch详解

	  - 16.break详解

	  - 17.continue 和 return

	  - 19.数组详解

	  - 21.二维数组详解

	  - 23.类与对象快速入门

	  - 24.方法详解


2. Java语法刷题完成18题：

	- JAVA1，类型转换，强制类型转换

	- JAVA2，简单运算，三元运算符

	- JAVA3，四舍五入，强制类型转换

	- JAVA4，交换变量值，没做出来，答案很巧妙

		```java
		a = (a + b) - (b = a);
		```

	- JAVA5，计算商场折扣，简单分支语句

	- JAVA6，判断体重指数，简单分支语句

	- JAVA7，判断学生成绩等级，switch

	- JAVA8，邮箱验证，没做出来，不了解String容器

		```java
		//利用matches()方法进行正则匹配，如果合法，则输出"邮箱格式合法"，否则输出"邮箱格式不合法"
		System.out.println(str.matches(emailMatcher)?"邮箱格式合法":"邮箱格式不合法");
		```

	- JAVA9，数列求和，注意用long防溢出

	- JAVA10，统计输入正数个数，while

	- JAVA11，求最大公倍数，辗转相除法

	- JAVA12，小球走过路程计算，注意float sum

	- JAVA13，求平均数，while

	- JAVA14，判断质数，遍历

	- JAVA15，计算整数位数，while

	- JAVA16，数组遍历

	- JAVA17，数组反转，双指针

	- JAVA18，二维数组求和，遍历

3. leetcode刷题：2题

	- 160.相交链表：双指针，一个指针指到nullptr时切换到另一链表的head，最终两指针相许处即为结果

		```java
		while (iter1 != iter2) {
		    iter1 = iter1 == nullptr ? headB : iter1->next;
		    iter2 = iter2 == nullptr ? headA : iter2->next;
		}
		```

	- 206.反转链表

		- 法一，pre结点法，递推思想，一个一个将结点指向从后一个改为前一个

			```java
			while (post) {
			    ListNode* temp = post->next;
			    post->next = pre;
			    pre = post;
			    post = temp;
			}
			```

		- 法二，递归，先将head后的反转，然后head->next->next = head，最后head->next（末尾）

			```java
			ListNode* reverseList(ListNode* head) {
			    if (!head || !head->next)  return head;
			    ListNode* newHead = reverseList(head->next);
			    head->next->next = head;
			    head->next = nullptr;
			    return newHead;
			}
			```




# ***2024.2.4打卡	Day 4***

1. Java基础完成：
	- 25.克隆与递归
	- 26.几个典型递归案例
	- 27.重载详解
	- 28.可变参数与作用域
	- 29.构造器与对象创建详解
	- 30.this详解
	- 32.IDEA入门

2. leetcode刷题：3题

	- 92.链表反转II

		- 法一，递推，拼接

			```java
			ListNode* pre = new ListNode(0, head);
			ListNode* ans = pre;
			ListNode* post = nNode(head, right)->next;
			for (int i = 1; i < left; i++) {
			    pre = pre->next;
			}
			pre->next = reserve(pre->next, right - left + 1);
			lastNode(pre)->next = post;
			return ans->next;
			```

		- 法二：递归，copy帅地的解法

			```java
			class Solution {
			private:
			    ListNode* last = nullptr;//帮助寻找反转后的结尾
			
			public:
			    ListNode* reverseBetween(ListNode* head, int left, int right) {
			        if (left == 1) {//将问题转换为前n个反转
			            return reverseThN(head, right);
			        }
			        head->next = reverseBetween(head->next, left - 1, right - 1);
			        return head;
			    }
			
			    ListNode* reverseThN(ListNode* head, int n) {
			        if (n == 1) {
			            last = head->next;
			            return head;
			        }
			
			        ListNode* newHead = reverseThN(head->next, n - 1);
			        head->next->next = head;
			        head->next = last;
			        return newHead;
			    }
			};
			```

	- 25.K个一组反转链表，前n个结点反序的递归调用版

		```java
		ListNode* reverseKGroup(ListNode* head, int k) {
		    if (!lastThNNode(head, k)) {//没有k个结点，返回head
		        return head;
		    }
		    ListNode* newHead = reverseThN(head, k);
		    head->next = reverseKGroup(head->next, k);
		    return newHead;
		}
		```

	- 141.环形链表

		Floyd判圈算法：`定义两个指针，一快一慢。慢指针每次只移动一步，而快指针每次移动两步。初始时，慢指针在位置 head，而快指针在位置 head.next。这样一来，如果在移动的过程中，快指针反过来追上慢指针，就说明该链表为环形链表` 

		```java
		bool hasCycle(ListNode* head) {
		    if (!head)   return false;
		    ListNode* slow = head;
		    ListNode* fast = head->next;
		    while (fast && fast->next) {
		        slow = slow->next;
		        fast = fast->next->next;
		        if (fast == slow) {
		            return true;
		        }
		    }
		    return false;
		}
		```




# ***2024.2.5打卡	Day 5***

1. Java基础完成：

	- 33.包与访问修饰符
	- 34.封装和继承详解
	- 35.Super详解
	- 36.方法重写
	- 37.多态详解

2. leetcode刷题：3题

	- 142.环形链表II，快慢指针，相遇时head有引入一个指针，最终和slow在目标节点处相遇

		> $a+(n+1)b+nc=2(a+b)⟹a=c+(n−1)(b+c)$
		> 有了 $a=c+(n−1)(b+c)a=c+(n-1)(b+c)，a=c+(n−1)(b+c)$ 的等量关系，我们会发现：从相遇点到入环点的距离加上 n−1 圈的环长，恰好等于从链表头部到入环点的距离。
		>
		> 因此，当发现 slow 与 fast相遇时，我们再额外使用一个指针 ptr。起始，它指向链表头部；随后，它和 slow 每次向后移动一个位置。最终，它们会在入环点相遇。

		```java
		while (fast && fast->next) {
		    slow = slow->next;
		    fast = fast->next->next;
		    if (fast == slow) {
		        ListNode* pre = head;
		        while (slow != pre) {
		            slow = slow->next;
		            pre = pre->next;
		        }
		        return slow;
		    }
		}
		```

	- 21.合并两个有序链表

		- 法一，递推，类似归并，不细说了

		- 法二，递归

			```java
			if (list1->val <= list2->val) {
			    list1->next = mergeTwoLists(list1->next, list2);
			    return list1;
			}
			list2->next = mergeTwoLists(list1, list2->next);
			return list2;
			```

	- 61.旋转链表，转化成向右移动一位的递归，注意特殊情况！

		```java
		class Solution {
		    ListNode* rotateOne(ListNode* head) {
		        if (!head || !head->next)    return head;
		        ListNode* pre = head;
		        while (pre->next->next) {
		            pre = pre->next;
		        }
		        ListNode* res = pre->next;
		        pre->next = nullptr;
		        res->next = head;
		        return res;
		    }
		    ListNode* Helper(ListNode* head, int k) {
		        if (k == 0) {
		            return head;
		        }
		        if (k == 1) {
		            return rotateOne(head);
		        }
		        return Helper(rotateOne(head), k - 1);
		    }
		public:
		    ListNode* rotateRight(ListNode* head, int k) {
		        if (!head || !head->next)   return head;
		        if (k == 0) {
		            return head;
		        }
		        int n = 0;
		        ListNode* iter = head;
		        while (iter) { iter = iter->next; n++; }
		        k %= n;
		        return Helper(head, k);
		    }
		};
		```



# ***2024.2.6打卡	Day 6***

1. Java基础完成：

	- 38.= = 和 equals

	- 39.hashCode | toString | finalize

	- 40.断点测试

		Java基础完结

2. Java进阶完成：

	- 1.类变量与类方法详解
	- 2.main方法
	- 3.代码块

3. Java语法刷题：完成6题

	- JAVA19，修改Data类的定义，改访问修饰符
	- JAVA20，检验年龄
	- JAVA21，补全构造方法，super
	- JAVA22，重写计算逻辑，super
	- JAVA23，定义打印方式，重写toString，拼接和打印中默认调用引用类型的toString
	- JAVA24，类型判断，instanceof判断运行类型，object.getClass().getSimpleName()

4. leetcode刷题：完成3题

	- 148.排序链表，一般排序法超时，采用归并算法

		1. 找链表中点
		2. 两个有序链表合并
		3. 递归调用

		```java
		class Solution {
		    ListNode* findMiddle(ListNode* head) {//找链表中点
		        ListNode* slow = head, * fast = head;
		        while (fast && fast->next && fast->next->next) {
		            slow = slow->next;
		            fast = fast->next->next;
		        }
		        return slow;
		    }
		    ListNode* merge(ListNode* head1, ListNode* head2) {//两个有序链表合并
		        if (!head1)  return head2;
		        if (!head2)  return head1;
		        if (head1->val <= head2->val) {
		            head1->next = merge(head1->next, head2);
		            return head1;
		        }
		        head2->next = merge(head1, head2->next);
		        return head2;
		    }
		public:
		    ListNode* sortList(ListNode* head) {//递归调用
		        if (!head || !head->next)  return head;
		        ListNode* mid = findMiddle(head);
		        ListNode* midNext = mid->next;
		        mid->next = nullptr;
		        ListNode* head1 = sortList(head);
		        ListNode* head2 = sortList(midNext);
		        return merge(head1, head2);
		    }
		};
		```

	- 382.链表随机结点，rand()函数

	- 138.随机链表的复制，没做出来

		1. 复制各节点，并构建拼接链表
		2. 构建各新节点的 random 指向
		3. 拆分两链表

		```java
		class Solution {
		public:
		    Node* copyRandomList(Node* head) {
		        if (head == nullptr) return nullptr;
		        Node* cur = head;
		        // 1. 复制各节点，并构建拼接链表
		        while (cur != nullptr) {
		            Node* tmp = new Node(cur->val);
		            tmp->next = cur->next;
		            cur->next = tmp;
		            cur = tmp->next;
		        }
		        // 2. 构建各新节点的 random 指向
		        cur = head;
		        while (cur != nullptr) {
		            if (cur->random != nullptr)
		                cur->next->random = cur->random->next;
		            cur = cur->next->next;
		        }
		        // 3. 拆分两链表
		        cur = head->next;
		        Node* pre = head, * res = head->next;
		        while (cur->next != nullptr) {
		            pre->next = pre->next->next;
		            cur->next = cur->next->next;
		            pre = pre->next;
		            cur = cur->next;
		        }
		        pre->next = nullptr; // 单独处理原链表尾节点
		        return res;      // 返回新链表头节点
		    }
		};
		```

		

# ***2024.2.7打卡	Day 7***

1. Java进阶完成：

	- 4.单例模式 与 final
	- 5.抽象类
	- 6.接口详解
	- 7.四种内部类详解

2. Java语法刷题：完成4题

	- JAVA25，实现抽象方法
	- JAVA26，实现接口
	- JAVA27，重写父类方法
	- JAVA28，创建单例对象

3. leetcode刷题：完成4题

	- 146.LRU缓存，没做出来，没想到双向链表，直接超时了

		***双向链表可以原地删除结点是实现 get和 put O(1)的关键***

		1. get时删除原链表中的结点，并将一个拷贝版添加到表头，释放内存

		2. put如果有，则删了再添到表头，改哈希表，释放内存

			如果没有找到就表头加，如果size=capicity就删表尾，删哈希表，释放内存

		```java
		void removeNode(DLinkedNode* node) {//双向链表可以原地删结点
		    node->prev->next = node->next;
		    node->next->prev = node->prev;
		}
		```

	- 232.用栈实现队列，就是把两个栈从栈底拼接到一起，数据先存入inStack，

		getTop时若outStack为空，则把inStack中所有元素移到outStack

	- 225.用队列实现栈，尝试把队列反过来，push时全出队，加入x，再把出队的拿回来
	
	- 20.有效的括号，做了很多遍了…



# ***2024.2.8打卡	Day 8***

1. Java进阶完成：

	- 8.枚举 Enum
	- 9.注解
	- 11.异常
	- 12.try | catch | throws

2. leetcode刷题：完成4题

	- 150.逆波兰表达式求值，易，远不如中序转后序再求值

		这里说一下中序转后序的思路：

		1. 数字直接入栈
		2. 符号有优先级，乘除 > 加减
		3. 遇到运算符，则一直出栈，直到栈空 或 栈顶的优先级小于它本身
		4. 遇到左括号直接入栈
		5. 遇到右括号一直出栈，直到把第一个左括号出栈

	- 155.最小栈，两个栈，一个是本栈，一个是最小栈，最小栈的栈顶为插入某元素后栈的最小值

		```java
		void push(int val) {
		    s.push(val);
		    if (minS.top() > val) {
		        minS.push(val);
		    }
		    else {
		        minS.push(minS.top());
		    }
		}
		void pop() {
		    s.pop();
		    minS.pop();
		}
		```

	- 215.数组中的第K个最大元素，优先队列创建小根堆

		```java
		std::priority_queue<int, std::vector<int>, std::greater<int>> min_heap;
		```

	- 347.前K个高频元素，优先队列大根堆，先从小到大排序，然后按照pair<int cnt, int num>入队，最后返回队里前k个数



# ***2024.2.9打卡	Day 9***

Java进阶完成：

- 14.八大常用类
- 15.Integer类：拆箱与装箱
- 16.String 详解



# ***2024.2.10打卡	Day 10***

1. Java进阶完成：

	- 17.StringBuffer 与 StringBuilder
	- 18.其它（至日期）

2. leetcode刷题：2题

	- 295.数据流的中位数

		1. 二分查找插入维护有序数组，我一开始的方法，但是速度719ms，直接垫底了…

		2. 创建两个优先队列，一个大根堆，一个小根堆，如果num<=大根堆顶就入大根堆

			当某一个堆的size大于另一个时，就把其顶部元素移至另一个堆

			```java
			void addNum(int num) {
			    if (queMin.empty() || num <= queMin.top()) {
			        queMin.push(num);
			        if (queMax.size() + 1 < queMin.size()) {
			            queMax.push(queMin.top());
			            queMin.pop();
			        }
			    }
			    else {
			        queMax.push(num);
			        if (queMax.size() > queMin.size()) {
			            queMin.push(queMax.top());
			            queMax.pop();
			        }
			    }
			}
			```

	- 1475.商品折扣后的最终价格，先将数组“反向”，然后单调栈

		```java
		 for (int i = n - 1; i >= 0; i--) {
		     while (!st.empty() && st.top() >= prices[i]) {//把比它大的都出栈，因为后序也不可能选到它们了
		         st.pop();
		     }
		     ans[i] = st.empty() ? prices[i] : prices[i] - st.top();
		     st.push(prices[i]);
		 }
		```




# ***2024.2.11打卡	Day 11***

1. Java进阶完成：

	- 18.其它（日期至剩余的）
	- 20.集合入门

2. 计算机组成原理开了个头

3. leetcode刷题：3题

	- 239.滑动窗口最大值，自己的方法太慢了，双向队列保存当前最大的下标，如果nums[i]>=nums[q.back]，就队尾出队；如果队头==i-k，就队头出队

		```java
		deque<int> q;
		for (int i = 0; i < k; i++) {
		    while (!q.empty() && nums[i] >= nums[q.back()]) {
		        q.pop_back();
		    }
		    q.push_back(i);//存的是下标
		}
		vector<int> ans;
		ans.push_back(nums[q.front()]);
		for (int i = k; i < size; i++) {
		    while (!q.empty() && nums[i] >= nums[q.back()]) {
		        q.pop_back();
		    }
		    q.push_back(i);//存的是下标
		    if (q.front() == i - k) {
		        q.pop_front();
		    }
		    ans.push_back(nums[q.front()]);
		}
		return ans;
		```

	- 144.二叉树的前序遍历，递归，递推需要借助栈，入栈根节点，然后不断取出栈顶，先入栈顶的右，再入栈顶的左

		```java
		while(!st.empty()){
			auto Temp = st.top();
			st.pop();
			push_back(Temp->val);
			if(Temp->right)	push(right);
			if(Temp->left)	push(left);
		}
		```

	- 94.二叉树的中序遍历

		```java
		class Solution {
		    vector<int> ans;
		public:
		    vector<int> inorderTraversal(TreeNode* root) {
		        if (root == nullptr)return {};
		        stack<TreeNode*> st; 
		        st.push(root);
		        root = root->left;
		        while (!st.empty() || root != nullptr) {
		            if (root != nullptr) {
		                st.push(root);
		                root = root->left;
		            }
		            else {
		                root = st.top();
		                st.pop();
		                ans.push_back(root->val);
		                root = root->right;
		            }
		        }
		        return ans;
		    }
		};
		```




# ***2024.2.12打卡	Day 12***

1. Java进阶完成：

	- 21.List | ArrayList | Vector
	- 22.HashSet 与 LinkedHashSet（至HashSet）

2. 计算机组成原理：至计算机的体系与结构

3. Java基础语法完成：8题

	- JAVA29.动态字符串，insert方法

	- JAVA30.判断字符串中字母出现次数，charAt方法

	- JAVA31.十进制转二进制数，***Integer.toBinaryString(num)，或 toString(num, 2)***

	- JAVA33.掷色子游戏，Random.nextInt方法

	- JAVA34.数学方法，Math类

	- JAVA35.输出某一年各个月份天数，***Calendar.set(year,month,day)*** 方法

	- JAVA36.日期转换，***Date.getTime方法***

		```java
		sdf.format(date.getTime() - (long)12* 60 * 60 * 1000)
		```

	- JAVA37.判断学生成绩，throw自定义异常

4. leetcode刷题：5题

	- 145.二叉树的后序遍历，递推版

		```java
		while (!st.empty() || root) {//循环条件要加 || root，否则进不了循环
		    while (root) {
		        st.push(root);
		        if (root->left) {//左子树为空，直接找右子树
		            root = root->left;
		        }
		        else {
		            root = root->right;
		        }
		    }
		    root = st.top();
		    st.pop();
		    ans.push_back(root->val);
		    if (!st.empty() && st.top()->right != root) {//当前的root不是右子树，则找右子树
		        root = st.top()->right;
		    }
		    else {
		        root = nullptr;//使得下次循环不进入while(root)，继续出栈
		    }
		}
		```

	- 102.二叉树的层次遍历，就是借助queue广搜

	- 107.二叉树的层次遍历II，上一题反转

	- 104.二叉树最大深度，递归

	- 110.平衡二叉树，求最大深度 + 左右子树递归

		```java
		class Solution {
		    int treeHeight(TreeNode* root) {
		        if (!root)   return 0;
		        return max(treeHeight(root->left), treeHeight(root->right)) + 1;
		    }
		public:
		    bool isBalanced(TreeNode* root) {
		        if (!root)   return true;
		        int BF = abs(treeHeight(root->left) - treeHeight(root->right));
		        if (BF > 1)    return false;
		        return isBalanced(root->left) && isBalanced(root->right);//要左右子树递归！
		    }
		};
		```




# ***2024.2.13打卡	Day 13***

1. Java进阶完成：

	- 22.HashSet 与 LinkedHashSet
	- 23.HashMap 与 HashTable（完成Set的6种遍历方式）

2. Java基础语法完成：3题

	- JAVA38.字符串去重
	- JAVA39.集合遍历
	- JAVA39.集合排序，***java.util.Collections.sort(Collection clt)***

3. leetcode刷题：4题

	- LCR 144.二叉树的镜像，递归

	- 101.对称二叉树，***没做出来***，构造新方法检查左右子树是否对称，递归调用

		```java
		class Solution {
		public:
		    bool isSymmetric(TreeNode* root) {
		        if (!root || (!root->left && !root->right)){
		            return true;
		        }
		
		        return f(root->left, root->right);
		    }
		
		    // 判断 A 和 B 是否互为镜像二叉树
		    bool f(TreeNode* A, TreeNode* B){
		        if (!A && !B){
		            return true;
		        }
		        // 一个为 nullptr 一个不为 nullptr
		        if (!A || !B){
		            return false;
		        }
		
		        if (A->val != B->val){
		            return false;
		        }
		
		        return f(A->left, B->right) && f(A->right, B->left);
		    }
		};
		```

	- 199.二叉树的右视图，层次遍历，输出每层最后一个…

	- 662.二叉树最大宽度，没做出来，自己的方法是将nullptr也入队，最后超内存了

		方法，运用完全二叉树的思想给每个结点标序号，leftPos=rootPos*2

		```java
		class Solution {
		public:
		    int widthOfBinaryTree(TreeNode* root) {
		        if (root == nullptr) {
		            return 0;
		        }
		
		        queue<pair<TreeNode*, unsigned long long>> q;
		        int max_width = 0;
		
		        q.push(make_pair(root, 1));
		        while (!q.empty()) {
		            int size = q.size();
		            vector<pair<TreeNode*, unsigned long long>> level_nodes;
		            for (int i = 0; i < size; i++) {
		                auto node = q.front().first;
		                auto pos = q.front().second;
		                q.pop();
		                level_nodes.push_back(make_pair(node, pos));
		                if (node->left) {
		                    q.push(make_pair(node->left, pos * 2));//完全二叉树的思想
		                }
		                if (node->right) {
		                    q.push(make_pair(node->right, pos * 2 + 1));//完全二叉树的思想
		                }
		            }
		            unsigned long long level_width = level_nodes.back().second - level_nodes.front().second + 1;
		            max_width = max(max_width, (int)level_width);
		        }
		
		        return max_width;
		    }
		};
		```




# ***2024.2.14打卡	Day 14***

1. Java进阶完成：

	- 23.HashMap 与 Hashtable（剩余部分）
	- 24.Proporties
	- 25.集合选型规则
	- 26.TreeSet 与 TreeMap
	- 27.Collections 工具集

2. Java基础语法完成：4题

	- JAVA40.排队系统，双向队列头插

	  - JAVA41.首位交替出队，头尾出队

	  - JAVA42.统计一句话中重复单词数量，可以用put方法实现更改key对应的value值

	  - JAVA43.map简单应用

		JAVA语法刷题完结

3. 计算机组成原理：完成第一章：概述篇
3. leetcode刷题：5题

	- 105.从前序与中序遍历序列构造二叉树，分成左右子树，递归
	- 106.从中序与后序遍历序列构造二叉树，分成左右子树，递归
	- 230.二叉搜索树中第k小的元素，中序遍历
	- 112.路径总和，回溯
	- 113.路径总和II，回溯



# ***2024.2.15打卡	Day 15***

1. Java进阶完成：

	- 29.泛型详解
	- 31.多线程详解（至为什么调start方法）

2. leetcode刷题：4题

	- 235.二叉搜索树的最近公共祖先，递归，如果两个都比root小，调左子树，都大右子树，夹中间root

		```java
		class Solution {
		public:
		    TreeNode* lowestCommonAncestor(TreeNode* root, TreeNode* p, TreeNode* q) {
		        if (root == nullptr)   return nullptr;
		        if (p->val < root->val && q->val < root->val) {
		            return lowestCommonAncestor(root->left, p, q);
		        }
		        if (p->val > root->val && q->val > root->val) {
		            return lowestCommonAncestor(root->right, p, q);
		        }
		        return root;
		    }
		};
		```

	- 236.二叉树的最近公共祖先，递归，记录每个结点是否有p，q

		```java
		class Solution {
		    TreeNode* ans;
		    int flag = 0;
		    pair<bool, bool> Helper(TreeNode* root, TreeNode* p, TreeNode* q) {
		        if (root == nullptr)   return { false,false };
		        pair<bool, bool> left = Helper(root->left, p, q);
		        pair<bool, bool> right = Helper(root->right, p, q);
		        bool have_p = root->val == p->val || left.first || right.first;
		        bool have_q = root->val == q->val || left.second || right.second;
		        if (have_p && have_q) { if (!flag)ans = root; flag = 1; }//ans最终被赋为最深的结点
		        return { have_p,have_q };
		    }
		public:
		    TreeNode* lowestCommonAncestor(TreeNode* root, TreeNode* p, TreeNode* q) {
		        Helper(root, p, q);
		        return ans;
		    }
		};
		```

	- 136.只出现一次的数字，按位异或

	- LCR 133.二进制中1的个数，***n & (n-1) 消除最右边的 1***

		```java
		class Solution {
		    int cnt = 0;
		public:
		    int hammingWeight(uint32_t n) {
		        while (n) {
		            n = n & (n - 1);
		            cnt++；
		        }
		        return cnt;
		    }
		};
		```



# ***2024.2.16打卡	Day 16***

1. Java进阶完成：

	- 31.多线程详解（剩余部分）
	- 33.文件基本操作
	- 34.文件流（至文件拷贝）

2. 计算机组成原理：至组成篇，输入输出设备

3. leetcode刷题：4题

	- LCR 134.Pow(x,n)，***快速幂算法***，将num转为二进制数，如13 = 0b1101，x不断平方自身，遇到二进制num中的1，ans*=x

		```java
		class Solution {
		public:
		    double myPow(double x, int n) {
		        double ans = 1;
		        int num = abs(n);
		        while (num > 0) {
		            if (num & 1) {//等价于num % 2
		                ans *= x;
		            }
		            x *= x;
		            num >>= 1;
		        }
		        return n > 0 ? ans : 1 / ans;
		    }
		};
		```

	- 260.只出现一次的数字III，先全部异或，再通过两数的异或得到它们不同的一位，根据此位将nums分为两组，且两数必定不在同一组

		```java
		class Solution {
		public:
		    vector<int> singleNumber(vector<int>& nums) {
		        int res = 0;
		        for (auto num : nums)  res ^= num;//res的1是两个数不同的位置
		        //找到res最右边的位，即两数最右边的不同的位
		        int rightestDiff = 1;
		        while (!(rightestDiff & res)) rightestDiff <<= 1;
		        int ans1 = 0, ans2 = 0;
		        for (auto num : nums) {//通过rightestDiff将nums分为两组，且两数必定不在同一组
		            if (num & rightestDiff)    ans1 ^= num;
		            else    ans2 ^= num;
		        }
		        return { ans1,ans2 };
		    }
		};
		```

	- 137.只出现一次的数字II，数字最大2^31-1，因此直接确定所有数字每一二进制位的个数，然后%3不为0，说明目标数该二进制位为1

		```java
		class Solution {
		public:
		    int singleNumber(vector<int>& nums) {
		        int ans = 0;
		        for (int i = 0; i < 32; i++) {//一共32个二进制位
		            int cnt = 0;
		            for (auto num : nums) {
		                if(num & (1 << i))//统计该位的个数
		                    cnt++;
		            }
		            if(cnt % 3)
		            	ans |= 1 << i;//不为0，说明目标数该二进制位为1
		        }
		        return ans;
		    }
		};
		```

	- 172.阶乘后的0，5贡献一个0，25贡献两个0，125贡献三个0，以此类推。找出小于n的最大5的幂，然后遍历加起来

		```java
		class Solution {
		public:
		    int trailingZeroes(int n) {
		        if (n < 5) return 0;
		        int num = 5;
		        int cnt = 1;
		        while (num <= n) {
		            num *= 5;
		            cnt++;
		        }
		        int ans = 0;
		        num = 5;
		        for (int i = 1; i < cnt; i++) {//最大幂为cnt-1
		            ans += (n / num);
		            num *= 5;
		        }
		        return ans;
		    }
		};
		```




# ***2024.2.17打卡	Day 17***

1. Java进阶完成：

	- 34.文件流
	- 35.buffered流
	- 36.对象处理流
	- 37.标准输入输出流和打印流
	- 38.Properties
	- 43.网络编程入门（至Socket）

2. leetcode刷题：4题，被暴打的一天

	- 169.多数元素，没做出来，就是哪个找一半以上的元素，***摩尔投票法***

		```java
		public class Solution {
		    public int majorityElement(int[] nums) {
		        int len = nums.length;
		        if (len == 1) return nums[0];
		        int x = nums[0];
		        int cnt = 1;
		        for (int i = 1; i < len; i++) {
		            if (cnt == 0) {
		                x = nums[i];
		                cnt = 1;
		            } else {
		                if (x == nums[i]) {
		                    cnt++;
		                } else {//数不相同，就抵消掉
		                    cnt--;
		                }
		            }
		        }
		        return x;
		    }
		}
		```

	- LCR 186.文物朝代判断，不重复 + max - min < 5

	- 343.整数拆分，用递归不出所料超时了捏，方法：找规律

		发现最大***n=3时不切更大***，那么就***把所有大于 3的切成众多 3的小份***，

		mod=1就把一个3换成4，mod=2就多乘一个2

		如9为333，10为334，11为3332

		```java
		class Solution {
		    public int integerBreak(int n) {
		        if (n == 2) return 1;
		        if (n == 3) return 2;
		        int res = n / 3;
		        int mod = n % 3;
		        switch (mod) {
		            case 0:
		                return (int) Math.pow(3, res);
		            case 1:
		                return (int) Math.pow(3, res - 1) * 4;
		            case 2:
		                return (int) Math.pow(3, res) * 2;
		            default:
		                return -1;
		        }
		    }
		}
		```

	- LCR 132.砍竹子II，***注意数据 long也存不下，必须边乘边取模***

		```java
		long pow(int a, int n){
		    int p = 1000000007;
		    long res = 1;
		
		    for(int i = 1; i <= n; i++){
		        res = res * a % p;
		    }
		
		    return res;
		}
		```



# ***2024.2.18打卡	Day 18***

1. Java进阶完成：

	- 43.网络编程入门（剩余部分，明天开始做QQ通讯项目）

2. leetcode刷题：***已采用 java刷题！***完成3题

	- 455.分发饼干，将greed和sizes都排序，优先用小饼干满足小孩子

	- 376.摆动序列，就是找可以形成多少个波峰和波谷

		```java
		class Solution {
		    public int wiggleMaxLength(int[] nums) {
		        if (nums.length == 1) return nums.length;
		
		        int res = 1;
		        int pre = 0;// 保存前一个峰值是正还是负
		        int cur = 0;// 保存当前差值
		
		        for (int i = 0; i < nums.length - 1; i++) {
		            cur = nums[i + 1] - nums[i];
		            if (pre <= 0 && cur > 0 || pre >= 0 && cur < 0) {
		                res++;
		                pre = cur;
		            }
		        }
		        return res;
		    }
		}
		```

	- 55.跳跃游戏，就是不断更新到当前结点可以到达的最远处，最后看到没到nums.length-1

		```java
		class Solution {
		    public boolean canJump(int[] nums) {
		        int maxLen = 0;
		        for (int i = 0; i < nums.length && i <= maxLen; i++) {
		            maxLen = Math.max(maxLen, i + nums[i]);
		        }
		        return maxLen >= nums.length - 1;
		    }
		}
		```




# ***2024.2.19打卡	Day 19***

1. Java进阶完成：

	- 网络编程项目登录部分

2. leetcode刷题：完成1题

	- 45.跳跃游戏II，简单方法：维护当前的最远距离，若到达上一结点可到的最远处，res++

		```java
		public int jump(int[] nums) {
		
		    int end =0;
		    int res = 0;
		    int max = 0;    
		    for(int i =0; i < nums.length - 1; i++){
		        //if(max >= nums.length - 1) return res;
		        max = Math.max(max, i + nums[i]);
		        if(i == end){
		            end = max;
		            res ++;
		        }
		
		    }
		
		    return res;
		}
		```




# ***2024.2.20打卡	Day 20***

1. Java进阶完成：
	- 网络通讯项目，拉取在线用户，无异常退出，私聊，群聊，文件传输

明天要回学校了，今天早点睡，就先做这么多，回学校再抓紧吧



# ***2024.2.21打卡	Day 21***

1. Java进阶完成：

	- 网络通讯项目：注册，服务器消息推送，离线接收消息和文件，以及最后优化了一下相关方法

		网络通讯项目完结！

		***做项目过程中遇到的问题和自己的猜测：***

		1. 不能传完一个数据就socket.shutdownOutput，因为服务器和客户端需要持续保持通讯，一旦shutdown，另一端接收到终止信息就不会再接收数据了！
		2. 文件传输时将文件流转为字节数组时传输正常，转成String会有损坏，猜测是编码问题，文件默认gbk，但是编译器是utf-8，或许设置一下编码就可了，没去试
		3. 离线接收中，当用户登录时将所有其未接收的消息和文件传给客户端时，如果使用同一个对象输出流传2个及以上Message对象时客户端会报错。最后改为每传一个对象就创建一个新的对象输出流就可以解决问题。怀疑是因为客户端一次只解包一个对象，而服务器同一个流对象传多个对象会导致客户端接收过量数据，而无法将其解包成一个Message对象。
		4. 其他就没什么了，还有就是同一个功能写成方法，而不要不停复制粘贴

2. leetcode刷题：4题

	- 621.任务调度器，先找最多的同一个任务，算一个没有其他任务的时长，其他任务插在其中的间隔里，若间隔不够插，则耗时为任务总数，***注意可能存在多个最长的任务！***

		```java
		class Solution {
		    public int leastInterval(char[] tasks, int n) {
		        //找一个数量最多的任务
		        //尝试把其他任务插到其间隔里
		        //插不下就是任务总数，注意可能有多个最长的任务
		        int nums[] = new int[26];
		        Arrays.fill(nums, 0);
		        for (char task : tasks) {
		            nums[task - 'A']++;
		        }
		        Arrays.sort(nums);
		        int max = nums[25];
		        int ans = (max - 1) * (n + 1) + 1;
		        for (int i = 0; i < 26; i++) {//多个最长的任务的情况
		            if (nums[i] == max)
		                ans++;
		        }
		        ans--;//多加了一次
		        return Math.max(ans, tasks.length);
		    }
		}
		```

	- 435.无重叠区间，***本质是找最多放多少个区间不重复，和教室办最多活动的那题一模一样***

		```java
		class Solution {
		    public int eraseOverlapIntervals(int[][] intervals) {
		        //思路：放下最多区间使得不重叠
		        //选择：右边界小的区间
		        //类似于安排教室办活动那题
		        if (intervals.length == 1) return 0;
		        Arrays.sort(intervals, new Comparator<int[]>() {
		            @Override
		            public int compare(int[] o1, int[] o2) {//按数组有边界大小升序排序
		                return o1[o1.length - 1] - o2[o2.length - 1];
		            }
		        });
		        int end = -2147483648;//end初始化为一个最小的数
		        int cnt = 0;
		        for (int i = 0; i < intervals.length; i++) {
		            if (intervals[i][0] >= end) {
		                end = intervals[i][intervals[i].length - 1];
		                cnt++;
		            }
		        }
		        return intervals.length - cnt;
		    }
		}
		```

	- 135.分发糖果，初始全为1，两次遍历，从左到右，从右到左，若分比旁边高且糖果<=旁边的，则将糖果设为旁边人的+1，之所以遍历两次是因为要排除遍历方向带来的误差

		```java
		for (int i = 0; i < candy.length; i++) {
		    if (i == 0) {
		        if (ratings[i + 1] < ratings[i] && !(candy[i] > candy[i + 1])) {
		            candy[i] = candy[i + 1] + 1;
		        }
		        continue;
		    }
		    if (i == candy.length - 1) {
		        if (ratings[i - 1] < ratings[i] && !(candy[i] > candy[i - 1])) {
		            candy[i] = candy[i - 1] + 1;
		        }
		        continue;
		    }
		
		    if (ratings[i + 1] < ratings[i] && !(candy[i] > candy[i + 1])) {
		        candy[i] = candy[i + 1] + 1;
		    }
		    if (ratings[i - 1] < ratings[i] && !(candy[i] > candy[i - 1])) {
		        candy[i] = candy[i - 1] + 1;
		    }
		
		}
		//反向来一次
		for (int i = candy.length - 1; i >= 0; i--) {
		    if (i == 0) {
		        if (ratings[i + 1] < ratings[i] && !(candy[i] > candy[i + 1])) {
		            candy[i] = candy[i + 1] + 1;
		        }
		        continue;
		    }
		    if (i == candy.length - 1) {
		        if (ratings[i - 1] < ratings[i] && !(candy[i] > candy[i - 1])) {
		            candy[i] = candy[i - 1] + 1;
		        }
		        continue;
		    }
		
		    if (ratings[i + 1] < ratings[i] && !(candy[i] > candy[i + 1])) {
		        candy[i] = candy[i + 1] + 1;
		    }
		    if (ratings[i - 1] < ratings[i] && !(candy[i] > candy[i - 1])) {
		        candy[i] = candy[i - 1] + 1;
		    }
		
		}
		```

	- 77.组合，经典回溯算法

		***注意传入ans时需要传入深拷贝的副本，否则 ans中的元素会随着 now的变化而变化***

		```java
		public void bfs(int start) {//为避免重复，传入遍历开始的数
		    if (now.size() >= k) {
		        List<Integer> temp = new ArrayList<>(now);
		        ans.add(temp);
		        return;
		    }
		
		    for (int i = start; i <= n; i++) {
		        if (visit[i] == 0) {
		            visit[i] = 1;
		            now.add(i);
		            bfs(i);
		            visit[i] = 0;
		            now.remove(now.size() - 1);
		        }
		    }
		}
		```



# ***2024.2.22打卡	Day 22***

1. Java进阶完成

	- 45.反射

		Java进阶完结，明天开始 JavaWeb！

2. leetcode刷题：3题

	- 216.组合总和III，经典回溯算法，可以剪一下枝

	- 40.组合总和II，没做出来，主要是不知道怎么去重

		去重方法：

		1. 将数组排序，将相同的数放在一起

		2. 比如[1,1,5]，target=6，那么第一个1回溯回来时，判断第二个1==它的前一个，那么就continue，如此即可排除重复数的影响，当然i == start时不用跳过

			```java
			class Solution {
			    public int target;
			    public int[] candidates;
			    public int sum = 0;//当前的和
			    List<List<Integer>> ans = new ArrayList<>();
			    List<Integer> now = new ArrayList<>();
			
			    public List<List<Integer>> combinationSum2(int[] candidates, int target) {
			        if (candidates.length == 1) {
			            if (candidates[0] == target) {
			                ArrayList<Integer> integers = new ArrayList<>();
			                integers.add(candidates[0]);
			                ans.add(integers);
			            }
			            return ans;
			        }
			        Arrays.sort(candidates);//升序排序，便于后续处理重复的组合
			        this.candidates = candidates;
			        this.target = target;
			        bfs(0);
			        return ans;
			    }
			
			    public void bfs(int start) {//传入层数
			        if (sum == target) {
			            ans.add(new ArrayList<>(now));
			            return;
			        }
			
			        if (sum > target) return;
			
			        for (int i = start; i < candidates.length; i++) {
			            if (i > start && candidates[i] == candidates[i - 1])//消除重复的组合
			                continue;
			            now.add(candidates[i]);
			            sum += candidates[i];
			            bfs(i + 1);
			            now.remove(now.size() - 1);
			            sum -= candidates[i];
			        }
			    }
			}
			```

	- 39.组合总和，有了上面那道题消除重复数的经验后

		只需将遍历时bfs(i+1)改为bfs(i)即可同一个数重复使用



# ***2024.2.23打卡	Day 23***

1. JavaWeb：
	- 1.MySQL简介，完成至MySQL的安装（各种报错我吐了，好在安好了…）
2. leetcode刷题：2题
	- 78.子集，回溯，注意递归终止条件是当前集合大小大于nums大小，而add之后不要return，否则子集只有一个数！
	- 90.子集II，增加子集不能重复的条件，其实就和昨天的`40.组合总和II`一样的解决方法，先把nums排序，然后dfs的for种如果`i>start && nums[i]==nums[i-1]`就continue



# ***2024.2.24打卡	Day 24***

1. JavaWeb：
	- 第一章：MySQL基础操作
	- 第二章：MySQL数据库设计和多表操作
	- 第三章：JDBC（Druid完成，练习不做）
	
2. leetcode刷题：3题

	- 46.全排列，用visit记录index处的数有没有使用

	- 47.全排列II，经典要求去重

		分析一下重复的原因：在同一层选用一个之前用过的数，如{1,1,2,2}中第一层的1执行完后，第二个1又放到第一位，势必得到相同的结果

		因此解决方法是先把nums排序，相同的数放一块，然后在dfs中创建一个变量cnt，记录当前数在本层前有没有被使用过

		```java
		public void dfs() {
		    if (now.size() >= nums.length) {
		        ans.add(new ArrayList<>(now));
		        return;
		    }
		
		    boolean isUsed = false;//记录当前数在本层前有没有被使用过
		    for (int i = 0; i < nums.length; i++) {
		        if (i > 0 && nums[i] != nums[i - 1]) isUsed = false;
		        if (isUsed && nums[i] == nums[i - 1]) continue;
		        if (visit[i] == 0) {
		            visit[i] = 1;
		            now.add(nums[i]);
		            dfs();
		            visit[i] = 0;
		            now.remove(now.size() - 1);
		            isUsed = true;
		        }
		    }
		}
		```

	- 51.N皇后，虽说是困难，但好在之前做过，还是做出来了，只不过刚开始初始化map时采用

		```java
		int[] tmp = new int[n];
		Arrays.fill(tmp, 0);
		map = new int[n][];
		Arrays.fill(map, tmp);
		```

		的方法，致命问题是用到tmp只开了一片内存，因此map中的n个数组对象全部指向同一块地址！

		整体思路还是比较清晰的

		```java
		public void dfs(int row) {
		    if (row == n && num == n) {
		        List<String> elem = new ArrayList<>();
		        for (int pos : now) {
		            String str = "";
		            for (int i = 0; i < n; i++) {
		                if (i == pos) str += "Q";
		                else str += ".";
		            }
		            elem.add(str);
		        }
		        ans.add(elem);
		        return;
		    }
		
		    for (int col = 0; col < n; col++) {
		        if (map[row][col] == 0) {
		            putQueen(row, col);
		            num++;
		            now.add(col);
		            dfs(row + 1);
		            removeQueen(row, col);
		            num--;
		            now.remove(now.size() - 1);
		        }
		    }
		
		}
		```



# ***2024.2.25打卡	Day 25***

1. JavaWeb：
	- 第四章：Maven
	- 第五章：Mybatis（最后突然所有方法都报错Invalid bound statement .xml和mapper接口连接失败，不知道为什么，之前还能正常执行的）

2. leetcode刷题：2题

	- 70.爬楼梯，简单动态规划
	- 746.使用最小花费爬楼梯，简单动态规划



# ***2024.2.26打卡	Day 26***

1. JavaWeb：
	- 第六、七章，前端三件套（学校里讲过，快速过）
	- 第八章，HTTP&Tomcat&Servlet（还行，主要是使用，难度不大）

2. leetcode刷题：4题，看完背包问题

	- 198.打家劫舍，明确当前只能来自i-2或i-3，当然帅地的方法更好，直接记录遍历到第i个屋子时的最多金钱，`dp[i]=Math.max(dp[i-2]+nums[i], dp[i-1])`
	
	- 62.不同路径，简单动态规划
	
	- 64.最小路径和，简单动态规划
	
	- 63.不同路径II，简单动态规划
	
	- 0-1背包
	
		```java
		 for(int i = 1; i <= N; i++){
		     for(int j = 0; j <= V; j++){
		         if(v[i] > j){
		             dp[i][j] = dp[i-1][j];
		         }else{
		             dp[i][j] = Math.max(dp[i-1][j], dp[i-1][j-v[i]] + w[i]);
		         }
		     }
		 }
		```
	
	- 计组，组成篇P4存储器概述看完



# ***2024.2.27打卡	Day 27***

1. JavaWeb：
	- 第九章：Request&Response
	- 第十章：JSP&会话技术，看完了JSP脚本
	
2. leetcode刷题：3题
	- 5.最长回文子串
	
		```java
		//含义：从i到j的子字符串是否为回文串，为0不是，为1是
		int[][] dp = new int[s.length()][s.length()];
		```
	
		中间夹的是回文且start和end相同，则回文
	
	- 718.最长重复子数组，没做出来，用的暴力遍历，超时了
	
		```java
		int[][] dp = new int[nums1.length + 1][nums2.length + 1];
		//含义：nums1中以i结尾和nums2中以j结尾的公共子串的长度
		//注意是公共子串，所以一旦发现不想等就及时清零
		for (int i = 1; i < nums1.length + 1; i++) {
		    for (int j = 1; j < nums2.length + 1; j++) {
		        if (nums1[i - 1] == nums2[j - 1]) {
		            dp[i][j] = dp[i - 1][j - 1] + 1;
		            maxLen = Math.max(maxLen, dp[i][j]);
		        } else {
		            dp[i][j] = 0;//一旦发现不想等就及时清零
		        }
		    }
		}
		```
	
	- 300.最长递增子序列，又没做出来
	
		```java
		int[] dp = new int[n];
		//含义：以i结尾的子数组的最长递增序列
		 for(int i = 1; i < n; i++){
		     dp[i] = 1;
		     for(int j = i - 1; j >= 0; j--){//往前遍历
		         if(nums[j] < nums[i]){//当前的数比它的结尾大，则可扩大为dp[j]+1
		             dp[i] = Math.max(dp[i], dp[j] + 1);
		         }
		     }
		
		     max = Math.max(max, dp[i]);
		 }
		```



# ***2024.2.28打卡	Day 28***

1. JavaWeb：
	- 第十章：JSP&会话技术
	
		JavaWeb完结！明天开始ssm
	
2. leetcode刷题：1题
	- 72.编辑距离，没做出来，可以将增删改表示为dp数组的形式，找到其与当前的关系
	
		```java
		//含义：长度为i的word1到长度为j的words2需要的最少步骤
		int[][] dp = new int[word1.length() + 1][word2.length() + 1];
		for(int i = 1; i <= word1.length(); i++){
		    for(int j = 1; j <= word2.length(); j++){
		        //相同，则这一位不需要变换
		        if(word1.charAt(i-1) == word2.charAt(j - 1)){
		            dp[i][j] = dp[i-1][j-1];
		        }else{//否则从增，删，改中选一个最少的，改变该位使之贴近目标
		            dp[i][j] = Math.min(Math.min(dp[i][j-1], dp[i-1][j]),dp[i-1][j-1])+1;
		        }
		    }
		}
		```
	



# ***2024.2.29打卡	Day 29***

1. ssm：

	- Mybatis速过一遍

		Mybatis完结！

	- Spring已完成实例化对象

2. leetcode刷题：3题

	- 10.正则表达式匹配

		又没做出来，不得不说我动规是真的菜

		```java
		/*
		     * 1、dp[i][j]：表示字符串长度为 i 的字符s和长度为 j 字符p是否匹配。
		     * 
		     * s = "aaaaa"
		     * p = "abca*"
		     * 
		     * 2、求关系式 dp[i][j]
		     * 在匹配的时候，肯定需要关注 s[i] 和 p[j]
		     * (1)、如果 p[j] 是普通字符:s[i] == p[j]=>dp[i][j] = dp[i-1][j-1]
		     * s[i] != p[j]=>dp[i][j] = false.
		     * (2)、如果 p[j] = '.' =>dp[i][j] = dp[i-1][j-1]
		     * 
		     * (3)、如果 p[j] = '*'，那么我们需要关注p[j-1]
		     * a.如果 P[j-1] != s[i] => dp[i][j] = dp[i][j-2]，任意为0个
		     * b.如果 p[j-1] == s[i]：
		     * 1).匹配0个=》dp[i][j] = dp[i][j-2]，如s="a",p="..*"，则匹配0个
		     * 2).匹配1个=〉dp[i][j] = dp[i-1][j-2]
		     * 3).匹配多个,相当于抵消了s[i]=》dp[i][j] = dp[i-1][j]。
		     * 
		     * dp[i][j] = dp[i][j-2] or dp[i-1][j-2] or dp[i-1][j]
		     * 
		     * 3、求初始值
		     * 若p[j]='*' dp[0][j] = dp[0][j-2]。
		     * dp[0][0] = true.
		     * 
		     * 
		     * 
		     * return dp[n][m]
		     */
		
		dp[0][0] = true;
		for (int j = 2; j <= m; j++) {
		    if (p.charAt(j - 1) == '*') {
		        dp[0][j] = dp[0][j - 2];
		    }
		}
		
		for (int i = 1; i <= n; i++) {
		    for (int j = 1; j <= m; j++) {
		        // 如果不是 *
		        if (p.charAt(j - 1) != '*') {
		            if (p.charAt(j-1) == '.' || p.charAt(j-1) == s.charAt(i-1)) {
		                dp[i][j] = dp[i - 1][j - 1];
		            }
		        } else {
		            // 如果是 *
		            // 匹配0个
		            if (p.charAt(j-2) != s.charAt(i-1) && p.charAt(j-2) != '.') {
		                dp[i][j] = dp[i][j - 2];
		            } else {
		                dp[i][j] = dp[i][j - 2] || dp[i - 1][j - 2] || dp[i - 1][j];
		            }
		        }
		    }
		}
		
		```

	- 122.买卖股票的最佳时机II

		```java
		//这一类题的模板
		public int maxProfit(int[] prices) {
		    if (prices.length == 1) return 0;
		    //表示到第i天是有和没有股票时的钱，j=0没有，j=1有
		    int[][] dp = new int[prices.length][2];
		    dp[0][0] = 0;
		    dp[0][1] = -prices[0];
		    for (int i = 1; i < prices.length; i++) {
		        //没有股票:(1)昨天没有，今天没买(2)昨天有，今天卖了
		        dp[i][0] = Math.max(dp[i - 1][0], dp[i - 1][1] + prices[i]);
		        //有股票:(1)昨天有，今天没卖(2)昨天没有，今天买了
		        dp[i][1] = Math.max(dp[i - 1][1], dp[i - 1][0] - prices[i]);
		    }
		    return dp[prices.length - 1][0];
		}
		```

	- 714.买卖股票的最佳时机含手续费

		在卖的时候支付fee块钱就可以了



# ***2024.3.1打卡	Day 30***

1. 今天学校的傻逼课开始发力了，状态没有前几天好，得想个办法把它们水了。。。

1. ssm：

	- Spring已完成至Java Config

2. leetcode刷题：2题

	- 121.买卖股票的最佳时机

		我用单调栈做的，看标答可以维护max和min，找它们的最大差值，感觉动规做这题复杂了

		```java
		public class Solution {
		    public int maxProfit(int prices[]) {
		        int minprice = Integer.MAX_VALUE;
		        int maxprofit = 0;
		        for (int i = 0; i < prices.length; i++) {
		            if (prices[i] < minprice) {
		                minprice = prices[i];
		            } else if (prices[i] - minprice > maxprofit) {
		                maxprofit = prices[i] - minprice;
		            }
		        }
		        return maxprofit;
		    }
		}
		```

	- 123.买卖股票的最佳时机III

		没做出来，要注意本题必须要设置dp\[0][3] = -prices[0];因为可能第一天买了又卖，再买就是3的情况了

		```java
		class Solution {
		    public int maxProfit(int[] prices) {
		        if (prices.length == 1) return 0;
		
		        /**
		         * j=0, 没有任何操作
		         * j=1, 买入一次
		         * j=2, 完成一次交易
		         * j=3, 第二次购买
		         * j=4, 完成全部两次交易
		         */
		        int[][] dp = new int[prices.length][5];
		        dp[0][1] = -prices[0];
		        //注意：第一天可以买了卖，再买，那么dp[0][3]=-prices[0]
		        dp[0][3] = -prices[0];
		
		        for (int i = 1; i < prices.length; i++) {
		            dp[i][0] = dp[i - 1][0];
		            dp[i][1] = Math.max(dp[i - 1][1], dp[i - 1][0] - prices[i]);
		            dp[i][2] = Math.max(dp[i - 1][2], dp[i - 1][1] + prices[i]);
		            dp[i][3] = Math.max(dp[i - 1][3], dp[i - 1][2] - prices[i]);
		            dp[i][4] = Math.max(dp[i - 1][4], dp[i - 1][3] + prices[i]);
		        }
		
		        int ans = 0;
		        for (int i = 0; i < 5; i++) {
		            ans = Math.max(ans, dp[prices.length - 1][i]);
		        }
		        return ans;
		    }
		}
		```
		



# ***2024.3.2打卡	Day 31***

1. ssm：

	- Spring已完成至事务，明天结束Spring，开始SpringMVC

2. leetcode刷题：3题

	- 188.买卖股票的最佳时机IIII

		就是在III的基础上，dp\[length][2*k+1]，然后注意初始化就可以

		```java
		//初始化dp[0][j]
		for (int i = 1; i < 2 * k + 1; i += 2) {
		    dp[0][i] = -prices[0];
		}
		
		for (int i = 1; i < prices.length; i++) {
		    dp[i][0] = dp[i - 1][0];
		    for (int j = 1; j < 2 * k + 1; j++) {
		        if (j % 2 == 1)//持有股票的状态
		            dp[i][j] = Math.max(dp[i - 1][j], dp[i - 1][j - 1] - prices[i]);
		        else//没有股票的状态
		            dp[i][j] = Math.max(dp[i - 1][j], dp[i - 1][j - 1] + prices[i]);
		    }
		}
		```

	- 309.买卖股票的最佳时机含冷冻期

		卖的时候没限制，和I一样；买的时候昨天如果没股票说明还在冷冻期，因此只能从i-2天买

		```java
		int[][] dp = new int[prices.length][2];
		dp[0][1] = -prices[0];
		
		for (int i = 1; i < prices.length; i++) {
		    dp[i][0] = Math.max(dp[i - 1][0], dp[i - 1][1] + prices[i]);
		    if (i == 1) {//第一天为特殊情况，可以从第0天买
		        dp[i][1] = Math.max(dp[i - 1][1], dp[i - 1][0] - prices[i]);
		        continue;
		    }
		    dp[i][1] = Math.max(dp[i - 1][1], dp[i - 2][0] - prices[i]);
		}
		```

	- 322.零钱兑换

		就是先排序coins，先初始化小的，小的次数多，会被后面的覆盖

		把dp初始化为2147483647
		
		用零钱初始化dp
		
		最后遍历amount，dp[i - coins[j]] == 2147483647说明其不能表示，因此continue
		
		```java
		class Solution {
		    public int coinChange(int[] coins, int amount) {
		        if (amount == 0) return 0;
		
		        int[] dp = new int[amount + 1];
		        Arrays.sort(coins);//先初始化小的，小的次数多，会被后面的覆盖
		
		        for (int i = 0; i <= amount; i++) {
		            dp[i] = 2147483647;
		        }
		
		        //初始化
		        for (int i = 0; i < coins.length; i++) {
		            int cnt = 0;
		            for (int j = 0; j <= amount; j += coins[i]) {
		                dp[j] = cnt++;
		            }
		        }
		
		        dp[0] = 0;
		
		        for (int i = 1; i <= amount; i++) {
		            for (int j = 0; j < coins.length; j++) {
		                if (i - coins[j] < 0) continue;
		                if (dp[i - coins[j]] == 2147483647) continue;
		                dp[i] = Math.min(dp[i], dp[i - coins[j]] + 1);
		            }
		        }
		
		        return dp[amount] == 2147483647 ? -1 : dp[amount];
		    }
		}
		```



# ***2024.3.3打卡	Day 32***

1. ssm：

	- Spring完结！
	- SpringMVC已完成至中文乱码问题

2. leetcode刷题：3题

	- 160.相交链表

		就是双指针指向两链表头，同时向后走，走到null后就转到另一根链表的表头，直到相遇

		如果没有公共结点必定会在null处相遇

		```java
		while (p1 != p2) {
		    if (p1 == null) {
		        p1 = headB;
		        continue;
		    }
		    if (p2 == null) {
		        p2 = headA;
		        continue;
		    }
		    p1 = p1.next;
		    p2 = p2.next;
		}
		```

	- 206.反转链表

		递归，注意最后要设置head.next=null

		```java
		class Solution {
		    public ListNode reverseList(ListNode head) {
		        if (head == null || head.next == null) return head;
		        ListNode tail = head.next;
		        ListNode newHead = reverseList(head.next);
		        tail.next = head;
		        head.next = null;
		        return newHead;
		    }
		}
		```

	- 234.回文链表

		没做出来，自己写的递归复杂度$O(n^2)$，超时了

		正确解法：

		1. 找到偏左的中点
		2. 将链表右半边反转
		3. 比较反转后的右边和剩下的左边，直到其中一个指向null

		```java
		public boolean isPalindrome(ListNode head) {
		    if (head == null || head.next == null) return true;
		    ListNode mid = findMidNode(head);
		    ListNode left = head;
		    ListNode right = reverseList(mid.next);
		    mid.next = null;
		    while (left != null && right != null) {
		        if (left.val != right.val) return false;
		        left = left.next;
		        right = right.next;
		    }
		    return true;
		}
		```



# ***2024.3.4打卡	Day 33***

1. ssm：

	- SpringMVC第二和第三章涉及之前JavaWeb跳过的内容，因此只学完第一章SpringMVC框架后就开始SpringBoot的学习。故SpringMVC完结（暂时，项目如果要用到再回来补）

		ssm暂时完结！

2. SpringBoot:

	- 比ssm好搞多了，直接搞定

		SpringBoot完结！

3. leetcode刷题：5题

	- 2.两数相加

		递归，找到l1和l2后一结点的求和链表，然后用flag记录当前进位情况，注意最后如果flag为空，还要创建一个值为1的结点用于进位

		```java
		public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
		    if (l1 == null) return l2;
		    if (l2 == null) return l1;
		
		    int sum = l1.val + l2.val;
		    int flag = 0;//记录是否进位
		    if (sum >= 10) flag = 1;
		    ListNode ans = new ListNode(sum % 10);//头结点
		    ListNode postSumList = addTwoNumbers(l1.next, l2.next);//后面的两表之和
		    ListNode pre = new ListNode(-1);
		    pre.next = postSumList;//pre结点法
		    ListNode record = pre;//记录结点位置
		    while (flag != 0 || pre.next != null) {
		        if (pre.next == null) {//最后一位出现进位
		            ListNode newNode = new ListNode(1);
		            pre.next = newNode;
		            break;
		        }
		        int num = pre.next.val + flag;
		        flag = 0;
		        if (num >= 10) flag = 1;
		        pre.next.val = num % 10;
		        pre = pre.next;
		    }
		    ans.next = record.next;
		    return ans;
		}
		```

	- 19.删除链表的倒数第N个结点

		一次遍历的方法：pre结点法 + 快慢指针，快指针比慢指针快n个结点

	- 23.合并K个升序列表

		采用归并排序（分治思想）合并，注意mid=lists.length/2，得到的是偏右的中点

	- 82.删除排序链表中重复元素II

		递归，枚举思想，情况有点多。。。

		```java
		public ListNode deleteDuplicates(ListNode head) {
		    if (head == null || head.next == null) return head;
		    ListNode postList = deleteDuplicates(head.next);
		    if (postList == null) {
		        if (head.val == head.next.val) {//(1,1,1)
		            return null;
		        }
		        //(1,2,2)
		        head.next = null;
		        return head;
		    }
		    if (postList.val == head.val) {//消除postList的表头(1,1,2)
		        return postList.next;
		    }
		    if (head.val == head.next.val) {//消除head自身(1,1,1,2)
		        return postList;
		    }
		    //不消除(1,2)
		    head.next = postList;
		    return head;
		}
		```

	- 92.反转链表II

		递归，将left->right转化为反转前right-left+1个

		```java
		public ListNode reverseBetween(ListNode head, int left, int right) {
		    if (left == 1) {
		        return reverseFirstN(head, right);
		    }
		    ListNode postList = reverseBetween(head.next, left - 1, right - 1);
		    head.next = postList;
		    return head;
		}
		
		public ListNode last;//记录n个结点后不需要反转的链表
		
		public ListNode reverseFirstN(ListNode head, int n) {
		    if (n == 1) {
		        last = head.next;
		        return head;
		    }
		    ListNode postList = reverseFirstN(head.next, n - 1);
		    ListNode p = postList;
		    for (int i = 0; i < n - 2; i++) {
		        p = p.next;
		    }
		    p.next = head;
		    head.next = last;
		    return postList;
		}
		```

明天开始电商项目。。。



# ***2024.3.5打卡	Day 34***

1. 电商支付：

	- Mybatis三剑客和相关配置完成
	- 支付模块刚开始，概念太多了有点晕。。。

2. leetcode刷题：2题

	- 142.环形链表II

		感觉只能硬记结论，先快慢指针相遇，然后ans结点从head出发与slow相遇的点就是第一个成环结点

		```java
		ListNode fast = head, slow = head;
		do {
		    if (fast != null && fast.next != null) {
		        fast = fast.next.next;
		        slow = slow.next;
		    } else {
		        return null;
		    }
		} while (fast != slow);
		ListNode ans = head;
		while (slow != ans) {
		    slow = slow.next;
		    ans = ans.next;
		}
		```

	- 143.重排链表

		第二次没做出来

		1. 先找左中点

		2. 将右侧链表反向

		3. 合并两链表

			```java
			public void mergeList(ListNode l1, ListNode l2) {//l1必须>=l2长度
			    ListNode l1_tmp;
			    ListNode l2_tmp;
			    while (l1 != null && l2 != null) {
			        l1_tmp = l1.next;
			        l2_tmp = l2.next;
			
			        l1.next = l2;
			        l1 = l1_tmp;
			
			        l2.next = l1;
			        l2 = l2_tmp;
			    }
			}
			```



# ***2024.3.6打卡	Day 35***

1. 电商支付：

	- 支付模块异步通知完成，但是我还没配置自己的接收支付平台异步通知的地址，而且支付宝的概念太多给我搞晕了，因此决定缓几天，先去做电商相关模块。。。
	- 用户模块完成注册，但是自己没下载postman进行post请求验证，其实可以html创建一个表单进行post验证的（但是对json的了解还不是很深刻，如果json传不了，那就去补一下黑马的web）
	
2. leetcode刷题：2题
	- 148.排序链表
	
		归并排序，又用到了找中点和合并链表。。。
	
	- 25.K个一组翻转链表
	
		将问题转换为翻转前k个后，将前半段(preList)和后半段递归结果拼起来
	
		```java
		class Solution {
		    private Integer num;
		    private ListNode last;//翻转完前k个后，后面链表的头结点
		    private boolean flag = true;
		
		    public ListNode reverseKGroup(ListNode head, int k) {
		        if (head == null || head.next == null) return head;
		        if (flag) {
		            num = lengthOfList(head);
		            flag = false;
		        }
		        if (num >= k) {
		            ListNode preList = reverseFirstKNodes(head, k);//翻转前k个
		            num -= k;
		            ListNode postList = reverseKGroup(last, k);//拿到后面的
		            head.next = postList;//拼接起来
		            return preList;
		        } else {//不足k个直接返回
		            return head;
		        }
		    }
		
		    public Integer lengthOfList(ListNode head) {
		        if (head == null) return 0;
		        return lengthOfList(head.next) + 1;
		    }
		
		    public ListNode reverseFirstKNodes(ListNode head, int k) {
		        if (k == 1) {
		            last = head.next;//最后一个结点的后面是后面那段链表的头节点
		            return head;
		        }
		        ListNode tail = head.next;
		        ListNode postList = reverseFirstKNodes(head.next, k - 1);
		        tail.next = head;
		        head.next = null;
		        return postList;
		    }
		}
		```



# ***2024.3.7打卡	Day 36***

1. 电商支付：

	- 支付模块用natapp内网穿透后成功接收微信的异步通知，并在此之上实现验证金额等业务逻辑。页面跳转部分ajax虽然没学但用到时能看懂，还剩MQ要mall模块完成后实现，总之支付模块完成！
	- 用户模块完成登录

2. leetcode刷题：5题

	- 94.二叉树的中序遍历

		非递归：栈辅助，栈空且root为null时退出循环

	- 101.对称二叉树

		需要借助辅助方法，判断左右子树是否对称

		```java
		class Solution {
		    public boolean isSymmetric(TreeNode root) {
		        return helper(root.left, root.right);
		    }
		
		    public boolean helper(TreeNode left, TreeNode right) {//两颗树是否对称
		        if (left == null && right == null) return true;
		        if (left == null || right == null) return false;
		        return left.val == right.val && helper(left.left, right.right) && helper(left.right, right.left);
		    }
		}
		```

	- 104.二叉树的最大深度

		递归

	- 110.平衡二叉树

		注意所有子树都必须平衡

	- 144.二叉树的前序遍历

		栈辅助



# ***2024.3.8打卡	Day 37***

1. 电商支付：

	- 用户模块完成登出，单测，其中用session储存用户data，并了解了Cookie跨域问题，用户模块完成
	- 分类模块完成，自己写的查询递归里调用sql，速度慢，应该一次全部查出，然后据此进行处理
	- 商品模块完成商品列表上
	
2. leetcode刷题：4题

	- LCR 144.翻转二叉树

		递归

	- 102.二叉树的层次遍历

		ArrayDeque的方法roll，移除队头并返回移除的元素

	- 105.从前序和中序遍历序列构造二叉树

		找左右子树的前序中序，递归

		优化方法：可以创建一个helper方法，存放pre和in的开始结束值，这样避免大量创建子数组浪费时间和空间，还可通过map寻找rootVal在inorder中的下标，进一步提高效率

		```java
		map.put(inorder[i], i);//通过map寻找rootVal在inorder中的下标，进一步提高效率
		
		TreeNode f(int[] pre, int l1, int r1, int[] in, int l2, int r2) {
		    if (l1 > r1 || l2 > r2) {
		        return null;
		    }
		
		    TreeNode root = new TreeNode(pre[l1]);
		    int i = map.get(pre[l1]);
		    root.left = f(pre, l1 + 1, l1 + (i - l2), in, l2, i - 1);
		    root.right = f(pre, l1 + (i - l2) + 1, r1, in, i + 1, r2);
		
		    return root;
		}
		```

	- 129.求根节点到叶结点的数字之和

		类似于回溯，注意当结点是叶子结点时就要ans+=num并返回了，如果在结点为null的时候ans+=num，那么最终的结果是正确结果的两倍，因为叶子结点左边的null结点要加一次，右边的null结点又要加一次



# ***2024.3.9打卡	Day 38***

1. 电商支付：

	- 商品模块完成
	- 购物车模块，redis安装完成，添加商品功能已能正常写入Redis
	
2. leetcode刷题：3题

	- 145.二叉树的后序遍历

		非递归解法没做出来

		1. 应该在循环内再嵌套循环遍历，优先去左子树，左子树为空就去右子树

		2. 然后出内循环之后说明栈顶的左右子树都遍历过了，可以出栈并加入到ans

		3. 然后判断此时的peek是否是它出栈后，新的栈顶的左子树

		4. 如果是，则root去右子树；否则root为空

		==关键就是要在循环内再嵌套循环遍历，优先去左子树，左子树为空就去右子树==，出循环直接可以把栈顶拿出来加入到ans，这和非递归的前中序都不一样

		```java
		class Solution {
		    public List<Integer> postorderTraversal(TreeNode root) {
		        ArrayList<Integer> ans = new ArrayList<>();
		        if (root == null) return ans;
		        Stack<TreeNode> stack = new Stack<>();
		        while (root != null || !stack.isEmpty()) {
		            while (root != null) {//步骤(1)
		                stack.add(root);
		                if (root.left != null)
		                    root = root.left;
		                else
		                    root = root.right;
		            }
		            //此时栈顶的左右子树都遍历了
		            TreeNode peek = stack.pop();
		            ans.add(peek.val);//步骤(2)
		            if (!stack.empty() && peek == stack.peek().left) {//步骤(3)
		                root = stack.peek().right;//步骤(4)
		            } else {
		                root = null;
		            }
		        }
		        return ans;
		    }
		}
		```

	- 199.二叉树的右视图

		层次遍历，取每层的最右侧

		> 我竟然用indexOf去取指定下标处的值…混淆了indexOf和get方法…

	- 230.二叉树最近公共祖先

		其中有一个为root，返回root；两结点在不同子树，返回root；否则递归

		可以使用boolean hasNode表示root结点是否含有p，q两结点之一，以便递归

		```java
		/*
		     *  公共祖先满足一下条件之一即可：
		     *  1. node的左子树和右子树中分别包含两个节点p，q, 则该节点为p，q公共祖先
		     *  2. node为p或者q其中一个，且node的后代中包含另一个
		     */
		    public boolean dfsRootHasTreeNode(TreeNode root, TreeNode p, TreeNode q) {
		        if(null == root) {
		            return false;
		        }
		        boolean leftHas = dfsRootHasTreeNode(root.left, p, q);
		        boolean rightHas = dfsRootHasTreeNode(root.right, p , q);
		        if(leftHas && rightHas) {//
		            ancNode = root;
		        }
		        if((leftHas || rightHas) && ((root == p) || (root == q))) {
		            ancNode = root;
		        }
		        if(leftHas || rightHas || root == p || root == q) {
		            return true;
		        }
		        return false;
		    }
		```



# ***2024.3.10打卡	Day 39***

1. 电商支付：

	- 购物车模块完成
	- 收货地址模块完成
	
2. leetcode刷题：3题

	- 662.二叉树最大深度
	
		没做出来，方法：==层次遍历+完全二叉树==
	
		==层次遍历时额外记录非null结点在完全二叉树中的index值==，并记录在level中，==最后本层长度就是level最左右结点差值+1==
	
		```java
		
		Queue<Pair<TreeNode, Integer>> queue = new LinkedList<>();
		int max = 0;
		
		queue.add(new Pair<TreeNode, Integer>(root, 1));
		while(!queue.isEmpty()){
		    int size = queue.size();
		    List<Pair<TreeNode, Integer>> res = new ArrayList<>();
		    for(int i = 0; i < size; i++){
		        Pair<TreeNode, Integer> temp = queue.poll();
		        // key-value
		        TreeNode key = temp.getKey();
		        int value = temp.getValue();
		        res.add(temp);
		        if(key.left != null){
		            queue.add(new Pair<TreeNode, Integer>(key.left, value * 2));
		        }
		        if(key.right != null){
		            queue.add(new Pair<TreeNode, Integer>(key.right, value * 2 + 1));
		        }
		    }
		    int t = res.get(res.size() - 1).getValue() - res.get(0).getValue() + 1;
		    max = Math.max(max, t);
		}
		```
	
	- 124.二叉树中的最大路径和
	
		没做出来，方法：
	
		定义辅助函数maxGain，表示能为父节点带来的最大收益
	
		如果左右子树的maxGain<0，那么就不加入路径
	
		否则加入路径，并更新最大值
	
		最终由于最大收益如果要和父节点相连，那么必须是"直的"，也就是当前的root同时与左右子树相连是不行的，因为无法再和父节点相连形成连线
	
		因此最后的返回是root.val+左右子树maxGain的最大值
	
		```java
		class Solution {
		    private int max = Integer.MIN_VALUE;
		
		    public int maxPathSum(TreeNode root) {
		        if (root == null) return 0;
		        maxGain(root);
		        return max;
		    }
		
		    public int maxGain(TreeNode root) {
		        if (root == null) return 0;
		        int left = Math.max(maxGain(root.left), 0);//小于0就不相连
		        int right = Math.max(maxGain(root.right), 0);//小于0就不相连
		        int maxLen = root.val + left + right;//当前最大路径长度
		        max = Math.max(maxLen, max);//更新
		        return root.val + Math.max(left, right);//为能和父节点相连，root只能和左右子树中maxGain较大者相连
		    }
		}
		```
	
	- ==297.二叉树的序列化和反序列化==
	
		我用的广搜，超时了；标答是在深搜，相当于是用前序遍历序列还原树
	
		被题目误导了，看来队列的插入和修改很耗时。。。
	
		标答的深搜前序遍历序列：
	
		```java
		class Codec {
		    public String serialize(TreeNode root) {
		        return rserialize(root, "");
		    }
		
		    public TreeNode deserialize(String data) {
		        String[] dataArray = data.split(",");
		        List<String> dataList = new LinkedList<String>(Arrays.asList(dataArray));
		        return rdeserialize(dataList);
		    }
		
		    public String rserialize(TreeNode root, String str) {
		        if (root == null) {
		            str += "None,";
		        } else {
		            str += str.valueOf(root.val) + ",";
		            str = rserialize(root.left, str);
		            str = rserialize(root.right, str);
		        }
		        return str;
		    }
		
		    public TreeNode rdeserialize(List<String> dataList) {
		        if (dataList.get(0).equals("None")) {
		            dataList.remove(0);
		            return null;
		        }
		
		        TreeNode root = new TreeNode(Integer.valueOf(dataList.get(0)));
		        dataList.remove(0);
		        root.left = rdeserialize(dataList);
		        root.right = rdeserialize(dataList);
		
		        return root;
		    }
		}
		```



# ***2024.3.11打卡	Day 40***

1. 电商支付：

	- 订单模块，完成create，list，detail方法并通过单源测试；
	
		cancel方法还没有删除数据库order和orderItem、还原商品库存
	
2. leetcode刷题：1题

	- 56.合并区间
	
		没做出来
	
		==将所有区间按左边界升序排序，那么所有可以合并的区间一定是相邻的==
	
		```java
		class Solution {
		    public int[][] merge(int[][] intervals) {
		        //归并排序思想
		        if (intervals.length == 0 || intervals.length == 1) return intervals;
		
		        Arrays.sort(intervals, new Comparator<int[]>() {
		            @Override
		            public int compare(int[] o1, int[] o2) {
		                //以左边界升序排列
		                return o1[0] - o2[0];
		            }
		        });
		
		        List<int[]> merged = new ArrayList<int[]>();
		        for (int i = 0; i < intervals.length; ++i) {
		            int L = intervals[i][0], R = intervals[i][1];
		            //不可重叠
		            if (merged.size() == 0 || merged.get(merged.size() - 1)[1] < L) {
		                merged.add(new int[]{L, R});
		            } else {//可重叠
		                merged.get(merged.size() - 1)[1] = Math.max(merged.get(merged.size() - 1)[1], R);
		            }
		        }
		        return merged.toArray(new int[merged.size()][]);
		    }
		}
		```
		
# ***2024.3.12打卡	Day 41***

1. 电商支付：

	- 订单模块完成cancel和controller
	- 安装docker desktop和rabbitMQ
	- 初步通过消息队列接通mall和pay，实现它们间的异步通信，使用natapp通过测试



# ***2024.3.13打卡	Day 42***

1. 电商支付：
	- 订单模块已完成，但是在部署模块发现自己对于mysql,redis,docker,rabbitMQ等组件或工具的理解有缺陷，且对于shell和git的操作不熟练，因此决定先补充一下基础知识
	
2. 计算机组成原理：

	  - 把当初剩下的都看完了，也算是填坑了

		计算机组成原理完结


3. 操作系统：

	- 看到进程的五大状态

4. leetcode刷题：1题

	- 215.数组中第k个最大元素

		又没做出来，主要是优先队列的题做的太少了，拿到题时没想到可以用优先队列

		add vs offer：都可以添加元素，区别是对于有容量限制的容器，add会抛出异常，而offer会返回false，相比之下offer更安全

		```java
		class Solution {
		    public int findKthLargest(int[] nums, int k) {
		        if (nums.length == 1) return nums[0];
		
		        Queue<Integer> minQueue = new PriorityQueue<>(new Comparator<Integer>() {
		            @Override
		            public int compare(Integer o1, Integer o2) {
		                return o1 - o2;//小根堆
		            }
		        });
		
		        for (int i = 0; i < nums.length; i++) {
		            if (minQueue.size() < k) {
		                minQueue.offer(nums[i]);
		            } else if (minQueue.peek() < nums[i]) {//当前数更大，代替peek成为第k大的数
		                minQueue.poll();
		                minQueue.offer(nums[i]);
		            }
		        }
		
		        return minQueue.peek();
		
		    }
		}
		```



# ***2024.3.14打卡	Day 43***

1. 操作系统：
	  - 进程篇完成


2. 仿牛客网：
	  - 跟着他前面几节复习了一下Spring和Spring MVC，明天开始写社区首页


2. leetcode刷题：5题

	- 315.计算右侧小于当前元素的个数

		没做出来，没想到这道是归并排序的应用，类似于当初做过的逆序对

		需要在归并排序的过程中，根据归并排序左右子数组有序的性质，做出一些操作

		如本题中，当右数组当前的数 >= 左数组当前的数时，那么就说明左数组当前的数大于右侧数组当前的数之前的所有数，即右侧数组当前比左数组当前的数小的数的个数为：rightIndex

		为方便统计，可以将nums[index]和index打包，方便判断左数组的数，在原始数组中的下标是多少，从而便于我们增加ans中对应index的值

		```java
		class Solution {
		    private List<Integer> ans = new ArrayList<>();//第index个数右侧比它小的数的总数
		
		    public List<Integer> countSmaller(int[] nums) {
		        if (nums.length == 1) {
		            ans.add(0);
		            return ans;
		        }
		
		        List<Pair<Integer/*num*/, Integer/*index*/>>
		                list = new ArrayList<>();//保存nums[index]和index
		
		        for (int i = 0; i < nums.length; i++) {
		            list.add(new Pair<>(nums[i], i));
		        }
		
		        for (int i = 0; i < nums.length; i++) {//注意此处不要new ArrayList<>(nums.length)，因为这样做会导致ans.size()=0，从而后续进行get,set时会报出界的错
		            ans.add(0);
		        }
		
		        merge(list);
		
		        return ans;
		    }
		
		    public List<Pair<Integer, Integer>> merge(List<Pair<Integer, Integer>> list) {
		        if (list.size() == 1) return list;
		
		        int mid = list.size() / 2;
		        List<Pair<Integer, Integer>> left = merge(list.subList(0, mid));
		        List<Pair<Integer, Integer>> right = merge(list.subList(mid, list.size()));
		
		        List<Pair<Integer, Integer>> merged = new ArrayList<>();//升序排序
		        int leftIndex = 0, rightIndex = 0;
		        while (leftIndex < left.size() && rightIndex < right.size()) {
		            if (left.get(leftIndex).getKey() > right.get(rightIndex).getKey()) {
		                merged.add(right.get(rightIndex));
		                rightIndex++;
		            } else {//右侧有序数列下表为rightIndex比此时左侧数列的数大
		                //那么说明右侧该数左边的所有数都比left的leftIndex处的数小
		                //因此left的leftIndex处对应的ans，应该增加rightIndex
		                ans.set(left.get(leftIndex).getValue(),//取出index
		                        ans.get(left.get(leftIndex).getValue()) + rightIndex);//增加rightIndex
		                merged.add(left.get(leftIndex));
		                leftIndex++;
		            }
		        }
		
		        while (leftIndex < left.size()) {//右侧数组遍历完了，说明此时左侧数组剩余的所有数
		            //都比右侧数组所有数大，因此它们在ans中对应位置的值都要增加right.size()
		            ans.set(left.get(leftIndex).getValue(),
		                    ans.get(left.get(leftIndex).getValue()) + right.size());
		            merged.add(left.get(leftIndex));
		            leftIndex++;
		        }
		
		        while (rightIndex < right.size()) {
		            merged.add(right.get(rightIndex));
		            rightIndex++;
		        }
		
		        return merged;
		    }
		}
		```
		
	- 69.x的平方根
	
	  本题要求省略小数，因此最后只剩两个数时，应该返回靠左的那个数，因此应该有right=mid-1，那么计算mid时就要mid=(left+right+1)/2，但是此题有一个坑，那就是0+2147483647+1会超出int范围，因此mid，left，right都要用long，最后强转回int
	
	  ```java
	  class Solution {
	      public int mySqrt(int x) {
	          if (x == 0 || x == 1) return x;
	  
	          long left = 0, right = x;
	          while (left < right) {
	              long mid = (left + right + 1) / 2;//偏右
	              if (mid * mid > x) {
	                  right = mid - 1;//向左边靠，从而省略小数
	              } else if (mid * mid < x) {
	                  left = mid;
	              } else {
	                  left = mid;
	                  break;
	              }
	          }
	  
	          return (int) left;
	      }
	  }
	  ```
	
	- 367.有效的完全平方数
	
	  和上面那题大同小异，也是二分查找找
	
	- 704.二分查找
	
	  有一个坑：最后出循环不能直接返回-1，因为可能left和right相遇的点刚好是target，如{2,5},target=5，left=right时，nums[left]刚好为target，但是因为left=right，就出循环了，因此最后要判断：
	
	  ```java
	  return nums[left] == target ? left : -1;//防止target刚好是left和right相遇的点
	  ```
	
	- 33.搜索旋转排序数组
	
	  没做出来，这题思路比较巧妙
	
	  先找到mid，然后判断nums[left]和nums[mid]的大小
	
	  如果nums[left]<nums[mid]，那么mid左侧有序，如果target在左侧有序数组中，则right=mid-1，否则left=mid;
	
	  nums[left]>nums[mid]，那么mid右侧有序，如果target在右侧有序数组中，则left=mid，否则right=mid-1
	
	  为什么可以这么判断呢，因为只分成了两段，中点肯定在某一个有序数组中，如果左侧不是有序的，那么左侧肯定是有序数组+部分右边的有序数组，那么右侧就一定是有序的了
	
	  注意：在判断是是用nums[left]做标杆，那么中点就要偏右，否则可能出现中点求出来就是左边界的情况，如{1,0}target=0，中点先到1，1<1不成立，但是中点及其右侧{1,0}并不是升序的
	
	  ```java
	  class Solution {
	      public int search(int[] nums, int target) {
	          if (nums.length == 1) return nums[0] == target ? 0 : -1;
	  
	          int left = 0, right = nums.length - 1;
	          while (left < right) {
	              int mid = (left + right + 1) / 2;//偏右
	              if (target == nums[mid]) {
	                  return mid;
	              }
	  
	              if (nums[left] < nums[mid]) {//mid左侧有序
	                  if (nums[left] <= target && target < nums[mid]) {//在左侧数组中
	                      right = mid - 1;
	                  } else {
	                      left = mid;
	                  }
	              } else {//mid右侧有序
	                  if (nums[mid] < target && target <= nums[right]) {//在右侧数组中
	                      left = mid;
	                  } else {
	                      right = mid - 1;
	                  }
	              }
	          }
	  
	      return nums[left] == target ? left : -1;//防止target刚好是left和right相遇的点
	      }
	  }
	  ```



# ***2024.3.15打卡	Day 44***

1. 仿牛客网：

	- 主页面的动态展示，置顶消息的展示已完成，现在进行到2、3、4…页码分页

2. leetcode：3题

	- 34.在排序数组中查找元素的第一个和最后一个位置

		就是在查到nums[mid]==target后，向左和向右遍历

	- 26.删除有序数组中的重复项

		双指针，left代表有序部分，right遍历整个数组

	- 88.合并两个有序数组

		写腻了。。。



# ***2024.3.16打卡	Day 45***

1. 仿牛客网：

	- 借助pageHelper实现分页功能，加强了对pageHelper的使用
	- 完成邮件发送
	- 注册功能完成至实现代码复用

2. leetcode：3题

	- 11.盛最多水的容器

		双指针，类似于接雨水

	- 15.三数之和

		遍历第一个数，然后后两个数双指针，加起来值为-nums[first]

		```java
		class Solution {
		    public List<List<Integer>> threeSum(int[] nums) {
		        int n = nums.length;
		        Arrays.sort(nums);
		        List<List<Integer>> ans = new ArrayList<List<Integer>>();
		        // 枚举 a
		        for (int first = 0; first < n; ++first) {
		            // 需要和上一次枚举的数不相同
		            if (first > 0 && nums[first] == nums[first - 1]) {
		                continue;
		            }
		            // c 对应的指针初始指向数组的最右端
		            int third = n - 1;
		            int target = -nums[first];
		            // 枚举 b
		            for (int second = first + 1; second < n; ++second) {
		                // 需要和上一次枚举的数不相同
		                if (second > first + 1 && nums[second] == nums[second - 1]) {
		                    continue;
		                }
		                // 需要保证 b 的指针在 c 的指针的左侧
		                while (second < third && nums[second] + nums[third] > target) {
		                    --third;
		                }
		                // 如果指针重合，随着 b 后续的增加
		                // 就不会有满足 a+b+c=0 并且 b<c 的 c 了，可以退出循环
		                if (second == third) {
		                    break;
		                }
		                if (nums[second] + nums[third] == target) {
		                    List<Integer> list = new ArrayList<Integer>();
		                    list.add(nums[first]);
		                    list.add(nums[second]);
		                    list.add(nums[third]);
		                    ans.add(list);
		                }
		            }
		        }
		        return ans;
		    }
		}
		```

	- 287.寻找重复数

		没做出来，双指针+二分查找没看懂。。。

		```java
		class Solution {
		    public int findDuplicate(int[] nums) {
		        int n = nums.length;
		        int l = 1, r = n - 1, ans = -1;
		        while (l <= r) {
		            int mid = (l + r) >> 1;
		            int cnt = 0;
		            for (int i = 0; i < n; ++i) {
		                if (nums[i] <= mid) {
		                    cnt++;
		                }
		            }
		            if (cnt <= mid) {
		                l = mid + 1;
		            } else {
		                r = mid - 1;
		                ans = mid;
		            }
		        }
		        return ans;
		    }
		}
		```

		另外可以二进制，判断哪一位的1比正常的那一位1的和大，那么重复的数对应位必为1
		
		还可以使用Floyd制圈法，下标为i的指向nums[i]的结点，有重复数必成环



# ***2024.3.17打卡	Day 46***

1. 仿牛客网：

	- 完成注册功能的页面跳转，表单验证和激活账号
	- 完成登录功能的页面跳转

2. leetcode：4题

	- 20.有效括号

		简单栈的应用

	- 155.最小栈

		加入的数比最小栈的栈顶小，则加入该数，否则加入最小栈栈顶的数

	- 225.用队列实现栈

		push的时候，把所有元素出队，把最新加入的元素放到队头，实现后进先出

	- 232.用栈实现队列

		将两个栈底部拼在一起，当pop或peek时，如果left为空则将right的所有元素加入left



# ***2024.3.18打卡	Day 47***

1. 仿牛客网：

	- 完成登录功能的生成验证码，登入和登出，想在勾选记住我之后实现自动进入登录完成页面的功能(直接跳过登录的页面)，有思路了，但还没实现



# ***2024.3.19打卡	Day 48***

1. 仿牛客网：

	- 通过登录凭证拦截器，判断当前的cookie是否有效，有效则进入登陆后的页面
	- 完成账号设置的上传、获取新头像，修改密码的功能
	- 实现忘记密码发送验证码邮件，检验验证码是否正确，填写新密码的功能
	
2. leetcode：1题

	- 239.滑动窗口的最大值

		双向链表，存储index，保证nums[index]再链表中单调递减

		```java
		class Solution {
		    public int[] maxSlidingWindow(int[] nums, int k) {
		        Deque<Integer> deque = new LinkedList<>();
		        for (int i = 0; i < k; i++) {
		            while (!deque.isEmpty() && nums[deque.getLast()] <= nums[i]) {
		                deque.pollLast();
		            }
		            deque.addLast(i);
		        }
		        int[] ans = new int[nums.length - k + 1];
		        int i = 0;
		        ans[i++] = nums[deque.getFirst()];
		        for (int right = k; right < nums.length; right++) {
		            while (!deque.isEmpty() && nums[deque.getLast()] <= nums[right]) {
		                deque.pollLast();
		            }
		            deque.addLast(right);
		            if (right - k >= deque.getFirst()) {
		                deque.pollFirst();
		            }
		            ans[i++] = nums[deque.getFirst()];
		        }
		        return ans;
		    }
		}
		```



# ***2024.3.20打卡	Day 49***

1. 仿牛客网：

	- 通过自定义注解和登录限制拦截器防止非登录用户访问登录限制页面
	- 通过前缀树实现从文件获取敏感词，敏感词写入前缀树，通过查找前缀树过滤敏感词的功能
	- 通过AJAX在不跳转页面的前提下发送异步请求，并接收和转换服务器的json格式回复，显示提示框

2. leetcode：3题

	- LCR 126.斐波那契数

		不能用递归，要用递推，递归做了太多无效运算了，要算$2^{100}$次

	- 70.爬楼梯

		简单dp

	- LCR 127.跳跃训练

		同70题



# ***2024.3.21打卡	Day 50***

1. 仿牛客网：

	- 完成帖子详情页的显示，和显示index差不多
	- 完成回复帖子的功能

2. leetcode：3题

	- 121.买卖股票的最佳时机

		dp[len]\[3]表示没有，持有，交易结束三种状态；当然可以动态记录当前最小值，然后一次遍历

	- 剑指offer 42.连续子数组最大和

		我的做法有点复杂，类似于回文字符串的处理方式

		```java
		public int maxSubArray(int[] num) {
		    if (num.length == 1) return num[0];
		    int[][] dp = new int[num.length][num.length];
		    int max = Integer.MIN_VALUE;
		    for (int i = 0; i < num.length; i++) {
		        dp[i][i] = num[i];
		        max = Math.max(max, dp[i][i]);
		    }
		    for (int len = 2; len <= num.length; len++) {
		        for (int begin = 0; begin + len - 1 < num.length; begin++) {
		            dp[begin][begin + len - 1] = Math.max(dp[begin + 1][begin + len - 1] + num[begin], dp[begin][begin + len - 2] + num[begin + len - 1]);
		            max = Math.max(max, dp[begin][begin + len - 1]);
		        }
		    }
		    return max;
		}
		```

		标答解法：只使用dp[len]，表示到当前未知的最大连续子数组和

		```java
		public int maxSubArray(int[] nums) {
		    int dp = nums[0];
		    int max = nums[0];
		    // 刷新dp之前，dp相当于是 dp[i-1],刷新之后，Dp就是dp[i]
		    for(int i = 1; i < nums.length; i++){
		        dp = Math.max(dp + nums[i], nums[i]);
		        max = Math.max(max, dp);
		    }
		
		    return max;
		}
		```

	- 5.最长回文字符串

		```java
		dp[begin][begin + len - 1] = dp[begin + 1][begin + len - 2] 
		    && s.charAt(begin) == s.charAt(begin + len - 1);
		if (dp[begin][begin + len - 1] && len > maxLen) {
		    maxLen = len;
		    left = begin;
		    right = begin + len - 1;
		}
		```



# ***2024.3.22打卡	Day 51***

1. 仿牛客网：

	- 完成私信列表和同一用户会话详情

2. leetcode：2题

	- 62.不同路径

		简单dp

	- 64.最小路径和

		简单dp



# ***2024.3.23打卡	Day 52***

1. 仿牛客网：

	- 完成发送私信，并优化未读私信显示的逻辑
	- 通过ControllerAdvice完成统一异常处理
	- 通过环绕通知完成统一日志处理
	- 点赞功能已完成Controller层

2. leetcode：2题

	- 198.打家劫舍

		简单dp

	- 221.最大正方形

		我的方法用时1621ms。。。

		```java
		else {//右下角为1
		    int preMax = Math.max(dp[i - 1][j], dp[i][j - 1]);
		    //遍历右下角边长为preMax+1的正方形
		    boolean isSquare = true;//右下角是否形成更大的正方形L
		    for (int k = 0; k < preMax + 1; k++) {
		        for (int l = 0; l < preMax + 1; l++) {
		            //出界或为0
		            if (i - k < 0 || j - l < 0 || matrix[i - k][j - l] == '0') {
		                isSquare = false;
		                break;
		            }
		        }
		        if (!isSquare) break;
		    }
		    if (isSquare) {
		        dp[i][j] = preMax + 1;
		    } else {
		        dp[i][j] = preMax;
		    }
		}
		```

		答案方法：

		`dp[i][j]`表示当右下角为1时，以`(i,j)`为右下角的最大正方形的边长

		则==`dp[i][j]=max(dp[i-1][j],dp[i-1][j-1],dp[i][j-1])+1`==

		```java
		if (matrix[i][j] == '1') {
		    if (i == 0 || j == 0) {//最上和最左边的情况，最大为1
		        dp[i][j] = 1;
		    } else {
		        dp[i][j] = 
		            Math.min(Math.min(dp[i - 1][j], dp[i][j - 1]), dp[i - 1][j - 1]) + 1;
		    }
		    maxSide = Math.max(maxSide, dp[i][j]);
		}
		```




# ***2024.3.24打卡	Day 53***

1. 仿牛客网：

	- 通过将用户id存入redis中对应实体Set中，并使用AJAX异步通知修改页面数值，完成点赞功能
	- 通过事务实现将用户id一同存入实体Set和User Set中
	- 用类似的方法实现关注功能
	- 建立page对象，实现redis的分页功能，并实现关注、粉丝列表功能
	- 由于点赞和关注的操作较为频繁，因此选择在redis中存储以提高操作的性能

2. leetcode：2题

	- 322.零钱兑换

		```java
		for (int i = 1; i <= amount; i++) {
		    for (int j = 0; j < coins.length; j++) {
		        if (i - coins[j] < 0 || dp[i - coins[j]] == Integer.MAX_VALUE) continue;
		        dp[i] = Math.min(dp[i], dp[i - coins[j]] + 1);
		    }
		}
		```

	- 518.零钱兑换II

		没做出来

		0元一种组合

		必须外层遍历硬币种类，这样分离每一枚硬币对组合数的影响，每一枚产生的组合数不会重复
		
		```java
		class Solution {
		    public int change(int amount, int[] coins) {
		        int[] dp = new int[amount + 1];
		        dp[0] = 1;
		        for (int coin : coins) {
		            for (int i = coin; i <= amount; i++) {
		                dp[i] += dp[i - coin];
		            }
		        }
		        return dp[amount];
		    }
		}
		```




# ***2024.3.25打卡	Day 54***

1. 仿牛客网：

	- 重构关注功能，使其支持用户对不同实体的关注，以便后期扩展业务
	- 完善关注、粉丝列表中的时间显示
	- 通过redis优化登录模块，将验证码信息、登陆凭证信息存入redis，并使用redis进行用户数据缓存，提升查询用户的效率
	- 看完了阻塞队列
	
2. 操作系统：

	- 看了内存的分配和回收一小节

3. leetcode：2题

	- 718.最长重复子数组

		没做出来

		这道题一定要清除dp的含义：

		dp[i]\[j]：以i结尾的nums1的子数组和以j结尾的nums2的子数组的最大公共部分，注意相同的部分必须是从结尾开始的。

		如1,2,3和1,2的最大公共部分则为0，因为从尾部开始没有公共部分；

		而1,2,3,4和1,2,4的最大公共部分为1，因为尾部4相同，再往前就不同了。

		因此如果尾部相同，则直接加一：

		```java
		if (nums1[i - 1] == nums2[j - 1]) {
		    dp[i][j] = dp[i - 1][j - 1] + 1;
		}
		```

		而尾部不同了，自然就直接为0了：

		```java
		else if (nums1[i - 1] != nums2[j - 1]) {
		    dp[i][j] = 0;
		}
		```

		```java
		for (int i = 1; i <= n1; i++) {
		    for (int j = 1; j <= n2; j++) {
		        if (nums1[i - 1] == nums2[j - 1]) {
		            dp[i][j] = dp[i - 1][j - 1] + 1;
		        } else if (nums1[i - 1] != nums2[j - 1]) {
		            dp[i][j] = 0;
		        }
		        max = Math.max(max, dp[i][j]);
		    }
		}
		```

	- 300.最长递增子序列

		没做出来

		初始设置dp[i]为1，因为有一个数

		然后遍历，看之前有没有比nums[i]小的，有的话可能值为dp[j]+1

		```java
		class Solution {
		    public int lengthOfLIS(int[] nums) {
		        int n = nums.length;
		        int[] dp = new int[n];
		
		        dp[0] = 1;
		        int max = 1;
		
		        for(int i = 1; i < n; i++){
		            //当前一个数，初始设置为1
		            dp[i] = 1;
		            //看之前有没有比nums[i]小的，有的话可能值为dp[j]+1
		            for(int j = i - 1; j >= 0; j--){
		                if(nums[j] < nums[i]){
		                    dp[i] = Math.max(dp[i], dp[j] + 1);
		                }
		            }
		
		            max = Math.max(max, dp[i]);
		        }
		
		        return max;
		    }
		}
		```




# ***2024.3.26打卡	Day 55***

1. 仿牛客网：

	- 完成Spring整合Kafka的相关配置
	- 完成在Kafka下的Event生产者和消费者的编写
	- 完成在添加评论、点赞、关注时向对应消息队列发送消息，并由消费者获取消息，并创建系统消息存入数据库
	- 完成从数据库中获取系统消息，并完成在头部以及相关页面显示的业务
	- 完成我的帖子和我的回复的页面跳转

2. leetcode：2题

	- 42.接雨水

		双指针，左边最高小于右边最高时，左指针的值和左边最高的差为这一格的雨水量；右边同理

		也可以正向遍历，再反向遍历取每一格的最小值，这样也能满足被两边同时限制

	- 72.编辑距离

		没做出来，主要是没想到可以==将长度为0作为边界==

		```java
		class Solution {
		    public int minDistance(String word1, String word2) {
		        if (word1.length() == 0 || word2.length() == 0) return word1.length() + word2.length();
		
		        //含义：长度为i的word1到长度为j的words2需要的最少步骤
		        int[][] dp = new int[word1.length() + 1][word2.length() + 1];
		
		        //零个长度到另一个长度
		        for (int i = 0; i <= word2.length(); i++) {
		            dp[0][i] = i;
		        }
		        for (int i = 0; i <= word1.length(); i++) {
		            dp[i][0] = i;
		        }
		
		        for (int i = 1; i <= word1.length(); i++) {
		            for (int j = 1; j <= word2.length(); j++) {
		                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
		                    dp[i][j] = dp[i - 1][j - 1];
		                } else {
		                    dp[i][j] = Math.min(
		                            Math.min(dp[i - 1][j]/*增加一个*/,
		                                     dp[i][j - 1]/*删除一个*/),
		                                dp[i - 1][j - 1])/*替换一个*/ + 1;
		                }
		            }
		        }
		
		        return dp[word1.length()][word2.length()];
		    }
		}
		```



# ***2024.3.27打卡	Day 56***

1. 仿牛客网：

	- 实现我的帖子功能
	- 实现我的回复功能
	- 实现删除私信和系统通知功能
	- 完成Spring整合Elasticsearch

2. leetcode：4题

	- 10.正则表达式匹配

		```java
		public boolean isMatch(String s, String p) {
		    int len1 = s.length();
		    int len2 = p.length();
		    //以j结尾的p，是否能匹配以i结尾的s
		    boolean[][] dp = new boolean[len1 + 1][len2 + 1];
		    dp[0][0] = true;//长度都是0，因此肯定可以匹配
		    //讨论是否能匹配0个字符
		    for (int j = 2; j < len2; j++) {
		        //以j结尾，则最后一个字符在j-1
		        if (p.charAt(j - 1) == '*') {
		            dp[0][j] = dp[0][j - 2];
		        }
		    }
		
		    //遍历
		    for (int i = 1; i <= len1; i++) {
		        for (int j = 1; j <= len2; j++) {
		            //不是*
		            if (p.charAt(j - 1) != '*') {
		                //匹配成功
		                if (p.charAt(j - 1)=='.' || p.charAt(j - 1) == s.charAt(i - 1)) {
		                    dp[i][j] = dp[i - 1][j - 1];
		                } else {//匹配失败
		                    dp[i][j] = false;
		                }
		            } else {//是*p.charAt(j-2)可以有0或n个
		                //当前这个无法匹配，只能匹配0个
		                if (p.charAt(j - 2)!=s.charAt(i - 1) && p.charAt(j - 2) != '.') {
		                    dp[i][j] = dp[i][j - 2];
		                } else {
		                    dp[i][j] = dp[i][j - 2]/*匹配0个*/
		                        || dp[i - 1][j - 2]/*匹配一个*/
		                        || dp[i - 1][j];/*匹配多个，相当于抵消s[i]*/
		                }
		            }
		        }
		    }
		
		    return dp[len1][len2];
		}
		```

	- 455.分发饼干

		将孩子和饼干都排序，如果满足就childIndex++，知道某一个遍历完成为止

	- 122.买卖股票的最佳时机II

		dp[n]\[2]，后面表示当前是否持有股票

		```java
		for (int i = 1; i < prices.length; i++) {
		    //没有股票:(1)昨天没有，今天没买(2)昨天有，今天卖了
		    dp[i][0] = Math.max(dp[i - 1][0], dp[i - 1][1] + prices[i]);
		    //有股票:(1)昨天有，今天没卖(2)昨天没有，今天买了
		    dp[i][1] = Math.max(dp[i - 1][1], dp[i - 1][0] - prices[i]);
		}
		```

	- 435.无重叠区间

		最少删除区间数，就是总数减去最多放下不重叠区间数，相当于教室安排最多获得那道题，就是要求摆下最多的区间，按结束index顺序排序



# ***2024.3.28打卡	Day 57***

1. 仿牛客网：

	- 通过Elasticsearch实现对帖子的搜索(可排序)，并高亮显示关键词
	- 完成SpringSecurity取代登录限制拦截器，对用户进行权限控制，并能进行对csrf攻击的防御
	- 通过SpringSecurity和thymeleaf-spring-security完成对帖子的置顶、加精、删除操作

2. leetcode：1题

	- 714.买卖股票的最佳时机含手续费

		可以用dp，也可以分别记录前一天持有和不持有股票的最多收益，后者空间O(1)



# ***2024.3.29打卡	Day 58***

1. 仿牛客网：

	- 通过redis高级数据类型HyperLogLog和bitMap完成数据统计功能，并借助拦截器和SpringSecurity完善了业务逻辑

2. leetcode：2题

	- 921.使括号有效的最小添加

		我的方法：栈

		标答贪心方法，设置leftCount，遇到右括号就减，遇到左括号就加，右括号没遇到左括号就直接ans++

	- 401.二进制手表

		做不来回溯，也不知道枚举的方法

		回溯思路：先得到按任意个hour按钮的所有小时数，存到List<Integer>[] hourMemo中；

		同理得到List<Integer>[] minuteMemo，其中minuteMemo[1]表示按2个按钮时所有分钟时间集合

		然后根据传入的turnedOn，将hour和min分别从0遍历到turnedOn-1，然后得到时间String

		```java
		class Solution {
		    //h最多亮3个,m最多亮5个,枚举h的个数 min = turnedOn - h;
		    List<String> resList = new ArrayList<String>();
		    final static List<Integer>[] hourMemo = new List[4];
		    final static List<Integer>[] minuteMemo = new List[6];
		    static int[] hour = new int[]{1, 2, 4, 8};
		    static int[] min = new int[]{1, 2, 4, 8, 16, 32};
		
		    /*
		    	得到按任意个hour按钮的所有小时数，存到List<Integer>[] hourMemo中；
		    	得到List<Integer>[] minuteMemo，其中minuteMemo[1]表示按2个按钮时所有分钟时间集合
		    */
		    static {
		        for (int i = 0; i <= 3; i++) {
		            List<Integer> hList = new ArrayList<Integer>();
		            backTrace(hour, 11, i, hList, 0, 0);
		            hourMemo[i] = hList;
		        }
		        for (int i = 0; i <= 5; i++) {
		            List<Integer> minList = new ArrayList<Integer>();
		            backTrace(min, 59, i, minList, 0, 0);
		            minuteMemo[i] = minList;
		        }
		    }
		
		    public List<String> readBinaryWatch(int turnedOn) {
		        if (turnedOn > 8) return resList;
		        for (int i = 0; i <= Math.min(3, turnedOn); i++) {
		            if (turnedOn - i > 5) continue;
		            for (int h : hourMemo[i]) {
		                for (int minute : minuteMemo[turnedOn - i]) {
		                    resList.add(h + ":" + (minute > 9 ? minute : "0" + minute));
		                }
		            }
		        }
		        return resList;
		    }
		
		    public static void backTrace(int[] times, int max, int count, List<Integer> list, int index, int timeSum) {
		        if (count == 0) {
		            list.add(timeSum);
		            return;
		        }
		        if (index == times.length) return;
		        for (int i = index; i < times.length; i++) {
		            if (timeSum + times[i] > max) break;
		            backTrace(times, max, count - 1, list, i + 1, timeSum + times[i]);
		        }
		    }
		}
		```



# ***2024.3.30打卡	Day 59***

1. 仿牛客网：

	- 通过Quartz实现分布式部署下定时任务执行，完成定时更新帖子分数，并同步更新ES中帖子数据
	- 通过ajax异步发送含文件字节流的post请求，将用户更新的头像上传至云服务器上

2. leetcode：3题

	- 22.括号生成

		没做出来，答案看得有点迷，感觉自己对于多个参数的回溯的能力不够

		```java
		void backTrace(int left, int right) {
		    if (nowStr.length() == n * 2) {
		        ans.add(new String(nowStr));
		        return;
		    }
		
		    if (left < n) {//左的数量<n，则可以放左
		        nowStr.append('(');
		        backTrace(left + 1, right);
		        nowStr.deleteCharAt(nowStr.length() - 1);
		    }
		    if (right < left) {//右的数量不应该比左多
		        nowStr.append(')');
		        backTrace(left, right + 1);
		        nowStr.deleteCharAt(nowStr.length() - 1);
		    }
		}
		```

	- 39组合总和

		注意回溯递归时传入当前的i，这样在下一次递归时可以从当前数开始，实现同一个数重复使用

		```java
		for (int i = index; i < candidates.length; i++) {
		    now += candidates[i];
		    nowList.add(candidates[i]);
		    backTrace(i);
		    now -= candidates[i];
		    nowList.remove(nowList.size() - 1);
		}
		```

	- 46.全排列

		引入visit，记录每一个位置是否被遍历

		```java
		for (int i = 0; i < nums.length; i++) {
		    if (!visit[i]) {
		        visit[i] = true;
		        now.add(nums[i]);
		        backTrace(num + 1);
		        visit[i] = false;
		        now.remove(now.size() - 1);
		    }
		}
		```



# ***2024.3.31打卡	Day 60***

1. 仿牛客网：

	- 修复上传头像到云服务器上后显示没有权限的bug，需要手动设置csrf令牌

	- 通过Caffeine将热帖列表按页进行本地缓存，并使用jmeter进行压力测试，测试结果为：当使用和不使用本地缓存的两种方法CPU负载均达到100%时，使用本地缓存的方法的吞吐量为不使用的方法的3.18倍(428 : 134)

	- 了解springboot单源测试

	- 了解通过springboot-atuator定义的断点对项目进行监控，并能自定义断点实现对特定业务的监控

	- 部署会在完成linux和nginx的学习完成后，同电商支付项目一并部署

		仿牛客网基础功能完成，后续会对其进行完善，新增功能，并完成部署。

2. leetcode：2题

	- 90.子集II

		可以先将数组排序，然后创建`backTrace(int num, int index)`，从index开始遍历，并且额外添加一个位置用于存放空

		```java
		private void backTrace(int num, int index) {
		    if (num >= nums.length) {
		        ans.add(new ArrayList<Integer>(now));
		        return;
		    }
		
		    for (int i = index; i <= nums.length; i++) {
		        if (i == nums.length) {
		            backTrace(num + 1, nums.length);
		        } else {//i < nums.length
		            /*
		            	从当前位置的后一个起，才开始检验是否重复，避免误伤
		            	如{1,2,2}，首次遍历到第二个2时，由于前面还有一个2，因此会直接continue，
		            	导致最后的子集没有{1,2,2}，只有{1,2}
		            */
		            if (i >= index + 1 && nums[i] == nums[i - 1]) {
		                continue;//避免出现重复子数组
		            }
		            now.add(nums[i]);
		            backTrace(num + 1, i + 1);
		            now.remove(now.size() - 1);
		        }
		    }
		}
		```

	- 93.复原ip地址

		没做出来

		dfs的结构是当前存入的ipnum的数量，当前新的数字的开头index

		`dfs(String s, int segId, int segStart)`，这样设计可以方便修改，并易于找出以segStart开头的数字的所有可能情况

		```java
		class Solution {
		    static final int SEG_COUNT = 4;
		    List<String> ans = new ArrayList<String>();
		    int[] segments = new int[SEG_COUNT];
		
		    public List<String> restoreIpAddresses(String s) {
		        segments = new int[SEG_COUNT];
		        dfs(s, 0, 0);
		        return ans;
		    }
		
		    public void dfs(String s, int segId, int segStart) {
		        if (segStart > s.length()) {
		            return;
		        }
		
		        // 如果找到了 4 段 IP 地址并且遍历完了字符串，那么就是一种答案
		        if (segId == SEG_COUNT) {
		            if (segStart == s.length()) {
		                StringBuffer ipAddr = new StringBuffer();
		                for (int i = 0; i < SEG_COUNT; ++i) {
		                    ipAddr.append(segments[i]);
		                    if (i != SEG_COUNT - 1) {
		                        ipAddr.append('.');
		                    }
		                }
		                ans.add(ipAddr.toString());
		            }
		            return;
		        }
		
		        // 如果还没有找到 4 段 IP 地址就已经遍历完了字符串，那么提前回溯
		        if (segStart == s.length()) {
		            return;
		        }
		
		        // 由于不能有前导零，如果当前数字为 0，那么这一段 IP 地址只能为 0
		        if (s.charAt(segStart) == '0') {
		            segments[segId] = 0;
		            dfs(s, segId + 1, segStart + 1);
		            return;
		        }
		
		        // 一般情况，枚举每一种可能性并递归
		        int addr = 0;
		        for (int segEnd = segStart; segEnd < s.length(); ++segEnd) {
		            addr = addr * 10 + (s.charAt(segEnd) - '0');
		            if (addr > 0 && addr <= 0xFF) {//不超过范围
		                segments[segId] = addr;
		                dfs(s, segId + 1, segEnd + 1);
		            } else {//超过范围，直接break，以segStart开头的可能性已经遍历完了
		                break;
		            }
		        }
		    }
		}
		```



# ***2024.4.1打卡	Day 61***

今天开始核心八股文学习~~

1. JVM
	- 完成第一周的内容，包括.java执行过程，类加载器，JVM内存分区，垃圾回收入门
	
2. Mysql
	- 完成3篇，sql语句的执行过程，redolog和binlog，事务的隔离级别和实现
	
3. Redis
	- 完成第一章：初识redis
	
5. leetcode：3题

	- 216.组合总和II
	
		简单回溯
	
	- 51.N皇后
	
		dfs只传一个参数，表示当前遍历到的行数，毕竟每一行只能放一个皇后
	
	- 130.被围绕的区域
	
		我的方法：广搜bfs，O形区域如果存在在边界上的点则不会变成X
	
		并查集忘了是什么了。。。明天去补补。。。



# ***2024.4.2打卡	Day 62***

1. JVM
	- 完成第二周的内容，包括JVM分代模型，对象在堆内存中的分配和流转，以及分析案例选用合适的JVM参数和不选用合适参数的反例

2. Mysql
	- 完成三篇，包括索引和全局锁、表级锁，还看了下昨天没怎么看懂的事务隔离实现

3. Redis
	- 第二章常用API完成一半

4. leetcode：2题

	- 200.岛屿数量
	
		我用的bfs，当然也可以用dfs，后者效率更高
	
		```java
		private void dfs(char[][] grid, int i, int j) {
		    if (i < 0 || i >= grid.length || j < 0 || j >= grid[0].length) {
		        return;
		    }
		    if (grid[i][j] == '0') {
		        return;
		    }
		
		    grid[i][j] = '0';
		    dfs(grid, i - 1, j);
		    dfs(grid, i + 1, j);
		    dfs(grid, i, j - 1);
		    dfs(grid, i, j + 1);
		}
		```
	
	- 695.岛屿的最大面积
	
		在上面代码的基础上改一下就可以了，当然也可以用dfs来做



# ***2024.4.3打卡	Day 63***

1. JVM
	- 完成第三周的内容，包括对象回收的时机，Minor GC和Full GC的算法，新生代进入老年代的时机，以及垃圾回收机制的痛点和几种常见垃圾回收机制的简介

2. Mysql
	- 看了一篇行锁和死锁，感觉没看懂，明天得重新看（感觉这个专栏难度好大。。。）

3. Redis
	- 第二章常用API结束

4. leetcode：3题

	- 721.账户合并
	
		没做出来，主要是还没搞懂并查集
	
		```java
		class Solution {
		    public List<List<String>> accountsMerge(List<List<String>> accounts) {
		        // 作用：存储每个邮箱属于哪个账户 ，同时 在遍历邮箱时，判断邮箱是否出现过[去重]
		        // 格式：<邮箱，账户id>
		        Map<String, Integer> emailToId = new HashMap<>();
		        int n = accounts.size();//id个数
		        UnionFind myUnion = new UnionFind(n);
		        for(int i = 0; i < n; i++){
		            int num = accounts.get(i).size();
		            for(int j = 1; j < num; j++){
		                String curEmail = accounts.get(i).get(j);
		                //当前邮箱没有出现过
		                if(!emailToId.containsKey(curEmail)){
		                    emailToId.put(curEmail, i);
		                }else{//当前邮箱已经出现过，那么代表这两个用户是同一个
		                    myUnion.union(i, emailToId.get(curEmail));
		                }
		            }
		        }
		        //进行完上面的步骤，同一个用户的所有邮箱已经属于同一个连通域了，但是就算在同一个连通域，不同的邮箱还是可能会对应不同的id
		        // 作用： 存储每个账户下的邮箱
		        // 格式： <账户id, 邮箱列表> >
		        // 注意：这里的key必须是账户id，不能是账户名称，名称可能相同，会造成覆盖
		        Map<Integer, List<String>> idToEmails = new HashMap<>();
		        //将同一个连通域内的邮箱对应到同一个id【也就是第一次出现的id，比如4、5在同一个连通域，那么这个连通域对应的id就是4】
		        for(Map.Entry<String, Integer> entry : emailToId.entrySet()){
		            int id = myUnion.find(entry.getValue());
		            List<String> emails = idToEmails.getOrDefault(id, new ArrayList<>());
		            emails.add(entry.getKey());
		            idToEmails.put(id,emails);
		        }
		        //经过上面的步骤，已经做到了id和邮箱集合对应起来，接下来把用户名对应起来就可以了
		        List<List<String>> res = new ArrayList<>();
		        for(Map.Entry<Integer, List<String>> entry : idToEmails.entrySet()){
		            List<String> emails = entry.getValue();
		            Collections.sort(emails);
		            List<String> tmp = new ArrayList<>();
		            tmp.add(accounts.get(entry.getKey()).get(0));//先添加用户名
		            tmp.addAll(emails);
		            res.add(tmp);
		        }
		        return res;
		    }
		}
		
		class UnionFind {
		    int[] parent;
		
		    public UnionFind(int n) {
		        parent = new int[n];
		        for (int i = 0; i < n; i++) {
		            parent[i] = i;
		        }
		    }
		
		    public void union(int index1, int index2) {
		        parent[find(index2)] = find(index1);
		    }
		
		    public int find(int index) {
		        if (parent[index] != index) {
		            parent[index] = find(parent[index]);
		        }
		        return parent[index];
		    }
		}
		```
	
	- 1.两数之和
	
		简单哈希表
	
		```java
		class Solution {
		    private Map<Integer/*num*/, Integer/*index*/> map = new HashMap<>();
		
		    public int[] twoSum(int[] nums, int target) {
		        for (int i = 0; i < nums.length; i++) {
		            if (map.containsKey(target - nums[i])) {
		                return new int[]{i, map.get(target - nums[i])};
		            } else {
		                map.put(nums[i], i);
		            }
		        }
		        return new int[]{-1, -1};
		    }
		}
		```
	
	- LRU缓存
	
		没做出来，忘了双向链表可以原地删除和添加了
	
		```java
		private Map<Integer, DLinkedNode> cache = new HashMap<Integer, DLinkedNode>();
		
		private void addToHead(DLinkedNode node) {
		    node.prev = head;
		    node.next = head.next;
		    head.next.prev = node;
		    head.next = node;
		}
		
		private void removeNode(DLinkedNode node) {
		    node.prev.next = node.next;
		    node.next.prev = node.prev;
		}
		
		private void moveToHead(DLinkedNode node) {
		    removeNode(node);
		    addToHead(node);
		}
		
		private DLinkedNode removeTail() {
		    DLinkedNode res = tail.prev;
		    removeNode(res);
		    return res;
		}
		```



# ***2024.4.4打卡	Day 64***

1. JVM
	- 完成第四周的内容，包括ParNew和CMS的工作原理，了解Full GC的一些工作细节

2. Mysql
	- 完成一篇8.事务到底是隔离的还是不隔离的？看了一遍没怎么看到之后去看小林coding，然后再看一遍，感觉有点理解了之后，又把前面所有事务相关的文章都又看了一遍，现在基本对事务隔离，行锁以及MVCC有一个整体的认识了

3. Redis
	- 第三章完成事务

4. leetcode：1题

	- 460.LFU缓存
	
		使用双向链表实现插入和删除O(1)，额外使用一个哈希表，存储每一个计数下的node集合，当容量不足，需要删除时，在删除后如果集合为空，则删除它，并将min设为1。
	
		需要注意min的更新方法：当结点的cnt==min且当前节点为改变前的counter中的最后一个元素，即移除后原counter的list大小=0，cnt增加时min也需要增加
	
		```java
		class LFUCache {
		    class DLinkedNode {
		        int key;
		        int value;
		        int cnt = 0;
		        DLinkedNode prev;
		        DLinkedNode next;
		
		        public DLinkedNode() {
		        }
		
		        public DLinkedNode(int _key, int _value) {
		            key = _key;
		            value = _value;
		        }
		    }
		
		    private Map<Integer, DLinkedNode> cache = new HashMap<Integer, DLinkedNode>();
		    private int size;
		    private int capacity;
		    private DLinkedNode head, tail;
		    private Map<Integer, List<DLinkedNode>> counter = new HashMap<>();
		    private int minCnt = Integer.MAX_VALUE;
		
		    public LFUCache(int capacity) {
		        this.size = 0;
		        this.capacity = capacity;
		        // 使用伪头部和伪尾部节点
		        head = new DLinkedNode();
		        tail = new DLinkedNode();
		        head.next = tail;
		        tail.prev = head;
		    }
		
		    public int get(int key) {
		        DLinkedNode node = cache.get(key);
		        if (node == null) {
		            return -1;
		        }
		        // 如果 key 存在
		        addCnt(node);
		        return node.value;
		    }
		
		    public void put(int key, int value) {
		        DLinkedNode node = cache.get(key);
		        if (node == null) {
		            if (size == capacity) {
		                List<DLinkedNode> minNodeList = counter.get(minCnt);
		                DLinkedNode rmNode = minNodeList.remove(0);
		                if (minNodeList.size() == 0) {
		                    minNodeList = null;
		                }
		                cache.remove(rmNode.key);
		                removeNode(rmNode);
		                size--;
		            }
		
		            DLinkedNode newNode = new DLinkedNode(key, value);
		            addCnt(newNode);
		
		            // 添加进哈希表
		            cache.put(key, newNode);
		            size++;
		
		            // 添加到双向链表
		            addToHead(newNode);
		
		        } else {
		            // 如果 key 存在
		            node.value = value;
		            addCnt(node);
		        }
		    }
		
		    private void addToHead(DLinkedNode node) {
		        node.prev = head;
		        node.next = head.next;
		        head.next.prev = node;
		        head.next = node;
		    }
		
		    private void removeNode(DLinkedNode node) {
		        node.prev.next = node.next;
		        node.next.prev = node.prev;
		    }
		
		    //修改计数器
		    private void addCnt(DLinkedNode node) {
		        List<DLinkedNode> preNodeList = counter.get(node.cnt);
		        if (preNodeList != null) {
		            preNodeList.remove(node);
		        }
		        //如果node的cnt==minCnt且原node移除之后，原cnt没有结点了，那么minCnt就要和cnt一起++
		        if (minCnt == node.cnt && (preNodeList == null || preNodeList.size() == 0)) {
		            minCnt++;
		        } else {
		            minCnt = Math.min(minCnt, node.cnt + 1);
		        }
		        node.cnt++;
		        List<DLinkedNode> nodeList = counter.get(node.cnt);
		        if (nodeList == null) {
		            nodeList = new ArrayList<>();
		        }
		        nodeList.add(node);
		        counter.put(node.cnt, nodeList);
		
		    }
		}
		```



# ***2024.4.5打卡	Day 65***

1. JVM
	- 完成第五周的内容，包括G1的工作原理，在设定的垃圾回收预留时间下清理新生代，以及老年代、新生代、大对象的Mixed GC流程，以及G1优化的核心参数`XX:MaxGCPauseMills`

2. Mysql
	- 3篇。唯一索引 vs 普通索引，优化器错选索引问题，字符串索引优化方法

3. Redis
	- 第三章，小功能大用处完成

4. leetcode：3题

	- 41.缺失的第一个正数
	
		没做出来题目要求的O(n)时间，O(1)空间的方法
	
		答案的意思是给出的数组长为n，那么最小未出现的正数最多是n+1。那么可以遍历数组，如果nums[i]在1~n，那么就在nums[nums[i]-1]的位置上做标记，记录当前位置下标+1的数存在，记录的方式就是把它变成相反数，这样便于最后遍历。
	
		```java
		class Solution {
		    public int firstMissingPositive(int[] nums) {
		        int n = nums.length;
		        //<=0的数一律变成n+1，防止影响标记
		        for (int i = 0; i < n; i++) {
		            if (nums[i] <= 0) {
		                nums[i] = n + 1;
		            }
		        }
		        for (int i = 0; i < n; i++) {
		            //首先取绝对值
		            int num = Math.abs(nums[i]);
		            if (num > n)
		                continue;
		            if (1 <= num && num <= n) {
		                //记录的方式就是把它变成相反数
		                nums[num - 1] = -Math.abs(nums[num - 1]);
		            }
		        }
		        for (int i = 0; i < n; i++) {
		            if (nums[i] < 0)
		                continue;
		            //第一个不小于0的位置，则index+1为最小未出现的数
		            return i + 1;
		        }
		        //否则就是n+1
		        return n + 1;
		    }
		}
		```
	
	- 9.回文数
	
		双指针
	
	- 415.字符串相加
	
		StringBuffer的运用



# ***2024.4.6打卡	Day 66***

1. JVM
	- 完成第六周内容，主要是对之前的PN+CMS和G1的回顾，以及对Young GC日志使用的入门

		图解JVM专栏完成，明天开始看书
	
2. Mysql
	- 3篇。内存淘汰脏页导致MySQL"抖了一下"，delete只会将记录位置改为可复用、磁盘文件大小不变，如果要去掉"空洞需要重建表"，InnoDB由于支持事务、因此count(*)必须遍历表，优化器会选择最小的索引数进行遍历，count(\*)是专门优化过的、因此首选count(\*)

3. Redis
	- 第五章持久化，完成RDB和AOF

4. leetcode：2题

	- 8.字符串转换整数(atoi)
	
		处理的条件很多，我是每一个条件单独写了一个方法，也便于我修改
	
	- 31.下一个排列
	
		没做出来，解答很巧妙，先是从右到左找到第一个非递增的数，然后将这个数与它后面从后往前遍历第一个大于它的数交换位置，最后把该数后面部分反向
	
		```java
		class Solution {
		    public void nextPermutation(int[] nums) {
		        int i = nums.length - 2;
		        while (i >= 0 && nums[i] >= nums[i + 1]) {
		            i--;
		        }
		        if (i >= 0) {
		            int j = nums.length - 1;
		            while (j >= 0 && nums[i] >= nums[j]) {
		                j--;
		            }
		            swap(nums, i, j);
		        }
		        reverse(nums, i + 1);
		    }
		
		    public void swap(int[] nums, int i, int j) {
		        int temp = nums[i];
		        nums[i] = nums[j];
		        nums[j] = temp;
		    }
		
		    public void reverse(int[] nums, int start) {
		        int left = start, right = nums.length - 1;
		        while (left < right) {
		            swap(nums, left, right);
		            left++;
		            right--;
		        }
		    }
		}
		```
	



# ***2024.4.7打卡	Day 67***

1. JVM
	- 书第六章开了个头

2. Mysql
	- 2篇，日志相关问题、聚合函数会导致优化器放弃走索引的B+树

3. Redis
	- 第五章持久化完成

4. leetcode：

	- 43.字符串相乘
	
		没想到还有toCharArray这么方便的api！
	
		```java
		char[] arr1 = num1.toCharArray();
		char[] arr2 = num2.toCharArray();
		int len1 = arr1.length;
		int len2 = arr2.length;
		int[] part = new int[len1 + len2];
		
		for (int i = len1 - 1; i >= 0; i--) {
		    for (int j = len2 - 1; j >= 0; j--) {
		        int mul = (arr1[i] - '0') * (arr2[j] - '0');
		        int sum = mul + part[i + j + 1];
		        part[i + j + 1] = sum % 10;
		        part[i + j] += sum / 10;
		    }
		}
		```
	
	- 189.轮换数组
	
		简单数组运用
	
	- 238.除自身以外所有数组的乘积
	
		前缀和，两次遍历
	
		```java
		class Solution {
		    public int[] productExceptSelf(int[] nums) {
		        int length = nums.length;
		        int[] answer = new int[length];
		
		        // answer[i] 表示索引 i 左侧所有元素的乘积
		        // 因为索引为 '0' 的元素左侧没有元素， 所以 answer[0] = 1
		        answer[0] = 1;
		        for (int i = 1; i < length; i++) {
		            answer[i] = nums[i - 1] * answer[i - 1];
		        }
		
		        // R 为右侧所有元素的乘积
		        // 刚开始右边没有元素，所以 R = 1
		        int R = 1;
		        for (int i = length - 1; i >= 0; i--) {
		            // 对于索引 i，左边的乘积为 answer[i]，右边的乘积为 R
		            answer[i] = answer[i] * R;
		            // R 需要包含右边所有的乘积，所以计算下一个结果时需要将当前值乘到 R 上
		            R *= nums[i];
		        }
		        return answer;
		    }
		}
		```



# ***2024.4.8打卡	Day 68***

1. JVM
	- 第六章：字节码的结构完成

2. Mysql
	- 3篇。查询一行时速度慢可能是被锁住或者一致性读中获取事务开始时的状态，幻读问题及RR中间隙锁解决幻读问题，RR级别下的加锁规则（行锁和间隙锁的添加场景）

3. Redis
	- 第七章阻塞，看完引起阻塞的内部原因

4. leetcode：6题

	- [704. 二分查找](https://leetcode.cn/problems/binary-search/)
	
		注意最后要考虑target就是left刚好和right的相遇点的情况
	
	- [69. x 的平方根](https://leetcode.cn/problems/sqrtx/)
	
		注意结果向下取整，因此二分查找需要偏右，然后right=mid-1，把结果左移
	
	- [27. 移除元素](https://leetcode.cn/problems/remove-element/)
	
		双指针
	
	- [26. 删除有序数组中的重复项](https://leetcode.cn/problems/remove-duplicates-from-sorted-array/)
	
		和27题类似，只不过这里left和right从1开始，因为index为0从数前面没有数，即遍历到index=0时，该数肯定不会引发重复
	
	- [11. 盛最多水的容器](https://leetcode.cn/problems/container-with-most-water/)
	
		双指针，右边比左边高，则左边++；否则右边-\-
	
	- [209. 长度最小的子数组](https://leetcode.cn/problems/minimum-size-subarray-sum/)
	
		双指针，滑动窗口



# ***2024.4.9打卡	Day 69***

1. JVM
	- 第六章：类文件结构完成

2. Mysql
	- 3篇，关闭sleep的事务线程、消除慢查询、断开某些功能临时提高性能，join的NLJ和BNL算法，对NLJ和BNL的优化(后者使用临时表)

3. Redis
	- 第七章阻塞完成

3. 操作系统
	
	- 完成至操作系统的文件管理
	
4. leetcode：3题

	- [LCR 180. 文件组合](https://leetcode.cn/problems/he-wei-sde-lian-xu-zheng-shu-xu-lie-lcof/)
	
		滑动窗口
	
	- [LCR 120. 寻找文件副本](https://leetcode.cn/problems/shu-zu-zhong-zhong-fu-de-shu-zi-lcof/)
	
		由于document[i] <= n-1，因此可以将document[i]看作索引，去标记数组中对应位置的数，标记就是加上负号(为了排除0的影响，最开始时所有document[i]要++)，如果document[document[i] - 1] < 0，说明这个地方已经被标记过了，因此document[i] - 1就是重复的数；否则将document[document[i] - 1]加上负号，表示被访问过了
	
		该算法的时间复杂度O(n)，空间复杂度O(1)(标记就是加在当前数组中的)
	
		```java
		class Solution {
		    public int findRepeatDocument(int[] documents) {
		        int n = documents.length;
		        //为了排除0的影响，最开始时所有document[i]要++
		        for (int i = 0; i < n; i++) {
		            documents[i]++;
		        }
		        for (int i = 0; i < n; i++) {
		            //documents[i]可能被加上了负号，因此要先取绝对值
		            int index = Math.abs(documents[i]);
		            //注意一开始加上的1要减去，如果documents[index - 1]已经被访问过了
		            if(documents[index - 1] < 0)
		                return index - 1;
		            //没有被访问过则加上负号(访问符)
		            else 
		                documents[index - 1] = (-1) * Math.abs(documents[index - 1]);
		        }
		        //没有重复的数
		        return -1;
		    }
		}
		```
	
	- [203. 移除链表元素](https://leetcode.cn/problems/remove-linked-list-elements/)
	
		递归，链表的题大多都可以递归
	



# ***2024.4.10打卡	Day 70***

1. JVM
	- 第七章：类加载的五个阶段、双亲委派机制完成

2. Mysql
	- 4篇，完成内存表，自增主键不连续的情况，insert语句涉及的锁，Mysql中常见自增id用完后的情况

		Mysql45讲标星文章已看完，主从部分没看
	
3. Redis
	- 完成内存消耗小节

5. leetcode：7题

	- [876. 链表的中间结点](https://leetcode.cn/problems/middle-of-the-linked-list/)
	
		快慢节点
	
	- [LCR 140. 训练计划 II](https://leetcode.cn/problems/lian-biao-zhong-dao-shu-di-kge-jie-dian-lcof/)
	
		寻找倒数第k个节点，快慢指针，快指针快k个节点
	
	- [19. 删除链表的倒数第 N 个结点](https://leetcode.cn/problems/remove-nth-node-from-end-of-list/)
	
		和上面那道题很相似，先找到倒数第N个节点，再删除
	
		也可以递归（不过要先求出链表总长，感觉没有快慢指针好）
	
	- [LCR 123. 图书整理 I](https://leetcode.cn/problems/cong-wei-dao-tou-da-yin-lian-biao-lcof/)
	
		递归翻转链表，注意最后要head.next = null，否则会成环。。。
	
	- [160. 相交链表](https://leetcode.cn/problems/intersection-of-two-linked-lists/)
	
		双指针，走到null之后变道另一个链表
	
	- [206. 反转链表](https://leetcode.cn/problems/reverse-linked-list/)
	
		递归，可以看上面的LCR 123
	
	- [234. 回文链表](https://leetcode.cn/problems/palindrome-linked-list/)
	
		最优方法：找到中点，翻转后半段链表，然后和前半段比较，不相同则返回false，出循环则返回true，这样可以避免额外创建一个链表进行记录



# ***2024.4.11打卡	Day 71***

1. JVM
	- 第八章：字节码执行引擎完成，包括栈帧的结构和字节码解释执行的过程

2. Mysql
	- 看了下Mysql相关的八股文，准备过几天再看一遍

3. Redis
	- 第八章内存完成，包括序列化，共享池，ziplist优化hash，intset优化整数集合，ziplist进行键分组

4. leetcode：4题

	- [92. 反转链表 II](https://leetcode.cn/problems/reverse-linked-list-ii/)
	
		递归，将left~right转化为求链表的前right-left+1个节点翻转
	
		```java
		public ListNode reverseBetween(ListNode head, int left, int right) {
		    if (head == null || head.next == null) return head;
		    if (left == 1)
		        return reverseN(head, right - left + 1);
		    else {
		        head.next = reverseBetween(head.next, left - 1, right - 1);
		        return head;
		    }
		}
		```
	
	- [25. K 个一组翻转链表](https://leetcode.cn/problems/reverse-nodes-in-k-group/)
	
		依然是递归
	
		```java
		public ListNode reverseKGroup(ListNode head, int k) {
		    if (head == null || head.next == null || k <= 1)
		        return head;
		    if (flag) {
		        ListNode temp = head;
		        while (temp != null) {
		            temp = temp.next;
		            len++;
		        }
		        flag = false;
		    }
		    if (len >= k) {
		        len -= k;//此处必须先减k，不然递归reverseKGroup时的len还没有减，
		        		 //会导致长度不足也翻转的情况
		        ListNode newHead = reverseN(head, k);
		        tail.next = reverseKGroup(last, k);
		        return newHead;
		    }
		    return head;
		}
		```
	
	- 【作业7】字节真题：变形的链表反转
	
		```java
		ListNode rvs = reverseList(head);//先反转
		ListNode rvsKGroup = reverseKGroup(rvs, k);//将反转的链表按K个一组反转
		ListNode ans = reverseList(rvsKGroup);//最后反转回来记为答案
		```
	
	- [141. 环形链表](https://leetcode.cn/problems/linked-list-cycle/)
	
		Floyd判圈法
	
		```java
		public class Solution {
		    public boolean hasCycle(ListNode head) {
		        if(head == null || head.next == null) return false;
		        if(head.next == head) return true;
		        //注意初始值left和right不能一致，否则进入循环就立马出来了！
		        ListNode left = head.next, right = head.next.next;
		        while(right != null && right.next != null) {
		            if(left == right)
		                break;
		            left = left.next;
		            right = right.next.next;
		        } 
		        if(left == right)
		            return true;
		        return false;
		    }
		}
		```



# ***2024.4.12打卡	Day 72***

1. JVM
	- 第一章（主要看了一下JDK和JRE，其它的没细看）
	- 第二章内存管理，包括内存分布以及各个内存区域的溢出情况(OOM，虚拟机栈还有stackoverflow)
	
2. 操作系统
	- 第二章：储存篇剩余的完成
	
	- 第三章：线程和进程同步
	
	- 看了一下操作系统的打卡区，下次看它应该是学Linux的时候了
	
		操作系统暂时结束，明天去看计网
	
3. Redis
	- 第十一章，完成缓存的收益和成本

4. leetcode：4题

	- [142. 环形链表 II](https://leetcode.cn/problems/linked-list-cycle-ii/)
	
		Floyd判圈法
	
	- [21. 合并两个有序链表](https://leetcode.cn/problems/merge-two-sorted-lists/)
	
		递归求解
	
	- [61. 旋转链表](https://leetcode.cn/problems/rotate-list/)
	
		没什么好说的
	
	- [148. 排序链表](https://leetcode.cn/problems/sort-list/)
	
		归并算法，找中点，把链表分成最小的一个节点，然后逐步返回left和right有序链表合并的结果
	
		```java
		public ListNode sortList(ListNode head) {
		    if(head == null || head.next == null) return head;
		    ListNode mid = middleNode(head);
		    ListNode right = sortList(mid.next);
		    mid.next = null;
		    ListNode left = sortList(head);
		    return mergeList(left, right);
		}
		```



# ***2024.4.13打卡	Day 73***

1. JVM
	- 第三章垃圾回收，包括垃圾回收算法、垃圾回收器和进入老年代的时机

2. 计算机网络
	- 完成计算机网络五层入门

3. Redis
	- 第十一章，看到穿透优化

4. leetcode：2题

	- [382. 链表随机节点](https://leetcode.cn/problems/linked-list-random-node/)
	
		对Random api的调用，`int ran = new Random().nextInt(len);`
	
	- [138. 随机链表的复制](https://leetcode.cn/problems/copy-list-with-random-pointer/)
	
		```java
		class Solution {
		    public Node copyRandomList(Node head) {
		        //创建新链表，并将新旧链表以next相连
		        Node newHead = null;
		        Node temp = head;
		        while(temp != null) {
		            Node newNode = new Node(temp.val);
		            newNode.next = temp.next;
		            temp.next = newNode;
		            temp = newNode.next;
		        }
		        //赋值新链表中的random字段
		        temp = head;
		        while(temp != null) {
		            temp.next.random = temp.random == null ? null : temp.random.next;
		            temp = temp.next.next;
		        }
		        //修正新链表的next字段，并将原链表复原(否则会报错！)
		        temp = head;
		        Node ans = null;
		        while(temp != null) {
		            Node nextNode = temp.next.next;
		            temp.next.next = nextNode == null ? null : nextNode.next;
		            if(temp == head)
		                ans = temp.next;
		            temp.next = nextNode;
		            temp = nextNode;
		        }
		        return ans;
		    }
		}
		```



# ***2024.4.14打卡	Day 74***

1. 计算机网络

	  - 完成如果让你来设计网络，了解一个数据包从一台电脑，经过交换器，路由器到达另一台电脑的过程

	  - 从输入网址到最后浏览器呈现页面内容，中间发生了什么？了解应用层request和response的生成规则


2. Redis
	  - 完成第十一章缓存设计，包括穿透、无底洞、雪崩、热点key重建优化


3. leetcode：3题

	  - [146. LRU 缓存](https://leetcode.cn/problems/lru-cache/)

		使用双向链表实现原地删除移动

	  - [232. 用栈实现队列](https://leetcode.cn/problems/implement-queue-using-stacks/)

		左右栈底部拼接，如果left为空则把right的所有元素移到left

	  - [225. 用队列实现栈](https://leetcode.cn/problems/implement-stack-using-queues/)

		用一个helper队列，每次put时，先把queue的所有元素移到helper，然后将新put的元素放到queue，此时新put的元素处于队头，然后把helper中的元素移回来




# ***2024.4.15打卡	Day 75***

1. JVM
	- 完成第十章早期编译优化，包括javac执行过程和语法糖
2. 计算机网络
	  - 图解HTTP，完成第一章


2. Redis
	  - 开始看Redis原理专栏


3. leetcode：3题
	  - [20. 有效的括号](https://leetcode.cn/problems/valid-parentheses/)
	
	  老生常谈
	
	- [150. 逆波兰表达式求值](https://leetcode.cn/problems/evaluate-reverse-polish-notation/)
	
	  注意处理负数
	
	- [155. 最小栈](https://leetcode.cn/problems/min-stack/)
	
	  如果新加入的值小于最小栈栈顶，则最小栈加入该值，否则加入栈顶的值，因为最小栈一直维护当前最小的数，因此如果栈顶就是当前最小的数，那么就要再添加一个用于占位



# ***2024.4.16打卡	Day 76***

1. JVM
	- 完成第十一章：晚期优化，即字节码到机器码的优化，包括JIT即时编译器，即时编译对象和触发条件，即时编译的优化技术，以及Java即时编译器和C/C++静态编译器比较
2. 计算机网络
	- 完成图解HTTP第二章简单的HTTP协议
	- 第三章HTTP报文中的HTTP信息

3. Redis
	- Redis原理专栏2篇，键值对数据库的系统结构和实现，全局键值对和5大数据类型的底层数据结构

4. leetcode：2题

	- [215. 数组中的第K个最大元素](https://leetcode.cn/problems/kth-largest-element-in-an-array/)

		使用优先队列(小根堆)，注意匿名内部类的格式，先`()`，再`{}`

		```java
		Queue<Integer> minQueue = new PriorityQueue<>(new Comparator<Integer>() {
		    @Override
		    public int compare(Integer o1, Integer o2) {
		        return o1 - o2;
		    }
		});
		```

		维护队头是第k小的数即可

		```java
		for (int i = 0; i < nums.length; i++) {
		    if(minQueue.size() < k) {
		        minQueue.offer(nums[i]);
		    } else if(nums[i] > minQueue.peek()) {
		        minQueue.poll();
		        minQueue.offer(nums[i]);
		    }
		}
		```

	- [347. 前 K 个高频元素](https://leetcode.cn/problems/top-k-frequent-elements/)

		注意有限队列创建时匿名内部类的格式

		```java
		Queue<Pair<Integer/* 频率 */, Integer/* 元素 */>> maxQueue = new PriorityQueue<>(
		    new Comparator<Pair<Integer/* 频率 */, Integer/* 元素 */>>() {
		        @Override
		        public int compare(Pair<Integer/* 频率 */, Integer/* 元素 */> o1,
		                           Pair<Integer/* 频率 */, Integer/* 元素 */> o2) {
		            return o2.getKey() - o1.getKey();
		        }
		    });
		```



# ***2024.4.17打卡	Day 77***

1. JVM
	- 完成第十三章：Java内存模型与线程，包括Java内存模型概述，原子性、可见性、有序性，先行先发原则，线程的实现、调度和状态模型
2. 计算机网络
	- 图解HTTP完成第四章：HTTP状态码
	- 第五章：与 HTTP协作的Web服务器
3. 计算机组成 & 操作系统
	  - 程序是怎样跑起来的，第一章 CPU
3. Redis
	- 专栏2篇，Redis单线程的可行性和实现，AOF 的实现、写入和重写机制
	
4. leetcode：2题

	- [295. 数据流的中位数](https://leetcode.cn/problems/find-median-from-data-stream/)
	
		两个优先队列，将数组分成两半，左边为大根堆，右边为小根堆
	
		`left: [1,2,3(peek)]; right: [4(peek),5]`
	
		```java
		public MedianFinder() {
		    left = new PriorityQueue<Integer>(new Comparator<Integer>(){
		        @Override
		        public int compare(Integer o1, Integer o2) {
		            return o2 - o1;//left为大根堆
		        }
		    });
		    right = new PriorityQueue<Integer>(new Comparator<Integer>(){
		        @Override
		        public int compare(Integer o1, Integer o2) {
		            return o1 - o2;//right为小根堆
		        }
		    });
		}
		```
	
		动态维护两个队列，分左右一样多，左比右多两种情况；如果`num <= right.peek()`，则加入left，否则加入right，并要维护left和right的大小，保证它们一样多或左比右多一
	
		```java
		public void addNum(int num) {
		    if(left.size() == 0) {
		        left.offer(num);
		        return;
		    }
		    if(right.size() == 0) {
		        left.offer(num);
		        right.offer(left.poll());
		        return;
		    }
		    if(left.size() == right.size()) {
		        if(num <= right.peek()) {
		            left.offer(num);
		        } else {
		            right.offer(num);
		            left.offer(right.poll());
		        }
		    } else {
		        if(num <= right.peek()) {
		            left.offer(num);
		            right.offer(left.poll());
		        } else {
		            right.offer(num);
		        }
		    }
		}
		```
	
	- [1475. 商品折扣后的最终价格](https://leetcode.cn/problems/final-prices-with-a-special-discount-in-a-shop/)
	
		创建一个单调递增栈，我们要从后向前遍历，因为先加入后面的price可以便于前面的price判断是否可以折扣，递增则是为了找到最近的点
	
		```java
		class Solution {
		    public int[] finalPrices(int[] prices) {
		        Stack<Integer> stack = new Stack<>();//单调递增栈
		        int[] ans = new int[prices.length];
		        for(int i = prices.length - 1; i >= 0; i--) {//从后向前遍历
		            if(stack.isEmpty()) {
		                ans[i] = prices[i];
		                stack.add(prices[i]);
		            } else {
		                //index比i大，值还比prices[i]大，肯定不会被前面的点选中，因此直接pop
		                while(!stack.isEmpty() && stack.peek() > prices[i]) {
		                    stack.pop();
		                }
		                if(stack.isEmpty()) {
		                    ans[i] = prices[i];
		                    stack.add(prices[i]);
		                } else {
		                    ans[i] = prices[i] - stack.peek();
		                    stack.add(prices[i]);
		                }
		            }
		        }
		        return ans;
		    }
		}
		```



# ***2024.4.18打卡	Day 78***

1. JVM

	- 完成第十三章：线程安全和锁优化

		深入理解Java虚拟机一刷结束！

2. 计算机网络

	  - 完成第六章：HTTP首部字段


4. Redis

	- 2篇，RDB的实现和适用场景，主从库读写分离和实现方式

5. leetcode：3题

	- [239. 滑动窗口最大值](https://leetcode.cn/problems/sliding-window-maximum/)
	
		维护一个nums[i]值单调减的双向队列(LinkedList实现，注意要熟悉api)，队头过期就出队，入队时把所有值 <= 当前值的都出队
	
	- [144. 二叉树的前序遍历](https://leetcode.cn/problems/binary-tree-preorder-traversal/)
	
		使用栈辅助，优先遍历左子树，到null时切换到栈顶的右子树，并将栈顶出栈
	
		```java
		while (root != null || !st.empty()) {
		    if(root != null) {
		        ans.add(root.val);
		        st.add(root);
		        root = root.left;
		    } else {
		        root = st.peek().right;
		        st.pop();
		    }
		}
		```
	
	- [94. 二叉树的中序遍历](https://leetcode.cn/problems/binary-tree-inorder-traversal/)
	
		类似前序遍历



# ***2024.4.19打卡	Day 79***

1. 计算机网络

	- 完成第七章：Web安全和HTTPS
	- 第八章：确认用户身份的几种认证方式
	
2. Redis
	- 2篇。哨兵在主从切换中监控，选主，通知的作用。哨兵通过发布订阅获取彼此的ip和port，且客户端也可订阅哨兵的频道获取主从切换的状态，以及哨兵投票选取Leader执行切换操作
	
3. leetcode刷题：6题

	  - [145. 二叉树的后序遍历](https://leetcode.cn/problems/binary-tree-postorder-traversal/)

	  - 解决遍历完左子树还需遍历右子树的操作

		```java
		while(root != null) {
		    stack.add(root);
		    if(root.left != null)
		        root = root.left;
		    else
		        root = root.right;
		}
		```

		栈顶出栈时，判断其是否来自左子树还是右子树，右子树则root=null，代表下一个循环直接遍历当前的栈顶

		```java
		//此时栈顶的左右子树都已被遍历
		TreeNode peek = stack.pop();
		ans.add(peek.val);
		//判断peek是否是左子树，如果是则遍历当前栈顶的右子树，
		//否则root=null，表示下一个循环栈顶要出栈
		if(!stack.isEmpty() && peek == stack.peek().left)
		    root = stack.peek().right;
		else
		    root = null;
		```

		完整代码

		```java
		while (root != null || !stack.isEmpty()) {
		    while(root != null) {
		        stack.add(root);
		        if(root.left != null)
		            root = root.left;
		        else
		            root = root.right;
		    }
		    //此时栈顶的左右子树都已被遍历
		    TreeNode peek = stack.pop();
		    ans.add(peek.val);
		    //判断peek是否是左子树，如果是则遍历当前栈顶的右子树，
		    //否则root=null，表示下一个循环栈顶要出栈
		    if(!stack.isEmpty() && peek == stack.peek().left)
		        root = stack.peek().right;
		    else
		        root = null;
		}
		```

	  - [LCR 149. 彩灯装饰记录 I](https://leetcode.cn/problems/cong-shang-dao-xia-da-yin-er-cha-shu-lcof/)

		层次遍历，注意Queue的实现类就直接用LinkedList，可以支持头插和尾插，最方便

	  - [107. 二叉树的层序遍历 II](https://leetcode.cn/problems/binary-tree-level-order-traversal-ii/)

		最后`Collections.reverse(ans);`

	  - [LCR 151. 彩灯装饰记录 III](https://leetcode.cn/problems/cong-shang-dao-xia-da-yin-er-cha-shu-iii-lcof/)

		和107差不多，就是在每一层level记录完之后判断level是否需要reverse即可

	  - [104. 二叉树的最大深度](https://leetcode.cn/problems/maximum-depth-of-binary-tree/)

		递归

	  - [110. 平衡二叉树](https://leetcode.cn/problems/balanced-binary-tree/)

		可以在计算depth的方法中顺带判断是否平衡，如果不平衡则ans = false，并且之后的depth方法都直接返回

		```java
		class Solution {
		    private boolean ans = true;
		
		    public boolean isBalanced(TreeNode root) {
		        if (root == null)
		            return true;
		        depth(root);
		        return ans;
		    }
		
		    public int depth(TreeNode root) {
		        if (ans == false)
		            return -1;
		        if (root == null)
		            return 0;
		        int left = depth(root.left);
		        int right = depth(root.right);
		        if (Math.abs(left - right) > 1)
		            ans = false;
		        return Math.max(left, right) + 1;
		    }
		}
		```




# ***2024.4.20打卡	Day 80***

1. 计算机网络

	- 完成第九章：HTTP的追加协议

	- 完成第十章：构建Web应用的技术

	- 完成第十一章：常见Web攻击

		图解HTTP完结，准备开始看计算机网络自顶向下

2. Redis

	- 3篇，切片集群使用横向扩展的方式解决数据量增大的问题，1~9章的常见问题，String类型元数据内存占用导致内存的开销反而大于键值对本身内存空间的情况，使用二级编码下的压缩列表进行优化

3. leetcode刷题：

	  - [LCR 144. 翻转二叉树](https://leetcode.cn/problems/er-cha-shu-de-jing-xiang-lcof/)
	
	  	左右翻转后，swap左右节点
	
	- [101. 对称二叉树](https://leetcode.cn/problems/symmetric-tree/)
	
	  参数只有根节点不好进行递归，创建helper接收left和right，用于判断left和right是否对称
	
	- [199. 二叉树的右视图](https://leetcode.cn/problems/binary-tree-right-side-view/)
	
	  法一：层次遍历，取每一行最后一个元素
	
	  法二（速度更快）：先序遍历root，会最后遍历每一行的最后一个元素，只需不断更新即可
	
	  ```java
	  private void dfs(TreeNode root, int level) {
	      if (root == null)
	          return;
	      if (level > ans.size()) {
	          ans.add(root.val);
	      } else {// 更新这一行最右侧的值
	          ans.set(level - 1, root.val);
	      }
	      dfs(root.left, level + 1);// 采用先序遍历，这样每一行最右侧的节点会被最后一个遍历到
	      dfs(root.right, level + 1);
	  }
	  ```
	
	- [662. 二叉树最大宽度](https://leetcode.cn/problems/maximum-width-of-binary-tree/)
	
	  使用完全二叉树保存每一个节点的index，以便直接获取每一行的长度
	
	  ```java
	  public int widthOfBinaryTree(TreeNode root) {
	      if (root == null) {
	          return 0;
	      }
	      Queue<Pair<TreeNode, Integer/* 完全二叉树中的index */>> queue = 
	          new LinkedList<>();
	      int max = 1;
	      queue.add(new Pair<TreeNode, Integer/* 完全二叉树中的index */>(root, 1));
	      while (!queue.isEmpty()) {
	          int size = queue.size();
	          List<Pair<TreeNode, Integer>> level = new ArrayList<>();
	          for (int i = 0; i < size; i++) {
	              Pair<TreeNode, Integer> poll = queue.poll();
	              if (poll.getKey().left != null)
	                  queue.offer(new Pair<TreeNode, Integer>
	                              (poll.getKey().left, poll.getValue() * 2));
	              if (poll.getKey().right != null)
	                  queue.offer(new Pair<TreeNode, Integer>
	                              (poll.getKey().right, poll.getValue() * 2 + 1));
	              level.add(poll);
	          }
	          if (level.size() >= 2)
	              max = Math.max(max, level.get(level.size() - 1).getValue() -
	                             level.get(0).getValue() + 1);
	      }
	      return max;
	  }
	  ```



# ***2024.4.21打卡	Day 81***

1. 计算机网络

	- 计算机网络自顶向下啃不动，转而去看文章。。。
	- 完成6篇文章，看完至TCP四次挥手

2. Redis

	- 专栏2篇，使用BitMap和HyperLogLog高效解决海量数据的统计工作，使用streams数据结构实现基于redis的消息队列

3. leetcode刷题：3题

	- [105. 从前序与中序遍历序列构造二叉树](https://leetcode.cn/problems/construct-binary-tree-from-preorder-and-inorder-traversal/)
	
		根据rootVal在pre和in中的index，找到左右子树的pre和in，递归
	
	- [106. 从中序与后序遍历序列构造二叉树](https://leetcode.cn/problems/construct-binary-tree-from-inorder-and-postorder-traversal/)
	
		同上，注意使用 `Arrays.copyOfRange` 快速复制子数组
	
	- [230. 二叉搜索树中第K小的元素](https://leetcode.cn/problems/kth-smallest-element-in-a-bst/)
	
		法一：中序遍历，inorder.get(k-1)，比较慢
	
		法二：采用栈实现的中序遍历，在输出第k个root.val时将其作为返回值
	
		```java
		while(root != null || !stack.isEmpty()) {
		    if(root != null) {
		        stack.add(root);
		        root = root.left;
		    } else {
		        TreeNode pop = stack.pop();
		        if(--k == 0) // 在输出第k个root.val时将其作为返回值
		            return pop.val;
		        root = pop.right;
		    }
		}
		```



# ***2024.4.22打卡	Day 82***

1. 计算机网络

	- 完成5篇文章，包括HTTP, HTTPS, SSL, DNS, DHCP

2. Redis

	- 专栏4篇。redis的阻塞点和异步线程，使用绑核解决CPU结构对Redis性能的影响，监控内存碎片并使用自动清理清理碎片，使用慢日志和`redis-cli --bigkeys`查询慢方法和bigkeys操作。

3. leetcode刷题：5题

	- [LCR 156. 序列化与反序列化二叉树](https://leetcode.cn/problems/xu-lie-hua-er-cha-shu-lcof/)
	
		使用层次遍历序列化二叉树，同时使用层次遍历还原二叉树
	
		```java
		public String serialize(TreeNode root) {
		    if(root == null){
		        return "";
		    }
		    StringBuilder build = new StringBuilder();
		    Queue<TreeNode> queue = new LinkedList<>();
		    queue.add(root);
		
		    // 使用层次遍历序列化二叉树
		    while(!queue.isEmpty()){
		        int size = queue.size();
		        for(int i = 0; i < size; i++) {
		            TreeNode poll = queue.poll();
		            if(poll != null) {
		                queue.add(poll.left);
		                queue.add(poll.right);
		                build.append(poll.val + ",");
		            } else {
		                build.append("null,");
		            }
		        }
		    }
		
		    return build.toString();
		}
		
		public TreeNode deserialize(String data) {
		    if(data == null || data.length() <= 0)
		        return null;
		    String[] s = data.split(",");
		    if(s[0].equals("null"))
		        return null;
		    Queue<TreeNode> queue = new LinkedList<>();
		    TreeNode root = new TreeNode(Integer.parseInt(s[0]));
		    queue.add(root);
		    int i = 1;
		    
		    // 使用层次遍历还原二叉树
		    while(!queue.isEmpty()){
		        TreeNode t = queue.poll();
		        // 构建做左节点
		        if(!s[i].equals("null")){
		            TreeNode left = new TreeNode(Integer.parseInt(s[i]));
		            t.left = left;
		            queue.add(left);
		        }
		        i++;
		        // 构建右节点
		        if(!s[i].equals("null")){
		            TreeNode right = new TreeNode(Integer.parseInt(s[i]));
		            t.right = right;
		            queue.add(right);
		        }
		        i++;
		    }
		    return root;
		}
		```
	
	- [112. 路径总和](https://leetcode.cn/problems/path-sum/)
	
		递归求解
	
		```java
		public boolean hasPathSum(TreeNode root, int targetSum) {
		    if (root == null)
		        return false;
		    if (root.left == null && root.right == null)// 叶子节点时
		        return root.val == targetSum;
		    return hasPathSum(root.left, targetSum - root.val) || 
		        hasPathSum(root.right, targetSum - root.val); // 寻找左右子树是否有该叶子节点
		}
		```
	
	- [113. 路径总和 II](https://leetcode.cn/problems/path-sum-ii/)
	
		就是多了ans和now，注意`ans.add(new ArrayList(now));`，否则传的时now的引用，最后now全部清空了。
	
	- [235. 二叉搜索树的最近公共祖先](https://leetcode.cn/problems/lowest-common-ancestor-of-a-binary-search-tree/)
	
		二叉搜索树节点有序，因此如果root夹在中间，则ans=root，否则递归到另一子树。
	
	- [236. 二叉树的最近公共祖先](https://leetcode.cn/problems/lowest-common-ancestor-of-a-binary-tree/)
	
		自己先求inorder存到哈希表居然超时了
	
		方法：先求左右子树中是否有这两个节点的祖先，然后据此判断
	
		```java
		public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
		    if(root == null) return root;
		    if(root == p || root == q) return root;
		    TreeNode left = lowestCommonAncestor(root.left, p, q);
		    TreeNode right = lowestCommonAncestor(root.right, p, q);
		    if(left != null && right != null) return root;
		    if(left == null && right != null) return right;
		    if(left != null && right == null) return left;
		    else {
		        return null;
		    }
		}
		```
	



# ***2024.4.23打卡	Day 83***

1. 计算机网络

	- 文章全部完成
	
2. Linux
	- 完成Xshell远程访问Linux虚拟机，Xftp上传下载虚拟机文件，Vim，文本命令，打包解压的tar命令，Ubuntu下的apt和CentOS下的yum命令安装卸载软件。
	
3. Redis

	- 专栏1篇，缓存的两要素和Redis中缓存的实现以及针对写请求的只读和读写缓存，其中读写缓存有同步直写和异步写回两种策略。
	
4. leetcode刷题：5题

	- [136. 只出现一次的数字](https://leetcode.cn/problems/single-number/)
	
		异或运算
	
	- [LCR 133. 位 1 的个数](https://leetcode.cn/problems/er-jin-zhi-zhong-1de-ge-shu-lcof/)
	
		注意：`>>`不会移动符号位！必须使用`>>>`才会移动符号位，否则遇到负数符号位为1，不会出循环。
	
		```java
		public int hammingWeight(int n) {
		    int ans = 0;
		    while(n != 0) {
		        ans += n & 1;
		        n >>>= 1;
		    }
		    return ans;
		}
		```
	
		技巧性比较强的另一种方法：
	
		```java
		public int hammingWeight(int n) {
		    int ans = 0;
		    while(n != 0) 
		        n &= (n - 1) + ans - ans++;
		    return ans;
		}
		```
	
	- [50. Pow(x, n)](https://leetcode.cn/problems/powx-n/)
	
		快速幂算法，比如n=3=0b11，那么最后一位如果为1，则ans*=x，每次循环把x平方，恰好对应n的更高一位
	
		```java
		public double myPow(double x, int n) {
		    boolean flag = n >= 0;
		    n = Math.abs(n);
		    double ans = 1.0;
		    while (n != 0) {
		        if ((n & 1) == 1)
		            ans *= x;
		        n >>>= 1;
		        x *= x;
		    }
		    return flag ? ans : 1 / ans;
		}
		```
	
	- [260. 只出现一次的数字 III](https://leetcode.cn/problems/single-number-iii/)
	
		首先全部异或，得到两个数的异或值，根据异或值找到两个数的最低不同的位，之后按照这个不同位将nums分为两组，两组的异或结果分别为两个数
	
		```java
		public int[] singleNumber(int[] nums) {
		    int xor = 0;
		    //得到两个数的异或值
		    for(int i = 0; i < nums.length; i++) 
		        xor ^= nums[i];
		    int diff = 1;
		    //根据异或值找到两个数的最低不同的位
		    while((xor & 1) != 1) {
		        xor >>>= 1;
		        diff <<= 1;
		    }
		    int num1 = 0, num2 = 0;
		    //按照这个不同位将nums分为两组，两组的异或结果分别为两个数
		    for(int i = 0; i < nums.length; i++) {
		        if((nums[i] & diff) != 0) 
		            num1 ^= nums[i];
		        else
		            num2 ^= nums[i];
		    }
		    return new int[]{num1, num2};
		}
		```
	
	- [137. 只出现一次的数字 II](https://leetcode.cn/problems/single-number-ii/)
	
		最多32位，记录每一位出现的次数，最后%3，如果不为0那么就说明多出的那一个数的本位为1
	
		```java
		public int singleNumber(int[] nums) {
		    //最多32位
		    int[] wei = new int[32];
		    int ans = 0;
		    for(int i = 31; i >= 0; i--) {
		        for(int j = 0; j < nums.length; j++) {
		            //记录每一位出现的次数
		            if(((nums[j] >>> i) & 1) == 1)
		                wei[i]++;
		        }
		        //最后%3，如果不为0那么就说明多出的那一个数的本位为1
		        if(wei[i] % 3 != 0) {
		            ans |= (1 << i);
		        }
		    }
		    return ans;
		}
		```
	



# ***2024.4.24打卡	Day 84***

1. Linux
	- 视频课内容完成（只不过我用的Ubuntu，在安装，创建应用服务和用户权限那里有点不一样）
	
2. Git
	  - 完成9个视频，主要学习了`.git`中几个常用的文件意义，并能通过`git cat-file -t/p xxx`来查看指定编号指向的类型和具体的内容。
	
2. Redis

	- 1篇，Redis 缓存大小建议，缓存更新策略，推荐使用allkeys或volatile的lru算法，注意要自行将脏数据写回数据库，以保证数据不丢失
	
3. leetcode刷题：5题

	- [172. 阶乘后的零](https://leetcode.cn/problems/factorial-trailing-zeroes/)
	
		注意5的幂次可以提供多个5，但是介于之前被计算过了，因此只会增加一次
	
		```java
		public int trailingZeroes(int n) {
		    if(n == 0) return 0;
		    int num = 5;
		    int ans = 0;
		    while(num <= n) {
		        ans += n / num; // 介于之前被计算过了，因此只会增加一次
		        num *= 5;
		    }
		    return ans;
		}
		```
	
	- [169. 多数元素](https://leetcode.cn/problems/majority-element/)
	
		记录一个sum和当前的num，表示num所具有的权值，由于多数数的数量大于总数的一半，因此最终多数数的数目一定会大于0。遍历时如果nums[i]不同，看sum，sum>0则sum-\-，否则更新当前num
	
		```java
		public int majorityElement(int[] nums) {
		    int sum = 1; // 由于多数数的数量大于总数的一半，因此最终多数数的数目一定会大于0
		    int num = nums[0]; 
		    for(int i = 1; i < nums.length; i++) {
		        if(nums[i] == num) {
		            sum++;
		        } else { // 如果nums[i]不同，看sum
		            if(sum > 0) { // sum>0则sum--
		                sum--;
		            } else { // 否则更新当前num
		                sum = 1;
		                num = nums[i];
		            }
		        }
		    }
		    return num;
		}
		```
	
	- [LCR 186. 文物朝代判断](https://leetcode.cn/problems/bu-ke-pai-zhong-de-shun-zi-lcof/)
	
		用set判断是否重复数，如果是0就跳过，最后max-min<5即可，因为最后有效的数字的个数<=5，因此只要max-min<5，那么就可以保证会连续。
	
		```java
		for (int i = 0; i < places.length; i++) {
		    if(places[i] == 0)
		        continue;
		    if (set.contains(places[i]))
		        return false;
		    set.add(places[i]);
		    max = Math.max(max, places[i]);
		    min = Math.min(min, places[i]);
		}
		return max - min < 5;
		```
	
	- [343. 整数拆分](https://leetcode.cn/problems/integer-break/)
	
		思路：幂次尽可能多，$2^3 < 3^2, 4^3 < 3^4$，而5可以拆分得到6，会变大，所以肯定不能作基底，因此选择3
	
		```java
		public int integerBreak(int n) {
		    if (n == 2) return 1;
		    if (n == 3) return 2; // 3再拆分反而变小
		    if (n == 4) return 4;
		    int res = n / 3;
		    int mod = n % 3;
		    switch (mod) {
		        case 0:
		            return (int)Math.pow(3, res);
		        case 1:
		            return (int)Math.pow(3, res - 1) * 4;
		        case 2:
		            return (int)Math.pow(3, res) * 2;
		    }
		    return -1;
		}
		```
	
	- [LCR 132. 砍竹子 II](https://leetcode.cn/problems/jian-sheng-zi-ii-lcof/)
	
		和上一道基本一致，但是注意幂次的时候要随时%(1e9+7)，否则会超过int的上限
	



# ***2024.4.25打卡	Day 85***

1. Git
	- 完成至第30个视频，已创建Github账户并完成提交。
	
2. Redis

	- 完成1篇。缓存和数据库不同的产生原因（失败和并发）和解决方法（重读和延迟双删）。
	
3. leetcode刷题：2题

	- [455. 分发饼干](https://leetcode.cn/problems/assign-cookies/)
	
		小的孩子吃小饼干，两个都排序
	
	- [376. 摆动序列](https://leetcode.cn/problems/wiggle-subsequence/)
	
		注意cur=0的时候不能pre=cur，否则比如pre>0，被赋成pre=0，
	
		而后面又有cur>0就会多统计一次res
	
		```java
		int res = 1;
		int pre = 0;
		int cur = 0;
		
		for (int i = 1; i < nums.length; i++) {
		    cur = nums[i] - nums[i - 1];
		    // 注意此处cur=0的时候不能pre=cur，否则比如pre>0，被赋成pre=0，
		    // 而后面又有cur>0就会多统计一次res
		    if (pre <= 0 && cur > 0 || pre >= 0 && cur < 0) {
		        res++;
		        pre = cur;
		    }
		}
		```



# ***2024.4.26打卡	Day 86***

1. Git
	- 完成至第45个视频。

2. Redis

	- 1篇。缓存雪崩、击穿、穿透的成因和解决方法。

3. leetcode刷题：3题

	- [55. 跳跃游戏](https://leetcode.cn/problems/jump-game/)
	
		不断维护能跳到的最大距离，for循环的上限为此最大距离
	
	- [45. 跳跃游戏 II](https://leetcode.cn/problems/jump-game-ii/)
	
		没做出来
	
		```java
		public int jump(int[] nums) {
		    int length = nums.length;
		    int end = 0;
		    int maxPosition = 0;
		    int step = 0;
		    for (int i = 0; i < length - 1; i++) {
		        maxPosition = Math.max(maxPosition, i + nums[i]);
		        if (i == end) { // 表示到达s一步能到达的最远点，此时再跳一步最多跳到maxLen
		            end = maxPosition;
		            step++;
		        }
		    }
		    return step;
		}
		```
	
	- [621. 任务调度器](https://leetcode.cn/problems/task-scheduler/)
	
		统计每个字符出现的次数，排序，如果有一个数量=maxNum就k++，最后+k
	
		```java
		public int leastInterval(char[] tasks, int n) {
		    int[] nums = new int[26];
		    for (int i = 0; i < tasks.length; i++) {
		        nums[(int) (tasks[i] - 'A')]++;
		    }
		    Arrays.sort(nums);
		    int ans = (nums[nums.length - 1] - 1) * (n + 1) + 1;
		    for (int i = nums.length - 1; i >= 0; i--) {
		        if (nums[i] == 0)
		            return ans;
		        ans = Math.max(ans, (nums[i] - 1) * (n + 1) + 1 + (25 - i));
		    }
		    return Math.max(ans, tasks.length);
		}
		```
	



# ***2024.4.27打卡	Day 87***

1. Git
	- 视频课专栏已完成。

2. Redis

	- 2篇。使用LFU清理不再使用的缓存数据，使用pika解决Redis单实例扩大数据量。

3. leetcode刷题：2题

	- [435. 无重叠区间](https://leetcode.cn/problems/non-overlapping-intervals/)
	
		和安排活动一样，剔除最少就等同于留下最多区间，按结束位置从大到小排序，维护结束位置end。
	
	- [135. 分发糖果](https://leetcode.cn/problems/candy/)
	
		先不考虑头尾（注意只有一个元素的情况），从左到右遍历，再从右到左遍历，最后考虑头尾
	
		```java
		if (ratings.length == 1) // 注意只有一个元素的情况
		    return 1;
		int[] candy = new int[ratings.length];
		for (int i = 1; i <= ratings.length - 2; i++) { // 从左到右遍历
		    if (ratings[i] > ratings[i + 1]) {
		        candy[i] = Math.max(candy[i], candy[i + 1] + 1);
		    }
		    if (ratings[i] > ratings[i - 1]) {
		        candy[i] = Math.max(candy[i], candy[i - 1] + 1);
		    }
		}
		for (int i = ratings.length - 2; i >= 1; i--) { // 再从右到左遍历
		    if (ratings[i] > ratings[i + 1]) {
		        candy[i] = Math.max(candy[i], candy[i + 1] + 1);
		    }
		    if (ratings[i] > ratings[i - 1]) {
		        candy[i] = Math.max(candy[i], candy[i - 1] + 1);
		    }
		}
		if (ratings[0] > ratings[1]) // 最后考虑头尾
		    candy[0] = candy[1] + 1;
		if (ratings[ratings.length - 1] > ratings[ratings.length - 2])
		    candy[ratings.length - 1] = candy[ratings.length - 2] + 1;
		int ans = candy.length;
		for (int i = 0; i < candy.length; i++) {
		    ans += candy[i];
		}
		return ans;
		```
	



# ***2024.4.28打卡	Day 88***

1. Java线程池视频课
	  - 快速全部过了一遍。


2. Redis视频课

	- 快速过了一遍，主要了解Redis.conf的配置。

3. 八股文一轮复习开始！

	- Java基础完成4个问题。

4. Redis
	  - 1篇。使用原子操作，如单命令操作INCR/DECR或Lua脚本实现原子操作，相比加锁，原子操作对资源的限制更少，一般具有更高的并发性能。

5. leetcode刷题：4题

      - [77. 组合](https://leetcode.cn/problems/combinations/)

        简单回溯，注意是`backTrace(i + 1);`而不是`backTrace(index + 1);`！

      - [216. 组合总和 III](https://leetcode.cn/problems/combination-sum-iii/)

        简单回溯，注意只能使用数字1~9。

      - [40. 组合总和 II](https://leetcode.cn/problems/combination-sum-ii/)

        先排序，后面如果不是某次的第一个，且上一个和当前的值一致，则说明当前的继续遍历肯定会重复，直接continue。注意必须剪枝，否则超时。

        ```java
        public List<List<Integer>> combinationSum2(int[] candidates, int target) {
            Arrays.sort(candidates); // 去重
            this.candidates = candidates;
            this.target = target;
            backTrace(0);
            return ans;
        }

        private void backTrace(int index) {
            if (sum == target) {
                ans.add(new ArrayList(now));
                return;
            }

            if(sum > target) // 剪枝
                return;

            for (int i = index; i < candidates.length; i++) {
                if(i > index && candidates[i] == candidates[i - 1]) // 去重
                    continue;
                now.add(candidates[i]);
                sum += candidates[i];
                backTrace(i + 1);
                now.remove(now.size() - 1);
                sum -= candidates[i];
            }
        }
        ```

      - [39. 组合总和](https://leetcode.cn/problems/combination-sum/)

        把上面那道的backTrace(i+1)改成backTrace(i)即可。

  	

# ***2024.4.29打卡	Day 89***

1. 八股文一轮复习开始！
	- Java基础完成10个问题，Java基础部分完成。


2. Redis
	- 1篇。通过单Redis实例实现分布式锁进行并发空值，并能使用多Redis实例增加可靠性。

3. leetcode刷题：2题

	- [78. 子集](https://leetcode.cn/problems/subsets/)
	
		就是遍历所有组合，递归终止条件是now.size() >= nums.length
	
		```java
		public void dfs(int start) {
		    if (now.size() > nums.length)
		        return;
		
		    ans.add(new ArrayList<>(now));
		
		    for (int i = start; i < nums.length; i++) {
		        now.add(nums[i]);
		        dfs(i + 1);
		        now.remove(now.size() - 1);
		    }
		}
		```
	
	- [90. 子集 II](https://leetcode.cn/problems/subsets-ii/)
	
		在上一题的基础上先排序，然后`if(i > start && nums[i] == nums[i - 1])`去重。
	



# ***2024.4.30打卡	Day 90***

1. 八股文一轮复习
	- Java集合部分完成。


2. Redis
	- 1篇。Redis 事务具有一致性和隔离性（EXEC之前使用WATCH机制），不具备持久性，原子性在事务中的命令执行出错时不具有。

3. leetcode刷题：3题

	- [46. 全排列](https://leetcode.cn/problems/permutations/)
	
		增加 visit，避免重复访问同一个元素。
	
	- [47. 全排列 II](https://leetcode.cn/problems/permutations-ii/)
	
		没做出来。
	
		去重时必须满足`visit[i - 1] == false`，否则前面的已入now，遍历到这里时只是第一次，不会重复，而如果前一个没有入now，就要放进当前的数了，那么肯定后续放入前一个的时候会造成重复。
	
		```java
		for (int i = 0; i < nums.length; i++) {
		    if (!visit[i]) {
		        // 去重时必须满足`visit[i - 1] == false`
		        if (i > 0 && nums[i] == nums[i - 1] && visit[i - 1] == false) {
		            continue;
		        }
		        visit[i] = true;
		        now.add(nums[i]);
		        backTrace(start + 1);
		        visit[i] = false;
		        now.remove(now.size() - 1);
		    }
		}
		```
	
	- [51. N 皇后](https://leetcode.cn/problems/n-queens/)
	
		创建一个map，记录每个节点被皇后覆盖的次数，只有为零时才能进一步遍历。还要注意一下ans的add过程不要搞混了。
	
		```java
		public void backTrace(int row) {
		    if (row >= n) {
		        List<String> method = new ArrayList<>();
		        for (int i = 0; i < n; i++) {
		            StringBuffer buffer = new StringBuffer();
		            int col = now.get(i);
		            for(int j = 0; j < n; j++) {
		                if(j == col)
		                    buffer.append("Q");
		                else
		                    buffer.append(".");
		            }
		            method.add(new String(buffer));
		        }
		        ans.add(method);
		        return;
		    }
		
		    for (int col = 0; col < n; col++) {
		        if (map[row][col] == 0) {
		            putQueen(row, col);
		            now.add(col);
		            backTrace(row + 1);
		            removeQueen(row, col);
		            now.remove(now.size() - 1);
		        }
		    }
		}
		```
	



# ***2024.5.1打卡	Day 91***

1. 八股文一轮复习
	- JVM 垃圾回收部分完成。


2. Redis

	- 1篇。主从同步时的坑，异步复制的延后，过期键设置相对过期时间导致读取从库过期键，不合理设置搞崩服务器。

3. leetcode刷题：6题

	- [70. 爬楼梯](https://leetcode.cn/problems/climbing-stairs/)

		简单dp

	- [746. 使用最小花费爬楼梯](https://leetcode.cn/problems/min-cost-climbing-stairs/)

		注意楼顶是index=cost.length。

	- [198. 打家劫舍](https://leetcode.cn/problems/house-robber/)

		注意要分3种情况，一种是今天不抢`dp[i-1]`，一种是基于2天前的抢今天的`dp[i-2]+nums[i]`，还有就是基于3天前的抢今天的`dp[i-3]+nums[i]`，没有四天前的了，因为4天前的肯定可以多抢一次2天前的。

		```java
		for (int i = 2; i < nums.length; i++) {
		    dp[i] = Math.max(dp[i - 1], dp[i - 2] + nums[i]);
		    if (i > 2)
		        dp[i] = Math.max(dp[i], dp[i - 3] + nums[i]);
		}
		```

	- [62. 不同路径](https://leetcode.cn/problems/unique-paths/)

		简单dp

	- [64. 最小路径和](https://leetcode.cn/problems/minimum-path-sum/)

		简单dp

	- [63. 不同路径 II](https://leetcode.cn/problems/unique-paths-ii/)

		障碍物到达方式为0即可。



# ***2024.5.2打卡	Day 92***

1. 八股文一轮复习
	- JVM 部分完成。
2. java 并发专栏
	  - 5篇。包括 java 内存模型，互斥锁，死锁。
3. Redis 专栏
	  - 1篇。Redis 缓存、并发控制和事务的课后习题。
4. leetcode刷题：5题

	- 0-1背包问题

		```java
		dp[i][v]，表示第0~i个物品中，当背包大小为v时可以获得的最大价值
		
		    对于第i个物品，有两种选择：
		    1.不拿取该物品，则价值和0~(i-1)一样，dp[i][v] = dp[i - 1][v]
		    2.拿取该物品，则价值增加v[i]，dp[i][v] = dp[i - 1][v - w[i]] + v[i]
		
		    dp[i][v] = Math.max(dp[i - 1][v], dp[i - 1][v - w[i]] + v[i])
		
		    for(int i = 0; i < N; i++) { // 遍历物品
		        for(int j = 0; j < V; j++) { // 遍历背包大小
		            if(j >= w[i]) { // 当前背包大小大于第i个物品的大小
		                dp[i][v] = Math.max(dp[i - 1][j], dp[i - 1][j - w[i]] + v[i]);
		            } else { // 否则装不下
		                dp[i][v] = dp[i - 1][v];
		            }
		        }
		    }
		```

	- 完全背包问题：每件物品可以无限拿（隐含可那的上限是 `V / w[i]`）

		```java
		dp[i][v]，表示第0~i个物品中，当背包大小为v时可以获得的最大价值
		
		    对于第i个物品，有两种选择：
		    1.不拿取该物品，则价值和0~(i-1)一样，dp[i][v] = dp[i - 1][v]
		    2.拿取该物品，遍历拿取的数量，for(int k = 0; k * w[i] <= j; k++)
		    dp[i][v] = dp[i - 1][j - k * w[i]] + k * v[i]
		
		dp[i][v] = Math.max(dp[i - 1][v], dp[i][v] = dp[i - 1][j - k * w[i]] + k * v[i])
		
		    for(int i = 0; i < N; i++) { // 遍历物品
		        for(int j = 0; j < V; j++) { // 遍历背包大小
		            for(int k = 0; k * w[i] <= j; k++) {
		                dp[i][v] = Math.max(dp[i - 1][j], 
		                                    dp[i - 1][j - k * w[i]] + k * v[i])
		            }
		        }
		    }
		```

	- 多重背包问题：第i件物品有s[i]件

		```java
		for(int i = 0; i < N; i++) { // 遍历物品
		    for(int j = 0; j < V; j++) { // 遍历背包大小
		        for(int k = 0; k <= s[i] && k * w[i] <= j; k++) {
		            dp[i][v] = Math.max(dp[i - 1][j], 
		                                dp[i - 1][j - k * w[i]] + k * v[i])
		        }
		    }
		}
		```

	- [5. 最长回文子串](https://leetcode.cn/problems/longest-palindromic-substring/)

		`dp[begin][end] = (s.charAt(begin) == s.charAt(end) && dp[begin + 1][end - 1])`

	- [718. 最长重复子数组](https://leetcode.cn/problems/maximum-length-of-repeated-subarray/)

		没做出来。

		```java
		class Solution {
		    public int findLength(int[] nums1, int[] nums2) {
		        int n1 = nums1.length;
		        int n2 = nums2.length;
		
		        // 以i,j结尾的数组的最长子数组
		        int[][] dp = new int[n1 + 1][n2 + 1];
		        int max = 0;
		
		        for (int i = 1; i <= n1; i++) {
		            for (int j = 1; j <= n2; j++) {
		                if (nums1[i - 1] == nums2[j - 1]) {
		                    /*
		                    	1 2 3 4 5
		                    	1 2 6 4 7
		                    	在遍历到4时，因为前面的3 != 6，因此dp[i - 1][j - 1]=0
		                    	故最终结果dp[i][j] = dp[i - 1][j - 1] + 1 = 1，没有问题
		                    */
		                    dp[i][j] = dp[i - 1][j - 1] + 1;
		                    max = Math.max(max, dp[i][j]);
		                }
		            }
		        }
		
		        return max;
		    }
		}
		```

		

# ***2024.5.3打卡	Day 93***

1. java 并发专栏

	- 1篇。用synchronized，wait(), notifyAll()实现“等待-通知”机制优化循环等待。

2. Redis 专栏

	- 1篇。Redis 在秒杀场景下的应用。

3. leetcode刷题：3题

	- [300. 最长递增子序列](https://leetcode.cn/problems/longest-increasing-subsequence/)

		注意第i个数必须时递增子序列的结尾

		```java
		class Solution {
		    public int lengthOfLIS(int[] nums) {
		        // 以第i个数结尾的递增子序列的长度
		        // 注意第i个数必须时递增子序列的结尾
		        int[] dp = new int[nums.length];
		        int ans = 1;
		        for (int i = 0; i < nums.length; i++) {
		            int len = 1;
		            for (int j = i - 1; j >= 0; j--) { // 向前遍历
		                if (nums[j] < nums[i]) {
		                    len = Math.max(len, dp[j] + 1);
		                }
		            }
		            dp[i] = len;
		            ans = Math.max(ans, len);
		        }
		        return ans;
		    }
		}
		```

	- [72. 编辑距离](https://leetcode.cn/problems/edit-distance/)

		如果`word1.charAt(i - 1) != word2.charAt(j - 1)`，那么有三种策略。

		替换一个，`dp[i-1][j-1]+1`

		增加一个，增加的肯定是`words2.get(j)`这个元素，因此次数为当前以i结尾的words1到以j-1结尾的words2的步骤数+1，即`dp[i][j-1]+1`

		减少一个，减少之后当前为以i-1结尾的words1，次数为`dp[i-1][j]+1`

	- [10. 正则表达式匹配](https://leetcode.cn/problems/regular-expression-matching/)

		专门把星号拿出来讨论就可以，星号可以匹配0个，因此有`dp[0][i]=dp[0][i-2]`

		然后遍历时有星号，星号前一个不能匹配就`dp[i][j]=dp[i][j-2]`

		可以匹配就有匹配0个，1个，多个三种情况，其中匹配多个相当于`s.get(i)`被抵消掉了，当前的星号可以继续向前匹配`s.get(i-1)`，如果还能成功匹配`s.get(i-1)`，就会递归似的继续向前匹配。

		```java
		int len1 = s.length();
		int len2 = p.length();
		// 以j结尾的p，是否能匹配以i结尾的s
		boolean[][] dp = new boolean[len1 + 1][len2 + 1];
		dp[0][0] = true;// 长度都是0，因此肯定可以匹配
		// x*可以是0个x，因此dp[0][j]=d[0][j-2]
		for (int j = 2; j < len2; j++) {
		    // 以j结尾，则最后一个字符在j-1
		    if (p.charAt(j - 1) == '*') {
		        dp[0][j] = dp[0][j - 2];
		    }
		}
		
		// 遍历
		for (int i = 1; i <= len1; i++) {
		    for (int j = 1; j <= len2; j++) {
		        // 不是*
		        if (p.charAt(j - 1) != '*') {
		            // 匹配成功
		            if (p.charAt(j - 1) == '.' || p.charAt(j - 1) == s.charAt(i - 1)) {
		                dp[i][j] = dp[i - 1][j - 1];
		            } else {// 匹配失败
		                dp[i][j] = false;
		            }
		        } else {// 是*p.charAt(j-2)可以有0或n个
		            // 当前这个无法匹配，只能匹配0个
		            if (p.charAt(j - 2) != s.charAt(i - 1) && p.charAt(j - 2) != '.') {
		                dp[i][j] = dp[i][j - 2];
		            // p.charAt(j - 2) 可以匹配 s.charAt(i - 1)
		            } else {
		                dp[i][j] = dp[i][j - 2]/* 匹配0个 */
		                    || dp[i - 1][j - 2]/* 匹配一个 */
		                    || dp[i - 1][j];/* 匹配多个，相当于抵消s[i]，即继续匹配s.get(i-1) */
		            }
		        }
		    }
		}
		
		return dp[len1][len2];
		```




# ***2024.5.4打卡	Day 94***

1. java 并发专栏

	- 5篇。已完成前11篇基础篇。

2. 并发线程池视频

	- 完成4个。

3. leetcode刷题：5题

	- [122. 买卖股票的最佳时机 II](https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-ii/)

		今天有股票，则昨天有或者昨天没有今天买的

		今天没股票，则昨天无或者昨天有今天刚卖

	- [714. 买卖股票的最佳时机含手续费](https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-with-transaction-fee/)

		买入的时候多交一个fee

	- [121. 买卖股票的最佳时机](https://leetcode.cn/problems/best-time-to-buy-and-sell-stock/)

		分三种情况，没买过，现在持有，和已完成交易

	- [123. 买卖股票的最佳时机 III](https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-iii/)

		和上面那道类似，只不过分五种情况了

	- [188. 买卖股票的最佳时机 IV](https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-iv/)

		注意要初始化第0天的`dp[0][1+2*k]为-prices[i]`



# ***2024.5.5打卡	Day 95***

最近感觉有点没动力了。。。自己被很多没有意义的事消耗了精力，我必须把它们赶出自己的生活。

1. java 并发视频课

	- 全部完成

2. java 并发文章

	- 5篇。

3. leetcode刷题：2题

	- [309. 买卖股票的最佳时机含冷冻期](https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-with-cooldown/)

		```java
		for (int i = 1; i < prices.length; i++) {
		    dp[i][0] = Math.max(dp[i - 1][0], dp[i - 1][1] + prices[i]);
		    if (i == 1) {
		        dp[i][1] = Math.max(dp[i - 1][1], dp[i - 1][0] - prices[i]);
		        continue;
		    }
		    // dp[i][1]是否能从dp[i-1][0]状态买入呢？ 
		    // 从dp[i-2][0]状态买入是否会丢失dp[i-1][0]的信息？答案是不能，不会。
		    // dp[i-1][0]这个状态可能是当天卖出，i-1天前卖出且处于非冷冻期，i-1天前卖出且处于冷冻期三种可能。
		    // i-1当天卖出，或者处于冷冻期，第i天都不能买入
		    // 若i-1为非冷冻期， 那么i-2天也为非冷冻期。且这两天的状态一模一样。即dp[i-1][0] = dp[i-2][0]
		    dp[i][1] = Math.max(dp[i - 1][1], dp[i - 2][0] - prices[i]);
		}
		```

	- [322. 零钱兑换](https://leetcode.cn/problems/coin-change/)

		记得先把零钱及其整数倍进行初始化，先排序coins，这样大面值在后面，遇到公因数最终会取小值。



# ***2024.5.6打卡	Day 96***

1. java 并发

	- 剩余的文章完成
	
2. 设计模式：
	  - 完成Spring中的设计模式，单例模式，代理模式，工厂模式


2. leetcode刷题：4题

	- [160. 相交链表](https://leetcode.cn/problems/intersection-of-two-linked-lists/)
	
		left为null则遍历另一链表，right也同理，最后两链表必然相遇（可能在null处相遇）
	
	- [206. 反转链表](https://leetcode.cn/problems/reverse-linked-list/)
	
		递归
	
	- [234. 回文链表](https://leetcode.cn/problems/palindrome-linked-list/)
	
		O(1) 空间的方法：先找偏左中点，分割，把右半反转，然后比较两个链表。
	
	- [2. 两数相加](https://leetcode.cn/problems/add-two-numbers/)
	
		先把最低位加出来，标记是否进位，然后递归得到后面的加法结果，然后循环进位后面的结果，最后把进位后的后面结果和当前new出来的新节点拼接。



# ***2024.5.7打卡	Day 97***

1. 八股文一轮复习：

	设计模式+框架部分已完成

2. leetcode刷题：4题

	- [19. 删除链表的倒数第 N 个结点](https://leetcode.cn/problems/remove-nth-node-from-end-of-list/)

		快慢节点法，fast 快 n 个节点，它们都从 pre 开始遍历，最后 `slow.next = slow.next.next`。

	- [23. 合并 K 个升序链表](https://leetcode.cn/problems/merge-k-sorted-lists/)

		归并排序，最后 merge 一下左右的有序链表

	- [82. 删除排序链表中的重复元素 II](https://leetcode.cn/problems/remove-duplicates-from-sorted-list-ii/)

		使用递归求解时要分多种情况讨论，比如 postList == null(1,2,2 or 1,1,1)，head.val == postList.val(1,1,2) ，head.val == head.next.val(即head也要消除(1,1,1,2))，(1,2)，直接head.next = postList，return head。

	- [92. 反转链表 II](https://leetcode.cn/problems/reverse-linked-list-ii/)

		把反转 left ~ right 递归化简为反转前 n 个。`head.next = reverseBetween(head.next, left - 1, right - 1);` and when left == 1 is true : `return reverseN(head, right - left + 1);`。



# ***2024.5.8打卡	Day 98***

1. 八股文一轮复习：

	Mysql 刷了一些题，开始二刷 Mysql 45讲，看完了索引。

2. leetcode刷题：4题

	- [142. 环形链表 II](https://leetcode.cn/problems/linked-list-cycle-ii/)

		注意 Floyd 判圈法使用时，`fast = head.next.next, slow = head.next`，否则直接 break。

	- [143. 重排链表](https://leetcode.cn/problems/reorder-list/)

		非常综合的一道题。首先找中点分割链表，然后反转右半部分，之后将 left 和 right 按照规律进行合并，注意最后 head 的地址不能变，因此不能 new，只能改变指针指向。

	- [148. 排序链表](https://leetcode.cn/problems/sort-list/)

		归并思想，先将其找中点分割到最小，然后合并左右有序链表。

	- [25. K 个一组翻转链表](https://leetcode.cn/problems/reverse-nodes-in-k-group/)

		先求出链表总长，然后当前链表长度 >= k，则反转前n个节点，并且连接后续的递归结果。



# ***2024.5.9打卡	Day 99***

1. 八股文一轮复习：

	Mysql 看了锁。

2. leetcode刷题：8题

	- [94. 二叉树的中序遍历](https://leetcode.cn/problems/binary-tree-inorder-traversal/)

		借助栈

	- [101. 对称二叉树](https://leetcode.cn/problems/symmetric-tree/)

		借助一个 helper 函数，传入 root 的左右子树

	- [104. 二叉树的最大深度](https://leetcode.cn/problems/maximum-depth-of-binary-tree/)

		递归

	- [110. 平衡二叉树](https://leetcode.cn/problems/balanced-binary-tree/)

		借助一个 depth 函数辅助，直接自底向上一次遍历

	- [144. 二叉树的前序遍历](https://leetcode.cn/problems/binary-tree-preorder-traversal/)

		借助栈

	- [LCR 144. 翻转二叉树](https://leetcode.cn/problems/er-cha-shu-de-jing-xiang-lcof/)

		递归

	- [102. 二叉树的层序遍历](https://leetcode.cn/problems/binary-tree-level-order-traversal/)

		借助队列辅助

	- [105. 从前序与中序遍历序列构造二叉树](https://leetcode.cn/problems/construct-binary-tree-from-preorder-and-inorder-traversal/)

		没什么好说的，构造左右子树的 pre 和 in order




# ***2024.5.10打卡	Day 100***

1. 八股文一轮复习：

	Mysql 完成日志，事务，和 explain 命令的 type 字段

2. leetcode刷题：4题

	- [129. 求根节点到叶节点数字之和](https://leetcode.cn/problems/sum-root-to-leaf-numbers/)

		回溯

	- [145. 二叉树的后序遍历](https://leetcode.cn/problems/binary-tree-postorder-traversal/)

		注意 root != null 时，如果 root.left != null，则 root = root.left，否则 root = root.right，这样可以保证遍历到 root == null 时，栈顶的左右子树均被遍历了。

	- [199. 二叉树的右视图](https://leetcode.cn/problems/binary-tree-right-side-view/)

		法一：层次遍历

		法二：右侧就是先序遍历每一层被最后遍历的节点，可以先序遍历，然后传入层数，不断覆盖相应层数的值

	- [236. 二叉树的最近公共祖先](https://leetcode.cn/problems/lowest-common-ancestor-of-a-binary-tree/)

		法一：构造函数 hasNode，判断某一子树下有没有某个节点，然后分类

		法二：

		```java
		public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
		    if (root == null || root == p || root == q) return root;
		    TreeNode left = lowestCommonAncestor(root.left, p, q);
		    TreeNode right = lowestCommonAncestor(root.right, p, q);
		    if (left == null) return right;
		    if (right == null) return left;
		    else return root;
		}
		```




# ***2024.5.11打卡	Day 101***

1. 八股文一轮复习：

	- 完成 explain 的 extra 字段，空值的影响和 count(*) 的优化
	- 完成 redis 单线程，数据类型和持久化

2. leetcode 刷题：2题

	- [662. 二叉树最大宽度](https://leetcode.cn/problems/maximum-width-of-binary-tree/)

		采用完全二叉树和层次遍历，插入时额外加入节点的index，每一层数量>2则末index-初index

	- [124. 二叉树中的最大路径和](https://leetcode.cn/problems/binary-tree-maximum-path-sum/)

		```java
		public int maxGain(TreeNode root) {
		    if (root == null) return 0;
		    int left = Math.max(maxGain(root.left), 0); // 如果左子树贡献值小于0则不相连
		    int right = Math.max(maxGain(root.right), 0); // 如果右子树贡献值小于0则不相连
		    max = Math.max(max, root.val + left + right); // 当前root的路径可以与左右子树相连
		    return root.val + Math.max(left, right); // 最后只有一边可以与父节点的路径相连
		}
		```

	- [297. 二叉树的序列化与反序列化](https://leetcode.cn/problems/serialize-and-deserialize-binary-tree/)

		采用层次遍历序列化和反序列化，注意反序列化时遇到null什么都不用做，因为默认子树已经是null了

		```java
		public TreeNode deserialize(String data) {
		    if(data == null || data.length() <= 0)
		        return null;
		    String[] s = data.split(",");
		    if(s[0].equals("null"))
		        return null;
		    Queue<TreeNode> queue = new LinkedList<>();
		    TreeNode root = new TreeNode(Integer.valueOf(s[0]));
		    queue.add(root);
		    int i = 1;
		    while(!queue.isEmpty()){
		        TreeNode t = queue.poll();
		        // 构造 t 的左子树
		        if(!s[i].equals("null")) {
		            TreeNode left = new TreeNode(Integer.valueOf(s[i]));
		            t.left = left;
		            queue.add(left);
		        } // 是 null 则什么都不用做
		        i++;
		        // 构造 t 的右子树
		        if(!s[i].equals("null")) {
		            TreeNode right = new TreeNode(Integer.valueOf(s[i]));
		            t.right = right;
		            queue.add(right);
		        }
		        i++;
		    }
		    return root;
		}
		```




# ***2024.5.12打卡	Day 102***

1. 八股文一轮复习：

	- 完成 Redis 剩余部分

2. leetcode 刷题：2题

	- [56. 合并区间](https://leetcode.cn/problems/merge-intervals/)

		先将区间按照begin排序，然后不断更新当前"队尾"合并后的区间（前面有序，只用判断结尾大小，选更大的）

		```java
		List<int[]> merged = new ArrayList<int[]>();
		for (int i = 0; i < intervals.length; ++i) {
		    int L = intervals[i][0], R = intervals[i][1];
		    if(merged.isEmpty() || L > merged.get(merged.size() - 1)[1]) {
		        merged.add(new int[]{L, R});
		    } else {// 更新当前"队尾"合并后的区间（前面有序，只用判断结尾大小，选更大的）
		        merged.set(merged.size() - 1, 
		                   new int[]{merged.get(merged.size() - 1)[0],
		                             Math.max(merged.get(merged.size() - 1)[1], R)});
		    }
		}
		```

	- [215. 数组中的第K个最大元素](https://leetcode.cn/problems/kth-largest-element-in-an-array/)

		优先队列的应用，一直维护一个peek是第K大元素的优先队列，方法就是队列里只有K个元素。

		注意：PriorityQueue没有实现Deque接口，没有addFirst，addLast之类的方法！



# ***2024.5.13打卡	Day 103***

1. 八股文一轮复习：

	- 完成计算机网络14篇文章的复习。

2. leetcode 刷题：5题

	- [315. 计算右侧小于当前元素的个数](https://leetcode.cn/problems/count-of-smaller-numbers-after-self/)

		在归并排序统计个数

		```java
		class Solution {
		    List<Integer> ans = new ArrayList<>();
		
		    public List<Integer> countSmaller(int[] nums) {
		        if (nums.length == 1) {
		            ans.add(0);
		            return ans;
		        }
		
		        // 需要记录 index，以便在 ans 中进行定位
		        List<Pair<Integer/*num*/, Integer/*index*/>> list = new ArrayList<>();
		
		        // 传入初始值 (num, index)
		        for (int i = 0; i < nums.length; i++) {
		            list.add(new Pair<>(nums[i], i));
		        }
		
		        // ans 占位
		        for (int i = 0; i < nums.length; i++) {
		            ans.add(0);
		        }
		
		        merge(list);
		
		        return ans;
		    }
		
		    /**
		        在归并排序的过程中，不断更新每个位置右侧上比它小的数的个数
		    */
		    public List<Pair<Integer, Integer>> merge(List<Pair<Integer, Integer>> list) {
		        if (list.size() == 1) return list;
		
		        int mid = list.size() / 2;
		        // 此时left中的数已经将 left 内部比它们右侧小的数的个数统计完成了，故此时只需继续统计
		        // right 中比它们小的数的个数，加到 ans 对应位置中
		        List<Pair<Integer, Integer>> left = merge(list.subList(0, mid));
		        List<Pair<Integer, Integer>> right = merge(list.subList(mid, list.size()));
		
		        List<Pair<Integer, Integer>> merged = new ArrayList<>();
		        int leftIndex = 0, rightIndex = 0;
		        while (leftIndex < left.size() && rightIndex < right.size()) {
		            // 左侧的数比右侧的数大，右侧数增大，看看左侧数的极限
		            if (left.get(leftIndex).getKey() > right.get(rightIndex).getKey()) {
		                merged.add(right.get(rightIndex));
		                rightIndex++;
		            } else { // 之所以在右侧的数大于左侧的数时进行添加，是因为右侧第一个比左侧
		                     // 对应位置的数大的数的下标，就是右侧所有比左侧数小的个数，因此直接: 
		                     // ans.set(left.get(leftIndex).getValue,
		                     // 	ans.get(left.get(leftIndex).getValue) + rightIndex)
		                ans.set(left.get(leftIndex).getValue(),
		                        ans.get(left.get(leftIndex).getValue()) + rightIndex);
		                merged.add(left.get(leftIndex));
		                leftIndex++;
		            }
		        }
		
		        // 左侧剩下的数比右侧的所有数都要大，直接增加 right.size()
		        while (leftIndex < left.size()) {
		            ans.set(left.get(leftIndex).getValue(),
		                    ans.get(left.get(leftIndex).getValue()) + right.size());
		            merged.add(left.get(leftIndex));
		            leftIndex++;
		        }
		
		        while (rightIndex < right.size()) {
		            merged.add(right.get(rightIndex));
		            rightIndex++;
		        }
		
		        return merged;
		    }
		}
		```

	- [69. x 的平方根 ](https://leetcode.cn/problems/sqrtx/)

		由于最后向下取整，对应 right = mid - 1，因此要偏右中点，同时注意 mid*mid 会超出 Integer.MAX_VALUE，因此要用 long。

	- [367. 有效的完全平方数](https://leetcode.cn/problems/valid-perfect-square/)

		和上面拿到题几乎一样，就是变了个问法

	- [704. 二分查找](https://leetcode.cn/problems/binary-search/)

		注意如果是 [1,2] 最后 left == right 的时候会刚好出循环，最后要判断 nums[left] == target

	- [33. 搜索旋转排序数组](https://leetcode.cn/problems/search-in-rotated-sorted-array/)

		```java
		if(nums[mid] == target) {
		    return mid;
		} else if(nums[left] <= nums[mid]) { // [left, mid] 有序
		    // 注意此处必须 <=，因为是偏左中点，比如(1,3)，mid = 0时，[0,0]有序，但[0,1]不有序
		    if(nums[left] <= target && target < nums[mid]) { // target 在该有序范围内
		        right = mid;
		    } else {
		        left = mid + 1;
		    }
		} else { // [mid, right] 有序
		    if(nums[right] >= target && target > nums[mid]) { // target 在该有序范围内
		        left = mid + 1;
		    } else {
		        right = mid;
		    }
		}
		```




# ***2024.5.14打卡	Day 104***

1. 八股文一轮复习：

	- 完成计算机网络视频课物理层，数据链路层，网络层一半。

2. leetcode 刷题：5题

	- [34. 在排序数组中查找元素的第一个和最后一个位置](https://leetcode.cn/problems/find-first-and-last-position-of-element-in-sorted-array/)

		就是二分查找找到后外加向左向右遍历

	- [26. 删除有序数组中的重复项](https://leetcode.cn/problems/remove-duplicates-from-sorted-array/)

		双指针，left 表示不重复的数的位置，这样实现空间 O(1)

	- [88. 合并两个有序数组](https://leetcode.cn/problems/merge-sorted-array/)

		双指针

	- [11. 盛最多水的容器](https://leetcode.cn/problems/container-with-most-water/)

		双指针，判断当前left高于right还是right高于left

	- [15. 三数之和](https://leetcode.cn/problems/3sum/)

		first 和 third 遍历，动态生成 second，while(sum < 0 && second < third) second++，如果得到的值依然 <0，则 break



# ***2024.5.15打卡	Day 105***

1. 八股文一轮复习：

	- 完成计算机网络视频课剩余部分完成。

2. leetcode 刷题：6题

	- [287. 寻找重复数](https://leetcode.cn/problems/find-the-duplicate-number/)

		由于数的大小不超过 `nums.length - 1`，因此可以使用"负号"用于标记该数是否已经出现。

	- [20. 有效的括号](https://leetcode.cn/problems/valid-parentheses/)

		左括号直接入栈，右括号匹配对应的左括号

	- [155. 最小栈](https://leetcode.cn/problems/min-stack/)

		最小栈保存了当前位置下的最小值，即push的时候如果val<栈顶，则push val，否则push当前栈顶。

	- [225. 用队列实现栈](https://leetcode.cn/problems/implement-stack-using-queues/)

		```java
		public void push(int x) {
		    if (queue.isEmpty()) {
		        queue.offer(x);
		    } else {
		        while (!queue.isEmpty())
		            helper.offer(queue.poll());
		        queue.offer(x); // 新增的数据放在队头，模拟栈顶
		        while (!helper.isEmpty()) // 再把之前的数据顺序放回来
		            queue.offer(helper.poll());
		    }
		}
		```

	- [232. 用栈实现队列](https://leetcode.cn/problems/implement-queue-using-stacks/)

		```java
		private void checkLeft() {
		    if (left.isEmpty()) // 左栈为空时将右栈全部放过来
		    // (left peek)[]  [1,2,3](right peek) -> (left peek)[1,2,3]  [](right peek)
		        while (!right.isEmpty())
		            left.add(right.pop());
		}
		```

	- [239. 滑动窗口最大值](https://leetcode.cn/problems/sliding-window-maximum/)

		单调栈，储存index，按照nums[index]的大小递减以保持单调。

		```java
		Deque<Integer/* index */> deque = new LinkedList<>(); // 存储index，按nums[i]单调递减
		// 初始化第一个滑动窗口
		for (int i = 0; i < k; i++) {
		    while (!deque.isEmpty() && nums[deque.getLast()] <= nums[i])
		        deque.removeLast();
		    deque.addLast(i);
		}
		List<Integer> ans = new ArrayList<>();
		ans.add(nums[deque.getFirst()]);
		for (int i = k; i < nums.length; i++) {
		    while (!deque.isEmpty() && deque.getFirst() <= i - k)
		        deque.removeFirst();
		    while (!deque.isEmpty() && nums[deque.getLast()] <= nums[i])
		        deque.removeLast();
		    deque.addLast(i);
		    ans.add(nums[deque.getFirst()]);
		}
		```




# ***2024.5.16打卡	Day 106***

1. 八股文一轮复习：

	- 计算机网络面试题4题。

2. leetcode 刷题：5题

	- [LCR 126. 斐波那契数](https://leetcode.cn/problems/fei-bo-na-qi-shu-lie-lcof/)

		注意要递推，递归的复杂度为$O(2^n)$

	- [70. 爬楼梯](https://leetcode.cn/problems/climbing-stairs/)

		简单动态规划

	- [LCR 127. 跳跃训练](https://leetcode.cn/problems/qing-wa-tiao-tai-jie-wen-ti-lcof/)

		和上面那题一样

	- [121. 买卖股票的最佳时机](https://leetcode.cn/problems/best-time-to-buy-and-sell-stock/)

		动规则分三种状态

		递推就维护当前的min和income

	- [LCR 161. 连续天数的最高销售额](https://leetcode.cn/problems/lian-xu-zi-shu-zu-de-zui-da-he-lcof/)

		简单dp



# ***2024.5.17打卡	Day 107***

1. 八股文一轮复习：

	- 计算机网络面试题暂停一下，有点厌倦了。

2. leetcode 刷题：5题

	- [5. 最长回文子串](https://leetcode.cn/problems/longest-palindromic-substring/)

		`dp[i][j] = d[i+1][j-1] && s.charAt(i)==s.charAt(j)`

	- [62. 不同路径](https://leetcode.cn/problems/unique-paths/)

		简单dp

	- [64. 最小路径和](https://leetcode.cn/problems/minimum-path-sum/)

		`dp[i][j] = Math.max(dp[i-1][j], dp[i][j-1]) + grid[i][j]`

	- [198. 打家劫舍](https://leetcode.cn/problems/house-robber/)

		三种情况：

		1. 今天不抢，`dp[i-1]`
		2. 今天在前天的基础上抢，`dp[i-2]+nums[i]`
		3. 在大前天的基础上，可以抢今天，也可能抢的是昨天`dp[i-3]+Math.max(nums[i],nums[i-1])`
		4. 当然，`i-4` 天肯定可以白嫖 `i-2` 天，因此不用考虑

		```java
		dp[i] = Math.max(Math.max(dp[i-1], dp[i-2] + nums[i]), dp[i-3] + Math.max(nums[i], nums[i-1]));
		```

	- [221. 最大正方形](https://leetcode.cn/problems/maximal-square/)

		dp 代表以`(i,j)`为右下角的最大正方形边长，即右下角必须是1的正方形，这样便于寻找关系

		随后如果当前遍历的格子为1，则 `dp[i][j]` 为上、下、左上方最小值+1

		```java
		dp[i][j] = Math.min(Math.min(dp[i-1][j], dp[i][j-1]), dp[i-1][j-1]) + 1;
		```




# ***2024.5.18打卡	Day 108***

1. 八股文一轮复习：

	- 完成操作系统高频面试题。

2. leetcode 刷题：4题

	- [322. 零钱兑换](https://leetcode.cn/problems/coin-change/)

		可以先将硬币排序，这样大的在后面，在初始化的时候公因数会最终分配为大面额硬币，数量少。

	- [518. 零钱兑换 II](https://leetcode.cn/problems/coin-change-ii/)

		直接每一个面额遍历一次所有硬币加起来即可

	- [718. 最长重复子数组](https://leetcode.cn/problems/maximum-length-of-repeated-subarray/)

		```java
		// 以第i、j个数结尾的最长子数组长度
		int[][] dp = new int[len1 + 1][len2 + 1];
		int max = 0;
		for (int i = 1; i <= len1; i++) {
		    for (int j = 1; j <= len2; j++) {
		        if (nums1[i - 1] == nums2[j - 1]) {
		            dp[i][j] = dp[i - 1][j - 1] + 1;
		        } else {
		            // 已经初始化为0了，不用再赋为0了
		        }
		        max = Math.max(max, dp[i][j]);
		    }
		}
		return max;
		```

	- [300. 最长递增子序列](https://leetcode.cn/problems/longest-increasing-subsequence/)

		以第i个数结尾的递增子序列的长度：`int[] dp = new int[nums.length];`

		```java
		// 向前遍历
		for (int j = i - 1; j >= 0; j--) {
		    if (nums[j] < nums[i]) {
		        maxLen = Math.max(maxLen, dp[j] + 1);
		    }
		}
		```




# ***2024.5.19打卡	Day 109***

1. 今天主要在整学校里的东西，下周要考试。。。明天看Linux八股文。

2. 学校课程的需要，复习了电商项目的业务逻辑。

3. leetcode 刷题：3题

	- [42. 接雨水](https://leetcode.cn/problems/trapping-rain-water/)

		注意while循环的退出条件为 `left<=right` 而不是 `left < right`，因为右侧找到最高点时会 right-\-，此时 right 为最高点的左边一个，如果 `left < right` 的话这个点会被忽视。

		比如 `[5,5,1,7,1,1,5,2,7,6]` 中的2就会因为 left < right 的限制不会被统计，因为找到右边的7时，此时的 right 正好指向2而不是7。

		```java
		while(left <= right) {
		    while(left <= right && maxLeft <= maxRight) {
		        if(height[left] > maxLeft) {
		            maxLeft = height[left];
		        } else {
		            ans += maxLeft - height[left];
		        }
		        left++;
		    }
		    while(left <= right && maxRight < maxLeft) {
		        if(height[right] > maxRight) {
		            maxRight = height[right];
		        } else {
		            ans += maxRight - height[right];
		        }
		        right--;;
		    }
		}
		```

	- [72. 编辑距离](https://leetcode.cn/problems/edit-distance/)

		替换 `dp[i-1][j-1]`

		增加 `dp[i][j-1] + 1`，相当于现在都后面少一个的次数+1

		删除 `dp[i-1][j] + 1`，自己少一个到现在的次数+1

	- [10. 正则表达式匹配](https://leetcode.cn/problems/regular-expression-matching/)

		```java
		for (int j = 2; j < len2; j++) {
		    // 以j结尾，则最后一个字符在j-1
		    if (p.charAt(j - 1) == '*') {
		        dp[0][j] = dp[0][j - 2];
		    }
		}
		
		// 遍历
		for (int i = 1; i <= len1; i++) {
		    for (int j = 1; j <= len2; j++) {
		        // 不是*
		        if (p.charAt(j - 1) != '*') {
		            // 匹配成功
		            if (p.charAt(j - 1) == '.' || p.charAt(j - 1) == s.charAt(i - 1)) {
		                dp[i][j] = dp[i - 1][j - 1];
		            } else {// 匹配失败
		                dp[i][j] = false;
		            }
		        } else {// 是*p.charAt(j-2)可以有0或n个
		            // 当前这个无法匹配，只能匹配0个
		            if (p.charAt(j - 2) != s.charAt(i - 1) && p.charAt(j - 2) != '.') {
		                dp[i][j] = dp[i][j - 2];
		                // p.charAt(j - 2) 可以匹配 s.charAt(i - 1)
		            } else {
		                dp[i][j] = dp[i][j - 2]/* 匹配0个 */
		                    || dp[i - 1][j - 2]/* 匹配一个 */
		                    || dp[i - 1][j];/* 匹配多个，相当于抵消s[i]，即继续递归匹配s.get(i-1) */
		            }
		        }
		    }
		}
		```




# ***2024.5.20打卡	Day 110***

1. 今天主要在整学校的东西，准备考试+实验+大作业，上个大学怎么这么多事儿啊…

2. 八股文进度还在看 Linux 性能排查方面的问题。

3. leetcode刷题：

	- [455. 分发饼干](https://leetcode.cn/problems/assign-cookies/)

		排序，小饼干分给小孩子

	- [122. 买卖股票的最佳时机 II](https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-ii/)

		最经典买卖股票例子

	- [435. 无重叠区间](https://leetcode.cn/problems/non-overlapping-intervals/)

		相当于那道最多安排活动的题，难点是按照结束时间递增排序

		```java
		Arrays.sort(intervals, new Comparator<>(){ // 注意此处是<>()，而不仅仅是<>
		    @Override
		    public int compare(int[] o1, int[]o2) {
		        if(o1[1] != o2[1])
		            return o1[1] - o2[1];
		        else
		            return o1[0] - o2[0];
		    }
		});
		```

	- [714. 买卖股票的最佳时机含手续费](https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-with-transaction-fee/)

		在上面那道经典模板的基础上加一个fee

	- [921. 使括号有效的最少添加](https://leetcode.cn/problems/minimum-add-to-make-parentheses-valid/)

		经典括号匹配题，就是最后统计一下stack剩余的元素个数即可



# ***2024.5.21打卡	Day 111***

1. 八股文一轮复习：

	- 完成Linux系统性能排查和常用命令部分。

2. leetcode 刷题：3题

	- [401. 二进制手表](https://leetcode.cn/problems/binary-watch/)

		法一：枚举，使用 `Integer.bineryCount(int)` 获得当前数的二进制1的个数。

		法二：回溯

		```java
		// 把path当作一个二进制数，它的前四位和后六位分别表示时和分，index为10位按键的index
		private void backtrace(int path, int index, int len, int total/*还需按的键数*/, List<String> res) {
		    if(total == 0) {
		        String time = convert(path); // path 转时间函数
		        if(!"".equals(time))
		            res.add(time);
		        return;
		    }
		
		    if(index + total > len) // 肯定剩余的按键不够total了，直接返回
		        return;
		
		    // 当前index键不按
		    backtrace(path, index + 1, len, total, res);
		    // 当前index键按
		    path |= (1 << index);
		    backtrace(path, index + 1, len, total - 1, res);
		}
		```

	- [22. 括号生成](https://leetcode.cn/problems/generate-parentheses/)

		回溯函数包含两个参数，left(当前左括号个数) 和 right(当前右括号个数)

		如果当前左括号小于等于右括号，那么只能添加左括号

		否则既可以添加左括号，也能添加右括号

		注意 StringBuffer 的 api：deleteCharAt(int) 和 length() 和 delete(int start, int end)

		```java
		void backTrace(int left/* 左括号数 */, int right/* 右括号数 */) {
		    if (left >= n && right >= n) {
		        ans.add(new String(buffer));
		        return;
		    }
		
		    if (left <= right && left < n) {
		        buffer.append("(");
		        backTrace(left + 1, right);
		        buffer.deleteCharAt(buffer.length() - 1);
		    } else if (left > right && right < n) {
		        if (left < n) {
		            buffer.append("(");
		            backTrace(left + 1, right);
		            buffer.deleteCharAt(buffer.length() - 1);
		        }
		        buffer.append(")");
		        backTrace(left, right + 1);
		        buffer.deleteCharAt(buffer.length() - 1);
		    }
		}
		```

	- [39. 组合总和](https://leetcode.cn/problems/combination-sum/)

		排序原数组，然后去重，注意去重时要保证 `i > index`，防止第一个数被误跳过，导致缺失情况

		然后 `backTrace(i)` 即可实现单个数选取无数次



# ***2024.5.22打卡	Day 112***

1. 八股文一轮复习：

	- Linux 高频面试题完成，后续开始看 Kafka, Zookeeper, MQ, Docker, Nginx 等中间件，然后刷完一轮复习的消息队列八股文，并结束八股文一轮复习。八股文一轮复习结束后完善和部署项目，之后使用八股文突击题库进行二轮复习，完成后填写简历准备下学期日常实习的实战面试。

2. leetcode 刷题：5题

	- [46. 全排列](https://leetcode.cn/problems/permutations/)

		简单回溯，因为数组中的数字不重复，因此无需考虑组合的重复情况。

	- [90. 子集 II](https://leetcode.cn/problems/subsets-ii/)

		排序以去重，由于需要的是子集，因此只要 size < length 就把当前的 now 加入 ans，这样最初的 now = null 的结果即空集也会加入 ans，满足子集的需要。

		```java
		private void backTrace(int start) {
		    if (now.size() > nums.length)
		        return;
			// 只要 size < length 就把当前的 now 加入 ans，
		    // 这样最初的 now = null 的结果即空集也会加入 ans，满足子集的需要
		    ans.add(new ArrayList(now));
		
		    for(int i = start; i < nums.length; i++) {
		        if(i > start && nums[i] == nums[i - 1]) {
		            continue;
		        }
		        now.add(nums[i]);
		        backTrace(i + 1);
		        now.remove(now.size() - 1);
		    }
		}
		```

	- [93. 复原 IP 地址](https://leetcode.cn/problems/restore-ip-addresses/)

		```java
		public void backTrace(String s, int segId/* segments的index */, int segStart/* 本次遍历s的start */) {
		
		    // 如果找到了 4 段 IP 地址并且遍历完了字符串，那么就是一种答案
		    if (segId == SEG_COUNT) {
		        if (segStart == s.length()) {
		            StringBuffer buffer = new StringBuffer();
		            for (int i = 0; i < SEG_COUNT; ++i) {
		                buffer.append(segments[i]);
		                if (i != SEG_COUNT - 1) {
		                    buffer.append('.');
		                }
		            }
		            ans.add(buffer.toString());
		        }
		        return; // 找到4个数字直接return
		    }
		
		    // 如果还没有找到 4 段 IP 地址就已经遍历完了字符串，那么提前回溯
		    if (segStart >= s.length()) {
		        return;
		    }
		
		    // 由于不能有前导零，如果当前数字为 0，那么这一段 IP 地址只能为 0
		    if (s.charAt(segStart) == '0') {
		        segments[segId] = 0;
		        backTrace(s, segId + 1, segStart + 1);
		        return;
		    }
		
		    int num = 0;
		    for (int i = segStart; i < s.length(); i++) {
		        num = num * 10 + (int) (s.charAt(i) - '0');
		        if (num > 0 && num <= 255) {
		            segments[segId] = num;
		            backTrace(s, segId + 1, i + 1);
		        } else {
		            break; // 此时已经超出255，直接break回退到上一个IP数字
		        }
		    }
		}
		```

	- [216. 组合总和 III](https://leetcode.cn/problems/combination-sum-iii/)

		简单回溯，`backTrace(i+1)`

	- [51. N 皇后](https://leetcode.cn/problems/n-queens/)

		做了很多次了，本质就是一个map表示当前的格子有没有被其它皇后波及



# ***2024.5.23打卡	Day 113***

1. 八股文一轮复习：

	- Kafka视频课一部分。

2. leetcode 刷题：5题

	- [130. 被围绕的区域](https://leetcode.cn/problems/surrounded-regions/)

		没有用并查集，用的广度优先搜索，判断当前的“岛屿”是否在边上即可。

	- [200. 岛屿数量](https://leetcode.cn/problems/number-of-islands/)

		和上面那题类似，广度优先搜索。

	- [695. 岛屿的最大面积](https://leetcode.cn/problems/max-area-of-island/)
	
		同样类似的广度优先搜索



# ***2024.5.24打卡	Day 114***

1. 八股文一轮复习：

	- Kafka视频课一部分，以及消息队列面试题视频一部分。

2. leetcode 刷题：2题

	- [1. 两数之和](https://leetcode.cn/problems/two-sum/)

		不断用 map 记录当前遍历的元素。

	- [146. LRU 缓存](https://leetcode.cn/problems/lru-cache/)

		使用双向链表实现O(1)时间复杂度下的移动节点

		```java
		public int get(int key) {
		    if (map.containsKey(key)) {
		        moveToHead(map.get(key));
		        return map.get(key).value;
		    }
		    return -1;
		}
		
		public void put(int key, int value) {
		    if (map.containsKey(key)) {
		        map.get(key).value = value;
		        moveToHead(map.get(key));
		    } else {
		        if (size >= capacity) {
		            map.remove(tail.pre.key);
		            delete(tail.pre);
		            size--;
		        }
		        Node newNode = new Node(key, value);
		        map.put(key, newNode);
		        insertToHead(newNode);
		        size++;
		    }
		}
		```




# ***2024.5.25打卡	Day 115***

1. 八股文一轮复习：

	- 消息队列面试题视频一部分。

2. leetcode 刷题：

	- [460. LFU 缓存](https://leetcode.cn/problems/lfu-cache/)

		这道题好像就用不上双向链表了，只需保存KV以及该节点的cnt即可。

		```java
		private final Map<Integer/* key */, DLinkedNode/* specific node */> map
		    = new HashMap<>();
		private final Map<Integer/* count */, List<DLinkedNode>/* count list */> counter
		    = new HashMap<>();
		```

		这样一来，和 LRU 的区别就是 moveToHead 变为了 addCnt，而关于 counter 的修改则全部放在 addCnt 中。

		```java
		private void addCnt(DLinkedNode node) {
		    List<DLinkedNode> preNodeList = counter.get(node.cnt);
		    if (preNodeList != null)
		        preNodeList.remove(node);
		    if (minCnt == node.cnt && (preNodeList == null || preNodeList.size() == 0))
		        minCnt++; // 被删掉的是之前最小值集合的最后一个元素，因此minCnt要手动++了
		    else
		        minCnt = Math.min(minCnt, node.cnt + 1);
		    node.cnt++;
		    List<DLinkedNode> nodeList = counter.get(node.cnt);
		    if (nodeList == null)
		        nodeList = new ArrayList<>();
		    nodeList.add(node);
		    counter.put(node.cnt, nodeList);
		}
		```

	- [41. 缺失的第一个正数](https://leetcode.cn/problems/first-missing-positive/)
	
		第一个缺失的整数肯定为1~length+1，用负号标记被遍历的正数即可



# ***2024.5.26打卡	Day 116***

1. 八股文一轮复习：

	- 消息队列面试题视频完成。

2. leetcode 刷题：

	- [9. 回文数](https://leetcode.cn/problems/palindrome-number/)

		化成字符串，双指针。

	- [415. 字符串相加](https://leetcode.cn/problems/add-strings/)

		记录当前是否进位，最后利用 StringBuffer 的 reverse 函数将逆序的 buffer 翻转过来。

	- [8. 字符串转换整数 (atoi)](https://leetcode.cn/problems/string-to-integer-atoi/)

		注意消除前导空格和前导0，还有比如`word and 987`，结果是0而不是987，因为'w'不是正负号或数字

	- [31. 下一个排列](https://leetcode.cn/problems/next-permutation/)

		```java
		public void nextPermutation(int[] nums) {
		    int i = nums.length - 2;
		    while (i >= 0 && nums[i] >= nums[i + 1]) {
		        i--;
		    }
		    if (i >= 0) {
		        int j = nums.length - 1;
		        while (j >= 0 && nums[i] >= nums[j]) {
		            j--;
		        }
		        swap(nums, i, j); // 此时把最靠后的两个(小,大)swap为(大,小)
		    }
		    reverse(nums, i + 1); // 此时后面部分严格从大到小，把后面部分反转为从小到大
		}
		```




# ***2024.5.27打卡	Day 117***

1. 八股文一轮复习：

	- Kafka 视频课一部分。

2. leetcode 刷题：1题，总算搞懂了并查集

	- [721. 账户合并](https://leetcode.cn/problems/accounts-merge/)

		可以使用并查集，相同的email即可视为union。

		```java
		class UnionFind {
		    int[] parent;
		
		    public UnionFind(int n) {
		        parent = new int[n];
		        for (int i = 0; i < n; i++)
		            parent[i] = i; // 每个元素最开始没有连接的时候，都指向自己，表示独立
		    }
		
		    /*
		     * 联合，本质就是将它们各自唯一的根节点连接起来
		     */
		    public void union(int index1, int index2) {
		        parent[find(index2)] = find(index1); // index2 的根节点的父节点设置为 index1 的根节点
		    }
		
		    /*
		     * 查询 index 的根节点
		     */
		    public int find(int index) {
		        if (parent[index] != index) // 父节点不是自己，即自己不是根节点，查询父节点的根节点
		            return parent[index] = find(parent[index]);
		        else
		            return index;
		    }
		}
		```

		然后进行合并，排序去重

		```java
		class Solution {
		    public List<List<String>> accountsMerge(List<List<String>> accounts) {
		        int n = accounts.size();
		        // 创建并查集
		        UnionFind uf = new UnionFind(n);
		        // 便于我们做并查集的union
		        Map<String/* email */, Integer/* index of account */> map = new HashMap<>();
		
		        // 将并查集中的元素进行union
		        for (int index = 0; index < n; index++) {
		            List<String> account = accounts.get(index);
		            account = account.subList(1, account.size());
		            for (String email : account) {
		                if (!map.containsKey(email)) { // email 没有重复
		                    map.put(email, index);
		                } else { // email 出现重复，即需要合并
		                    uf.union(index, map.get(email));
		                }
		            }
		        }
		
		        // 合并相同账户
		        List<List<String>> ans = new ArrayList<>();
		        Map<Integer/* 根节点 */, Integer/* 在ans中的下标位置 */> mp = new HashMap<>();
		        for (int index = 0; index < n; index++) {
		            int rootIndex = uf.find(index);
		            if (!mp.containsKey(rootIndex)) {
		                List<String> account = new ArrayList<>(accounts.get(index));
		                mp.put(rootIndex, ans.size());
		                ans.add(account);
		            } else { // 已有相同根节点插入，当前账户和那个账户合并
		                List<String> oldAccount = ans.get(mp.get(rootIndex));
		                List<String> addAccount = new ArrayList<>(accounts.get(index));
		                for (int i = 1; i < addAccount.size(); i++)
		                    oldAccount.add(addAccount.get(i));
		            }
		        }
		
		        List<List<String>> res = new ArrayList<>();
		        // 排序并去重 email
		        for (List<String> account : ans) {
		            List<String> emails = account.subList(1, account.size());
		            emails.sort(new Comparator<String>() {
		                @Override
		                public int compare(String o1, String o2) {
		                    return o1.compareTo(o2);
		                }
		            });
		            List<String> newAccount = new ArrayList<>();
		            newAccount.add(account.get(0));
		            for (int i = 0; i < emails.size(); i++) {
		                if (i > 0 && emails.get(i).equals(emails.get(i - 1))) {
		                    continue;
		                } else {
		                    newAccount.add(emails.get(i));
		                }
		            }
		            res.add(newAccount);
		        }
		        return res;
		    }
		}
		```




# ***2024.5.28打卡	Day 118***

1. 八股文一轮复习：

	- Kafka 视频课完成

2. 数据结构：

	- 重学了不太清楚的堆排序和快速排序

3. leetcode 刷题：

	- [43. 字符串相乘](https://leetcode.cn/problems/multiply-strings/)

		采用的是每一位相乘，然后把结果相加的方法，可以说和上面的字符串加法相结合吧。

	- [189. 轮转数组](https://leetcode.cn/problems/rotate-array/)

		原地算法，部分反转数组三次。

		```java
		public void rotate(int[] nums, int k) {
		    if (k == 0)
		        return;
		    k %= nums.length;
		    reverse(nums, 0, nums.length - 1);
		    reverse(nums, 0, k - 1);
		    reverse(nums, k, nums.length - 1);
		}
		```

	- [238. 除自身以外数组的乘积](https://leetcode.cn/problems/product-of-array-except-self/)

		前缀和，先从左到右的前缀和，再记录从右到左的前缀和，最后把它们两个乘起来即可。



# ***2024.5.29打卡	Day 119***

1. 数据结构：

	- 重学了不太清楚的冒泡排序和希尔排序

2. leetcode 刷题：4题

	- [剑指 Offer 18. 删除链表的节点](https://leetcode.cn/problems/shan-chu-lian-biao-de-jie-dian-lcof/)

		pre 节点法

	- [剑指 Offer 22. 链表中倒数第k个节点](https://leetcode.cn/problems/lian-biao-zhong-dao-shu-di-kge-jie-dian-lcof/)

		fast, slow 指针法

	- [剑指 Offer 25. 合并两个排序的链表](https://leetcode.cn/problems/he-bing-liang-ge-pai-xu-de-lian-biao-lcof/)

		法一：while 循环，直到某一个遍历完

		法二：递归，设置一个 newNode 即可，稍微比递推慢一点，估计是往递归栈中插入和销毁栈帧占用了额外的时间

	- [剑指 Offer 52. 两个链表的第一个公共节点](https://leetcode.cn/problems/liang-ge-lian-biao-de-di-yi-ge-gong-gong-jie-dian-lcof/)

		拼接两个链表，注意 while 循环的退出条件是 `while(p1 != p2)`，这样如果没有公共节点它们会在 null 处相遇，然后退出返回 null



# ***2024.5.30打卡	Day 120***

1. 简历书写完成。明天开始完善项目。

2. leetcode 刷题：3题

	- [剑指 Offer 06. 从尾到头打印链表](https://leetcode.cn/problems/cong-wei-dao-tou-da-yin-lian-biao-lcof/)

		递归反转链表

	- [剑指 Offer 24. 反转链表](https://leetcode.cn/problems/fan-zhuan-lian-biao-lcof/)

		同上题

	- [剑指 Offer 35. 复杂链表的复制](https://leetcode.cn/problems/fu-za-lian-biao-de-fu-zhi-lcof/)

		分为三个部分：

		1. 创建新链表，新旧链表使用next相连，新链表在后面，便于设置random
		2. 设置新链表的random为旧链表.random.next
		3. 断开新旧链表



# ***2024.5.31打卡	Day 121***

1. 为完善项目正在学 Docker。

2. leetcode 刷题：3题

	- [字节真题：单链表相加](https://www.playoffer.cn/question/3483.html)

		先把逆序的数字链表反转，然后设置进位记录符，`while(num1 != null || num2 != null || jinwei)`，最后反转回来即可

	- [剑指 Offer 09. 用两个栈实现队列](https://leetcode.cn/problems/yong-liang-ge-zhan-shi-xian-dui-lie-lcof/)

		就是在delete时候，判断 left 是否为空，如果为空，则将 right 全部转移到 left

	- [225. 用队列实现栈](https://leetcode.cn/problems/implement-stack-using-queues/)

		```java
		public void push(int x) {
		    if (queue.isEmpty()) {
		        queue.offer(x);
		    } else {
		        while (!queue.isEmpty())
		            helper.offer(queue.poll());
		        queue.offer(x); // 新增的数据放在队头，模拟栈顶
		        while (!helper.isEmpty()) // 再把之前的数据顺序放回来
		            queue.offer(helper.poll());
		    }
		}
		```



# ***2024.6.1打卡	Day 122***

1. Docker视频课完成。

2. leetcode 刷题：2题

	- [剑指 Offer 30. 包含min函数的栈](https://leetcode.cn/problems/bao-han-minhan-shu-de-zhan-lcof/)

		最小栈不断维护其栈顶为最小的元素即可

	- [剑指 Offer 31. 栈的压入、弹出序列](https://leetcode.cn/problems/zhan-de-ya-ru-dan-chu-xu-lie-lcof/)

		用index记录当前需要出栈的元素，如果当前需要入栈的元素就是当前需要出栈的元素，则它不入栈，而是index++，并且此时要遍历以及入栈的元素是否符合当前继续出栈的要求（这一点很关键）

		```java
		class Solution {
		    public boolean validateBookSequences(int[] putIn, int[] takeOut) {
		        Stack<Integer> stack = new Stack<>();
		        int len = putIn.length;
		        int index = 0;
		        for (int i = 0; i < len; i++) {
		            if (putIn[i] != takeOut[index])
		                stack.add(putIn[i]);
		            else // 如果当前需要入栈的元素就是当前需要出栈的元素，则它不入栈，而是index++
		                index++;
		            // 此时要遍历以及入栈的元素是否符合当前继续出栈的要求（这一点很关键）
		            while(!stack.isEmpty() && stack.peek() == takeOut[index]) {
		                stack.pop();
		                index++;
		            }
		        }
		        while (!stack.isEmpty()) { // 最后遍历完了之后，剩下的元素必须按照剩下的takeOut的顺序出栈，不满足则返回false
		            if (stack.peek() == takeOut[index]) {
		                stack.pop();
		                index++;
		            } else {
		                return false;
		            }
		        }
		        // 最后必须栈空（刚好全部出栈），且出栈序列遍历完才行
		        return (stack.isEmpty() && index == len) ? true : false;
		    }
		}
		```

		

# ***2024.6.2打卡	Day 123***

1. Nginx视频课完成。

2. leetcode 刷题：5题

	- [剑指 Offer 55 – I. 二叉树的深度](https://leetcode.cn/problems/er-cha-shu-de-shen-du-lcof/)

		递归

	- [剑指 Offer 55 – II. 平衡二叉树](https://leetcode.cn/problems/ping-heng-er-cha-shu-lcof/)

		可以使用辅助函数depth内部判断维护balanced，这样就可以一次搜索完成判断，否则就要调用depth两次（left和right各一次）才能完成判断。

	- [剑指 Offer 27. 二叉树的镜像](https://leetcode.cn/problems/er-cha-shu-de-jing-xiang-lcof)

		简单递归

	- [剑指 Offer 28. 对称的二叉树](https://leetcode.cn/problems/dui-cheng-de-er-cha-shu-lcof)

		使用辅助函数helper，传入left和right作为参数，方便进行逻辑判断

	- [剑指 Offer 26. 树的子结构](https://leetcode.cn/problems/shu-de-zi-jie-gou-lcof)

		首先A和B都不能是null

		然后要么A的左子树中有B作为子树，要么A的右子树中有，如果A和B的val相同，那么就可以借助辅助函数判断当前的B是否"属于"A

		```java
		class Solution {
		    public boolean isSubStructure(TreeNode A, TreeNode B) {
		        if(A == null || B == null)
		            return false;
		        return recur(A, B) || // A.val == B.val，直接判断B是否属于A
		            // 判断B左右子树中是否在A的左右子树中
		            isSubStructure(A.left, B) || isSubStructure(A.right, B);
		    }
		    // 判断B左右子树中是否在A的左右子树中
		    public boolean recur(TreeNode A,TreeNode B){
		        if(B == null) 
		            return true;
		        if(A == null || A.val != B.val) 
		            return false;
		        return recur(A.left, B.left) && recur(A.right, B.right);
		    }
		}
		```




# ***2024.6.3打卡	Day 124***

1. 今天尝试去完善项目，但是好多地方都记不清了，就不得不查之前的笔记，听之前的课，感觉没做出来什么。

2. leetcode 刷题：3题

	- [剑指 Offer 32 – I. 从上到下打印二叉树](https://leetcode.cn/problems/cong-shang-dao-xia-da-yin-er-cha-shu-lcof)

		经典层次遍历

	- [剑指 Offer 32 – II. 从上到下打印二叉树](https://leetcode.cn/problems/cong-shang-dao-xia-da-yin-er-cha-shu-ii-lcof)

		同上

	- [剑指 Offer 32 – III. 从上到下打印二叉树](https://leetcode.cn/problems/cong-shang-dao-xia-da-yin-er-cha-shu-iii-lcof)

		记录一下当前的行为奇数还是偶数即可



# ***2024.6.4打卡	Day 125***

1. 在学完docker，nginx，MQ，Linux基础之后成功将电商支付项目部署到虚拟机上，可惜前端代码写死了，导致有些功能使用不了，很可惜。

2. leetcode 刷题：

	- [剑指 Offer 07. 重建二叉树](https://leetcode.cn/problems/zhong-jian-er-cha-shu-lcof/)

		递归，`Arrays.copyOfRange(int[], int, int)`

	- [剑指 Offer 33. 二叉搜索树的后序遍历序列](https://leetcode.cn/problems/er-cha-sou-suo-shu-de-hou-xu-bian-li-xu-lie-lcof/)

		没做出来

		思路：如果是二叉搜索树，那么根节点右侧的节点都>=根节点的值
		
		如果存在右子树中小于根节点的节点，那么肯定就不是二叉搜索树
		
		因此就要确定右子树的范围
		
		如果找到一个>=根节点的值，由于后序遍历的顺序是【左子树，右子树，根节点】，因此该节点属于右子树，其右边的值都应该>=根节点的值，如果存在<根节点的节点，就肯定不是二叉搜索树
		
		最后递归（还不清楚其合理性和意义）
		
		```java
		class Solution {
		    public boolean verifyPostorder(int[] postorder) {
		        if (postorder == null) {
		            return true;
		        }
		
		        return f(postorder, 0, postorder.length - 1);
		    }
		
		    private boolean f(int[] postorder, int i, int j) {
		        if (i >= j) {
		            return true;
		        }
		
		        int root = postorder[j];
		        int p = i;
		        // 获取第一个大于或者等于 root 等元素的位置
		        while (postorder[p] < root)
		            p++;
		        // 判断 p ~ j -1 这个范围是否存在小于root的元素
		        for (int k = p; k < j; k++) {
		            if (postorder[k] < root) {
		                return false;
		            }
		        }
		
		        return f(postorder, i, p - 1) && f(postorder, p, j - 1);
		    }
		}
		```
		



# ***2024.6.5打卡	Day 126***

1. 把电商支付项目的pay模块和mall都成功部署，并且使用nginx的反向代理将前端代码中和我们实际部署中不同的接口请求（比如192.xxx.xxx.xxx/api/products）进行映射（比如将/api/(注意要加右边的反斜线)映射到127.0.0.1:8080/products/），并且在`/etc/systemd/system`中创建`mall.service`和`pay.service`将运行jar包的命令服务化，可以用`systemctl`直接启动。

2. 完成rabbitmq入门视频。

3. leetcode 刷题：3题

	- [剑指 Offer 54. 二叉搜索树的第k大节点](https://leetcode.cn/problems/er-cha-sou-suo-shu-de-di-kda-jie-dian-lcof/)

		与中序遍历的非递归版本相结合即可

	- [剑指 Offer 68 – I. 二叉搜索树的最近公共祖先](https://leetcode.cn/problems/er-cha-sou-suo-shu-de-zui-jin-gong-gong-zu-xian-lcof/)

		由于是二叉搜索树，因此可以根据p和q的val大小判断其所在的子树，都在某一侧或分布在两侧

	- [剑指 Offer 37. 序列化二叉树](https://leetcode.cn/problems/xu-lie-hua-er-cha-shu-lcof/)

		使用层次遍历序列化和反序列化，序列化很好理解，不为null就queue.offer其左右子树，为null就不加入queue，只buffer.append("null,")；而反序列化相对复杂一点，首先split(",")，然后queue中存放创建的新节点，首先创建root节点，然后类似地poll，注意这里的queue只需offer左右子树中不为Null的即可，因为本来新创建的节点默认子树为Null，并且加入的队列的节点都要调用.left，.right，如果节点是null的话显然会空指针异常。



# ***2024.6.6打卡	Day 127***

1. 完成 Kafka 入门视频。明天部署社区项目。

2. leetcode 刷题：2题

	- [543. 二叉树的直径](https://leetcode.cn/problems/diameter-of-binary-tree/)

		在depth函数中获取ans，ans=left+right

	- [199. 二叉树的右视图](https://leetcode.cn/problems/binary-tree-right-side-view/)

		法一：层次遍历，每层的最右节点

		法二：先序遍历，记录层号，每层的最后一个元素（不断覆盖即可）



# ***2024.6.7打卡	Day 128***

1. 感觉今天没做什么，就只把社区项目部署工作的环境准备了一下。

2. leetcode 刷题：6题

	- 二叉搜索的四个模板

	- [剑指 Offer 53 – I. 在排序数组中查找](https://leetcode.cn/problems/zai-pai-xu-shu-zu-zhong-cha-zhao-shu-zi-lcof)

		注意最后出循环的时候可能刚刚为target的位置，因此最后的return要用三目运算

	- [剑指 Offer 53 – II. 0～n-1中缺失的数字](https://leetcode.cn/problems/que-shi-de-shu-zi-lcof)

		法一：以下标的负号标记

		法二：因为这里的数和index是一一对应的，因此可以直接二分搜索，本质就是找第一个不满足的index。



# ***2024.6.8打卡	Day 129***

1. 社区项目部署完成，这次是在服务器上安装tomcat，打包成war包放到tomcat上运行，相比于打包成jar包，需要额外安装tomcat，但是好处是可以将源码传过去，然后在服务器上mvn clean pakage -Dmaven.test.skip=true打包，这样可以直接使用服务器上的jdk环境，避免开发和生产环境不一样，导致开发环境打的包在生产环境上用不了。

2. leetcode 刷题：3题

	- [剑指 Offer 11. 旋转数组的最小数字](https://leetcode.cn/problems/xuan-zhuan-shu-zu-de-zui-xiao-shu-zi-lcof)

		```java
		public int stockManagement(int[] numbers) {
		    // O（n)
		    // logN O1
		    int l = 0;
		    int r = numbers.length - 1;
		    while (l < r) {
		        // 当出现中点在右半边等于最左侧数的情况时l++，
		        // 比如[10,1,10,10,10]，第一次判断后l++，此时[1,10,10,10]完全有序直接返回1
		        // 或者l=mid+1后刚好全部有序，这时如果有序则直接返回有序
		        // 比如在[4,5,8,3,4]中，8>4，l=mid+1，左边直接跑到3的位置，此时[3,4]已经有序
		        // 由于之前的[l,mid]有序，而[mid+1,r]也有序，因此mid+1为ans
		        if (numbers[l] < numbers[r]) {
		            return numbers[l];
		        }
		
		        int mid = (r + l) / 2;
		        if (numbers[mid] > numbers[l]) {
		            l = mid + 1;
		        } else if (numbers[mid] < numbers[l]) {
		            r = mid;
		        } else {
		            l++;
		        }
		    }
		
		    return numbers[l];
		}
		
		```

	- [剑指 Offer 15. 二进制中1的个数](https://leetcode.cn/problems/er-jin-zhi-zhong-1de-ge-shu-lcof)

		```java
		while(n != 0) {
		    n &= (n - 1);
		    ans++;
		}
		```

		比如n=110，最后一位已经是零，不管n-1是多少，最后一位无论怎么&都会是0

		而n-1相比于n，n的最后一个不为0的尾数肯定会变成0，则这最后一位变成0

		因此&的次数就是"1"的个数

	- [剑指 Offer 16. 数值的整数次方](https://leetcode.cn/problems/shu-zhi-de-zheng-shu-ci-fang-lcof)

		快速幂

		```java
		while(n != 0) {
		    if((n & 1) != 0)
		        ans *= x;
		    x *= x;
		    n >>>= 1; // 全部位都右移，其实也没必要，毕竟取了绝对值
		}
		```




# ***2024.6.9打卡	Day 130***

1. 今天主要准备学校的考试，然后看了一下java8新特性。

2. leetcode 刷题：2题

	- [剑指 Offer 56 – I. 数组中数字出现的次数](https://leetcode.cn/problems/shu-zu-zhong-shu-zi-chu-xian-de-ci-shu-lcof/)

		通过两个数亦或的最后一个不为0的位，将原数组分为两部分，然后再亦或

	- [剑指 Offer 56 – II. 数组中数字出现的次数 II](https://leetcode.cn/problems/shu-zu-zhong-shu-zi-chu-xian-de-ci-shu-ii-lcof/)

		遍历32次，每次一个比特位，找出每个比特位为1出现的次数，然后%3不为0则说明有这一位



# ***2024.6.10打卡	Day 131***

1. 今天主要在整课程的论文，没怎么看八股文。

2. leetcode 刷题：3题

	- [剑指 Offer 14- I. 剪绳子](https://leetcode.cn/problems/jian-sheng-zi-lcof)

		尽量分割成长度为3的小段可以使得积最大

		可以简单枚举+说明，如果长度是2，继续分反而变小；如果长度是3，继续分同样会变小；如果长度是4的话，继续分割积为4，没有变小，因此最小长度选择3，可以让段数更多。

		```java
		public int cuttingBamboo(int bamboo_len) {
		    if(bamboo_len == 2)
		        return 1;
		    if(bamboo_len == 3)
		        return 2;
		    int cnt = bamboo_len / 3;
		    int flag = bamboo_len % 3;
		    if(flag == 0)
		        return (int)Math.pow(3, cnt);
		    if(flag == 1)
		        return (int)Math.pow(3, cnt - 1) * 4;
		    if(flag == 2)
		        return (int)Math.pow(3, cnt) * 2;
		    return -1;
		}
		```

	- [剑指 Offer 14- II. 剪绳子 II](https://leetcode.cn/problems/jian-sheng-zi-ii-lcof)

		修改pow方法，注意返回值必须是long类型，否则会产生强制类型转换（long -> int）带来的误差

		```java
		long pow(int a, int n) {
		    long res = 1;
		
		    for (int i = 1; i <= n; i++) {
		        res = res * a % p; // 每乘一项%一次和得出最后结果再%的结果是一样的
		    }
		
		    return res;
		}
		```

	- [剑指 Offer 43. 1～n 整数中 1 出现的次数](https://leetcode.cn/problems/1nzheng-shu-zhong-1chu-xian-de-ci-shu-lcof/)

		```java
		// 几个变量计算：cur = (n/bit)%10, low = n % bit, high = n / bit / 10
		// 几个公示
		// cur > 1 => (high + 1) * bit
		// cur == 1 => (high * bit) + (1 + low)
		// cur = 0 => high * bit
		
		long bit = 1;
		long sum = 0;
		while (bit <= n) { // 思路，bit增大后，n / bit，这样不断缩小n进行归约
		    long cur = (n / bit) % 10;
		    long low = n % bit;
		    long high = n / bit / 10;
		
		    if (cur > 1) {
		        sum += (high + 1) * bit;
		    } else if (cur == 1) {
		        sum += (high * bit) + (1 + low);
		    } else {
		        sum += high * bit;
		    }
		    bit = bit * 10;
		}
		```




# ***2024.6.11打卡	Day 132***

1. 今天还在整课程的论文，以及复习考试，没怎么看八股文。

2. leetcode 刷题：1题

	- [剑指 Offer 44. 数字序列中某一位的数字](https://leetcode.cn/problems/shu-zi-xu-lie-zhong-mou-yi-wei-de-shu-zi-lcof/)

		```java
		public int findKthNumber(int n) {
		    if (n == 0) {
		        return 0;
		    }
		
		    long bit = 1;
		    int i = 1;
		    long count = 9;
		
		    while (count < n) {
		        n = (int) (n - count);
		        bit = bit * 10;
		        i++;
		        count = bit * i * 9;
		    }
		    // 确定是在这个区间的哪个数
		    long num = bit + (n - 1) / i;
		    // 确定在 Num 的那个字符
		    int index = (n - 1) % i + 1;
		    int res = (int) (num / Math.pow(10, i - index)) % 10;
		
		    return res;
		}
		```



# ***2024.6.12打卡	Day 133***

1. 今天论文写完了，然后复习快要考试的《数据库原理与设计》。这么课讲得比较系统，和训练营Mysql课程的区别是它涵盖的面特别广，但是很多都讲得不深入。复习这门课的同时也在回顾当初Mysql原理课程的笔记，可以有助于我理解一些当初觉得很突兀的概念和原理。

2. leetcode 刷题：2题

	- [剑指 Offer 39. 数组中出现次数超过一半的数字](https://leetcode.cn/problems/shu-zu-zhong-chu-xian-ci-shu-chao-guo-yi-ban-de-shu-zi-lcof/)

		摩尔投票法

		```java
		int ans = stock[0];
		int num = 0;
		for (int i = 0; i < stock.length; i++) {
		    if (stock[i] == ans)
		        num++;
		    else
		        num--;
		    if (num < 0) {
		        ans = stock[i]; // 小于0后，产生新的ans
		        num = 1; // 新的ans的记数为1
		    }
		}
		```

	- [剑指 Offer 61. 扑克牌中的顺子](https://leetcode.cn/problems/bu-ke-pai-zhong-de-shun-zi-lcof/)

		先排序，遇到0就continue，然后如果出现非0重复的数就false，最后return非0数max-min<5



# ***2024.6.13打卡	Day 134***

1. 今天把学校数据库原理课程看完了。

2. leetcode 刷题：2题

	- [剑指 Offer 57 – II. 和为s的连续正数序列](https://leetcode.cn/problems/he-wei-sde-lian-xu-zheng-shu-xu-lie-lcof/)

		经典滑动窗口，sum<target，++right；sum>target，left++。

	- [剑指 Offer 59 – I. 滑动窗口的最大值](https://leetcode.cn/problems/hua-dong-chuang-kou-de-zui-da-zhi-lcof/)

		老生常谈

		```java
		LinkedList<Integer/* 存放index，要求heights[index]递减 */> deque = new LinkedList<>();
		```

		注意编译类型要为LinkList，否则使用不了getFirst, addLast, removeLast 等 api。



# ***2024.6.14打卡	Day 135***

1. 今天状态奇差无比，期末太浮躁了，又看不进去八股文，又看不进去学校的课，浑浑噩噩的一天。

2. leetcode 刷题：2题

	- [剑指 Offer 21. 调整数组顺序使奇数位于偶数前面](https://leetcode.cn/problems/diao-zheng-shu-zu-shun-xu-shi-qi-shu-wei-yu-ou-shu-qian-mian-lcof)

		可以使用类似于二路快速排序的patition的方法解决此题：

		```java
		int left = 0, right = actions.length - 1;
		while(left < right) {
		    // 左指针找到第一个偶数
		    while(left < right && actions[left] % 2 == 1)
		        left++;
		    // 右指针找到第一个奇数
		    while(left < right && actions[right] % 2 == 0)
		        right--;
		    if(left < right) {
		        swap(actions, left, right);
		        left++;
		        right--;
		    }
		}
		```

	- [剑指 Offer 45. 把数组排成最小的数](https://leetcode.cn/problems/ba-shu-zu-pai-cheng-zui-xiao-de-shu-lcof/)

		这道题的难点在于Comparator怎么定义

		比如遇到3和30，如果直接用String.compareTo的话，会把3排在前面

		但是330>303，这样反而排反了。究其原因是字符串长度不一样。

		那就可以考虑将字符串长度变成一样再来compareTo。

		`return (s1 + s2).compareTo(s2 + s1);`，这样拼接之后长度一致，并且符合题意的拼接思路。

		再看3和30的例子，3+30=330，30+3=303，这样就会把303排到前面去

		```java
		Arrays.sort(arr, new Comparator<String>() {
		    @Override
		    public int compare(String s1, String s2) {
		        return (s1 + s2).compareTo(s2 + s1);
		    }
		});
		```




# ***2024.6.15打卡	Day 136***

1. 今天看了Java并发专题线程安全的八股文。

2. leetcode 刷题：3题

	- [剑指 Offer 40. 最小的k个数](https://leetcode.cn/problems/zui-xiao-de-kge-shu-lcof/)

		可以使用最大堆维护最小的k个数。

		也可以直接使用快速排序，如果一次交换后的基准点左侧恰有k个数，那么它们就是最小的k个数。

		```java
		public int[] quickSort(int[] stock, int cnt, int l, int r) {
		    int i = l;
		    int j = r;
		
		    // 以左边界作为哨兵，二路快速排序
		    while(i < j) {
		        while(i < j && stock[j] >= stock[l]) j--;
		        while(i < j && stock[i] <= stock[l]) i++;
		        swap(stock, i, j);
		    }
		    // 把哨兵放到两段中间位置,跳出while循环的时候，i==j
		    swap(stock, i, l);
		    
		    if(i > cnt) return quickSort(stock, cnt, l, i-1);
		    if(i < cnt) return quickSort(stock, cnt, i+1, r);
		    return Arrays.copyOf(stock, cnt);
		
		}
		```

	- [剑指 Offer 41. 数据流中的中位数](https://leetcode.cn/problems/shu-ju-liu-zhong-de-zhong-wei-shu-lcof/)

		维护两个堆，left为最大堆，right为最小堆

		```java
		public void addNum(int num) {
		    if (left.size() == right.size()) { // 让左边变多
		        if (left.isEmpty()) { // 防止后序 peek 空指针异常
		            left.add(num);
		        } else { // 此时右侧==左侧==不为空
		            if (num > right.peek()) { // 比右侧最小的大，添加到右侧
		                left.add(right.poll());
		                right.add(num);
		            } else { // 直接添加到左侧
		                left.add(num);
		            }
		        }
		    } else { // 让右边变多
		        if (num < left.peek()) { // 这里不用考虑空指针，因为left必定不为空
		            right.add(left.poll());
		            left.add(num);
		        } else {
		            right.add(num);
		        }
		    }
		}
		```

	- [剑指 Offer 51. 数组中的逆序对](https://leetcode.cn/problems/shu-zu-zhong-de-ni-xu-dui-lcof/)

		直接使用冒泡排序会超时，因此使用归并排序，在归并的时候顺便统计逆序对

		```java
		while (i < left.length && j < right.length) {
		    if (left[i] <= right[j]) {
		        res[index++] = left[i++];
		    } else { // 此时右边的比左边的小，出现逆序对，如[7,9] [4,5,6]，遍历到4就产生两个逆序对
		        ans += (left.length - i);
		        res[index++] = right[j++];
		    }
		}
		```

		
