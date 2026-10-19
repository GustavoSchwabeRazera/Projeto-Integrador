package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Evento;

public class EventoDAO {

    // =========================================================
    // LISTAR EVENTOS DO USUÁRIO (agrupados por dia)
    // =========================================================

    public Map<LocalDate, List<Evento>> listarPorUsuario(String cpfUsuario) throws SQLException {

        Map<LocalDate, List<Evento>> eventos = new HashMap<>();

        String sql = "SELECT id_evento, data_evento, descricao FROM Eventos "
                + "WHERE CPF_usuario = ? ORDER BY data_evento, id_evento";

        try (PreparedStatement stmt = ConnectionFactory.getConnection().prepareStatement(sql)) {

            stmt.setString(1, cpfUsuario);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Evento e = new Evento();
                    e.setIdEvento(rs.getInt("id_evento"));
                    e.setData(rs.getDate("data_evento").toLocalDate());
                    e.setDescricao(rs.getString("descricao"));

                    eventos.computeIfAbsent(e.getData(), d -> new ArrayList<>()).add(e);
                }
            }
        }

        return eventos;
    }

    // =========================================================
    // ADICIONAR EVENTO
    // =========================================================

    public void adicionar(String cpfUsuario, LocalDate data, String descricao) throws SQLException {

        String sql = "INSERT INTO Eventos (CPF_usuario, data_evento, descricao) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = ConnectionFactory.getConnection().prepareStatement(sql)) {

            stmt.setString(1, cpfUsuario);
            stmt.setDate(2, java.sql.Date.valueOf(data));
            stmt.setString(3, descricao);

            stmt.executeUpdate();
        }
    }

    // =========================================================
    // REMOVER EVENTO
    // =========================================================

    public void remover(int idEvento) throws SQLException {

        String sql = "DELETE FROM Eventos WHERE id_evento = ?";

        try (PreparedStatement stmt = ConnectionFactory.getConnection().prepareStatement(sql)) {

            stmt.setInt(1, idEvento);

            stmt.executeUpdate();
        }
    }
}
