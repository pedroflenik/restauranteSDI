
# GNU Makefile

JAR = /usr/bin/jar
JAVA = /usr/bin/java
JAVAC = /usr/bin/javac

JFLAGS = -g

.SUFFIXES: .java .class

# Rule to compile Java files in root folder
%.class: %.java
	$(JAVAC) $(JFLAGS) $<

# Rule to compile Java files inside wsMercado package
wsMercado/%.class: wsMercado/%.java
	$(JAVAC) $(JFLAGS) $<

# List Java source files in root folder
ROOT_CLASSES = \
	Administracao.java \
	Restaurante.java \
	Mesa.java \
	Preparo.java

# Java source file inside wsMercado package
PKG_CLASSES = \
	wsMercado/wsClientMercado.java

# Targets for class files in root and package folders
ROOT_CLASSFILES = $(ROOT_CLASSES:.java=.class)
PKG_CLASSFILES = $(PKG_CLASSES:.java=.class)

default: classes

classes: $(ROOT_CLASSFILES) $(PKG_CLASSFILES)

clean:
	rm -f *.class wsMercado/*.class
