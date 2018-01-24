public class HighArray {
    private long[] a;
    private int nElems;

    public HighArray(int max) {
        a = new long[max];
        nElems = 0;
    }

    public HighArray(long[] arr) {
        a = arr;
        nElems = arr.length;
    }

    public boolean find(long searchKey) {
        int i = 0;
        for (i = 0; i < nElems; i++)
            if (a[i] == searchKey)
                break;
        if (i == nElems)
            return false;
        else
            return true;
    }

    public void insert(long value) {
        a[nElems] = value;
        nElems++;
    }

    public boolean delete(long value) {
        int j;
        for (j = 0; j < nElems; j++)
            if (value == a[j])
                break;
        if (j == nElems)
            return false;
        else {
            for (int i = j; i < nElems - 1; i++)
                a[i] = a[i + 1];
            nElems--;
            return true;
        }
    }

    public long getMax() {
        long max = -1;
        for (int i = 0; i < nElems; i++)
            if (a[i] > max)
                max = a[i];
        return max;
    }

    public int getElems() {
        return nElems;
    }

    public long removeMax() {
        long max = -1;
        for (int i = 0; i < nElems; i++)
            if (a[i] > max)
                max = a[i];
        delete(max);
        return max;
    }

    public void noDups() {
        for (int i = 0; i < nElems; i++){
            for (int j = i + 1; j < nElems; j++){
                if (a[i] == a[j]) {
//                    delete(a[j]);
                    a[j] = -1L;
//                    j--;
                }
            }
        }
        while(delete(-1L)){}
    }

    public void display(){
        for (int i = 0; i < nElems; i++)
            System.out.print(a[i]+" ");
        System.out.println();
    }
}
