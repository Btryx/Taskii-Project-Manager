package com.webfejl.beadando.query;

public enum ProjectQuery {

    SELECT_ALL("select * from projects"),

    SELECT_PROJECT_BY_ID("SELECT * FROM projects WHERE project_id = ?"),
    SELECT_TASK_BY_PARENT_ID("SELECT * FROM projects WHERE parent_id = ?"),


    CREATE_PROJECT("insert into projects(project_id, project_name, created_at, active, parent_id) values(?,?,?,?,?)"),
    EDIT_PROJECT("update projects set project_name=?, created_at=?, active=?, parent_id=? where project_id=?"),
    DELETE_PROJECT("delete from projects where project_id=?");

    private final String query;

    ProjectQuery(String query) {
        this.query = query;
    }

    public String getQuery(){ return this.query; }
}
