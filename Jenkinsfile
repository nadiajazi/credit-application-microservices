pipeline {
    agent any

    environment {
        // Use Jenkins credentials
        ANSIBLE_SSH_CREDENTIALS_ID = 'ansibleadmin_credentials'
        ANSIBLE_HOST = '20.86.49.158'
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

                    withCredentials([usernamePassword(credentialsId: env.ANSIBLE_SSH_CREDENTIALS_ID, usernameVariable: 'ANSIBLE_SSH_USER', passwordVariable: 'ANSIBLE_SSH_PASSWORD')]) {
                        // Use sshpass for password-based SSH
                        sh """
                        sshpass -p \${ANSIBLE_SSH_PASSWORD} ssh -o StrictHostKeyChecking=no \${ANSIBLE_SSH_USER}@\${ANSIBLE_HOST} 'echo "cd /opt/docker && ansible-playbook ansible.yml" > /tmp/run_playbook.sh && chmod +x /tmp/run_playbook.sh && /tmp/run_playbook.sh'
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
