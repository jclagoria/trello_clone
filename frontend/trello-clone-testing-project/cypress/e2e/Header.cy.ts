describe('Header Test', () => {
    it('Should display the header correctly and navigate to login and signup', () => {
        cy.visit('/')

        cy.get('header').contains('Trello Clone')

        //cy.get('a').contains('Login').click()
        //cy.url().should('include', '/login')

        //cy.get('back')

        cy.get('a').contains('Sign Up').click()
        cy.url().should('include', '/signup')
        cy.go('back')
    })
})