job('NodeJS example') {
    scm {
        git('git://github.com/wardviaene/docker-demo.git') {  node -> // is hudson.plugins.git.GitSCM
            node / gitConfigName('Jenkins DSL User')
            node / gitConfigEmail('jenkins-dsl@cubicle7.net')
        }
    }
    triggers {
        scm('H/5 * * * *') //poll git every 5 minutes
    }
    wrappers {
        nodejs('nodejs') // this is the name of the NodeJS installation in 
                         // Manage Jenkins -> Configure Tools -> NodeJS Installations -> Name
    }
    steps {
        shell("npm install")
    }
}