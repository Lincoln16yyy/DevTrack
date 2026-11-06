import type { Project } from '../types'

type ProjectListProps = {
    projects: Project[]
}

function ProjectList({ projects }: ProjectListProps) {
    return (
        <ul>
            {projects.map((project) => (
                <li key={project.id}>
                    <strong>{project.name}</strong>
                    <p>{project.description}</p>
                </li>
            ))}
        </ul>
    )
}

export default ProjectList