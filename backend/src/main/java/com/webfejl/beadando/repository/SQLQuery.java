package com.webfejl.beadando.repository;

public enum SQLQuery {

    SELECT_ALL("select * from tasks"),
    SELECT_TASK("select * from tasks where task_id = ?"),
    CREATE_TASK("insert into tasks(task_id, task_title, task_status, task_priority, task_date, task_desc) values(?,?,?,?,?,?)"),
    EDIT_TASK("update tasks set task_title=?, task_status=?, task_priority=?, task_date=?, task_desc=? where task_id=?"),
    DELETE_TASK("delete from tasks where task_id=?");

    private final String query;

    SQLQuery(String query) {
        this.query = query;
    }

    public String getQuery(){ return this.query; }
}
