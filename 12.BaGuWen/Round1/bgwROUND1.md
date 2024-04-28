# 1.Java系列

## 1.Java基础

### 1.介绍一下 Object 常见方法？🌟🌟🌟🌟🌟

==我的回答==：

1. Object 的常用方法包括  getClass(), equals(Object), hashCode(), toString(), notify(), notifyAll(), wait(无参/long/long, int), finalize() ，下面我将一一进行介绍。

2. getClass() 方法用于返回对象==运行时的类对象(Class对象)==。可进一步获取某一对象运行时类的信息。

	> ```java
	> /* Returns the runtime class of this {@code Object}.
	> ```

3. equals 方法==默认情况下使用 == ， 比较引用类型是否相等==。比如 String 类中就重写了此方法，优先比较两对象的地址和运行时类是否相同，随后以比较字符串的方式比较两个 String 对象。

4. hashCode  方法==在默认情况下根据对象的内存地址返回一个int整数==。

	```java
	/* This is typically implemented by converting the internal address of the object into an integer
	```

	==值得注意的是：由于 hashCode 方法可以被重写，因此其返回值不能直接和对象的内存地址挂钩==。hashCode 方法可以用于==帮助 HashMap 实现哈希映射==，但是一个对象在 HashMap 中映射的结果并不等同于其 hashCode，而是基于 hashCode 做了一层封装。

	```java
	static final int hash(Object key) {
	    int h;
	    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
	}
	```

	此外还规定：==如果两个对象使用 equals 方法得到 true 的话，那么它们的 hashCode 必须相同==。

	```java
	/* If two objects are equal according to the {@code equals(Object)} method, then calling the {@code hashCode} method on each of the two objects must produce the same integer result.
	```

	所以==一般两个方法会一同被重写==。

	```java
	@Override
	public boolean equals(Object o) {
	    if (this == o) return true; // 先判断内存地址是否一致，相同则必然相等
	    if (o == null || getClass() != o.getClass()) return false; // 然后判断两对象的运行
	    // 时类型（getClass( )方法）是否与该对象一致，不一致就肯定不相等
	    ThisClass that = (ThisClass) o;
	    return field1 == that.field1 && // 基本数据类型字段直接判断值是否相等
	        field2.equals(that.field2) && // 引用类型则调用其各自的 equals 方法
	        field3.equals(that.field3); // field1 为基本类型，2、3为引用类型
	}
	
	@Override
	public int hashCode() { // 同时重写 hashCode 方法
	    return Objects.hash(field1, field2, field3);
	}
	```

5.  toString  方法用于生成一个 String 字符串以描述一个对象。==默认情况下为==

	```java
	object.getClass().getName() + "@" + Integer.toHexString(object.hashCode())
	```

	==建议所有的类都自行实现 toString 方法==，以更友好的方式告诉程序员某个对象。

6. wait(无参/long/long, int) 方法用于==将此对象上的线程置于等待状态==，直到它被另一个线程以该对象的 notify 方法或 notifyAll() 方法唤醒。

7. notify(), notifyAll() 用于==唤醒某一对象上处于等待状态的线程==。值得注意的是， notify() 随机挑选一个线程唤醒，而另一个则唤醒全部线程。

8. finalize() 方法用于在JVM垃圾回收阶段，==当一个对象将被回收时，会先调用其 finalize() 方法==，目的是==尝试让该对象获得 GC Roots 的引用以逃过这次回收==。

==参考答案==：

- Object 类常见的方法有 toString( )，equals( ) 和 hashCode( )，wait( ) 和 notify( ) 等等。下面分别说一下：
- ==toString( ) 返回的是对象的字符串表示==，==默认为 「class 名 + @ + hashCode 的十六进制表示」==，我们一般会在子类将它重写为打印各个字段的值，==在调试和打日志中用的多==。
- ==equals( ) 和 hashCode( ) 通常用于对象之间的比较==。其中 equals( ) 用于判断两个对象是否相等，==默认使用 “= =” 判断==，hashCode( ) 用于获取对象的哈希码，==默认以对象的内存地址为参考==。在实践中，==为了保证元素在 HashMap 和 HashSet 等集合中的正确存储，通常需要将它俩一起重写==。
- wait( )，notify( ) 以及 notifyAll( ) 通常用于==线程间的协作和同步==。其中 ==wait( ) 使当前线程释放锁并进入等待状态==，直至被其它线程的 notify 或 notifyAll 唤醒。
- notify( ) 会在对该对象调用了 wait( ) 的线程中，==随机挑选一个唤醒，解除其阻塞状态==。而 notifyAll( ) 会唤醒==所有==在该对象上等待的线程。==wait( ) 搭配 notify( ) 可以实现一个简单的“生产-消费模型”：生产者线程产生消息后，调用 notify( ) 唤醒消费者。消费者被唤醒后消费消息，消费完成后调用 wait( ) 等待==。

> 参考答案里举了更多的例子，使得回答更形象具体，让面试官感觉你是真的懂并且能够正确运用，而不是只会干巴巴地背诵。

---



### 2.Java为什么被称为平台无关性语言？🌟🌟🌟🌟🌟

==我的回答==：

1. 首先我想说说==什么是平台无关性语言==。平台无关性语言是指“==一次编译，处处运行(Write once, run anywhere)==”的语言，平台无关性是java的设计者在设计java时的美好愿景。
2. Java实现“一次编译，处处运行”的方法是通过==javac编译器将Java源代码编译为的字节码==，而==不同的操作系统有不同的JVM==，==JVM将同一份字节码翻译为对应操作系统的指令集==，这样就让原本不能跨平台的Java源代码实现了平台无关性。

==参考答案==：

- 平台无关性是说，==一种语言在一台计算机上的运行不受平台的限制，一次编译，到处运行==。
- Java 语言具有平台无关性的关键在于 JVM。虽然==不同的操作系统使用不同的机器指令集来执行任务，同一份代码在不同的操作系统上可能无法直接执行==，但是 Java 源文件经过 ==javac 编译器编译后形成的二进制字节码，可以被各个操作系统的 JVM 翻译成该操作系统所需的指令集==，进而执行。这可以提高 Java 程序的可移植性，因为==只需针对不同操作系统提供对应的 JVM 即可，无需修改源代码==。

---



### 3.= =和equals有什么区别？🌟🌟🌟🌟🌟

==我的回答==：

1. ====是一个操作符==，可以比较==基本数据类型==之间和==引用数据类型==之间是否相等。而 ==equals 是 Object 类的一个方法==，==只能比较两个引用数据类型==是否相等。

2. 具体地说，====比较基本数据类型时，就直接比较值是否相等==。比如int就比较整型值是否相等，boolean就比较布尔值。而==\==比较引用数据类型时，则比较对象的内存地址是否一致==。

3. 而 ==equals 方法在 Object 方法中就是使用 == 比较两个对象是否相等==：

	```java
	public boolean equals(Object obj) {
	    return (this == obj);
	}
	```

	但是==我们可以根据不同对象的意义重写此方法==，以自定义的方式判断两个对象是否相等。比如 String 类就重写了 equals 方法，优先判断两个对象的内存地址和运行时类型是否一致，然后以比较字符串的方法判断两个 String 对象是否相等。==由于要求 equals 为 true 的两个对象的 hashCode 也必须相同，因此一般该方法的重写会伴随着 hashCode 方法的重写==。

	```java
	@Override
	public boolean equals(Object o) {
	    if (this == o) return true; // 先判断内存地址是否一致，相同则必然相等
	    if (o == null || getClass() != o.getClass()) return false; // 然后判断两对象的运行
	    // 时类型（getClass( )方法）是否与该对象一致，不一致就肯定不相等
	    ThisClass that = (ThisClass) o;
	    return field1 == that.field1 && // 基本数据类型字段直接判断值是否相等
	        field2.equals(that.field2) && // 引用类型则调用其各自的 equals 方法
	        field3.equals(that.field3); // field1 为基本类型，2、3为引用类型
	}
	
	@Override
	public int hashCode() { // 同时重写 hashCode 方法
	    return Objects.hash(field1, field2, field3);
	}
	```

==参考答案==：

- 首先 === = 是一个操作符== ，==equals 是超类 Object 中的方法==，==默认==是用 = = 来比较的。也就是说，对于没有重写 equals 方法的子类，equals 和 = = 是一样的。
- 而 = = 在比较时，根据所比较的类的类型不同，功能也有所不同：对于==基础数据类型==，如 int 类型等，==比较的是具体的值==；而对于==引用数据类型==，比较的是==引用的地址==是否相同。
- 对于重写的 equals 方法，比的内容==取决于这个方法的实现==。

---



### 4.讲一下equals()与hashcode()，什么时候重写，为什么重写，怎么重写？🌟🌟🌟🌟🌟

==我的回答==：

1. 首先我想说说 equals 和 hashCode 方法。它们都是 Object 类的方法。在==默认情况下，equals 方法采用 == 操作符比较引用类型的内存地址==，而 ==hashCode 方法默认情况下根据内存地址返回哈希值==。

2. 由于 equals 方法默认只能比较引用类型的内存地址，而==在业务中我们常常有自己判断两个对象是否相等的逻辑==，比如一个 Person 类有 name 和 identity 两个字段，我们可以认为这两个字段都相同的 Person 实例对象相等，于是就可以重写 Person 类的 equals 方法。

3. 由于有==规定：如果两个对象使用 equals 方法相同，那么它们的 hashCode 也必须相同==。因此 ==equals 方法的重写一般也伴随着 hashCode 方法的重写==。

4. 至于重写的方法，还是以上述的 Person 类为例，重写 equals 方法时，可以==先判断内存地址是否一致==，相同则必然相等，然后==判断两对象的运行时类型（getClass( )方法）是否与该对象一致==，不一致就肯定不相等，最后对于==基本数据类型字段直接判断值==是否相等，而==引用类型则调用其各自的 equals 方法==。

	```java
	@Override
	public boolean equals(Object o) {
	    if (this == o) return true; // 先判断内存地址是否一致，相同则必然相等
	    if (o == null || getClass() != o.getClass()) return false; // 然后判断两对象的运行
	    // 时类型（getClass( )方法）是否与该对象一致，不一致就肯定不相等
	    ThisClass that = (ThisClass) o;
	    return field1 == that.field1 && // 基本数据类型字段直接判断值是否相等
	        field2.equals(that.field2) && // 引用类型则调用其各自的 equals 方法
	        field3.equals(that.field3); // field1 为基本类型，2、3为引用类型
	}
	```

5. 重写 hashCode 方法可以使用==工具类 Objects 的 hash 方法==根据所有指定字段返回一个哈希值。

	```java
	@Override
	public int hashCode() { // 同时重写 hashCode 方法
	    return Objects.hash(field1, field2, field3);
	}
	```

==参考答案==：

- 首先 equals( ) 是 Object 中的方法，默认是用 = = 来比较的。hashCode( ) 也是 Object 类的方法，根据一定的规则将与对象相关的信息，比如对象的内存地址，映射成一个数值，这个数值称作为哈希值。
- 有时候我们想要==自定义类的比较规则时，需要重写 equals( )==，但是为了==保证类在 HashSet 和 HashMap 等集合中的正确存储，也要同时重写 hashCode( )== 。
- 以 HashMap 为例， ==HashMap底层在添加相同的元素时，会先调用两个对象的 hashCode( ) 是否相同，如果相同还会再用 equals( ) 比较两个对象是否相同==。
- 假设有一个 Person 类，有 name 和 age 两个字段，我们现在重写 equals( ) 规定只有两个 Person 的 name 和 age 都相同时，才认为两个 Person 相等。现在 new 出两个 name 和 age 都相同的 Person，分别添加到 HashMap 中。
- ==我们期望最后 HashMap 中只有一个 Person，但其实是有两个==。原因在于添加第二个 Person 时，先比较的是两个 Person 的 hashCode( )，注意此时我们==没有重写 hashCode( ) ==，那么分别 new 出来的 Person 的哈希值肯定是不同的，到这里 HashMap 就会将两个 Person 认定为不同的元素添加进去。
- 解决的办法就是重写 hashCode( )，最简单的返回 name 和 age 的哈希值的乘积即可。

---



