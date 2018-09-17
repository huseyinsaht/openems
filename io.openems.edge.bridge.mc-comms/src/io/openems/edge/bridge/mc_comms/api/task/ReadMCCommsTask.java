package io.openems.edge.bridge.mc_comms.api.task;

import io.openems.edge.bridge.mc_comms.MCCommsBridge;
import io.openems.edge.bridge.mc_comms.api.element.MCCommsElement;
import io.openems.edge.bridge.mc_comms.util.MCCommsException;
import io.openems.edge.bridge.mc_comms.util.MCCommsPacket;
import io.openems.edge.common.taskmanager.ManagedTask;
import io.openems.edge.common.taskmanager.Priority;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ReadMCCommsTask extends AbstractMCCommsTask implements ManagedTask {

    private final int expectedReplyCommand;

    public ReadMCCommsTask(MCCommsBridge bridge, int slaveAddress, int readCommand, int expectedReplyCommand, Priority priority, MCCommsElement<?>... elements) {
        super(bridge, slaveAddress, readCommand, priority, elements);
        this.expectedReplyCommand = expectedReplyCommand;
    }

    /**
     * Sends a query for this AbstractMCCommsTask to the MCComms device
     *
     * @throws MCCommsException
     */
    @Override
    public void executeQuery() throws MCCommsException {
        bridge.IOPacketBuffer.TXPacketQueue.add(new MCCommsPacket(0, this.bridge.getMasterAddress(), this.slaveAddress));
        MCCommsPacket commandReplyPacket = transferQueue.poll();
        if (commandReplyPacket != null && commandReplyPacket.getCommand() == this.expectedReplyCommand) {
            //retrieve payload
            byte[] payload = commandReplyPacket.getPayload();
            //assign payload bytes to elements
            for (MCCommsElement<?> element : this.elements) {
                int byteAddress, numBytes;
                byteAddress = element.getByteAddress();
                numBytes = element.getNumBytes();
                ByteBuffer elementBuffer = ByteBuffer.allocate(numBytes);
                for (int i = byteAddress; i < (byteAddress + numBytes); i++) {
                    elementBuffer.order(ByteOrder.BIG_ENDIAN).put(payload[i]);
                }
                element._setRawValue(elementBuffer);
            }

        } else {
            throw new MCCommsException("[MCCOMMS] Unexpected command! Expecting command code [" + this.expectedReplyCommand + "] but got command [" + commandReplyPacket.getCommand());
        }
    }
}
