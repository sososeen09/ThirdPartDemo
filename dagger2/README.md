# [都是套路——Dagger2没有想象的那么难](http://www.jianshu.com/p/47c7306b2994)

1. 纯粹的@Inject提供依赖
2. 用@Module，@Provides提供依赖，需要@Component中采用modules把Module类加入。
3. 用@Qualifier标识符@Named，同一个类，提供不同的对象。
4. 自定义@Qualifier标识符。
5. @Module和@Inject优先级
6. 自定义@Scope，单例
7. @SubComponent
8. 组织Component
	1. 依赖dependences
	2. 采用@SubComponent
	3. 继承Component