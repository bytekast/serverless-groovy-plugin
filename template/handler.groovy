@Grab('com.amazonaws:aws-lambda-java-core:1.2.0')
@Grab('com.amazonaws:aws-lambda-java-events:2.2.7')
@Grab('org.codehaus.groovy:groovy-json:2.5.8')

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2ProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2ProxyResponseEvent
import groovy.json.JsonOutput
import groovy.transform.CompileStatic

@CompileStatic
def hello(event) {
  return event instanceof String ? "Hello, $event".toString() : event
}

@CompileStatic
def api(APIGatewayV2ProxyRequestEvent event) {
  final response = new APIGatewayV2ProxyResponseEvent()
  response.setStatusCode(200)
  response.setBody(JsonOutput.prettyPrint(JsonOutput.toJson(event)))
  return response
}
