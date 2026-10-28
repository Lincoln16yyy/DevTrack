package com.devtrack.project;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository repository;

    public ProjectService(ProjectRepository repository) {
        this.repository = repository;
    }

    public List<ProjectResponse> findAll() {
        return repository.findAll().stream().map(ProjectResponse::from).toList();
    }
    @Transactional
    public ProjectResponse create(ProjectRequest request) {
        Project project = new Project(request.name(), request.description());
        Project saved = repository.save(project);
        return ProjectResponse.from(saved);
    }
}