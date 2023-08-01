#!/usr/bin/env groovy
void call(Map pipelineParams) {
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

            // stage('Build Image') {
            //     steps {
            //         // Build your NodeJS application
            //         // sh 'npm run dev'
            //     }
            // }

            // stage('Deploy') {
            //     steps {

            //     }
            // }
        }

        // post {
        //     always {
        //         // Cleanup steps or post-processing, if needed
        //     }

        //     success {
        //         // Actions to perform when the pipeline succeeds
        //     }

        //     failure {
        //         // Actions to perform when the pipeline fails
        //     }
        // }
    } 
}
