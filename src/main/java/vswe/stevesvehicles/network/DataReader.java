package vswe.stevesvehicles.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

import java.io.IOException;


public class DataReader {

    private ByteBuf stream;
    private int byteBuffer;
    private int bitCountBuffer;

    DataReader(ByteBuf buf) {
        stream = buf;
    }

    public int readData(IBitCount count) {
        return readData(count.getBitCount());
    }

    public int readData(int bitCount) {
        int data = 0;
        int readBits = 0;

        while (true) {
            int bitsLeft = bitCount - readBits;
            if (bitCountBuffer >= bitsLeft) {
                data |= (byteBuffer & ((int)Math.pow(2, bitsLeft) - 1)) << readBits;
                byteBuffer >>>= bitsLeft;
                bitCountBuffer -= bitsLeft;
                readBits += bitsLeft;
                break;
            }else{
                data |= byteBuffer << readBits;
                readBits += bitCountBuffer;

                byteBuffer = stream.readUnsignedByte();
                bitCountBuffer = 8;
            }
        }



        return data;
    }

    public int readSignedData(IBitCount count) {
        return readSignedData(count.getBitCount());
    }


    public int readSignedData(int bitCount) {
        int data = readData(bitCount);
        int threshold = (int)Math.pow(2, bitCount - 1);
        int max = (int)Math.pow(2, bitCount) - 1;

        if (data >= threshold) {
            data = max - data;
        }

        return data;
    }

    public String readString() {
        return readString(DataBitHelper.DEFAULT_STRING);
    }

    public String readString(IBitCount bits) {
        int length = readData(bits);
        if (length == 0) {
            return null;
        }else{
            byte[] bytes = new byte[length];
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = (byte)readByte();
            }
            return new String(bytes);
        }
    }

    public NBTTagCompound readNBT(){
        if (readBoolean()) {
            byte[] bytes = new byte[readData(DataBitHelper.NBT_LENGTH)];
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = (byte)readByte();
            }

            try {
                return CompressedStreamTools.decompress(bytes);
            }catch (IOException ex) {
                return null;
            }
        }else{
            return null;
        }
    }


    /**
     * Easy access methods
     */

    public boolean readBoolean() {
        return readData(DataBitHelper.BOOLEAN) != 0;
    }

    public int readByte() {
        return readData(DataBitHelper.BYTE);
    }

    public int readShort() {
        return readData(DataBitHelper.SHORT);
    }

    public int readInteger() {
        int data = readData(DataBitHelper.INTEGER);
        return data < 0 ? 0 : data;
    }

    public int readSignedByte() {
        return readSignedData(DataBitHelper.BYTE);
    }

    public int readSignedShort() {
        return readSignedData(DataBitHelper.SHORT);
    }

    public int readSignedInteger() {
        return readData(DataBitHelper.INTEGER); //will automatically be signed due to the return value being an integer
    }

    public <T extends Enum> T readEnum(Class<T> clazz) {
        try {
            Object[] values = (Object[] )clazz.getMethod("values").invoke(null);
            int length = values.length;
            if (length == 0) {
                return null;
            }
            int bitCount = (int)(Math.log10(length) / Math.log10(2)) + 1;

            int val = readData(bitCount);
            return (T)values[val];
        }catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }
}