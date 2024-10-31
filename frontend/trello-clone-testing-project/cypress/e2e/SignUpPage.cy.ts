describe('Signup Page Tests', () => {
    beforeEach(() => {
        cy.visit('/signup')
    })

    it('Should display the signup page correctly', () => {
        cy.get('h2').contains('Sign Up')
        cy.get('form').within(() => {
            cy.get('input[name="username"]').should('exist')
            cy.get('input[name="email"]').should('exist')
            cy.get('input[name="password"]').should('exist')
            cy.get('input[name="confirmPassword"]').should('exist')
        })
    })
})