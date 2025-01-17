// Top-level build file where you can add configuration options common to all sub-projects/modules.

// add for firebase
buildscript{
    plugins{
        ("classpath 'com.google.gms:goolge-services:4.4.2'")
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.google.gms.google.services) apply false
    // add for firebase
    ("id 'com.google.gms.google-services' version '4.4.2' apply false")
}