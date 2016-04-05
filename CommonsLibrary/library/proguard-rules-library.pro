# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/negwiki/tools/Android-sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-optimizations              !class/unboxing/enum
-adaptresourcefilenames     **.properties,**.gif,**.jpg
-adaptresourcefilecontents  **.properties,META-INF/MANIFEST.MF
-keepattributes             *Annotation*
-keepattributes             Signature
-keepattributes             SourceFile,LineNumberTable

-keepclassmembers public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    void set*(***);
    *** get*();
}

#####################################
######### 第三方库或者jar包 ###########
#####################################

# Needed to keep generic types and @Key annotations accessed via reflection
-keepattributes Signature,RuntimeVisibleAnnotations,AnnotationDefault
-keepclassmembers class * {
  @com.google.api.client.util.Key <fields>;
  @com.google.gson.annotations.Expose <fields>;
  @com.google.gson.annotations.SerializedName <fields>;
}

#Jackson SERIALIZER SETTINGS
-keepclassmembers,allowobfuscation class * {
    @org.codehaus.jackson.annotate.* <fields>;
    @org.codehaus.jackson.annotate.* <init>(...);
}

#Simple XML SERIALIZER SETTINGS
-keepclassmembers,allowobfuscation class * {
    @org.simpleframework.xml.* <fields>;
    @org.simpleframework.xml.* <init>(...);
}


#http://jakewharton.github.io/butterknife/index.html
-dontwarn butterknife.internal.**
-keep class butterknife.** { *; }
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

#https://github.com/greenrobot/EventBus/blob/master/HOWTO.md
-keepclassmembers class ** {
    public void onEvent*(**);
}
# Only required if you use AsyncExecutor
-keepclassmembers class * extends de.greenrobot.event.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

#commons library
-dontwarn java.**
-keep class java.** { *; }

-dontwarn javax.**
-keep class javax.** { *; }

-dontwarn android.**
-keep class android.** { *; }

-dontwarn org.apache.**
-keep class org.apache.** { *; }

-dontwarn com.google.**
-keep class com.google.** { *; }

-dontwarn com.sun.**
-keep class com.sun.** { *; }

-dontwarn org.w3c.**
-keep class org.w3c.** { *; }

-dontwarn org.codehaus.jackson.**
-keep class org.codehaus.jackson.** { *; }

-dontwarn org.springframework.**
-keep class org.springframework.** { *; }

#baidu
-dontwarn com.baidu.**
-keep class com.baidu.** { *; }

#amap
-dontwarn com.amap.**
-keep class com.amap.** { *; }
-dontwarn com.autonavi.**
-keep class com.autonavi.** { *; }
#-dontwarn com.loc.**
#-keep class com.loc.** { *; }

#robospice
-dontwarn com.octo.**
-keep class com.octo.** { *; }

#universal-image-loader
-dontwarn com.nostra13.universalimageloader.**
-keep class com.nostra13.universalimageloader.** { *; }



