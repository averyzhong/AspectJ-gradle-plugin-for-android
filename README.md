# AspectJ-gradle-plugin-for-android


AspectJ-gradle-plugin-for-android is a gradle plugin that integrates AspectJ into Android applications quickly.
As long as you use this plugin, your project will immediately have the capability of AOP(Aspect Oriented Programming).


[ ![Download](https://api.bintray.com/packages/averyzhong/AndroidRepo/AspectJ-gradle-plugin-for-android/images/download.svg?version=1.0.0) ](https://bintray.com/averyzhong/AndroidRepo/AspectJ-gradle-plugin-for-android/1.0.0/link)

# How to use?
1. Add `classpath 'com.avery.android.aspectj:aspectj-plugin:1.0.0'` to build.gradle in the project root directory

```
buildscript {
    repositories {
         google()
         jcenter()
               
    }    
    dependencies {
        ......
        classpath 'com.avery.android.aspectj:aspectj-plugin:1.0.0'
        ......  
    }
    ......
}
 
```

2. Add `apply plugin: 'com.avery.android.aspectj'` to the build.gradle in the app directory

```
    apply plugin: 'com.avery.android.aspectj'
    
    android {
        ......
    }
    dependencies {
        ......
    }
 
```

# Disable WeaveLog file generation
You can disable the WeaveLog file generation by adding the below code.

``` 
    android {
        ......
    }
    
    aspectj {
        generateWeaveLog = false
    }
    
    dependencies {
        ......
    }
```

# Example
Let's print logs in each lifecycle method of MainActivity by using AspectJ.

#### com.avery.android.aspectj.example.MainActivity

```
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}

```
#### Aspect

```
@Aspect
public class ActivityLifecycleLogAspect {
    private static final String TAG = ActivityLifecycleLogAspect.class.getSimpleName();

    @Pointcut("execution(* com.avery.android.aspectj.example.MainActivity.on*(..))")
    public void logActivityLifecycle() {}

    @Before("logActivityLifecycle()")
    public void log(final JoinPoint joinPoint) {
        Log.v(TAG, "log: " + joinPoint.toLongString());
    }

}
```

#### Logcat outputs
```
V/ActivityLifecycleLogAspect: log: execution(protected void com.avery.android.aspectj.example.MainActivity.onCreate(android.os.Bundle))
V/ActivityLifecycleLogAspect: log: execution(protected void com.avery.android.aspectj.example.MainActivity.onStart())
V/ActivityLifecycleLogAspect: log: execution(protected void com.avery.android.aspectj.example.MainActivity.onResume())

```

## License

```
Copyright 2019 AveryZhong

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

