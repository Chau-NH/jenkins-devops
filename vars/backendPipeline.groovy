#!/usr/bin/env groovy
void call(Map pipelineParams) {
    pipeline {
        agent any

        stages {
            stage('Checkout') {
                steps {
                    // Checkout from GIT
                }
            }

            stage('Install Dependencies') {
                steps {
                    // Install Node.js and npm on the Jenkins agent if not already available
                    // You can use a tool like Node Version Manager (NVM) for this purpose
                    // Example: sh 'nvm install 14'

                    // Install project dependencies using npm
                    // sh 'npm ci'
                }
            }

            stage('Test') {
                steps {
                    // Run tests
                    // Example: sh 'npm test'
                }
            }

            stage('Build') {
                steps {
                    // Build your NodeJS application
                    // sh 'npm run dev'
                }
            }

            stage('Deploy') {
                steps {

                }
            }
        }

        post {
            always {
                // Cleanup steps or post-processing, if needed
            }

            success {
                // Actions to perform when the pipeline succeeds
            }

            failure {
                // Actions to perform when the pipeline fails
            }
        }
    } 
}
