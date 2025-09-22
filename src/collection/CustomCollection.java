package collection;

import java.util.*;

public class CustomCollection<T> implements List<T> {

    private Object[] elements;  // Внутренний массив для хранения элементов
    private int size = 0;       // Текущее количество элементов в коллекции

    /**
     * Конструктор по умолчанию - создает коллекцию с начальной емкостью 10
     */
    public CustomCollection() {
        this(10);
    }

    /**
     * Конструктор с заданной начальной емкостью
     *
     * @param initialCapacity начальная емкость внутреннего массива
     * @throws IllegalArgumentException если емкость отрицательная
     */
    public CustomCollection(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
        }
        this.elements = new Object[initialCapacity];
    }

    /**
     * Обеспечивает достаточную емкость внутреннего массива
     * Увеличивает емкость в 2 раза, если требуется больше места
     *
     * @param minCapacity минимальная требуемая емкость
     */
    private void ensureCapacity(int minCapacity) {
        if (minCapacity > elements.length) {
            int newCapacity = Math.max(minCapacity, elements.length * 2);
            elements = Arrays.copyOf(elements, newCapacity);
        }
    }

    /**
     * Добавляет элемент в конец коллекции
     * @param element элемент для добавления
     * @return true (как указано в интерфейсе Collection)
     */
    @Override
    public boolean add(T element) {
        ensureCapacity(size + 1);
        elements[size++] = element;
        return true;
    }

    /**
     * Вставляет элемент по указанному индексу, сдвигая существующие элементы
     * @param index позиция для вставки
     * @param element элемент для вставки
     * @throws IndexOutOfBoundsException если индекс выходит за границы
     */
    @Override
    public void add(int index, T element) {
        rangeCheckForAdd(index);
        ensureCapacity(size + 1);
        System.arraycopy(elements, index, elements, index + 1, size - index);
        elements[index] = element;
        size++;
    }

    /**
     * Возвращает элемент по указанному индексу
     * @param index индекс элемента
     * @return элемент по указанному индексу
     * @throws IndexOutOfBoundsException если индекс выходит за границы
     */
    @SuppressWarnings("unchecked")
    @Override
    public T get(int index) {
        rangeCheck(index);
        return (T) elements[index];
    }

    /**
     * Заменяет элемент по указанному индексу
     * @param index индекс заменяемого элемента
     * @param element новый элемент
     * @return предыдущий элемент по этому индексу
     * @throws IndexOutOfBoundsException если индекс выходит за границы
     */
    @SuppressWarnings("unchecked")
    @Override
    public T set(int index, T element) {
        rangeCheck(index);
        T oldValue = (T) elements[index];
        elements[index] = element;
        return oldValue;
    }

    /**
     * Удаляет элемент по указанному индексу
     * @param index индекс удаляемого элемента
     * @return удаленный элемент
     * @throws IndexOutOfBoundsException если индекс выходит за границы
     */
    @SuppressWarnings("unchecked")
    @Override
    public T remove(int index) {
        rangeCheck(index);
        T oldValue = (T) elements[index];
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(elements, index + 1, elements, index, numMoved);
        }
        elements[--size] = null;
        return oldValue;
    }

    /**
     * Удаляет первое вхождение указанного элемента
     * @param o элемент для удаления
     * @return true если элемент был найден и удален, false если элемент не найден
     */
    @Override
    public boolean remove(Object o) {
        for (int index = 0; index < size; index++) {
            if (Objects.equals(elements[index], o)) {
                remove(index);
                return true;
            }
        }
        return false;
    }

    /**
     * Возвращает количество элементов в коллекции
     * @return количество элементов
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Проверяет, пуста ли коллекция
     * @return true если коллекция пуста, false в противном случае
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Проверяет, содержит ли коллекция указанный элемент
     * @param o элемент для поиска
     * @return true если элемент найден, false в противном случае
     */
    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    /**
     * Возвращает индекс первого вхождения указанного элемента
     * @param o элемент для поиска
     * @return индекс элемента или -1 если элемент не найден
     */
    @Override
    public int indexOf(Object o) {
        if (o == null) {
            for (int i = 0; i < size; i++) {
                if (elements[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (o.equals(elements[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * Возвращает индекс последнего вхождения указанного элемента
     * @param o элемент для поиска
     * @return индекс последнего вхождения или -1 если элемент не найден
     */
    @Override
    public int lastIndexOf(Object o) {
        if (o == null) {
            for (int i = size - 1; i >= 0; i--) {
                if (elements[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = size - 1; i >= 0; i--) {
                if (o.equals(elements[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * Очищает коллекцию, удаляя все элементы
     */
    @Override
    public void clear() {
        Arrays.fill(elements, 0, size, null);
        size = 0;
    }

    /**
     * Возвращает итератор для последовательного обхода элементов коллекции
     * @return итератор коллекции
     */
    @SuppressWarnings("unchecked")
    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            int cursor = 0;

            @Override
            public boolean hasNext() {
                return cursor < size;
            }

            @Override
            public T next() {
                if (cursor >= size) {
                    throw new NoSuchElementException();
                }
                return (T) elements[cursor++];
            }
        };
    }

    /**
     * Возвращает массив, содержащий все элементы коллекции
     * @return массив элементов коллекции
     */
    @Override
    public Object[] toArray() {
        return Arrays.copyOf(elements, size);
    }

    /**
     * Возвращает массив, содержащий все элементы коллекции
     * @param a массив, в который будут помещены элементы (если достаточного размера)
     * @return массив элементов коллекции
     */
    @SuppressWarnings("unchecked")
    @Override
    public <E> E[] toArray(E[] a) {
        if (a.length < size) {
            return (E[]) Arrays.copyOf(elements, size, a.getClass());
        }
        System.arraycopy(elements, 0, a, 0, size);
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    /**
     * Проверяет, содержит ли коллекция все элементы указанной коллекции
     * @param c коллекция для проверки
     * @return true если все элементы содержатся, false в противном случае
     */
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object e : c) {
            if (!contains(e)) return false;
        }
        return true;
    }

    /**
     * Добавляет все элементы указанной коллекции в конец
     * @param c коллекция для добавления
     * @return true если коллекция изменилась
     */
    @Override
    public boolean addAll(Collection<? extends T> c) {
        boolean modified = false;
        for (T e : c) {
            add(e);
            modified = true;
        }
        return modified;
    }

    /**
     * Вставляет все элементы указанной коллекции начиная с указанной позиции
     * @param index позиция для вставки
     * @param c коллекция для добавления
     * @return true если коллекция изменилась
     * @throws IndexOutOfBoundsException если индекс выходит за границы
     */
    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        rangeCheckForAdd(index);
        boolean modified = false;
        for (T e : c) {
            add(index++, e);
            modified = true;
        }
        return modified;
    }

    /**
     * Удаляет все элементы, содержащиеся в указанной коллекции
     * @param c коллекция элементов для удаления
     * @return true если коллекция изменилась
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object e : c) {
            while (remove(e)) {
                modified = true;
            }
        }
        return modified;
    }

    /**
     * Оставляет только элементы, содержащиеся в указанной коллекции
     * @param c коллекция элементов которые нужно оставить
     * @return true если коллекция изменилась
     */
    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Iterator<T> it = iterator();
        while (it.hasNext()) {
            if (!c.contains(it.next())) {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public ListIterator<T> listIterator() {
        throw new UnsupportedOperationException("listIterator not implemented");
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        throw new UnsupportedOperationException("listIterator not implemented");
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException("subList not implemented");
    }

    /**
     * Проверяет корректность индекса для операций доступа (get/set/remove)
     * @param index проверяемый индекс
     * @throws IndexOutOfBoundsException если индекс невалиден
     */
    private void rangeCheck(int index) {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    /**
     * Проверяет корректность индекса для операций добавления (add)
     * @param index проверяемый индекс
     * @throws IndexOutOfBoundsException если индекс невалиден
     */
    private void rangeCheckForAdd(int index) {
        if (index > size || index < 0) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }
}