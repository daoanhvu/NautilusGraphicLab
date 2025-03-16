cmake_minimum_required(VERSION 3.10)

# Set the project name and version
project(NMathJNI VERSION 1.0 LANGUAGES C CXX)

# Specify the C++ standard
set(CMAKE_CXX_STANDARD 11)
set(CMAKE_CXX_STANDARD_REQUIRED True)

# Include directories
include_directories(${CMAKE_SOURCE_DIR}/include)
include_directories(${NMATH_SOURCE_DIR}/include)

# Find JNI package
find_package(JNI REQUIRED)
include_directories(${JNI_INCLUDE_DIRS})

message("Native code source: " ${CMAKE_SOURCE_DIR})

if( DEFINED BUILD_TEST )
    add_library(testjni SHARED
        test_build_jni.cpp
    )
    target_link_libraries(testjni ${JNI_LIBRARIES})

    # Specify the output directory for the shared library
    set_target_properties(testjni PROPERTIES
        LIBRARY_OUTPUT_DIRECTORY ${CMAKE_SOURCE_DIR}/../natives
    )
else()
  include_directories(
    ${NMATH_SOURCE_DIR}/glm
    ${NMATH_SOURCE_DIR}/camera
    ${NMATH_SOURCE_DIR}/include
    ${CMAKE_SOURCE_DIR}/include
  )

  set(NMATH2_SRC
    ${NMATH_SOURCE_DIR}/src/internal_common.cpp
    ${NMATH_SOURCE_DIR}/src/nlablexer.cpp
    ${NMATH_SOURCE_DIR}/src/utf8.cpp
    ${NMATH_SOURCE_DIR}/src/StackUtil.cpp
  )

  add_library(nmath2 SHARED ${NMATH2_SRC})

  set(CAMERA_SRC
      ${NMATH_SOURCE_DIR}/camera/camera.cpp
    )
  add_library(camera SHARED ${CAMERA_SRC})

  # Add the native library
  add_library(jni_nmath SHARED
      jnifunction.cpp
      jnicamera.cpp
  )

  # Link the native library with JNI
  target_link_libraries(jni_nmath nmath2 camera ${JNI_LIBRARIES})

  # Specify the output directory for the shared library
  set_target_properties(jni_nmath PROPERTIES
      LIBRARY_OUTPUT_DIRECTORY ${CMAKE_SOURCE_DIR}/../natives
  )
endif()
