@file:Suppress("DSL_SCOPE_VIOLATION")

import org.jetbrains.compose.desktop.application.dsl.TargetFormat


plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose)
    alias(libs.plugins.moko.resources)
}

kotlin {
    androidTarget()

    jvm("desktop")

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    js(IR) {
        browser {
            useCommonJs()
        }
        binaries.executable()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.materialIconsExtended)
                implementation(compose.components.uiToolingPreview)
                implementation(compose.components.resources)

                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.datetime)

                api(libs.moko.mvvm.core)
                api(libs.moko.mvvm.compose)
                implementation(libs.moko.resources)

                implementation(libs.koin.core)
            }
        }
        val commonTest by getting

        val desktopMain by getting {
            dependsOn(commonMain)
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(libs.kotlinx.coroutines.swing)
                implementation(libs.korge.core)
            }
        }

        val androidMain by getting {
            dependsOn(commonMain)
            dependencies {
                implementation(libs.koin.android)
                implementation(libs.androidx.activity.compose)
                implementation(libs.androidx.appcompat)
                implementation(libs.androidx.core.ktx)
            }
        }

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }

        val jsMain by getting {
            dependsOn(commonMain)
        }

        all {
            languageSettings.optIn("org.jetbrains.compose.resources.ExperimentalResourceApi")
        }
    }
}

android {
    compileSdk = libs.versions.androidCompileSdk.get().toInt()
    namespace = "com.mobnetic.newtonstimer"

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")
    sourceSets["main"].apply {
//        assets.srcDir(File(buildDir, "generated/moko/androidMain/assets"))
        res.srcDir(File(buildDir, "generated/moko/androidMain/res"))
    }

    defaultConfig {
        minSdk = libs.versions.androidMinSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlin {
        jvmToolchain(11)
    }
}

compose.desktop {
    application {
        mainClass = "com.mobnetic.newtonstimer.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.mobnetic.newtonstimer"
            packageVersion = "1.0.0"
        }
    }
}

compose.experimental {
    web.application {}
}

multiplatformResources {
    multiplatformResourcesPackage = "com.mobnetic.newtonstimer"
}

// Fix for moko resource
afterEvaluate {
    tasks.matching { it.name == "desktopProcessResources" }.configureEach {
        dependsOn(tasks.matching { it.name == "generateMRdesktopMain" })
    }
    tasks.matching { it.name == "iosSimulatorArm64ProcessResources" }.configureEach {
        dependsOn(tasks.matching { it.name == "generateMRiosSimulatorArm64Main" })
    }
    tasks.matching { it.name == "metadataIosMainProcessResources" }.configureEach {
        dependsOn(tasks.matching { it.name == "generateMRcommonMain" })
    }
    tasks.matching { it.name == "metadataCommonMainProcessResources" }.configureEach {
        dependsOn(tasks.matching { it.name == "generateMRcommonMain" })
    }
    tasks.matching { it.name == "iosX64ProcessResources" }.configureEach {
        dependsOn(tasks.matching { it.name == "generateMRiosX64Main" })
    }
    tasks.matching { it.name == "iosArm64ProcessResources" }.configureEach {
        dependsOn(tasks.matching { it.name == "generateMRiosArm64Main" })
    }
}