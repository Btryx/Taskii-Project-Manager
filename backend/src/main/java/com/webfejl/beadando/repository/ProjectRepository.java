package com.webfejl.beadando.repository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.webfejl.beadando.entity.Project;
import com.webfejl.beadando.query.ProjectQuery;

import java.util.List;

@Repository
public class ProjectRepository{

    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<Project> getAllProjects(){
        String sql = ProjectQuery.SELECT_ALL.getQuery();
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Project.class));
    }

    public Project getProjectById(String id){
        String sql = ProjectQuery.SELECT_PROJECT_BY_ID.getQuery();
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Project.class), id);
    }

    public int createProject(Project project){
        String sql = ProjectQuery.CREATE_PROJECT.getQuery();
        return jdbcTemplate.update(sql, project.getProjectId(), project.getProjectName(),
                project.getCreatedAt(), project.getActive(), project.getParentId());
    }

    public List<Project> getProjectByParentId(Integer parentId) {
        String sql = ProjectQuery.SELECT_TASK_BY_PARENT_ID.getQuery();
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Project.class), parentId);
    }

    public int editProject(Project project, String id){
        String sql = ProjectQuery.EDIT_PROJECT.getQuery();
        return jdbcTemplate.update(sql,  project.getProjectName(),
        project.getCreatedAt(), project.getActive(), project.getParentId(), id);
    }

    public int deleteProject(String id){
        String sql = ProjectQuery.DELETE_PROJECT.getQuery();
        return jdbcTemplate.update(sql, id);
    }
}
