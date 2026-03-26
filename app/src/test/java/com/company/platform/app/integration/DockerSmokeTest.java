package com.company.platform.app.integration;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@Disabled("Local Windows Docker/Testcontainers issue: Testcontainers cannot detect a valid Docker environment")
class DockerSmokeTest {

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16");

    @Test
    void should_start_container() {
        assertThat(postgres.isRunning()).isTrue();
    }}
