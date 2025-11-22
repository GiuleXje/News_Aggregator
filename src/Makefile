
JFLAGS = -g
JC = javac
JVM = java

SRC = main/java/Tema1.java

LIBS = libs

CP = .:$(LIBS)/*

.PHONY: build clean run

build:
	mvn -f ../pom.xml dependency:copy-dependencies -DoutputDirectory=src/$(LIBS)
	$(JC) $(JFLAGS) -cp "$(CP)" -d . $(SRC)

run: build
	$(JVM) -cp "$(CP)" Tema1 $(ARGS)

clean:
	rm -rf *.class
	rm -rf $(LIBS)
