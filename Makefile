.PHONY: all run clean run-fast genNiveaux clean-GenNiveaux

MVN=mvn
JARPATH1=target/jeu-0.1-jar-with-dependencies.jar
JARPATH2=GenNiveaux/target/GenNiveaux-0.1-jar-with-dependencies.jar
JAVA_OPT=-jar
JAVA=java $(JAVA_OPT)
TARGET1=aquavias.jar
TARGET2=genNiveaux.jar

# Target all builds the project.
all: genNiveaux
	$(MVN) install assembly:single
	cp $(JARPATH1) \
	   $(TARGET1)

aquavias:
	$(MVN) install assembly:single
	cp $(JARPATH1) \
    	   $(TARGET1)

# Target run executes the program and start with target all to build the
# project. Run just aquavias and not genNiveaux
run : all
	$(JAVA) $(TARGET1)

# Target clean removes all files produced during build.
clean :
	if [ -e $(TARGET1) ]; then rm $(TARGET1); fi;
	$(MVN) clean

# Compile and Run but without testing and genNiveaux
run-fast : clean
	$(MVN) install assembly:single -DskipTests
	cp $(JARPATH1) \
   	$(TARGET1)
	$(JAVA) $(TARGET1)

# Target just GenNiveaux
genNiveaux : clean
	cd GenNiveaux && $(MVN) install assembly:single
	cp $(JARPATH2) \
   	$(TARGET2)

# Target clean removes all files produced during build for genNiveaux.
clean-GenNiveaux : clean
	if [ -e $(TARGET2) ]; then rm $(TARGET2); fi;
	cd GenNiveaux && $(MVN) clean
