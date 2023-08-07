#!/usr/bin/env groovy
void call(Map pipelineParams) {
    String name = 'backend'
    String ecrUrl = '480566855108.dkr.ecr.us-east-1.amazonaws.com'
    String awsRegion = 'us-east-1'
    pipeline {
        agent any

        stages {
            stage('Prepare packages') {
                script {
                    writeFile file: '.ci/backend.yml', text: libraryResource('backend.yml')
                }
            }

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
                    sh 'npm install'
                }
            }

            stage('Test') {
                steps {
                    // Run tests ()
                    // Note: There is not script test in backend source
                    sh 'npm test'
                }
            }

            // stage('Build Docker Image') {
            //     steps {
            //         // Build Docker Image for Application
            //         withAWS(credentials: 'aws-credentials', region: "${awsRegion}") {
            //             sh "aws ecr get-login-password --region ${awsRegion} | docker login --username AWS --password-stdin ${ecrUrl}"
            //             sh "docker build -t ${name} ."
            //         }
            //     }
            // }

            // stage('Push Docker Image') {
            //     steps {
            //         sh "docker tag ${name}:latest ${ecrUrl}/${name}:latest"
            //         sh "docker push ${ecrUrl}/${name}:latest" 
            //     }
            // }

            // stage('Deploy to K8S') {
            //     steps {
                        // sh 'kubectl apply -f .cd/backend.yml'
            //     }
            // }
        }
    } 
}
