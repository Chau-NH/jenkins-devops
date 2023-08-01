#!/usr/bin/env groovy
void call(Map pipelineParams) {
    String name = 'backend'
    String devopsRegistry = 'Registy on AWS (ECR)'
    pipeline {
        agent any

        options {
            disableConcurrentBuilds()
            disableResume()
            timeout(time: 1, unit: 'HOURS')
        }

        stages {
            stage('Checkout') {
                steps {
                    // Checkout from GIT
                    sh 'git checkout main'
                    sh 'git pull'
                }
            }

            stage('Install Dependencies') {
                steps {
                    // Install Node.js and npm on the Jenkins agent if not already available
                    // You can use a tool like Node Version Manager (NVM) for this purpose
                    // Example: sh 'nvm install 13'

                    // Install project dependencies using npm
                    sh 'npm ci'
                }
            }

            stage('Test') {
                steps {
                    // Run tests ()
                    // Note: There is not script test in backend source
                    sh 'npm test'
                }
            }

            stage('Build Docker Image') {
                steps {
                    // Build Docker Image for Application
                    sh 'docker build . -t backend'
                }
            }

            // stage('Push Docker Image') {
            //     steps {
            //         docker.withRegistry('', '') {
            //             sh "docker login ${devopsRegistry} -u ${USERNAME} -p ${PASSWORD}"
            //             sh "docker push ${devopsRegistry}/${name}:${BUILD_NUMBER}"
            //         }
            //     }
            // }

            // stage('Deploy to K8S') {
            //     steps {
                        // sh 'kubectl apply -f resources/backend.yml'
            //     }
            // }
        }
    } 
}
