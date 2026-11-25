package dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.lmgbgpatr.dao.UserDAO;
import com.lmgbgpatr.dto.UserLevel;
import com.lmgbgpatr.model.User;

@ExtendWith(MockitoExtension.class)
public class UserDAOTest {

    @Mock
    private DataSource mockDS;
    @Mock
    private Connection mockConn;
    @Mock
    private Statement mockStmt;
    @Mock
    private PreparedStatement mockPstmt;
    @Mock
    private ResultSet mockRS;

    @InjectMocks
    private UserDAO dao;

    User mockUser0 =
        new User(
            0,
            "testUsername",
            "testPassword",
            "testFname",
            "testLname",
            UserLevel.UNAUTHORIZED
        );

    User mockUser1 =
        new User(
            1,
            "testUsername1",
            "testPassword",
            "testFname",
            "testLname",
            UserLevel.CUSTOMER
        );

    User[] mockUserArr = new User[]{mockUser0, mockUser1};

    @Nested
    class TestPreparedStatementNonUpdateMethods {

        @BeforeEach
        void setUpDefaultMockBehavior() throws SQLException {
            when(mockDS.getConnection()).thenReturn(mockConn);
            when(mockConn.prepareStatement(anyString())).thenReturn(mockPstmt);
            when(mockPstmt.executeQuery()).thenReturn(mockRS);
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1})
        void validID_getID_returnUser(int id) throws SQLException {
            when(mockRS.next()).thenReturn(true);
            when(mockRS.getInt("id")).thenReturn(mockUserArr[id].id());
            when(mockRS.getString("username")).thenReturn(mockUserArr[id].username());
            when(mockRS.getString("password")).thenReturn(mockUserArr[id].password());
            when(mockRS.getString("fname")).thenReturn(mockUserArr[id].fname());
            when(mockRS.getString("lname")).thenReturn(mockUserArr[id].lname());
            when(mockRS.getInt("level")).thenReturn(mockUserArr[id].level().ordinal());

            User actual = dao.getByID(id);

            assertEquals(mockUserArr[id], actual);
            verify(mockPstmt).setInt(1, mockUserArr[id].id());
        }

        @Test
        void invalidID_getID_returnNull() throws SQLException {
            when(mockRS.next()).thenReturn(false);

            int id = -1; // no user has a negative id
            User actual = dao.getByID(id);

            assertNull(actual);
            verify(mockPstmt).setInt(1, id);
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1})
        void validUsername_getByUsername_returnUser(int index) throws SQLException {
            User expected = mockUserArr[index];
            when(mockRS.next()).thenReturn(true);
            when(mockRS.getInt("id")).thenReturn(expected.id());
            when(mockRS.getString("username")).thenReturn(expected.username());
            when(mockRS.getString("password")).thenReturn(expected.password());
            when(mockRS.getString("fname")).thenReturn(expected.fname());
            when(mockRS.getString("lname")).thenReturn(expected.lname());
            when(mockRS.getInt("level")).thenReturn(expected.level().ordinal());

            User actual = dao.getByUsername(expected.username());

            assertEquals(expected, actual);
            verify(mockPstmt).setString(1, expected.username());
        }

        @Test
        void invalidUsername_getByUsername_returnNull() throws SQLException {
            when(mockRS.next()).thenReturn(false);

            String username = "nonexistentUser";
            User actual = dao.getByUsername(username);

            assertNull(actual);
            verify(mockPstmt).setString(1, username);
        }
    }

    @Nested
    class TestStatementMethods {

        @BeforeEach
        void setUpDefaultMockBehavior() throws SQLException {
            when(mockDS.getConnection()).thenReturn(mockConn);
            when(mockConn.createStatement()).thenReturn(mockStmt);
            when(mockStmt.executeQuery(anyString())).thenReturn(mockRS);
        }

        @Test
        void populatedUserTable_getAll_returnHydratedList() throws SQLException {
            when(mockRS.next()).thenReturn(true, true, false);
            when(mockRS.getInt("id")).thenReturn(mockUser0.id(), mockUser1.id());
            when(mockRS.getString("username")).thenReturn(mockUser0.username(), mockUser1.username());
            when(mockRS.getString("password")).thenReturn(mockUser0.password(), mockUser1.password());
            when(mockRS.getString("fname")).thenReturn(mockUser0.fname(), mockUser1.fname());
            when(mockRS.getString("lname")).thenReturn(mockUser0.lname(), mockUser1.lname());
            when(mockRS.getInt("level")).thenReturn(mockUser0.level().ordinal(), mockUser1.level().ordinal());

            List<User> actual = dao.getAll();

            assertEquals(List.of(mockUser0, mockUser1), actual);
            // each user has two int fields (id and level), hence 2
            verify(mockRS, times(mockUserArr.length * 2)).getInt(anyString());
        }

        @Test
        void unpopulatedUserTable_getAll_returnEmptyList() throws SQLException {
            when(mockRS.next()).thenReturn(false);

            List<User> actual = dao.getAll();

            assertTrue(actual.isEmpty());
            verify(mockRS, times(0)).getInt(anyString());
        }
    }

    @Nested
    class TestUpdateMethods {

        @BeforeEach
        void setUpDefaultMockBehavior() throws SQLException {
            when(mockDS.getConnection()).thenReturn(mockConn);
            when(mockConn.prepareStatement(anyString())).thenReturn(mockPstmt);
        }

        @Test
        void validUser_insert_unpackModelAndCommit() throws SQLException {
            dao.insert(mockUser0);

            verify(mockPstmt).setString(1, mockUser0.username());
            verify(mockPstmt).setString(2, mockUser0.password());
            verify(mockPstmt).setString(3, mockUser0.fname());
            verify(mockPstmt).setString(4, mockUser0.lname());
            verify(mockConn, times(1)).setAutoCommit(anyBoolean());
            verify(mockConn, times(1)).commit();
        }

        @Test
        void invalidUser_insert_noCommit() throws SQLException {
            dao.insert(null);

            verify(mockConn, times(1)).setAutoCommit(anyBoolean());
            verify(mockConn, times(0)).commit();
        }
    }
}