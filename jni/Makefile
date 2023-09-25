CXXFLAGS=-m64 -O2
CFLAGS=-m64 -O2
CC=g++
INCLUDE=-I../../FunctionPlotter/FunctionPlotter/app/src/main/jni \
	-I../../FunctionPlotter/FunctionPlotter/app/src/main/jni/gm \
	-I$(JAVA_HOME)/include \
	-I$(JAVA_HOME)/include/win32

SRC=../../FunctionPlotter/FunctionPlotter/app/src/main/jni/camera.cpp \
	nautilus_lab_graphics_Camera3D.cpp

camera.o: $(SRC)
	$(CC) $(CXXFLAGS) -c $(INCLUDE) $(SRC)