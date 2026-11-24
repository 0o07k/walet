# محفظتي اليومية - مشروع Android

تطبيق Android بسيط يعرض صفحة التطبيق داخل WebView، ويعمل بلا إنترنت بعد أول تشغيل.
البيانات تُحفظ على الجهاز، والنطق الصوتي (en-US) يستخدم محرك النطق في Android.

## الملفات
- `app/src/main/assets/index.html`: واجهة التطبيق كاملة (كل الشاشات والميزات).
- `app/src/main/java/com/vocabwallet/app/MainActivity.java`: الغلاف الأصلي، ويوفر النطق الصوتي والنسخ عبر `AndroidBridge`.
- `app/src/main/AndroidManifest.xml`: إعدادات التطبيق.
- `app/build.gradle`: الإصدار `1.0.0`، والحد الأدنى Android 7.0 (API 24).

## بناء نسخة APK للتثبيت
1. ثبّت Android Studio (Ladybug أو أحدث).
2. من القائمة: File > Open، ثم اختر مجلد `VocabWallet`.
3. انتظر حتى ينتهي Gradle Sync. سيُنشئ Android Studio ملفات Gradle Wrapper تلقائيًا.
4. من القائمة: Build > Build Bundle(s) / APK(s) > Build APK(s).
5. ستجد الملف في: `app/build/outputs/apk/debug/app-debug.apk`.
6. انقل الملف إلى الهاتف وثبّته (فعّل "تثبيت من مصادر غير معروفة" إن طُلب منك).

لإصدار نهائي للنشر في Google Play: Build > Generate Signed App Bundle / APK، وأنشئ مفتاح توقيع خاصًا بك واحفظه في مكان آمن.

## ملاحظات
- في أول تشغيل قد يحتاج الخط العربي إلى إنترنت لتحميله، وإن لم يتوفر يستخدم التطبيق خطًا احتياطيًا للنظام.
- النطق يحتاج صوت English (United States) مثبتًا. من إعدادات الهاتف: اللغة والإدخال > تحويل النص إلى كلام.
