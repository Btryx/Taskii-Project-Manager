package com.webfejl.beadando.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@Table("projects")
public class Project {
    @Id
    private @Column("project_id") String projectId= UUID.randomUUID().toString();
    private @Column("project_name") String projectName;
    private @Column("created_at") Date createdAt;
    private @Column("active") Boolean active;
    private @Column("parent_id") Integer parentId;
}

