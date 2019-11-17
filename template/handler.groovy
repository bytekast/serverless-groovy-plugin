@Grab('com.amazonaws:aws-lambda-java-core:1.2.0')
@Grab('com.amazonaws:aws-lambda-java-events:2.2.7')
@Grab('org.codehaus.groovy:groovy-json:2.5.8')

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2ProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2ProxyResponseEvent
import groovy.json.JsonOutput
import groovy.transform.CompileStatic

def hello(event) {
  return event instanceof String ? "Hello, $event" : JsonOutput.prettyPrint(JsonOutput.toJson(event))
}

@CompileStatic
def api(APIGatewayV2ProxyRequestEvent event) {
  final response = new APIGatewayV2ProxyResponseEvent()
  response.setStatusCode(200)
  response.setBody(JsonOutput.prettyPrint(JsonOutput.toJson(event)))
  return response
}

class HandlerTest extends GroovyTestCase {
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
junit.textui.TestRunner.run(HandlerTest)
