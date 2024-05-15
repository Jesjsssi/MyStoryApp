plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.ksp)
    alias(libs.plugins.parcelize)
    alias(libs.plugins.googleAndroidLibrariesMapsplatformSecretsGradlePlugin)
}

android {
    namespace = "com.dicoding.mystoryapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.dicoding.mystoryapp"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField(
            "String",
            "API_KEY",
            "\"9c2e0d4802c9486eb74509b376dadd37\""
        )
        buildConfigField(
            "String",
            "BASE_URL",
            "\"https://story-api.dicoding.dev/v1/\""
        )
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn")
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    testOptions {
        animationsDisabled = true
        unitTests {
            isReturnDefaultValues = true
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //lottie
    implementation(libs.lottie)

    //Glide
    implementation(libs.glide)
    annotationProcessor(libs.glideCompiler)

    //Retrofit
    implementation(libs.retrofit)
    implementation(libs.converterGson)
    implementation(libs.loggingInterceptor)
    implementation(libs.lifecycleRuntimeKtx)

    //coroutines
    implementation(libs.lifecycleViewModelKtx)
    implementation(libs.coroutinesCore)
    implementation(libs.lifecycleLivedataKtx)
    implementation(libs.coroutinesAndroid)

    //DataStore
    implementation(libs.datastorePreferences)

    //room
    implementation(libs.roomKtx)
    implementation(libs.roomRuntime)
    ksp(libs.roomCompiler)

    //circle image
    implementation(libs.circleImageView)

    //google maps
    implementation(libs.play.services.maps)
    implementation(libs.playServicesLocation)

    //paging
    implementation(libs.pagingRuntimeKtx)
    implementation(libs.pagingCommonKtx)
    implementation(libs.roomPaging)

    //testing
    testImplementation(libs.coreTesting)
    testImplementation(libs.coroutinesTest)
    testImplementation(libs.mockitoCore)
    testImplementation(libs.mockitoInline)
    androidTestImplementation(libs.androidxTestExtJunit)
    androidTestImplementation(libs.mockwebserver)
    androidTestImplementation(libs.okhttpTls)
    implementation(libs.espressoIdlingResource)

}