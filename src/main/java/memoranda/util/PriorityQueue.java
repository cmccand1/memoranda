package memoranda.util;

import java.util.Arrays;
import nu.xom.Element;

public class PriorityQueue {

  private Pair[] pq;
  private int size;

  public PriorityQueue(int size) {
    pq = new Pair[size + 2];
    this.size = 0;
  }

  public void insert(Pair x) {
    pq[++size] = x;
    swim(size);
  }

  private void swim(int k) {
    while (k > 1 && less(k/2, k)) {
      swap(k/2, k);
      k = k/2;
    }
  }

  private void sink(int k) {
    while (2 * k <= size) {
      int j = 2 * k;
      if (j < size && less(j, j + 1)) {
        j++;
      }
      if (!less(k, j)) {
        break;
      }
      swap(k, j);
      k = j;
    }
  }

  public Element removeMax() {
    if (!isEmpty()) {
      Element m = pq[1].getElement();
      swap(1, size--);
      pq[size + 1] = null;
      sink(1);
      return m;
    } else {
      return null;
    }
  }

  private void swap(int j, int k) {
    Pair temp = pq[j];
    pq[j] = pq[k];
    pq[k] = temp;
  }

  private boolean less(int i, int j) {
    return pq[i].getPriority() < pq[j].getPriority();
  }

  public int size() {
    return size;
  }

  public boolean isEmpty() {
    return size == 0;
  }

  @Override
  public String toString() {
    return "PriorityQueue{" +
        "pq=" + Arrays.toString(pq) +
        ", size=" + size +
        '}';
  }
}