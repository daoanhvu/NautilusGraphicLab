cl /O2 /EHcs /I%JAVA_HOME%\include /I%JAVA_HOME%\include\win32 /I..\..\FunctionPlotter\FunctionPlotter\app\src\main\jni /I..\..\FunctionPlotter\FunctionPlotter\app\src\main\jni\gm /LD ..\..\FunctionPlotter\FunctionPlotter\app\src\main\jni\camera.cpp nautilus_lab_graphics_Camera3D.cpp /link /OUT:nmath.dll