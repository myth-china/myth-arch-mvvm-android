myth-arch-mvvm-android
===============

一款基于Jetpack的轻量级MVVM基础库，主要特色有：

* 非侵入式接入，只需要实现MythView和MythViewModel接口，创建并返回Provider实例
* 可无限扩展能力的ViewModel（基于Kotlin扩展方法），扩展方式详见Demo
* 自动化的生命周期管理方式（基于JetPack的Lifecycle及Kotlin的Coroutine）
* 避免了Activity及Fragment生命周期引起的内存及空指针问题（基于JetPack的Lifecycle）
