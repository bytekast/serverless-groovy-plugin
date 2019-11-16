# Serverless Groovy Plugin

#### Deploy [Groovy](http://groovy-lang.org/) [Lambda](https://aws.amazon.com/lambda/) Functions the EASY way using the [Serverless Framework](https://serverless.com/)

## 📦 Prerequisites

Install [Node](https://www.npmjs.com/get-npm).

Install the [Serverless Framework](https://serverless.com/framework/).

Install **Java** and **Groovy**. The easiest way to install is to use [SDK MAN](https://sdkman.io/):

```bash
➜ curl -s "https://get.sdkman.io" | bash
➜ sdk install java 8.0.222-amzn
➜ sdk install groovy
```

You also need to setup your AWS credentials/profiles in the `~/.aws/credentials` file.

```
[default]
aws_access_key_id = XXXXXXXXXXXXXX
aws_secret_access_key = XXXXXXXXXXXXXX
region = us-east-1
```

## 🛵 Usage

A Serverless Framework template is available here: https://github.com/bytekast/serverless-groovy-plugin/tree/master/template

To use the template, simply run:
```
➜ serverless create --template-url https://github.com/bytekast/serverless-groovy-plugin/tree/master/template --path my-api
➜ cd my-api
➜ serverless plugin install -n serverless-groovy
➜ serverless deploy 
```

## 📖 Details

The are many Java/Groovy tools and frameworks that allow developers to deploy serverless functions. However, most of them require complicated setup and build configurations. The intent of this plugin is to make it as simple as possible to use Groovy with Serverless.

It only requires 2 files: the `serverless.yml` configuration file and a `handler.js` groovy script that contains your function logic. This makes it as simple as `python` or `node js` based Serverless Framework projects.

Here is a sample `serverless.yml` config file:

```yaml
service: serverless-groovy

provider:
  name: aws

functions:
  hello:
    handler: handler::hello

plugins:
  - serverless-groovy

```

The `handler` config above specifies the name of the groovy file (`handler.groovy`) without the `.groovy` extension and the function (`hello`).

Here is the corresponding `handler.groovy` file:

```groovy
def hello(message) {
  return "Hello, ${message}"
}
```

You may even use external dependencies in your handler:

```groovy
@Grab('com.amazonaws:aws-lambda-java-core:1.2.0')
@Grab('com.amazonaws:aws-lambda-java-events:2.2.7')
@Grab('org.codehaus.groovy:groovy-json:2.5.8')

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2ProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2ProxyResponseEvent
import groovy.json.JsonOutput
import groovy.transform.CompileStatic

@CompileStatic
def api(APIGatewayV2ProxyRequestEvent event) {
  final response = new APIGatewayV2ProxyResponseEvent()
  response.setStatusCode(200)
  response.setBody(JsonOutput.prettyPrint(JsonOutput.toJson(event)))
  return response
}
```

> As you can see above, you can enable static typing by annotating your function with `@CompileStatic`

The `serverless.yml` could look like this if you are exposing an http endpoint:

```yaml
service: serverless-groovy

provider:
  name: aws

functions:
  api:
    handler: handler::api
    events:
      - http:
          path: api
          method: get

plugins:
  - serverless-groovy

```


To deploy your service, simply run `serverless deploy`.

The plugin will take care of compiling and packaging your Groovy scripts automatically.

```
Serverless: Packaging service...
Serverless: Excluding development dependencies...
Serverless: Successfully packaged function: hello
Serverless: Successfully packaged function: api
Serverless: Creating Stack...
Serverless: Checking Stack create progress...
........
Serverless: Stack create finished...
Serverless: Uploading CloudFormation file to S3...
Serverless: Uploading artifacts...
Serverless: Uploading service hello.jar file to S3 (6.08 MB)...
Serverless: Uploading service api.jar file to S3 (6.08 MB)...
Serverless: Validating template...
Serverless: Updating Stack...
Serverless: Checking Stack update progress...
.......................................
Serverless: Stack update finished...
Service Information
service: my-api
stage: dev
region: us-east-1
stack: my-api-dev
resources: 14
api keys:
  None
endpoints:
  GET - https://xxxxxxxxxx.execute-api.us-east-1.amazonaws.com/dev/api
functions:
  hello: my-api-dev-hello
  api: my-api-dev-api
layers:
  None
```

#### Voiala!
