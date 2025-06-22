plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.maven.publish)
}

group = project.findProperty("GROUP_NAME") as String
version = project.findProperty("VERSION_NAME") as String

android {
    namespace = "com.kivpson.extensions.kivstore.flow_support"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    api(project(":kivstore"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlin.reflect)
    implementation(libs.datastore.preferences)
}

publishing {
    publications {
        create<MavenPublication>("release") {
            groupId = project.group.toString()
            artifactId = "flow-support"
            version = project.version.toString()

            afterEvaluate {
                from(components["release"])
            }
        }
    }
}