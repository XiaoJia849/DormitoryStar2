# 2021.10.20

寝室之星计划

鉴于我是一个非常容易放弃的人，我也不能保证可以做完这个项目
但是如果做不完，这也太可惜了，我会非常难受的
加油！！！

1. 采用手机号注册
   https://blog.csdn.net/the_only_god/article/details/104005745
   https://blog.csdn.net/zhaojunwei666/article/details/104532777
   密钥库口令默认是android
   kao  MobTech需要公司营业执照，醉了,算了，这个也正常
   那么基本可以放弃手机号注册了，哭了
   
2. 用户的注册信息传到服务器
   https://blog.csdn.net/u014026084/article/details/73124157?spm=1001.2101.3001.6650.4&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7EBlogCommendFromBaidu%7Edefault-4.no_search_link&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7EBlogCommendFromBaidu%7Edefault-4.no_search_link
   正好我有一个服务器，希望问题不大，哭了，这个暂时不做，先快点把页面设计全部完成
   但是注册信息的检验过程呢？怎么做？用服务器
   
3. Android 
   点击一段文字，触发事件，打开dialog
   https://blog.csdn.net/mq2553299/article/details/78033581

4. 旋转
   res/anim专门写动画的xml，这个太好使了，感谢上苍
   https://www.cnblogs.com/llguanli/p/8674274.html
   https://developer.android.google.cn/guide/topics/graphics/spring-animation?hl=zh-cn
   单纯的旋转没有用，我需要的是一个序列所有东西在绕着一个点旋转
   https://developer.android.google.cn/training/animation/reposition-view?hl=zh-cn
   MD 我要骂人了，官方文档说可以用xml，那你整一个用xml的例子啊？我小白我怎么知道怎么做，真是不当人
   我想哭，反正我找不到，算了，就这样吧，用代码实现吧
   https://www.jianshu.com/p/706d0943451e
   关于path
   https://www.jianshu.com/p/db01b37b6231
   设置view的绝对位置
   https://www.cnblogs.com/huansky/p/11937840.html
   完了，这个旋转我要整好久，不慌，大不了最后作业就交个旋转
   圆形的view
   https://www.cnblogs.com/lyd447113735/p/9133776.html

5. 日历
   https://www.jianshu.com/p/0aa8ebfeb682
   android采用svg
   https://blog.csdn.net/megatronkings/article/details/52878466
   使用阿里巴巴矢量库
   https://blog.csdn.net/xialong_927/article/details/107221892
   文字叠加：采用framlayout实现
   https://www.runoob.com/w3cnote/android-tutorial-framelayout.html
   日历的view,这个人写的基础上再改改应该问题不大
   https://github.com/SundeepK/CompactCalendarView
   现在已经基本完成了   


6 个人信息
   https://blog.csdn.net/qq_36328643/article/details/104379956
   带圆形边框的imageview
   https://blog.csdn.net/a_ycmbc/article/details/51373211
   创建圆形imagview
   https://www.cnblogs.com/tianzhijiexian/p/4297172.html
   自定义控件
   https://zhuanlan.zhihu.com/p/145196277
   ??怎么给自定义控件添加事件

7 AlertDialog
   几个参考  
   https://github.com/mylhyl/Android-CircleDialog
   https://github.com/droidbyme/DroidDialog
   感谢上苍，让我遇到：这个写的特别全，贼好！！！！
   https://github.com/XXApple/AndroidLibs

关于每个页面
LoginOrRegister:
   注册后sharepreference "user" 保存用户信息，每次点击APP检查有无这个信息，有登录并且显示loading,无就注册
MemberLeaderElect:
   显示一个寝室的用户，旋转动画，显示信息为nickname，点击选举寝室长
   这个页面我一直在为动画担心，好烦，等会问问老师
   两个模式，正常旋转，与选举寝室长
   寝室长有两个模式 正常旋转，开始选举
PlanChooseCreate:
   寝室长选择打扫卫生的计划，或者自行指定计划
CalenderWatch:
   所有人可以查看日历，并且在轮到自己的那一天盖个章，表示打扫卫生了
   显示所有人昵称和对应的图标
   用图标盖章，当做了就变灰，没打扫卫生就变为红色，血红
UserInfoEdit:
   用户查看个人信息并且进行编辑

# 2021.10.21

现在进度:所有页面框架打了，但是部分功能没有实现
1. 登录，需要服务器相应返回json
   我一开始想用我的腾讯云，但我突然意识到，我有阿帕奇
   我完全可以访问这个网址，关键是更新新的xml我应该在什么代码写呢?
   我需要一个文件，放在电脑阿帕奇的公开部分，但是我要用android里的java代码对这个文件进行修改。
   这里不能用sharepreference 因为文件在电脑，不在手机
   我想要用apache,但是我电脑的ip肯定要变，我必须让他不变，这个好烦啊，用热点应该不会变，那就这样
   我突然想起我有一个已经OK的网站，我在那个的基础上改一下就行了，真蠢啊
2. 日历，我已经把网上的日历改造完成了

# 2021.10.22

1. 今天我想先做那个动画，应该很有意思
   参考:
   https://github.com/sharish/BitmapMerger
   canvas绘制椭圆
   https://www.php.cn/java-article-360706.html

2. 日历盖章形状还可以再修改：【不重要】
   https://github.com/siyamed/android-shape-imageview
   https://github.com/MostafaGazar/CustomShapeImageView

3. 登录
   如果用户手机上把信息删了，我就当作从来没有这个用户了。本身用户量就不多。就这样
   解析JSON的文件
   我之前写的代码在这里
   F:\MyAndroid\NetworkTest\app\src\main\java\com\example\networktest
   这个就是纯解析，搜索功能我写在Android代码还是服务器代码啊?
   写到服务器吧
   我确实要多写写这方面代码，但是我现在有点想呕吐
   MD，最近代码含量太高了，出去转两圈，休息一下
   
# 2021.10.23

昨天回寝室就没出来过，不该相信自己哈

1. JSON的读写方法已经全部完成

2. JSP响应请求完成JSON的读写操作，这里出现了很多问题
   主要是一直没办法解析JSON，即使我导入了包
   我还把JDK版本变成了1.6,屁用没有，气死我了
   我现在很生气，我先做其他东西
   
其他东西是做完了，但是我还是不想做这个，好烦啊
哭了

3. 我也需要navi,虽然刚做了一个，但是那个navi我不喜欢，我看看整个其他的navi玩玩
   美貌就是一切，一定要做个好看的navi,当然不可能我自己做
   
最近总想着做那些有技术含量的东西，忽略了很多非常实际的应用，这样不好

4. 页面引导。在刚安装这个软件之后，点击软件进行引导。【有时间做】
   https://github.com/sacot41/SCViewPager
   
5. userEdit页面
   编辑生日
   https://github.com/wdullaer/MaterialDateTimePicker

时常因为网速痛苦

git commit add 无法检测到我这个代码变动
push 返回错误 cant gind git repository
我用命令行成功Push了

git add *
git commit -m "init "
git push -u origin master

Android Studio 的工具无法帮我add commit push 
不知道为啥，但是就这样吧。想哭

# 2021.10.24
麻了

今天上午解决了本地服务器与JAVA应用的响应问题，响应方法都写完了，但是

在JAVA项目中一样的命令发送请求到了android竟然行不通，我真的醉了

算了，先把其他问题解决

1. 更新生日
   用不了  https://github.com/maiconhellmann/heins-input-dialogs
   这个没办法直接用，我试着修改了一下不成功，不想再试了
   https://github.com/wdullaer/MaterialDateTimePicker 
   这个人超贴心，写的特别细，感谢
   https://github.com/code-troopers/android-betterpickers

2. 更新昵称  学校
   https://github.com/ayaremin/panter-dialog
   
3. 更新性别  男女  寝室长  是否
   https://github.com/ch-muhammad-adil/Android-Material-Picker-Dialog
   这个真的太好看了，虽然它放到我的代码里风格不统一
   但是那又怎样，我一定要用它,该死，不用想了，用不了，可恶
   https://github.com/ceryle/RadioRealButton

4.  [不重要]时间段的选择器
   https://github.com/MedAmineTazarki/DateRangePicker
   

除了时间段现在还不需要,没修改,基本都修改了,逻辑顺了一遍.
主要是累了,不想做了,明天再继续修改吧

看到了一个特别适合的navi
https://github.com/Devlight/NavigationTabStrip
有点小问题，就是type 真烦啊


# 2021.10.25

把calendar的两种模式确定了。

现在就差数据了，基本上

哎，好难受

好暴躁，要疯了

这个东西吧，越想越复杂，我写了不知道多少行代码了，真的要疯了

calendar的颜色我之后再研究一下

要求一个寝室4个人，我已经定下来了4这个数字了


# 2021.10.26

写了一堆方法，我现在都脑壳疼