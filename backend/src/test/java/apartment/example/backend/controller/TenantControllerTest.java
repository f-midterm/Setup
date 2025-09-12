// backend/src/test/java/apartment/example/backend/controller/TenantControllerTest.java
package apartment.example.backend.controller;

import apartment.example.backend.dto.TenantRequestDto;
import apartment.example.backend.dto.TenantResponseDto;
import apartment.example.backend.entity.Tenant;
import apartment.example.backend.service.TenantService;
import jakarta.persistence.EntityNotFoundException;
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
import static org.mockito.Mockito.*;

class TenantControllerTest {

    @InjectMocks
    private TenantController tenantController;

    @Mock
    private TenantService tenantService;

    private Tenant tenant;
    private TenantRequestDto tenantRequestDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tenant = new Tenant();
        tenant.setId(1L);
        tenant.setName("John Doe");

        tenantRequestDto = new TenantRequestDto();
        tenantRequestDto.setName("John Doe");
    }

    @Test
    void getAllTenants_ShouldReturnListOfTenants() {
        // Given
        // Why: We mock the service to return a list containing our test tenant.
        when(tenantService.getAllTenants()).thenReturn(Collections.singletonList(tenant));

        // When
        ResponseEntity<List<TenantResponseDto>> response = tenantController.getAllTenants();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("John Doe", response.getBody().get(0).getName());
    }

    @Test
    void createTenant_ShouldReturnCreatedTenant() {
        // Given
        // Why: We mock the createTenant service method to return our test tenant.
        when(tenantService.createTenant(any(TenantRequestDto.class))).thenReturn(tenant);

        // When
        ResponseEntity<TenantResponseDto> response = tenantController.createTenant(tenantRequestDto);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("John Doe", response.getBody().getName());
    }

    @Test
    void updateTenant_ShouldReturnUpdatedTenant() {
        // Given
        // Why: We mock the updateTenant service method to return the updated tenant.
        when(tenantService.updateTenant(eq(1L), any(TenantRequestDto.class))).thenReturn(tenant);

        // When
        ResponseEntity<TenantResponseDto> response = tenantController.updateTenant(1L, tenantRequestDto);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("John Doe", response.getBody().getName());
    }

    @Test
    void updateTenant_WhenNotFound_ShouldReturnNotFound() {
        // Given
        // Why: We mock the service to throw an EntityNotFoundException.
        when(tenantService.updateTenant(eq(1L), any(TenantRequestDto.class))).thenThrow(new EntityNotFoundException());

        // When
        ResponseEntity<TenantResponseDto> response = tenantController.updateTenant(1L, tenantRequestDto);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteTenant_ShouldReturnNoContent() {
        // Given
        // Why: We use doNothing() because the deleteTenant service method returns void.
        doNothing().when(tenantService).deleteTenant(1L);

        // When
        ResponseEntity<Void> response = tenantController.deleteTenant(1L);

        // Then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        // Why: We verify that the deleteTenant method was called exactly once with the correct ID.
        verify(tenantService, times(1)).deleteTenant(1L);
    }

    @Test
    void deleteTenant_WhenNotFound_ShouldReturnNotFound() {
        // Given
        // Why: We mock the service to throw an exception when trying to delete a non-existent tenant.
        doThrow(new EntityNotFoundException()).when(tenantService).deleteTenant(1L);

        // When
        ResponseEntity<Void> response = tenantController.deleteTenant(1L);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}