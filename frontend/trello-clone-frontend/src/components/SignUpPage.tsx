import React from 'react'
import { useSignUpForm } from '../hooks/useSignUpForm'

export const SignUpPage: React.FC = () => {
    const { formData, formError, loading, error, status, handleChange, handleSubmit } = useSignUpForm()

    return (
        <div className='signup-container'>
            <h2>Sign Up</h2>
            <form className='signup-form' onSubmit={handleSubmit}>
                <div className='form-group'>
                    <label htmlFor='username'>Username</label>
                    <input 
                        type='text' 
                        id='username'
                        name='username'
                        value={formData.username}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div className='form-group'>
                    <label htmlFor='email'>Email</label>
                    <input
                        type='email'
                        id='email'
                        name='email'
                        value={formData.email}
                        onChange={handleChange}
                        required 
                    />
                </div>
                <div className='form-group'>
                    <label htmlFor='password'>Password</label>
                    <input 
                        type='password'
                        id='password'
                        name='password'
                        value={formData.password}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div className='form-group'>
                    <label htmlFor='confirmPassword'>Confirm Password</label>
                    <input 
                        type='password'
                        id='confirmPassword'
                        name='confirmPassword'
                        value={formData.confirmPassword}
                        onChange={handleChange}
                        required
                    />
                </div>
                {formError && <p className="error-message">{formError}</p>}
                {error && <p className="error-message">{error}</p>}
                {status === 200 && <p className="success-message">Account created successfully!</p>}
                {loading && <p>Loading...</p>}
                <button type="submit">Sign Up</button>
            </form>
        </div>
    )
}