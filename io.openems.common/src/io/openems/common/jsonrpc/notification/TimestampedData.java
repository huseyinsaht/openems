package io.openems.common.jsonrpc.notification;

import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.openems.common.exceptions.OpenemsException;
import io.openems.common.jsonrpc.base.JsonrpcNotification;
import io.openems.common.types.ChannelAddress;
import io.openems.common.utils.JsonUtils;

/**
 * Represents a JSON-RPC Notification for timestamped data sent from Edge to
 * Backend.
 * 
 * <pre>
 * {
 *   "jsonrpc": "2.0",
 *   "method": "timestampedData",
 *   "params": {
 *     [timestamp: epoch in seconds]: {
 *       [channelAddress]: String | Number
 *     }
 *   }
 * }
 * </pre>
 * 
 * @param timestamp
 * @param jData
 * @return
 */
public class TimestampedData extends JsonrpcNotification {

	public static TimestampedData from(JsonrpcNotification notification) throws OpenemsException {
		TimestampedData result = new TimestampedData();
		JsonObject j = notification.getParams();
		for (Entry<String, JsonElement> e1 : j.entrySet()) {
			long timestamp = Long.parseLong(e1.getKey());
			JsonObject jTime = JsonUtils.getAsJsonObject(e1.getValue());
			for (Entry<String, JsonElement> e2 : jTime.entrySet()) {
				result.add(timestamp, ChannelAddress.fromString(e2.getKey()), e2.getValue());
			}
		}
		return result;
	}

	public final static String METHOD = "timestampedData";

	private final Table<Long, ChannelAddress, JsonElement> data = HashBasedTable.create();

	public TimestampedData() {
		super(METHOD);
	}

	public TimestampedData(long timestamp, Map<ChannelAddress, JsonElement> values) {
		super(METHOD);
		this.add(timestamp, values);
	}

	public void add(long timestamp, Map<ChannelAddress, JsonElement> data) {
		for (Entry<ChannelAddress, JsonElement> entry : data.entrySet()) {
			this.add(timestamp, entry.getKey(), entry.getValue());
		}
	}

	public void add(long timestamp, ChannelAddress address, JsonElement value) {
		this.data.put(timestamp, address, value);
	}

	@Override
	public JsonObject getParams() {
		JsonObject j = new JsonObject();
		for (Entry<Long, Map<ChannelAddress, JsonElement>> e1 : this.data.rowMap().entrySet()) {
			JsonObject jTime = new JsonObject();
			for (Entry<ChannelAddress, JsonElement> e2 : e1.getValue().entrySet()) {
				ChannelAddress address = e2.getKey();
				JsonElement value = e2.getValue();
				jTime.add(address.toString(), value);
			}
			j.add(e1.getKey().toString(), jTime);
		}
		return j;
	}

	public Table<Long, ChannelAddress, JsonElement> getData() {
		return data;
	}
}