package dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
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

import com.lmgbgpatr.dao.AccountDAO;
import com.lmgbgpatr.dto.AccountType;
import com.lmgbgpatr.model.Account;

@ExtendWith(MockitoExtension.class)
public class AccountDAOTest {

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
    private AccountDAO dao;

    Account mockAcc0 =
        new Account(
            0,
            0,
            AccountType.CHECKING,
            false,
            new BigDecimal(0)
        );

    Account mockAcc1 =
        new Account(
            1,
            0,
            AccountType.SAVING,
            true,
            new BigDecimal(1)
        );

    Account[] mockAccArr = new Account[]{mockAcc0, mockAcc1};

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
        void validID_getID_returnAccount(int id) throws SQLException {
            when(mockRS.next()).thenReturn(true);
            when(mockRS.getInt("id")).thenReturn(mockAccArr[id].id());
            when(mockRS.getInt("custid")).thenReturn(mockAccArr[id].custID());
            when(mockRS.getString("type")).thenReturn(mockAccArr[id].type().name());
            when(mockRS.getBoolean("isfrozen")).thenReturn(mockAccArr[id].isFrozen());
            when(mockRS.getBigDecimal("balance")).thenReturn(mockAccArr[id].balance());

            Account actual = dao.getByID(id);

            assertEquals(mockAccArr[id], actual);
            verify(mockPstmt).setInt(1, mockAccArr[id].id());
        }

        @Test
        void invalidID_getID_returnNull() throws SQLException {
            when(mockRS.next()).thenReturn(false);


            int id = -1; // no account has a negative id
            Account actual = dao.getByID(id);

            assertNull(actual);
            verify(mockPstmt).setInt(1, id);
        }
    
        @Test
        void validID_getCustID_returnHydratedList() throws SQLException {
            when(mockRS.next()).thenReturn(true, true, false);
            when(mockRS.getInt("id")).thenReturn(mockAcc0.id(), mockAcc1.id());
            when(mockRS.getInt("custid")).thenReturn(mockAcc0.custID(), mockAcc1.custID());
            when(mockRS.getString("type")).thenReturn(mockAcc0.type().name(), mockAcc1.type().name());
            when(mockRS.getBoolean("isfrozen")).thenReturn(mockAcc0.isFrozen(), mockAcc1.isFrozen());
            when(mockRS.getBigDecimal("balance")).thenReturn(mockAcc0.balance(), mockAcc1.balance());

            List<Account> actual = dao.getByCustID(mockAcc0.custID()); // custid is shared for both mockAcc
            
            assertEquals(List.of(mockAcc0, mockAcc1), actual);
            verify(mockPstmt).setInt(1, mockAcc0.custID());
            // each account has two int fields, hence 2
            verify(mockRS, times(mockAccArr.length * 2)).getInt(anyString());
        }

        @Test
        void invalidID_getCustID_returnEmptyList() throws SQLException {
            when(mockRS.next()).thenReturn(false);

            int id = -1;
            List<Account> actual = dao.getByCustID(id);
            
            assertTrue(actual.isEmpty());
            verify(mockPstmt).setInt(1, id);
            verify(mockRS, times(0)).getInt(anyString());
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
        void populatedAccTable_getAll_returnHydratedList() throws SQLException {
            when(mockRS.next()).thenReturn(true, true, false);
            when(mockRS.getInt("id")).thenReturn(mockAcc0.id(), mockAcc1.id());
            when(mockRS.getInt("custid")).thenReturn(mockAcc0.custID(), mockAcc1.custID());
            when(mockRS.getString("type")).thenReturn(mockAcc0.type().name(), mockAcc1.type().name());
            when(mockRS.getBoolean("isfrozen")).thenReturn(mockAcc0.isFrozen(), mockAcc1.isFrozen());
            when(mockRS.getBigDecimal("balance")).thenReturn(mockAcc0.balance(), mockAcc1.balance());

            List<Account> actual = dao.getAll();

            assertEquals(List.of(mockAcc0, mockAcc1), actual);
            verify(mockRS, times(mockAccArr.length * 2)).getInt(anyString());
        }

        @Test
        void unpopulatedAccTable_getAll_returnEmptyList() throws SQLException {
            when(mockRS.next()).thenReturn(false);

            List<Account> actual = dao.getAll();

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
        void validAccount_insert_unpackModelAndCommit() throws SQLException {
            dao.insert(mockAcc0);

            verify(mockPstmt).setInt(1, mockAcc0.id());
            verify(mockPstmt).setInt(2, mockAcc0.custID());
            verify(mockPstmt).setString(3, mockAcc0.type().toString());
            verify(mockPstmt).setBoolean(4, mockAcc0.isFrozen());
            verify(mockPstmt).setBigDecimal(5, mockAcc0.balance());
            verify(mockConn, times(1)).setAutoCommit(anyBoolean());
            verify(mockConn, times(1)).commit();
        }

        @Test
        void invalidAccount_insert_noCommit() throws SQLException {
            dao.insert(null);

            verify(mockConn, times(1)).setAutoCommit(anyBoolean());
            verify(mockConn, times(0)).commit();
        }
    }
}
