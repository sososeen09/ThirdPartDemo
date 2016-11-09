1.如果不调用addConverterFactory方法添加Converter.Factory对象，那么默认的有一个BuiltInConverters会生成返回ResponseBody对象或者Void。即是接口中Call泛型是

	Call<ResponseBody>
	Call<Void>

2.Retrofit版本2.1后，baseUrl不能结尾必须有“/”，否则会抛出异常。

3.ServiceMethod主要用于将我们接口中的方法转化为一个Request对象


##注意事项：

###1. baseUrl
	1. baseUrl后面要有“/”结尾
	2. path前面最好不要带“/”，然后按照相对路径和baseUrl拼接起来，组成完成的url。


	
**这第一种方式比较好**

	Base URL: http://example.com/api
	Endpoint: foo/bar/
	Result: http://example.com/foo/bar/
	
	Base URL: http://example.com/api/
	Endpoint: /foo/bar/
	Result: http://example.com/foo/bar/
	
	Base URL: http://example.com/
	Endpoint: /foo/bar/
	Result: http://example.com/foo/bar/
	
	Base URL: http://example.com/
	Endpoint: https://github.com/square/retrofit/
	Result: https://github.com/square/retrofit/
		
	Base URL: http://example.com
	Endpoint: //github.com/square/retrofit/
	Result: http://github.com/square/retrofit/ (note the scheme stays 'http')

###2 Retrofit的build()方法中都干了啥
- 调用Retrofit的Builder-->会创建一个Platform，在Android中这个Platform就是Android。在Builder的构造方法中。List<Converter.Factory> converterFactories集合还会添加一个默认的Converter.Factory就是BuiltInConverters。主要是对ResponseBody做处理。默认的是返回ResponseBody和Void。

- 在build()方法中，会对Retrofit的一些成员变量进行初始化。包括：
	- HttpUrl baseUrl，这个是必须的
	- 还有okhttp3.Call.Factory callFactory 注意这个callFactory基本指的就是OkHttpClient，因为OkHttpClient实现了这个Call.Factory接口。
	- List<Converter.Factory> converterFactories 这个主要是指我们通过addConverterFactory()方法添加的Converter.Factory对象，如我们添加的GsonConverterFactory。
	- List<CallAdapter.Factory> adapterFactories，这个主要指我们通过addCallAdapterFactory()方法添加的CallAdapter.Factory对象，如我们添加的RxJavaCallAdapterFactory。在Android中默认会添加一个ExecutorCallAdapterFactory。还有一个DefaultCallAdapterFactory，当callbackExecutor==null的时候会创建这个对象，但是基本上不太可能用得上。因为callbackExecutor==null的时候，Android平台会自动创建一个MainThreadExecutor。也就是下面所说的。
	- Executor callbackExecutor，这个主要是指通过回调的onResponse和onFailed方法在哪个线程，可以自己添加。Retrofit默认实现的是platform.defaultCallbackExecutor()，而这个platform指的就是Android。我们可以查看这个返回的就是Android中的MainThreadExecutor，顾名思义就是主线程的执行者，它的execute方法是这样的最终执行的是**handler.post(Runnable r)**方法。ExecutorCallAdapterFactory的构造方法中默认传递的就是这个Executor（如果不人为指定就是MainThreadExecutor）。


###3 Retrofit中的Call
这个Call有两个子类，一个是OkHttpCall，另一个是ExecutorCallAdapterFactory的内部类ExecutorCallbackCall。

###4 Retrofit创建Service对象
Retrofit通过create(final Class<T> service)返回需要Class对应类型的接口对象。

	  @SuppressWarnings("unchecked") // Single-interface proxy creation guarded by parameter safety.
	  public <T> T create(final Class<T> service) {
	    Utils.validateServiceInterface(service);
	    if (validateEagerly) {
	      eagerlyValidateMethods(service);
	    }
	//根据Proxy类创建实例
	    return (T) Proxy.newProxyInstance(service.getClassLoader(), new Class<?>[] { service },
	        new InvocationHandler() {
	          private final Platform platform = Platform.get();
	
	          @Override public Object invoke(Object proxy, Method method, Object... args)
	              throws Throwable {
	            // If the method is a method from Object then defer to normal invocation.
	            if (method.getDeclaringClass() == Object.class) {
	              return method.invoke(this, args);
	            }
	            if (platform.isDefaultMethod(method)) {
	              return platform.invokeDefaultMethod(method, service, proxy, args);
	            }
				//把注解的方法改成OkHttp的请求并封装在ServiceMethod中
	            ServiceMethod serviceMethod = loadServiceMethod(method);
				
				//创建OkhttpCall对象，这个OkhttpCall实现了Retrofit中的接口Call
	            OkHttpCall okHttpCall = new OkHttpCall<>(serviceMethod, args);
				//这个serviceMethod.callAdapter就是retrofit中的callAdapter，我们知道，如果不添加RxJavaCallAdapterFactory的话这个callAdapte指的就是ExecutorCallAdapterFactory通过get方法返回的CallAdapter。当调用adapter(Call call)方法时，实际调用的就是重写的一个方法
	            return serviceMethod.callAdapter.adapt(okHttpCall);
	          }
	        });
	  }

	#Retrofit
	 public CallAdapter<?> nextCallAdapter(CallAdapter.Factory skipPast, Type returnType,
	      Annotation[] annotations) {
	    checkNotNull(returnType, "returnType == null");
	    checkNotNull(annotations, "annotations == null");
	
	    int start = adapterFactories.indexOf(skipPast) + 1;
	    for (int i = start, count = adapterFactories.size(); i < count; i++) {
		//调用ExecutorCallAdapterFactory的get方法返回CallAdapter
	      CallAdapter<?> adapter = adapterFactories.get(i).get(returnType, annotations, this);
	      if (adapter != null) {
	        return adapter;
	      }
	    }

	
	#ExecutorCallAdapterFactory
	 @Override
	  public CallAdapter<Call<?>> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
	    if (getRawType(returnType) != Call.class) {
	      return null;
	    }
	    final Type responseType = Utils.getCallResponseType(returnType);
	    return new CallAdapter<Call<?>>() {
	      @Override public Type responseType() {
	        return responseType;
	      }
	
	      @Override public <R> Call<R> adapt(Call<R> call) {
			//可以看到，上面调用的adapter方法，最终会调用到这里，相当于构建了一个ExecutorCallbackCall对象。我们可以知道，我们在Service接口中写的方法返回值都是Call<T>等，返回的实际就是ExecutorCallbackCall
	        return new ExecutorCallbackCall<>(callbackExecutor, call);
	      }
	    };
	  }

当调用异步请求Call.enqueue的时候，实际调用的就是ExecutorCallbackCall中的enqueue方法，是这样的，这个delegate实际上就就是OkHttpCall，而OkHttpCall是对OkHttp3中的Call进行了一层封装。这个callbackExecutor指的是MainThreadExecutor，那么可以看出最终执行的Runnable其实就相当于是调用了Handler.post(Runnable r)。在onResponse和 onFailure中均做了封装，把Retrofit的CallBack回调到主线程。

	#ExecutorCallbackCall
	 @Override public void enqueue(final Callback<T> callback) {
	      if (callback == null) throw new NullPointerException("callback == null");
	
	      delegate.enqueue(new Callback<T>() {
	        @Override public void onResponse(Call<T> call, final Response<T> response) {

			//相当于是调用了Handler.post(Runnable r)。
	          callbackExecutor.execute(new Runnable() {
	            @Override public void run() {
	              if (delegate.isCanceled()) {
	                // Emulate OkHttp's behavior of throwing/delivering an IOException on cancellation.
	                callback.onFailure(ExecutorCallbackCall.this, new IOException("Canceled"));
	              } else {
	                callback.onResponse(ExecutorCallbackCall.this, response);
	              }
	            }
	          });
	        }
	
	        @Override public void onFailure(Call<T> call, final Throwable t) {
	          callbackExecutor.execute(new Runnable() {
	            @Override public void run() {
	              callback.onFailure(ExecutorCallbackCall.this, t);
	            }
	          });
	        }
	      });
	    }

