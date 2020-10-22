package marker;

public class MarkerContainer {
    private byte[] container;
    private byte[] bitMessage;
    private byte[] message;
    private int containerCapacity;
    private int messageLength;
    private boolean containerFilled;

    public MarkerContainer(byte[] mess, byte[] cont) {
        container = cont;
        int j = 0;
        message = mess;
        bitMessage = convertMess(mess);
        containerFilled = false;
        messageLength = bitMessage.length;
        containerCapacity = capacityCount(container);
    }

    public int getContainerCapacity() {
        return containerCapacity;
    }

    public int getMessageLength() {
        return messageLength;
    }

    public void fillContainer() {
        if (messageLength > containerCapacity) {
            throw new MarkerException();
        }
        int counter = 0;
        char n = '\n';
        char r = '\r';
        for (int i = 0; i < container.length; i++) {
            if ((char)container[i] == n && (char)container[i + 1] == r || (char)container[i] == r && (char)container[i + 1] == n ) {
                if (counter < bitMessage.length) {
                    if (bitMessage[counter++] == 1) {
                        container[i] = (byte) n;
                        container[i + 1] = (byte) r;
                    } else {
                        container[i] = (byte) r;
                        container[i + 1] = (byte) n;
                    }
                }
            }
        }
        containerFilled = true;
    }

    public byte[] getContainer() {
        return container;
    }

    public byte[] pullOutContainer() {
        int counter = 0;
        char n = '\n';
        char r = '\r';
        byte[] res = new byte[containerCapacity];
        for (int i = 0; i < container.length; i++) {
            if ((char)container[i] == n && (char)container[i + 1] == r) {
                res[counter++] = (byte)1;
            }
            if ((char)container[i] == r && (char)container[i + 1] == n) {
                res[counter++] = (byte)0;
            }
        }
        res = reconvertMess(res);
        containerFilled = true;
        return res;
    }

    private static int capacityCount(byte[] container) {
        int res = 0;
        for (int i = 0; i < container.length - 1; i++) {
            char n = '\n';
            char r = '\r';
            if ((char)container[i] == n && (char)container[i + 1] == r || (char)container[i] == r && (char)container[i + 1] == n ) {
                res++;
            }
        }
        return res;
    }

    private static byte[] convertMess(byte[] mess) {
        byte[] res = new byte[mess.length * 8];
        for (int i = 0; i < mess.length; i++) {
            byte[] tmp = byteDestroy(mess[i]);
            for (int j = 0; j < 8; j++) {
                res[i * 8 + j] = tmp[j];
            }
        }
        return res;
    }

    private static byte[] reconvertMess(byte[] mess) {
        byte[] res = new byte[mess.length / 8];
        for (int j = 0; j < res.length; j++) {
            byte[] tmp = new byte[8];
            for (int i = 0; i < 8; i++) {
                    tmp[i] = mess[j * 8 + i];
            }
            res[j] = byteBuild(tmp);
        }
        return res;
    }

    private static byte[] byteDestroy(byte mes) {
        byte[] res = new byte[8];
        byte tmp;
        for (int i = 0; i < 8; i++) {
            if ((mes & (byte)Math.pow(2, i)) == 0) {
                tmp = 0;
            } else {
                tmp = 1;
            }
            res[i] = tmp;
        }
        return res;
    }
    
    private static byte byteBuild(byte[] mes) {
        byte res = 0;
        for (int i = 7; i >= 0; i--) {
            res = (byte) (res + Math.pow(2, i) * mes[i]);
        }
        return res;
    }

    public void setMessage(byte[] message) {
        this.message = message;
        this.bitMessage = convertMess(message);
        messageLength = bitMessage.length;
    }
}
