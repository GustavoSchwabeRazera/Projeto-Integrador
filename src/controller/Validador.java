package controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

// Validações usadas no cadastro e na alteração de usuário
public final class Validador {

    private static final DateTimeFormatter FORMATO_DATA =
            DateTimeFormatter.ofPattern("dd/MM/uuuu")
                    .withResolverStyle(ResolverStyle.STRICT);

    private Validador() {
    }


    // Devolve a mensagem do primeiro erro, ou null se estiver tudo certo
    public static String validarUsuario(
            String nome,
            String email,
            String telefone,
            String dataNascimento) {

        if (nome.length() > 50) {
            return "O nome deve ter no máximo 50 caracteres.";
        }

        if (email.length() > 50) {
            return "O e-mail deve ter no máximo 50 caracteres.";
        }

        if (!email.matches("[^@\\s]+@[^@\\s]+\\.[^@\\s]+")) {
            return "Digite um e-mail válido. Exemplo: nome@email.com";
        }

        if (telefone.length() > 20 || !telefone.matches("[0-9 ()+-]+")) {
            return "Digite um telefone válido, usando apenas números.";
        }

        int digitos = telefone.replaceAll("\\D", "").length();

        if (digitos < 10 || digitos > 11) {
            return "O telefone deve ter DDD e número (10 ou 11 dígitos).";
        }

        if (!dataNascimentoValida(dataNascimento)) {
            return "Digite uma data de nascimento válida (dd/mm/aaaa).";
        }

        return null;
    }


    private static boolean dataNascimentoValida(String data) {

        try {

            LocalDate nascimento = LocalDate.parse(data, FORMATO_DATA);

            return nascimento.getYear() >= 1900
                    && !nascimento.isAfter(LocalDate.now());

        } catch (DateTimeParseException e) {

            return false;
        }
    }
}
