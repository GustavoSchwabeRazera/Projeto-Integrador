package View;

// Mostra os telefones sempre no mesmo formato: (47) 12345-6789
public final class Telefone {

    private Telefone() {
    }

    public static String formatar(String telefone) {

        if (telefone == null) {
            return "";
        }

        String digitos = telefone.replaceAll("\\D", "");

        // Tira o +55 do Brasil, se tiver
        if (digitos.startsWith("55") && (digitos.length() == 12 || digitos.length() == 13)) {
            digitos = digitos.substring(2);
        }

        // Celular: (47) 12345-6789
        if (digitos.length() == 11) {
            return "(" + digitos.substring(0, 2) + ") "
                    + digitos.substring(2, 7) + "-" + digitos.substring(7);
        }

        // Fixo: (47) 1234-5678
        if (digitos.length() == 10) {
            return "(" + digitos.substring(0, 2) + ") "
                    + digitos.substring(2, 6) + "-" + digitos.substring(6);
        }

        // Fora do padrão: mostra como foi cadastrado
        return telefone;
    }
}
