// Max Kilzieh
// maki6493

import java.io.Serializable;

public class EncodedMessage<H, M> implements Serializable {
    /**
     * Information som behövs för att avkoda meddelandet, dvs trädet i något
     * lämpligt format.
     */
    public final H header;

    /**
     * Det kodade meddelandet. Även här är formatet relativt fritt. Ett tips är att
     * spara det i ett binärt format som en sträng av '1' och '0'. Det gör det lätt
     * att titta på om det skulle behövas, men skulle enkelt kunna ersättas med en
     * riktig bit-variant.
     */
    public final M message;

    public EncodedMessage(H header, M message) {
        this.header = header;
        this.message = message;
    }
}