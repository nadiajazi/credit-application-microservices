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
               sh """
               ${SSHPASS_PATH} -p 'Eniso@11' ssh -o StrictHostKeyChecking=no ansibleadmin@20.86.49.158 'echo "cd /opt/docker && ansible-playbook ansible.yml" > /tmp/run_playbook.sh && chmod +x /tmp/run_playbook.sh && /tmp/run_playbook.sh'
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
