public class OrdArray {
    private int[] a; // Ссылка на массив a
    private int nElems; // Количество элементов данных
    //-----------------------------------------------------------
    public OrdArray(int max) // Конструктор
    {
        a = new int[max]; // Создание массива
        nElems = 0;
    }
    public int size()
    { return nElems; }
    //-----------------------------------------------------------
    public int find(int searchKey)
    {
        int lowerBound = 0;
        int upperBound = nElems-1;
        int curIn;
        while(true)
        {
            curIn = (lowerBound + upperBound ) / 2;
            if(a[curIn]==searchKey)
                return curIn; // Элемент найден
            else if(lowerBound > upperBound)
                return nElems; // Элемент не найден
            else // Деление диапазона
            {
                if(a[curIn] < searchKey)
                    lowerBound = curIn + 1; // В верхней половине
                else
                    upperBound = curIn - 1; // В нижней половине
            }
        }
    }
    //-----------------------------------------------------------
    public void insert(int value) // Вставка элемента в массив
    {
        int j;
        int lowerBound = 0;
        int upperBound = nElems-1;
        int curIn;
        while(true)
        {
            curIn = (lowerBound + upperBound ) / 2;
            if(a[curIn]==value){
                j = curIn + 1;
                break;
            }
            else if(lowerBound > upperBound){
                j = lowerBound;
                break;
            }
            else // Деление диапазона
            {
                if(a[curIn] < value)
                    lowerBound = curIn + 1; // В верхней половине
                else
                    upperBound = curIn - 1; // В нижней половине
            }
        }
        for(int k=nElems; k>j; k--) // Перемещение последующих элементов
            a[k] = a[k-1];
        a[j] = value; // Вставка
        nElems++; // Увеличение размера
    }
    //-----------------------------------------------------------
    public boolean delete(int value)
    {
        int j = find(value);
        if(j==nElems) // Найти не удалось
            return false;
        else // Элемент найден
        {
            for(int k=j; k<nElems; k++) // Перемещение последующих элементов
                a[k] = a[k+1];
            nElems--;
            return true;
        }
    }

    static int[] merge(int[] first, int[] second){
        int[] res = new int[first.length + second.length];
        int f, s;
        f = s = 0;
        for(int r = 0; r < res.length; r++){
                if(f < first.length && s < second.length){
                    if(first[f] <= second[s]){
                        res[r] = first[f];
                        f++;
                    }
                    else{ //first[f] > second[s]
                        res[r] = second[s];
                        s++;
                    }
                }
                else if(f >= first.length && s < second.length){
                    res[r] = second[s];
                    s++;
                }
                else if(s >= second.length && f < first.length){
                    res[r] = first[f];
                    f++;
                }
        }
        return res;
    }


    public int[] getArr() {
        return a;
    }

    public int getElems() {
        return nElems;
    }

    //-----------------------------------------------------------
    public void display() // Вывод содержимого массива
    {
        for(int j=0; j<nElems; j++) // Перебор всех элементов
            System.out.print(a[j] + " "); // Вывод текущего элемента
        System.out.println("");
    }
}
