# Collections에서 sort는 어떻게 이뤄질까
새로운 클래스를 만들어 정렬할 때, ```Collections``` 라이브러리의 정적 메서드 ```sort```
를 사용한다. ```sort```메서드가 상속받은 ```Comparable```을 바탕으로 정렬된다는 사실은 알지만
내부적으로 어떻게 동작하고, 어떤 알고리즘이 돌아가는지 궁금해하지 않는 이상 알 수 없다.

나는 궁금하기 때문에 한 번 라이브러리를 뜯어보겠다.

### Collections.sort
```java
public static <T extends Comparable<? super T>> void sort(List<T> list) {
    list.sort(null);
}
```
> 파라미터의 T는 꼭 Comparable을 오버라이딩 한 상태여야한다.
> 그리고 모든 요소는 상호 비교가능하게끔 연결되야만 한다.

이 메서드는 ```list.sort```메서드를 호출한다

### list.sort()
```java
default void sort(Comparator<? super E> c) {
    Object[] a = this.toArray();
    Arrays.sort(a, (Comparator) c); // 파라미터 c는 null
    ListIterator<E> i = this.listIterator();
    for (Object e : a) {
        i.next();
        i.set((E) e);
    }
}
```
1. 먼저 매개변수로 받은 ```List``` 객체를 배열로 바꿔준다.
2. ```Arrays```의 ```sort```를 호출한다. 

### Arrays.sort()
```java
public static <T> void sort(T[] a, Comparator<? super T> c) {
    if (c == null) {
        sort(a);
    } else {
        if (LegacyMergeSort.userRequested)
            legacyMergeSort(a, c);
        else
            TimSort.sort(a, 0, a.length, c, null, 0, 0);
    }
}
```
>* 설명
> This implementation is a stable, adaptive, iterative mergesort that requires far fewer than n lg(n) comparisons when the input array is partially sorted,
> > ```sort```메서드의 수행은 , 부분적으로 정렬되어 있으면 n log(n) 시간보다 훨씬 적은 시간이 소비된다. (merge sort)
> The input array is nearly sorted, the implementation requires approximately n comparisons
> > 거의 정렬되어 있으면 n 시간 소비된다.

```Collections```의 ```sort```에서 ```c```를 ```null```로 선언했기 때문에 ```sort(a)``` 메서드를 호출한다.

### sort(a)
```java
public static void sort(Object[] a) {
    if (LegacyMergeSort.userRequested)
        legacyMergeSort(a);
    else
        ComparableTimSort.sort(a, 0, a.length, null, 0, 0);
}
```
// LegacyMergeSort의 설명을 보니, 순환참조 문제로 ```static boolean```이 안돼 추후 release에는 사라진다고한다.
// ```ComparableTimSort```기준으로 확인해보자

### ComparableTimSort.sort()
> 코드가 길기 때문에 주석으로 설명하겠다.
```java
static <T> void sort(T[] a, int lo, int hi, Comparator<? super T> c,
                     T[] work, int workBase, int workLen) {
    // a가 null이 아닌지, lo가 0이상인지, hi가 lo보다 큰지, hi가 정렬하고자 하는 배열의 길이보다 작거나 같은지
    assert c != null && a != null && lo >= 0 && lo <= hi && hi <= a.length;

    int nRemaining  = hi - lo;
    if (nRemaining < 2)
        return;  // 배열의 크기가 0이나 1일 경우 항상 정렬됨

    // If array is small, do a "mini-TimSort" with no merges
    if (nRemaining < MIN_MERGE) { // MIN_MERGE = 32
        // 이진 정렬을 위한 작동 구간을 탐색한다. (피봇값) 
        int initRunLen = countRunAndMakeAscending(a, lo, hi, c);
        binarySort(a, lo, hi, lo + initRunLen, c);
        return;
    }

    /**
     * March over the array once, left to right, finding natural runs,
     * extending short natural runs to minRun elements, and merging runs
     * to maintain stack invariant.
     */
    TimSort<T> ts = new TimSort<>(a, c, work, workBase, workLen);
    int minRun = minRunLength(nRemaining);
    do {
        // Identify next run
        int runLen = countRunAndMakeAscending(a, lo, hi, c);

        // If run is short, extend to min(minRun, nRemaining)
        if (runLen < minRun) {
            int force = nRemaining <= minRun ? nRemaining : minRun;
            binarySort(a, lo, lo + force, lo + runLen, c);
            runLen = force;
        }

        // Push run onto pending-run stack, and maybe merge
        ts.pushRun(lo, runLen);
        ts.mergeCollapse();

        // Advance to find next run
        lo += runLen;
        nRemaining -= runLen;
    } while (nRemaining != 0);

    // Merge all remaining runs to complete sort
    assert lo == hi;
    ts.mergeForceCollapse();
    assert ts.stackSize == 1;
}
```
> 결론은 descending인지 ascending인지 파악하여, 이진 삽입 정렬을 실시한다.

### list.sort()
```java
default void sort(Comparator<? super E> c) {
    Object[] a = this.toArray();
    Arrays.sort(a, (Comparator) c); // 파라미터 c는 null
    ListIterator<E> i = this.listIterator();
    for (Object e : a) {
        i.next();
        i.set((E) e);
    }
}
```
다시 이 메서드로 돌아와 정렬한 배열을 반복문을 돌려 iterator.set으로 값을 세팅해준다. 

이렇게 ```Collections.sort```는 마무리된다.
