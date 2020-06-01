@Library('jenkins_library@v0.1.3')
import com.pratclot.*

slack = new Slack(this).startThread()
extendedSteps = new ExtendedSteps(this)

GITHUB_TOKEN_ID = "github_token"
GOOGLE_SERVICE_CREDENTIALS_ID = "google_service_account_firebase_admin"
GOOGLE_PROJECT_VAR_ID = "firebase_project_id"
GOOGLE_SERVICES_FILE_VAR_ID = "google_services.json"

pipeline {
    agent any

    stages {
        stage('Lint') {
            steps {
                script {
                    extendedSteps """
                        ./gradlew ktLintDebugCheck
                    """
                }
            }
        }
        stage('Build') {
            steps {
                script {
                    extendedSteps """
                        ./gradlew clean compileDebugSources
                    """
                }
            }
        }
    }

    post {
        success {
            script {
                slack.markThreadAsGreen()
            }
        }
        failure {
            script {
                slack.markThreadAsRed()
            }
        }
        always {
            junit "**/build/test-results/**/*.xml"
        }
    }
}
