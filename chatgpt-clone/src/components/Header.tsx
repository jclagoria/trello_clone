import React from "react"

interface HeaderProps {
    onSignUpClick: () => void
}

export const Header: React.FC<HeaderProps> = ({ onSignUpClick }) => {
    return (
        <header>
            <h1>Web Clone</h1>

            <nav>
                <a href="#features">Features</a>
                <a href="#about">About</a>
            </nav>

            <div>
                <a href="#" onClick={(e) => {
                    e.preventDefault()
                    onSignUpClick() }}>
                    Sign Up
                </a>
                <a href="#signin">Sign In</a>
            </div>
        </header>
    )
}