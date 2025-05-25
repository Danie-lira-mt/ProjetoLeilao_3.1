
import java.sql.PreparedStatement;
import java.sql.Connection;
import javax.swing.JOptionPane;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutosDAO {

    private Connection conn;

    public boolean conectar() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/uc11?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
                "root",
                "12345aA."
            );
            return true;
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Erro ao conectar: " + e.getMessage());
            return false;
        }
    }

    public int salvar(ProdutosDTO produto) {
        String sql = "INSERT INTO produtos (id, nome, valor, status) VALUES (?, ?, ?, ?)";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, produto.getId());
            st.setString(2, produto.getNome());
            st.setInt(3, produto.getValor());
            st.setString(4, produto.getStatus());

            return st.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao salvar produto: " + e.getMessage());
            return -1;
        }
    }

    public ArrayList<ProdutosDTO> listarTodos() {
        ArrayList<ProdutosDTO> listagem = new ArrayList<>();
        if (!conectar()) {
            System.out.println("Falha ao conectar ao banco");
            return listagem;
        }
        String sql = "SELECT * FROM produtos";
        try (PreparedStatement st = conn.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                ProdutosDTO produto = new ProdutosDTO();
                produto.setId(rs.getInt("id"));
                produto.setNome(rs.getString("nome"));
                produto.setValor(rs.getInt("valor"));
                produto.setStatus(rs.getString("status"));

                listagem.add(produto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return listagem;
    }

    public ProdutosDTO buscarPorId(int id) {
        if (!conectar()) {
            System.out.println("Falha ao conectar ao banco");
            return null;
        }
        String sql = "SELECT * FROM produtos WHERE id = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                ProdutosDTO produto = new ProdutosDTO();
                produto.setId(rs.getInt("id"));
                produto.setNome(rs.getString("nome"));
                produto.setValor(rs.getInt("valor"));
                produto.setStatus(rs.getString("status"));
                rs.close();
                return produto;
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return null;
    }

    public int atualizarStatusPorId(int id, String novoStatus) {
        if (!conectar()) {
            System.out.println("Falha ao conectar ao banco");
            return -1;
        }
        String sql = "UPDATE produtos SET status = ? WHERE id = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, novoStatus);
            st.setInt(2, id);

            return st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        } finally {
            try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

     public ArrayList<ProdutosDTO> listarProdutosVendidos() {
        ArrayList<ProdutosDTO> listagem = new ArrayList<>();
        if (!conectar()) {
            System.out.println("Falha ao conectar ao banco");
            return listagem;
        }
        String sql = "SELECT * FROM produtos WHERE status = 'Vendido'";
        try (PreparedStatement st = conn.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                ProdutosDTO produto = new ProdutosDTO();
                produto.setId(rs.getInt("id"));
                produto.setNome(rs.getString("nome"));
                produto.setValor(rs.getInt("valor"));
                produto.setStatus(rs.getString("status"));

                listagem.add(produto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return listagem;
    }
    
}

