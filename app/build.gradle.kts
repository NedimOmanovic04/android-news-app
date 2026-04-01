plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("org.jetbrains.kotlin.kapt")
}

android {
    namespace = "etf.ri.rma.newsfeedapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "etf.ri.rma.newsfeedapp"
        minSdk = 31
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17          // ★ dignuto na 17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"                                      // ★ dignuto na 17
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    /* ---- Compose & UI ---- */
    implementation(libs.coil.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    /* ---- Testovi (JVM) ---- */
    testImplementation(libs.junit)
    testImplementation(kotlin("test"))
    testImplementation("androidx.room:room-testing:2.5.1")

    /* ---- Instrumentacijski testovi ---- */
    androidTestImplementation(libs.androidx.junit)           // androidx.test.ext:junit:1.1.5
    androidTestImplementation(libs.androidx.espresso.core)   // Espresso
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)  // Compose UI test
    androidTestImplementation("androidx.test:runner:1.5.2")  // ★ VAŽNO – AndroidJUnitRunner
    androidTestImplementation("org.jetbrains.kotlin:kotlin-test-junit")

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    /* ---- Navigation + Lifecycle ---- */
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("androidx.compose.material:material-icons-extended:1.6.3")
    implementation("androidx.compose.runtime:runtime-livedata:1.6.3")

    /* ---- Materijal & dialog ---- */
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.compose.material3:material3:1.2.0-alpha02")
    implementation("io.github.vanpra.compose-material-dialogs:datetime:0.9.0")

    /* ---- Korutine + Serializacija ---- */
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")

    /* ---- Retrofit / OkHttp / Moshi ---- */
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:okhttp-tls:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")
    androidTestImplementation("com.squareup.okhttp3:mockwebserver:4.12.0")
    implementation("com.squareup.moshi:moshi:1.15.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.15.0")

    /* ---- Room ---- */
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
}
