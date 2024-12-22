package com.webfejl.beadando.query;

public enum TaskQuery {

    SELECT_ALL("select * from tasks"),
    SELECT_TASK("select * from tasks where task_id = ?"),
    SELECT_TASK_BY_STATUS("SELECT * FROM tasks WHERE task_status = ?"),
    SELECT_TASK_BY_PRIORITY("SELECT * FROM tasks WHERE task_priority = ?"),
    SELECT_TASK_ORDER_BY_TITLE("SELECT * FROM tasks ORDER BY task_title"),
    SELECT_TASK_ORDER_BY_DATE("SELECT * FROM tasks ORDER BY task_date"),
    CREATE_TASK("insert into tasks(task_id, task_title, task_status, task_priority, task_date, task_desc) values(?,?,?,?,?,?)"),
    EDIT_TASK("update tasks set task_title=?, task_status=?, task_priority=?, task_date=?, task_desc=? where task_id=?"),
    DELETE_TASK("delete from tasks where task_id=?");

    private final String query;

    TaskQuery(String query) {
        this.query = query;
    }

    public String getQuery(){ return this.query; }
}
