package nl.cloud.location.adapter;

import io.vertx.core.AbstractVerticle

class HttpVerticle : AbstractVerticle() {

	// Called when verticle is deployed
	override fun start() {
		println("Started HTTP server!")
	}

	// Optional - called when verticle is undeployed
	override fun stop() {
	}
}
