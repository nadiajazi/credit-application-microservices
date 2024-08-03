pipeline {
    agent any

    environment {
        // Define your Docker registry credentials and Kubernetes cluster context
        DOCKER_REGISTRY_CREDENTIALS = 'docker-registry-credentials-id'
        KUBECONFIG_CREDENTIALS = 'kubeconfig-credentials-id'
    }

    stages {
        stage('Checkout') {
            steps {
                // Checkout the code from the repository
                checkout scm
            }
        }

        stage('Build Docker Images') {
            parallel {
                stage('Build Core Service') {
                    steps {
                        script {
                            docker.build('jazinadia11/core:latest', 'core')
                        }
                    }
                }

                stage('Build Payment Service') {
                    steps {
                        script {
                            docker.build('jazinadia11/payment:latest', 'payment')
                        }
                    }
                }

                stage('Build Notification Service') {
                    steps {
                        script {
                            docker.build('jazinadia11/notification:latest', 'notification')
                        }
                    }
                }

                stage('Build Gateway Service') {
                    steps {
                        script {
                            docker.build('jazinadia11/gateway:latest', 'gateway')
                        }
                    }
                }

                stage('Build Frontend') {
                    steps {
                        script {
                            docker.build('jazinadia11/frontend:latest', 'frontend')
                        }
                    }
                }
            }
        }

        stage('Test') {
            parallel {
                stage('Test Core Service') {
                    steps {
                        script {
                            // Run your tests for the core service
                            sh 'mvn test -f core/pom.xml'
                        }
                    }
                }

                stage('Test Payment Service') {
                    steps {
                        script {
                            // Run your tests for the payment service
                            sh 'mvn test -f payment/pom.xml'
                        }
                    }
                }

                stage('Test Notification Service') {
                    steps {
                        script {
                            // Run your tests for the notification service
                            sh 'mvn test -f notification/pom.xml'
                        }
                    }
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                script {
                    // Log in to Docker registry
                    docker.withRegistry('https://index.docker.io/v1/', DOCKER_REGISTRY_CREDENTIALS) {
                        // Push images to Docker registry
                        docker.image('jazinadia11/core:latest').push('latest')
                        docker.image('jazinadia11/payment:latest').push('latest')
                        docker.image('jazinadia11/notification:latest').push('latest')
                        docker.image('jazinadia11/gateway:latest').push('latest')
                        docker.image('jazinadia11/frontend:latest').push('latest')
                    }

                    // Set up kubectl context
                    withKubeConfig([credentialsId: KUBECONFIG_CREDENTIALS]) {
                        // Apply Kubernetes manifests
                        sh 'kubectl apply -f k8s/'
                    }
                }
            }
        }
    }

    post {
        always {
            // Clean up Docker images
            sh 'docker system prune -af'
        }
    }
}
