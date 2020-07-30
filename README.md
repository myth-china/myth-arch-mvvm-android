myth-arch-mvvm-android
=

一款基于Jetpack的轻量级MVVM基础库，主要特色有：

* 非侵入式接入，只需要实现MythView和MythViewModel接口，创建并返回Provider实例
* 可无限扩展能力的ViewModel（基于Kotlin扩展方法），扩展方式详见Demo
* 自动化的生命周期管理方式（基于JetPack的Lifecycle及Kotlin的Coroutine）
* 避免了Activity及Fragment生命周期引起的内存及空指针问题（基于JetPack的Lifecycle）

Activity或者Fragment继承MythView
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


ViewModel继承MythViewModel接口
-
当您的ViewModel继承MythViewModel接口，并实现接口中的getProvider()方法后，您就可以轻松的扩展ViewModel的能力，并且使用此库中定义的一些便捷
能力（这些能力我们会放到最后一部分讲解），getProvider()返回的是MythViewModelProvider实例，MythViewModelProvider类是作者定义的ViewModel扩展引擎，你也可以扩展此类为ViewModel实现更多的能力，默认返回此类实例就好

```
open class BaseViewModel : ViewModel(), MythViewModel {

    private val mythViewModelProvider by lazy { MythViewModelProvider(this) }

    override fun getProvider(): MythViewModelProvider {
        return mythViewModelProvider
    }
}
```

MythViewModel能力扩展有哪些优点？
-
* 首先MythViewModel可以让你在ViewModel中直接使用Activity/Fragment/Context等只有在View中才能直接引用的实例，并且不用关心生命周期问题，之所以在ViewModel中使用Activity/Fragment/Context等不需要关心生命周期问题，是因为获取引用的方式是通过LiveData回调实现的，并且LiveData与Lifecycle绑定
* 其次此库中MythViewModel的所有扩展能力均采用Kotlin的扩展方法实现，并按功能归类到不同的kt文件中，所以您不必担心ViewModel基类膨胀


我如何扩展ViewModel基类的能力？
-
下面是作者实现的扩展，您可以参考并实现自己的扩展功能

```
/**
 * Use Block
 */
fun MythViewModel.useFragment(callback: (Fragment) -> Unit) {
    val name = "useFragment"

    /**
     * 从MythViewModel的MythViewModelProvider实例中获取我们的LiveData，如果此LiveData不存在，则new一个，
     * 之后会通过MythViewModelProvider.addViewModelExt(name, toastData)，将此LiveData注册到MythViewModelProvider实例中，
     * 这样我们就可以通过此LiveData发送指令给View，实现我们的逻辑
     */
    val toastData = getProvider().getViewModelExtData(name) ?: MutableLiveData<(Fragment) -> Unit>()

    toastData.value = callback

    if (toastData.hasObservers()) {
        return
    }

    /**
     * 注册我们的LiveData和View建立关系
     */
    getProvider().addViewModelExt(name, toastData) { view, data ->
        data.observe(view.getLifeCycleOwner(), Observer {
            callback(view.getFragment2())
        })
    }
}
```

上面是一个在ViewModel中获取Fragment引用的扩展功能，实现方式请看注释，有能力的童鞋可以直接看源码，其实设计思路很简单



