package com.barchart.backend.testmaven

import com.twitter.finagle.builder.Server
import com.twitter.finagle.builder.ServerBuilder
import com.twitter.util.Future
import com.twitter.finagle.Service
import com.twitter.finagle.http.Http
import org.jboss.netty.handler.codec.http.HttpRequest
import org.jboss.netty.handler.codec.http.HttpResponse
import org.jboss.netty.handler.codec.http.DefaultHttpResponse
import java.net.InetSocketAddress
import java.net.SocketAddress
import org.jboss.netty.handler.codec.http.HttpVersion
import org.jboss.netty.handler.codec.http.HttpResponseStatus
import com.twitter.finagle.http.RichHttp
import com.twitter.finagle.http.Request
import org.jboss.netty.handler.codec.http.DefaultHttpRequest
import com.twitter.finagle.builder.ClientBuilder
import org.jboss.netty.handler.codec.http.HttpMethod


object ServerBase {

	val service: Service[HttpRequest, HttpResponse] = new Service[HttpRequest, HttpResponse] { 
		def apply(request: HttpRequest) = Future(
		    new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK))          
	}
	
	val address: SocketAddress = new InetSocketAddress(10000)
	
	val server: Server = ServerBuilder()                            
		.codec(Http.get)
		.bindTo(address)
		.name("HttpServer")
		.build(service)
  
	val client: Service[HttpRequest, HttpResponse] = ClientBuilder()                          
		.codec(Http.get)
		.hosts(address)
		.hostConnectionLimit(1)
		.build()
	
	def main(args: Array[String]) {
		// Issue a request, get a response:
		val request: HttpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/")
			request.setHeader("Authorization", "open sez me");
	
		val responseFuture: Future[HttpResponse] = 
			client(request) onSuccess { 
				response => println("Received response: " + response)                   
			}
	 }
	
}








