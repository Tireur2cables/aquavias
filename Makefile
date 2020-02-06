.PHONY: all run clean

MVN=mvn
CLASSPATH=./target/*.jar
JAVA_OPT=-cp $(CLASSPATH)
JAVA=java $(JAVA_OPT)
TARGET=GameLauncher

# Target all builds the project.
all:
	$(MVN) package assembly:single
	cp target/aquavias-0.1.jar \
	   aquavias.jar

# Target run executes the program and start with target all to build the
# project.
run : all
	$(JAVA) $(TARGET)

# Target clean removes all files produced during build.
clean :
	$(MVN) clean
