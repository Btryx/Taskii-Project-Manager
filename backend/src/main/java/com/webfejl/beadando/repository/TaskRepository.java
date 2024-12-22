package com.webfejl.beadando.repository;

import com.webfejl.beadando.dto.TaskDTO;
import com.webfejl.beadando.entity.Task;
import com.webfejl.beadando.query.TaskQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TaskRepository{

    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<Task> getAllTasks(){
        String sql = TaskQuery.SELECT_ALL.getQuery();
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Task.class));
    }

    public Task getTaskById(String id){
        String sql = TaskQuery.SELECT_TASK.getQuery();
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Task.class), id);
    }

    public int createTask(Task task){
        String sql = TaskQuery.CREATE_TASK.getQuery();
        return jdbcTemplate.update(sql, task.getTaskId(), task.getTaskTitle(),
                task.getTaskStatus(), task.getTaskPriority(), task.getTaskDate(), task.getTaskDesc());
    }

    public List<Task> findByStatus(String status) {
        String sql = TaskQuery.SELECT_TASK_BY_STATUS.getQuery();
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Task.class), status);
    }

    public List<Task> findByPriority(int priority) {
        String sql = TaskQuery.SELECT_TASK_BY_PRIORITY.getQuery();
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Task.class), priority);
    }

    public List<Task> sortByTitle() {
        String sql = TaskQuery.SELECT_TASK_ORDER_BY_TITLE.getQuery();
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Task.class));
    }

    public List<Task> sortByDate() {
        String sql = TaskQuery.SELECT_TASK_ORDER_BY_DATE.getQuery();
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Task.class));
    }

    public int editTask(Task task, String id){
        String sql = TaskQuery.EDIT_TASK.getQuery();
        return jdbcTemplate.update(sql, task.getTaskTitle(), task.getTaskStatus(), task.getTaskPriority(),
                task.getTaskDate(), task.getTaskDesc(), id);
    }

    public int deleteTask(String id){
        String sql = TaskQuery.DELETE_TASK.getQuery();
        return jdbcTemplate.update(sql, id);
    }
}
