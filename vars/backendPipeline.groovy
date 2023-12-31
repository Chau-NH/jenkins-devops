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

            stage ('Prepare Package') {
                steps {
                    script {
                        writeFile file: '.ci/html.tpl', text: libraryResource('trivy/html.tpl')
                    }
                }
            }

            stage ("Trivy Scan Secret") {
                steps {
                        script {
                        sh "trivy fs --security-checks secret --exit-code 0 --format template --template @.ci/html.tpl -o .ci/secretreport.html ."
                        publishHTML (target : [allowMissing: true,
                            alwaysLinkToLastBuild: true,
                            keepAll: true,
                            reportDir: '.ci',
                            reportFiles: 'secretreport.html',
                            reportName: 'Trivy Secrets Report',
                            reportTitles: 'Trivy Secrets Report']
                        )
                    }
                }
            }
            
            stage('Install Dependencies') {
                steps {
                    // Install project dependencies using npm
                    sh 'npm install'
                }
            }

            stage ("Trivy Scan Vulnerabilities") {
                steps {
                        script {
                        sh "trivy fs --severity HIGH,CRITICAL --security-checks vuln --exit-code 0 --format template --template @.ci/html.tpl -o .ci/vulnreport.html ."
                        publishHTML (target : [allowMissing: true,
                            alwaysLinkToLastBuild: true,
                            keepAll: true,
                            reportDir: '.ci',
                            reportFiles: 'vulnreport.html',
                            reportName: 'Trivy Vulnerabilities Report',
                            reportTitles: 'Trivy Vulnerabilities Report']
                        )
                    }
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
                        sh "docker build -t ${name} ."
                        sh "docker tag ${name}:latest ${ecrUrl}/${name}:latest"
                        sh "docker push ${ecrUrl}/${name}:latest" 
                    }
                }
            }

            // stage('Deploy') {
            //     steps {
            //         withAWS(credentials: 'aws-credentials', region: "${awsRegion}") {
            //             withKubeConfig([credentialsId: 'eks-credentials']) {
            //                 sh 'curl -LO "https://storage.googleapis.com/kubernetes-release/release/v1.20.5/bin/linux/amd64/kubectl"'  
            //                 sh 'chmod u+x ./kubectl'
            //                 sh "./kubectl config set-context --current --namespace eks-ns"
            //                 sh "aws eks describe-cluster --region ${awsRegion} --name ${clusterName} --query cluster.status"
            //                 sh "aws eks --region ${awsRegion} update-kubeconfig --name ${clusterName}"
            //                 sh "./kubectl rollout restart deploy ${name}"
            //             }
            //         }
            //     }
            // }
        }
    } 
}
