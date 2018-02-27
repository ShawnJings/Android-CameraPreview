# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

set(lib_src_DIR ${CMAKE_CURRENT_SOURCE_DIR})

include_directories(${CMAKE_SOURCE_DIR}/src/main/cpp/songstudio/libcommon)

add_subdirectory(${lib_src_DIR}/libcommon)
add_subdirectory(${lib_src_DIR}/camera_preview)

add_library(libsongstudio SHARED ChangbaRecordingPreviewScheduler.cpp)

target_link_libraries(libsongstudio

            libcommontool
            libcamerapreview

            GLESv2
            z
            gomp
            android
            log
            EGL)