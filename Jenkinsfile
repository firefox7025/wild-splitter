podTemplate(yaml: """
apiVersion: v1
kind: Pod
metadata:
  labels:
    some-label: builder
spec:
  containers:
  - name: builder
    image: openjdk:15-jdk-nanoserver
    command:
    - cat
    tty: true
"""
  ) {

  node(POD_LABEL) {
    stage('Build and test') {
    checkout scm
      container('rust') {
        sh './gradlew clean build'
      }
    }
  }
}