#!/usr/bin/env groovy
void call(Map pipelineParams) {
    String name = 'backend'
    String ecrUrl = '480566855108.dkr.ecr.us-east-1.amazonaws.com'
    String awsRegion = 'us-east-1'
    String clusterName = 'eks-demo'
    pipeline {
        agent any

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

            stage('Build Docker Image') {
                steps {
                    // Build Docker Image for Application
                    withAWS(credentials: 'aws-credentials', region: "${awsRegion}") {
                        sh "aws ecr get-login-password --region ${awsRegion} | docker login --username AWS --password-stdin ${ecrUrl}"
                        sh "docker build --no-cache -t ${name} ."
                        sh "docker tag ${name}:latest ${ecrUrl}/${name}:latest"
                        sh "docker push ${ecrUrl}/${name}:latest" 
                    }
                }
            }

            stage('Deploy') {
                steps {
                    withAWS(credentials: 'aws-credentials', region: "${awsRegion}") {
                        sh "aws eks describe-cluster --region ${awsRegion} --name ${clusterName} --query cluster.status"
                        sh "aws eks --region ${awsRegion} update-kubeconfig --name ${clusterName}"
                        sh "kubectl config set-context --current --namespace eks-ns"
                        sh "kubectl apply -f .cd/${name}.yaml"
                    }
                }
            }
        }
    } 
}
