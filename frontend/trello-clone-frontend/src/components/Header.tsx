import React from "react"
import { Link } from "react-router-dom"

export const Header: React.FC = () => {
    return (
        <header>
            <h1>Trello Clone</h1>
            <div className="header-actions">
                <Link id='{crypto.randomUUID()}' to='/login'>Login</Link>
                <Link id='{crypto.randomUUID()}' to='/signup'>Sign Up</Link>
            </div>
        </header>
    )
}