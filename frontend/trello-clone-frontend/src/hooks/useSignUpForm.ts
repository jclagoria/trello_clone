import { useState } from "react"

export function useSignUpForm() {
    const [formData, setFormData] = useState({
        username: '', 
        email: '', 
        password: '', 
        confirmPassword: ''
    })

    const [error, setError] = useState<string | null>(null)
    const [success, setSuccess] = useState<boolean>(false)

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setFormData({...formData, [e.target.name]: e.target.value })
    }

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault()

        if(formData.password!== formData.confirmPassword) {
            setError("Passwords do not match")
            return     
        }

        try { 
            const response = await fetch('http://localhost:8090/api/service/account/create',{
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    username: formData.username,
                    email: formData.email,
                    password: formData.password
                })
            })

            if (!response.ok) {
                const errorData = await response.json()
                throw new Error(errorData.message || "Something went wrong")       
            }

            setSuccess(true)
            setError(null)
        } catch (err: any) {
            setError(err.message || 'Error occurred while creating an account')
            setSuccess(false)
        }

       return {
        formData,
        error,
        success,
        handleChange,
        handleSubmit
       }
    }
}