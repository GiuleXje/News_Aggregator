JFLAGS = -g
JC = javac
JVM = java

SOURCE_DIR = main/java
TARGET_DIR = ../target

SRC := $(shell find $(SOURCE_DIR) -name "*.java")

LIBS = libs

CP = $(TARGET_DIR):$(LIBS)/*

.PHONY: build clean run setup

setup:
	@mkdir -p $(TARGET_DIR)

build: setup
	mvn -f ../pom.xml dependency:copy-dependencies -DoutputDirectory=src/$(LIBS)

	$(JC) $(JFLAGS) -sourcepath $(SOURCE_DIR) -cp "$(CP)" -d $(TARGET_DIR) $(SRC)

run: build
	$(JVM) -cp $(CP) Tema1 $(ARGS)

clean:
	rm -rf *.class
	rm -rf $(LIBS)
	rm -rf $(TARGET_DIR)
