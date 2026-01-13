describe('Visitor Flow', () => {
  beforeEach(() => {
    // Intercept API calls to prevent actual backend mutations
    cy.intercept('POST', '**/api/contact', {
      statusCode: 200,
      body: 'Mensaje enviado con éxito'
    }).as('submitContact');

    cy.intercept('GET', '**/api/daily-content*', { fixture: 'daily-content.json' }).as('getDailyContent');
    cy.visit('/');
  });

  it('should display the home page correctly', () => {
    cy.title().should('include', 'Alfredo Soto');
    cy.contains('Alfredo Soto').should('be.visible');
    cy.get('app-hero').should('be.visible');
  });

  it('should navigate to sections via scrolling', () => {
    // Scroll to bottom to trigger defer loading
    cy.scrollTo('bottom');
    // Wait for the contact section to appear (defer trigger)
    cy.get('#contacto', { timeout: 10000 }).should('be.visible');

    cy.get('#experiencia').scrollIntoView().should('be.visible');
    cy.get('#educacion').scrollIntoView().should('be.visible');
  });

  it('should fill and submit the contact form', () => {
    // Ensure contact section is loaded
    cy.scrollTo('bottom');
    cy.get('#contacto', { timeout: 10000 }).should('be.visible');
    cy.get('#contacto').scrollIntoView();

    cy.get('input[formControlName="name"]').type('Cypress Tester');
    cy.get('input[formControlName="email"]').type('test@cypress.io');
    cy.get('input[formControlName="subject"]').type('E2E Test Subject');
    cy.get('textarea[formControlName="message"]').type('This is a test message from Cypress E2E. It is long enough.');

    // Wait for form to become valid
    cy.get('app-contact button[type="submit"]').should('not.be.disabled');

    cy.wait(1000); // Wait for any animations
    cy.get('app-contact button[type="submit"]').scrollIntoView().click({ force: true });

    cy.wait('@submitContact');

    // Check for success feedback
    // After submit, form resets so it should be invalid and button disabled
    cy.get('app-contact input[formControlName="name"]').should('have.value', '');
    cy.get('app-contact button[type="submit"]').should('be.disabled');
  });
});
