import { useEffect, useState } from 'react'
import './App.css'
import ProjectList from './components/ProjectList'
import type { Project } from './types'

function App() {
    const [projects, setProjects] = useState<Project[]>([])
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState<string | null>(null)

    useEffect(() => {
        async function loadProjects() {
            try {
                const response = await fetch('/api/projects')

                if (!response.ok) {
                    throw new Error('Erro ao buscar projetos')
                }

                const data = await response.json()
                setProjects(data)
            } catch (err) {
                console.error(err)
                setError('Não foi possível carregar os projetos')
            } finally {
                setLoading(false)
            }
        }

        loadProjects()
    }, [])

    if (loading) return <p>Carregando...</p>
    if (error) return <p>{error}</p>

    return (
        <main>
            <h1>Meus projetos</h1>
            <ProjectList projects={projects} />
        </main>
    )
}

export default App