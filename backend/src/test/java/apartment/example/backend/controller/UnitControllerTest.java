// backend/src/test/java/apartment/example/backend/controller/UnitControllerTest.java
package apartment.example.backend.controller;

import apartment.example.backend.entity.Unit;
import apartment.example.backend.service.UnitService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class UnitControllerTest {

    @InjectMocks
    private UnitController unitController;

    @Mock
    private UnitService unitService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllUnits_ShouldReturnListOfUnits() {
        // Given
        Unit unit = new Unit();
        unit.setId(1L);
        unit.setRoomNumber("101");

        // Why: We mock the service to return a list with our test unit.
        when(unitService.getAllUnits()).thenReturn(Collections.singletonList(unit));

        // When
        ResponseEntity<List<Unit>> response = unitController.getAllUnits();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("101", response.getBody().get(0).getRoomNumber());
    }
}