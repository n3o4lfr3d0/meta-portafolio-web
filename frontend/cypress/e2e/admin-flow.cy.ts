import { ADMIN_ROUTE } from '../../src/app/config/admin.config';

describe('Admin Flow', () => {
  const mockComments = [
    { id: '1', username: 'User 1', content: 'Great portfolio!', approved: false, createdAt: '2024-01-01' },
    { id: '2', username: 'User 2', content: 'Spam message', approved: false, createdAt: '2024-01-02' }
  ];

  beforeEach(() => {
    cy.intercept('POST', '**/auth/login', {
      statusCode: 200,
      body: { token: 'fake-jwt-token' }
    }).as('loginRequest');

    cy.intercept('GET', '**/comments/admin', {
      statusCode: 200,
      body: mockComments
    }).as('getComments');

    cy.intercept('PUT', '**/comments/*/approve', {
      statusCode: 200,
      body: { ...mockComments[0], approved: true }
    }).as('approveComment');

    cy.intercept('DELETE', '**/comments/*', {
      statusCode: 204,
      body: {}
    }).as('deleteComment');

    cy.visit(`/${ADMIN_ROUTE}/login`);
  });

  it('should login successfully', () => {
    cy.get('input[name="username"]').type('admin');
    cy.get('input[name="password"]').type('password');
    cy.get('button[type="submit"]').click();

    cy.wait('@loginRequest');
    // It might redirect to /alfredo-nexus which then redirects to /alfredo-nexus/dashboard
    // or just /alfredo-nexus if that is the dashboard.
    // Let's assume dashboard is default child.
    cy.url().should('include', `/${ADMIN_ROUTE}`);
  });

  it('should display comments in dashboard', () => {
    // Login first
    cy.get('input[name="username"]').type('admin');
    cy.get('input[name="password"]').type('password');
    cy.get('button[type="submit"]').click();
    cy.wait('@loginRequest');

    cy.wait('@getComments');
    cy.contains('User 1').should('be.visible');
    cy.contains('Great portfolio!').should('be.visible');
  });

  it('should approve a comment', () => {
    // Login
    cy.get('input[name="username"]').type('admin');
    cy.get('input[name="password"]').type('password');
    cy.get('button[type="submit"]').click();
    cy.wait('@loginRequest');

    // Approve first comment
    cy.contains('User 1').parent().find('button[title="Aprobar"]').click();

    // Verify loading state or call
    cy.wait('@approveComment');
  });

  it('should delete a comment using the modal', () => {
    // Login
    cy.get('input[name="username"]').type('admin');
    cy.get('input[name="password"]').type('password');
    cy.get('button[type="submit"]').click();
    cy.wait('@loginRequest');

    // Click Delete on second comment
    cy.contains('User 2').parent().find('button[title="Eliminar"]').click();

    // Verify Modal is open
    cy.get('#delete_modal').should('be.visible');
    cy.contains('CONFIRMAR_ELIMINACIÓN').should('be.visible');

    // Confirm Delete
    cy.get('#delete_modal').contains('ELIMINAR').click();

    cy.wait('@deleteComment');
    cy.get('#delete_modal').should('not.be.visible');
  });
});
