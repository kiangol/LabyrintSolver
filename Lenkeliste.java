import java.util.Iterator;


public class Lenkeliste<T> implements Liste<T> {
    protected int antall;
    protected Node forste;
    protected Node siste;
    protected Node forrigeNode;
    protected Node nesteNode;


    protected class Node {
        protected Node neste = null;
        protected Node forrige = null;
        protected T data;

        public Node(T x) {data = x; }
    }

    @Override
    public Iterator<T> iterator() {
        return new LenkelisteIterator();
    }

    @Override
    public int stoerrelse() {
        return antall;
    }

    @Override
    public void leggTil(T x) {
        Node ny = new Node(x);
        antall++;
        if(forste == null) {
            forste = ny;
            siste = forste;
        } else {
            siste.neste = ny;
            ny.forrige = siste;
            siste = ny;
        }
    }

    @Override
    public void leggTil(int pos, T x) throws UgyldigListeIndeks {
        Node ny = new Node(x);
        int teller = 0;
        Node current = forste;
        if ((pos <= antall && pos >= 0)||(pos == 0 && forste == null)) {
            antall++;
            if(forste == null && pos == 0) { //Tom liste
                forste = ny;
                siste = forste;
            } else if(pos == 0) { //Ikke tom liste, legges inn på første indeks
                forste = ny;
                ny.neste = current;
                current.forrige = ny;
            } else if (pos+1 == antall) { //Ikke tom liste, legges inn på siste indeks
                siste.neste = ny;
                ny.forrige = siste;
                siste = ny;
            } else { //Ikke tom liste, legges inn i mellom to andre noder i listen
                while(teller < antall) {
                    if(teller == pos) {
                        forrigeNode = current.forrige;
                        nesteNode = current;
                        forrigeNode.neste = ny;
                        nesteNode.forrige = ny;
                        ny.forrige = forrigeNode;
                        ny.neste = nesteNode;
                        break;
                    }
                    teller++;
                    current = current.neste;
                }
            }
        }
        else {
            throw new UgyldigListeIndeks(pos);
        }
    }

    @Override
    public void sett(int pos, T x) throws UgyldigListeIndeks {
        Node ny = new Node(x);
        Node current = forste;
        int teller = 0;
        if(pos+1 <= antall && pos >= 0 && forste != null) {
            if(pos == 0) { //erstatter første indeks
                current = forste.neste;
                forste = ny;
                current.forrige = forste;
                forste.neste = current;
            } else if (pos+1 == antall) { //erstatter siste indeks
                current = siste.forrige;
                current.neste = ny;
                ny.forrige = current;
                siste = ny;
            } else {
                while(teller < antall) {
                    if(teller == pos) {
                        forrigeNode = current.forrige;
                        nesteNode = current.neste;
                        forrigeNode.neste = ny;
                        nesteNode.forrige = ny;
                        ny.forrige = forrigeNode;
                        ny.neste = nesteNode;
                        break;
                    }
                    teller++;
                    current = current.neste;
                }
            }
        }
        else {
            throw new UgyldigListeIndeks(pos);
        }
    }

    @Override
    public T hent(int pos) throws UgyldigListeIndeks {
        int teller = 0;
        Node current = forste;
        if(pos+1 <= antall && pos >= 0) {
            while(teller < antall) {
                if(teller == pos) {
                    return current.data;
                }
                teller++;
                current = current.neste;
            }
        }
        else {
            throw new UgyldigListeIndeks(pos);
        }
        return null;
    }

    @Override //Fjerner fra gitt indeks og returnerer det fjernede elementet
    public T fjern(int pos) throws UgyldigListeIndeks {
        int teller = 0;
        Node current = forste;
        if(pos+1 <= antall && pos >= 0) {
            if(pos == 0 && forste.neste != null) { //lenke med 2 elementer
                forste = forste.neste;
                forste.forrige = null;
                antall--;
                return current.data;
            }
            else if(pos+1 == antall && antall == 1) {  //lenke med ett element
                forste = null;
                siste = null;
                antall--;
                return current.data;
            }
            else if(pos+1 == antall) {  //fjerne siste element
                current = siste;
                siste = siste.forrige;
                siste.neste = null;
                antall--;
                return current.data;
            }
            else {                      //fjerne midt i listen
                while(teller < antall) {
                    if(teller == pos) {
                        forrigeNode = current.forrige;
                        nesteNode = current.neste;
                        forrigeNode.neste = nesteNode;
                        nesteNode.forrige = forrigeNode;
                        antall--;
                        return current.data;
                    }
                    teller++;
                    current = current.neste;
                }
            }
        }
        else {
            throw new UgyldigListeIndeks(pos);
        }
        return null;
    }


    @Override //Fjerner og returnerer elementet på starten av listen
    public T fjern() throws UgyldigListeIndeks {
        Node current = forste;
        if(antall == 0) {
            throw new UgyldigListeIndeks(-1);
        } else if (antall == 1) {
            forste = null;
            siste = null;
            antall--;
            return current.data;
        } else {
            forste = forste.neste;
            forste.forrige = null;
            antall--;
            return current.data;
        }
    }

    public class LenkelisteIterator implements Iterator<T> {
        private Node current = forste;

        @Override
        public boolean hasNext() {
            return(current != null);
        }

        @Override
        public T next() {
            T data = current.data;
            current = current.neste;
            return data;

        }
    }


}
