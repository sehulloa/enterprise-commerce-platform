pipeline {
    agent any

    environment {
        APP_IMAGE = "enterprise-commerce-platform"
        IMAGE_TAG = "${env.BUILD_NUMBER}"
    }

    tools {
        jdk 'jdk21'
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh './gradlew clean build'
            }
        }

        stage('SonarQube Analysis') {
            when {
                expression { return env.SONAR_HOST_URL != null && env.SONAR_TOKEN != null }
            }
            steps {
                withEnv([
                        "SONAR_HOST_URL=${env.SONAR_HOST_URL}",
                        "SONAR_TOKEN=${env.SONAR_TOKEN}"
                ]) {
                    sh './gradlew sonar'
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                sh "docker build -t ${APP_IMAGE}:${IMAGE_TAG} ."
            }
        }

        stage('Publish Docker Image') {
            when {
                branch 'main'
            }
            steps {
                sh """
                    echo "${GHCR_TOKEN}" | docker login ghcr.io -u "${GHCR_USERNAME}" --password-stdin
                    docker tag ${APP_IMAGE}:${IMAGE_TAG} ghcr.io/${GHCR_USERNAME}/${APP_IMAGE}:${IMAGE_TAG}
                    docker tag ${APP_IMAGE}:${IMAGE_TAG} ghcr.io/${GHCR_USERNAME}/${APP_IMAGE}:latest
                    docker push ghcr.io/${GHCR_USERNAME}/${APP_IMAGE}:${IMAGE_TAG}
                    docker push ghcr.io/${GHCR_USERNAME}/${APP_IMAGE}:latest
                """
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: '**/build/libs/*.jar', allowEmptyArchive: true
        }
        success {
            echo 'Pipeline completed successfully.'
        }
        failure {
            echo 'Pipeline failed.'
        }
    }
}