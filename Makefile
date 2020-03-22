.PHONY: all run clean run-fast genNiveau

MVN=mvn
JARPATH1=target/aquavias-0.1-jar-with-dependencies.jar
JARPATH2=target/GenNiveaux-0.1-jar-with-dependencies.jar
JAVA_OPT=-jar
JAVA=java $(JAVA_OPT)
TARGET1=aquavias.jar
TARGET2=genNiveaux.jar

# Target all builds the project.
all: clean
	$(MVN) package assembly:single
	cp $(JARPATH1) \
	   $(TARGET1)

# Target run executes the program and start with target all to build the
# project.
run : all
	$(JAVA) $(TARGET1)

# Target clean removes all files produced during build.
clean :
	if [ -e $(TARGET1) ]; then rm $(TARGET1); fi;
	$(MVN) clean

# Compile and Run but without testing
run-fast : clean
	$(MVN) package assembly:single -DskipTests
	cp $(JARPATH1) \
   	$(TARGET1)
	$(JAVA) $(TARGET1)

genNiveau :
	cd GenNiveaux && $(MVN) package assembly:single
	cp $(JARPATH2) \
   	$(TARGET2)
