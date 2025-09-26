// Jenkinsfile
pipeline {
    agent any // สั่งให้ Jenkins ทำงานบน node ไหนก็ได้ที่มี

    environment {
        // กำหนดค่าตัวแปรกลางที่ใช้ใน Pipeline
        // 🛡️ Security First: ควรใช้ Jenkins Credentials สำหรับข้อมูลสำคัญ
        DOCKERHUB_CREDENTIALS_ID = 'apartment-dockerhub-credentials'
        DOCKERHUB_USERNAME = 'pipatpongpri404'
        PROJECT_NAME = 'apartment-management'
    }

    stages {
        stage('1. Checkout Code') {
            steps {
                echo 'Checking out code from Git...'
                git branch: 'main', url: 'https://github.com/f-midterm/Setup.git' // <-- ❗️ เปลี่ยนเป็น URL ของ Repo คุณ
            }
        }

        stage('2. Build Backend') {
            steps {
                echo 'Building Spring Boot backend...'
                dir('backend') {
                    sh './gradlew clean build'
                }
            }
        }

        stage('3. Build Frontend') {
            steps {
                echo 'Building React frontend...'
                dir('frontend') {
                    sh 'npm install'
                    sh 'npm run build'
                }
            }
        }

        stage('4. Build Docker Images') {
            steps {
                echo 'Building Docker images...'
                script {
                    // สร้าง Image Tag จาก Build Number ของ Jenkins เพื่อให้ไม่ซ้ำกัน
                    def imageTag = "${env.BUILD_NUMBER}"

                    // Build Image
                    sh "docker build -t ${env.DOCKERHUB_USERNAME}/${env.PROJECT_NAME}-backend:${imageTag} ./backend"
                    sh "docker build -t ${env.DOCKERHUB_USERNAME}/${env.PROJECT_NAME}-frontend:${imageTag} ./frontend"
                    sh "docker build -t ${env.DOCKERHUB_USERNAME}/${env.PROJECT_NAME}-nginx:${imageTag} ./nginx"

                    // 🚀 Performance Optimization: ลบ dangling images เพื่อประหยัดพื้นที่
                    sh 'docker image prune -f'
                }
            }
        }

        stage('5. Push to Docker Hub') {
            steps {
                echo 'Logging in and pushing images to Docker Hub...'
                script {
                    def imageTag = "${env.BUILD_NUMBER}"
                    withCredentials([usernamePassword(credentialsId: env.DOCKERHUB_CREDENTIALS_ID, usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                        sh "echo ${DOCKER_PASS} | docker login -u ${DOCKER_USER} --password-stdin"
                        sh "docker push ${env.DOCKERHUB_USERNAME}/${env.PROJECT_NAME}-backend:${imageTag}"
                        sh "docker push ${env.DOCKERHUB_USERNAME}/${env.PROJECT_NAME}-frontend:${imageTag}"
                        sh "docker push ${env.DOCKERHUB_USERNAME}/${env.PROJECT_NAME}-nginx:${imageTag}"
                    }
                }
            }
        }

        stage('6. Deploy Application') {
            steps {
                echo 'Deploying application using Docker Compose...'
                //  หมายเหตุ: ขั้นตอนนี้เป็นตัวอย่าง deploy บนเครื่อง Jenkins เอง
                // ใน Production จริง อาจจะต้องใช้ SSH Agent เพื่อสั่ง deploy ไปยัง Server อื่น
                sh 'docker-compose down' // หยุด Container เก่า (ถ้ามี)
                sh 'docker-compose up -d'  // เริ่ม Container ใหม่ทั้งหมด
            }
        }
    }

    post {
        always {
            echo 'Pipeline finished.'
            // Logout จาก Docker Hub เพื่อความปลอดภัย
            sh 'docker logout'
        }
    }
}