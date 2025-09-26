// frontend/cypress/e2e/tenant_management.cy.js

describe('Tenant Management E2E Test', () => {
  // This function runs before each test in this block
  beforeEach(() => {
    // We will perform login here to ensure a clean state for each test
    cy.visit('/login');
    cy.get('input[name="username"]').type('admin');
    cy.get('input[name="password"]').type('password'); // Note: Use the actual admin password
    cy.get('button[type="submit"]').click();

    // Wait for the redirect to the dashboard and check for a known element
    cy.url().should('include', '/dashboard');
    cy.contains('Dashboard').should('be.visible');
  });

  it('should allow an admin to add a new tenant and then log out', () => {
    // 1. Navigate to the tenants page
    cy.visit('/tenants');
    cy.url().should('include', '/tenants');
    cy.contains('Tenant Management').should('be.visible');

    // 2. Click the "Add New Tenant" button
    cy.contains('เพิ่มผู้เช่าใหม่').click(); // Using the button text to find it

    // 3. Fill in the new tenant form
    // Note: The form elements' selectors (like name, id, or class) are needed here.
    // I'm using placeholder selectors. You'll need to inspect your HTML to get the correct ones.
    cy.get('input[name="name"]').type('John Doe');
    cy.get('input[name="idCard"]').type('1234567890123');
    cy.get('input[name="phone"]').type('0812345678');
    cy.get('select[name="unitId"]').select('1'); // Assuming '1' is a valid unit ID in the dropdown
    cy.get('input[name="checkInDate"]').type('2025-10-01');
    cy.get('input[name="leaseDuration"]').type('12'); // 12 months
    cy.get('input[name="rentAmount"]').type('5000');

    // 4. Submit the form
    cy.get('button[type="submit"].save-button').click(); // Example selector for the save button

    // 5. Verify the new tenant appears in the list
    cy.contains('John Doe').should('be.visible');

    // 6. Log out
    // Assuming there is a logout button in the sidebar or navbar
    cy.get('.logout-button').click(); // Example selector for the logout button

    // 7. Verify we are back on the login page
    cy.url().should('include', '/login');
    cy.contains('Login').should('be.visible');
  });
});