# R8 rules for PgdQuiz release builds.
# Libraries in use (Room, kotlinx.serialization, Play Billing, Google Mobile Ads, Compose,
# AndroidX lifecycle) ship their own consumer rules; the rules below cover what those can't see
# or are an explicit safety net for app classes.

# --- Crash reports -------------------------------------------------------------------------
# Keep file/line info so Play Console stack traces can be de-obfuscated with the mapping file
# (AGP bundles mapping.txt into the .aab automatically).
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# --- kotlinx.serialization -----------------------------------------------------------------
# Question JSON is parsed into @Serializable DTOs (QuestionDto / QuestionsResponseDto) and the
# Room converter stores the lives map as JSON. The library's own rules normally cover this;
# these keep the generated serializers for the app's own classes regardless.
-keepattributes *Annotation*, InnerClasses
-keepclassmembers @kotlinx.serialization.Serializable class com.tradezmantom.pgdquiz.** {
    *** Companion;
    kotlinx.serialization.KSerializer serializer(...);
}
-if @kotlinx.serialization.Serializable class com.tradezmantom.pgdquiz.**
-keepclassmembers class <1>$Companion {
    kotlinx.serialization.KSerializer serializer(...);
}
-keepclasseswithmembers class com.tradezmantom.pgdquiz.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# --- Enums persisted by name ---------------------------------------------------------------
# QuizType / SubscriptionTier / QuizDifficulty are stored in Room as their names and read back
# with valueOf(). Keep the constants and valueOf/values so existing installs' data still maps.
-keepclassmembers enum com.tradezmantom.pgdquiz.** {
    <fields>;
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# --- Room ----------------------------------------------------------------------------------
# Room's consumer rules keep generated *_Impl classes; keep the entity/DAO/converter shapes too
# since the database file on existing installs must keep matching them.
-keep class com.tradezmantom.pgdquiz.data.database.** { *; }

# Question banks are loaded with Resources.getIdentifier("drainsquestions", "raw", ...), which
# the resource shrinker can't trace. They're kept by res/raw/keep.xml (not a ProGuard rule).
