fetch-source:
	cp ../eclipse-plugin-vhdl/com.eccelerators.plugins.vhdl.parent/com.eccelerators.plugins.vhdl/src/com/eccelerators/plugins/vhdl/Vhdl.xtext com.eccelerators.plugins.vhdl/src/main/java/com/eccelerators/plugins/vhdl/Vhdl.xtext
	cp ../eclipse-plugin-vhdl/com.eccelerators.plugins.vhdl.parent/com.eccelerators.plugins.vhdl/src/com/eccelerators/plugins/vhdl/validation/VhdlValidator.xtend com.eccelerators.plugins.vhdl/src/main/java/com/eccelerators/plugins/vhdl/validation/VhdlValidator.xtend
	cp ../eclipse-plugin-vhdl/com.eccelerators.plugins.vhdl.parent/com.eccelerators.plugins.vhdl/src/com/eccelerators/plugins/vhdl/linking/VhdlLinkingService.xtend com.eccelerators.plugins.vhdl/src/main/java/com/eccelerators/plugins/vhdl/linking/VhdlLinkingService.xtend
	cp ../eclipse-plugin-vhdl/com.eccelerators.plugins.vhdl.parent/com.eccelerators.plugins.vhdl/src/com/eccelerators/plugins/vhdl/scoping/VhdlAbstractDeclarativeScopeProvider.java com.eccelerators.plugins.vhdl/src/main/java/com/eccelerators/plugins/vhdl/scoping/VhdlAbstractDeclarativeScopeProvider.java
	cp ../eclipse-plugin-vhdl/com.eccelerators.plugins.vhdl.parent/com.eccelerators.plugins.vhdl/src/com/eccelerators/plugins/vhdl/scoping/VhdlScopeProvider.java com.eccelerators.plugins.vhdl/src/main/java/com/eccelerators/plugins/vhdl/scoping/VhdlScopeProvider.java

clean:
	./gradlew clean

build:
	./gradlew build
