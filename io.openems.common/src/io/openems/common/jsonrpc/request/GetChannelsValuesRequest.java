package io.openems.common.jsonrpc.request;

import java.util.UUID;

import com.google.gson.JsonObject;

import io.openems.common.exceptions.OpenemsException;
import io.openems.common.jsonrpc.base.JsonrpcRequest;



public class GetChannelsValuesRequest extends JsonrpcRequest {



	public static GetChannelsValues from(JsonrpcRequest r) throws OpenemsException {
		return new GetChannelsValues(r.getId());
	}

	public final static String METHOD = "GetChannelsValues";

	public GetChannelsValues() {
		this(UUID.randomUUID());
	}

	public GetChannelsValues(UUID id) {
		super(id, METHOD);
	}

	@Override
	public JsonObject getParams() {
		return new JsonObject();
	}

}
