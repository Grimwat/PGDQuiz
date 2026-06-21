# Gson specific rules
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn sun.misc.**

-keep class com.google.gson.reflect.TypeToken { *; }
-keep,allowobfuscation,allowshrinking class com.google.gson.reflect.TypeToken
-keep,allowobfuscation,allowshrinking class * extends com.google.gson.reflect.TypeToken

# Keep your data classes used with Gson
-keep class com.tradezmantom.pgdquiz.ui.data.QuizType { *; }
-keep class com.tradezmantom.pgdquiz.ui.data.SubscriptionTier { *; }