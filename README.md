# FX City

JavaFX, FXGL, AtlantaFX extension project. Providing useful tools based on our game-dev experiences.  

# Requirement

* JDK 24+

## Maven
If you are using Maven, add the following dependency to the dependencies section of your project descriptor to access the FX City API:
```xml
<dependency>
    <groupId>io.github.chengenzhao</groupId>
    <artifactId>fxcity</artifactId>
    <version>0.5.3</version>
</dependency>
```

## Gradle
```groovy
dependencies {
 compile 'io.github.chengenzhao:fxcity:0.5.3'
}
```

# Demo Project
https://github.com/chengenzhao/fxcity-demo
![Game Demo Screenshot](https://github.com/chengenzhao/fxcity-demo/assets/5525436/70be792b-d454-4613-8280-60ca8e9c6335)
# Porgram Runtime
Javafx is a native dependency so standard JRE(Java runtime) won't work. We need to expand the runtime.
Here is the expanded runtime and Github Action provides download for it.  
https://github.com/chengenzhao/fxgl-runtime
