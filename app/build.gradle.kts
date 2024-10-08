plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs.kotlin")


}

android {
    namespace = "com.luisavillacorte.gosportapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.luisavillacorte.gosportapp"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }



    dependencies {


        implementation("androidx.navigation:navigation-fragment-ktx:2.7.0")
        implementation("androidx.navigation:navigation-ui-ktx:2.7.0")

        implementation("androidx.activity:activity-ktx:1.6.0")
        implementation("com.squareup.okhttp3:okhttp:4.9.0")
        implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")
        implementation("com.squareup.okhttp3:okhttp-urlconnection:4.9.0")
        implementation("com.google.android.material:material:1.4.0")
        implementation("com.squareup.retrofit2:retrofit:2.9.0")
        implementation("com.squareup.retrofit2:converter-gson:2.9.0")
        implementation("com.google.code.gson:gson:2.8.9")
        implementation("androidx.cardview:cardview:1.0.0")
        implementation("com.github.bumptech.glide:glide:4.15.1")
        implementation(libs.androidx.core.ktx)
        implementation(libs.androidx.appcompat)
        implementation(libs.material)
        implementation(libs.androidx.activity)
        implementation(libs.androidx.constraintlayout)
        implementation(libs.androidx.leanback)
        implementation(libs.androidx.runtime.saved.instance.state)
    implementation ( "androidx.activity:activity-ktx:1.6.0")
    implementation ("com.squareup.okhttp3:okhttp:4.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.0")
    implementation ("com.squareup.okhttp3:okhttp-urlconnection:4.9.0")
    implementation ("com.google.android.material:material:1.4.0")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.google.code.gson:gson:2.8.9")
    implementation ("androidx.cardview:cardview:1.0.0")
    implementation("com.github.bumptech.glide:glide:4.15.1")
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation ("androidx.core:core-ktx:1.6.0")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.leanback)
    implementation(libs.androidx.runtime.saved.instance.state)


//    implementation(libs.androidx.navigation.fragment.ktx)
//    implementation(libs.androidx.navigation.ui.ktx)
        testImplementation(libs.junit)
        androidTestImplementation(libs.androidx.junit)
        androidTestImplementation(libs.androidx.espresso.core)



        implementation("com.github.bumptech.glide:glide:4.15.1")
        implementation("com.google.android.material:material:1.10.0")
        implementation(libs.androidx.foundation.android)

        testImplementation(libs.junit)
        androidTestImplementation(libs.androidx.junit)
        androidTestImplementation(libs.androidx.espresso.core)

        implementation("com.squareup.picasso:picasso:2.8")
    }
}