public class SortArray {
    private int[] array;
    private int nElems;

    public SortArray(int max){ // creates object with given size of an array
        array = new int[max]; // Создание массива
        nElems = 0; // Пока нет ни одного элемента
    }

    public SortArray(int[] array){ //creates object with existing array
        this.array = array;
        nElems = array.length;
    }
    //--------------------------------------------------------------
    public void insert(int value){ // inserts elements
        array[nElems] = value; // Собственно вставка
        nElems++; // Увеличение размера
    }
    //--------------------------------------------------------------
    public void display(){ // outputs elements
        for(int j=0; j<nElems; j++) // Для каждого элемента
            System.out.print(array[j] + " "); // Вывод
        System.out.println("");
    }
    //--------------------------------------------------------------
    public void bubbleSort(){ //implements bubble sort
        int out, in;
        for(out=nElems-1; out>1; out--) // Внешний цикл (обратный)
            for(in=0; in<out; in++) // Внутренний цикл (прямой)
                if( array[in] > array[in+1] ) // Порядок нарушен?
                    swap(in, in+1); // Поменять местами
    }

    //Chapter 3, Application Project 3.1
    public void bubbleSortPlus(){ //improved bubble sort sorts in two directions for one outer iteration
        int lout, rout, in;
        for(lout=nElems-1, rout=0; lout>1; lout--, rout++) { // Внешний цикл (обратный)
            for (in = 0; in < lout; in++) // Внутренний цикл (прямой)
                if (array[in] > array[in + 1]) // Порядок нарушен?
                    swap(in, in + 1); // Поменять местами
            for(in = lout - 1; in > rout; in--) // Inner cycle (reversed)
                if (array[in] < array[in-1])  //Is order wrong?
                    swap(in,in-1); //Smaller moves to the left
        }
    }

    //Chapter 3, Application Project 3.2
    public int median(){ //returns median of the array
        if(array.length % 2 == 0)
            return array[nElems/2];
        else
            return (array[nElems/2] + array[nElems/2+1])/2;
    }

    //Chapter 3, Application Project 3.3
    public void noDups(){ //removes all of duplicates leaving only one unique value from the range
        int i = 0;
        for (int j = 1; j < nElems; j++)
            if(array[i] != array[j])
                array[++i] = array[j];
        nElems = i + 1;
    }

    //Chapter 3, Application Project 3.4
    public void oddEvenSort(){ //implements odd-even sort
        for (int i = 0; i < nElems; i++) {
            for (int j = i%2+1; j < nElems; j+=2)
                if(array[j-1] > array[j])
                    swap(j-1,j);
        }
    }

    public void selectionSort(){ //implements selection sort
        int out, in, min;
        for(out=0; out<nElems-1; out++){
            min = out;
            for(in=out+1; in<nElems; in++)
                if(array[in] < array[min] )
                    min = in;
            if (min != out)
                swap(out, min);
        }
    }

    public void insertionSort(){ //implements insertion sort
        int in, out;
        for(out=1; out<nElems; out++) // out - разделительный маркер
        {
            int temp = array[out]; // Скопировать помеченный элемент
            in = out; // Начать перемещения с out
            while(in>0 && array[in-1] >= temp) // Пока не найден меньший элемент
            {
                array[in] = array[in-1]; // Сдвинуть элемент вправо
                --in; // Перейти на одну позицию влево
            }
            array[in] = temp; // Вставить помеченный элемент
        }
    }

    // NOTE: needs to check whether indices equal or not inside selection sort
    // because Java working only with references
    // so it can cause unexpected values
    private void swap(int first, int second){ //swaps two elements of an array without using temporary variable
        array[first] += array[second];
        array[second] = array[first] - array[second];
        array[first] -= array[second];
    }
}
