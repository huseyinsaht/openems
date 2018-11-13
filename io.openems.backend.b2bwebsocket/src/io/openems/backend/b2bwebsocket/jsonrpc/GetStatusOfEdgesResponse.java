package io.openems.backend.b2bwebsocket.jsonrpc;

import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.openems.common.jsonrpc.base.JsonrpcResponseSuccess;
import io.openems.common.utils.JsonUtils;

/**
 * Represents a JSON-RPC Request for 'getStatusOfEdges'.
 * 
 * <pre>
 * {
 *   "jsonrpc": "2.0",
 *   "id": "UUID",
 *   "result": {
 *     "edge0": {
 *       "online": true
 *     },
 *     "edge1": {
 *       "online": false
 *     }
 *   }
 * }
 * </pre>
 */
public class GetStatusOfEdgesResponse extends JsonrpcResponseSuccess {

	public static class EdgeInfo {
		protected final boolean online;

		public EdgeInfo(boolean online) {
			this.online = online;
		}
	}

	private final Map<String, EdgeInfo> edgeInfos;

	public GetStatusOfEdgesResponse(Map<String, EdgeInfo> edgeInfos) {
		this(UUID.randomUUID(), edgeInfos);
	}

	public GetStatusOfEdgesResponse(UUID id, Map<String, EdgeInfo> edgeInfos) {
		super(id);
		this.edgeInfos = edgeInfos;
	}

	@Override
	public JsonElement getResult() {
		JsonObject j = new JsonObject();
		for (Entry<String, EdgeInfo> entry : this.edgeInfos.entrySet()) {
			EdgeInfo edge = entry.getValue();
			j.add(entry.getKey(), JsonUtils.buildJsonObject() //
					.addProperty("online", edge.online) //
					.build());
		}
		return j;
	}

}
