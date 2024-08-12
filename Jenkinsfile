pipeline {
    agent any

    environment {

        ANSIBLE_SSH_CREDENTIALS_ID = 'ansibleadmin_credentials'
        ANSIBLE_HOST_CREDENTIALS_ID = 'ansible_host'
    }

    stages {
        stage('Clone Repository') {
            steps {
                git branch: 'main', url: 'https://github.com/nadiajazi/credit-application-microservices.git'
            }
        }

        stage('Run Ansible Playbook') {
            steps {
                script {

                    withCredentials([
                        usernamePassword(credentialsId: env.ANSIBLE_SSH_CREDENTIALS_ID, usernameVariable: 'ANSIBLE_SSH_USER', passwordVariable: 'ANSIBLE_SSH_PASSWORD'),
                        string(credentialsId: env.ANSIBLE_HOST_CREDENTIALS_ID, variable: 'ANSIBLE_HOST')
                    ]) {

                        sh """
                        sshpass -p \${ANSIBLE_SSH_PASSWORD} ssh -o StrictHostKeyChecking=no \${ANSIBLE_SSH_USER}@\${ANSIBLE_HOST} 'cd /opt/docker && ansible-playbook ansible.yml'
                        """
                    }
                }
            }
        }

        stage('Deploy to Kubernetes'') {
                    steps {
                        script {

                            withCredentials([
                                usernamePassword(credentialsId: env.ANSIBLE_SSH_CREDENTIALS_ID, usernameVariable: 'ANSIBLE_SSH_USER', passwordVariable: 'ANSIBLE_SSH_PASSWORD'),
                                string(credentialsId: env.ANSIBLE_HOST_CREDENTIALS_ID, variable: 'ANSIBLE_HOST')
                            ]) {

                                sh """
                                sshpass -p \${ANSIBLE_SSH_PASSWORD} ssh -o StrictHostKeyChecking=no \${ANSIBLE_SSH_USER}@\${ANSIBLE_HOST} 'cd /opt/docker && ansible-playbook kubernetes.yml'
                                """
                            }
                        }
                    }
                }
    }

    post {
            always {

                cleanWs()
            }
        }
}
