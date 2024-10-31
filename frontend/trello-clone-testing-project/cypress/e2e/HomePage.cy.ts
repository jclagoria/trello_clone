describe('Home Page Test', () => {
    it('Should visit the home page content correctly', () => {
        cy.visit('/')
        cy.contains('h2', 'Welcome to Trello Clone')
        cy.contains('p', 'Manage your tasks, collaborate with your team, and achieve more.')
    })
})