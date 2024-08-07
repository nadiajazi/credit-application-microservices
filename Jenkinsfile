pipeline {
    agent any

    environment {
        ANSIBLE_SSH_CREDENTIALS_ID = 'ansibleadmin_ssh'
        SSHPASS_PATH = '/usr/bin/sshpass'
    }

    stages {
        stage('Clone Repository') {
            steps {
                git branch: 'main', url: 'https://github.com/nadiajazi/credit-application-microservices.git'
            }
        }

        stage('Run Ansible Playbook') {
            steps {
                sh "echo 'Running Ansible Playbook...'"
                sh """
                ${SSHPASS_PATH} -p 'Eniso@11' ssh -o StrictHostKeyChecking=no ansibleadmin@20.86.49.158 <<EOF
                cd /opt/docker
                ansible-playbook ansible.yml
                EOF
                """
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}
