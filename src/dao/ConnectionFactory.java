package dao;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

// Os dados do banco online ficam em banco.properties, na pasta do projeto, e
// esse arquivo não vai para o Git. Sem ele, o sistema usa o MySQL local.
public class ConnectionFactory {

    private static final Path ARQUIVO_CONFIGURACAO = Paths.get("banco.properties");

    // Servidores online derrubam conexões paradas: depois desse tempo sem uso
    // (em milissegundos) a conexão é testada antes de ser entregue
    private static final long TESTAR_APOS_MS = 30_000;
    private static final int TEMPO_TESTE_SEGUNDOS = 5;

    // Faz CURRENT_DATE e CURRENT_TIMESTAMP usarem o horário de Brasília,
    // mesmo com o servidor em outro fuso. Os tempos limite evitam que
    // uma queda de internet deixe a tela travada para sempre.
    private static final String PARAMETROS_MYSQL =
            "?connectionTimeZone=-03:00"
            + "&forceConnectionTimeZoneToSession=true"
            + "&connectTimeout=15000"
            + "&socketTimeout=30000";

    // No PostgreSQL o driver já envia o fuso do computador.
    // stringtype=unspecified deixa gravar data enviada como texto, como no MySQL.
    private static final String PARAMETROS_POSTGRES =
            "?stringtype=unspecified"
            + "&connectTimeout=15"
            + "&socketTimeout=30";

    private static Connection conn;
    private static long ultimoUso;
    private static Properties configuracao;

    public static Connection getConnection() throws SQLException {

        long agora = System.currentTimeMillis();

        if (conn == null || conn.isClosed() || !continuaViva(agora)) {

            conn = novaConexao();
        }

        ultimoUso = agora;

        return conn;
    }

    private static boolean continuaViva(long agora) throws SQLException {

        return agora - ultimoUso < TESTAR_APOS_MS || conn.isValid(TEMPO_TESTE_SEGUNDOS);
    }

    // No PostgreSQL as tabelas vêm completas do database-postgres.sql
    public static boolean usaPostgres() throws SQLException {

        return "postgresql".equalsIgnoreCase(carregarConfiguracao().getProperty("tipo", "mysql").trim());
    }

    // Conexão separada da compartilhada; quem pede é quem fecha
    public static Connection novaConexao() throws SQLException {

        Properties config = carregarConfiguracao();

        boolean postgres = usaPostgres();

        String host = config.getProperty("host", "localhost").trim();
        String porta = config.getProperty("porta", postgres ? "5432" : "3306").trim();
        String banco = config.getProperty("banco", "capasvivas").trim();
        String usuario = config.getProperty("usuario", "root").trim();
        String senha = config.getProperty("senha", "admin").trim();

        // Pela internet, a senha e os dados só trafegam criptografados
        boolean local = host.equals("localhost");

        String url = postgres
                ? "jdbc:postgresql://" + host + ":" + porta + "/" + banco
                        + PARAMETROS_POSTGRES + (local ? "" : "&sslmode=require")
                : "jdbc:mysql://" + host + ":" + porta + "/" + banco
                        + PARAMETROS_MYSQL + (local ? "" : "&sslMode=REQUIRED");

        return DriverManager.getConnection(url, usuario, senha);
    }

    private static Properties carregarConfiguracao() throws SQLException {

        if (configuracao == null) {

            Properties config = new Properties();

            if (Files.exists(ARQUIVO_CONFIGURACAO)) {

                try (Reader leitor = Files.newBufferedReader(ARQUIVO_CONFIGURACAO, StandardCharsets.UTF_8)) {

                    config.load(leitor);

                } catch (IOException e) {

                    throw new SQLException("Não foi possível ler o arquivo banco.properties.", e);
                }
            }

            configuracao = config;
        }

        return configuracao;
    }
}
