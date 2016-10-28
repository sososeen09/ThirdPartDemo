# 都是套路——Dagger2没有想象的那么难
##1 Dagger2——是什么
Dagger2是从Squar公司接手的项目。是第一个一站式生成代码的依赖注入框架。通过注解生成的代码跟手写的一样简单，可以追踪并且保证了性能。

使用Dagger2的好处：

 - 充分的解耦。我需要使用你这个对象，但是我并不关心你这个实例是怎么生成的，我只管拿来用就好了。当你这个对象的创建过程变化的时候，我这个类基本不用动。
 - 测试更加简单。可以模拟实例的生成来注入到需要依赖的地方。

必须了解的一些注解：？？？

一些结论：

 - 在编译期有需要依赖的地方就要有提供依赖的地方，否则会报错。




思路

1. 纯粹的@Inject提供依赖
2. 用@Module，@Provides提供依赖，需要@Component中采用modules把Module类加入。
3. 用@Qualifier标识符@Named，同一个类，提供不同的对象。
4. 自定义@Qualifier标识符。


# TODO
0. @Module和@Inject优先级
1. 自定义@Scope，单例
2. @SubComponent
3. 组织Component
	1. 依赖dependences
	2. 采用@SubComponent


@Inject有缺陷，不能注解多个构造函数，(即便加上 @Qualifier也没用，因为 @Qualifier不能标识@Inject)也不能注解第三方的类（因为修改不了）。

##单例
如果在@Module中对应的@Provides添加@Singleton，那么需要在对应的@Component中也加入@Singleton，否则会报错。通常单例的Component都是在Application中初始化。只有每次Component的对象是同一个，注入的实例才有可能是同一个对象。只在作用域内局部看做是单例的

@Singleton真的有创建单例的能力吗？


## 依赖：
在整个App生命周期内，有一些单例的ApplicationComponent是在App启动的时候创建。如果其他的作用域，如Activity/Fragment，可能会需要Application中的中的全局属性。Component存在依赖和从属关系。

一个Component可以依赖一个或多个Component，并拿到被依赖Component暴露出来的实例，Component的dependencies属性就是确定依赖关系的实现。

Application中提供一个全局的Context，这个在AppComponent中需要显式的曝露给外部


	@Singleton
	@Component(modules = {AppModule.class})
	public interface AppComponent {
	    void inject(MainActivity mainActivity);
	//其他的依赖想要用这个Context，必须显式的暴露
	    Context context();
	}



一个Component A依赖一个有作用域@Scope的Component B，那么A必须要有作用域，并且这个作用域不能与B相同。
