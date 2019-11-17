@Grab('com.amazonaws:aws-lambda-java-core:1.2.0')
@Grab('com.amazonaws:aws-lambda-java-events:2.2.7')
@Grab('org.codehaus.groovy:groovy-json:2.5.8')

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2ProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2ProxyResponseEvent
import groovy.json.JsonOutput

class MyTest extends GroovyTestCase {
  void 'test hello'() {
    def handler = new handler()
    assert handler.hello("Rowell") == "Hello, Rowell"
  }

  void 'test api'() {
    def handler = new handler()
    def event = new APIGatewayV2ProxyRequestEvent()
    event.body = "Rowell"
    assert handler.api(event) == [
      statusCode: 200,
      body      : JsonOutput.prettyPrint(JsonOutput.toJson(event))
    ] as APIGatewayV2ProxyResponseEvent
  }
}