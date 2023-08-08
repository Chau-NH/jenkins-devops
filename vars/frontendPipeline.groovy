#!/usr/bin/env groovy
void call(Map pipelineParams) {
    String name = 'backend'
    String ecrUrl = '480566855108.dkr.ecr.us-east-1.amazonaws.com'
    String awsRegion = 'us-east-1'
    pipeline {
        agent any

        stages {
            stage('Checkout') {
                steps {
                    // Checkout source code from GIT
                    sh 'git checkout main'
                    sh 'git pull'
                }
            }

            stage('Install Dependencies') {
                steps {
                    // Install project dependencies using npm
                    sh 'npm ci'
                }
            }

            stage('Test') {
                steps {
                    // Run tests if you have them
                    sh 'npm test'
                }
            }

            stage('Build Docker Image') {
                steps {
                    // Build Docker Image for Application
                    withAWS(credentials: 'aws-credentials', region: "${awsRegion}") {
                        sh "aws ecr get-login-password --region ${awsRegion} | docker login --username AWS --password-stdin ${ecrUrl}"
                        sh "docker build -t ${name} ."
                        sh "docker tag ${name}:latest ${ecrUrl}/${name}:latest"
                        sh "docker push ${ecrUrl}/${name}:latest"
                    }
                }
            }

            // stage('Deploy') {
            //     steps {

            //     }
            // }
        }
    }
}
