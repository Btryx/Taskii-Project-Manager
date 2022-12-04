package com.webfejl.beadando.repository;

import com.webfejl.beadando.entity.TaskDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TaskRepo{

    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<TaskDTO> getAllTasks(){
        String sql = SQLQuery.SELECT_ALL.getQuery();
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TaskDTO.class));
    }

    public TaskDTO getTaskById(String id){
        String sql = SQLQuery.SELECT_TASK.getQuery();
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(TaskDTO.class), id);
    }

    public int createTask(TaskDTO task){
        String sql = SQLQuery.CREATE_TASK.getQuery();
        return jdbcTemplate.update(sql, task.getTaskId(), task.getTaskTitle(),
                task.getTaskStatus(), task.getTaskPriority(), task.getTaskDate(), task.getTaskDesc());
    }

    public int editTask(TaskDTO task, String id){
        String sql = SQLQuery.EDIT_TASK.getQuery();
        return jdbcTemplate.update(sql, task.getTaskTitle(), task.getTaskStatus(), task.getTaskPriority(),
                task.getTaskDate(), task.getTaskDesc(), id);
    }

    public int deleteTask(String id){
        String sql = SQLQuery.DELETE_TASK.getQuery();
        return jdbcTemplate.update(sql, id);
    }
}
