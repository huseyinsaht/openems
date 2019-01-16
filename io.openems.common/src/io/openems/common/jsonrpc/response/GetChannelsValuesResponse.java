package io.openems.common.jsonrpc.response;

import java.util.Map;
import java.util.UUID;

public class GetChannelsValuesResponse {
	
	
	
	
	
		public static class EdgeInfo {
			protected final boolean online;

			public EdgeInfo(boolean online) {
				this.online = online;
			}
		}

		private final Map<String, EdgeInfo> edgeInfos;

		public  GetChannelsValuesResponse (Map<String, EdgeInfo> edgeInfos) {
			this(UUID.randomUUID(), edgeInfos);
		}

		public  GetChannelsValuesResponse(UUID id, Map<String, EdgeInfo> edgeInfos) {
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



