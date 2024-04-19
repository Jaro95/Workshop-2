package pl.coderslab.DAO;

import com.mysql.cj.exceptions.NumberOutOfRange;
import org.mindrot.jbcrypt.BCrypt;
import pl.coderslab.Model.User;

import javax.lang.model.element.TypeElement;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    private static final String CREATE_USER_QUERY = """
        INSERT INTO users (username, email, password) VALUES(?, ?, ?);
        """;

    private static final String READ_USER_ID = """
        SELECT * FROM users WHERE id = ? ;
        """;
    private static final String READ_USER_BY_COLUMN = """
        SELECT * FROM users WHERE column = ? ;
        """;
    private static final String UPDATE_USER_ALL_COLUMN = """
        UPDATE users SET email = ? , username = ? , password = ? WHERE id = ? ;
        """;
    private static final String DELETE_BY_ID = """
        DELETE FROM users WHERE id = ? ;
        """;
    private static final String FIND_ALL_USERS = """
        SELECT * FROM users;
        """;
    private List<String> columnsUsers = List.of("id", "email", "username", "password");

    public User create(User user) {
        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement statement =
                    conn.prepareStatement(CREATE_USER_QUERY, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getEmail());
            statement.setString(3, hashPassword(user.getPassword()));
            ResultSet resultSet = statement.getGeneratedKeys();
            statement.executeUpdate();
            if (resultSet.next()) {
                user.setId(resultSet.getInt(1));
            }
            resultSet.close();
            return user;
        } catch (SQLException e) {
            System.out.println("User : " + user.getUserName() + " can't be created. The email provided is already taken");
            return null;
        }
    }

    public User readByColumn(String column, String value) {
        if(!columnsUsers.contains(column)) {
            throw new SecurityException("Column not exist");
        }
        User user = new User();
        try(Connection connection = DbUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(READ_USER_BY_COLUMN.replace("column", column));
            statement.setString(1, value);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                user.setId(resultSet.getInt("id"));
                user.setUserName(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
            }
            statement.close();
            resultSet.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(user.getId() == 0) {
            throw new NumberOutOfRange("The specified value could not be found");
        }
        return user;
    }

    public void update(User user) {
        try (Connection connection = DbUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(UPDATE_USER_ALL_COLUMN);
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getUserName());
            statement.setString(3, hashPassword(user.getPassword()));
            statement.setInt(4, user.getId());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<User> findAll() {
        List<User> userList = new ArrayList<>();
        try (Connection connection = DbUtil.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(FIND_ALL_USERS);
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setEmail(resultSet.getString("email"));
                user.setUserName(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }

    public void delete (int userId) {
        try (Connection connection = DbUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(DELETE_BY_ID);
            statement.setInt(1, userId);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public void checkCorrectColumn(String column) {

    }

    /*
    public User read(int userId) {
        User user = new User();
        try(Connection connection = DbUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(READ_USER_ID);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                user.setId(resultSet.getInt("id"));
                user.setUserName(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
            }

            resultSet.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
    */
}
