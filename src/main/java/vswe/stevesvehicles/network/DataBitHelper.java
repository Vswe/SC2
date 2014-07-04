package vswe.stevesvehicles.network;


public enum DataBitHelper implements IBitCount {
    BOOLEAN(1),
    BYTE(8),
    SHORT(16),
    INTEGER(32),


    NBT_LENGTH(15),
    DEFAULT_STRING(31);
    private int bitCount;

    DataBitHelper(int bitCount) {
        this.bitCount = bitCount;
    }

    @Override
    public int getBitCount() {
        return bitCount;
    }
}
