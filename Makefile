# Makefile para Proyecto de Punto de Venta (MVC - JavaFX)

# Comandos de Maven
MVN = mvn

# Clase principal (definida en pom.xml, pero se puede sobrescribir aquí)
MAIN_CLASS = application.App

.PHONY: all compile run clean

# 1. Comando para compilar
compile:
	@echo "Compilando el proyecto..."
	$(MVN) compile

# 2. Comando para ejecutar
run:
	@echo "Ejecutando la aplicación..."
	$(MVN) javafx:run

# 3. Comando para compilar y ejecutar en uno solo
all:
	@echo "Compilando y ejecutando..."
	$(MVN) compile javafx:run

# Comando para limpiar archivos generados
clean:
	@echo "Limpiando archivos temporales..."
	$(MVN) clean
