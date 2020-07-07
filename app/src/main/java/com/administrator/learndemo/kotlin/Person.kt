package com.administrator.learndemo.kotlin

/**
 * class className([var/val] property: Type…)
这种方式和上面一种方式多加了一组括号，代表构造函数，我们把这样的构造函数称之为 primary constructor。这种方式声明一个类的主要做了一下几件事：

会生成一个构造方法，参数就是括号里的那些参数
会根据括号的参数生成对应的属性
会根据 val 和 var 关键字来生成 setter、getter 方法
var 和 val 关键字：var 表示该属性可以被修改；val 表示该属性不能被修改
 */
class Person(val name: String) {
}