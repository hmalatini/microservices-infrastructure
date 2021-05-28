package com.infra.resources.core.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@RequiredArgsConstructor
@Getter
@Setter
@Entity
public class Microservice {

    @Id
    private String name;
    private String createdBy;
    private String team;
    private String gitUrl;
    private String deploymentGitUrl;
    private String infraGitUrl;
    private String terraformWorkspaceId;
    private String template;
}
