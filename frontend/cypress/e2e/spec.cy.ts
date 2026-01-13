describe('Portfolio E2E', () => {
  it('Visits the home page and checks title', () => {
    cy.visit('/');
    cy.title().should('include', 'Alfredo Soto');
    cy.contains('Alfredo Soto'); // Check for name in hero
  });
});
