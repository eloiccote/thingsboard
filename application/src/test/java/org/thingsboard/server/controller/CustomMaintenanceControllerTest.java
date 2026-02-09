package org.thingsboard.server.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class CustomMaintenanceControllerTest {

    @Mock
    private DataSource mockDataSource;

    @Mock
    private Connection mockConnection;

    @InjectMocks
    private CustomMaintenanceController controller;

    @Before
    public void setUp() throws SQLException {
        when(mockDataSource.getConnection()).thenReturn(mockConnection);
    }

    @Test
    public void testDatabaseUp() throws SQLException {
        when(mockConnection.isValid(1)).thenReturn(true);

        String result = controller.getMaintenanceStatus();

        System.out.println("Test UP Result: " + result);
        assertTrue("Doit indiquer CONNECTED", result.contains("CONNECTED"));
        assertTrue("Doit afficher la mémoire", result.contains("memory_used_mb"));
    }

    @Test
    public void testDatabaseDown() throws SQLException {
        when(mockDataSource.getConnection()).thenThrow(new SQLException("Connection timeout"));

        String result = controller.getMaintenanceStatus();

        System.out.println("Test DOWN Result: " + result);
        assertTrue("Doit indiquer DISCONNECTED", result.contains("DISCONNECTED"));
    }
}