CREATE TABLE users (
    user_id VARCHAR(36) PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    enabled BOOLEAN NOT NULL
);

ALTER TABLE users ADD COLUMN email VARCHAR(128);
ALTER TABLE `users` ADD UNIQUE `tasks_uq_email`(`email`);

CREATE TABLE projects (
    project_id VARCHAR(36) PRIMARY KEY,
    project_name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    active BOOLEAN NOT NULL,
    parent_id INT,
    user_id VARCHAR(36) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

ALTER TABLE `projects` ADD UNIQUE `projects_uq_01`(`project_name`, `user_id`);
ALTER TABLE projects ADD COLUMN project_desc text;

CREATE TABLE tasks (
    task_id VARCHAR(36) PRIMARY KEY,
    task_title VARCHAR(100) NOT NULL,
    task_status VARCHAR(50) NOT NULL,
    task_priority INT NOT NULL,
    task_date TIMESTAMP NOT NULL,
    task_desc TEXT,
    project_id VARCHAR(36) NOT NULL,
    FOREIGN KEY (project_id) REFERENCES projects(project_id)
);

ALTER TABLE tasks MODIFY COLUMN task_date timestamp null;
ALTER TABLE tasks MODIFY COLUMN task_desc TEXT null;
ALTER TABLE tasks ADD COLUMN assignee VARCHAR(36);
ALTER TABLE tasks ADD COLUMN order_number INT UNSIGNED;

CREATE TABLE collaborators (
    collaborator_id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    project_id VARCHAR(36) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (project_id) REFERENCES projects(project_id)
);

ALTER TABLE collaborators ADD COLUMN role VARCHAR(36);

CREATE TABLE statuses (
    status_id VARCHAR(36) PRIMARY KEY,
    status_name VARCHAR(36),
    order_number INT UNSIGNED,
    project_id VARCHAR(36) NOT NULL
);

ALTER TABLE `statuses` ADD UNIQUE `statuses_uq_01`(`status_name`, `project_id`);

CREATE TABLE comments (
    comment_id VARCHAR(36) PRIMARY KEY,
    task_id VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    comment text NOT NULL,
    user_id VARCHAR(36) NOT NULL,
    FOREIGN KEY (task_id) REFERENCES tasks(task_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);
