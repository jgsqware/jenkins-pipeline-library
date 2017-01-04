
@NonCPS
def images() {
    return [
        maven: 's00vl9961140.be.net.intra:8183/cip/maven:3.3.9-jdk-8-alpine',
        sonar: 's00vl9961140.be.net.intra:8183/sonar-runner:2.4'
    ]
}

@NonCPS
def kubeNodeName(){
    return org.apache.commons.lang.RandomStringUtils.random(12, true, true)
}

@NonCPS
def containerTemplates(c){
    return c.collect { c_name ->
            if(!images()[c_name]){
                error "'${c_name}' is not an existing containerTemplate"
            }
            containerTemplate(name: c_name, image: images()[c_name], ttyEnabled: true, command: 'cat')
        }
}

def call(c, Closure body) {

    def name = kubeNodeName()
    podTemplate(
        label: name, 
        containers: containerTemplates(c) 
    ) {
        node(name) {
            body()
        }
    }
}