# بناء APK باستخدام Capacitor

المتطلبات: Node.js (الإصدار 18 أو أحدث)، وAndroid Studio.

1. من داخل هذا المجلد، ثبّت الحزم:
   npm install
2. أضف مشروع Android (يُنشأ مرة واحدة فقط):
   npm run android:add
3. انسخ ملفات الويب إلى مشروع Android:
   npm run android:sync
4. افتح المشروع في Android Studio:
   npm run android:open
5. من القائمة: Build > Build APK(s)، وستجد الملف في android/app/build/outputs/apk/debug/

ملاحظة: بعد أي تعديل على ملفات الويب، شغّل الخطوة 3 مرة أخرى قبل إعادة البناء.
