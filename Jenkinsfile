// // f-midterm/setup/Jenkinsfile

// pipeline {
//     // 1. Agent Configuration
//     // The 'agent any' directive tells Jenkins to execute this pipeline on any available agent.
//     agent any

//     // 2. Environment Variables
//     // Define environment variables that can be used throughout the pipeline.
//     environment {
//         // This variable is used to prefix our Docker images, making them easy to identify.
//         // In a real-world scenario, you might get this from a Docker Registry credential.
//         DOCKER_IMAGE_PREFIX = 'apartment-mgmt'
//     }

//     // 3. Pipeline Stages
//     // The 'stages' block contains the sequence of steps for our CI/CD process.
//     stages {
//         // Stage 1: Checkout code from version control
//         stage('Checkout') {
//             steps {
//                 echo 'Checking out the source code...'
//                 // 'scm' is a special variable that holds the SCM configuration from the Jenkins job.
//                 checkout scm
//             }
//         }

//         // Stage 2: Build and run unit tests for the backend
//         stage('Build & Test Backend') {
//             steps {
//                 echo 'Building Spring Boot application and running unit tests...'
//                 // We execute the Gradle wrapper to ensure a consistent build environment.
//                 // 'clean' removes previous build artifacts, and 'build' compiles and tests the code.
//                 dir('backend') {
//                     // FIX: Add execute permission to the gradlew script
//                     sh 'chmod +x ./gradlew' 
                    
//                     // Now, run the build command
//                     sh './gradlew clean build'
//                 }
//             }
//         }

//         // Stage 3: Build Docker images for both services
//         stage('Build Docker Images') {
//             steps {
//                 script {
//                     echo "Building Backend Docker image..."
//                     // We use the git commit hash as the image tag for precise versioning.
//                     def backendImageTag = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
//                     // The 'docker.build' command from the Docker Pipeline plugin builds the image.
//                     docker.build("${DOCKER_IMAGE_PREFIX}/backend:${backendImageTag}", './backend')

//                     echo "Building Frontend Docker image..."
//                     def frontendImageTag = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
//                     docker.build("${DOCKER_IMAGE_PREFIX}/frontend:${frontendImageTag}", './frontend')
//                 }
//             }
//         }
        
//         /*
//         // -----------------------------------------------------------------
//         // Stage 4: End-to-End Testing with Cypress (Commented out for now)
//         // -----------------------------------------------------------------
//         // This stage is disabled by default. To enable it:
//         // 1. Ensure your Cypress tests in 'frontend/cypress' are ready.
//         // 2. Ensure your docker-compose.yml is configured to use the newly built images.
//         // 3. Uncomment the entire 'stage' block below.
//         // -----------------------------------------------------------------
//         stage('E2E Testing') {
//             steps {
//                 echo 'Starting application stack for E2E tests...'
//                 // We start all services in the background using docker-compose.
//                 sh 'docker-compose -f docker-compose.yml up -d'
                
//                 // 🚀 Performance Optimization: It's crucial to wait for the backend to be fully ready.
//                 // A simple 'sleep' is easy but not robust. A better approach is to poll a health check endpoint.
//                 echo 'Waiting for backend API to be available...'
//                 sh 'sleep 30' // Giving services 30 seconds to initialize.

//                 echo 'Running Cypress E2E tests...'
//                 // We navigate to the frontend directory to run the npm script.
//                 dir('frontend') {
//                     // This command runs the Cypress tests defined in your 'package.json'.
//                     sh 'npm install'
//                     sh 'npm run cy:run'
//                 }
//             }
//             // The 'post' block defines actions that run after the stage completes.
//             // 'always' ensures these actions run whether the tests passed or failed.
//             post {
//                 always {
//                     echo 'Tearing down the application stack...'
//                     // It's critical to clean up the running containers to free up resources.
//                     sh 'docker-compose -f docker-compose.yml down'
//                 }
//             }
//         }
//         */

//         // Stage 5: Deploy the Application
//         stage('Deploy') {
//             steps {
//                 echo 'Deploying application using Docker Compose...'
//                 // This command starts all services in detached mode.
//                 // In a real production environment, this step would be more complex,
//                 // likely involving SSHing to a remote server and running the command there.
//                 sh 'docker-compose -f docker-compose.yml up -d'
//             }
//         }
//     }
// }

// f-midterm/setup/Jenkinsfile

pipeline {
    // 1. Agent Configuration
    // WHY: We specify a Docker agent to ensure that Docker commands are available.
    // The image 'docker:24-git' includes both Docker and Git clients.
    // The '-v' argument mounts the host's Docker socket into the container,
    // allowing the container to communicate with the host's Docker daemon.
    agent {
        docker {
            image 'docker:24-git' // A container image with Docker and Git installed
            args '-v /var/run/docker.sock:/var/run/docker.sock'
        }
    }

    // 2. Environment Variables
    environment {
        // This variable is used to prefix our Docker images, making them easy to identify.
        DOCKER_IMAGE_PREFIX = 'apartment-mgmt'
    }

    // 3. Pipeline Stages
    stages {
        // Stage 1: Checkout is implicitly handled by Jenkins when using SCM,
        // but we can keep an explicit stage if we want more control or logging.
        stage('Checkout') {
            steps {
                echo 'Checking out the source code...'
                checkout scm
            }
        }

        // Stage 2: Build and run unit tests for the backend
        stage('Build & Test Backend') {
            steps {
                echo 'Building Spring Boot application and running unit tests...'
                dir('backend') {
                    // This permission is usually needed when the file is checked out on a non-Linux system.
                    sh 'chmod +x ./gradlew'
                    sh './gradlew clean build'
                }
            }
        }

        // Stage 3: Build Docker images for both services
        stage('Build Docker Images') {
            steps {
                script {
                    // Use the first 7 characters of the git commit hash for a unique image tag.
                    def imageTag = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()

                    // 🚀 Performance Optimization: Run builds in parallel to save time.
                    parallel(
                        backend: {
                            echo "Building Backend Docker image: ${DOCKER_IMAGE_PREFIX}/backend:${imageTag}"
                            // The docker.build command is part of the Docker Pipeline plugin.
                            // It's a clean way to build images within a Jenkinsfile.
                            // The second argument is the build context path.
                            docker.build("${DOCKER_IMAGE_PREFIX}/backend:${imageTag}", './backend')
                        },
                        frontend: {
                            echo "Building Frontend Docker image: ${DOCKER_IMAGE_PREFIX}/frontend:${imageTag}"
                            docker.build("${DOCKER_IMAGE_PREFIX}/frontend:${imageTag}", './frontend')
                        }
                    )
                }
            }
        }
        
        // ⚙️ Deployment Readiness: Added a placeholder stage for pushing to a registry.
        stage('Push to Registry') {
            steps {
                script {
                    // In a real production pipeline, you would push the images to a
                    // container registry like Docker Hub, AWS ECR, or Google GCR.
                    // This step is currently a placeholder.
                    echo "Skipping push to registry for this example."
                    // Example push commands (would require docker.withRegistry block):
                    // docker.image("${DOCKER_IMAGE_PREFIX}/backend:${imageTag}").push()
                    // docker.image("${DOCKER_IMAGE_PREFIX}/frontend:${imageTag}").push()
                }
            }
        }

        // Stage 4: Deploy the Application
        stage('Deploy') {
            steps {
                echo 'Deploying application using Docker Compose...'
                // This command starts all services in detached mode.
                // It will now use the images built in the previous stage if they are
                // referenced correctly in your docker-compose.yml file.
                sh 'docker-compose -f docker-compose.yml up -d'
            }
        }
    }
}
