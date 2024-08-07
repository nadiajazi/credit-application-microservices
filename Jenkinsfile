pipeline {
    agent any

    environment {

        ANSIBLE_SSH_CREDENTIALS_ID = 'ansibleadmin_ssh'
    }

    stages {
        stage('Clone Repository') {
            steps {
                git branch: 'main', url: 'git@github.com:nadiajazi/credit-application-microservices.git'
            }
        }

        stage('Run Ansible Playbook') {
            steps {
                sshagent(credentials: [env.ANSIBLE_SSH_CREDENTIALS_ID]) {
                  sh '''
                  ssh -o StrictHostKeyChecking=no ansibleadmin@20.86.49.158 <<EOF
                  cd /opt/docker
                  ansible-playbook ansible.yml
                  EOF
                 '''
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
