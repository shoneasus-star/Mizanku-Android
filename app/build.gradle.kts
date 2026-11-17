plugins {
 id("com.android.application")
 id("org.jetbrains.kotlin.android")
}
android {
 namespace = "id.mizanku.app"
 compileSdk = 35
 defaultConfig { applicationId = "id.mizanku.app"; minSdk = 23; targetSdk = 35; versionCode = 2; versionName = "0.2.0" }
 compileOptions { sourceCompatibility = JavaVersion.VERSION_17; targetCompatibility = JavaVersion.VERSION_17 }
}
kotlin { jvmToolchain(17) }
dependencies { implementation("com.journeyapps:zxing-android-embedded:4.3.0") }
