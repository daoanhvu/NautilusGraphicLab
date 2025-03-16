cl /O2 /EHcs -D_WIN32 /I%JAVA_HOME%\include /I%JAVA_HOME%\include\win32 ^
    /I..\..\FunctionPlotter\FunctionPlotter\app\src\main\jni ^
    /I..\..\FunctionPlotter\FunctionPlotter\app\src\main\jni\gm ^
    /I..\..\libnmath\cpp2 ^
    /LD ^
    ..\..\FunctionPlotter\FunctionPlotter\app\src\main\jni\camera.cpp ^
    ..\..\libnmath\cpp2\common.cpp ^
    ..\..\libnmath\cpp2\criteria.cpp ^
    ..\..\libnmath\cpp2\SimpleCriteria.cpp ^
    ..\..\libnmath\cpp2\compositecriteria.cpp ^
    ..\..\libnmath\cpp2\StackUtil.cpp ^
    ..\..\libnmath\cpp2\nlablexer.cpp ^
    ..\..\libnmath\cpp2\nlabparser.cpp ^
    ..\..\libnmath\cpp2\nmath_pool.cpp ^
    ..\..\libnmath\cpp2\StringUtil.cpp ^
    ..\..\libnmath\cpp2\nfunction.cpp ^
    jnicamera.cpp ^
    jnifunction.cpp ^
    /link /OUT:nmath.dll

cmake .. -DNMATH_SOURCE_DIR=/Users/vudao/projects/libnmath