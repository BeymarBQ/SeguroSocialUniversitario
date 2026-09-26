package com.si2.segsocu.db;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
""
/**
 * Maneja la conexion a la base de datos SQLite del sistema.
 * El archivo segsocu.db se crea junto al ejecutable/jar si no existe.
 *
 * Uso: Database.get().getConnection()
 */
public class Database {

    private static final String DB_FILE = "segsocu.db";
    private static final String URL = "jdbc:sqlite:" + DB_FILE;

    private static Database instance;

    /**
     * IMPORTANTE: cada llamada a getConnection() abre una conexion NUEVA.
     * Los DAO la usan dentro de un try-with-resources, que la cierra al
     * terminar; si aqui devolvieramos siempre la misma Connection, el
     * primer DAO que la use la cerraria para siempre y toda la app
     * quedaria sin base de datos. SQLite soporta perfectamente abrir y
     * cerrar muchas conexiones cortas sobre el mismo archivo .db.
     */
    private Database() {
        try (Connection con = DriverManager.getConnection(URL);
             Statement st = con.createStatement()) {
            st.execute("PRAGMA foreign_keys = ON;");
            runScript(con, "/com/si2/segsocu/schema.sql");
            if (isEmpty(con, "afiliados")) {
                runScript(con, "/com/si2/segsocu/seed.sql");
            }
        } catch (SQLException e) {
            throw new RuntimeException("No se pudo inicializar la base de datos", e);
        }
    }

    public static synchronized Database get() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            Connection con = DriverManager.getConnection(URL);
            try (Statement st = con.createStatement()) {
                st.execute("PRAGMA foreign_keys = ON;");
            }
            return con;
        } catch (SQLException e) {
            throw new RuntimeException("No se pudo abrir conexion a la base de datos", e);
        }
    }

    private boolean isEmpty(Connection con, String table) throws SQLException {
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) AS c FROM " + table)) {
            return rs.next() && rs.getInt("c") == 0;
        }
    }

    /**
     * Ejecuta un archivo .sql (varias sentencias separadas por ';') que
     * viene empaquetado como recurso dentro del jar.
     */
    private void runScript(Connection con, String resourcePath) throws SQLException {
        StringBuilder sql = new StringBuilder();
        try (InputStream is = Database.class.getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new RuntimeException("No se encontro el recurso: " + resourcePath);
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String trimmed = line.trim();
                    if (trimmed.isEmpty() || trimmed.startsWith("--")) {
                        continue;
                    }
                    sql.append(line).append("\n");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error leyendo " + resourcePath, e);
        }

        try (Statement st = con.createStatement()) {
            for (String statement : sql.toString().split(";")) {
                String trimmed = statement.trim();
                if (!trimmed.isEmpty()) {
                    st.execute(trimmed);
                }
            }
        }
    }
}
