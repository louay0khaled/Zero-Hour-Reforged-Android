# Android ARM64 bootstrap

This directory is the initial Android/Gradle integration for the native SDL3 port. It targets **only**
`arm64-v8a`, pins Android NDK r28c, and asks CMake to build the existing `generals` target as
`libGeneralsReforged.so`. SDL3's Android Java sources and resources are taken from the exact SDL3
source archive pinned by `GeneralsMD/Code/Tools/vendor.sh`; third-party source is not copied into this
repository.

## Prerequisites

- Android SDK with platform 35 and Build Tools installed
- Android NDK **28.2.13676358** (r28c)
- CMake 3.22.1
- JDK 17 and Gradle 8.9 (Android Gradle Plugin 8.7.3)

Set `ANDROID_HOME` (or `ANDROID_SDK_ROOT`) and install the pinned NDK/CMake versions through SDK
Manager. From the repository root, run:

```sh
gradle -p android assembleDebug
```

The Gradle build runs the repository's pinned vendor script before native configure/compile tasks. The
debug APK, if the native target compiles successfully, is written under
`android/app/build/outputs/apk/debug/`.

## Rendering and native entry point

The game uses SDL3's GPU API and Vulkan, not an OpenGL ES renderer. Android loads SDL3's shared library
first, then `libGeneralsReforged.so`; SDL3 invokes the `SDL_main` entry point generated from
`Main/PosixMain.cpp`.

## Current scope / known limitation

This is a **build-system bootstrap**, not a playable Android release yet. The existing desktop POSIX
entry point still assumes desktop install-root selection and desktop lifecycle behavior. Android
storage/data discovery, lifecycle pause/resume, input adaptation, shader/device verification, and
full ARM64 compile/runtime validation remain follow-up work. The project does not include EA game data.
