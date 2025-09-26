// f-midterm/setup/Jenkinsfile

pipeline {
    // 1. Agent Configuration
    // The 'agent any' directive tells Jenkins to execute this pipeline on any available agent.
    agent any

    // 2. Environment Variables
    // Define environment variables that can be used throughout the pipeline.
    environment {
        // This variable is used to prefix our Docker images, making them easy to identify.
        // In a real-world scenario, you might get this from a Docker Registry credential.
        DOCKER_IMAGE_PREFIX = 'apartment-mgmt'
    }

    // 3. Pipeline Stages
    // The 'stages' block contains the sequence of steps for our CI/CD process.
    stages {
        // Stage 1: Checkout code from version control
        stage('Checkout') {
            steps {
                echo 'Checking out the source code...'
                // 'scm' is a special variable that holds the SCM configuration from the Jenkins job.
                checkout scm
            }
        }

        // Stage 2: Build and run unit tests for the backend
        stage('Build & Test Backend') {
            steps {
                echo 'Building Spring Boot application and running unit tests...'
                // We execute the Gradle wrapper to ensure a consistent build environment.
                // 'clean' removes previous build artifacts, and 'build' compiles and tests the code.
                dir('backend') {
                    sh './gradlew clean build'
                }
            }
        }

        // Stage 3: Build Docker images for both services
        stage('Build Docker Images') {
            steps {
                script {
                    echo "Building Backend Docker image..."
                    // We use the git commit hash as the image tag for precise versioning.
                    def backendImageTag = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
                    // The 'docker.build' command from the Docker Pipeline plugin builds the image.
                    docker.build("${DOCKER_IMAGE_PREFIX}/backend:${backendImageTag}", './backend')

                    echo "Building Frontend Docker image..."
                    def frontendImageTag = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
                    docker.build("${DOCKER_IMAGE_PREFIX}/frontend:${frontendImageTag}", './frontend')
                }
            }
        }
        
        /*
        // -----------------------------------------------------------------
        // Stage 4: End-to-End Testing with Cypress (Commented out for now)
        // -----------------------------------------------------------------
        // This stage is disabled by default. To enable it:
        // 1. Ensure your Cypress tests in 'frontend/cypress' are ready.
        // 2. Ensure your docker-compose.yml is configured to use the newly built images.
        // 3. Uncomment the entire 'stage' block below.
        // -----------------------------------------------------------------
        stage('E2E Testing') {
            steps {
                echo 'Starting application stack for E2E tests...'
                // We start all services in the background using docker-compose.
                sh 'docker-compose -f docker-compose.yml up -d'
                
                // 🚀 Performance Optimization: It's crucial to wait for the backend to be fully ready.
                // A simple 'sleep' is easy but not robust. A better approach is to poll a health check endpoint.
                echo 'Waiting for backend API to be available...'
                sh 'sleep 30' // Giving services 30 seconds to initialize.

                echo 'Running Cypress E2E tests...'
                // We navigate to the frontend directory to run the npm script.
                dir('frontend') {
                    // This command runs the Cypress tests defined in your 'package.json'.
                    sh 'npm install'
                    sh 'npm run cy:run'
                }
            }
            // The 'post' block defines actions that run after the stage completes.
            // 'always' ensures these actions run whether the tests passed or failed.
            post {
                always {
                    echo 'Tearing down the application stack...'
                    // It's critical to clean up the running containers to free up resources.
                    sh 'docker-compose -f docker-compose.yml down'
                }
            }
        }
        */

        // Stage 5: Deploy the Application
        stage('Deploy') {
            steps {
                echo 'Deploying application using Docker Compose...'
                // This command starts all services in detached mode.
                // In a real production environment, this step would be more complex,
                // likely involving SSHing to a remote server and running the command there.
                sh 'docker-compose -f docker-compose.yml up -d'
            }
        }
    }
}