# GNU Makefile

# Correct tool paths
JAR = /usr/bin/jar
JAVA = /usr/bin/java
JAVAC = /usr/bin/javac

# Compilation flags
JFLAGS = -g

# Pattern rule to compile .java to .class
.SUFFIXES: .java .class
.java.class:
	$(JAVAC) $(JFLAGS) $<

# Java source files
CLASSES = \
    Administracao.java \
    Restaurante.java \
	Mesa.java \
	Preparo.java

# Default target
default: classes

# Compile all class files
classes: $(CLASSES:.java=.class)

# Clean up
clean:
	rm -f *.class
