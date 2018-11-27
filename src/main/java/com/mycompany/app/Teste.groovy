import hudson.EnvVars
import hudson.model.*
import jenkins.model.*

String buildType = getBuildType()
def version = getBuildVersion()
setVariables(buildType, version)

def setVariables(String buildType, version) {
    def releaseVersion = incrementVersion(buildType, version)
    def developmentVersion = releaseVersion + '-SNAPSHOT'
    def vars = [releaseVersion: releaseVersion, developmentVersion: developmentVersion]
    build.environments.add(0, Environment.create(new EnvVars(vars)))
}

def getBuildType() {
    build.getEnvironment(listener).get('param')
}

def  getBuildVersion() {
    def thr = Thread.currentThread()
    def currentBuild = thr?.executable
    def version = currentBuild.getParent().getModules().toArray()[0].getVersion()

    println '########################  version ########################'
    println version

    return version
}

def incrementVersion(buildType, version) {

    List listVersion = version.split("-")
    List numbers = listVersion[0].split("\\.")

    if (buildType == 'bug fix') {
        println '########################  bug fix ########################'

        numbers[2]++
    }

    if (buildType == 'feature') {
        println '########################  feature ########################'

        numbers[1]++
        numbers[2] = 0
    }

    if (buildType == 'release') {
        println '########################  release ########################'

        numbers[0]++
        numbers[1] = 0
        numbers[2] = 0
    }

    def newVersion = numbers.stream().inject() {
        str, item -> str + "." + item
    }
    println '########################  new version ########################'
    println newVersion

    return newVersion
}
