.PHONY: all run clean run-fast

MVN=mvn
JARPATH=target/aquavias-0.1-jar-with-dependencies.jar
JAVA_OPT=-jar
JAVA=java $(JAVA_OPT)
TARGET=aquavias.jar

# Target all builds the project.
all: clean
	$(MVN) package assembly:single
	cp $(JARPATH) \
	   $(TARGET)

# Target run executes the program and start with target all to build the
# project.
run : all
	$(JAVA) $(TARGET)

# Target clean removes all files produced during build.
clean :
	if [ -e $(TARGET) ]; then rm $(TARGET); fi;
	$(MVN) clean

# Compile and Run but without testing
run-fast : clean
	$(MVN) package assembly:single -DskipTests
	cp $(JARPATH) \
   	$(TARGET)
	$(JAVA) $(TARGET)
