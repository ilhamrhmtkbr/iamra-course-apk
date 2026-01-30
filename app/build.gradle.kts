import java.util.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("dagger.hilt.android.plugin")
    id("kotlin-kapt")
}

// Load local.properties
val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(FileInputStream(localPropertiesFile))
}

// Helper function untuk ambil property
fun getLocalProperty(key: String, defaultValue: String = ""): String {
    return localProperties.getProperty(key) ?: defaultValue
}

android {
    namespace = "com.ilhamrhmtkbr"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.ilhamrhmtkbr"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            storeFile = file("../key/my-release-key.keystore")  // Path ke keystore
            storePassword = getLocalProperty("release.store.password")
            keyAlias = getLocalProperty("release.key.alias")
            keyPassword = getLocalProperty("release.key.password")
        }
    }

    buildTypes {
        debug {
            // Sensitive keys dari local.properties
            buildConfigField("String", "GOOGLE_ID", "\"${getLocalProperty("google.id")}\"")
            buildConfigField(
                "String",
                "MIDTRANS_CLIENT_KEY",
                "\"${getLocalProperty("midtrans.client.key")}\""
            )

            // Image URLs
            buildConfigField(
                "String",
                "INSTRUCTOR_COURSE_IMAGE_URL",
                "\"${getLocalProperty("debug.instructor.image.url")}\""
            )
            buildConfigField(
                "String",
                "USER_PROFILE_IMAGE_URL",
                "\"${getLocalProperty("debug.user.image.url")}\""
            )

            // API URLs
            buildConfigField(
                "String",
                "PUBLIC_API_URL",
                "\"${getLocalProperty("debug.public.api")}\""
            )
            buildConfigField("String", "USER_API_URL", "\"${getLocalProperty("debug.user.api")}\"")
            buildConfigField(
                "String",
                "STUDENT_API_URL",
                "\"${getLocalProperty("debug.student.api")}\""
            )
            buildConfigField(
                "String",
                "INSTRUCTOR_API_URL",
                "\"${getLocalProperty("debug.instructor.api")}\""
            )
            buildConfigField(
                "String",
                "FORUM_API_URL",
                "\"${getLocalProperty("debug.forum.api")}\""
            )

            // WebSocket URLs dengan app keys dari local.properties
            val reverbUserKey = getLocalProperty("reverb.user.app.key")
            val reverbForumKey = getLocalProperty("reverb.forum.app.key")
            buildConfigField(
                "String",
                "REVERB_USER_URL",
                "\"ws://192.168.8.100/user-api/v1/wss/notif/app/$reverbUserKey\""
            )
            buildConfigField(
                "String",
                "REVERB_USER_AUTH",
                "\"http://192.168.8.100/user-api/v1/broadcasting/auth\""
            )
            buildConfigField(
                "String",
                "REVERB_FORUM_URL",
                "\"ws://192.168.8.100/forum-api/v1/wss/notif/app/$reverbForumKey\""
            )
            buildConfigField(
                "String",
                "REVERB_FORUM_AUTH",
                "\"http://192.168.8.100/forum-api/v1/broadcasting/auth\""
            )
        }

        release {
            // Sensitive keys dari local.properties
            buildConfigField("String", "GOOGLE_ID", "\"${getLocalProperty("google.id")}\"")
            buildConfigField(
                "String",
                "MIDTRANS_CLIENT_KEY",
                "\"${getLocalProperty("midtrans.client.key")}\""
            )

            // Image URLs
            buildConfigField(
                "String",
                "INSTRUCTOR_COURSE_IMAGE_URL",
                "\"${getLocalProperty("release.instructor.image.url")}\""
            )
            buildConfigField(
                "String",
                "USER_PROFILE_IMAGE_URL",
                "\"${getLocalProperty("release.user.image.url")}\""
            )

            // API URLs
            buildConfigField(
                "String",
                "PUBLIC_API_URL",
                "\"${getLocalProperty("release.public.api")}\""
            )
            buildConfigField(
                "String",
                "USER_API_URL",
                "\"${getLocalProperty("release.user.api")}\""
            )
            buildConfigField(
                "String",
                "STUDENT_API_URL",
                "\"${getLocalProperty("release.student.api")}\""
            )
            buildConfigField(
                "String",
                "INSTRUCTOR_API_URL",
                "\"${getLocalProperty("release.instructor.api")}\""
            )
            buildConfigField(
                "String",
                "FORUM_API_URL",
                "\"${getLocalProperty("release.forum.api")}\""
            )

            // WebSocket URLs dengan app keys dari local.properties
            val reverbUserKey = getLocalProperty("reverb.user.app.key")
            val reverbForumKey = getLocalProperty("reverb.forum.app.key")
            buildConfigField(
                "String",
                "REVERB_USER_URL",
                "\"wss://api-user.course.iamra.site/v1/wss/notif/app/$reverbUserKey\""
            )
            buildConfigField(
                "String",
                "REVERB_USER_AUTH",
                "\"https://api-user.course.iamra.site/v1/broadcasting/auth\""
            )
            buildConfigField(
                "String",
                "REVERB_FORUM_URL",
                "\"wss://api-forum.course.iamra.site/v1/wss/notif/app/$reverbForumKey\""
            )
            buildConfigField(
                "String",
                "REVERB_FORUM_AUTH",
                "\"https://api-forum.course.iamra.site/v1/broadcasting/auth\""
            )

            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = false
        /**
         * Fungsi isCoreLibraryDesugaringEnabled = false :
         * Jika menulis kode Java "Lama" (Java 7 kebawah): HP Android jadul sampai yang terbaru pasti
         * mengerti kode tanpa perlu bantuan apa-apa.
         * Jika menulis kode Java "Baru" (Java 8+): Karena desugaring dimatikan, maka HP Android
         * versi lama (di bawah minSdk) akan bingung dan aplikasi bisa crash
         */
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    kotlin {
        jvmToolchain(21)
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    packaging {
        jniLibs {
            keepDebugSymbols.add("**/*.so")
        }
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/versions/**"
            excludes += "j$/**"
        }
    }
}

configurations.all {
    exclude(group = "com.android.tools", module = "desugar_jdk_libs")
    exclude(group = "com.android.tools", module = "desugar_jdk_libs_nio")
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Api
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    // Crop Image
    implementation(libs.ucrop)

    // Room components
    implementation(libs.room.runtime)
    kapt(libs.room.compiler)

    // Midtrans
    implementation(libs.midtrans.sanbox)

    // Chart
    implementation(libs.mpandroidchart)

    // Crypto buat amankan token cookie access token
    implementation(libs.security.crypto)

    // Glide, buat ambil image url
    implementation(libs.glide)
    kapt(libs.compiler)

    // ViewPager2, buat onboarding activity
    implementation(libs.viewpager2)

    // Buat Login google
    implementation(libs.play.services.safetynet)
    implementation(libs.play.services.auth)

    // SwipeRefreshLayout, pas scroll kebawah refresh data
    implementation(libs.swiperefreshlayout)

    // CameraX
    implementation(libs.camera.core)
    implementation(libs.camera.camera2)
    implementation(libs.camera.lifecycle)
    implementation(libs.camera.view)

    // ML Kit Barcode Scanning
    implementation(libs.barcode.scanning)

    // Map
    implementation(libs.osmdroid.android)

    // DI
    implementation("com.google.dagger:hilt-android:2.52")
    kapt("com.google.dagger:hilt-compiler:2.52")
}