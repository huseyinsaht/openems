package io.openems.common.jsonrpc;

import java.util.UUID;

import com.google.gson.JsonObject;

import io.openems.common.exceptions.OpenemsException;
import io.openems.common.jsonrpc.base.GenericJsonrpcRequest;
import io.openems.common.jsonrpc.base.JsonrpcRequest;
import io.openems.common.utils.JsonUtils;

public class ComponentJsonApiRequest extends JsonrpcRequest {

	public static ComponentJsonApiRequest from(JsonrpcRequest r) throws OpenemsException {
		JsonObject p = r.getParams();
		String componentId = JsonUtils.getAsString(p, "componentId");
		JsonrpcRequest payload = GenericJsonrpcRequest.from(JsonUtils.getAsJsonObject(p, "payload"));
		return new ComponentJsonApiRequest(r.getId(), componentId, payload);
	}

	public final static String METHOD = "componentJsonApi";

	private final String componentId;
	private final JsonrpcRequest payload;

	public ComponentJsonApiRequest(String componentId, JsonrpcRequest payload) {
		this(UUID.randomUUID(), componentId, payload);
	}

	public ComponentJsonApiRequest(UUID id, String componentId, JsonrpcRequest payload) {
		super(id, METHOD);
		this.componentId = componentId;
		this.payload = payload;
	}

	@Override
	public JsonObject getParams() {
		return JsonUtils.buildJsonObject() //
				.addProperty("componentId", this.componentId) //
				.add("payload", this.payload.toJsonObject()) //
				.build();
	}

	public String getComponentId() {
		return componentId;
	}

	public JsonrpcRequest getPayload() {
		return payload;
	}

}
