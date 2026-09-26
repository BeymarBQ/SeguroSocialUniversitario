package com.si2.segsocu;

/**
 * Punto de entrada del jar ejecutable (target/segSocU-executable.jar).
 *
 * Esta clase NO extiende javafx.application.Application a propósito:
 * cuando un jar "fat/uber" se ejecuta con `java -jar`, si la clase del
 * manifest (Main-Class) extiende Application directamente, el runtime de
 * Java a veces no detecta los módulos de JavaFX y lanza
 * "Error: JavaFX runtime components are missing". Delegar el arranque
 * a App.main(...) desde una clase separada evita ese problema.
 */
public class Main {
    public static void main(String[] args) {
        App.main(args);
    }
}
