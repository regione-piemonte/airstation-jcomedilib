# Uncomment this to override your JAVA_HOME environment variable
JAVA_HOME = /usr/lib/jvm/java-8-oracle
COMEDI_HOME = ./
SRC_DIR = ./src/c
DST_DIR = ./dist/dev/bin/jcomedilib
CC = gcc
RM = rm
DEFINES =
CCFLAGS = -Wall -O2 -g -fPIC -c
LDFLAGS = -Wall -O2 -g --shared
INCLUDES = -I${JAVA_HOME}/include -I${JAVA_HOME}/include/linux
LIBDIRS32 = -L${COMEDI_HOME}/lib/lib32
LIBDIRS64 = -L${COMEDI_HOME}/lib/lib64
LDLIBS = -ldl -lcomedi

all: ${DST_DIR}/lib32/comedijni.o ${DST_DIR}/lib64/comedijni.o ${DST_DIR}/lib32/libcomedijni.so ${DST_DIR}/lib64/libcomedijni.so

clean:
	$(RM) -f ${DST_DIR}/lib32/libcomedijni.so
	$(RM) -f ${DST_DIR}/lib64/libcomedijni.so

${DST_DIR}/lib32/comedijni.o: ${SRC_DIR}/comedijni.c ${SRC_DIR}/it_csi_aria_jcomedilib_Comedi.h
	${CC} $(CCFLAGS) -m32 $(DEFINES) $(INCLUDES) -I${COMEDI_HOME}/lib/include32 -o $@ $<

${DST_DIR}/lib64/comedijni.o: ${SRC_DIR}/comedijni.c ${SRC_DIR}/it_csi_aria_jcomedilib_Comedi.h
	${CC} $(CCFLAGS) -m64 $(DEFINES) $(INCLUDES) -I${COMEDI_HOME}/lib/include64 -o $@ $<

${DST_DIR}/lib32/libcomedijni.so: ${DST_DIR}/lib32/comedijni.o
	${CC} ${LDFLAGS} -m32 ${LIBDIRS32} -o $@ $< ${LDLIBS}

${DST_DIR}/lib64/libcomedijni.so: ${DST_DIR}/lib64/comedijni.o
	${CC} ${LDFLAGS} -m64 ${LIBDIRS64} -o $@ $< ${LDLIBS}
