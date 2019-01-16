package io.openems.backend.b2bwebsocket;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;

import io.openems.backend.b2bwebsocket.B2bWebsocket;
import io.openems.common.exceptions.OpenemsException;
import io.openems.common.jsonrpc.request.GetChannelsValuesRequest;
import io.openems.common.jsonrpc.request.GetStatusOfEdgesRequest;
import io.openems.common.jsonrpc.request.SetGridConnScheduleRequest;
import io.openems.common.jsonrpc.request.SetGridConnScheduleRequest.GridConnSchedule;

public class B2bWebsocketTest {

	private static TestClient preparteTestClient() throws URISyntaxException, InterruptedException {
		TestClient client = new TestClient(new URI("ws://fems-test.beegy-dev.cc:10002"));
		client.startBlocking();
		return client;
	}

	@Test
	public void testGetStatusOfEdgesRequest() throws URISyntaxException, InterruptedException, OpenemsException {
		TestClient client = preparteTestClient();

		GetStatusOfEdgesRequest request = new GetStatusOfEdgesRequest();
		client.sendRequest(request, response -> {
			System.out.println(response);
		});

		while (true) {
			Thread.sleep(5000);
		}
	}
     
	@Test
	public void testGetChannelsValuesRequest() throws URISyntaxException, InterruptedException, OpenemsException {
		TestClient client = preparteTestClient();

		GetChannelsValuesRequest request = new GetChannelsValuesRequest();
		client.sendRequest(request, response -> {
			System.out.println(response);
		});

		while (true) {
			Thread.sleep(5000);
		}
	}
	
	
	@Test
	public void testSetGridConnSchedule() throws URISyntaxException, InterruptedException, OpenemsException {
		TestClient client = preparteTestClient();

		SetGridConnScheduleRequest request = new SetGridConnScheduleRequest("edge1");
		request.addScheduleEntry(new GridConnSchedule(System.currentTimeMillis() / 1000, 40, 8000));
		System.out.println("Sending Request " + request);
		client.sendRequest(request, response -> {
			System.out.println("Response: " + response);
		});

		while (true) {
			Thread.sleep(5000);
		}
	}
}
