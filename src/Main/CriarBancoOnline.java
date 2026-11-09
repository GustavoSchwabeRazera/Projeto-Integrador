package Main;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import dao.ConnectionFactory;

// Cria as tabelas no banco online usando os dados do banco.properties.
// Basta rodar uma vez. Se as tabelas já existirem, dá erro e nada é alterado.
public class CriarBancoOnline {

    public static void main(String[] args) throws Exception {

        if (!ConnectionFactory.usaPostgres()) {
            System.out.println("O banco.properties precisa ter tipo=postgresql.");
            return;
        }

        String script = new String(
                Files.readAllBytes(Paths.get("database-postgres.sql")),
                StandardCharsets.UTF_8);

        try (Connection conexao = ConnectionFactory.novaConexao();
             Statement comando = conexao.createStatement()) {

            // Tudo ou nada: se um comando falhar, nenhuma tabela fica criada
            conexao.setAutoCommit(false);

            try {

                comando.execute(script);

                conexao.commit();

                System.out.println("Banco criado com sucesso!");

            } catch (SQLException e) {

                conexao.rollback();

                System.out.println("Nada foi alterado. Erro: " + e.getMessage());
            }
        }
    }
}
