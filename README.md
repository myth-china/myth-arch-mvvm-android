myth-arch-mvvm-android
=

一款基于Jetpack的轻量级MVVM基础库，主要特色有：

* 非侵入式接入，只需要实现MythView和MythViewModel接口，创建并返回Provider实例
* 可无限扩展能力的ViewModel（基于Kotlin扩展方法），扩展方式详见Demo
* 自动化的生命周期管理方式（基于JetPack的Lifecycle及Kotlin的Coroutine）
* 避免了Activity及Fragment生命周期引起的内存及空指针问题（基于JetPack的Lifecycle）

继承MythView
-
如果要通过Activit或者Fragment使用此库，则需要Activity或者Fragment实现MythView接口，接口中的方法全部是缺省方法，所以您不必实现任何方法，继承此接口即可：

```
class MainActivity : AppCompatActivity(), MythView {
    private val viewModel by lazy { viewModelOf(MainViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn.setOnClickListener {
            viewModel.openSecondPageForResult()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.toast("OnResumed toast with ext")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0x11 && resultCode == 0x11) {
            btn.text = data?.getStringExtra("data")
        }
    }
}
```

如果项目中要全量使用myth-arch-mvvm-android的话可以用您的BaseActivity或BaseFragment继承此接口

