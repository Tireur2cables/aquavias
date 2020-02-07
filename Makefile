.PHONY: all run clean

MVN=mvn
CLASSPATH=./target/*.jar
JAVA_OPT=-cp $(CLASSPATH)
JAVA=java $(JAVA_OPT)
TARGET=GameLauncher

# Target all builds the project.
all: clean
	$(MVN) package assembly:single
	cp target/aquavias-0.1-jar-with-dependencies.jar \
	   aquavias.jar

# Target run executes the program and start with target all to build the
# project.
run : all
	$(JAVA) $(TARGET)

# Target clean removes all files produced during build.
clean :
	if [ -e aquavias.jar ]; then rm aquavias.jar; fi;
	$(MVN) clean
