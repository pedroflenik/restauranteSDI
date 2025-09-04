# GNU Makefile


JAR = /usr/bin/jar
JAVA = /usr/bin/java
JAVAC = /usr/bin/javac

JFLAGS = -g

.SUFFIXES: .java .class
.java.class:
	$(JAVAC) $(JFLAGS) $<

CLASSES = \
    Administracao.java \
    Restaurante.java \
	Mesa.java \
	Preparo.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	rm -f *.class
