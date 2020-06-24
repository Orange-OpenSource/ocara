# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\My Program Files\android-sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradlee.
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

-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontpreverify
-verbose
-repackageclasses

## Application classes that will be serialized/deserialized over Gson
-keep class com.orange.ocara.data.net.model.** { *; }
-keep class com.orange.ocara.data.cache.db.** { *; }

## Application classes used by jMustache
-keep class com.orange.ocara.ui.intent.export.docx.** { *; }

## Twowayview
-keep class org.lucasr.twowayview.** { *; }

## Retrofit 2
-dontnote retrofit2.Platform
-dontwarn retrofit2.Platform$Java8
-keepattributes Signature
-keepattributes Exceptions
-dontwarn okio.**

## Android Annotations
-dontwarn org.springframework.**

## Picasso
-dontwarn com.squareup.okhttp.**

## ActiveAndroid
-keep class com.activeandroid.** { *; }
-keep class com.activeandroid.**.** { *; }
-keep class * extends com.activeandroid.Model
-keep class * extends com.activeandroid.serializer.TypeSerializer

## Proguard configuration for Jackson 2.x (fasterxml package instead of codehaus package)
-keep @com.fasterxml.jackson.annotation.JsonIgnoreProperties class * { *; }
-keep @com.fasterxml.jackson.annotation.JsonCreator class * { *; }
-keep @com.fasterxml.jackson.annotation.JsonValue class * { *; }
-keep class org.codehaus.** { *; }
-keepnames class com.fasterxml.jackson.** { *; }
-keepclassmembers public final enum com.fasterxml.jackson.annotation.JsonAutoDetect$Visibility {
    public static final com.fasterxml.jackson.annotation.JsonAutoDetect$Visibility *;
}
-keep class com.fasterxml.jackson.annotation.** { *; }
-dontwarn com.fasterxml.jackson.databind.**
-dontwarn org.springframework.**

## The -optimizations option disables some arithmetic simplifications that Dalvik 1.0 and 1.5 can't handle.
##-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*,!method/propagation/parameter
-keepattributes SourceFile,LineNumberTable,InnerClasses

## Google play services proguard rules:
-keep class * extends java.util.ListResourceBundle {
    protected Object[][] getContents();
}

-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
    public static final *** NULL;
}

-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
    @com.google.android.gms.common.annotation.KeepName *;
}

-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}
## because google play service gets stripped of unnessecary methods and code dont warn
-dontwarn com.google.android.gms.**

## Ignore overridden Android classes
-dontwarn android.**
-keep class android.**

## Ignore imported library references
-dontwarn android.support.v4.**
-keep class android.support.v4.** { *; }

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.preference.Preference

-keepclasseswithmembernames class * {
    native <methods>;
}

## Keep all public constructors of all public classes, but still obfuscate+optimize their content.
## This is necessary because optimization removes constructors which are called through XML.
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context);
}
-keepclassmembers class * extends android.app.Activity {
    public void *(android.view.View);
}
-keepclassmembers class **.R$* {
    public static <fields>;
}

## Keep serializable objects
-keepclassmembers class * implements java.io.Serializable {
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

-keep class * extends java.util.ListResourceBundle {
    protected Object[][] getContents();
}

## support-v4
-dontwarn android.support.v4.**
-keep class android.support.v4.app.** { *; }
-keep interface android.support.v4.app.** { *; }

## support-v7
-dontwarn android.support.v7.**
-keep class android.support.v7.internal.** { *; }
-keep interface android.support.v7.internal.** { *; }
-keep class android.support.v7.** { *; }

## Gson
-keep class com.google.gson.** { *; }
-keep class com.google.inject.** { *; }
-dontwarn com.google.gson.**

## For using GSON @Expose annotation
-keepattributes *Annotation*

## Gson specific classes
-keep class sun.misc.Unsafe { *; }

## OkHttp 3
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
## A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

## Joda time
-keep public class org.joda.time.** {public private protected *;}
-dontwarn org.joda.time.**

## Poi (improve it, using 14K methods)
-keep public class org.apache.poi.** {*;}
-dontwarn org.apache.poi.**

## About Library
-keep class .R
-keep class **.R$* {
    <fields>;
}

## PDF Viewer
-keep class com.shockwave.**