import { useState } from "react"

interface ApiRespose<T> {
    data: T | null;
    error: string | null;
    loading: boolean;
    status: number | null;
}

export function useApi<T> () {
    const [response, setResponse] = useState<ApiRespose<T>>({
        data: null,
        error: null,
        loading: false,
        status: null,
    })

    const makeRequest = async (url: string, options: RequestInit) => {
        setResponse({ data: null, error: null, loading: true, status: null })

        try {
            const res = await fetch(url, options)

            const status = res.status
            const data = await res.json()

            if(!res.ok) {
                throw new Error(data.message || "Request failed")
            }

            setResponse({ data, error: null, loading: false, status })
        } catch (error: any) {
            setResponse({
                data: null,
                error: error.message || "An error occurred",
                loading: false,
                status: response.status || 500
            })
        }
    }

    return {
        ...response,
        makeRequest
    }
}