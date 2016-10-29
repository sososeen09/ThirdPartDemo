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
	3. 继承Component


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



一个Component A依赖一个有作用域@Scope的Component B，那么A必须要有作用域，并且这个作用域不能与B相同。可以依赖一个或者多个Component

在包含它的Component可以包含一个或多个SubComponent，要显式的暴露这个SubComponent

	@Subcomponent
	@PerActivity //如果是包含的方式，作用域与上一层的Component相同也没关系。采用依赖的方式就不行。
	public interface ActSubComponent {
	    void inject(SubFragment subFragment);
	}
	
	@PerActivity
	//@Singleton //不能与依赖的AppComponent的作用域相同，否则会报错
	@Component(dependencies = AppComponent.class, modules = ActModule.class)
	public interface ActivityComponent {
	
	    void inject(DependenceTestActivity DependenceTestActivity);
	
	    void inject(SubComponentTestActivity subComponentTestActivity);
	
	    //包含SubComponent,这样的话该SubComponent也可以拿到ActivityComponent中能提供的依赖。
	    ActSubComponent getActSubComponent();
	}


#套路


1 如果在Component中没有modules，那么DaggerComponent可以直接调用create()方法，注入的时候就会找@Inject标注的构造方法
	
	DaggerOnlyInjectComponent.create().inject(this);


2 在Component中添加了modules，那么注入的时候Dagger前缀的Component就会有一个以Module名字同名的方法，参数就是这个Module。如果有多个module，就会有多个类似的方法。如果Module有默认的无参构造，那么不传这个方法也可以的。

	@Component(modules = DataModule.class)
	public interface PersonComponent {
	    void inject(ModuleTestActivity moduleTestActivity);
	}

	aggerPersonComponent.builder()
						.dataModule(new DataModule())
						.build()
						.inject(this);

	//dataModule不传也可以，因为在build()方法里会默认创建无参的构造方法。
	aggerPersonComponent.builder()		
						.build()
						.inject(this);


3 Component和它对应的Modules中作用域要一致。比如负责给整个App提供全局单例实例的一个ApplicationComponent和它对应的ApplicationModule，module中的@Provides包含@Singleton，那么对应的Component必须要有@Singleton

4 如果需要依赖一个类型的不同对象，可以用@Qualifier标识符来表示需要哪个对应的对象。可以自定义标识符，也可以用已经存在的一个标识符@Named。

	@Qualifier
	@Documented
	@Retention(RUNTIME)
	public @interface Named {
	
	    /** The name. */
	    String value() default "";
	}

用法：
	
	    @Provides
	    @Named("male")
	//采用@Qualifier注解，表示我可以提供这种标识符的Person
	    Person providePersonMale() {
	        return new Person("汉子");
	    }
	
	    @Provides
	    @Named("female")
	    Person providePersonFemale() {
	        return new Person("妹子");
	    }

	    //这么多对象，如果需要特定的对象，用@Qualifier标识符注解，@Named是自定义的一个标识符注解
	    @Inject
	    @Named("male")
	    Person mPersonMale;
	
	    @Inject
	    @Named("female")
	    Person mPersonFemale;

5 Modules中的优先级要高于@Inject注解的构造方法。

6 默认的单例@Singleton，那么@Singleton真的有单例能力吗？
其实它只是在局部算是单例。如果在Application中实例化一次，那么在整个App运行过程中，依赖对象只实例化了一次，那么它当然算是单例了。跟我们理解的单例不太一样。
@Singleton作用：

- 方便管理，包含@Singleton的modules，对应的Component必须要有@Singleton

Dagger2将图中的范围实例和Component的实现类相关联，因此组件要申明它们自己要代表哪个范围。在一个组件上面绑定不同不同生命周期的作用域是没有意义的。

7 组织Component

7.1 依赖
dependences，依赖双方Component的作用域不能一致。

	DaggerActivityComponent.builder()
							.appComponent(((App) getApplication())
							.getAppComponent())
							.build()
							.inject(this);

由于ActivityComponent依赖AppComponent，那么创建的时候要调用appComponent方法，表示你依赖哪个具体的AppComponent对象。

7.2 包含
SubComponent
7.3 继承


# 问题
1.为什么全局的ApplicationConponent要暴露给外界提供的单例对象，外界不能直接获取吗？

2.为什么Component都要有一个inject方法？
>对某些需要注入的成员添加@Inject注解的时候，Dagger2就会生成一个DaggerActivity_MembersInjector的东西，当调用inject(this)的时候，估计会把变量赋值。（需要研究源码了）

3.@Singleton并不是创建单例的意思。更多的是一种提醒。

4.为什么要在ActivityComponent中添加一个inject方法呢，并且他的参数一定要是目标类，如DaggerActivity?
> 这个其实是因为到最后注入的时候，会把提供的实例与对应的变量结合。如下，可以看到就相当于给类的成员变量赋值。

	  @Override
	  public void injectMembers(DependenceTestActivity instance) {
	    if (instance == null) {
	      throw new NullPointerException("Cannot inject members into a null reference");
	    }
	//下面的这个get方法就是返回AppCompotent注入的Context;
	    instance.mContext = mContextProvider.get();
	  }

5.如果纯粹用@Inject提供依赖的实例，那么Component是如何查找依赖的呢？
对于@Inject标注的类，会生成一个对应的Factoty，如User会生成一个User_Factory

	public enum User_Factory implements Factory<User> {
	  INSTANCE;
	
	  @Override
	  public User get() {
	    return new User();
	  }
	
	  public static Factory<User> create() {
	    return INSTANCE;
	  }
	}


	public interface Factory<T> extends Provider<T> {
	}

	public interface Provider<T> {
	//这个Provider就是提供实例的。
	    T get();
	}



# 一些细节
1.在Activity或者其他被注入的类中，只有标记的@Inject才会生成对应的MembersInjector，这个类中管理被注解的类。
2.被@Inject标记的构造函数的类，会生成一个Factory

	public class TestEntity {
	    public String desc;
	
	    public TestEntity() {
	    }
	    /**
	     * 只要被@Inject标记编译后就会生成一个类{@link TestEntity_Factory},并且如果有带参的构造的话就会生成一个方法
	     * public static Factory<TestEntity> create(Provider<String> descProvider)
	     * 表示需要其它的地方提供这个参数。
	     */
	    @Inject
	    public TestEntity(String desc) {
	        this.desc = desc;
	    }
	}

如果只标记无参的构造函数，是一个枚举类

	public enum TestEntity_Factory implements Factory<TestEntity> {
	  INSTANCE;
	
	  @Override
	  public TestEntity get() {
	    return new TestEntity();
	  }
	
	  public static Factory<TestEntity> create() {
	    return INSTANCE;
	  }
	}

如果TestEntity中@Inject标记的是一个带参的构造方法。那么生成的类是这样的，是一个final类。

	public final class TestEntity_Factory implements Factory<TestEntity> {
	  private final Provider<String> descProvider;
	
	  public TestEntity_Factory(Provider<String> descProvider) {
	    assert descProvider != null;
	    this.descProvider = descProvider;
	  }
	
	  @Override
	  public TestEntity get() {
	    return new TestEntity(descProvider.get());
	  }
	
	  public static Factory<TestEntity> create(Provider<String> descProvider) {
	    return new TestEntity_Factory(descProvider);
	  }
	}



# 总结：
##1 @Inject
@Inject有两个作用

 - 在需要依赖的类中标记成员变量告诉Dagger这个类型的变量需要一个实例对象。
 - 标记构造函数告诉Dagger我可以提供这个依赖。

缺陷：

 - 只能标记一个构造函数
 - 不能标记第三方我们自己不能修改的类

@Inject标记的类的变量，在编译的时候会生成对应的MembersInjector。这是一个接口

	public interface MembersInjector<T> {
	
	  /**
	   * 给目标类注入依赖到成员变量中
	   * @param instance into which members are to be injected
	   */
	  void injectMembers(T instance);
	}

泛型T代表的就是要需要依赖的目标类，比如MainActivity或者MainFragment。里面的那个方法就是把Dagger提供的依赖赋值给目标类中需要依赖的变量的过程。生成的实现类名也是有规则的，比如注入MainActivity，那么就会生成这样的实现类。

	public final class ModuleTestActivity_MembersInjector
	    implements MembersInjector<ModuleTestActivity> 
