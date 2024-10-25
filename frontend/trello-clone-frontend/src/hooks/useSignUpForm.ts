import { useState } from "react"
import {useApi} from "./useApi.tsx";

interface SignUpFormData {
    username: string;
    email: string;
    password: string;
    confirmPassword: string;
}

export function useSignUpForm() {
    const [formData, setFormData] = useState<SignUpFormData>({
        username: '', 
        email: '', 
        password: '', 
        confirmPassword: ''
    })

    const [formError, setFormError] = useState<string | null>(null)
    const { data, error, loading, status, makeRequest } = useApi<unknown>()

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setFormData({...formData, [e.target.name]: e.target.value })
    }

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault()

        if(formData.password!== formData.confirmPassword) {
            setFormError("Passwords do not match")
            return     
        }

        setFormError(null)

        const requestOptions = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                username: formData.username,
                email: formData.email,
                password: formData.password
            })
        }

        await makeRequest('http://localhost:8090/api/service/account/create', requestOptions)

    }

    return {
        formData,
        formError,
        loading,
        error,
        status,
        handleChange,
        handleSubmit
    }
}