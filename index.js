'use strict';

const {spawnSync} = require("child_process")

class ServerlessPlugin {
  constructor(serverless, options) {
    this.serverless = serverless
    this.options = options

    this.servicePath = this.serverless.config.servicePath
    this.serverlessPath = `${this.serverless.config.servicePath}/.serverless`
    this.jarScript = `${__dirname}/scriptjar.groovy`
    this.testScript = `${__dirname}/runTests.groovy`
    this.functions = (this.serverless.service || {}).functions
    this.functionKeys = Object.keys(this.functions) || []

    this.commands = {
      groovy: {
        commands: {
          test: {
            lifecycleEvents: ['run']
          }
        }
      }
    }

    this.hooks = {
      "groovy:test:run": this.test.bind(this),
      "after:package:createDeploymentArtifacts": this.jar.bind(this),
      "before:deploy:function:packageFunction": this.jar.bind(this)
    }
  }

  test() {
    spawnSync('groovy', [this.testScript, this.servicePath], {
      cwd: process.cwd(),
      env: process.env,
      stdio: 'inherit',
      encoding: 'utf-8'
    })
  }

  jar() {
    this.functionKeys.forEach((name) => {

      const func = this.functions[name]

      const deploymentArtifact = `${this.serverlessPath}/${name}.jar`
      const handler = `${func.handler.split('::')[0]}.groovy`

      const result = spawnSync('groovy', [this.jarScript, handler, deploymentArtifact], {
        cwd: process.cwd(),
        env: process.env,
        stdio: 'inherit',
        encoding: 'utf-8'
      })
      if (result.status == 0) {
        func.runtime = 'java8'
        func.package = {
          artifact: deploymentArtifact
        }
        this.serverless.cli.log(`Successfully packaged function: ${name}`)
      } else {
        console.error(`Unable to package deployment artifact ${deploymentArtifact}`)
        console.error(result.stderr)
      }
    })
  }
}

module.exports = ServerlessPlugin
