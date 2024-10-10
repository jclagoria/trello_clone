import React, {useState} from "react"

export const SingUp: React.FC = () => {
    const [name, setName] = useState<string>('');
    const [email, setEmail] = useState<string>('')
    const [password, setPassword] = useState<string>('')
    const [confirmPassword, setConfirmPassword] = useState<string>('')
    const [error, setError] = useState<string>('')
    const [success, setSuccess] = useState<string>('')

    const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault()

        if (password !== confirmPassword) {
            setError('Passwords do not match')
            setSuccess('')
            return
        }

        if (name === '' || email === '' || password === '') {
            setError('Please fill in all fields')
            setSuccess('')
            return
        }

        setSuccess('Sing up successful! Welcome to the platform, ' + name)
        setError('')

        setSuccess('')
        setEmail('')
        setPassword('')
        setConfirmPassword('')
    }

    return (
        <div className='signup-container'>
            <h2>Entrale a la plataforma</h2>
            <form onSubmit={handleSubmit} className='signup-form'>
                <div className='form-group'>
                    <label htmlFor='name'>Name:</label>
                    <input
                        type='text'
                        id='name'
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                        placeholder='Enter your name'
                        required
                    />
                </div>
                <div className='form-group'>
                    <label htmlFor='email'>Email:</label>
                    <input
                        type='email'
                        id='email'
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        placeholder='Enter your email'
                        required
                    />
                </div>
                <div className='form-group'>
                    <label htmlFor='password'>Password:</label>
                    <input
                        type={password}
                        id='password'
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        placeholder='Enter your password'
                        required
                    />
                </div>
                <div className='form-group'>
                    <label htmlFor='confirmPassword'>Confirm Password</label>
                    <input
                        type='password'
                        id='confirmPassword'
                        value={confirmPassword}
                        onChange={(e) => setConfirmPassword(e.target.value)}
                        placeholder='Confirm your password'
                        required
                    />
                </div>

                { error && <p className='error-message'>{error}</p>}
                {success && <p className='success-message'>{success}</p>}

                <button className='signup-button'>Sign Up</button>

            </form>
        </div>
    )
}