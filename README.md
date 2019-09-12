组件、View、数据分开------解耦
    组件要负责渲染View，那就封装View给它渲染
    组件要负责管理生命周期，根据生命周期状态设置View，View提供接口给组件控制
                          根据生命周期状态设置数据，数据提供接口给组件控制
    数据和view互相提供接口，相互影响
    MVP模式：View实现视图，注入Presenter，向Presenter注入ViewController,View向Presenter请求数据、事情处理处理函数，同步的直接返回，异步的从ViewController返回，

xml id实例化
https://www.buzzingandroid.com/tools/android-layout-finder/

shape 属性
https://www.cnblogs.com/MianActivity/p/5867776.html

standard、singleTop、singleTask、singleInstance
    https://blog.csdn.net/sinat_14849739/article/details/78072401
    标准模式下，只要启动一次Activity，系统就会在当前任务栈新建一个实例。

Activity的生命周期
创建到显示：onCreate--onStart--onResume
销毁：onPause--onStop--onDestroy

横竖屏切换：onPause--onStop--onDestroy--onCreate--onStart--onResume

从A页面跳转到B页面执行的方法
1、B页面完全覆盖A页面的情况
    A.onPause--B.onCreate--B.onStart--B.onResume--A.onStop
2、B页面不完全覆盖A页面的情况
    A.onPause--B.onCreate--B.onStart--B.onResume

B页面点击返回
1、B页面完全覆盖A页面的情况
    B.onpause--A.restart--A.onStart--A.onResume--B.onStop--B.onDestory
2、B页面不完全覆盖A页面的情况
    B.onpause--A.onResume--B.onStop--B.onDestory