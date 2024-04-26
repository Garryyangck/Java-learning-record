# 算法系统课

## 二分查找

### 704 二分查找

就是简单套模板，注意取mid时左右偏移（至少我是这么叫的）

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

### 69 x的平方根

也是二分查找，但是注意：

```java
long long mid = (long long)(left) + (long long)(right - left + 1) / 2;
```

`right - left + 1`可能超INT_MAX，因此都用long long

本题除了二分查找还可用牛顿迭代法计算f(x) = $x^{\frac{1}{2}}$，数值分析没白学



## 双指针

### 27 移除元素

双指针，因为题目说了不用管后面的部分，那么后面的部分就放值为val元素

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

### 11 盛最多水的容器

双指针。之前做过一个接雨水，这个和它思路差不多，就不多赘述了

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

### 26.删除有序数组中的重复项

双指针

```java
while (right < num) {
    if (nums[right - 1] != nums[right]) {
    	nums[++left] = nums[right];//此处需用赋值运算符，不能交换，会对后续的判断会有影响
    }
    right++;
}
```

### 209.长度最小的子数组

双指针

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



## 链表

### 203.删除链表结点

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

### 876.链表的中间结点

快慢指针法

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

### 19.删除链表倒数第n个结点

快慢指针，快指针快n个结点

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

### 160.相交链表

双指针，一个指针指到nullptr时切换到另一链表的head，最终两指针相许处即为结果

```java
while (iter1 != iter2) {
    iter1 = iter1 == nullptr ? headB : iter1->next;
    iter2 = iter2 == nullptr ? headA : iter2->next;
}
```

### 206.反转链表

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

### 92.链表反转II

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

### 25.K个一组反转链表

前n个结点反序的递归调用版

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

### 141.环形链表

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

### 142.环形链表II

快慢指针，相遇时head有引入一个指针，最终和slow在目标节点处相遇

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

### 21.合并两个有序链表

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

### 61.旋转链表

转化成向右移动一位的递归，注意特殊情况！

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

### 148.排序链表

一般排序法超时，采用归并算法

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

### 382.链表随机结点

rand()函数

### 138.随机链表的复制

没做出来

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

### 146.LRU缓存

没做出来，没想到双向链表，直接超时了

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



## 栈与队列

### 232.用栈实现队列

就是把两个栈从栈底拼接到一起，数据先存入inStack，

getTop时若outStack为空，则把inStack中所有元素移到outStack

### 225.用队列实现栈

尝试把队列反过来，push时全出队，加入x，再把出队的拿回来

### 20.有效的括号

做了很多遍了…

### 150.逆波兰表达式求值

易，远不如中序转后序再求值

这里说一下中序转后序的思路：

1. 数字直接入栈
2. 符号有优先级，乘除 > 加减
3. 遇到运算符，则一直出栈，直到栈空 或 栈顶的优先级小于它本身
4. 遇到左括号直接入栈
5. 遇到右括号一直出栈，直到把第一个左括号出栈

### 155.最小栈

两个栈，一个是本栈，一个是最小栈，最小栈的栈顶为插入某元素后栈的最小值

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



## 优先队列

### 215.数组中的第K个最大元素

优先队列创建小根堆

```java
std::priority_queue<int, std::vector<int>, std::greater<int>> min_heap;
```

### 347.前K个高频元素

优先队列大根堆，先从小到大排序，然后按照pair<int cnt, int num>入队，最后返回队里前k个数

### 295.数据流的中位数

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



## 单调栈

### 1475.商品折扣后的最终价格

先将数组“反向”，然后单调栈

```java
 for (int i = n - 1; i >= 0; i--) {
     while (!st.empty() && st.top() >= prices[i]) {//把比它大的都出栈，因为后序也不可能选到它们了
         st.pop();
     }
     ans[i] = st.empty() ? prices[i] : prices[i] - st.top();
     st.push(prices[i]);
 }
```

### 239.滑动窗口最大值

自己的方法太慢了，双向队列保存当前最大的下标，如果nums[i]>=nums[q.back]，就队尾出队；如果队头==i-k，就队头出队

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



## 二叉树

### 144.二叉树的前序遍历

递归，递推需要借助栈，入栈根节点，然后不断取出栈顶，先入栈顶的右，再入栈顶的左

```java
while(!st.empty()){
	auto Temp = st.top();
	st.pop();
	push_back(Temp->val);
	if(Temp->right)	push(right);
	if(Temp->left)	push(left);
}
```

### 94.二叉树的中序遍历

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

### 145.二叉树的后序遍历

递推版

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

### 102.二叉树的层次遍历

就是借助queue广搜

### 107.二叉树的层次遍历II

上一题反转

### 104.二叉树最大深度

递归

### 110.平衡二叉树

求最大深度 + 左右子树递归

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

### LCR 144.二叉树的镜像

递归

### 101.对称二叉树

***没做出来***，构造新方法检查左右子树是否对称，递归调用

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

### 199.二叉树的右视图

层次遍历，输出每层最后一个…

### 662.二叉树最大宽度

没做出来，自己的方法是将nullptr也入队，最后超内存了

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

### 105.从前序与中序遍历序列构造二叉树

分成左右子树，递归

### 106.从中序与后序遍历序列构造二叉树

分成左右子树，递归

### 230.二叉搜索树中第k小的元素

中序遍历

### 112.路径总和

回溯

### 113.路径总和II

回溯

### 235.二叉搜索树的最近公共祖先

递归，如果两个都比root小，调左子树，都大右子树，夹中间root

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

### 236.二叉树的最近公共祖先

递归，记录每个结点是否有p，q

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



## 位运算与数学

### 136.只出现一次的数字

按位亦或

### LCR 133.二进制中1的个数

***n & (n-1) 消除最右边的 1***

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

### LCR 134.Pow(x,n)

***快速幂算法***，将num转为二进制数，如13 = 0b1101，x不断平方自身，遇到二进制num中的1，ans*=x

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

### 260.只出现一次的数字III

先全部亦或，再通过两数的亦或得到它们不同的一位，根据此位将nums分为两组，且两数必定不在同一组

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

### 137.只出现一次的数字II

数字最大2^31-1，因此直接确定所有数字每一二进制位的个数，然后%3不为0，说明目标数该二进制位为1

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

### 172.阶乘后的0

5贡献一个0，25贡献两个0，125贡献三个0，以此类推。找出小于n的最大5的幂，然后遍历加起来

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

### 169.多数元素

没做出来，就是哪个找一半以上的元素，***摩尔投票法***

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

### LCR 186.文物朝代判断

不重复 + max - min < 5

### 343.整数拆分

用递归不出所料超时了捏，方法：找规律

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

### LCR 132.砍竹子II

***注意数据 long也存不下，必须边乘边取模***

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



## 贪心算法

### 455.分发饼干

将greed和sizes都排序，优先用小饼干满足小孩子

### 376.摆动序列

就是找可以形成多少个波峰和波谷

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

### 55.跳跃游戏

就是不断更新到当前结点可以到达的最远处，最后看到没到nums.length-1

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

### 45.跳跃游戏II

简单方法：维护当前的最远距离，若到达上一结点可到的最远处，res++

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

### 621.任务调度器

先找最多的同一个任务，算一个没有其他任务的时长，其他任务插在其中的间隔里，若间隔不够插，则耗时为任务总数，***注意可能存在多个最长的任务！***

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

### 435.无重叠区间

***本质是找最多放多少个区间不重复，和教室办最多活动的那题一模一样***

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

### 135.分发糖果

初始全为1，两次遍历，从左到右，从右到左，若分比旁边高且糖果<=旁边的，则将糖果设为旁边人的+1，之所以遍历两次是因为要排除遍历方向带来的误差

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



## 回溯算法

### 77.组合

经典回溯算法

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

### 216.组合总和III

经典回溯算法，可以剪一下枝

### 40.组合总和II

没做出来，主要是不知道怎么去重

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

### 39.组合总和

有了上面那道题消除重复数的经验后

只需将遍历时bfs(i+1)改为bfs(i)即可同一个数重复使用

### 78.子集

回溯，注意递归终止条件是当前集合大小大于nums大小，而add之后不要return，否则子集只有一个数！

### 90.子集II

增加子集不能重复的条件，其实就和昨天的`40.组合总和II`一样的解决方法，先把nums排序，然后dfs的for种如果`i>start && nums[i]==nums[i-1]`就continue

### 46.全排列

用visit记录index处的数有没有使用

### 47.全排列II

经典要求去重

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

### 51.N皇后

虽说是困难，但好在之前做过，还是做出来了，只不过刚开始初始化map时采用

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



## 动态规划

### 70.爬楼梯

简单动态规划

### 746.使用最小花费爬楼梯

简单动态规划

### 198.打家劫舍

明确当前只能来自i-2或i-3，当然帅地的方法更好，直接记录遍历到第i个屋子时的最多金钱，`dp[i]=Math.max(dp[i-2]+nums[i], dp[i-1])`

### 62.不同路径

简单动态规划

### 64.最小路径和

简单动态规划

### 63.不同路径II

简单动态规划

### 0-1背包

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

### 完全背包

```java
public class Main{
    public static void main(String[] args) throws Exception {
        // 读入数据的代码
        Scanner reader = new Scanner(System.in);
        // 物品的数量为N
        int N = reader.nextInt();
        // 背包的容量为V
        int V = reader.nextInt();
        // 一个长度为N的数组，第i个元素表示第i个物品的体积；
        int[] v = new int[N + 1] ;
        // 一个长度为N的数组，第i个元素表示第i个物品的价值；
        int[] w = new int[N + 1] ;
    for (int i=1 ; i <= N ; i++){
        // 接下来有 N 行，每行有两个整数:v[i],w[i]，用空格隔开，分别表示第i件物品的体积和价值
        v[i] = reader.nextInt();
        w[i] = reader.nextInt();
    }
    reader.close() ;

    // 核心模式代码

    int[][] dp = new int[N+1][V+1];
    // 初始化

    for(int i = 1; i <= N; i++){
        for(int j = 0; j <= V; j++){
            for(int k = 0; k*v[i] <= j; k++){
                dp[i][j] = Math.max(dp[i][j], dp[i-1][j-k*v[i]] + k * w[i]);
            }
        }
    }

    System.out.println(dp[N][V]);

}
```
### 多重背包

```java
public class Main{
    public static void main(String[] args) throws Exception {
        // 读入数据的代码
        Scanner reader = new Scanner(System.in);
        // 物品的数量为N
        int N = reader.nextInt();
        // 背包的容量为V
        int V = reader.nextInt();
        // 一个长度为N的数组，第i个元素表示第i个物品的体积；
        int[] v = new int[N + 1] ;
        // 一个长度为N的数组，第i个元素表示第i个物品的价值；
        int[] w = new int[N + 1] ;

        for (int i=1 ; i <= N ; i++){
            // 接下来有 N 行，每行有两个整数:v[i],w[i]，用空格隔开，分别表示第i件物品的体积和价值
            v[i] = reader.nextInt();
            w[i] = reader.nextInt();
        }
        reader.close() ;

        // 核心模式代码

        int[][] dp = new int[N+1][V+1];
        // 初始化

        for(int i = 1; i <= N; i++){
            for(int j = 0; j <= V; j++){
                for(int k = 0; k <= s[i] && k*v[i] <= j; k++){
                    dp[i][j] = Math.max(dp[i][j], dp[i-1][j-k*v[i]] + k * w[i]);
                }
            }
        }

        System.out.println(dp[N][V]);

    }
}
```

### 5.最长回文子串

```java
//含义：从i到j的子字符串是否为回文串，为0不是，为1是
int[][] dp = new int[s.length()][s.length()];
```

中间夹的是回文且start和end相同，则回文

### 718.最长重复子数组

没做出来，用的暴力遍历，超时了

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

### 300.最长递增子序列

又没做出来

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

### 72.编辑距离

没做出来，可以将增删改表示为dp数组的形式，找到其与当前的关系

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

### 10.正则表达式匹配

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

### 122.买卖股票的最佳时机II

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

### 714.买卖股票的最佳时机含手续费

在卖的时候支付fee块钱就可以了

### 121.买卖股票的最佳时机

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

### 123.买卖股票的最佳时机III

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

### 188.买卖股票的最佳时机IIII

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

### 309.买卖股票的最佳时机含冷冻期

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

### 322.零钱兑换

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

---



# 算法高频题库

## 链表与双指针

### 160.相交链表

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

### 206.反转链表

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

### 234.回文链表

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

### 2.两数相加

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

### 19.删除链表的倒数第N个结点

一次遍历的方法：pre结点法 + 快慢指针，快指针比慢指针快n个结点

### 23.合并K个升序列表

采用归并排序（分治思想）合并，注意mid=lists.length/2，得到的是偏右的中点

### 82.删除排序链表中重复元素II

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

### 92.反转链表II

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

### 142.环形链表II

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

### 143.重排链表

第二次没做出来

1. 先找左中点
2. 将右侧链表反向
3. 合并两链表

```java
class Solution {
    public void reorderList(ListNode head) {
        if (head == null) {
            return;
        }
        ListNode mid = middleNode(head);
        ListNode l1 = head;
        ListNode l2 = mid.next;
        mid.next = null;
        l2 = reverseList(l2);
        mergeList(l1, l2);
    }

    public ListNode middleNode(ListNode head) {
        ListNode slow = head;
        ListNode fast = head;
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }

    public ListNode reverseList(ListNode head) {
        ListNode prev = null;
        ListNode curr = head;
        while (curr != null) {
            ListNode nextTemp = curr.next;
            curr.next = prev;
            prev = curr;
            curr = nextTemp;
        }
        return prev;
    }

    public void mergeList(ListNode l1, ListNode l2) {
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
}
```

### 148.排序链表

归并排序，又用到了找中点和合并链表。。。

### 25.K个一组翻转链表

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

## 二叉树

### 94.二叉树的中序遍历

非递归：栈辅助，栈空且root为null时退出循环

### 101.对称二叉树

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

### 104.二叉树的最大深度

递归

### 110.平衡二叉树

注意所有子树都必须平衡

### 144.二叉树的前序遍历

栈辅助

### LCR 144.翻转二叉树

递归

### 102.二叉树的层次遍历

ArrayDeque的方法roll，移除队头并返回移除的元素

### 105.从前序和中序遍历序列构造二叉树

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

### 129.求根节点到叶结点的数字之和

类似于回溯，注意当结点是叶子结点时就要ans+=num并返回了，如果在结点为null的时候ans+=num，那么最终的结果是正确结果的两倍，因为叶子结点左边的null结点要加一次，右边的null结点又要加一次

### 145.二叉树的后序遍历

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

### 199.二叉树的右视图

层次遍历，取每层的最右侧

> 我竟然用indexOf去取指定下标处的值…混淆了indexOf和get方法…

### 230.二叉树最近公共祖先

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

### 662.二叉树最大深度

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

### 124.二叉树中的最大路径和

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

### ==297.二叉树的序列化和反序列化==

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

## 排序算法

### 56.合并区间

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

### 215.数组中第k个最大元素

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

### 315.计算右侧小于当前元素的个数

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

## 二分查找

### 69.x的平方根

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

### 367.有效的完全平方数

和上面那题大同小异，也是二分查找找

### 704.二分查找

有一个坑：最后出循环不能直接返回-1，因为可能left和right相遇的点刚好是target，如{2,5},target=5，left=right时，nums[left]刚好为target，但是因为left=right，就出循环了，因此最后要判断：

```java
return nums[left] == target ? left : -1;//防止target刚好是left和right相遇的点
```

### 33.搜索旋转排序数组

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

### 34.在排序数组中查找元素的第一个和最后一个位置

就是在查到nums[mid]==target后，向左和向右遍历

## 双指针

### 26.删除有序数组中的重复项

双指针，left代表有序部分，right遍历整个数组

### 88.合并两个有序数组

写腻了。。。

### 11.盛最多水的容器

双指针，类似于接雨水

### 15.三数之和

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

### 287.寻找重复数

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

## 栈与队列

### 20.有效括号

简单栈的应用

### 155.最小栈

加入的数比最小栈的栈顶小，则加入该数，否则加入最小栈栈顶的数

### 225.用队列实现栈

push的时候，把所有元素出队，把最新加入的元素放到队头，实现后进先出

### 232.用栈实现队列

将两个栈底部拼在一起，当pop或peek时，如果left为空则将right的所有元素加入left
